package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName RedundancyFieldEnum
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/8/3 11:13
 */
@AllArgsConstructor
@Getter
public enum RedundancyFieldEnum {
    extra1("extra1", "业务表编号"),
    extra2("extra2", ""),
    extra3("extra3", ""),
    extra4("extra4", ""),
    extra5("extra5", ""),
    extra6("check_people", "");

    public String name;
    public String desc;


}
