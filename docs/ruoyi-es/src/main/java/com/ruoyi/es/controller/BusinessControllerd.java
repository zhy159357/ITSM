package com.ruoyi.es.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeBusinessExample;
import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeSearch;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeCategoryService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeOperationHistoryService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.es.service.SearchService;
import com.ruoyi.es.thread.InsertDescribeThread;
import com.ruoyi.system.mapper.OgUserMapper;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.OgPostService;

/**
 * 业务知识管理页面
 */
@Controller
@RequestMapping("knowledge/business")
public class BusinessControllerd extends BaseController {

    private String prefix = "knowledge/business";

    @Autowired
    KnowledgeContentService contentService;
    @Autowired
    KnowledgeBusinessContentService businessContentService;
    @Autowired(required=false)
    KnowledgeCategoryService knowledgeCategoryService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    SearchService searchService;
    @Autowired(required=false)
    KnowledgeTitleService knowledgeTitleService;
    @Autowired
    private OgPostService ogPostService;
    @Autowired
    private OgUserMapper ogUserMapper;
    @Autowired(required=false)
    private KnowledgeOperationHistoryService operationHistoryService;

    @GetMapping()
    public String business(ModelMap mmap) {
        List categoryList = knowledgeCategoryService.getCateList();
        mmap.addAttribute("categoryList", categoryList);
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
        mmap.addAttribute("status", status);
        return prefix + "/cont1";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo businesslist(KnowledgeBusinessContent businessContent) {       
        businessContent.setEventType(EventType.BUSINESS.getCode());
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        
        if("3".equals(status)){//当前为二线厂商
        	Map<String,Object> params = new HashMap<>();
        	params.put("loginUser", ShiroUtils.getOgUser().getpId());
        	businessContent.setParams(params);
        }
        startPage();
        List<KnowledgeBusinessContent> list = businessContentService.businessContentList(businessContent);
        return getDataTable(list);
    }

    /**
     * 新增
     */
    @GetMapping("/add/{selecttreeId}")
    public String add(@PathVariable("selecttreeId") String selecttreeId, ModelMap mmap) {
        List categoryList = knowledgeCategoryService.getCateList();

        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(EventType.BUSINESS.getCode());
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleSysList(knowledgeTitle);
        //二级标题
        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        //一级标题
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        List eventList = EventType.getList();
        KnowledgeCategory folder = contentService.selectCateById(selecttreeId);
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String createName = userPerson.getpName();
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("categoryId", folder.getCategoryId());
        mmap.addAttribute("categoryName", folder.getCategoryName());
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("eventList", eventList);
        mmap.addAttribute("content", "自助银行系统");
        mmap.addAttribute("createName", createName);
        mmap.addAttribute("createTime", DateUtils.getTime());
        return prefix + "/add";
    }


    /**
     * 根据父id查询标题
     */
    @PostMapping("/selectTitle")
    @ResponseBody
    public List<KnowledgeTitle> selectTitle(@Validated String category,@Validated String parentId,@Validated String sysId) {
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory(category);
        knowledgeTitle.setParentId(parentId);
        knowledgeTitle.setSysId(sysId);
        knowledgeTitle.setEventType(EventType.BUSINESS.getCode());
        return knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
    }

    /**
     * 新增保存
     */
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(@Validated KnowledgeContent knowledgeContent, KnowledgeSearch knowledgeSearch) {
        String cont_id = knowledgeContent.getId();
        if (StringUtils.isEmpty(cont_id)) {
            knowledgeContent.setId(UUID.getUUIDStr());
            knowledgeContent.setStatus("1");
            knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
            businessContentService.insertContent(knowledgeContent);
        }
        return AjaxResult.success("操作成功", knowledgeContent.getId());
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
        mmap.addAttribute("users", getAuditors(status));
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
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        return prefix + "/detail";
    }

    //新增暂存
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveDesc(KnowledgeContent knowledgeContent) {
        String describe = knowledgeContent.getDescribes();
        knowledgeContent.setDescribes("");
        knowledgeContent.setStatus("0");
        knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
        if(StringUtils.isEmpty(knowledgeContent.getId())){
            knowledgeContent.setId(UUID.getUUIDStr());
            knowledgeContent.setEventType(EventType.BUSINESS.getCode());
            businessContentService.insertContent(knowledgeContent);
        } else {
        	businessContentService.updateCont(knowledgeContent);
        }
        InsertDescribeThread thread = new InsertDescribeThread();
        KnowledgeContent kc = new KnowledgeContent();
        kc.setId(knowledgeContent.getId());
        kc.setDescribes(describe);
        thread.setKnowledgeContent(kc);
        thread.start();
        return AjaxResult.success("操作成功", knowledgeContent.getId());
    }

    /**
     * 修改暂存
     */
    @PostMapping("/saveStatus")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveStatus(KnowledgeContent knowledgeContent) {
        knowledgeContent.setStatus("0");
        knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
        businessContentService.updateCont(knowledgeContent);
        return AjaxResult.success("操作成功", knowledgeContent.getId());
    }

    /**
     * 查询当前登录人是否是创建人
     */
    @PostMapping("/selectCreateIdById")
    @ResponseBody
    public int selectCreateIdById(@Validated String id) {
        return businessContentService.selectCreateIdById(id);
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id,ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();
        List categoryList = knowledgeCategoryService.getCateList();

        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleSysList(knowledgeTitle);
        //一级标题
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = new ArrayList<>();
        if(StringUtils.isNotEmpty(knowledgeContent.getSysId())){
        	KnowledgeTitle kt = new KnowledgeTitle();
        	kt.setSysId(knowledgeContent.getSysId());
        	kt.setCategory("1");
            kt.setEventType(EventType.BUSINESS.getCode());
            titleList1 = knowledgeTitleService.selectKnowledgeTitleList(kt);
        }
        //二级标题
        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList2 = new ArrayList<>();
        if(StringUtils.isNotEmpty(knowledgeContent.getContent())){
            knowledgeTitle.setParentId(knowledgeContent.getContent());
            titleList2 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        }
        //三级标题
        knowledgeTitle.setCategory("3");
        List<KnowledgeTitle> titleList3 = new ArrayList<>();
        if(StringUtils.isNotEmpty(knowledgeContent.getSectitle())){
            knowledgeTitle.setParentId(knowledgeContent.getSectitle());
            titleList3 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        }

        List eventList = EventType.getList();
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("titleList2", titleList2);
        mmap.addAttribute("titleList3", titleList3);
        mmap.addAttribute("eventList", eventList);
        mmap.put("cont", knowledgeContent);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(@Validated KnowledgeContent knowledgeContent) {
        knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
        knowledgeContent.setStatus("1");
        return toAjax(businessContentService.updateCont(knowledgeContent));
    }

    /**
     * 修改保存
     * @param
     * @return
     */
    @PostMapping("/updateTree")
    @ResponseBody
    public AjaxResult updateTree(@Validated KnowledgeCategory knowledgeCategory) {
        return toAjax(knowledgeCategoryService.updateKnowledgeCategory(knowledgeCategory));
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(contentService.deleteContByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KnowledgeBusinessContent businessContent) {
        String isCurrentPage = (String) businessContent.getParams().get("currentPage");
        businessContent.setCreateId(ShiroUtils.getOgUser().getpId());
        businessContent.setEventType(EventType.BUSINESS.getCode());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<KnowledgeBusinessContent> list = businessContentService.businessContentList(businessContent);
            ExcelUtil<KnowledgeBusinessContent> util = new ExcelUtil<KnowledgeBusinessContent>(KnowledgeBusinessContent.class);
            return util.exportExcel(list, "业务知识查看");
        } else {
            List<KnowledgeBusinessContent> list = businessContentService.businessContentList(businessContent);
            ExcelUtil<KnowledgeBusinessContent> util = new ExcelUtil<KnowledgeBusinessContent>(KnowledgeBusinessContent.class);
            return util.exportExcel(list, "业务知识查看");
        }
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setEventType(EventType.BUSINESS.getCode());
        List<Ztree> ztrees = contentService.selectCategoryTree(knowledgeCategory);
        return ztrees;
    }

    //=======================左侧树操作------------------------------
    //新增
    @RequestMapping("/contAdd")
    public String add(HttpServletRequest req, ModelMap mmap) {
        String parentId = req.getParameter("treeName");
        String categoryId = req.getParameter("id");
        if (!"-1".equals(categoryId)) {
            mmap.put("Cate", contentService.selectCateById(categoryId));
        } else {
            mmap.put("Cate", new KnowledgeCategory());
        }
        mmap.put("parentId", parentId);
        mmap.put("categoryId", categoryId);
        return prefix + "/contTreeAdd";
    }

    /**
     * 左侧树新增保存
     */
    @PostMapping("/contTreeAdd")
    @ResponseBody
    public AjaxResult addSave(@Validated KnowledgeCategory knowledgeCategory) {

//        if ("".equals(knowledgeCategory.getCategoryId()) || "null".equals(knowledgeCategory.getCategoryId()) || null == knowledgeCategory.getCategoryId()) {
//            knowledgeCategory.setParentId(knowledgeCategory.getCategoryId());
//        }
        OgUser ogUser = ShiroUtils.getOgUser();
        knowledgeCategory.setCreateBy(ogUser.getUserId());
        knowledgeCategory.setEventType("1");
        return AjaxResult.success("操作成功", contentService.insertContTree(knowledgeCategory));
    }

    //  修改
    @RequestMapping("/contEdit")
    public String edit(HttpServletRequest req, ModelMap mmap) {
        String parentId = req.getParameter("treeName");
        String categoryId = req.getParameter("id");
        KnowledgeCategory knowledgeCategory = contentService.selectCateById(categoryId);
        String parent = null;
        if (knowledgeCategory.getParentId() == null || knowledgeCategory.getParentId() == "") {
            parent = "";
        } else {
            parent = contentService.selectParentName(knowledgeCategory.getParentId());
        }
        mmap.put("parentId", parent);
        mmap.put("id_", categoryId);
        mmap.put("Cate", knowledgeCategory);
        return prefix + "/contTree";
    }

    /**
     * 左侧树修改保存
     */
    @PostMapping("/contTreeEdit")
    @ResponseBody
    public AjaxResult editSave(@Validated KnowledgeCategory knowledgeCategory) {
        return AjaxResult.success("操作成功", contentService.saveContTree(knowledgeCategory));
    }

    /**
     * 删除树节点
     */
    @PostMapping("/contDel")
    @ResponseBody
    public AjaxResult deleteTree(String categoryId) {
        List<KnowledgeContent> list =contentService.countContentList(categoryId);
        if(list.size()>0){
            return error("先删除类别下知识库，再删除知识类别");
        }else{
            return toAjax(contentService.deleteCont(categoryId));
        }
    }

    @GetMapping("/category")
    public String category(ModelMap mmap) {
        List categoryList = knowledgeCategoryService.getCateList();
        mmap.addAttribute("categoryList",categoryList);
        return prefix + "/audit";
    }

    @PostMapping("/auditlist")
    @ResponseBody
    public TableDataInfo auditlist(KnowledgeBusinessContent businessContent) {
        startPage();
        businessContent.setEventType("1");
        List<KnowledgeBusinessContent> list = businessContentService.businessListAud(businessContent);
        return getDataTable(list);
    }

    /*
     * 审核
     */
    @GetMapping("/audit")
    public String audit(ModelMap mmap) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
        List categoryList = knowledgeCategoryService.getCateList();
        mmap.addAttribute("categoryList",categoryList);
        mmap.addAttribute("create_id",ShiroUtils.getOgUser().getUsername());
        mmap.addAttribute("status",status);
        return prefix + "/audit";
    }

    /*
     * 审核验证是否二级管理员
     */
    @PostMapping("/checkAudit")
    @ResponseBody
    public int checkAudit(@Validated String id) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String cstatus = businessContentService.getStatusByUserId(knowledgeContent.getCreateId());
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        int result = 0;
        if("4".equals(cstatus) && "4".equals(status)){
            result = 1;
        }
        return result;

    }
    /*
     * 审核按钮点击跳转
     */
    @GetMapping("/auditing/{id}")
    public String audit(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();
        List categoryList = knowledgeCategoryService.getCateList();
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("3");
        List<KnowledgeTitle> threeList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        mmap.addAttribute("threeList", threeList);

        List eventList = EventType.getList();
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("eventList", eventList);
        mmap.put("cont", knowledgeContent);
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String create_id = userPerson.getpName();
        mmap.addAttribute("create_id", create_id);
        mmap.addAttribute("status",status);
        return prefix + "/auditing";
    }

