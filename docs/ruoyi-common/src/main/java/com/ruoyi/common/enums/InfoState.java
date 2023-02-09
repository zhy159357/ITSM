package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum InfoState {

    /*
     * 状态
     */
    FORMAL("0", "正式"),
    TESTRUN("1", "试行");

    private final String code;
    private final String info;

    InfoState(String code, String info)
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
        InfoState[] allEnums = values();
        for (InfoState allEnum : allEnums) {
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
        InfoState[] allEnums = values();
        for (InfoState allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
