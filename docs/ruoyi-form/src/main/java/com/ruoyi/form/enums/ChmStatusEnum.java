package com.ruoyi.form.enums;

public enum ChmStatusEnum {
    ONE("0","待提交"),
    TWO("1","已提交"),
    THREE("2","进行中"),
    FOURE("3","已转事件单"),
    FIVE("4","退回待分派"),
    SIX("5","待复核"),
    SEVEN("6","已关闭")
    ;

    private final String code;
    private final String info;


    ChmStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据code获取对应属性
     * @param code
     * @return
     */
    public static String getInfo(String code){
        for (ChmStatusEnum genderEnum : ChmStatusEnum.values()){
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
        for (ChmStatusEnum genderEnum : ChmStatusEnum.values()){
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
