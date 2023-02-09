package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.CustInfo;
import com.ruoyi.common.core.domain.entity.FmBiz;
import org.springframework.stereotype.Service;

import java.util.List;
public interface CustInfoService {
    //查询客户信息列表
    List<CustInfo> checkCustInfo(CustInfo custInfo);
    //新增
    int insertCustInfo(CustInfo custInfo);
    //根据主键查询
    CustInfo getByCId(String cId);
    //根据客户名称、手机号查询
    CustInfo getByCustInfo(CustInfo custInfo);
    //根据主键id修改
    int updateById(CustInfo custInfo);
    //查询手机号是否重复
    int getByCount(String phone);
    //客户信息删除
    int deleteCustByIds(String ids);

}
