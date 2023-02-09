package com.ruoyi.es.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.EsSearchBean;
import com.ruoyi.es.domain.KnowledgeAlarmExample;
import com.ruoyi.es.service.*;

import com.ruoyi.system.service.server.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeTitle;

/**
 * 知识库接口
 */
@Controller
@RequestMapping("/knowledgeApi")
//  需要暴露给外部时 请在ShiroConfig过滤器配置部分shiroFilterFactoryBean
//  添加   filterChainDefinitionMap.put("/knowledgeApi/**", "anon")
public class KnowledgeApi {

	@Autowired
	private SearchService searchService;
	@Autowired
	KnowledgeContentService contentService;
	@Autowired
	EsSearchService esSearchService;
	@Autowired
	KnowledgeBusinessContentService businessContentService;
	
	@Autowired
	private KnowledgeTitleService knowledgeTitleService;

	private static KnowledgeApi knowledgeApi = null;

	public static KnowledgeApi getInstance(){
		if(knowledgeApi==null){
			knowledgeApi = new KnowledgeApi();
		}
		return knowledgeApi;
	}

	@PostMapping("/test")
    @ResponseBody
    public String test(){
		return "SUCCESS";
	}
	
	/**
	 * 监控事件单查询知识库调用接口
	 * @param sysid 系统id
	 * @param sectitle 二级标题（告警来源）
	 * @param name 标签（告警描述）
	 * @return
	 */
    public List<KnowledgeContent> searchKnowledge( String sysid, String sectitle, String name){
    	List<KnowledgeContent> resultList = new ArrayList<>();
		String content = getTitileId("" ,"1" , sysid);
		sectitle = getTitileId(sectitle ,"2" , "");
		Map<String,String> searchMap = new HashMap<>();
		searchMap.put("eventType", "0");
		searchMap.put("content", content);
		searchMap.put("sectitle", sectitle);
		searchMap.put("title", name);
		resultList = searchService.searchMonitorApi(searchMap);
		return resultList;
	}
	

    
    /**
	 * 业务事件单查询知识库调用接口(新)
	 * @param systemName 系统id
	 * @param title 标题
	 * @param content 一级标题id
	 * @param sectitle 二级标题id
	 * @param threetitle 三级标题id
	 * @param nameList 标签id list
	 * @return
	 */
    public List<KnowledgeContent> searchKnowledge(String systemName, String title, String content, String sectitle, String threetitle, List<String> nameList){
		StringBuilder name = new StringBuilder();
		for(String s : nameList){
			name.append(knowledgeTitleService.selectKnowledgeTitleById(s).getName()).append(" ");
		}
		
		Map<String,String> searchMap = new HashMap<>();
		searchMap.put("eventType", "1");
		searchMap.put("systemName", systemName);
		searchMap.put("title", title);
		searchMap.put("content", content);
		searchMap.put("sectitle", sectitle);
		searchMap.put("threetitle", threetitle);
		searchMap.put("name", name.toString());
		return searchService.search(searchMap);
	}

	private String getTitileId(String name, String category, String sysid) {	
		if(category.equals("1")&&StringUtils.isEmpty(sysid)){
			return null;
		}
		if(category.equals("2")&&StringUtils.isEmpty(name)){
			return null;
		}
		KnowledgeTitle knowledgeTitle = new KnowledgeTitle();
		knowledgeTitle.setEventType(EventType.MONITORING.getCode());
		knowledgeTitle.setCategory(category);
		knowledgeTitle.setName(name);
		knowledgeTitle.setSysId(sysid);
		List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleByName(knowledgeTitle);
		if(!list.isEmpty() ){
			return list.get(0).getId();
		}
		return "-1";
	}

	/**
	 * 监控知识迁移
	 * @return
	 */
	@GetMapping("/monitoringTransfer")
	@ResponseBody
	public String monitoringTransfer(KnowledgeContent knowledgeContent){
		knowledgeContent.setEventType(EventType.MONITORING.getCode());
		knowledgeContent.setStatus(PostConstants.STATUS_TWO);
		List<KnowledgeContent> list = contentService.ContentList(knowledgeContent);
		int count = 0;
		for(KnowledgeContent content : list){
			EsSearchBean esSearchBean = esSearchService.querySearchById(content.getId());
			if (null == esSearchBean) {
				businessContentService.saveSearchAndEs(content);
				count++;
			}
		}
		return count+"条监控知识数据迁移成功";
	}

    /**
     * checkExport
     * @return
     */
    @GetMapping("/checkExport")
    @ResponseBody
    public String checkExport	( ){
        List<KnowledgeAlarmExample> list = (List<KnowledgeAlarmExample>) CacheUtils.get("alarmAnalizeList");
        if(null==list||list.size()==0){
            return "监控知识数据不存在";
        }else{
            return "监控知识数据有 "+ list.size()+" 条,可以进行导出操作.";
        }
    }

	/**
	 * getpd
	 * @return
	 */
	@GetMapping("/getpd")
	@ResponseBody
	public String getpd	( String userName){
		//获取当前登录人的信息
		OgUser u = ShiroUtils.getOgUser();
		//指定登陆人才可以看到pd
		if((null!=u) && (u.getUsername().equals("admin"))){
			if(null !=CacheUtils.get(UserConstants.GETPD+userName)){
				return "当前登陆人: "+u.getUsername()+",查询用户:"+userName+", pd: "+Base64.decode4login(CacheUtils.get(UserConstants.GETPD+userName).toString());
			}else{
				return "当前登陆人: "+u.getUsername()+",查询用户:"+userName+", pd: 未找到数据";
			}
		}else{
			// 非admin -- 权限不足
			return "当前登陆人: "+u.getUsername()+", 权限不足.";
		}
	}

	/**
	 * 业务知识迁移
	 * @return
	 */
	@GetMapping("/businessTransfer")
	@ResponseBody
	public String businessTransfer(KnowledgeContent knowledgeContent){
		knowledgeContent.setEventType(EventType.BUSINESS.getCode());
		knowledgeContent.setStatus(PostConstants.STATUS_TWO);
		List<KnowledgeContent> list = contentService.ContentList(knowledgeContent);
		int count = 0;
		for(KnowledgeContent content : list){
			EsSearchBean esSearchBean = esSearchService.querySearchById(content.getId());
			if (null == esSearchBean) {
				businessContentService.saveSearchAndEs(content);
				count++;
			}
		}
		return count+"条业务知识数据迁移成功";
	}
	
	/**
	 * 自定义图片上传插件接口
	 * @param request
	 * @param response
	 * @return 返回图片url
	 */
	@PostMapping("/pwupload")
	@ResponseBody
	public String pwUpload(HttpServletRequest request, HttpServletResponse response) {
		String url = "";
		String filename = request.getParameter("fileName");
		
		InputStream input;
		try{
			input = request.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*4];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				out.write(buffer, 0 , n);
			}
			byte[] data = out.toByteArray();//将inputstream图片转为byte[]
			
			url = FastDFSHelper.getInstance().uploadImage(data, filename);
		}catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Length", url.length()+"");
		return url;
	}
	@PostMapping("/getPostUrl")
	@ResponseBody
	public String getPostUrl(HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/knowledgeApi/pwupload";
	}
}
