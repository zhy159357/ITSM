package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 类别数字字典
 * @author dayong_sun
 */
public enum SubstituteStatus {

    ZERO("0", "待审核"),
    ONE("1", "已通过"),
    TWO("2", "未通过");

    private final String code;
    private final String info;

    SubstituteStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    /**
     * 根据编号获取名称
     * @param code:编号
     **/
    public static String getName(String code) {
        SubstituteStatus[] allEnums = values();
        for (SubstituteStatus allEnum : allEnums) {
            if (code.equals(allEnum.getCode())) {
                return allEnum.getInfo();
            }
        }
        return null;
    }

    /**
     * 获取枚举list
     **/
    public static List getList() {
        List list = new ArrayList();
        SubstituteStatus[] allEnums = values();
        for (SubstituteStatus allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
