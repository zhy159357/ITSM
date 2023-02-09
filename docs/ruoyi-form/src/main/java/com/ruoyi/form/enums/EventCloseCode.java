package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventCloseCode {
    /**
     * 关闭代码
     * Successful-Resolved：1-成功解决
     * Template-Resolved：2-临时解决
     * UnSuccessful：3-不成功
     * Disappear：4-消失
     * Neglectable：5-可忽略
     * Cancle：6-撤销
     * Reject/Distort：7-拒绝/误报
     */
    Successful_Resolved("Successful-Resolved", "成功解决"),
    Template_Resolved("Template-Resolved", "临时解决"),
    UnSuccessful("UnSuccessful", "不成功"),
    Disappear("Disappear", "消失"),
    Neglectable("Neglectable", "可忽略"),
    Cancle("Cancle", "撤销"),
    Reject_Distort("Reject/Distort", "拒绝/误报");

    private String code;
    private String info;
}
