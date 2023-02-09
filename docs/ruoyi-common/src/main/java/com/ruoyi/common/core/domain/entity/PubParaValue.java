package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;

/**
 * 参数项值
 */
public class PubParaValue  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String  paraValueId;
    private String  paraId;
    private String  value;
    private String valueDetail;
    private Long  serail;
    private String paraExplain;

    public String getParaValueId() {
        return paraValueId;
    }

    public void setParaValueId(String paraValueId) {
        this.paraValueId = paraValueId;
    }

    public String getParaId() {
        return paraId;
    }

    public void setParaId(String paraId) {
        this.paraId = paraId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueDetail() {
        return valueDetail;
    }

    public void setValueDetail(String valueDetail) {
        this.valueDetail = valueDetail;
    }

    public Long getSerail() {
        return serail;
    }

    public void setSerail(Long serail) {
        this.serail = serail;
    }

    public String getParaExplain() {
        return paraExplain;
    }

    public void setParaExplain(String paraExplain) {
        this.paraExplain = paraExplain;
    }

    @Override
    public String toString() {
        return "PubParaValue{" +
                "  value='" + value + '\'' +
                ", valueDetail='" + valueDetail + '\'' +
                '}';
    }
}
