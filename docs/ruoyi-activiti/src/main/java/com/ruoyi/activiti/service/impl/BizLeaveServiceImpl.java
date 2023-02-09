package com.ruoyi.activiti.service.impl;

import com.github.pagehelper.Page;
import com.ruoyi.activiti.domain.BizLeaveVo;


import com.ruoyi.activiti.mapper.BizLeaveMapper;
import com.ruoyi.activiti.service.IBizLeaveService;
import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.mapper.OgUserMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysWorkService;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假业务Service业务层处理
 *
 * @author gggggg Tech
 * @date 2019-10-11
 */
@Service
@Transactional
public class BizLeaveServiceImpl implements IBizLeaveService {
	@Autowired
	private BizLeaveMapper bizLeaveMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private  ISysWorkService ISysWorkService;
	@Autowired
	private OgUserMapper ogUserMapper;
	@Value("${pagehelper.helperDialect}")
	private String dbType;

	/**
	 * 查询请假业务
	 *
	 * @param id 请假业务ID
	 * @return 请假业务
	 */
	@Override
	public BizLeaveVo selectBizLeaveById(Long id) {
		BizLeaveVo leave = bizLeaveMapper.selectBizLeaveById(id);
		//SysUser sysUser = userMapper.selectUserByLoginName(leave.getApplyUser());
		//if (sysUser != null) {
			leave.setApplyUserName(ShiroUtils.getLoginName());
		//}
		return leave;
	}

	/**
	 * 查询请假业务列表
	 *
	 * @param bizLeave 请假业务
	 * @return 请假业务
	 */
	@Override
	public List<BizLeaveVo> selectBizLeaveList(BizLeaveVo bizLeave) {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();

		// PageHelper 仅对第一个 List 分页
		Page<BizLeaveVo> list = (Page<BizLeaveVo>) bizLeaveMapper.selectBizLeaveList(bizLeave);
		Page<BizLeaveVo> returnList = new Page<>();
		for (BizLeaveVo leave : list) {
			Map<String,Object> map1 = new HashMap<>();
			map1.put("username",leave.getCreateBy());
			OgUser user1 = ogUserMapper.selectUserByLoginName(map1);
			if (user1 != null) {
				leave.setCreateUserName(user1.getUserName());
			}
			OgUser user2 = null;
			if("mysql".equals(dbType)){
				user2 = ogUserMapper.selectUserByLoginNameMysql(map1);
			}else{
				user2 = ogUserMapper.selectUserByLoginName(map1);
			}

			if (user2 != null) {
				leave.setApplyUserName(user2.getUserName());
			}
			// 当前环节
			if (StringUtils.isNotBlank(leave.getInstanceId())) {
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(leave.getInstanceId())
//                        .singleResult();
						.list(); // 例如请假会签，会同时拥有多个任务
				if (!CollectionUtils.isEmpty(taskList)) {
					TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
					leave.setTaskId(task.getId());
					if (task.getSuspensionState() == 2) {
						leave.setTaskName("已挂起");
						leave.setSuspendState("2");
					} else {
						leave.setTaskName(task.getName());
						leave.setSuspendState("1");
					}
				} else {
					// 已办结或者已撤销
					leave.setTaskName("已结束");
				}
			} else {
				leave.setTaskName("未启动");
			}
			returnList.add(leave);
		}
		returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
		returnList.setPageNum(pageNum);
		returnList.setPageSize(pageSize);
		return returnList;
	}

	/**
	 * 新增请假业务
	 *
	 * @param bizLeave 请假业务
	 * @return 结果
	 */
	@Override
	public int insertBizLeave(BizLeaveVo bizLeave) {
		//String userName = (String) PermissionUtils.getPrincipalProperty("userName");
		bizLeave.setCreateBy(ShiroUtils.getLoginName());

		bizLeave.setApplyUserName(ShiroUtils.getLoginName());
		bizLeave.setCreateTime(DateUtils.getNowDate());

		bizLeave.setId(Long.valueOf(IdUtils.getUUIDInOrderId()));
		return bizLeaveMapper.insertBizLeave(bizLeave);
	}

	/**
	 * 修改请假业务
	 *
	 * @param bizLeave 请假业务
	 * @return 结果
	 */
	@Override
	public int updateBizLeave(BizLeaveVo bizLeave) {
		bizLeave.setUpdateTime(DateUtils.getNowDate());
		return bizLeaveMapper.updateBizLeave(bizLeave);
	}

	/**
	 * 删除请假业务对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteBizLeaveByIds(String ids) {
		return bizLeaveMapper.deleteBizLeaveByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除请假业务信息
	 *
	 * @param id 请假业务ID
	 * @return 结果
	 */
	@Override
	public int deleteBizLeaveById(Long id) {
		return bizLeaveMapper.deleteBizLeaveById(id);
	}

	/**
	 * 请假流程发起
	 * 
	 * @param entity
	 * @param applyUserId
	 * @return
	 */
	@Override
	public ProcessInstance submitApply(BizLeaveVo entity, String applyUserId, String key, Map<String, Object> variables) {

		String businessKey = entity.getId().toString(); // 实体类 ID，作为流程的业务 key
		//1.得到ProcessEngine对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		//2.得到RunService对象
		RuntimeService runtimeService = processEngine.getRuntimeService();
		String userLogName=ShiroUtils.getLoginName();
		Map<String,Object> infoProcess=new HashMap<String,Object>();
		//流程变量
		infoProcess.put("table","测试123123");
		infoProcess.put("applyName",userLogName);
		//开始流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjialiucheng2",businessKey, infoProcess);

		entity.setApplyTime(DateUtils.getNowDate());
		entity.setInstanceId(processInstance.getProcessInstanceId());
		entity.setStatus("1");//0 未启动 1 启动 2 结束
		bizLeaveMapper.updateBizLeave(entity);
		return processInstance;
	}

