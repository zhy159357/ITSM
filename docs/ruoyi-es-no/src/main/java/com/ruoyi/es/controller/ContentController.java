package com.ruoyi.es.controller;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.*;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeCategoryService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.es.service.SearchService;
import com.ruoyi.system.service.ISysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控知识管理页面
 */
@Controller
@RequestMapping("knowledge/cont")
public class ContentController extends BaseController {

    private String prefix = "knowledge/content";

    @Autowired(required=false)
    KnowledgeContentService contentService;
    @Autowired(required=false)
    KnowledgeCategoryService knowledgeCategoryService;
    @Autowired(required=false)
    SearchService searchService;
    @Autowired(required=false)
    KnowledgeTitleService knowledgeTitleService;
    @Autowired(required=false)
    KnowledgeBusinessContentService businessContentService;
    @Autowired
    ISysUserService sysUserService;

    @GetMapping()
    public String cont(ModelMap mmap) {
    	mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("status", status);
        mmap.addAttribute("user", ShiroUtils.getOgUser().getpId());
        return prefix + "/cont";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeContent knowledgeContent) {      
        knowledgeContent.setEventType(EventType.MONITORING.getCode());
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        
        if("3".equals(status)){//当前为二线厂商
        	Map<String,Object> params = new HashMap<>();
        	params.put("loginUser", ShiroUtils.getOgUser().getpId());
        	knowledgeContent.setParams(params);
        }
        startPage();
        List<KnowledgeContent> list = contentService.ContentList(knowledgeContent);
        for(KnowledgeContent kc : list){
        	if(StringUtils.isNotEmpty(kc.getContentId())){
        		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
        	}
        }
        return getDataTable(list);
    }

