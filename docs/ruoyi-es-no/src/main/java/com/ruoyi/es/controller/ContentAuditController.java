package com.ruoyi.es.controller;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeCategoryService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("knowledge/audit")
public class ContentAuditController extends BaseController {

    private String prefix = "knowledge/content";

    @Autowired(required=false)
    KnowledgeContentService contentService;
    @Autowired(required=false)
    KnowledgeCategoryService knowledgeCategoryService;
    @Autowired(required=false)
    KnowledgeTitleService knowledgeTitleService;
    @Autowired(required=false)
    KnowledgeBusinessContentService businessContentService;
    @Autowired
    ISysUserService sysUserService;
    
    @GetMapping()
    public String category(ModelMap mmap) {
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("status", status);
        return prefix + "/audit";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeContent knowledgeContent) {
        startPage();
        knowledgeContent.setEventType(EventType.MONITORING.getCode());
        List<KnowledgeContent> list = contentService.ContentListAud(knowledgeContent);
        for(KnowledgeContent kc : list){
        	if(StringUtils.isNotEmpty(kc.getContentId())){
        		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
        	}
        }
        return getDataTable(list);
    }

    /*
     * ????????????????????????
     */
    @GetMapping("/audit/{id}")
    public String audit(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getTitleList("1"));   
        
        return prefix + "/auditing";
    }
    
    /**
     * ??????????????????????????????
     */
    @PostMapping("/auditPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult auditPass(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.auditPass(knowledgeContent));
    }
    
    /**
     * ??????????????????????????????
     */
    @PostMapping("/auditRefuse")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult auditRefuse(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.auditRefuse(knowledgeContent));
    }
    
    /**
     * ??????????????????????????????
     */
    @PostMapping("/backPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult backPass(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.backPass(knowledgeContent));
    }
    
    /**
     * ??????????????????????????????
     */
    @PostMapping("/backRefuse")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult backRefuse(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.backRefuse(knowledgeContent));
    }
    
    /**
     * ??????????????????????????????
     */
    @PostMapping("/trashPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult trashPass(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.trashPass(knowledgeContent));
    }
    
    /**
     * ??????????????????????????????
     */
    @PostMapping("/trashRefuse")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult trashRefuse(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.trashRefuse(knowledgeContent));
    }
    
    /**
     * ????????????????????????
     */
    @GetMapping("/forceBack/{id}")
    public String forceBack(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getTitleList("1"));     
        mmap.addAttribute("operate", PostConstants.OPERATION_EIGHTEEN);
        return prefix + "/force";
    }
    
    /**
     * ????????????
     */
    @PostMapping("/forceBack")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult forceBack(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.forceBack(knowledgeContent));
    }
    
    /**
     * ????????????????????????
     */
    @GetMapping("/forceTrash/{id}")
    public String forceTrash(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getTitleList("1"));     
        mmap.addAttribute("create_id", ShiroUtils.getOgUser().getLoginName());
        mmap.addAttribute("operate", PostConstants.OPERATION_NINETEEN);
        return prefix + "/force";
    }
    
    /**
     * ????????????
     */
    @PostMapping("/forceTrash")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult forceTrash(@Validated KnowledgeContent knowledgeContent)
    {
    	return toAjax(contentService.forceTrash(knowledgeContent));
    }
    
    //???????????????????????????
    private List<KnowledgeTitle> getTitleList(String category){
    	KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
    	knowledgeTitle.setEventType(EventType.MONITORING.getCode());
    	knowledgeTitle.setCategory(category);
    	return knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
    }
    
  //????????????????????????
    private List<OgUser> getAuditors(String status){
    	OgUser user = new OgUser();
    	user.setPostId(Long.valueOf(PostConstants.ONEADMIN));
    	List<OgUser> list1 = sysUserService.selectAllocatedListPost(user);
    	
    	user.setPostId(Long.valueOf(PostConstants.TWOADMIN));
    	List<OgUser> list2 = sysUserService.selectAllocatedListPost(user);
    	for(int i=0; i<list2.size(); i++){
    		for(int j=0; j<list1.size(); j++){
    			if(list1.get(j).getUserId().equals(list2.get(i).getUserId())){
    				list2.remove(i);
    				break;
    			}
    		}
    	}
    	
        if("3".equals(status)){//?????????????????????
            //user.setPostId(Long.valueOf(PostConstants.TWOADMIN));//?????????????????????
        	return list2;
        }else if("4".equals(status)){//????????????????????????
            //user.setPostId(Long.valueOf(PostConstants.ONEADMIN));//?????????????????????
        	return list1;
        }
        return new ArrayList<>();
    }
}