	/**
	 * 查询待办任务
	 */
	public Page<BizLeaveVo> findTodoTasks(BizLeaveVo leave) {
		// 手动分页
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();
		Page<BizLeaveVo> list = new Page<>();
		String userLogName=ShiroUtils.getUserId();
		List<BizLeaveVo> results = new ArrayList<>();
		//个人任务查询
		List<Task> usertasks = taskService.createTaskQuery().taskAssignee(userLogName).list();
		//获取ProcessEngine对象
		ProcessEngine processEngines = ProcessEngines.getDefaultProcessEngine();
		//获取TaskService对象
		TaskService taskServices = processEngines.getTaskService();
		//查询组任务
		//获取工作组
		List<OgGroup> userGroupList=ISysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
		List<Task> grouptasks= new ArrayList<>();
		for(OgGroup gp:userGroupList){
			List<Task> gpTaskList= taskServices.createTaskQuery().taskCandidateGroup(gp.getGroupId()).list();
			grouptasks.addAll(gpTaskList);
		}

		// 根据流程的业务ID查询实体并关联
		for (Task task : grouptasks) {
			TaskEntityImpl taskImpl = (TaskEntityImpl) task;
			String processInstanceId = taskImpl.getProcessInstanceId();
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

			//4.使用流程实例对象获取BusinessKey
			String business_key = pi.getBusinessKey();
			if(business_key!=null){
				BizLeaveVo leave2  =bizLeaveMapper.selectBizLeaveById(new Long(business_key));
				//组任务未认领
				leave2.setIsClaim("0");
				leave2.setInstanceId(processInstanceId);
				leave2.setTaskId(task.getId());
				results.add(leave2);
			}

		}
		for (Task task : usertasks) {
			TaskEntityImpl taskImpl = (TaskEntityImpl) task;
			String processInstanceId = taskImpl.getProcessInstanceId();
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			//4.使用流程实例对象获取BusinessKey
			String business_key = pi.getBusinessKey();

			if(business_key!=null){
				BizLeaveVo leave2  =bizLeaveMapper.selectBizLeaveById(new Long(business_key));
				//个人任务 设置已认领
				leave2.setIsClaim("1");
				leave2.setInstanceId(processInstanceId);
				leave2.setTaskId(task.getId());
				results.add(leave2);
			}

		}
		List<BizLeaveVo> tempList;
		if (pageNum != null && pageSize != null) {
			int maxRow = (pageNum - 1) * pageSize + pageSize > results.size() ? results.size()
					: (pageNum - 1) * pageSize + pageSize;
			tempList = results.subList((pageNum - 1) * pageSize, maxRow);
			list.setTotal(results.size());
			list.setPageNum(pageNum);
			list.setPageSize(pageSize);
		} else {
			tempList = results;
		}

		list.addAll(tempList);

		return list;
	}
	/**
	 * 认领任务
	 * zx
	 */
	public void  claim(String taskId,String userId){
		ProcessEngines.getDefaultProcessEngine().getTaskService().claim(taskId,userId);
	}
	/**
	 * 查询已办列表
	 * 
	 * @param bizLeave
	 * @param
	 * @return
	 */
	@Override
	public Page<BizLeaveVo> findDoneTasks(BizLeaveVo bizLeave) {
		// 手动分页
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();
		Page<BizLeaveVo> list = new Page<>();
		String userLogName=ShiroUtils.getLoginName();
		List<BizLeaveVo> results = new ArrayList<>();
		List<HistoricTaskInstance> hisList  = historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(userLogName).unfinished()
				.list();
		List<HistoricTaskInstance> hisList2  = historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(userLogName).finished()
				.list();
		hisList.addAll(hisList2);

		// 根据流程的业务ID查询实体并关联
		for (HistoricTaskInstance instance : hisList) {
			String processInstanceId = instance.getProcessInstanceId();
			List<HistoricProcessInstance>  hisProcessList = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).orderByProcessInstanceId().desc().list();
			for(HistoricProcessInstance hi:hisProcessList){
				String businessId=hi.getBusinessKey();
				if(businessId!=null){
					BizLeaveVo leave2 = bizLeaveMapper.selectBizLeaveByInstanceId(processInstanceId);
					BizLeaveVo newLeave = new BizLeaveVo();
					if(leave2!=null){

						BeanUtils.copyProperties(leave2, newLeave);
						newLeave.setApplyUserName(leave2.getApplyUser());
						newLeave.setTaskId(instance.getId());
						newLeave.setTaskName(instance.getName());
						newLeave.setDoneTime(instance.getEndTime());
						//SysUser sysUser = userMapper.selectUserByLoginName(leave2.getApplyUser());
						results.add(newLeave);
					}
				}
			}

		}

		List<BizLeaveVo> tempList;
		if (pageNum != null && pageSize != null) {
			int maxRow = (pageNum - 1) * pageSize + pageSize > results.size() ? results.size()
					: (pageNum - 1) * pageSize + pageSize;
			tempList = results.subList((pageNum - 1) * pageSize, maxRow);
			list.setTotal(results.size());
			list.setPageNum(pageNum);
			list.setPageSize(pageSize);
		} else {
			tempList = results;
		}

		list.addAll(tempList);

		return list;
	}

}
