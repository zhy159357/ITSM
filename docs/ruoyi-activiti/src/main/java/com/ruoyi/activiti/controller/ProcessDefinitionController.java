package com.ruoyi.activiti.controller;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.ruoyi.activiti.config.ICustomProcessDiagramGenerator;
import com.ruoyi.activiti.config.WorkflowConstants;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.activiti.domain.ProcessDefinition;
import com.ruoyi.activiti.service.ProcessDefinitionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;

@Controller
@RequestMapping("/definition")
public class ProcessDefinitionController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

	private String prefix = "definition";

	@Autowired
	private ProcessDefinitionService processDefinitionService;
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ProcessEngine processEngine;


	@Autowired
	private RuntimeService runtimeService;

	@GetMapping
	public String processDefinition() {
		return prefix + "/definition";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProcessDefinition processDefinition) {
		List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(processDefinition);
		return getDataTable(list);
	}

	/**
	 * ??????????????????
	 */
	@Log(title = "????????????", businessType = BusinessType.INSERT)
	@PostMapping("/upload")
	@ResponseBody
	public AjaxResult upload(@RequestParam("processDefinition") MultipartFile file) {
		try {
			if (!file.isEmpty()) {
				String extensionName = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf('.') + 1);
				if (!"bpmn".equalsIgnoreCase(extensionName) && !"zip".equalsIgnoreCase(extensionName)
						&& !"bar".equalsIgnoreCase(extensionName)) {
					return error("??????????????????????????? bpmn, zip ??? bar ?????????");
				}
				// p.s. ?????? FileUploadUtils.upload() ??????????????? fileName ?????????
				// Constants.RESOURCE_PREFIX????????????
				// ??????: FileUploadUtils.getPathFileName(...)
				String fileName = FileUploadUtils.upload(RuoYiConfig.getProfile() + "/processDefiniton", file);
				if (StringUtils.isNotBlank(fileName)) {
					/*String realFilePath = RuoYiConfig.getProfile()
							+ fileName.substring(Constants.RESOURCE_PREFIX.length());*/
					processDefinitionService.deployProcessDefinition(fileName);
					return success();
				}
			}
			return error("???????????????????????????");
		} catch (Exception e) {
			log.error("?????????????????????????????????", e);
			return error(e.getMessage());
		}
	}

	@Log(title = "????????????", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		try {
			return toAjax(processDefinitionService.deleteProcessDeploymentByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	@Log(title = "????????????", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export() {
		List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(new ProcessDefinition());
		ExcelUtil<ProcessDefinition> util = new ExcelUtil<>(ProcessDefinition.class);
		return util.exportExcel(list, "??????????????????");
	}

	@PostMapping("/suspendOrActiveApply")
	@ResponseBody
	public AjaxResult suspendOrActiveApply(String id, String suspendState) {
		processDefinitionService.suspendOrActiveApply(id, suspendState);
		return success();
	}

	/**
	 * ??????????????????
	 *
	 * @param processDefinitionId ????????????ID
	 * @param resourceName        ????????????
	 */
	@RequestMapping(value = "/readResource")
	public void readResource(@RequestParam("pdid") String processDefinitionId,
			@RequestParam("resourceName") String resourceName, HttpServletResponse response) throws Exception {
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId)
				.singleResult();

		// ??????????????????
		InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

		// ?????????????????????????????????
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param processDefinitionId
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@PostMapping(value = "/convert2Model")
	@ResponseBody
	public AjaxResult convertToModel(@Param("processDefinitionId") String processDefinitionId)
			throws UnsupportedEncodingException, XMLStreamException {
		org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				processDefinition.getResourceName());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);
		Model modelData = repositoryService.newModel();
		modelData.setKey(processDefinition.getKey());
		modelData.setName(processDefinition.getResourceName());
		modelData.setCategory(processDefinition.getDeploymentId());

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);

		repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

		return success();
	}
	@RequestMapping(value = "/read-resource")
	public void readResource(String pProcessInstanceId, HttpServletResponse response) throws Exception {
		// ?????????????????????
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		String processDefinitionId = "";
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(pProcessInstanceId).singleResult();
		if (processInstance == null) {
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pProcessInstanceId).singleResult();
			processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		} else {
			processDefinitionId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

		String resourceName = pd.getDiagramResourceName();

		if (resourceName.endsWith(".png") && StringUtils.isEmpty(pProcessInstanceId) == false) {
			getActivitiProccessImage(pProcessInstanceId, response);
			// ProcessDiagramGenerator.generateDiagram(pde, "png",
			// getRuntimeService().getActiveActivityIds(processInstanceId));
		} else {
			// ??????????????????
			InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

			// ?????????????????????????????????
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		}
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 */
	public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
		// logger.info("[??????]-?????????????????????");
		try {
			// ????????????????????????
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pProcessInstanceId).singleResult();

			if (historicProcessInstance == null) {
				// throw new BusinessException("??????????????????ID[" + pProcessInstanceId +
				// "]????????????????????????????????????");
			} else {
				// ??????????????????
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

				// ??????????????????????????????????????????????????????????????????????????????????????????
				List<HistoricActivityInstance> historicActivityInstanceList = historyService
						.createHistoricActivityInstanceQuery().processInstanceId(pProcessInstanceId)
						.orderByHistoricActivityInstanceId().asc().list();

				// ??????????????????ID??????
				List<String> executedActivityIdList = new ArrayList<String>();
				int index = 1;
				// logger.info("???????????????????????????ID");
				for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
					executedActivityIdList.add(activityInstance.getActivityId());

					// logger.info("???[" + index + "]??????????????????=" + activityInstance.getActivityId() + "
					// : " +activityInstance.getActivityName());
					index++;
				}

				BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

				// ?????????????????????
				List<String> flowIds = new ArrayList<String>();
				// ???????????????????????? (getHighLightedFlows??????????????????)
				flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);

//                // ??????????????????????????????
//                ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//                //????????????
//                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,"??????","????????????","??????",null,2.0);

				Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(pProcessInstanceId).list()
						.stream().map(e -> e.getActivityId()).collect(Collectors.toSet());

				ICustomProcessDiagramGenerator diagramGenerator = (ICustomProcessDiagramGenerator) processEngine
						.getProcessEngineConfiguration().getProcessDiagramGenerator();
				InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
						flowIds, "??????", "??????", "??????", null, 1.0,
						new Color[] { WorkflowConstants.COLOR_NORMAL, WorkflowConstants.COLOR_CURRENT }, currIds);

				response.setContentType("image/png");
				OutputStream os = response.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				imageStream.close();
			}
			// logger.info("[??????]-?????????????????????");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// logger.error("????????????-????????????????????????" + e.getMessage());
			// throw new BusinessException("????????????????????????" + e.getMessage());
		}
	}

	private List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
											 List<HistoricActivityInstance> historicActivityInstances) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24?????????
		List<String> highFlows = new ArrayList<String>();// ????????????????????????flowId

		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
			// ?????????????????????????????????
			// ?????????????????????????????????
			FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess()
					.getFlowElement(historicActivityInstances.get(i).getActivityId());

			List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// ?????????????????????????????????????????????
			FlowNode sameActivityImpl1 = null;

			HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// ???????????????
			HistoricActivityInstance activityImp2_;

			for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
				activityImp2_ = historicActivityInstances.get(k);// ?????????1?????????

				if (activityImpl_.getActivityType().equals("userTask")
						&& activityImp2_.getActivityType().equals("userTask")
						&& df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) // ??????usertask???????????????????????????????????????????????????????????????????????????????????????
				{

				} else {
					sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(historicActivityInstances.get(k).getActivityId());// ????????????????????????????????????
					break;
				}

			}
			sameStartTimeNodes.add(sameActivityImpl1); // ????????????????????????????????????????????????????????????
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// ?????????????????????
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// ?????????????????????

				if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// ???????????????????????????????????????????????????????????????
					FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// ????????????????????????
					break;
				}
			}
			List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // ?????????????????????????????????

			for (SequenceFlow pvmTransition : pvmTransitions) {// ???????????????????????????
				FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess()
						.getFlowElement(pvmTransition.getTargetRef());// ?????????????????????????????????????????????????????????????????????????????????id?????????????????????
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return highFlows;

	}
}
