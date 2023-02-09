package com.ruoyi.form.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.RiskAccessRecordMapper;
import com.ruoyi.form.domain.RiskAccessRecord;
import com.ruoyi.form.service.IRiskAccessRecordService;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-10-02
 */
@Service
public class RiskAccessRecordServiceImpl implements IRiskAccessRecordService 
{
    @Autowired
    private RiskAccessRecordMapper riskAccessRecordMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RiskAccessRecord selectRiskAccessRecordById(Long id)
    {
        return riskAccessRecordMapper.selectRiskAccessRecordById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param riskAccessRecord 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RiskAccessRecord> selectRiskAccessRecordList(RiskAccessRecord riskAccessRecord)
    {
        return riskAccessRecordMapper.selectRiskAccessRecordList(riskAccessRecord);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param riskAccessRecord 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRiskAccessRecord(RiskAccessRecord riskAccessRecord)
    {
        return riskAccessRecordMapper.insertRiskAccessRecord(riskAccessRecord);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param riskAccessRecord 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRiskAccessRecord(RiskAccessRecord riskAccessRecord)
    {
        return riskAccessRecordMapper.updateRiskAccessRecord(riskAccessRecord);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRiskAccessRecordByIds(String ids)
    {
        return riskAccessRecordMapper.deleteRiskAccessRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRiskAccessRecordById(Long id)
    {
        return riskAccessRecordMapper.deleteRiskAccessRecordById(id);
    }
}
