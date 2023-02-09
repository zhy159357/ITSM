package com.ruoyi.es.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 类别数字字典
 * @author dayong_sun
 */
public enum MyThreeStatus {

    ZERO("0", "我创建的"),
    ONE("1", "待审核的"),
    TWO("2", "已审核的");

    private final String code;
    private final String info;

    MyThreeStatus(String code, String info)
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
        MyThreeStatus[] allEnums = values();
        for (MyThreeStatus allEnum : allEnums) {
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
        MyThreeStatus[] allEnums = values();
        for (MyThreeStatus allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
