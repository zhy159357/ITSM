package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class OgRole extends BaseEntity {
    /** 角色id */
    @Excel(name = "角色ID")
    private String rid;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String rName;

    /** 失效标志 */
    @Excel(name = "状态",readConverterExp="1=启用,0=停止")
    private String invalidationMark;

    /** 备注 */
    @Excel(name = "备注")
    private String memo;

    /** 函数列表 */
    //@Excel(name = "函数列表")
    private String funclist;

    private String adder;
    private String addTime;
    private String moder;
    private String modTime;
    private String updatetime;

    private String postId;
    /** 岗位组 */
    private String[] postIds;

    /** 岗位名称 */
    private String postName;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String[] getPostIds() {
        return postIds;
    }

    public void setPostIds(String[] postIds) {
        this.postIds = postIds;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    /** 菜单组 */
    private Long[] menuIds;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFunclist() {
        return funclist;
    }

    public void setFunclist(String funclist) {
        this.funclist = funclist;
    }

    public String getAdder() {
        return adder;
    }

    public void setAdder(String adder) {
        this.adder = adder;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getModer() {
        return moder;
    }

    public void setModer(String moder) {
        this.moder = moder;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public Long[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
    }
}
