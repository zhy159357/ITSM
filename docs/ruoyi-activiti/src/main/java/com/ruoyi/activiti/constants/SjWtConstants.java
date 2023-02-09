package com.ruoyi.activiti.constants;

/**
 * 应用问题单常量
 */
public final class SjWtConstants {
    //我的标识  我创建的1  我处理过的2
    public static final String MYFLAG_CREAT="1";
    public static final String MYFLAG_DEAL="2";
    //业务复核状态码
    public static final String STATUS_YEWUFUHE="401";
    //问题单关联业务 业务码
    public static final String TRANSFERRED_YW="14";
    //问题单关联运行 业务码
    public static final String TRANSFERRED_EVENTRUN="13";
    //问题单关联数据变更 业务码
    public static final String TRANSFERRED_FMBIZ="16";
    //问题单 业务码
    public static final String TRANSFERRED_YHPC="15";
    //问题单关联资源变更
    public static final String TRANSFERRED_CMBIZ="04";
    //问题单运行状态码
    public static final String STATUS_DAITIJIAO="0";//待提交
    public static final String STATUS_DAIPINGGU="1";//待评估
    public static final String STATUS_DAISHENGHE="2";//待审核
    public static final String STATUS_DAIFENFA="3";//待分发
    public static final String STATUS_DAICHULI="5";//待技术处理
    public static final String STATUS_DAIYUJIEJUE="6";//待预解决
    public static final String STATUS_DAIYWSHOULI="7";//待业务受理
    public static final String STATUS_DAIJIEJUE="8";//待解决
    public static final String STATUS_DAIGUNABI="9";//待关闭
    public static final String STATUS_DAIEDIT="10";//待修改
    public static final String STATUS_CLOSE="11";//关闭
    /** 业务部门 */
    public static final String VM_DEPT = "vm_dept";
    //业务复核角色码
    public static final String ROLE_YWFUHE="2302";
    //业务受理
    public static final String ROLE_YWSHOULI="9986";
    //审核人角色码
    public static final String ROLE_SHENGHE="9983";
    //协同评估人角色码
    public static final String ROLE_PINGGU="9980";
    public static final String ROLE_PINGGU2="9987";
    //技术经理
    public static final String ROLE_JSJINGLI="9982";
    //问题经理
    public  static final String ROLE_WTJINGLI="9985";
    //问题分发角色
    public static final String ROLE_WTFENFA="9984";
    //问题单建设单位处理 解决  预解决 角色
    public static final String ROLE_INSIDE="0206";
    //岗位
    public static final String POST_DATACENTER="0010";
    //处理问题单
    public static final String DEAL_SHOULI="0";//受理
    public static final String DEAL_ZJISHU="1";//转技术处理
    public static final String DEAL_ZYEWU="2";//转业务
    public static final String DEAL_BACK="3";//退回
    public static final String DEAL_WTJINGLI="4";//转问题经理
    //应用处
    public static final String APPLY_ONE="4028aba3414f790e0141a53bb79a39b7";
    public static final String APPLY_TWO="4028aba3414f790e0141a53c367739d9";
    public static final String APPLY_THREE="40288a14276f110301276fb926de001c";
    public static final String APPLY_FOUR="ff8080816667bf72016685eb2fea33ba";
    //报表使用部门
    //总行信息科技部 3级机构
    public static final String ORG_ZHXXKJB_ID="4028c4852a274aeb012a2753d98a0025";
    //数据中心2   （包含 -丰台中心-合肥中心-亦庄中心 3级机构）
    public static final String ORG_DATACENTER_FENGTAI="40288a14276f110301276f7e5bc80016";
    public static final String ORG_DATACENTER_HEFEI="8b8080f3469537860146a7f6b585002c";
    public static final String ORG_DATACENTER_YIZHUNAG="8b8080f34b5a43ed014b775937565a6b";
    //金融科技创新部 3级
    public static final String FINANCE_SAT_ID="ff808081745387f801746cbb5cf846d8";
    //信用卡中心 4级
    public static final String CARD_CENTER_ID="4028c4852b1006c4012b1942a9e819d9";
    //管理信息部 3级
    public static final String MANAGE_INFO_ID="ff8080816d3d6497016d7aff3af655d1";
    //邮储总行软件研发中心 3级
    public static final String SOFTWARE_CENTER_ID="8b8080f34f155231014f1ba3c5dd502d";
    //总行信息科技部 3级机构
    public static final String ORG_ZHXXKJB="1";
    //数据中心  （包含 -丰台中心-合肥中心-亦庄中心 3级机构）
    public static final String ORG_DATACENTER="2";
    //金融科技创新部 3级
    public static final String FINANCE_SAT="3";
    //信用卡中心 4级
    public static final String CARD_CENTER="4";
    //管理信息部 3级
    public static final String MANAGE_INFO="5";
    //邮储总行软件研发中心 3级
    public static final String SOFTWARE_CENTER="6";
    //其他
    public static final String OTHER_ORG="7";
}
