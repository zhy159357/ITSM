package com.ruoyi.es.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.enums.MyThreeStatus;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeCategoryService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.es.service.SearchService;
import com.ruoyi.system.mapper.OgUserMapper;

/**
 * 业务知识查询页面
 */
@Controller
@RequestMapping("knowledge/businessSearch")
public class BusinessSearchController extends BaseController {

    private String prefix = "knowledge/business";

    @Autowired(required=false)
    KnowledgeContentService contentService;
    @Autowired(required=false)
    KnowledgeBusinessContentService businessContentService;
    @Autowired(required=false)
    KnowledgeCategoryService knowledgeCategoryService;
    @Autowired(required=false)
    SearchService searchService;
    @Autowired(required=false)
    KnowledgeTitleService knowledgeTitleService;
    @Autowired
    private OgUserMapper ogUserMapper;

    @GetMapping()
    public String business(ModelMap mmap) {
        List categoryList = knowledgeCategoryService.getCateList();
        mmap.addAttribute("categoryList", categoryList);
        List statusList = MyThreeStatus.getList();
        mmap.addAttribute("statusList", statusList);
        return prefix + "/search";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo businesslist(KnowledgeBusinessContent businessContent) {
        startPage();
        businessContent.setEventType("1");
        List<KnowledgeBusinessContent> list = businessContentService.businessSearchtList(businessContent);
        return getDataTable(list);
    }


    /**
     * 详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();

        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        List categoryList = knowledgeCategoryService.getCateList();
        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        if("1".equals(eventType)){
            knowledgeTitle.setCategory("3");
            List<KnowledgeTitle> threeList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
            mmap.addAttribute("threeList", threeList);
        }
        List eventList = EventType.getList();

        String status = businessContentService.getStatusByUserId(knowledgeContent.getCreateId());
        OgUser user = new OgUser();
        List<OgUser> users = new ArrayList<>();
        user.setPostId(Long.valueOf(PostConstants.TWOADMIN));
        List<OgUser> users1 = ogUserMapper.selectAllocatedListPost(user);
        users.addAll(users1);
        user.setPostId(Long.valueOf(PostConstants.ONEADMIN));
        List<OgUser> users2 = ogUserMapper.selectAllocatedListPost(user);
        users.addAll(users2);

        mmap.addAttribute("users", users);
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("eventList", eventList);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.put("cont", knowledgeContent);
        return prefix + "/detail";
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setEventType("1");
        List<Ztree> ztrees = contentService.selectCategoryTree(knowledgeCategory);
        return ztrees;
    }

}