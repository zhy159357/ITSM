package com.ruoyi.activiti.modeler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;

@Controller
public class ModelerController extends BaseController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
    private static final String PREFIX = "modeler";

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/modeler/modelList")
    public String modelList(HttpServletRequest request) {
        return PREFIX + "/modelList";
    }

    @PostMapping("/modeler/list")
    @ResponseBody
    public TableDataInfo list(ModelEntityImpl modelEntity) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        modelQuery.orderByLastUpdateTime().desc();

        // ????????????
        if (com.ruoyi.common.utils.StringUtils.isNotBlank(modelEntity.getKey())) {
            modelQuery.modelKey(modelEntity.getKey());
        }
        if (com.ruoyi.common.utils.StringUtils.isNotBlank(modelEntity.getName())) {
            modelQuery.modelNameLike("%" + modelEntity.getName() + "%");
        }

        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        List<Model> resultList = modelQuery.listPage((pageNum - 1) * pageSize, pageSize);

        Page<Model> list = new Page<>();
        list.addAll(resultList);

        list.setTotal(modelQuery.count());
        list.setPageNum(pageNum);
        list.setPageSize(pageSize);

        return getDataTable(list);
    }

    @GetMapping("/modeler/addModal")
    public String addModal() {
        return PREFIX + "/modelModal";
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/modeler/create")
    @ResponseBody
    public AjaxResult create(@RequestParam("name") String name, @RequestParam("key") String key,
                             @RequestParam(value = "description", required = false) String description) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(MODEL_DESCRIPTION, description);

            Model newModel = repositoryService.newModel();
            newModel.setMetaInfo(modelObjectNode.toString());
            newModel.setName(name);
            newModel.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(newModel);
            repositoryService.addModelEditorSource(newModel.getId(), editorNode.toString().getBytes("utf-8"));

            return new AjaxResult(AjaxResult.Type.SUCCESS, "??????????????????", newModel.getId());
        } catch (Exception e) {
            logger.error("?????????????????????", e);
        }
        return error();
    }

    /**
     * ??????Model????????????
     */
    @RequestMapping(value = "/modeler/deploy/{modelId}")
    @ResponseBody
    public AjaxResult deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
            LOGGER.info("?????????????????????ID=" + deployment.getId());
            return success("????????????");
        } catch (Exception e) {
            LOGGER.error("?????????????????????????????????modelId={}", modelId, e);

        }
        return error("????????????");
    }

    /**
     * ??????model???xml??????
     */
    @RequestMapping(value = "/modeler/export/{modelId}")
    public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // ??????????????????
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

                ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
                String filename = bpmnModel.getMainProcess().getId() + ".bpmn";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            } else {
                try {
                    response.sendRedirect("/modeler/modelList");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception e) {
            LOGGER.error("??????model???xml???????????????modelId={}", modelId, e);
            try {
                response.sendRedirect("/modeler/modelList");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Log(title = "????????????", businessType = BusinessType.DELETE)
    @PostMapping("/modeler/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            repositoryService.deleteModel(ids);
            return toAjax(true);
        }
        catch (Exception e) {
            return error(e.getMessage());
        }
    }

}
