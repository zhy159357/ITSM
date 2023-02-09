package com.ruoyi.form.constants;

public class EventFlowConstants {

    /*****  事件单流程activiti配置节点el表达式中的key  *******/
    public static final String EVENT_FLOW_NODE_KEY_ASSIGN = "receptionId";
    public static final String EVENT_FLOW_NODE_KEY_RECEIVE = "reveiveGroupId";
    public static final String EVENT_FLOW_NODE_KEY_DEAL = "dealId";
    public static final String EVENT_FLOW_NODE_KEY_PRE_SOLUTION = "preSolutionId";
    public static final String EVENT_FLOW_NODE_KEY_SOLUTION = "solutionId";
    public static final String EVENT_FLOW_NODE_KEY_CLOSE = "closeId";
    public static final String EVENT_FLOW_NODE_KEY_BACK_CREATE = "startUserId";
    /******  事件单流程activiti配置节点el表达式中的key  *******/

    /******事件单流程activiti分支条件表达式中的key *******/
    public static final String EVENT_FLOW_BRANCH_KEY = "reCode";
    public static final String EVENT_FLOW_BRANCH_MINUS2 = "-2";
    public static final String EVENT_FLOW_BRANCH_MINUS1 = "-1";
    public static final String EVENT_FLOW_BRANCH_ZERO = "0";
    public static final String EVENT_FLOW_BRANCH_POSITIVE1 = "1";
    public static final String EVENT_FLOW_BRANCH_POSITIVE2 = "2";

    /******事件单页面事件类型及分类存储在og_typeinfo表中的编号key *******/
    public static final String EVENT_TYPE_NO = "BankOfShangHaiEventLevel";
    public static final String EVENT_CATEGORY_NO = "EventCategory";

    /******事件单退回补全标识 *******/
    public static final String BACK_ONE_LINE_BACK = "A";// 一线退回补全
    public static final String BACK_TWO_LINE_BACK = "B";// 二线退回补全
}
