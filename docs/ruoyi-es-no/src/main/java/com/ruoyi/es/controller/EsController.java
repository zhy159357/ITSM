package com.ruoyi.es.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.*;
import com.ruoyi.es.service.EsService;
import com.ruoyi.es.service.KnowledgeCollectionService;
import com.ruoyi.es.service.KnowledgeCommentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.es.service.KnowledgeVisitsService;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.es.service.SearchService;
import com.ruoyi.system.service.ISysApplicationManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author dayong_sun
 * @version 1.0
 */
@Controller
@RequestMapping("es")
public class EsController extends BaseController {

    private String prefix = "es";

    @Autowired(required=false)
    SearchService searchService;
    
    @Autowired(required=false)
    KnowledgeCommentService knowledgeCommentService;
    
    @Autowired(required=false)
    KnowledgeVisitsService knowledgeVisitsService;
    
    @Autowired(required=false)
    KnowledgeCollectionService knowledgeCollectionService;
    
    @Autowired(required=false)
    KnowledgeTitleService knowledgeTitleService;
    
    @Autowired(required=false)
    EsService esService;
    
    @Autowired
    ISysApplicationManagerService sysApplicationManagerService;

    @GetMapping("/search")
    public String search(){
        return prefix +"/search";
    }


    /**
     * 查询检索的数据
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo search(@RequestParam Map<String,String> searchMap){
//        startPage();
        List<KnowledgeContent> list = searchService.search(searchMap);
        for(KnowledgeContent kc : list){
        	if(kc.getEventType().equals(EventType.MONITORING.getCode()) && StringUtils.isNotEmpty(kc.getContentId())){
        		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
        	}
        }
        return getDataTable_ideal(list);
    }

    /**
     * 表格回传父窗体
     */
    @GetMapping("/content")
    public String content()
    {
        return prefix + "/content";
    }
    /**
     * 表格回传父窗体
     */
    @PostMapping("/content")
    @ResponseBody
    public TableDataInfo contentList(KnowledgeSearch knowledgeSearch)
    {
        startPage();
        List<KnowledgeSearch> knowledgeSearches = searchService.selectContentList(knowledgeSearch);
        return getDataTable(knowledgeSearches);
    }

    @GetMapping("/selectAll")
    public String list(){
        return prefix +"/selectAll";
    }

    @PostMapping("/selectAll")
    @ResponseBody
    public TableDataInfo selectAll(KnowledgeSearch knowledgeSearch){
        startPage();
        List<KnowledgeSearch> knowledgeSearches = searchService.selectSearchList(knowledgeSearch);
        return getDataTable(knowledgeSearches);
    }

//    @GetMapping("/getMess/{id}")
//    @ResponseBody
//    public List<KnowledgeSearch> getMess(@PathVariable("id") String id){
//        KnowledgeSearch knowledgeSearch = new KnowledgeSearch();
//        knowledgeSearch.setId(id);
//        List<KnowledgeSearch> knowledgeSearches = searchService.selectSearchList(knowledgeSearch);
//        return knowledgeSearches;
//    }

    @RequestMapping("/add")
    public String add(Model view){
        return "/es/add";
    }

    @PostMapping("/addSave")
    @ResponseBody
    public AjaxResult addSave(KnowledgeSearch knowledgeSearch){
        return toAjax(searchService.save(knowledgeSearch));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        mmap.put("knowledgeSearch", searchService.selectSearchById(id));
        return prefix +"/edit";
    }

