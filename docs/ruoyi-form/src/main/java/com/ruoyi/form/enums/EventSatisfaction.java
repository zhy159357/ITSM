package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventSatisfaction {
    /**
     * 满意度（最低为1星）
     * null：0星
     * one：1星
     * two：2星
     * there：3星
     * four：4星
     * five：5星
     */
    Null("null", "0星"),
    one("one", "1星"),
    two("two", "2星"),
    there("there", "3星"),
    four("Closed", "4星"),
    five("five", "5星");

    private String code;
    private String info;
}
