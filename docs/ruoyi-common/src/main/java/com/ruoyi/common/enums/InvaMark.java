package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum InvaMark {

    /*
     * 状态
     */
    MARK("0", "使用中"),
    NOMARK("1", "已停用");

    private final String code;
    private final String info;

    InvaMark(String code, String info)
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
        InvaMark[] allEnums = values();
        for (InvaMark allEnum : allEnums) {
            if (allEnum.getCode() == code) {
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
        InvaMark[] allEnums = values();
        for (InvaMark allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
