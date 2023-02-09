package com.ruoyi.system.http.entegorserver.entity;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 上传审批启动发送类
 * @author 14735
 */
public class UploadMsg extends BaseEntity {

    private String action;

    private List<UploadApproval> demand;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<UploadApproval> getDemand() {
        return demand;
    }

    public void setDemand(List<UploadApproval> demand) {
        this.demand = demand;
    }
}
