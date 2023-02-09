package com.ruoyi.form.enums;



/**
 * 关联关系类型
 */
public enum RelationRequestType {
    PROBLEM("1", "问题调查", "problem"),
    CHANGE("2", "基础设施变更", "change"),
    EVENT("3", "事件", "incident"),
    PROBLEM_TASK("4", "问题任务执行", "problem_task_new"),
    CHM("5","硬件报障", "chm");

    private String code;
    private String info;
    private String tableName;

    RelationRequestType(String code, String info, String tableName) {
        this.code = code;
        this.info = info;
        this.tableName = tableName;
    }

    /**
     * 根据编号获取名称
     *
     * @param code:编号
     **/
    public static String getName(String code) {
        RelationRequestType[] statges = values();
        for (RelationRequestType stage : statges) {
            if (code.equals(stage.getCode())) {
                return stage.getInfo();
            }
        }
        return null;
    }

    /**
     * 根据编号获取表名
     *
     * @param code:编号
     **/
    public static String getTableName(String code) {
        RelationRequestType[] statges = values();
        for (RelationRequestType stage : statges) {
            if (code.equals(stage.getCode())) {
                return stage.getTableName();
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }}
