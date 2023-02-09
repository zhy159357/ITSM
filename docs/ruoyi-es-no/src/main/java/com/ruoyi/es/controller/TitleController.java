package com.ruoyi.es.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxResult.Type;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.OgPostService;

/**
 * 监控事件单标题维护
 * @author jintong
 *
 */
@Controller
@RequestMapping("knowledge/title")
public class TitleController extends BaseController {

    private String prefix = "knowledge/title";
    
    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;
    
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
    	knowledgeTitleService.synchronizeMonitoringContent();//监控知识一级标题名称同步
        startPage();
        knowledgeTitle.setEventType(EventType.MONITORING.getCode());
        List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);      
        return getDataTable(list);
    }
    /*
     * 下拉框用查询
     */
    @PostMapping("/titleList")
    @ResponseBody
    public List<KnowledgeTitle> titleList(KnowledgeTitle knowledgeTitle) {
        List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);      
        return list;
    }
    /**
     * 查询父级标题下拉框
     */
    @PostMapping("/selectParentTitle")
    @ResponseBody
    public List<KnowledgeTitle> selectParentTitle(String category,String eventType) {
    	KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
    	knowledgeTitle.setCategory(category);
    	knowledgeTitle.setEventType(eventType);
        List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);      
//        for(KnowledgeTitle k : list){
//        	k.setIsUsed(knowledgeTitleService.isUsed(k.getId()));
//        }
        return list;
    }
    /**
     * 新增标题
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存标题
     */
    @Log(title = "标题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KnowledgeTitle knowledgeTitle) {
    	knowledgeTitle.setId(UUID.getUUIDStr());
    	knowledgeTitle.setEventType(EventType.MONITORING.getCode());
    	knowledgeTitle.setCreateId(ShiroUtils.getOgUser().getpId());
    	knowledgeTitle.setCreateTime(DateUtils.getTime());
    	if("1".equals(knowledgeTitle.getCategory()) && knowledgeTitleService.checkSysUnique(knowledgeTitle)){
			return new AjaxResult(Type.WARN,"该系统已在标题维护中存在");
		}
        return toAjax(knowledgeTitleService.insertKnowledgeTitle(knowledgeTitle));
    }
    
    /**
     * 修改标题
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
    	KnowledgeTitle knowledgeTitle =  knowledgeTitleService.selectKnowledgeTitleById(id);
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
    	knowledgeTitle.setEventType(EventType.MONITORING.getCode());
    	if("1".equals(knowledgeTitle.getCategory()) && knowledgeTitleService.checkSysUnique(knowledgeTitle)){
			return new AjaxResult(Type.WARN,"该系统已在标题维护中存在");
		}
        return toAjax( knowledgeTitleService.updateKnowledgeTitle(knowledgeTitle));
    }
    
    /**
     * 删除标题
     */
    @Log(title = "标题", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id")String id) {
        return toAjax( knowledgeTitleService.deleteKnowledgeTitleById(id));
    }
    
}
