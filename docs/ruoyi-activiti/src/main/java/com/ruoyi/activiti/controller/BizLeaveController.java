package com.ruoyi.activiti.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ruoyi.activiti.mapper.BizLeaveMapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysWorkService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.BizLeaveVo;
import com.ruoyi.activiti.service.IBizLeaveService;
import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 请假业务Controller
 *
 * @author gggggg Tech
 * @date 2019-10-11
 */
@Controller
@RequestMapping("/leave")
public class BizLeaveController extends BaseController {
    private String prefix = "leave";

    @Autowired
    private IBizLeaveService bizLeaveService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IProcessService processService;
    @Autowired
    private ActivitiCommService ActivitiCommService;
    @Autowired
    private ISysWorkService ISysWorkService;
    @Autowired
    private BizLeaveMapper bizLeaveMapper;
    @GetMapping()
    public String leave(ModelMap mmap) {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/leave";
    }

    /**
     * 查询请假业务列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizLeaveVo bizLeave) {
        if (!OgUser.isAdmin(ShiroUtils.getUserId())) {
            bizLeave.setCreateBy(ShiroUtils.getLoginName());
        }
        bizLeave.setType("leave");
        startPage();
        List<BizLeaveVo> list = bizLeaveService.selectBizLeaveList(bizLeave);
        return getDataTable(list);
    }

    /**
     * 流程图进度
     * @param instanceId
     * @param
     * @return
     */
    @GetMapping("/processImg/{businessKey}")
    public String processImg(@PathVariable("businessKey") String businessKey, ModelMap mmap) {
        mmap.put("businessKey", businessKey);
        return  "process/processImg";//可通用
    }
    /**
     * 导出请假业务列表
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizLeaveVo bizLeave) {
        bizLeave.setType("leave");
        List<BizLeaveVo> list = bizLeaveService.selectBizLeaveList(bizLeave);
        ExcelUtil<BizLeaveVo> util = new ExcelUtil<BizLeaveVo>(BizLeaveVo.class);
        return util.exportExcel(list, "leave");
    }

    /**
     * 新增请假业务
     */
    @GetMapping("/add")
    public String add() {

        return prefix + "/add";
    }

    /**
     * 新增保存请假业务
     */
    @Log(title = "请假业务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizLeaveVo bizLeave) {
        /*String userId = ShiroUtils.getUserId();
        if (OgUser.isAdmin(userId)) {
            return error("提交申请失败：不允许管理员提交申请！");
        }*/
        bizLeave.setType("leave");
        return toAjax(bizLeaveService.insertBizLeave(bizLeave));
    }

