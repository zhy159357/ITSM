package com.ruoyi.es.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeVisits;
import com.ruoyi.es.service.KnowledgeCollectionService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.es.service.KnowledgeVisitsService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 类别Controller
 * @author dayong_sun
 * @date 2021-04-09
 */
@Controller
@RequestMapping("knowledge/collection")
public class CollectionController extends BaseController {
    private String prefix = "knowledge/collection";

    @Autowired(required=false)
    private KnowledgeCollectionService collectionService;
    
    @Autowired(required=false)
    private KnowledgeVisitsService knowledgeVisitsService;

    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;
    
    @GetMapping()
    public String collection() {
        return prefix + "/collection";
    }

    /**
     * 查询收藏列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeCollect knowledgeCollect) {
        startPage();
        knowledgeCollect.setStatus("0");
        List<KnowledgeCollect> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(knowledgeCollect.getKnowledgeContent().getEventType())){
        	list = collectionService.selectKnowledgeCollectionList(knowledgeCollect);
        }
        
        for(KnowledgeCollect kcl : list){
        	KnowledgeContent kc = kcl.getKnowledgeContent();
        	if(kc.getEventType().equals(EventType.MONITORING.getCode()) && StringUtils.isNotEmpty(kc.getContentId())){
        		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
        	}
        }
        
        return getDataTable(list);
    }

    @GetMapping("/recommend")
    public String recommend() {
        return prefix + "/recommend";
    }

    /**
     * 查询推荐列表
     */
    @PostMapping("/recommend")
    @ResponseBody
    public TableDataInfo recommend(KnowledgeCollect knowledgeCollect) {
        startPage();
        //knowledgeCollect.setStatus("1");
       // List<KnowledgeCollect> list = collectionService.selectKnowledgeCollectionList(knowledgeCollect);
        List<KnowledgeVisits> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(knowledgeCollect.getKnowledgeContent().getEventType())){
        	list = knowledgeVisitsService.selectTopVisitsByUserId(ShiroUtils.getOgUser().getpId(),10,knowledgeCollect);
        }
        return getDataTable(list);
    }

    /**
     * 删除收藏
     */
    @Log(title = "收藏", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax( collectionService.deleteKnowledgeCollectionByIds(ids));
    }
    
    /**
     * 收藏
     */
    @Log(title = "收藏", businessType = BusinessType.INSERT)
    @PostMapping("/save/{id}")
    @ResponseBody
    public AjaxResult save(@PathVariable("id")String id) {
    	KnowledgeCollect knowledgeCollect = new KnowledgeCollect();
    	knowledgeCollect.setId(UUID.getUUIDStr());
    	knowledgeCollect.setCreateId(ShiroUtils.getOgUser().getpId());
    	knowledgeCollect.setContentId(id);
    	knowledgeCollect.setStatus("0");
        knowledgeCollect.setCreateTime(DateUtils.getDate());
        return toAjax( collectionService.insertKnowledgeCollection(knowledgeCollect));
    }
    
    

}
