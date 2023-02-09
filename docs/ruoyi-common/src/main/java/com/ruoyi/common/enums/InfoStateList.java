package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum InfoStateList {

    /*
     * 状态
     */
    FORMAL("0", "正式"),
    TESTRUN("1", "试行"),
    TESTRUN2("2", "新增"),
    TESTRUN3("3", "废止"),
    TESTRUN4("4", "修订"),
    TESTRUN5("5", "退回待审核"),
    TESTRUN6("6", "待删除"),
    TESTRUN7("7", "待废止"),
    TESTRUN8("8", "待审核");




    private final String code;
    private final String info;

    InfoStateList(String code, String info)
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
        InfoStateList[] allEnums = values();
        for (InfoStateList allEnum : allEnums) {
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
        InfoStateList[] allEnums = values();
        for (InfoStateList allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
