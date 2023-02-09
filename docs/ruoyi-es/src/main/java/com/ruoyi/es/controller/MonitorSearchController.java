package com.ruoyi.es.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.es.domain.*;
import com.ruoyi.es.service.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务知识查询页面
 */
@Controller
@RequestMapping("knowledge/monitoringSearch")
public class MonitorSearchController extends BaseController {

    private String prefix = "knowledge/content";

    @Autowired
    KnowledgeContentService contentService;
    @Autowired
    KnowledgeBusinessContentService businessContentService;
    @Autowired
    KnowledgeCategoryService knowledgeCategoryService;
    @Autowired
    SearchService searchService;
    @Autowired
    KnowledgeTitleService knowledgeTitleService;
    

    @GetMapping()
    public String monitoring(ModelMap mmap) {
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        return prefix + "/search";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo monitoring(KnowledgeContent knowledgeContent) {
        startPage();
        knowledgeContent.setEventType(EventType.MONITORING.getCode());
        List<KnowledgeContent> list = contentService.monitoringSearchtList(knowledgeContent);
        for(KnowledgeContent kc : list){
        	if(StringUtils.isNotEmpty(kc.getContentId())){
        		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
        	}
        }
        return getDataTable(list);
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KnowledgeContent knowledgeContent) {
        String isCurrentPage = (String) knowledgeContent.getParams().get("currentPage");
        knowledgeContent.setCreateId(ShiroUtils.getOgUser().getpId());
        knowledgeContent.setEventType(EventType.MONITORING.getCode());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<KnowledgeContent> list = contentService.monitoringSearchtList(knowledgeContent);
            for(KnowledgeContent kc : list){
            	if(StringUtils.isNotEmpty(kc.getContentId())){
            		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
            	}
            }
            ExcelUtil<KnowledgeContent> util = new ExcelUtil<>(KnowledgeContent.class);
            return util.exportExcel(list, "监控知识查看");
        } else {
        	List<KnowledgeContent> list = contentService.monitoringSearchtList(knowledgeContent);
        	for(KnowledgeContent kc : list){
        		if(StringUtils.isNotEmpty(kc.getContentId())){
            		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
            	}
            }
            ExcelUtil<KnowledgeContent> util = new ExcelUtil<>(KnowledgeContent.class);
            return util.exportExcel(list, "监控知识查看");
        }
    }



}