package com.ruoyi.form.foreign;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUpdown;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.form.service.ForeignService;
import com.ruoyi.system.domain.SysBizFile;
import com.ruoyi.system.service.ISysBizFileService;
import com.ruoyi.system.service.server.EntegorServerImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 外接系统对接接口
 */
@RestController
@RequestMapping("/foreign/receive")
@Api(tags = "外接系统对接接口")
@Valid
public class ForeignController extends BaseController {

	@Autowired
	private ForeignService foreignService;
    @Autowired
    private EntegorServerImpl entegorServer;
    
	@PostMapping("/adpm/get")
	@ResponseBody
	@ApiOperation("adpm查询事件变更问题单列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "jsonData", value = "请求参数body", required = true),
			@ApiImplicitParam(name = "workOrderType",
                    value = "类型(change_state:变更单, change_task_info:变更单子任务,event_list:事件单, problem_list:问题单)",
                    required = true)
	})
	public Map<String, Object> get(@NotBlank(message= "jsonData不能为空!") @RequestBody String jsonData, @NotBlank(message= "workOrderType不能为空!") @RequestParam("workOrderType") String workOrderType) {
		return foreignService.get(jsonData, workOrderType);
	}


	@PostMapping("/adpm/task/update")
	@ResponseBody
	@ApiOperation("测试--更新adpm变更任务信息")
	public AjaxResult update(@RequestParam("changeTaskNo") String changeTaskNo) {
		return foreignService.updateAdpmChangeTask(changeTaskNo);
	}

	@PostMapping("/adpm/change/update")
	@ResponseBody
	@ApiOperation("测试--更新adpm变更单信息")
	public AjaxResult modifyChange(@RequestParam("changeNo") String changeNo) {
		return foreignService.modifyAdpmChange(changeNo);
	}
	/*@PostMapping("/adpm/query")
	@ResponseBody
	public Map<String, Object> query(@RequestBody String jsonData) {
		Map<String, Object> map = new HashMap<>();
		map = foreignService.query(jsonData);
		return map;
	}*/
	/**
	 * 查询待办\已办列表
	 * @param changeState
	 * @return
	 */
	@PostMapping("/api/change/mobile/list")
	@ResponseBody
	public Map<String, Object> queryChangeList(@RequestBody String jsonData) {
		//changeState2:代办3:已办 
		//workerId
		Map<String, Object> map = new HashMap<>();
		map = foreignService.queryDonelist(jsonData);
		return map;
	}
	/**
	 * 查询变更单详情
	 * @param changeNo
	 * @return
	 */
	@PostMapping("/api/change/mobile/info")
	@ResponseBody
	public Map<String, Object> queryChangeDetail(@PathVariable("changeNo") String changeNo) {
		//changeNo 单号
		Map<String, Object> map = new HashMap<>();
		map = foreignService.queryChangeDetail(changeNo);
		return map;
	}
	/**
	 * 查询变更单操作记录详情
	 * @param changeNo
	 * @return
	 */
	@PostMapping("/api/change/mobile/work/detail")
	@ResponseBody
	public Map<String, Object> queryChangeInfo(@PathVariable("changeNo") String changeNo) {
		//changeNo 单号
		Map<String, Object> map = new HashMap<>();
		Page page = buildPageObject();
		map = foreignService.queryworkdetaillist(changeNo,page);
		return map;
	}
	/**
	 * 查询表更任务详情
	 * @param itsmNo
	 * @return
	 */
	@PostMapping("/api/change/mobile/tasklist")
	@ResponseBody
	public Map<String, Object> queryChangeTaskDetail(@PathVariable("changeNo")  String changeNo  ) {
		//changeTaskNo
		Map<String, Object> map = new HashMap<>();
		map = foreignService.queryChangeTaskDetail(changeNo);
		return map;
	}
	/**
	 * 查询附件
	 * @param itsmNo
	 * @return
	 */
	@PostMapping("/api/queryFilelist")
	@ResponseBody
	public Map<String, Object> queryChangeFiles(@PathVariable("changeNo") String changeNo) {
		//changeTaskNo
		Map<String, Object> map = new HashMap<>();
		map = foreignService.queryChangeFiles(changeNo);
		return map;
	}
	@PostMapping("/api/downFiles")
    public void download(@PathVariable("changeNo") String changeNo,
                         HttpServletResponse response, HttpServletRequest request) throws Exception {
        SysUpdown att = new SysUpdown();
        att.setId_(changeNo);
//        file_id
//        file_name
//        created_time
//        created_name

//        List<SysUpdown> updowns = updownService.selectUpdownList(att);
//        for (SysUpdown updown : updowns) {
//            String filePath = updown.getFile_path_();
//            String fileName = updown.getFile_name_();
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("multipart/form-data");
//            response.setHeader("Content-Disposition",
//                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, URLCodeUtils.encode(fileName)));
//            byte[] bytes = FastDFSHelper.getInstance().downloadFile("group1", filePath);
//            FileUtils.writeBytes(bytes, response.getOutputStream());
//        }

    }
	/**
	 * 
	 * @param itsmNo
	 * @return
	 */
	@PostMapping("/est/receiveDealReason")
	@ResponseBody
	public Map<String, Object> confirmChangeTaskResult(@RequestBody String jsonData) {
		//changeTaskNo
		Map<String, Object> map = new HashMap<>();
		map = foreignService.confirmChangeTaskResult(jsonData);
		return map;
	}
	
    @PostMapping("/auto/createItsmToMiddleUsePythonTemplate")
    @ResponseBody
    public Map createItsmToMiddleUsePythonTemplate(@RequestBody String json){
       Map<String, Object> paramMap = new HashMap<>();
        paramMap = entegorServer.createItsmToMiddleUsePythonTemplate(json);
        return paramMap;
    }
    @PostMapping("/auto/queryShItsmToMiddleUsePythonTemplate")
    @ResponseBody
    public Map queryShItsmToMiddleUsePythonTemplate(@RequestBody String json){
       Map<String, Object> paramMap = new HashMap<>();
        paramMap = entegorServer.queryShItsmToMiddleUsePythonTemplate(json);
        return paramMap;
    }
    /**
     * 维护窗口创建接口
     * @param json
     * @return
     */
   /* @PostMapping("/monitor/api/v3.1/maintain")
    @ResponseBody
    public Map maintain(@RequestBody String json){
    	Map<String, Object> paramMap = new HashMap<>();
    	Map<String, Object> dataJson = JSON.parseObject(json);
    	Map<String, Object> data = new HashMap<>();
    	String timeType="fixed";
    	String beginTime=String.valueOf(dataJson.get("beginTime"));
    	String changeId=String.valueOf(dataJson.get("changeId"));
    	String name=String.valueOf(dataJson.get("name"));
    	//pasoCode,ipAddress,indexType,systemCode,objectName,insName
    	List group=new ArrayList();
    	List list=new ArrayList();
    	data.put("name", name);
    	data.put("active", true);
    	data.put("timeType", timeType);
    	data.put("timeValues", group);
    	data.put("beginTime", beginTime);
    	data.put("endTime", "");
    	data.put("changeId", changeId);
    	data.put("condition", JSONObject.parse("{}"));
    	data.put("exceptionTime", list);
    	paramMap = foreignService.maintain(json);
    	return paramMap;
    }*/
    /**
    /* * 维护窗口关闭接口
     * @param json
     * @return
     *//*
    @PostMapping("/monitor/api/v3.1/maintain/updateActive")
    @ResponseBody
    public Map updateActive(String changeId,boolean active){
    	Map<String, Object> paramMap = new HashMap<>();
    	paramMap = foreignService.updateActive(changeId,active);
    	return paramMap;
    }*/
    /**
     * 通过单号获取附件列表
     * @param taskNo
     * @param taskType
     * @return
     */
    @PostMapping("/download/get")
    @ResponseBody
    public AjaxResult getTaskId(String taskNo,String taskType){
    	String ticket_id = foreignService.getTaskId(taskNo,taskType);
    	SysBizFile sysBizFile=new SysBizFile();
    	sysBizFile.setTicketId(ticket_id);
    	List<SysBizFile> list  = service.list(sysBizFile);
    	return AjaxResult.success(list);
    }
    /**
     * 通过附件id下载附件
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "下载附件")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "附件 id", required = true),})
    @PostMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> download(@PathVariable("id") String id, HttpServletRequest request,HttpServletResponse response) {
    	 FileInputStream fileInputStream = null;
    	 InputStream fis = null;
    	try {
        	SysBizFile file = service.get(Long.parseLong(id));
                File f = new File(file.getLocation());
                String filename = file.getOriginFileName();
                fileInputStream = new FileInputStream(f);
                fis = new BufferedInputStream(fileInputStream);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                response.setCharacterEncoding("UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                response.addHeader("Content-Length", "" + f.length());
                response.setContentType("application/octet-stream");
                FileUtils.writeBytes(buffer, response.getOutputStream());
                fis.close();
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("下载文件失败" + e);
        }finally {
        	try {
        	if(null!=fis) {
        		fis.close();
        	}
        	if(null!=fileInputStream) {
        		fileInputStream.close();
        	} 
        	} catch (Exception e) {
            	e.printStackTrace();
            }
        }
        return null;
    }
    @RequestMapping("/sendComplexMail")
    @ResponseBody
    public void sendComplexMail() {
    	foreignService.sendComplexMail("testno","guojie2@bosc.cn", null, "test-mail", "test-mail------------------------", "", "", "");
    }
    @RequestMapping("/sendComplexSms")
    @ResponseBody
    public void sendComplexSms() {
    	foreignService.sendSmsMessageBySocket("10010","testno","18686481135", "wulala----------------");
    }
    
    @PostMapping("/cmdb/osUserApplication")
    @ResponseBody
    public Map osUserApplication(@RequestBody String json){
    	Map<String, Object> paramMap = new HashMap<>();
    	//"ApiId:ODSM_OsUserList_002"
    	paramMap = foreignService.osUserApplication(json);
    	return paramMap;
    }
    @PostMapping("/cmdb/osApplication")
    @ResponseBody
    public Map osApplication(@RequestBody String json){
    	Map<String, Object> paramMap = new HashMap<>();
    	paramMap = foreignService.osApplication(json);
    	return paramMap;
    }
    
    @Autowired
    private ISysBizFileService service;
}
