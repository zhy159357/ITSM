package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 2021业务事件考核对象 fm_biz_kh
 *
 * @author ruoyi
 * @date 2021-03-15
 */
public class FmBizKh extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 系统id
     */
    private String sysid;

    @Excel(name = "系统", sort = 2)
    private String sysName;
    /**
     * 考核日期
     */
    @Excel(name = "考核年月", sort = 1)
    private String datetime;

    /**
     * 关闭事件单数量
     */
    @Excel(name = "关闭事件单数量", sort = 3)
    private String completecount;

    /**
     * 紧急事件单超时数量
     */
    @Excel(name = "紧急事件单超时数量", sort = 4)
    private String jjcount;

    /**
     * 非紧急事件单超时数量
     */
    @Excel(name = "非紧急事件单超时数量", sort = 5)
    private String fjjcount;

    /**
     * 满意率
     */
    @Excel(name = "满意率", sort = 6)
    private String satisfaction;

    /**
     * 不满意率
     */
    @Excel(name = "不满意率", sort = 7)
    private String unsatisfactory;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSysid() {
        return sysid;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setCompletecount(String completecount) {
        this.completecount = completecount;
    }

    public String getCompletecount() {
        return completecount;
    }

    public void setJjcount(String jjcount) {
        this.jjcount = jjcount;
    }

    public String getJjcount() {
        return jjcount;
    }

    public void setFjjcount(String fjjcount) {
        this.fjjcount = fjjcount;
    }

    public String getFjjcount() {
        return fjjcount;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setUnsatisfactory(String unsatisfactory) {
        this.unsatisfactory = unsatisfactory;
    }

    public String getUnsatisfactory() {
        return unsatisfactory;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sysid", getSysid())
                .append("datetime", getDatetime())
                .append("completecount", getCompletecount())
                .append("jjcount", getJjcount())
                .append("fjjcount", getFjjcount())
                .append("satisfaction", getSatisfaction())
                .append("unsatisfactory", getUnsatisfactory())
                .toString();
    }
}