    @PostMapping("/editSave")
    @ResponseBody
    public AjaxResult editSave(KnowledgeSearch knowledgeSearch){
        return toAjax(searchService.updateById(knowledgeSearch));
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult deleteById(String ids){
        return toAjax(searchService.deleteById(ids));
    }
    
    /**
     * 知识检索详情页
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap)
    {
    	String userid = ShiroUtils.getOgUser().getpId();
    	KnowledgeContent knowledgeContent = searchService.selectContentById(id);
    	mmap.put("knowledgeComment", knowledgeCommentService.selectCommentByContentId(id));
    	mmap.put("collectFlag", knowledgeCollectionService.isCollected(userid, id));
    	mmap.put("commentCount", knowledgeCommentService.selectCommentCountByContentId(id, null));
    	mmap.put("commentGoodCount", knowledgeCommentService.selectCommentCountByContentId(id, "0"));
    	mmap.put("commentBadCount", knowledgeCommentService.selectCommentCountByContentId(id, "1"));
        mmap.put("knowledgeContent", knowledgeContent);
        String sectitle = knowledgeContent.getSectitle();
        if(!StringUtil.isEmpty(sectitle)){
        	KnowledgeTitle sectitleName = knowledgeTitleService.selectKnowledgeTitleById(sectitle);
        	knowledgeContent.setSectitle(sectitleName.getName());
        }
        String content = knowledgeContent.getContent();
        if(!StringUtil.isEmpty(content)){
        	KnowledgeTitle contentName = knowledgeTitleService.selectKnowledgeTitleById(content);
        	if(knowledgeContent.getEventType().equals(EventType.MONITORING.getCode())){
        		knowledgeContent.setContent(contentName.getSysName());
        	}else if(knowledgeContent.getEventType().equals(EventType.BUSINESS.getCode())){
        		knowledgeContent.setContent(contentName.getName());
        	}   	
        }
        String threetitle = knowledgeContent.getThreetitle();
        if(!StringUtil.isEmpty(threetitle)){
        	KnowledgeTitle threetitleName = knowledgeTitleService.selectKnowledgeTitleById(threetitle);
        	knowledgeContent.setThreetitle(threetitleName.getName());
        }
        String sysId = knowledgeContent.getSysId();
        if(!StringUtil.isEmpty(sysId)){
        	OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(sysId);
        	knowledgeContent.setSystemName(ogSys.getCaption());
        }
        KnowledgeVisits knowledgeVisits = new KnowledgeVisits();
        knowledgeVisits.setId(UUID.getUUIDStr());
        knowledgeVisits.setCreateId(userid);
        knowledgeVisits.setCreateTime(DateUtils.getTime());
        knowledgeVisits.setContentId(id);
        knowledgeVisitsService.insertKnowledgeVisits(knowledgeVisits);
    	
        return prefix +"/detail";
    }
    
    //
    @GetMapping("/comment/{id}")
    public String comment(@PathVariable("id") String id, ModelMap mmap)
    {
    	KnowledgeComment knowledgeComment = new KnowledgeComment();
    	knowledgeComment.setContentId(id);
    	mmap.put("knowledgeComment", knowledgeComment);
        return prefix +"/comment";
    }
    @PostMapping("/comment")
    @ResponseBody
    public AjaxResult commentSave(KnowledgeComment knowledgeComment){
    	knowledgeComment.setId(UUID.getUUIDStr());
    	knowledgeComment.setCreateId(ShiroUtils.getOgUser().getpId());
    	knowledgeComment.setCreateTime(DateUtils.getTime());
        return toAjax(knowledgeCommentService.insertKnowledgeComment(knowledgeComment));
    }

    @GetMapping("/reply/{id}&{contentId}")
    public String reply(@PathVariable("id") String id,@PathVariable("contentId") String contentId, ModelMap mmap)
    {
    	KnowledgeComment knowledgeComment = new KnowledgeComment();
    	knowledgeComment.setReplyId(id);
    	knowledgeComment.setContentId(contentId);
    	mmap.put("knowledgeComment", knowledgeComment);
        return prefix +"/reply";
    }
   
    /**
     * 根据事件类别查询
     */
    @PostMapping("/selectEvent")
    @ResponseBody
    public Map<String,Object> selectEvent(@Validated String eventType) {
        Map<String,Object> map = new HashMap<>();
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("1");
        knowledgeTitle.setEventType(eventType);
        List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        map.put("titleList1",list);
        knowledgeTitle.setCategory("2");
        list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        map.put("titleList2",list);
        if("1".equals(eventType)){
            knowledgeTitle.setCategory("3");
            list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
            map.put("titleList3",list);
            knowledgeTitle.setCategory("0");
            list = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
            map.put("systemNameList",list);
        }
        return map;
    }
}
