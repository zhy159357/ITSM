package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;

public class OgUserPost  implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String postId;
    private String postName;
    private String memo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
