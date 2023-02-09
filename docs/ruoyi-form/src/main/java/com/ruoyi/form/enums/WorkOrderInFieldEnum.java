package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
@Getter
public enum WorkOrderInFieldEnum {
    incident_event_source_1("incident","event_source","1","电话(事件报备)"),
    incident_event_source_2("incident","event_source","2","柜面报障"),
    incident_event_source_3("incident","event_source","3","IM报障"),
    incident_event_source_4("incident","event_source","4","其他"),
    incident_event_source_5("incident","event_source","5","页面报障"),
    incident_event_source_6("incident","event_source","6","移动报障"),
    incident_event_source_7("incident","event_source","7","应急演练"),
    incident_event_source_8("incident","event_source","8","运营助手"),
    incident_event_source_9("incident","event_source","9","容量管理"),
    incident_event_source_10("incident","event_source","10","客服派单"),
    incident_event_source_11("incident","event_source","11","邮件保修"),
    incident_event_source_12("incident","event_source","12","客服报障"),
    incident_event_source_13("incident","event_source","13","日常检查"),
    incident_event_source_14("incident","event_source","14","监控告警"),
    incident_event_source_15("incident","event_source","15","电话"),
    incident_event_source_16("incident","event_source","16","PAD报障"),
    incident_complain_flag_1("incident","complain_flag","0","否"),
    incident_complain_flag_2("incident","complain_flag","1","是"),
    incident_urgent_flag_1("incident","urgent_flag","0","否"),
    incident_urgent_flag_2("incident","urgent_flag","1","是"),
    incident_finance_flag_1("incident","finance_flag","0","否"),
    incident_finance_flag_2("incident","finance_flag","1","是"),
    incident_change_flag_1("incident","change_flag","0","否"),
    incident_change_flag_2("incident","change_flag","1","是"),
    incident_mointor_invalid_1("incident","mointor_invalid","0","否"),
    incident_mointor_invalid_2("incident","mointor_invalid","1","是"),
    incident_inf_use_1("incident","inf_use","0","否"),
    incident_inf_use_2("incident","inf_use","1","是"),
    incident_solution_valid_flag_1("incident","solution_valid_flag","0","否"),
    incident_solution_valid_flag_2("incident","solution_valid_flag","1","是"),

    incident_suspend_flag_1("incident","suspend_flag","0","否"),
    incident_suspend_flag_2("incident","suspend_flag","1","是"),
    incident_urge_flag_1("incident","urge_flag","0","否"),
    incident_urge_flag_2("incident","urge_flag","1","是"),
    incident_service_back_flag_1("incident","service_back_falg","0","否"),
    incident_service_back_flag_2("incident","service_back_falg","1","是"),
    incident_add_msg_back_flag_1("incident","add_msg_back_falg","0","否"),
    incident_add_msg_back_flag_2("incident","add_msg_back_falg","1","是"),
    incident_cancel_flag_1("incident","cancel_falg","0","否"),
    incident_cancel_flag_2("incident","cancel_falg","1","是"),
    incident_back_completion_flag_1("incident","back_completion_flag","0","否"),
    incident_back_completion_flag_2("incident","back_completion_flag","1","是");


    private String code;
    private String field;
    private String value;
    private String desc;

    public static String getByCode(String code,String field,String value){
        for(WorkOrderInFieldEnum v:values()) {
            if(v.code.equals(code)&&v.field.equals(field)&v.value.equals(value)) {
                return v.desc;
            }
        }
        return null;
    }
}
