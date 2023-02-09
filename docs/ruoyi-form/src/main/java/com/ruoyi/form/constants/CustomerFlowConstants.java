package com.ruoyi.form.constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName CustomerFlowConstants
 * @Description 自定义流程常量
 * @Author JiaQi Zhang
 * @Date 2022/7/12 11:05
 */
public class CustomerFlowConstants {
    public static final String submitApplyButtonName="提交申请";
    public static final String submitApplyButtonPath="/customerForm/processSubmit";
    public static final String suspendFlowButtonName="挂起";
    public static final String suspendFlowButtonPath="/customerForm/suspendOrActiveApply";
    public static final String activityButtonFlowName="激活";
    public static final String activityButtonFlowPath="/customerForm/suspendOrActiveApply";
    public static final String cancelFlowButtonName="撤销流程";
    public static final String cancelButtonName="取消";
    public static final String cancelFlowButtonPath="/customerForm/cancelApply";

    public static final String deleteGenralManagersButtonName="取消总经理";
    public static final String deleteGeneralManagersButtonPath="/customerForm/deleteGeneralManagers";

    public static final String popButtonPath="/customerForm/approvalPopUp";


    public static final String baseDataButtonName="查看详情";
    public static final String baseDataButtonPath="/customerForm/getFormJsonData/{code}";

    public static final String approveHistoryButtonName="审批历史";
    public static final String approveHistoryButtonPath="/customerForm/listHistory";

    public static final String editButtonName="编辑";
    public static final String editButtonPath="/customerForm/getFormJsonData/{code}";

    public static final String addPlanCompleteTimeButtonName="变更计划完成时间";
    public static final String addButtonName="保存";
    public static final String addButtonPath="/insertOrUpdate";

    public static final String deleteButtonName="删除";
    public static final String deleteButtonPath="/customerForm/delete/{code}";

    public static final String closeButtonName="关闭";
    public static final String closeButtonPath="";

    public static final String assignButtonName="分派";
    public static final String getAssignButtonPath="/assignPage/{eventId}/{taskId}";


    public static final String approvalImageButtonName="查看流程图";
    public static final String approvalImageButtonPath="/customerForm/getHistoryImage";

    /*流程实例挂起状态  采用activiti默认策略 值为2  项目中另一个挂起状态为0  在自定义流程中不采用  */
    public static final int PROCESS_INSTANCE_SUSPEND=2;
    // 事件管理员
    public static final String ADMIN_USER_ID = "8b8080f457fffe39015800015ce60006";
    //分派人员组
    public static final String ASSIGN_ROLE="8ea8da23a73d420b8da886d1a05d8724";
    //一线处理人员 front_line_role
    public static final String FRONT_LINE_ROLE="4c5850e07baf45719963080baab28b9a";
    //二线处理人员 sssecondary_line_role
    public static final String SSSECONDARY_LINE_ROLE="dbcc74fbdb85489f86cb70019ab11874";
    //业务处理 business_role
    public static final String BUSINESS_ROLE="6f987fb042e044e0bb6667ad3f3e702f";
    //外联单位 outreach_role
    public static final String OUTREACH_ROLE="42032535e00f492c83ed648b91bcf2fb";
    /*流程实例激活状态  采用activiti默认策略 值为1   */
    public static final int PROCESS_INSTANCE_activity=1;
    //问题管理员
    public static final String PROBLEM_ADMIN="f3ec04830847402288b5cf45a263c412";
    public static final String TINGJING_BRANCH_PROBLEM_ADMIN="2b68266725154778b921b27f9c341d1c";
    public static final String CHENDU_BRANCH_PROBLEM_ADMIN="3ff45b9c02cc4387a4fc1854062e6fb7";
    public static final String NANJIN_BRANCH_PROBLEM_ADMIN="44ad3573006542e08885e71af6afc4fe";
    public static final String SUZHOU_BRANCH_PROBLEM_ADMIN="53c448efda024d2fb26046751cf44c91";
    public static final String NINGBO_BRANCH_PROBLEM_ADMIN="9ba0a9aa12c94b539bcaaf9bb1d09ed7";
    public static final String BEIJIN_BRANCH_PROBLEM_ADMIN="b55c68e94aa348cf811e20bf0d5daa14";
    public static final String SHENZHENG_BRANCH_PROBLEM_ADMIN="b7faec0531ce488a8536ea516f5f6d83";
    public static final String HANZHOU_BRANCH_PROBLEM_ADMIN="d355520b498d40e984464c96473305af";
    public static final JSONObject DICTIONARY= JSON.parseObject("{\"event\":\"incident\",\"problem\":\"problem\",\"problem_task\":\"problem_task_new\",\"change\":\"change\",\"chm\":\"chm\"}");
    public static final String BRANCH_PROBLEM_ADMIN="b55c68e94aa348cf811e20bf0d5daa14";
    //变更单排班 处事领导 排班管理员
    public static final String CHANGE_TASK_ADMIN="e22ac4236cc942adaa32722e361d5e37";
    //变更实施人
    public static final String CHANGE_TASK_USER="0b9bbf243c024df8a5236dc689768b0c";
    //硬件报障IT服务台组
    public static final String CHM_IT_GROUP="a67fc4417a60421594b2f0ea88a9716b";
    //硬件报障 硬件支持组
    public static final String CHM_YJ_GROUP="8814a04c92ce4cb9b05a3313a84349b5";
    //硬件报障 远程服务支持组
    public static final String CHM_YC_GROUP="2bd30fc9da7e4fc1b141327a415e0856";
    //硬件报障 应用支持部
    public static final String CHM_YYZC_ORG="310200898";
    //硬件报障 移动端报障用户
    public static final String CHM_ADMIN="chm_admin";
    //排班发布人 角色 0021
    public static final String PBPERSON_ROLE="0021";
    //投产实施人 角色 0022
    public static final String PB_EFFECTUSER="0022";
    //排班观察者
    public static final String PB_TOURIST="0023";
}
