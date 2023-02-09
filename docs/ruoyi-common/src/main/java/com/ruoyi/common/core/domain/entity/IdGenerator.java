package com.ruoyi.common.core.domain.entity;

/**
 * 业务类型id生成
 * @author
 */
public class IdGenerator {
    private String id;
    private Long currentId;
    private String bizType;
    private String currentDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
