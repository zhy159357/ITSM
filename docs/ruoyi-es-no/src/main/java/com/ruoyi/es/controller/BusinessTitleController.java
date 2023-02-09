package com.ruoyi.es.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxResult.Type;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务事件单标题维护
 *
 * @author jintong
 */
@Controller
@RequestMapping("knowledge/businesstitle")
public class BusinessTitleController extends BaseController {

    private String prefix = "knowledge/businesstitle";

    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;
    @Autowired
    private ISysWorkService iSysWorkService;

    @GetMapping()
    public String title() {
        return prefix + "/title";
    }

    /**
     * 查询标题列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeTitle knowledgeTitle) {
        if (StringUtils.isNotEmpty(knowledgeTitle.getParentId())) {
            KnowledgeTitle parent = knowledgeTitleService.selectKnowledgeTitleById(knowledgeTitle.getParentId());
            KnowledgeTitle kdt = new KnowledgeTitle();
            /*kdt.setEventType(EventType.BUSINESS.getCode());
            kdt.setSysId(parent.getSysId());
            kdt.setName(knowledgeTitle.getName());
            kdt.setCategory(knowledgeTitle.getCategory());
            kdt.setParentId(null);//重置父id
            kdt.setCreateId(parent.getId());//假传选中的系统id*/
            kdt.setSysId(parent.getSysId());
            kdt.setParentId(knowledgeTitle.getParentId());
            kdt.setStatus(knowledgeTitle.getStatus());
            kdt.setEventType("1");
            List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(kdt);
            return getDataTable_ideal(list);
        } else {
            throw new BusinessException("请选则左侧系统后再点击查询按钮。");
        }
    }


    /**
     * 新增标题
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap mmap) {

        KnowledgeTitle knowledgeTitle = knowledgeTitleService.selectKnowledgeTitleById(id);

        if ("4".equals(knowledgeTitle.getCategory())) {
            throw new BusinessException("关键字为最后一级，无法继续向下添加。");
        }
        mmap.put("parentName", knowledgeTitle.getName());
        mmap.put("parentId", knowledgeTitle.getId());
        mmap.put("knowledgeTitle", knowledgeTitle);
        return prefix + "/add";
    }

    /**
     * 新增标题
     */
    @GetMapping("/addSys")
    public String addSys(ModelMap mmap) {
        return prefix + "/addSys";
    }

    /**
     * 新增保存标题
     */
    @Log(title = "标题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KnowledgeTitle knowledgeTitle) {
        knowledgeTitle.setId(UUID.getUUIDStr());
        knowledgeTitle.setEventType(EventType.BUSINESS.getCode());
        knowledgeTitle.setCreateId(ShiroUtils.getOgUser().getpId());
        knowledgeTitle.setCreateTime(DateUtils.getTime());
        knowledgeTitle.setStatus("0");
        if ("0".equals(knowledgeTitle.getCategory())) {//如果为添加系统判断是否已经添加过该系统
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setSysId(knowledgeTitle.getSysId());
            kdt.setCategory("0");
            List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(kdt);
            if (!list.isEmpty()) {
                throw new BusinessException("该系统已在事件分类中存在。");
            }
        }
        return toAjax(knowledgeTitleService.insertKnowledgeTitle(knowledgeTitle));
    }

    /**
     * 修改标题
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeTitle knowledgeTitle = knowledgeTitleService.selectKnowledgeTitleById(id);
        mmap.put("parentName", knowledgeTitle.getParentName());
        mmap.put("parentId", knowledgeTitle.getParentId());
        mmap.put("knowledgeTitle", knowledgeTitle);
        return prefix + "/edit";
    }

    /**
     * 修改保存标题
     */
    @Log(title = "类别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KnowledgeTitle knowledgeTitle) {
        knowledgeTitle.setEventType(EventType.BUSINESS.getCode());
        int res = 0;
        if ("0".equals(knowledgeTitle.getCategory())) {//修改绑定的系统
            if (knowledgeTitleService.checkSysUnique(knowledgeTitle)) {
                return new AjaxResult(Type.WARN, "该系统已在标题维护中存在");
            }
            res = knowledgeTitleService.updateKnowledgeTitleSys(knowledgeTitle);
        }
        return toAjax(knowledgeTitleService.updateKnowledgeTitle(knowledgeTitle) + res);
    }

    /**
     * 删除标题
     */
    @Log(title = "标题", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") String id) {
        return toAjax(knowledgeTitleService.deleteKnowledgeTitleById(id));
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(KnowledgeTitle knowledgeTitle) {
        String userId = ShiroUtils.getOgUser().getUserId();
        List<OgGroup> groupList = iSysWorkService.selectLoginGroups(userId);//获取当前登陆人拥有哪些工作组
        List<Ztree> ztrees = new ArrayList<>();
        List<String> sysId = new ArrayList<>();
        if (!groupList.isEmpty()) {
            for (OgGroup group : groupList) {
                String str[] = group.getSysId().split(",");
                for (String s : str) {
                    sysId.add(s);
                }
            }
            knowledgeTitle.getParams().put("sysId", sysId);
            knowledgeTitle.setEventType("1");
            ztrees = knowledgeTitleService.selectTitleTree(knowledgeTitle);
        }
        return ztrees;
    }
}
