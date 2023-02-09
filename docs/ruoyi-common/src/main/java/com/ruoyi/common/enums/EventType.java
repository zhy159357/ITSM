package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间类型
 * @author dayong_sun
 */
public enum EventType
{
    /**
     * 监控事件单
     */
    MONITORING("0", "监控事件单"),
    /**
     * 业务事件单
     */
    BUSINESS("1", "业务事件单");

    private final String code;
    private final String info;

    EventType(String code, String info)
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
        EventType[] allEnums = values();
        for (EventType allEnum : allEnums) {
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
        EventType[] allEnums = values();
        for (EventType allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }
}
