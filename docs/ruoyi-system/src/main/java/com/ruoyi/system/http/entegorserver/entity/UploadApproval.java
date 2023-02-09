package com.ruoyi.system.http.entegorserver.entity;

/**
 * 上传审批启动
 */
public class UploadApproval {
    /**该单的唯一标识*/
    private String taskid;
    /**变更说明*/
    private String changedes;
    /**为上传的excel名称*/
    private String excelname;
    /**配置读取文件路径*/
    private String excelpath;
    /**指令类型默认是1*/
    private String tasktype;
    /**任务发起人（可不填写）*/
    private String startuser;
    /**任务审核人（可不填写）*/
    private String checkuser;
    /**是否检查包名（可不填写，默认为true） true为检查，flase为不检查*/
    private boolean isCheckPkgname = true;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getChangedes() {
        return changedes;
    }

    public void setChangedes(String changedes) {
        this.changedes = changedes;
    }

    public String getExcelname() {
        return excelname;
    }

    public void setExcelname(String excelname) {
        this.excelname = excelname;
    }

    public String getExcelpath() {
        return excelpath;
    }

    public void setExcelpath(String excelpath) {
        this.excelpath = excelpath;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getStartuser() {
        return startuser;
    }

    public void setStartuser(String startuser) {
        this.startuser = startuser;
    }

    public String getCheckuser() {
        return checkuser;
    }

    public void setCheckuser(String checkuser) {
        this.checkuser = checkuser;
    }

    public boolean getIsCheckPkgname() {
        return isCheckPkgname;
    }

    public void setIsCheckPkgname(boolean isCheckPkgname) {
        this.isCheckPkgname = isCheckPkgname;
    }
}
