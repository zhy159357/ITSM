package com.ruoyi.activiti.constants;

/**
 *
 * 版本发布单相关状态常量类
 * @author 14735
 */
public class VersionStatusConstants {
    /*********版本发布版本状态码值start*********/
    /**待提交*/
    public static final String VERSION_STATUS_1 = "1";
    /**待安全审核*/
    public static final String VERSION_STATUS_2 = "2";
    /**待业务｜技术审核*/
    public static final String VERSION_STATUS_3 = "3";
    /**待业务审核*/
    public static final String VERSION_STATUS_4 = "4";
    /**待技术审核*/
    public static final String VERSION_STATUS_6 = "6";
    /**待业务主管审批*/
    public static final String VERSION_STATUS_7 = "7";
    /**待技术主管审批*/
    public static final String VERSION_STATUS_8 = "8";
    /**待版本审核*/
    public static final String VERSION_STATUS_9 = "9";
    /**待运维审核*/
    public static final String VERSION_STATUS_10 = "10";
    /**待发布审批*/
    public static final String VERSION_STATUS_11 = "11";
    /**已发布*/
    public static final String VERSION_STATUS_12 = "12";
    /**待紧急审批*/
    public static final String VERSION_STATUS_13 = "13";
    /**作废*/
    public static final String VERSION_STATUS_14 = "14";
    /*********版本发布状态码值end*********/

    /*********版本发布技术审核状态码值start*********/
    /**待技术审核*/
    public static final String TECHNOLOGY_STATUS_1 = "1";
    /**待技术主管审核*/
    public static final String TECHNOLOGY_STATUS_2 = "2";
    /**技术主管审核已通过*/
    public static final String TECHNOLOGY_STATUS_PASS = "TY";
    /*********版本发布技术审核状态码值end*********/

    /*********版本发布业务审核状态码值start*********/
    /**待业务审核*/
    public static final String BUSINESS_STATUS_1 = "1";
    /**待业务主管审核*/
    public static final String BUSINESS_STATUS_2 = "2";
    /**业务主管审核已通过*/
    public static final String BUSINESS_STATUS_PASS = "BY";
    /*********版本发布业务审核状态码值end*********/

    /**##############是否通过标识##############*/
    public static final String PASS_FLAG_1 = "1";
    public static final String PASS_FLAG_0 = "0";

    /**##############向未下载省发送短信##############*/
    public static final String MSG_FLAG_1 = "1";
    /**##############向未完成省发送短信##############*/
    public static final String MSG_FLAG_2 = "2";

    /**##############全国中心机构id##############*/
    public static final String QGZX_ORGID = "40288a14276f110301276f7dc7a60015";

    /**##############值班中心编码##############*/
    public static final String ZBZX_NO = "000000";
    /**##############长安通信编码##############*/
    public static final String ZBZX_CANO = "999999";

    /**##############白班编码##############*/
    public static final String DAYSHIFT_NO = "100000";

    /**##############夜班编码##############*/
    public static final String NIGHTSHIFT_NO = "200000";

    /**##############领导编码##############*/
    public static final String LEADER_NO = "666666";

    /**##############值班角色编码##############*/
    public static final String ZBJS_NO = "6203";
    /**##############值班角色##############*/
    public static final String ZBJS_ROLE = "6211";

    /**##############上传附件为升级结果填报截图标识##############*/
    public static final String VERSION_INFO_IMAGE = "versionInfoTaskImage";
}