    /**
     * 修改请假业务
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        BizLeaveVo bizLeave = bizLeaveService.selectBizLeaveById(id);
        mmap.put("bizLeave", bizLeave);
        return prefix + "/edit";
    }

    /**
     * 修改保存请假业务
     */
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizLeaveVo bizLeave) {
        return toAjax(bizLeaveService.updateBizLeave(bizLeave));
    }

    /**
     * 删除请假业务
     */
    @Log(title = "请假业务", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizLeaveService.deleteBizLeaveByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping("/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        BizLeaveVo leave = bizLeaveService.selectBizLeaveById(id);
        String applyUserId = ShiroUtils.getLoginName();
      // bizLeaveService.submitApply(leave, applyUserId, "leave", new HashMap<>());
        Map<String,Object> variable=new HashMap<>();
        variable.put("userId",ShiroUtils.getUserId()+",4028aba3365822fe013686a7635e137a");
       Map<String,Object> process=ActivitiCommService.startProcess(id.toString(),"qingjialiucheng2",variable);
        leave.setApplyTime(DateUtils.getNowDate());
        leave.setInstanceId(process.get("processInstanceId").toString());
        leave.setStatus("1");//0 未启动 1 启动 2 结束
        bizLeaveMapper.updateBizLeave(leave);
        return success();
    }

    @GetMapping("/leaveTodo")
    public String todoView() {
        return prefix + "/leaveTodo";
    }

    /**
     * 我的待办列表
     *
     * @return
     */
    @PostMapping("/taskList")
    @ResponseBody
    public TableDataInfo taskList(BizLeaveVo bizLeave) {
        bizLeave.setType("leave");
        List<Map<String,Object>> reList =ActivitiCommService.userTask("qingjialiucheng2","");
        List<OgGroup> userGroupList= ISysWorkService.selectGroupByUserId(ShiroUtils.getUserId());

        List<Map<String,Object>> regList=ActivitiCommService.groupTasks("qingjialiucheng2","");
        regList.addAll(reList);
        List<BizLeaveVo> list=new ArrayList<>();
        for(Map<String,Object> map:regList){
            String business_key=map.get("businesskey").toString();
            if(business_key!=null){
                BizLeaveVo leave2  =bizLeaveMapper.selectBizLeaveById(new Long(business_key));
                //个人任务 设置已认领
                leave2.setIsClaim("0");
                leave2.setInstanceId(map.get("processInstanceId").toString());
                leave2.setTaskId(map.get("taskId").toString());
                list.add(leave2);
            }
        }
        return getDataTable(list);
    }

    /**
     * 加载审批弹窗
     *
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        BizLeaveVo bizLeave = bizLeaveService.selectBizLeaveById(new Long(processInstance.getBusinessKey()));
        mmap.put("bizLeave", bizLeave);
        mmap.put("taskId", taskId);
        String verifyName = task.getTaskDefinitionKey().substring(0, 1).toUpperCase()
                + task.getTaskDefinitionKey().substring(1);
        return prefix + "/taskDeptLeaderVerify";
    }

    @GetMapping("/showFormDialog/{instanceId}")
    public String showFormDialog(@PathVariable("instanceId") String instanceId, ModelMap mmap) {
        String businessKey = processService.findBusinessKeyByInstanceId(instanceId);
        BizLeaveVo bizLeaveVo = bizLeaveService.selectBizLeaveById(new Long(businessKey));
        mmap.put("bizLeave", bizLeaveVo);
        return prefix + "/view";
    }

    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId,
                               @RequestParam(value = "saveEntity", required = false) String saveEntity,
                               @ModelAttribute("preloadLeave") BizLeaveVo leave, HttpServletRequest request) {
        boolean saveEntityBoolean = BooleanUtils.toBoolean(saveEntity);
/*        processService.complete(taskId, leave.getInstanceId(), leave.getTitle(), leave.getReason(), "qingjialiucheng2",
               new HashMap<String, Object>(), request);*/
        Map<String,Object> reMap=new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        String comment = null; // 批注
        boolean agree = true;
        try {
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
		/*		if (parameterName.startsWith("p_")) {
					// 参数结构：p_B_name，p为参数的前缀，B为类型，name为属性名称
					String[] parameter = parameterName.split("_");
					if (parameter.length == 3) {*/
                String paramValue = request.getParameter(parameterName);
                if ("totalTime".equals(parameterName)) {
                    parameterName = "total_time";
                    reMap.put(parameterName, paramValue);
                }
                if ("agree".equals(parameterName)) {
                    reMap.put(parameterName, paramValue);
                }
                if ("comment".equals(parameterName)) {
                    comment = paramValue;
                    reMap.put(parameterName, paramValue);
                }
            }
        }catch (Exception e){
            return error(e.toString());
        }
        reMap.put("businessKey",leave.getId());
        reMap.put("taskId",taskId);
        reMap.put("reCode",0);
        reMap.put("userId","4028aba3365822fe013686a7635e137a");
        ActivitiCommService.complete(reMap);
        if (saveEntityBoolean) {
            bizLeaveService.updateBizLeave(leave);
        }
        return success("任务已完成");
    }
    /**
     * 认领任务
     */
    @RequestMapping(value="/claim/{taskId}",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public AjaxResult claim(@PathVariable("taskId") String businessKey,String taskId, HttpServletRequest request){
        //bizLeaveService.claim(taskId,ShiroUtils.getUserId());
        ActivitiCommService.claim(businessKey,taskId);
        return success("认领成功");
    }
    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadLeave")
    public BizLeaveVo getLeave(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return bizLeaveService.selectBizLeaveById(id);
        }
        return new BizLeaveVo();
    }

    @GetMapping("/leaveDone")
    public String doneView() {
        return prefix + "/leaveDone";
    }

    /**
     * 我的已办列表
     *
     * @param bizLeave
     * @return
     */
    @PostMapping("/taskDoneList")
    @ResponseBody
    public TableDataInfo taskDoneList(BizLeaveVo bizLeave) {
        bizLeave.setType("leave");
        List<BizLeaveVo> list =new ArrayList<>();
       List<Map<String,Object>> reList= ActivitiCommService.allComplete(ShiroUtils.getUserId(),"qingjialiucheng2");
       for(Map<String,Object> mp:reList){
            String businesskey=mp.get("businesskey").toString();
           if(businesskey!=null){
               BizLeaveVo leave2 = bizLeaveMapper.selectBizLeaveById(new Long(businesskey));
               BizLeaveVo newLeave = new BizLeaveVo();
               if(leave2!=null){

                   BeanUtils.copyProperties(leave2, newLeave);
                   newLeave.setApplyUserName(leave2.getApplyUser());
                   newLeave.setTaskId(mp.get("taskId").toString());
                   newLeave.setTaskName(mp.get("taskName").toString());
                  // newLeave.setDoneTime(mp.get("").toString());
                   //SysUser sysUser = userMapper.selectUserByLoginName(leave2.getApplyUser());
                   list.add(newLeave);
               }
           }
       }
        return getDataTable(list);
    }
    /**
     * 加载审批历史弹窗
     */
    @GetMapping("/historyList/{businessKey}")
    public String historyList(@PathVariable("businessKey") String businessKey, ModelMap mmap) {
        mmap.put("instanceId", businessKey);
        return "/process/historyList";
    }

}
