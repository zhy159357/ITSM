package com.ruoyi.form.openapi.vo;

import lombok.Data;

@Data
public class AdpmChangeTaskVO {
    private String referSys;
    private String wantImplTime;
    private String deployWay;
    private String mergeFlag;
    private String mergeTaskNo;
    private String branchId;
    private String devopsAppCode;
/*    private String deployName;*/
    private String name;
    private String path;
/*    private String deployPath;*/
    private String releaseVersion;
 /*   private String changeFlag;*/
/*    private String itsmChildNo;*/
/*    private String bhNo;*/
/*
    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
        if("1".equals(changeFlag)){
            this.mergeFlag = "0";
        }else if("2".equals(changeFlag)){
            this.mergeFlag = "1";
        }
    }

    public void setItsmChildNo(String itsmChildNo) {
        this.itsmChildNo = itsmChildNo;
        this.mergeTaskNo = itsmChildNo;
    }

    public void setBhNo(String bhNo) {
        this.bhNo = bhNo;
        this.branchId = bhNo;
    }*/
}
