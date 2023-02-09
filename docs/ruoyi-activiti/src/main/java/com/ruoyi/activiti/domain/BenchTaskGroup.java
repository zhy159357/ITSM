package com.ruoyi.activiti.domain;


import java.io.Serializable;

/**
 * @author LiuPeng
 */
public class BenchTaskGroup implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6471626088298339530L;

    private long fmbizCount;//业务事件单数量
    private long cmsjCount;//数据变更单数量
    private long vmbnCount;//发布管理数量
    private long cmbizCount;//资源变更数量
    private long imbizCount;//问题单数量
    private long fmswCount;//事务单数量
    private long fmyxCount;//运行单数量
    private long cmqqCount;//变更请求单数量
    private long lxbgCount;//例行变更单数量
    private long fmddCount;//调度请求单数量
    private long sawoCount;//态势感知工单数量

    public long getSawoCount() {
        return sawoCount;
    }

    public void setSawoCount(long sawoCount) {
        this.sawoCount = sawoCount;
    }

    public long getFmddCount() {
        return fmddCount;
    }

    public void setFmddCount(long fmddCount) {
        this.fmddCount = fmddCount;
    }

    public long getFmbizCount() {
        return fmbizCount;
    }

    public void setFmbizCount(long fmbizCount) {
        this.fmbizCount = fmbizCount;
    }

    public long getCmsjCount() {
        return cmsjCount;
    }

    public void setCmsjCount(long cmsjCount) {
        this.cmsjCount = cmsjCount;
    }

    public long getVmbnCount() {
        return vmbnCount;
    }

    public void setVmbnCount(long vmbnCount) {
        this.vmbnCount = vmbnCount;
    }

    public long getCmbizCount() {
        return cmbizCount;
    }

    public void setCmbizCount(long cmbizCount) {
        this.cmbizCount = cmbizCount;
    }

    public long getImbizCount() {
        return imbizCount;
    }

    public void setImbizCount(long imbizCount) {
        this.imbizCount = imbizCount;
    }

    public long getFmswCount() {
        return fmswCount;
    }

    public void setFmswCount(long fmswCount) {
        this.fmswCount = fmswCount;
    }

    public long getFmyxCount() {
        return fmyxCount;
    }

    public void setFmyxCount(long fmyxCount) {
        this.fmyxCount = fmyxCount;
    }

    public long getCmqqCount() {
        return cmqqCount;
    }

    public void setCmqqCount(long cmqqCount) {
        this.cmqqCount = cmqqCount;
    }

    public long getLxbgCount() {
        return lxbgCount;
    }

    public void setLxbgCount(long lxbgCount) {
        this.lxbgCount = lxbgCount;
    }
}
