package com.ruoyi.form.controller.customerForm;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.PersonnelAllocation;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.ChangeTaskStatusEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.service.*;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/customerForm")
@Api(tags = "自定义表单模块")
@CrossOrigin
public class CustomerFormController extends BaseController {


    @Resource
    CustomerFormService customerFormService;

    @Autowired
    EventConsumeDetailsService consumeDetailsService;

    @Autowired
    EventForeignService eventForeignService;

    @Autowired
    IncidentService incidentService;
    @Resource
    WorkerOrderRulesService workerOrderRulesService;
   @Autowired
    IImplRecordService IImplRecordService;
    @Autowired
     ISysRoleService iSysRoleService;
    @PostMapping("/list/{code}")
    @ApiOperation("自定义表单数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表单名称",required = true),
            @ApiImplicitParam(name = "customerFormListDTO",value = "表单列表搜索条件",dataType = "CustomerFormListDTO"),
            @ApiImplicitParam(name = "type",value = "类型数据类型：1、全部列表（后续改为只查看自己填写的的）2、代办列表 3、已办列表"),
    })
    public AjaxResult customerList(@PathVariable("code") String code,String type,@RequestBody CustomerFormListDTO customerFormListDTO) {
        Page page = buildPageObject();
        if(WorkOrderInformation.workerOrderRules.getCode().equals(code)||WorkOrderInformation.workerOrder.getCode().equals(code)){
            return  workerOrderRulesService.list(code,type,page);
        }
        AjaxResult result = customerFormService.list(code,type, page,customerFormListDTO);
        return result;
    }


    @PostMapping("/insertOrUpdate")
    @ApiOperation("自定义表单新增/修改数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表单名称",required = true),
            @ApiImplicitParam(name = "customerFormContent",paramType = "body",dataType = "CustomerFormContent",value = "表单内容",required = true)
    })
    public AjaxResult insertOrUpdate(String fromCode, String id, String code, @RequestBody CustomerFormContent customerFormContent) {
        if (WorkOrderInformation.workerOrderRules.getCode().equals(code)){
            return workerOrderRulesService.insertOrUpdate(code,customerFormContent);
        }
        AjaxResult result = customerFormService.insertOrUpdate(fromCode, id, code ,customerFormContent);
        return result;
    }


    @DeleteMapping("/delete/{code}")
    @ApiOperation("自定义表单删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表单名称",required = true),
            @ApiImplicitParam(name = "id",value = "存储信息的ID",required = true)
    })
    public AjaxResult delete(@PathVariable("code") String code, Integer id) {
        AjaxResult result = customerFormService.deleteById(code, id);
        return result;
    }

    @GetMapping("getParaValue")
    @ApiOperation("根据类型code获取具体搜索条件参数值")
    @ApiImplicitParam(name = "paraName",value = "参数名：固定传值：formSearchCondition",required = true)
    public AjaxResult getParaValue(String paraName){
        AjaxResult result = customerFormService.getParaValue(paraName);
        return result;
    }


    @GetMapping("/getFormJsonData/{code}")
    @ApiOperation("获取表单存储信息详情(包含审批节点中已填写的表单信息)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表名",required = true),
            @ApiImplicitParam(name = "id",value = "表单列表ID",required = true),
            @ApiImplicitParam(name = "processId",value = "流程实例ID",required = false)
    })
    public Object getFormJsonData(@PathVariable("code") String code,Integer id,String processId){

        return customerFormService.getFormInfo(code, id,processId);
    }


    @GetMapping("/getFormJson/{code}")
    @ApiOperation("新增表单数据前先查询表单json数据渲染")
    @PersonnelAllocation
    public AjaxResult getFormInfo(@PathVariable("code") String code){
        AjaxResult formInfo = customerFormService.getFormJson(code);
        return formInfo;
    }
    @GetMapping("/getProblemTaskFormInfo/{code}")
    @ApiOperation("新增问题任务单数据前先查询表单json数据渲染")
    @PersonnelAllocation
    public AjaxResult getProblemTaskFormInfo(@PathVariable("code") String code, String problemNo){
        AjaxResult formInfo = customerFormService.getProblemTaskFormInfo(code, problemNo);
        return formInfo;
    }

    /**
     * 事件单创建问题单 变更单
     * @param code
     * @param id
     * @return
     */
    @GetMapping("/incidentCreate/{fromCode}/{code}/{id}")
    @ApiOperation("发起问题单变更单")
    public AjaxResult incidentCreate(@PathVariable("fromCode") String fromCode,@PathVariable("code") String code,@PathVariable("id")String id){
        AjaxResult formInfo = customerFormService.incidentCreate(fromCode,code,id);
        return formInfo;
    }
    @PostMapping("/processSubmit")
    @ApiOperation("开始流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表名",required = true),
            @ApiImplicitParam(name = "businessKey",value = "业务主表ID",required = true),
            @ApiImplicitParam(name = "variables",value = "开始流程条件",paramType = "body",required = false)
    })
    public AjaxResult processSubmit(String code,String businessKey,String version,@RequestBody Map<String,Object> variables){
        if (WorkOrderInformation.workerOrderRules.getCode().equals(code)){
            return workerOrderRulesService.processSubmit(code,businessKey,version,variables);
        }
        AjaxResult result = customerFormService.processSubmit(code, businessKey,version,variables);
        return result;
    }


    @GetMapping("/approvalPopUp")
    @ApiOperation("加载审批弹框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表名",required = true),
            @ApiImplicitParam(name = "processId",value = "流程实例"),
            @ApiImplicitParam(name = "id",value = "业务主表ID",required = true),
            @ApiImplicitParam(name = "type",value = "类型",required = true),
            @ApiImplicitParam(name = "version",value = "版本号"),
    })
    public AjaxResult getHistoryApproveList(String code,String processId,Integer id,String type,String version){
        if(WorkOrderInformation.workerOrderRules.getCode().equals(code)||WorkOrderInformation.workerOrder.getCode().equals(code)){
            return workerOrderRulesService.approvalPopUp(code,processId,id,type,version);
        }
        AjaxResult formKeyInfo = customerFormService.approvalPopUp(code,processId,id,type,version);
        return formKeyInfo;
    }

    @PostMapping("/complete")
    @ApiOperation("提交流程审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "业务表code",required = true),
            @ApiImplicitParam(name = "taskId",value = "列表任务ID",required = true),
            @ApiImplicitParam(name = "instanceId",value = "流程实例ID",required = true),
            @ApiImplicitParam(name = "statusStr",value = "当前节点按钮的中文含义",required = true),
            @ApiImplicitParam(name = "customerVo",value = "参数对象",paramType = "body",required = true)
    })
    public AjaxResult complete(String code,String taskId, String instanceId,String statusStr, @RequestBody CustomerVo customerVo){
        if (WorkOrderInformation.workerOrderRules.getCode().equals(code)||WorkOrderInformation.workerOrder.getCode().equals(code)){
            return workerOrderRulesService.complete(code, taskId, instanceId, statusStr, customerVo);
        }
        AjaxResult result = customerFormService.complete(code, taskId, instanceId, statusStr, customerVo);
        return result;
    }


    @PostMapping( "/cancelApply")
    @ApiOperation("撤销流程")
    public AjaxResult cancelApply(String instanceId) {
        AjaxResult result = customerFormService.cancelApply(instanceId);
        return result;
    }


    @PostMapping( "/suspendOrActiveApply")
    @ApiOperation("挂起或激活")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "业务主表code编码",required = true),
            @ApiImplicitParam(name = "instanceId",value = "流程实例ID",required = true),
            @ApiImplicitParam(name = "bizNo",value = "单子编号",required = true)
    })
    public AjaxResult suspendOrActiveApply(String code,String instanceId,String bizNo) {
        AjaxResult result = customerFormService.suspendOrActiveApply(code,instanceId,bizNo);
        return result;
    }


    @PostMapping("/listHistory")
    @ApiOperation("审批历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "业务主表code编码",required = true),
            @ApiImplicitParam(name = "instanceId",value = "流程实例ID",required = true),
    })
    public AjaxResult listHistory(String code,String instanceId) {
        AjaxResult result = customerFormService.selectHistoryList(code,instanceId);
        return result;
    }


    @GetMapping("/activityXmlResource")
    @ApiOperation("查找activity流文件")
    @ApiImplicitParam(name = "code",value = "业务主表ID/流程定义key",required = true)
    public String activityXmlResource(String code){
        return customerFormService.activityXmlResource(code);
    }



    @GetMapping("/getStartProcessCondition")
    @ApiOperation("获取流程开始时的流条件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "业务主表表名/流程定义key",required = true),
            @ApiImplicitParam(name = "bussinessKey",value = "业务主表Id",required = true)})
    public AjaxResult getStartProcessCondition(String code,String bussinessKey){
        if(WorkOrderInformation.workerOrderRules.getCode().equals(code)){
            return workerOrderRulesService.getStartProcessCondition(code);
        }
        return customerFormService.getStartProcessCondition(code,bussinessKey);
    }


    @GetMapping("/getHistoryImage")
    @ApiOperation("获取流程实例的流程图，高亮展示")
    public void getHistoryImage(String instanceId,HttpServletResponse response){
        customerFormService.createHistoryImage(instanceId,response);
    }


    @GetMapping("/addCaddCandidateUser")
    @ApiOperation("指派任务到XX人")
    public AjaxResult addCaddCandidateUser(String taskId,String userId){
        return customerFormService.addCaddCandidateUser(taskId,userId);
    }

    @PostMapping("/problemTaskList")
    @ApiOperation("根据问题单编号查询子任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemNo",value = "问题单编号",required = true)
    })
    public AjaxResult problemTaskList(String problemNo) {
        Page page = buildPageObject();
        AjaxResult result = customerFormService.problemTaskList(problemNo, page);
        return result;
    }


    @PostMapping("/getOperationDetails")
    @ApiOperation("获取单子操作详情")
    @ApiImplicitParam(name = "bizNo",value = "具体单子编号",required = true)
    public AjaxResult getOperationDetails(String bizNo){
        Page page = buildPageObject();

        return customerFormService.getOperationDetails(bizNo,page);
    }

    @PostMapping("/insertOperationDetails")
    @ApiOperation("保存单子操作详情")
    public AjaxResult insertOperationDetails(String code,@RequestBody OperationDetails operationDetails){
        return customerFormService.insertOperationDetails(code,operationDetails);
    }

    /**
     *表单列表编辑接口
     * @param customerFormContent
     * @return
     */
    @PostMapping("/updateList")
    @ApiOperation("表单修改数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerFormContent",paramType = "body",dataType = "CustomerFormContent",value = "编辑内容",required = true)
    })
    public AjaxResult updateList(@RequestBody CustomerFormContent customerFormContent) {
        AjaxResult result = customerFormService.updateFromList(customerFormContent);
        return result;
    }

    /**
     * /主页变更列表
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/listChangeShifts")
    @ApiOperation("主页排班列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerFormListDTO",value = "表单列表搜索条件",dataType = "CustomerFormListDTO"),
    })
    public AjaxResult listChangeShifts(@RequestBody CustomerFormListDTO customerFormListDTO) {
        Page page = buildPageObject();
        AjaxResult result = customerFormService.listChangeShifts(WorkOrderInformation.change.getCode(), "2", page,customerFormListDTO);
        return result;
    }

    /**
     * /排班叫号列表
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/listChangeSequence")
    @ApiOperation("排班叫号列表")
    public AjaxResult listChangeSequence() {
        Page page = buildPageObject();
        AjaxResult result = customerFormService.listChangeSequence();
        return result;
    }
    /**
     * /排班叫号完成接口
     * @param
     * @param
     * @param
     * @return
     */
    @GetMapping("/comepleteChangeSequence/{changeTaskNo}")
    @ApiOperation("排班叫完成接口")
    public AjaxResult comepleteChangeSequence(@PathVariable("changeTaskNo") String changeTaskNo) {
        ImplRecord implRecord=new ImplRecord();
        implRecord.setChangeNo(changeTaskNo);
        implRecord.setTaskStatus(ChangeTaskStatusEnum.closed.getName());
        IImplRecordService.updateImplRecordChangeTaskNo(implRecord);
        return AjaxResult.success();
    }
    /**
     * /排班发布列表
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/selectImplRecordListVo")
    @ApiOperation("排班发布列表")
    public AjaxResult selectImplRecordListVo(@RequestBody String reString,Integer pageSize,Integer pageNum) {
        startPage(pageNum,pageSize);
        ImplRecord implRecord=new ImplRecord();
        Map<String, Object> params=new HashMap<>();
        if(StringUtils.isNotEmpty(reString)){
            JSONObject object=JSONObject.parseObject(reString);
            if(StringUtils.isNotEmpty(object.get("startTime"))){
                JSONArray startTime = (JSONArray) object.get("startTime");
                object.put("startTime","");
                if(!CollectionUtils.isEmpty(startTime)){
                    params.put("startTimeStart",startTime.get(0));
                    params.put("startTimeEnd",startTime.get(1));
                }
            }
            if(StringUtils.isNotEmpty(object.get("changeStartTime"))){
                JSONArray changeStartTime= (JSONArray) object.get("changeStartTime");
                object.put("changeStartTime","");
                if(!CollectionUtils.isEmpty(changeStartTime)){
                    params.put("changeStartTimeStart",changeStartTime.get(0));
                    params.put("changeStartTimeEnd",changeStartTime.get(1));
                }
            }
            implRecord=JSONObject.parseObject(object.toJSONString(),ImplRecord.class);
            implRecord.setParams(params);
        }
        //new PageInfo(list).getTotal()
        List<ImplRecord> list= IImplRecordService.selectImplRecordListVo(implRecord);
        Map<String, Object> reMap = new HashMap<>();
        Set<String> roleIds=iSysRoleService.selectAllRoleKeys(CustomerBizInterceptor.currentUserThread.get().getUserId());
        if(roleIds.contains(CustomerFlowConstants.PBPERSON_ROLE)){
            reMap.put("role",CustomerFlowConstants.PBPERSON_ROLE);
        }else {
            if(roleIds.contains(CustomerFlowConstants.PB_EFFECTUSER)){
                reMap.put("role",CustomerFlowConstants.PB_EFFECTUSER);
            }else {
                reMap.put("role",CustomerFlowConstants.PB_TOURIST);
            }
        }
        reMap.put("data",list);
        reMap.put("total",new PageInfo(list).getTotal());
        return AjaxResult.success(reMap);
    }
    /**
     * 排班列表导出
     * @param
     * @return
     */
    @PostMapping("implRecordListVoExport")
    @ResponseBody
    public void export(@RequestBody String reString,Integer pageSize,Integer pageNum,HttpServletResponse httpServletResponse) throws IOException {
        ImplRecord implRecord=new ImplRecord();
        Map<String, Object> params=new HashMap<>();
        if(StringUtils.isNotEmpty(reString)){
            JSONObject object=JSONObject.parseObject(reString);
            if(StringUtils.isNotEmpty(object.get("startTime"))){
                JSONArray startTime = (JSONArray) object.get("startTime");
                object.put("startTime","");
                if(!CollectionUtils.isEmpty(startTime)){
                    params.put("startTimeStart",startTime.get(0));
                    params.put("startTimeEnd",startTime.get(1));
                }
            }
            if(StringUtils.isNotEmpty(object.get("changeStartTime"))){
                JSONArray changeStartTime= (JSONArray) object.get("changeStartTime");
                object.put("changeStartTime","");
                if(!CollectionUtils.isEmpty(changeStartTime)){
                    params.put("changeStartTimeStart",changeStartTime.get(0));
                    params.put("changeStartTimeEnd",changeStartTime.get(1));
                }
            }
            if("0".equals(object.getString("isAll"))){
                startPage(pageNum,pageSize);
            }
            implRecord=JSONObject.parseObject(object.toJSONString(),ImplRecord.class);
            implRecord.setParams(params);
        }
        //new PageInfo(list).getTotal()
        List<ImplRecord> resultList= IImplRecordService.selectImplRecordListVo(implRecord);
       // CustomerStrategyUtil.exportExcel(list,formFieldInfos,"排班发布",changeRetrievalVo.getExcelParams(),httpServletResponse);
        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("排班列表", "UTF-8").replaceAll("\\+", "%20");
        httpServletResponse.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<List<String>> keys=ListUtils.newArrayList();
//            IDCodeConvertChineseUtil.convertIdToName(params.getCode(),resultList);
//            IDCodeConvertChineseUtil.convertEnumToName(params.getCode(),resultList);
      //  IDCodeConvertChineseUtil.convertEnumToNameSet(params.getCode(),resultList);
        ExcelUtil<ImplRecord> util = new ExcelUtil<ImplRecord>(ImplRecord.class);
        util.createExcelField();
        List<Object[]> fields=util.fields;
        for (Object[] os : fields) {
            List<String> head = ListUtils.newArrayList();
            head.add( ((Excel) os[1]).name());
            keys.add(head);
        }
        List<List<Object>> list = ListUtils.newArrayList();
        resultList.forEach(r->{
            List<Object> data = ListUtils.newArrayList();
            fields.forEach(b->{
                Field field = (Field) b[0];
                String str = JSON.toJSONString(r);
                JSONObject jsonObject=JSONObject.parseObject(str);
                data.add(jsonObject.get(field.getName()));
            });
            list.add(data);
        });
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(httpServletResponse.getOutputStream()).head(keys).sheet("模板").doWrite(list);
    }

    protected void startPage(Integer pageNum,Integer pageSize) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
    /**
     * /排班发布列表--保存功能
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/saveImplRecordListVo")
    @ApiOperation("排班发布列表保存功能")
    public AjaxResult saveImplRecordListVo(@RequestBody String reString ) {
        if(StringUtils.isNotEmpty(reString)){
            ImplRecord implRecord=JSONObject.parseObject(reString,ImplRecord.class);
            int a= IImplRecordService.updateImplRecord(implRecord);
        }
        return AjaxResult.success();
    }
    /**
     * /排班发布列表--批量保存功能
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/saveImplRecordListVoAll")
    @ApiOperation("排班发布列表批量保存功能")
    public AjaxResult saveImplRecordListVoAll(@RequestBody String reString ) {
        if(StringUtils.isNotEmpty(reString)){
            JSONArray jsonArray=JSONArray.parseArray(reString);
            for(Object object:jsonArray){
                String im= String.valueOf(object);
                ImplRecord implRecord=JSONObject.parseObject(im,ImplRecord.class);
                int a= IImplRecordService.updateImplRecord(implRecord);
            }
        }
        return AjaxResult.success();
    }
    /**
     * /排班发布列表批量发布功能
     * @param
     * @param
     * @param
     * @return
     */
    @PostMapping("/issueImplRecordListVo")
    @ApiOperation("排班发布列表批量发布功能")
    public AjaxResult issueImplRecordListVo(@RequestBody String reString ) {
        if(StringUtils.isNotEmpty(reString)){
            JSONArray jsonArray=JSONArray.parseArray(reString);
            String[] imArray=new String[jsonArray.size()];
            for(int i=0;i<jsonArray.size();i++){
                String im= String.valueOf(jsonArray.get(i));
                JSONObject jsonObject=JSONObject.parseObject(im);
                imArray[i]=jsonObject.getString("id");
            }
            IImplRecordService.issueImplRecordListVo(imArray);
        }
        return AjaxResult.success();
    }

    /**
     * 修改值班排序
     * @param
     * @param id
     * @return
     */
    @PostMapping("/ChangeSequence")
    @ApiOperation("修改值班排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true),
            @ApiImplicitParam(name = "userId",value = "人员ID",required = true),
            @ApiImplicitParam(name = "upOrdown",value = "操作",required = true)
    })
    public AjaxResult ChangeSequence( String id,String userId, String upOrdown){
        AjaxResult formInfo = customerFormService.changeSequence(id,userId,upOrdown);
        return formInfo;
    }


    @PostMapping("/incident/reminder")
    @ApiOperation("事件单催单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "事件单code",required = true),
            @ApiImplicitParam(name = "operationDetails",value = "操作对象参数",dataType = "OperationDetails"),

    })
    public AjaxResult reminder(String code,@RequestBody OperationDetails operationDetails){
        return customerFormService.reminder(code,operationDetails);
    }


    @PostMapping("/incident/suspend")
    @ApiOperation("事件单挂起")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "事件单code",required = true),
            @ApiImplicitParam(name = "instanceId",value = "实例ID",required = true),
            @ApiImplicitParam(name = "bizNo",value = "单子业务编号",required = true),

    })
    public AjaxResult suspend(String  code,String instanceId,String bizNo){
        return customerFormService.suspend(code,instanceId,bizNo);
    }

    @PostMapping("/saveRelationLogList")
    @ApiOperation("保存关联单子")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "formId",value = "当前单子id",required = true),
            @ApiImplicitParam(name = "formCode",value = "当前单子业务表code",required = true),
            @ApiImplicitParam(name = "relationFormId",value = "关联单子id",required = true),
            @ApiImplicitParam(name = "relationFormCode",value = "关联单子业务表code",required = true)
    })
    public AjaxResult saveRelationLogList(String formId, String formCode, String relationFormId, String relationFormCode){

        return customerFormService.saveRelationLogList(formId, formCode, relationFormId, relationFormCode);
    }

    @PostMapping("/getRelationLogList")
    @ApiOperation("获取关联单子列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "formNo",value = "当前单子编号",required = true),
            @ApiImplicitParam(name = "requestType",value = "当前单子请求类型编码 1-问题调查/2-基础设施变更/3-事件/4-问题任务执行",required = false)
    })
    public AjaxResult getRelationLogList(String formNo, String requestType) {
        Page page = buildPageObject();
        return customerFormService.getRelationLogList(formNo, requestType, page);
//        return customerFormService.getRelationLogList(formNo, requestType);
    }

    @PostMapping("/deleteGeneralManagers")
    @ApiOperation("删除总经理审批人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true),
            @ApiImplicitParam(name = "managerIds", value = "页面选择要删除的总经理室人员id,如果选择了多个用逗号分割拼接成字符串", required = true)
    })
    public AjaxResult deleteGeneralManager(String instanceId, String managerIds) {
        return customerFormService.deleteGeneralManager(instanceId, managerIds);
    }

    @PostMapping( "/getNoById")
    @ApiOperation("根据问题单id获取编号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "当前数据单子id", required = true),
            @ApiImplicitParam(name = "code", value = "表单code", required = true)
    })
    public AjaxResult getNoById(String id, String code) {
        AjaxResult result = customerFormService.getNoById(id, code);
        return result;
    }

    @PostMapping("/selectEventConsumeDetailsByNo")
    @ApiOperation("根据单号获取事件耗时详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizNo", value = "事件单号", required = true),
    })
    public AjaxResult selectEventConsumeDetailsByNo(String bizNo) {
        Page page = buildPageObject();
        AjaxResult ajaxResult = consumeDetailsService.selectEventConsumeDetailsByNo(bizNo, page);
        return ajaxResult;
    }

    @PostMapping("/selectServiceAssignDetailsByNo")
    @ApiOperation("根据单号获取客服派单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizNo", value = "事件单号", required = true),
    })
    public AjaxResult selectServiceAssignDetailsByNo(String bizNo) {
        Page page = buildPageObject();
        AjaxResult ajaxResult = eventForeignService.selectServiceAssignDetailsByNo(bizNo, page);
        return ajaxResult;
    }

    /**
     * 事件单告警批量关闭查询接口
     * @param jsonData
     * @return
     */
    @PostMapping("/muliCloseEvent")
    @ResponseBody
    public Map<String, Object> muliCloseEvent(@RequestBody String jsonData) {
        return eventForeignService.muliCloseEvent(jsonData);
    }

    /**
     * 批量关闭接口  调用complete完成流程运转
     * @param code
     * @param taskId
     * @param instanceId
     * @param statusStr
     * @param customerVo
     * @param eventNos
     * @return
     */
    @PostMapping("/execMuliCloseEvent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "业务表code",required = true),
            @ApiImplicitParam(name = "taskId",value = "列表任务ID",required = true),
            @ApiImplicitParam(name = "instanceId",value = "流程实例ID",required = true),
            @ApiImplicitParam(name = "statusStr",value = "当前节点按钮的中文含义",required = true),
            @ApiImplicitParam(name = "customerVo",value = "参数对象",paramType = "body",required = true)})
    @ResponseBody
    public AjaxResult execMuliCloseEvent(String code,String taskId, String instanceId,String statusStr, @RequestBody CustomerVo customerVo) {
        return eventForeignService.execMuliCloseEvent(code,taskId, instanceId,statusStr, customerVo);
    }

    /**
     * 事件单告警批量接单查询接口
     * @param params
     * @return
     */
    @PostMapping("/selectIncidentListByDealClose")
    @ResponseBody
    public Map<String, Object> selectIncidentListByDealClose(@RequestBody Map<String, Object> params) {
        Page page = buildPageObject();
        return eventForeignService.selectIncidentListByDealClose(page, params);
    }

    /**
     * 批量接单方法
     * @param code
     * @param taskId
     * @param instanceId
     * @param statusStr
     * @param customerVo
     * @return
     */
    @PostMapping("/batchDealEvent")
    @ResponseBody
    public AjaxResult batchDealEvent(String code,String taskId, String instanceId,String statusStr, @RequestBody CustomerVo customerVo) {
        return eventForeignService.batchDealEvent(code,taskId, instanceId,statusStr, customerVo);
    }

    @PostMapping("/getAlertListByItsmId")
    @ApiOperation("获取告警信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizNo", value = "事件单号")
    })
    public AjaxResult getAlertListByItsmId(String bizNo) {
        Map<String, Object> map = eventForeignService.getAlertListByItsmId(bizNo);
        return AjaxResult.success(map);
    }

    @PostMapping("/queryInstanceIdAndIdByBizNo")
    @ApiOperation("查询单子实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizNo", value = "各单子编号",required = true),
            @ApiImplicitParam(name = "code",value = "业务表code",required = true)
    })
    public AjaxResult queryInstanceIdAndIdByBizNo(String bizNo, String code) {
        return customerFormService.queryFormDetailByBizNo(bizNo, code);
    }
}
