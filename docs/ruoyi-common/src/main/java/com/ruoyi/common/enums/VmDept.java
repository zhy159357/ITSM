package com.ruoyi.common.enums;

/**
 * 兼容老系统vm_dept参数信息
 * @author 14735
 */
public enum VmDept {
    /**老系统生产环境vm_dept参数value-valueDetail值对应关系*/
    VM_DEPT_1("1", "总行个人金融部"),
    VM_DEPT_2("2", "总行三农金融部"),
    VM_DEPT_3("3", "总行网络金融部"),
    VM_DEPT_4("4", "总行公司金融部"),
    VM_DEPT_5("5", "总行小企业金融部"),
    VM_DEPT_6("6", "总行国际业务部"),
    VM_DEPT_7("7", "总行运营管理部"),
    VM_DEPT_8("8", "总行消费信贷部"),
    VM_DEPT_9("9", "总行资产负债部"),
    VM_DEPT_10("10", "总行审计局"),
    VM_DEPT_11("11", "总行金融市场部"),
    VM_DEPT_12("12", "总行财务管理部"),
    VM_DEPT_13("13", "总行战略发展部"),
    VM_DEPT_14("14", "总行战略客户部"),
    VM_DEPT_15("15", "总行授信管理部"),
    VM_DEPT_16("16", "总行风险管理部"),
    VM_DEPT_17("17", "总行法律事务部"),
    VM_DEPT_18("18", "总行托管业务部"),
    VM_DEPT_19("19", "总行资产管理部"),
    VM_DEPT_20("20", "信用卡中心"),
    VM_DEPT_21("21", "总行人力资源部"),
    VM_DEPT_22("22", "总行安全保卫部"),
    VM_DEPT_23("23", "总行工程建设部"),
    VM_DEPT_24("24", "总行监察部"),
    VM_DEPT_25("25", "邮储总行软件研发中心"),
    VM_DEPT_26("26", "总行资产保全部"),
    VM_DEPT_27("27", "邮储总行信息科技部"),
    VM_DEPT_28("28", "总行办公室"),
    VM_DEPT_29("29", "总行采购管理部"),
    VM_DEPT_30("30", "总行董事会办公室"),
    VM_DEPT_31("31", "总行监事会办公室"),
    VM_DEPT_32("32", "邮储总行管理信息部"),
    VM_DEPT_33("33", "总行内控合规部"),
    VM_DEPT_34("34", "总行投资银行部"),
    VM_DEPT_35("35", "总行信用审批部"),
    VM_DEPT_36("36", "总行金融科技创新部"),
    VM_DEPT_37("37", "总行交易银行部"),
    VM_DEPT_38("38", "总行工会"),
    VM_DEPT_39("39", "中邮金融消费有限公司"),
    VM_DEPT_40("40", "应用管理一处"),
    VM_DEPT_41("41", "总行金融同业部"),
    VM_DEPT_42("42", "总行总务部"),
    VM_DEPT_43("43", "总行代理金融管理部"),
    VM_DEPT_44("44", "中邮理财有限责任公司");

    private String num;
    private String orgName;

    VmDept(String num, String orgName){
        this.num = num;
        this.orgName = orgName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 根据数值key获取机构名称
     * @param num:数值key
     **/
    public static String getOrgName(String num) {
        for (VmDept e : values()) {
            if (e.getNum().equals(num)) {
                return e.getOrgName();
            }
        }
        return null;
    }
}
