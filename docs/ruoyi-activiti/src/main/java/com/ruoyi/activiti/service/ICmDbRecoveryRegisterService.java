package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.CmDbRecoveryRegister;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
public interface ICmDbRecoveryRegisterService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CmDbRecoveryRegister selectCmDbRecoveryRegisterById(String recoveryRegisterId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cmDbRecoveryRegister 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CmDbRecoveryRegister> selectCmDbRecoveryRegisterList(CmDbRecoveryRegister cmDbRecoveryRegister);

    /**
     * 新增【请填写功能名称】
     * 
     * @param cmDbRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    public int insertCmDbRecoveryRegister(CmDbRecoveryRegister cmDbRecoveryRegister);

    /**
     * 修改【请填写功能名称】
     * 
     * @param cmDbRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    public int updateCmDbRecoveryRegister(CmDbRecoveryRegister cmDbRecoveryRegister);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmDbRecoveryRegisterByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCmDbRecoveryRegisterById(String recoveryRegisterId);
}
