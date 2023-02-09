package com.ruoyi.form.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;

public interface ForeignService {
	// change_state:变更单状态;change_task_info:变更单子任务信息;change_event_list:事件单子任务信息;change_problem_list:问题单子任务信息
	Map<String, Object> get(String jsonData, String workOrderType);

	Map<String, Object> block(String jsonData);
	
	AjaxResult modifyAdpmChange(String changeNo);
	
	AjaxResult updateAdpmChangeTask(String changeTaskNo);

	AjaxResult queryAdpmTask(String changeNo);

	Map<String, Object> queryDonelist(String jsonData);
	
	Map<String, Object> queryChangeDetail(String changeNo);
	
	Map<String, Object> queryworkdetaillist(String changeNo, Page page);
	
	Map<String, Object> queryChangeTaskDetail(String changeNo);
	
	Map<String, Object> queryChangeFiles(String changeNo);
	
	Map<String, Object> confirmChangeTaskResult(String jsonData);
	
	AjaxResult maintain(Map<String,Object> map);
	
	AjaxResult updateActive(String changeId,boolean active);
	
	String sendComplexMail(String taskNo,String receivers,String copys, String subject, String content, String imgPath, String filePath1, String filePath2);
	
	String sendSmsMessageBySocket(String userNo,String taskNo,String mobiles, String contents);
	
	Map<String, Object> osUserApplication(String jsonData);
	
	Map<String, Object> osApplication(String jsonData);
	
	String getTaskId(String taskNo,String taskType);
	
	List<Map<String, Object>> ittablesCol(String tableName);
	
	List<Map<String, Object>> ittablesCols(String tableName);
	
	List<Map<String, Object>> ittablesData(String itsmNo, String tableName);
	
	int insertData(String taskNo,String tableName,String jsonData);
	
}
