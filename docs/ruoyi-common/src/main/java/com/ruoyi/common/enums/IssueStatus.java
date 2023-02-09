package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题单状态
 * @author dayong_sun
 */
public enum IssueStatus {

    SUBMITTED("0", "待提交"),
    REVIEWED("1", "待审核"),
    PENDING("2", "待处理"),
    MODIFICATED("3", "退回待修改"),
    PROCESSED("4", "已处理"),
    TOVOID("5", "作废");

    private final String code;
    private final String info;

    IssueStatus(String code, String info)
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
        IssueStatus[] allEnums = values();
        for (IssueStatus allEnum : allEnums) {
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
        IssueStatus[] allEnums = values();
        for (IssueStatus allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
