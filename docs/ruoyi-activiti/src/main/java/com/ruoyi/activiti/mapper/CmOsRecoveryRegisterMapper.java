package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.CmOsRecoveryRegister;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
public interface CmOsRecoveryRegisterMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CmOsRecoveryRegister selectCmOsRecoveryRegisterById(String recoveryRegisterId);

    public CmOsRecoveryRegister selectCmOsRecoveryRegisterByIdMysql(String recoveryRegisterId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cmOsRecoveryRegister 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CmOsRecoveryRegister> selectCmOsRecoveryRegisterList(CmOsRecoveryRegister cmOsRecoveryRegister);

    public List<CmOsRecoveryRegister> selectCmOsRecoveryRegisterListMysql(CmOsRecoveryRegister cmOsRecoveryRegister);

    /**
     * 新增【请填写功能名称】
     * 
     * @param cmOsRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    public int insertCmOsRecoveryRegister(CmOsRecoveryRegister cmOsRecoveryRegister);

    /**
     * 修改【请填写功能名称】
     * 
     * @param cmOsRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    public int updateCmOsRecoveryRegister(CmOsRecoveryRegister cmOsRecoveryRegister);

    /**
     * 删除【请填写功能名称】
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCmOsRecoveryRegisterById(String recoveryRegisterId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param recoveryRegisterIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmOsRecoveryRegisterByIds(String[] recoveryRegisterIds);
}
