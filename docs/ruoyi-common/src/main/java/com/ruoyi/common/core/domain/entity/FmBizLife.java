package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事件单效率对象 fm_biz_life
 *
 * @author ruoyi
 * @date 2021-01-21
 */
public class FmBizLife extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String fmLifeId;

    /**
     * 所属系统
     */
    private String sysId;

    /**
     * 单位级别 1公司 2系统
     */
    private String deptLev;

    /**
     * 剩余数量
     */
    @Excel(name = "剩余数量", sort = 3)
    private String surCount;

    /**
     * 8小时未处理数量
     */
    @Excel(name = "8小时未处理数量", sort = 5)
    private String eightNdealCount;

    /**
     * 8小时处理数量
     */
    @Excel(name = "8小时处理数量", sort = 4)
    private String eightDealCount;

    /**
     * 8小时处理效率
     */
    @Excel(name = "8小时处理效率", sort = 6)
    private String eightDealLife;

    /**
     * 16小时未处理数量
     */
    @Excel(name = "16小时未处理数量", sort = 8)
    private String sixteenNdealCount;

    /**
     * 16小时处理数量
     */
    @Excel(name = "16小时处理数量", sort = 7)
    private String sixteenDealCount;

    /**
     * 16小时处理效率
     */
    @Excel(name = "16小时处理效率", sort = 9)
    private String sixteenDealLife;

    /**
     * 24小时未处理数量
     */
    @Excel(name = "24小时未处理数量", sort = 11)
    private String ttfourNdealCount;

    /**
     * 24小时处理数量
     */
    @Excel(name = "24小时处理数量", sort = 10)
    private String ttfourDealCount;

    /**
     * 24小时处理效率
     */
    @Excel(name = "24小时处理效率", sort = 12)
    private String ttfourDealLife;

    /**
     * 统计时间
     */
    private String statisticalTime;

    /**
     * 所属公司
     */
    private String orgId;

    /**
     * 统计标识 1日常效率 2报表
     */
    private String flag;

    /**
     * 历史数据标志 1 当前数据 0 历史数据
     */
    private String invalidationMark;

    /**
     * 系统
     */
    @Excel(name = "所属系统", sort = 2)
    private String sysName;

    private String orgName;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setFmLifeId(String fmLifeId) {
        this.fmLifeId = fmLifeId;
    }

    public String getFmLifeId() {
        return fmLifeId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setDeptLev(String deptLev) {
        this.deptLev = deptLev;
    }

    public String getDeptLev() {
        return deptLev;
    }

    public void setSurCount(String surCount) {
        this.surCount = surCount;
    }

    public String getSurCount() {
        return surCount;
    }

    public void setEightNdealCount(String eightNdealCount) {
        this.eightNdealCount = eightNdealCount;
    }

    public String getEightNdealCount() {
        return eightNdealCount;
    }

    public void setEightDealCount(String eightDealCount) {
        this.eightDealCount = eightDealCount;
    }

    public String getEightDealCount() {
        return eightDealCount;
    }

    public void setEightDealLife(String eightDealLife) {
        this.eightDealLife = eightDealLife;
    }

    public String getEightDealLife() {
        return eightDealLife;
    }

    public void setSixteenNdealCount(String sixteenNdealCount) {
        this.sixteenNdealCount = sixteenNdealCount;
    }

    public String getSixteenNdealCount() {
        return sixteenNdealCount;
    }

    public void setSixteenDealCount(String sixteenDealCount) {
        this.sixteenDealCount = sixteenDealCount;
    }

    public String getSixteenDealCount() {
        return sixteenDealCount;
    }

    public void setSixteenDealLife(String sixteenDealLife) {
        this.sixteenDealLife = sixteenDealLife;
    }

    public String getSixteenDealLife() {
        return sixteenDealLife;
    }

    public void setTtfourNdealCount(String ttfourNdealCount) {
        this.ttfourNdealCount = ttfourNdealCount;
    }

    public String getTtfourNdealCount() {
        return ttfourNdealCount;
    }

    public void setTtfourDealCount(String ttfourDealCount) {
        this.ttfourDealCount = ttfourDealCount;
    }

    public String getTtfourDealCount() {
        return ttfourDealCount;
    }

    public void setTtfourDealLife(String ttfourDealLife) {
        this.ttfourDealLife = ttfourDealLife;
    }

    public String getTtfourDealLife() {
        return ttfourDealLife;
    }

    public void setStatisticalTime(String statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    public String getStatisticalTime() {
        return statisticalTime;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fmLifeId", getFmLifeId())
                .append("sysId", getSysId())
                .append("deptLev", getDeptLev())
                .append("surCount", getSurCount())
                .append("eightNdealCount", getEightNdealCount())
                .append("eightDealCount", getEightDealCount())
                .append("eightDealLife", getEightDealLife())
                .append("sixteenNdealCount", getSixteenNdealCount())
                .append("sixteenDealCount", getSixteenDealCount())
                .append("sixteenDealLife", getSixteenDealLife())
                .append("ttfourNdealCount", getTtfourNdealCount())
                .append("ttfourDealCount", getTtfourDealCount())
                .append("ttfourDealLife", getTtfourDealLife())
                .append("statisticalTime", getStatisticalTime())
                .append("orgId", getOrgId())
                .append("flag", getFlag())
                .append("invalidationMark", getInvalidationMark())
                .toString();
    }
}
