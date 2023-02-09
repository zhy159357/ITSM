package com.ruoyi.form.enums;

public enum changeTypeEnum {
    ONE("1","普通变更"),
    TWO("2","紧急变更"),
    THREE("3","评审变更")
    ;

    private final String code;
    private final String info;


    changeTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据code获取对应属性
     * @param code
     * @return
     */
    public static String getInfo(String code){
        for (changeTypeEnum genderEnum : changeTypeEnum.values()){
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
        for (changeTypeEnum genderEnum : changeTypeEnum.values()){
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
