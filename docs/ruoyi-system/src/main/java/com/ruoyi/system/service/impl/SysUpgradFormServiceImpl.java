package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysUpgradForm;
import com.ruoyi.system.mapper.SysUpgradFormMapper;
import com.ruoyi.system.service.ISysUpgradFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 系统升级申请单
 */
@Service
public class SysUpgradFormServiceImpl implements ISysUpgradFormService {
    @Autowired
    private SysUpgradFormMapper sysUpgradFormMapper;

    /**
     * 查询列表
     * @param sysUpgradForm
     * @return
     */
    @Override
    public List<SysUpgradForm> selectSysUpgradFormList(SysUpgradForm sysUpgradForm) {
        return sysUpgradFormMapper.selectSysUpgradFormList(sysUpgradForm);
    }

    /**
     * 新增
     * @param sysUpgradForm
     * @return
     */
    @Override
    public int insertSysUpgradForm(SysUpgradForm sysUpgradForm) {
        return sysUpgradFormMapper.insertSysUpgradForm(sysUpgradForm);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public SysUpgradForm selectSysUpgradFormById(String id) {
        return sysUpgradFormMapper.selectSysUpgradFormById(id);
    }

    /**
     * 根据编号查询
     * @param sysNumber
     * @return
     */
    @Override
    public SysUpgradForm selectSysUpgradFormByCode(String sysNumber) {
        return sysUpgradFormMapper.selectSysUpgradFormByCode(sysNumber);
    }

    /**
     * 修改
     * @param sysUpgradForm
     * @return
     */
    @Override
    public int updateSysUpgradForm(SysUpgradForm sysUpgradForm) {
        return sysUpgradFormMapper.updateSysUpgradForm(sysUpgradForm);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @Override
    public int deleteSysUpgradFormByIds(String ids) {
        return sysUpgradFormMapper.deleteSysUpgradFormByIds(Convert.toStrArray(ids));
    }

    /**
     * 根据编号查询数量
     * @param sysNumber
     * @return
     */
    @Override
    public int getByCount(String sysNumber) {
        return sysUpgradFormMapper.getByCount(sysNumber);
    }


}
