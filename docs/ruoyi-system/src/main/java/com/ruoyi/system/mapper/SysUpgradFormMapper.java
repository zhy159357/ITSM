package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysUpgradForm;

import java.util.List;

/**
 * 系统升级申请单  数据层
 */
public interface SysUpgradFormMapper {

    //查询列表
    List<SysUpgradForm> selectSysUpgradFormList(SysUpgradForm sysUpgradForm);
    //新增
    int insertSysUpgradForm(SysUpgradForm sysUpgradForm);

    //根据id查询
    SysUpgradForm selectSysUpgradFormById(String id);

    //根编号查询
    SysUpgradForm selectSysUpgradFormByCode(String sysNumber);

    //修改
    int updateSysUpgradForm(SysUpgradForm sysUpgradForm);

    //删除
    int deleteSysUpgradFormByIds(String[] ids);

    //根据编号查询数量
    int getByCount(String sysNumber);
}
