package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;

/**
 * 参数管理参数表
 */
public class PubPara   implements Serializable {


    private static final long serialVersionUID = 1L;

    private String paraId;

    private String paraName;

    private String paraExplain;

    private String state;

    private String updateFlag;

    public String getParaId() {
        return paraId;
    }

    public void setParaId(String paraId) {
        this.paraId = paraId;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getParaExplain() {
        return paraExplain;
    }

    public void setParaExplain(String paraExplain) {
        this.paraExplain = paraExplain;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }
}
