package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.RiskAssessment;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-05
 */
public interface RiskAssessmentMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param formId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RiskAssessment selectRiskAssessmentById(String formId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param riskAssessment 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RiskAssessment> selectRiskAssessmentList(RiskAssessment riskAssessment);

    /**
     * 新增【请填写功能名称】
     *
     * @param riskAssessment 【请填写功能名称】
     * @return 结果
     */
    public int insertRiskAssessment(RiskAssessment riskAssessment);

    /**
     * 修改【请填写功能名称】
     *
     * @param riskAssessment 【请填写功能名称】
     * @return 结果
     */
    public int updateRiskAssessment(RiskAssessment riskAssessment);

    /**
     * 删除【请填写功能名称】
     *
     * @param formId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRiskAssessmentById(String formId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param formIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRiskAssessmentByIds(String[] formIds);
}
