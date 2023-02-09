package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum DeleteRea {

    /*
     * 废止原因
     */
    REVISE("0", "修订"),
    ABOLISH("1", "废止");

    private final String code;
    private final String info;

    DeleteRea(String code, String info)
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
        DeleteRea[] allEnums = values();
        for (DeleteRea allEnum : allEnums) {
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
        DeleteRea[] allEnums = values();
        for (DeleteRea allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
