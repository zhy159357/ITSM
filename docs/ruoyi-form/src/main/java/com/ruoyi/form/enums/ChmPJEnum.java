package com.ruoyi.form.enums;

public enum ChmPJEnum {
    ONE("1","优"),
    TWO("2","良"),
    THREE("3","中")
    ;

    private final String code;
    private final String info;


    ChmPJEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据code获取对应属性
     * @param code
     * @return
     */
    public static String getInfo(String code){
        for (ChmPJEnum genderEnum : ChmPJEnum.values()){
            if (genderEnum.getCode().equals(code)){
                return genderEnum.getInfo();
            }
        }
        return null;
    }

    /**
     * 根据属性获取对应状态码
     * @param info
     * @return
     */
    public static String getCode(String info){
        for (ChmPJEnum genderEnum : ChmPJEnum.values()){
            if (genderEnum.getInfo().equals(info)){
                return genderEnum.getCode();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
