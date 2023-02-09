package com.ruoyi.activiti.constants;

/**
 * 任务类型枚举类型
 */
public enum  ProcessKeyConstants {

    fmbiz("FmBiz","业务事件单"), fmsw("FmSw","事务事件单"),fmyx("FmYx","运行事件单"),fmdd("FmDd","调度事件单"),pmyy("PMYY","应用问题单"),pmyh("PMYH","隐患排查单"), fmsjbg("FmSJBG","数据变更单"), cmzy("CmZy","资源变更单"), vmbn("VmBn","发布管理"), BGQQ("BGQQ","变更请求单");
    private final String code;
    private final String info;

    private ProcessKeyConstants(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }}
