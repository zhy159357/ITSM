package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysUpgradForm;

import java.util.List;

/**
 * 系统升级申请单
 */

public interface ISysUpgradFormService {
    //查询列表
    List<SysUpgradForm> selectSysUpgradFormList(SysUpgradForm sysUpgradForm);

    //新增
    int insertSysUpgradForm(SysUpgradForm sysUpgradForm);

    //根据id查询
    SysUpgradForm selectSysUpgradFormById(String id);

    //根据编号查询
    SysUpgradForm selectSysUpgradFormByCode(String sysNumber);

    //修改
    int updateSysUpgradForm(SysUpgradForm sysUpgradForm);

    //删除
    int deleteSysUpgradFormByIds(String ids);

    //查询编号
    int getByCount(String sysNumber);
}
