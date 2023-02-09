package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.domain.RiskAssessment;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2022-08-05
 */
public interface IRiskAssessmentService {
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

    int updateCurrentLevel(String currentLevel,String formId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRiskAssessmentByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param formId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRiskAssessmentById(String formId);

    String getAssessManIdByChangeNo(String changeNo);

    void access(RiskAssessment riskAssessment);
}
