package com.ruoyi.form.constants;

import com.ruoyi.common.core.domain.entity.PubParaValue;

import java.util.List;

public class EventFieldConstants {

    public static final String REPORT_ORG = "report_org";// 上报部门
    public static final String REPORT_PERSON = "report_person";// 上报人
    public static final String REPORT_PHONE = "report_phone";// 上报人电话
    public static final String SYSTEM_NAME = "system_name";// 应用系统
    public static final String ASSIGNED_GROUP = "assigned_group";// 分派组
    public static final String ASSIGNED_PERSON = "assigned_person";// 分派人
    public static final String SECOND_DEAL_ORG = "second_deal_org";// 二线部门
    public static final String SECOND_DEAL_PERSON = "second_deal_person";// 二线处理人

    public static final String EVENT_TITLE = "event_title";// 事件标题
    public static final String EVENT_INFO = "event_info";// 事件描述
    public static final String SOLVE_PLAN = "solve_plan";// 解决方案
    public static final String SECOND_SOLVE_PLAN = "second_solve_plan";// 二线解决方案
    public static final String BUSINESS_SOLVE_PLAN = "business_solve_plan";// 业务解决方案
    public static final String SOLUTION_VALID_FLAG = "solution_valid_flag";// 二线解决方案有效性
    public static final String MOINTOR_INVALID = "mointor_invalid";// 监控有效性
    public static final String MONITOR_INVALID_REASON = "monitor_invalid_reason";// 监控无效原因

    public static final String SIDE_FLAG = "side_flag";// 中心侧/网点侧事件
    public static final String FINANCE_FLAG = "finance_flag";// 财务事件
    public static final String COMPLAIN_FLAG = "complain_flag";//是否涉及投诉
    public static final String URGENT_FLAG = "urgent_flag";// 是否加急
    public static final String ORG_FLAG = "org_flag";// 总分行事件标记
    public static final String INF_LEVEL = "inf_level";// 事件影响程度
    public static final String TARGET_RESOLVE_DATE = "target_resolve_date";// 目标解决时间

    public static final String INIT_FIRST_LEVEL = "init_first_level";// 事件初始类型一级
    public static final String INIT_SECOND_LEVEL = "init_second_level";// 事件初始类型二级
    public static final String INIT_THREE_LEVEL = "init_three_level";// 事件初始类型三级

    public static final String FINAL_FIRST_LEVEL = "final_first_level";// 事件最终类型一级
    public static final String FINAL_SECOND_LEVEL = "final_second_level";// 事件最终类型二级
    public static final String FINAL_THREE_LEVEL = "final_three_level";// 事件最终    类型三级

    public static final String EVENT_CATEGORY = "event_category";// 类别
    public static final String EVENT_SUBCLASS = "event_subclass";// 子类
    public static final String EVENT_ENTRY = "event_entry";// 条目
    public static final String EVENT_SUBENTRY1 = "event_subentry1";// 子条目一
    public static final String EVENT_SUBENTRY2 = "event_subentry2";// 子条目二

    public static final String EVENT_SOURCE_SYSTEM = "event_source_system";// 事件源系统
    public static final String RELATIONAL_SYSTEM = "relational_system";// 关联系统

    public static final String EVENT_SOURCE = "event_source";// 事件来源
    public static final String EVENT_PRIORITY = "event_priority";// 优先级
    public static final String INF_FACE = "inf_face";// 影响面
    public static final String EVENT_LEVEL = "event_level";// 事件级别
    public static final String EVENT_REASON_CATEGORY = "event_reason_category";// 事件原因
    public static final String CLOSE_CODE = "close_code";// 关闭代码

    public static final String CREATED_BY = "created_by";// 创建人
    public static final String CREATED_TIME = "created_time";// 创建时间

    public static final String EVENT_SOLVE_LABEL_ID = "el-tab-pane-5KiAPkeRqP";// 事件表单json中的事件解决标签id

    public static final String EVENT_ADMIN_ROLE_ID = "9305";// 事件管理员
    public static final String EVENT_SUSPEND_ROLE_ID = "9306";// 事件挂起人员

    public static final String RECEPTION_GROUP_NAME = "服务台值班组";// 服务台值班组名称


    public static String convertParaList(List<PubParaValue> list, String value) {
        String name = "";
        for(PubParaValue p : list) {
            if(value.equals(p.getValue())) {
                name = p.getValueDetail();
            }
        }
        return name;
    }

    public static String convertFinanceFlag(String code) {
        String info = "";
        switch (code) {
            case "1":
                info = "是";
                break;
            case "0":
                info = "否";
                break;
        }
        return info;
    }
}
