package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.CmOsRecoveryRegister;
import com.ruoyi.activiti.mapper.CmOsRecoveryRegisterMapper;
import com.ruoyi.activiti.service.ICmOsRecoveryRegisterService;
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
public class CmOsRecoveryRegisterServiceImpl implements ICmOsRecoveryRegisterService
{
    @Autowired
    private CmOsRecoveryRegisterMapper cmOsRecoveryRegisterMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询【请填写功能名称】
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CmOsRecoveryRegister selectCmOsRecoveryRegisterById(String recoveryRegisterId)
    {
        if("mysql".equals(dbType)){
            return cmOsRecoveryRegisterMapper.selectCmOsRecoveryRegisterByIdMysql(recoveryRegisterId);
        }else{
            return cmOsRecoveryRegisterMapper.selectCmOsRecoveryRegisterById(recoveryRegisterId);
        }
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cmOsRecoveryRegister 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CmOsRecoveryRegister> selectCmOsRecoveryRegisterList(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        if("mysql".equals(dbType)){
            return cmOsRecoveryRegisterMapper.selectCmOsRecoveryRegisterListMysql(cmOsRecoveryRegister);
        }else{
            return cmOsRecoveryRegisterMapper.selectCmOsRecoveryRegisterList(cmOsRecoveryRegister);
        }
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param cmOsRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCmOsRecoveryRegister(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        return cmOsRecoveryRegisterMapper.insertCmOsRecoveryRegister(cmOsRecoveryRegister);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param cmOsRecoveryRegister 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCmOsRecoveryRegister(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        return cmOsRecoveryRegisterMapper.updateCmOsRecoveryRegister(cmOsRecoveryRegister);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCmOsRecoveryRegisterByIds(String ids)
    {
        return cmOsRecoveryRegisterMapper.deleteCmOsRecoveryRegisterByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param recoveryRegisterId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCmOsRecoveryRegisterById(String recoveryRegisterId)
    {
        return cmOsRecoveryRegisterMapper.deleteCmOsRecoveryRegisterById(recoveryRegisterId);
    }
}
