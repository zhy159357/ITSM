package com.ruoyi.form.entity;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TwWorkOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 工单编号
     */
    @Excel(name = "工单编号")
    private String workNum;
    /**
     * 实例id
     */
    private String instanceId;
    /**
     * 审批机构
     */
    private String implementorOrgid;
    /**
     * 审批人机构名称
     */
    private String implementorName;
    /**
     * 审批人Id
     */
    private String implementorId;
    /**
     * 审批人名字
     */
    private String implementorPeopleName;
    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;
    /**
     * 应用系统
     */
    private String appSystem;

    /**
     * 工单类型：默认（环境搭建）
     */
    private String workType;

    /**
     * 工单状态：默认（新建）
     */
    private String workState;

    /**
     * 项目编号
     */
    private String projectNum;

    /**
     * 需求编号
     */
    private String demandNum;

    /**
     * 审批人
     */
    private String approver;

    /**
     * 接口人
     */
    private String contact;

    /**
     * 接口人Name
     */
    private String contactName;

    /**
     * 工单内容或描述
     */
    private String workContent;

    /**
     * 工单状态
     */
    @Excel(name = "工单状态")
    private String status;

    /**
     * 创建人id
     */
    private String createBy;

    /**
     * 创建人名字
     */
    private String createByName;

    /**
     * 创建时间
     */
    //  private String createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    // private String updateTime;
    /**
     * 审批结构 recode=0：退回,recode=1:通过
     */
    @TableField(exist = false)
    private Integer recode;

    /**
     * 备注
     */
    @TableField(exist = false)
    private String comment;

    @TableField(exist = false)
    private List<String> statusList;

    private String dealPersonId;

    private String dealPersonName;

}
