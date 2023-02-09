package com.ruoyi.form.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 【请填写功能名称】对象 risk_assessment
 *
 * @author ruoyi
 * @date 2022-08-05
 */
@Data
public class RiskAssessment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 变更单单号
     */
    @NotBlank
    private String formId;

    /**
     * 可能影响的系统数量
     */
    @Excel(name = "可能影响的系统数量")
    @NotNull
    private String infSysCount;

    /**
     * 影响在用业务中断的情况
     */
    @Excel(name = "影响在用业务中断的情况")
    @NotNull
    private String infFuncInterruptType;

    /**
     * 变更类别
     */
    @Excel(name = "变更类别")
    @NotNull
    private String changeType;

    /**
     * 变更规划耗时
     */
    @Excel(name = "变更规划耗时")
    @NotNull
    private String changePlanTime;

    /**
     * 变更实施耗时
     */
    @Excel(name = "变更实施耗时")
    @NotNull
    private String changeImplTime;

    /**
     * 回退耗时
     */
    @Excel(name = "回退耗时")
    @NotNull
    private String backTime;

    /**
     * 有无应急或退回方案
     */
    @Excel(name = "有无应急或退回方案")
    @NotNull
    private String backPlanFlag;

    /**
     * 实施所涉及的部室个数
     */
    @Excel(name = "实施所涉及的部室个数")
    @NotNull
    private String referDeptCount;

    /**
     * 软件所属变更模块
     */
    @Excel(name = "软件所属变更模块")
    private String softwareChangeType;

    /**
     * 应用所属变更模块
     */
    @Excel(name = "应用所属变更模块")
    private String appChangeType;

    /**
     * 涉及系统
     */
    @NotEmpty
    private String[] referSysList;

    @Excel(name = "涉及系统")
    private String referSys;
    /**
     * 风险级别
     */
    @Excel(name = "原始风险级别")
    private Integer riskLevel;

    /**
     * 风险级别
     */
    @Excel(name = "当前风险级别")
    private String currentLevel;
}
