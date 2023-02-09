package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName WorkOrderInformation
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/8/8 9:12
 */
@AllArgsConstructor
@Getter
public enum WorkOrderInformation {
    incident("incident", "事件单","incidentServiceImpl"),
    problem("problem", "问题单","problemServiceImpl"),
    TINYWEB_DB_RECOVER("tinyWeb_db_recoverA", "tinyWeb数据库恢复",""),
    TINYWEB_FAULT_SOLVE("tinyWeb_fault_solveA", "tinyWeb故障解决",""),
    TINYWEB_SERVER("tinyWeb_server", "tinyWeb服务请求",""),
    problem_task("problem_task_new", "问题单子任务","problemTaskServiceImpl"),
    change("change", "变更单","changeServiceImpl"),
    changeTask("changeTask","变更任务单","changeTaskServiceImpl"),
    chm_task("chm","硬件报障",""),
    biz_table_prefix("design_biz","前缀",""),
    workerOrderRules("worker_order_rules","工单生成规则","workerOrderRulesServiceImpl"),
    workerOrder("worker_order","工单","workerOrderRulesServiceImpl"),
    customerStrategy("currency","通用","customerStrategyServiceImpl");
    private String code;
    private String desc;
    private String customerServiceImpl;

    public static String getByCode(String code){
        for(WorkOrderInformation v:values()) {
            if(v.code.equals(code)) {
                return v.customerServiceImpl;
            }
        }
        return WorkOrderInformation.customerStrategy.customerServiceImpl;
    }
    /**
     * 根据编号获取名称
     *
     * @param code:编号
     **/
    public static String getName(String code) {
        WorkOrderInformation[] values = values();
        for (WorkOrderInformation workOrderInformation : values) {
            if (code.equals(workOrderInformation.getCode())) {
                return workOrderInformation.getDesc();
            }
        }
        return null;
    }
}
