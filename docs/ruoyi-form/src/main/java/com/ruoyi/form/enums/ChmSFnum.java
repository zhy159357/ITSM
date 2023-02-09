package com.ruoyi.form.enums;

public enum ChmSFnum {
    ONE("1","单台设备"),
    TWO("2","多台设备"),
    THREE("3","网点所有设备")
    ;

    private final String code;
    private final String info;


    ChmSFnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据code获取对应属性
     * @param code
     * @return
     */
    public static String getInfo(String code){
        for (ChmSFnum genderEnum : ChmSFnum.values()){
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
        for (ChmSFnum genderEnum : ChmSFnum.values()){
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
