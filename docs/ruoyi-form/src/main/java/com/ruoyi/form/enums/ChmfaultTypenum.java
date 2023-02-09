package com.ruoyi.form.enums;

public enum ChmfaultTypenum {
    ONE("1", "硬件更换"),
   TWO("2", "硬件调试"),
    THREE("3","移位搬迁"),
    FOUR("4","项目实施"),
    FIVE("5", "安装开通"),
    SIX("6", "人为故障"),
    SEVEN("7", "软件调试"),
    EIGHT("8", "网络通信")
    ;

    private final String code;
    private final String info;


    ChmfaultTypenum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据code获取对应属性
     * @param code
     * @return
     */
    public static String getInfo(String code){
        for (ChmfaultTypenum genderEnum : ChmfaultTypenum.values()){
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
        for (ChmfaultTypenum genderEnum : ChmfaultTypenum.values()){
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