    /**
     * 打开新增页面
     */
    @GetMapping("/add/{selecttreeId}")
    public String add(@PathVariable("selecttreeId") String selecttreeId, ModelMap mmap) {
        KnowledgeCategory folder = contentService.selectCateById(selecttreeId);
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("categoryId", folder.getCategoryId());
        mmap.addAttribute("categoryName", folder.getCategoryName());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getSysList("1"));
        mmap.addAttribute("content", "自助银行系统");
        mmap.addAttribute("create_id", ShiroUtils.getOgUser().getLoginName());
        mmap.addAttribute("createTime", DateUtils.getTime());
        return prefix + "/add";
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
     * 打开修改页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getSysList("1"));     
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        //mmap.addAttribute("create_id", ShiroUtils.getOgUser().getLoginName());
        return prefix + "/edit";
    }
    /**
     * 暂存
     * @param knowledgeContent
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult save(@Validated KnowledgeContent knowledgeContent) {
    	return toAjax(contentService.save(knowledgeContent));
    }
    /**
     * 提交
     * @param knowledgeContent
     * @return
     */
    @PostMapping("/submit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult submit(@Validated KnowledgeContent knowledgeContent) {
    	return toAjax(contentService.submit(knowledgeContent));
    }


    /**
     * 详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getTitleList("1"));
        mmap.addAttribute("create_id", ShiroUtils.getOgUser().getLoginName());
        mmap.addAttribute("users1", getAuditors("4"));
        mmap.addAttribute("users2", getAuditors("3"));
        return prefix + "/detail";
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
    public AjaxResult export(KnowledgeContent knowledgeContent) {
        String isCurrentPage = (String) knowledgeContent.getParams().get("currentPage");
        knowledgeContent.setCreateId(ShiroUtils.getOgUser().getpId());
        knowledgeContent.setEventType(EventType.MONITORING.getCode());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<KnowledgeContent> list = contentService.ContentList(knowledgeContent);
            for(KnowledgeContent kc : list){
            	if(StringUtils.isNotEmpty(kc.getContentId())){
            		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
            	}
            }
            ExcelUtil<KnowledgeContent> util = new ExcelUtil<>(KnowledgeContent.class);
            return util.exportExcel(list, "监控知识查看");
        } else {
            List<KnowledgeContent> list = contentService.ContentList(knowledgeContent);
            for(KnowledgeContent kc : list){
            	if(StringUtils.isNotEmpty(kc.getContentId())){
            		kc.setContent(knowledgeTitleService.selectKnowledgeTitleById(kc.getContentId()).getSysName());
            	}
            }
            ExcelUtil<KnowledgeContent> util = new ExcelUtil<>(KnowledgeContent.class);
            return util.exportExcel(list, "监控知识查看");
        }
    }
    
    /**
     * 打开申请下线页面
     */
    @GetMapping("/applyBack/{id}")
    public String applyBack(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getTitleList("1"));     
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("create_id", ShiroUtils.getOgUser().getLoginName());
        mmap.addAttribute("operate", PostConstants.OPERATION_FOUR);
        return prefix + "/applying";
    }
    /**
     * 申请下线
     */
    @PostMapping("/applyBack")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult applyBack(@Validated KnowledgeContent knowledgeContent) {
    	String status = contentService.selectContById(knowledgeContent.getId()).getStatus();
    	if(!status.equals(PostConstants.STATUS_TWO)){
    		return AjaxResult.warn("知识状态已经改变，请刷新页面");
    	}
    	return toAjax(contentService.backApply(knowledgeContent));
    }
    /**
     * 打开申请作废页面
     */
    @GetMapping("/applyTrash/{id}")
    public String applyTrash(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("cont", contentService.selectContById(id));
        mmap.addAttribute("categoryList", knowledgeCategoryService.getCateList());
        mmap.addAttribute("titleList", getTitleList("2"));
        mmap.addAttribute("titleList1", getTitleList("1"));     
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        mmap.addAttribute("users", getAuditors(status));
        mmap.addAttribute("status", status);
        mmap.addAttribute("create_id", ShiroUtils.getOgUser().getLoginName());
        mmap.addAttribute("operate", PostConstants.OPERATION_FIVE);
        return prefix + "/applying";
    }
    /**
     * 申请作废
     */
    @PostMapping("/applyTrash")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult applyTrash(@Validated KnowledgeContent knowledgeContent) {
    	String status = contentService.selectContById(knowledgeContent.getId()).getStatus();
    	if(status.equals(PostConstants.STATUS_NINE)){
    		return AjaxResult.warn("该知识已作废，请刷新页面");
    	}
    	return toAjax(contentService.trashApply(knowledgeContent));
    }
    
    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setEventType("0");
        return contentService.selectCategoryTree(knowledgeCategory);
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
        knowledgeCategory.setEventType("0");
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
        if(!list.isEmpty()){
            return error("先删除类别下知识库，再删除知识类别");
        }else{
            return toAjax(contentService.deleteCont(categoryId));
        }
    }
    
    //获取知识标题下拉框
    private List<KnowledgeTitle> getTitleList(String category){
    	KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
    	knowledgeTitle.setEventType(EventType.MONITORING.getCode());
    	knowledgeTitle.setCategory(category);
    	return knowledgeTitleService.selectKnowledgeTitleList(knowledgeTitle);
    }
  //获取知识标题下拉框（关联系统）
    private List<KnowledgeTitle> getSysList(String category){
    	KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
    	knowledgeTitle.setEventType(EventType.MONITORING.getCode());
    	knowledgeTitle.setCategory(category);
    	return knowledgeTitleService.selectKnowledgeTitleSysList(knowledgeTitle);
    }
    
    //获取审核人下拉框
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
    @GetMapping("/alarmAnalize")
    public String alarmAnalize(ModelMap mmap) {
        return prefix + "/alarmAnalize";
    }
    
    /**
     * 告警分析
     */
    @PostMapping("/alarmAnalizeList")
    @ResponseBody
    public TableDataInfo alarmAnalizeList(KnowledgeAlarmExample knowledgeAlarmExample) {
    	startPage();
        List<KnowledgeAlarmExample> list = searchService.alarmAnalizeList(knowledgeAlarmExample);
        return getDataTable(list);
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<KnowledgeAlarmImport> util = new ExcelUtil<KnowledgeAlarmImport>(KnowledgeAlarmImport.class);
        List<KnowledgeAlarmImport> alarmList = null;
        try{
            alarmList = util.importExcel(file.getInputStream());
        }catch (Exception e){
            return AjaxResult.warn("导入数据有误！");
        }
        String message = contentService.importAlarmKnowledge(alarmList);
        return AjaxResult.success(message);
    }
    
    @PostMapping("/exportAlarm")
    @ResponseBody
    public AjaxResult exportAlarm(KnowledgeAlarmExample knowledgeAlarmExample) {
        String isCurrentPage = (String) knowledgeAlarmExample.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<KnowledgeAlarmExample> list = searchService.alarmAnalizeList(knowledgeAlarmExample);
            ExcelUtil<KnowledgeAlarmExample> util = new ExcelUtil<>(KnowledgeAlarmExample.class);
            return util.exportExcel(list, "监控知识查看");
        } else {
            List<KnowledgeAlarmExample> list = (List<KnowledgeAlarmExample>) CacheUtils.get("alarmAnalizeList");
            ExcelUtil<KnowledgeAlarmExample> util = new ExcelUtil<>(KnowledgeAlarmExample.class);
            AjaxResult ajaxResult = util.exportExcel(list, "监控知识查看");
            if (null==list||list.size()==0){
                ajaxResult.put("alarmFlag",0);
            }
            return ajaxResult;
        }
    }
    
    /**
     * 跳转至模板定制页面
     * @return
     */
    @GetMapping("/template")
    public String template(ModelMap mmap) {
    	KnowledgeContent knowledgeContent = contentService.selectContById("TEMPLATE0001");
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
    	knowledgeContent.setId("TEMPLATE0001");
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
    	return contentService.selectContById("TEMPLATE0001");
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
    
}