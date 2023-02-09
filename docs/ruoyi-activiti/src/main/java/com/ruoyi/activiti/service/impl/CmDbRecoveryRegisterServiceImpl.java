package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.CmDbRecoveryRegister;
import com.ruoyi.activiti.mapper.CmDbRecoveryRegisterMapper;
import com.ruoyi.activiti.service.ICmDbRecoveryRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
@Service
public class CmDbRecoveryRegisterServiceImpl implements ICmDbRecoveryRegisterService
{
    @Autowired
    private CmDbRecoveryRegisterMapper cmDbRecoveryRegisterMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询【请填写功能名称】
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CmDbRecoveryRegister selectCmDbRecoveryRegisterById(String recoveryRegisterId)
    {
        if("mysql".equals(dbType)){
            return cmDbRecoveryRegisterMapper.selectCmDbRecoveryRegisterByIdMysql(recoveryRegisterId);
        }else{
            return cmDbRecoveryRegisterMapper.selectCmDbRecoveryRegisterById(recoveryRegisterId);
        }
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cmDbRecoveryRegister 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CmDbRecoveryRegister> selectCmDbRecoveryRegisterList(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        if("mysql".equals(dbType)){
            return cmDbRecoveryRegisterMapper.selectCmDbRecoveryRegisterListMysql(cmDbRecoveryRegister);
        }else{
            return cmDbRecoveryRegisterMapper.selectCmDbRecoveryRegisterList(cmDbRecoveryRegister);
        }
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param cmDbRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCmDbRecoveryRegister(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        return cmDbRecoveryRegisterMapper.insertCmDbRecoveryRegister(cmDbRecoveryRegister);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param cmDbRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCmDbRecoveryRegister(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        return cmDbRecoveryRegisterMapper.updateCmDbRecoveryRegister(cmDbRecoveryRegister);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCmDbRecoveryRegisterByIds(String ids)
    {
        return cmDbRecoveryRegisterMapper.deleteCmDbRecoveryRegisterByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCmDbRecoveryRegisterById(String recoveryRegisterId)
    {
        return cmDbRecoveryRegisterMapper.deleteCmDbRecoveryRegisterById(recoveryRegisterId);
    }
}
