package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 类别数字字典
 * @author dayong_sun
 */
public enum TypeinfoNumber {

    ONE("1", "1"),
    TWO("2", "2"),
    three("3", "3"),
    FOUR("4", "4"),
    FIVE("5", "5"),
    SIX("6", "6"),
    SEVEN("7", "7"),
    EIGHT("8", "8"),
    NINE("9", "9"),
    TEN("10","10"),
    ELEVEN("11","11"),
    TWELVE("12","12"),
    THIRTEEN("13","13"),
    FOURTEEN("14","14"),
    FIFTEEN("15","15"),
    SIXTEEN("16","16"),
    SEVENTEEN("17","17"),
    EIGHTEEN("18","18"),
    NINETEEN("19","19"),
    TWENTY("20","20"),
    TWENTYOND("21","21"),
    TWENTYTWO("22","22"),
    TWENTYTHREE("23","23"),
    TWENTYFOUR("24","24"),
    TWENTYFIVE("25","25"),
    TWENTYSEX("26","26"),
    TWENTYSEVEN("27","27"),
    TWENTYEIGHT("28","28"),
    TWENTYNINE("29","29");

    private final String code;
    private final String info;

    TypeinfoNumber(String code, String info)
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
        TypeinfoNumber[] allEnums = values();
        for (TypeinfoNumber allEnum : allEnums) {
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
        TypeinfoNumber[] allEnums = values();
        for (TypeinfoNumber allEnum : allEnums) {
            list.add(allEnum);
        }
        return list;
    }

}
