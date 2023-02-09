package com.ruoyi.es.constant;

/**
 * 通用常量信息
 * @author dayong_sun
 */
public class PostConstants {

	//超级管理  = admin用户 = 0001
    public static final String SUPERADMIN = "0001";
    //一线厂商（暂无）
    //public static final String ONEFIRM = ""
    //二线厂商 = 厂商人员 = 0002
    public static final String TWOFIRM = "0002";
    //二线管理员 = 数据中心人员 =0010
    public static final String TWOADMIN = "0010";
    //一线管理员 = 5602
    public static final String ONEADMIN = "5602";
    //数据中心领导 = 0012
    public static final String SJZXLD = "0012";
    //值班管理员
    public static final String ZBGLY = "0100278";
    //草稿
    public static final String STATUS_ZERO = "0";
    //待审核
    public static final String STATUS_ONE = "1";
    //已发布
    public static final String STATUS_TWO = "2";
    //已下线(退回修改)
    public static final String STATUS_THREE = "3";
    //初审通过
    public static final String STATUS_FOUR = "4";
    //待下线-待修改
    public static final String STATUS_FIVE = "5";
    //下线初审通过-修改初审通过
    public static final String STATUS_SIX = "6";
    //待作废
    public static final String STATUS_SEVEN = "7";
    //作废初审通过
    public static final String STATUS_EIGHT = "8";
    //已作废
    public static final String STATUS_NINE = "9";

    //暂存
    public static final String OPERATION_ZERO = "0";
    //提交
    public static final String OPERATION_ONE = "1";
    //初审通过
    public static final String OPERATION_TWO = "2";
    //初审拒绝
    public static final String OPERATION_THREE = "3";
    //申请下线-申请修改
    public static final String OPERATION_FOUR = "4";
    //申请作废
    public static final String OPERATION_FIVE = "5";
    //删除
    public static final String OPERATION_SIX = "6";
    //
    public static final String OPERATION_SEVEN = "7";
    //初审同意下线-申请同意修改
    public static final String OPERATION_EIGHT = "8";
    //初审拒绝下线-初审拒绝修改
    public static final String OPERATION_NINE = "9";
    //初审同意作废
    public static final String OPERATION_TEN = "10";
    //初审拒绝作废
    public static final String OPERATION_ELEVEN = "11";
    //综审通过
    public static final String OPERATION_TWELVE = "12";
    //综审拒绝
    public static final String OPERATION_THIRTEEN = "13";
    //综审同意下线-综审同意修改
    public static final String OPERATION_FOURTEEN = "14";
    //综审拒绝下线-综审拒绝修改
    public static final String OPERATION_FIFTEEN = "15";
    //综审同意作废
    public static final String OPERATION_SIXTEEN = "16";
    //综审拒绝作废
    public static final String OPERATION_SEVENTEEN = "17";
    //强制下线-强制修改
    public static final String OPERATION_EIGHTEEN = "18";
    //强制作废
    public static final String OPERATION_NINETEEN = "19";

    /** 机构编码常量 */
    //todo 提交代码前，更换正式环境编码
    //丰台动力机构编码
    public static final String FENGTAI_DONGLI = "010107888";
    //合肥动力机构编码
    public static final String HEFEI_DONGLI = "010302888";

    /**岗位常量*/
    //todo 提交代码前，更换正式环境岗位编码
    //数据中心处长岗位ID
    public static final String SJZX_CZ = "0011";
    //机房出入人员岗位
    public static final String JF_CRRY = "1502";
    //机房岗位-动力处
    public static final String JF_DLG = "1503";
}