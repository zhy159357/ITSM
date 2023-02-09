package com.ruoyi.common.core.domain.entity;

public class FmBizMaint {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String mId;

    /**
     * 事件标题
     */
    private String mTitle;

    /**
     * 事件描述
     */
    private String mDetail;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDetail() {
        return mDetail;
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }
}
