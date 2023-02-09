package com.ruoyi.form.service;

import com.ruoyi.form.domain.RiskAccessRecord;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-10-02
 */
public interface IRiskAccessRecordService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RiskAccessRecord selectRiskAccessRecordById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param riskAccessRecord 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RiskAccessRecord> selectRiskAccessRecordList(RiskAccessRecord riskAccessRecord);

    /**
     * 新增【请填写功能名称】
     * 
     * @param riskAccessRecord 【请填写功能名称】
     * @return 结果
     */
    public int insertRiskAccessRecord(RiskAccessRecord riskAccessRecord);

    /**
     * 修改【请填写功能名称】
     * 
     * @param riskAccessRecord 【请填写功能名称】
     * @return 结果
     */
    public int updateRiskAccessRecord(RiskAccessRecord riskAccessRecord);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRiskAccessRecordByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRiskAccessRecordById(Long id);
}