    /**
     *审核通过
     * @return
     */
    @PostMapping("/audit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult auditSave(@Validated KnowledgeContent knowledgeContent)
    {
        return toAjax(businessContentService.auditBusiness(knowledgeContent));
    }

    /**
     *审核不通过
     * @return
     */
    @PostMapping("/noPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult noPass(@Validated KnowledgeContent knowledgeContent)
    {
        String userId = ShiroUtils.getOgUser().getUserId();
        knowledgeContent.setCreateId(userId);
        String cstatus = businessContentService.getStatusByUserId(userId);
        String status = "3";//初审拒绝
        if("2".equals(cstatus)){
            //终审拒绝
            status = "13";
        }
        knowledgeContent.setStatus(status);
        operationHistoryService.insertOperationHistory(knowledgeContent);
        knowledgeContent.setStatus("0");
        return toAjax(businessContentService.auditContent(knowledgeContent));
    }

    /**
     * 批量下线
     */
    @PostMapping("/offline")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult removeOffline(String ids) {
        try {
            return toAjax(businessContentService.offlineBusinessByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /*
     * 打开申请修改-页面
     */
    //    @GetMapping("/apply/{id}/{type}")
    @GetMapping("/applyBack/{id}")
    public String audits(@PathVariable("id") String id,ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();
        List categoryList = knowledgeCategoryService.getCateList();
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("3");
        List<KnowledgeTitle> threeList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        List eventList = EventType.getList();
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String pname = userPerson.getpName();

        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("threeList", threeList);
        mmap.addAttribute("eventList", eventList);
        mmap.put("cont", knowledgeContent);
        mmap.addAttribute("pname", pname);
        mmap.addAttribute("operate", PostConstants.OPERATION_FOUR);
        mmap.addAttribute("operTime", DateUtils.getTime());
        return prefix + "/applying";
    }

    /**
     * 打开申请作废页面
     */
    @GetMapping("/applyTrash/{id}")
    public String applyTrash(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();
        List categoryList = knowledgeCategoryService.getCateList();
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("3");
        List<KnowledgeTitle> threeList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        List eventList = EventType.getList();
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String pname = userPerson.getpName();

        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("threeList", threeList);
        mmap.addAttribute("eventList", eventList);
        mmap.put("cont", knowledgeContent);
        mmap.addAttribute("pname", pname);
        mmap.addAttribute("operTime", DateUtils.getTime());
        mmap.addAttribute("operate", PostConstants.OPERATION_FIVE);
        return prefix + "/applying";
    }

    /**
     * 打开强制申请下线页面
     */
    @GetMapping("/forceBack/{id}")
    public String forceBack(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();
        List categoryList = knowledgeCategoryService.getCateList();
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("3");
        List<KnowledgeTitle> threeList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        List eventList = EventType.getList();
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String pname = userPerson.getpName();

        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("threeList", threeList);
        mmap.addAttribute("eventList", eventList);
        mmap.put("cont", knowledgeContent);
        mmap.addAttribute("pname", pname);
        mmap.addAttribute("operTime", DateUtils.getTime());
        mmap.addAttribute("operate", PostConstants.OPERATION_EIGHTEEN);
        return  prefix + "/force";
    }

    /**
     * 打开强制申请作废页面
     */
    @GetMapping("/forceTrash/{id}")
    public String forceTrash(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        String eventType = knowledgeContent.getEventType();
        List categoryList = knowledgeCategoryService.getCateList();
        KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
        knowledgeTitle.setCategory("0");
        knowledgeTitle.setEventType(eventType);
        //系统名称
        List<KnowledgeTitle> sysnameList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        knowledgeTitle.setCategory("2");
        List<KnowledgeTitle> titleList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("1");
        List<KnowledgeTitle> titleList1 = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
        knowledgeTitle.setCategory("3");
        List<KnowledgeTitle> threeList = knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);

        List eventList = EventType.getList();
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String pname = userPerson.getpName();

        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryList", categoryList);
        mmap.addAttribute("sysnameList", sysnameList);
        mmap.addAttribute("titleList", titleList);
        mmap.addAttribute("titleList1", titleList1);
        mmap.addAttribute("threeList", threeList);
        mmap.addAttribute("eventList", eventList);
        mmap.put("cont", knowledgeContent);
        mmap.addAttribute("pname", pname);
        mmap.addAttribute("operTime", DateUtils.getTime());
        mmap.addAttribute("operate", PostConstants.OPERATION_NINETEEN);
        return  prefix + "/force";
    }

    
    //获取审核人下拉框
    private List<OgUser> getAuditors(String status){
    	OgUser user = new OgUser();
    	user.setPostId(Long.valueOf(PostConstants.ONEADMIN));
    	List<OgUser> list1 = ogUserMapper.selectAllocatedListPost(user);
    	
    	user.setPostId(Long.valueOf(PostConstants.TWOADMIN));
    	List<OgUser> list2 = ogUserMapper.selectAllocatedListPost(user);
    	for(int i=0; i<list2.size(); i++){
    		for(int j=0; j<list1.size(); j++){
    			if(list1.get(j).getUserId().equals(list2.get(i).getUserId())){
    				list2.remove(i);
    				break;
    			}
    		}
    	}
    	
        if("3".equals(status)){//当前为二线厂商
            //user.setPostId(Long.valueOf(PostConstants.TWOADMIN));//获取二线管理员
        	return list2;
        }else if("4".equals(status)){//当前为二线管理员
            //user.setPostId(Long.valueOf(PostConstants.ONEADMIN));//获取一线管理员
        	return list1;
        }
        return new ArrayList<>();
    }
    

    /**
     * 跳转至告警分析页面
     * @return
     */
    @GetMapping("/businessAnalize")
    public String alarmAnalize(ModelMap mmap) {
        return prefix + "/businessAnalize";
    }

    /**
     * 业务分析
     */
    @PostMapping("/businessAnalizeList")
    @ResponseBody
    public TableDataInfo businessAnalizeList(KnowledgeBusinessContent businessContent) {
        startPage();
        List<KnowledgeBusinessContent> list = searchService.businessAnalizeList(businessContent);
        return getDataTable(list);
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<KnowledgeBusinessExample> util = new ExcelUtil<KnowledgeBusinessExample>(KnowledgeBusinessExample.class);
        List<KnowledgeBusinessExample> businessList = null;
        try{
            businessList = util.importExcel(file.getInputStream());
        }catch (Exception e){
            return AjaxResult.warn("导入数据有误！");
        }
        String message = businessContentService.importBusinessData(businessList);
        return AjaxResult.success(message);
    }
    
    /**
     * 跳转至模板定制页面
     * @return
     */
    @GetMapping("/template")
    public String template(ModelMap mmap) {
    	KnowledgeContent knowledgeContent = contentService.selectContById("TEMPLATE0002");
    	if(knowledgeContent == null){
    		knowledgeContent = new KnowledgeContent();
    		knowledgeContent.setDescribes("");
    	}        
    	mmap.put("cont", knowledgeContent);
        return prefix + "/template";
    }
    
    /**
     * 保存模板
     * @param knowledgeContent
     * @return
     */
    @PostMapping("/savetemplate")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult savetemplate(KnowledgeContent knowledgeContent) 
    {
    	knowledgeContent.setId("TEMPLATE0002");
    	return toAjax(contentService.savetemplate(knowledgeContent));
    }
    
    /**
     * 加载模板
     * @return
     */
    @PostMapping("/loadtemplate")
    @ResponseBody
    public KnowledgeContent loadsavetemplate() 
    {
    	return contentService.selectContById("TEMPLATE0002");
    }
    
    /**
     * 检查并确保知识已存入es
     * @param id
     * @return
     */
    @PostMapping("/estatus")
    @ResponseBody
    public AjaxResult estatus(String id) 
    {    	
    	return businessContentService.esCheckAndSave(id);
    }

    /**
     * 提交申请修改下线-保存
     * @return
     */
    @PostMapping("/applyBack")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult applyBackSave(@Validated KnowledgeContent knowledgeContent)
    {
        String status = contentService.selectContById(knowledgeContent.getId()).getStatus();
        if(!status.equals(PostConstants.STATUS_TWO)){
            return AjaxResult.warn("知识状态已经改变，请刷新页面");
        }
        return toAjax(businessContentService.backApply(knowledgeContent));
    }

    /**
     * 提交申请做废-保存
     * @return
     */
    @PostMapping("/applyTrash")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult applyTrashSave(@Validated KnowledgeContent knowledgeContent)
    {
        String status = contentService.selectContById(knowledgeContent.getId()).getStatus();
        if(status.equals(PostConstants.STATUS_NINE)){
            return AjaxResult.warn("该知识已作废，请刷新页面");
        }
        return toAjax(businessContentService.trashApply(knowledgeContent));
    }

    /**
     * 提交申请强制修改下线-保存
     * @return
     */
    @PostMapping("/forceBack")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult forceBackSave(@Validated KnowledgeContent knowledgeContent)
    {
        String status = contentService.selectContById(knowledgeContent.getId()).getStatus();
        if(!status.equals(PostConstants.STATUS_TWO)){
            return AjaxResult.warn("知识状态已经改变，请刷新页面");
        }
        return toAjax(businessContentService.forceBack(knowledgeContent));
    }


    /**
     * 提交申请强制作废-保存
     * @return
     */
    @PostMapping("/forceTrash")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult forceTrashSave(@Validated KnowledgeContent knowledgeContent)
    {
        String status = contentService.selectContById(knowledgeContent.getId()).getStatus();
        if(status.equals(PostConstants.STATUS_NINE)){
            return AjaxResult.warn("该知识已作废，请刷新页面");
        }
        return toAjax(businessContentService.forceTrash(knowledgeContent));
    }

    /* 知识库对应问题单 */


    /**
     * 保存业务知识时，查询填写问题单号是否存在
     */
    @PostMapping("/selectIssuefxByNo")
    @ResponseBody
    public int selectIssuefxByNo(KnowledgeContent knowledgeContent) {
        return businessContentService.selectIssuefxByNo(knowledgeContent);
    }


}