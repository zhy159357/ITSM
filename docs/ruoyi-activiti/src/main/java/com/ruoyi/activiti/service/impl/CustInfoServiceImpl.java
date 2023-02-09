package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.CustInfoMapper;
import com.ruoyi.activiti.service.CustInfoService;
import com.ruoyi.common.core.domain.entity.CustInfo;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CustInfoServiceImpl implements CustInfoService {
    @Autowired
    private CustInfoMapper custInfoMapper;
    //查询客户信息
    @Override
    public List<CustInfo> checkCustInfo(CustInfo custInfo) {
        return custInfoMapper.checkCustInfo(custInfo);
    }
    //新增
    @Override
    public int insertCustInfo(CustInfo custInfo) {
        return custInfoMapper.insertCustInfo(custInfo);
    }
    //根据主键查询
    @Override
    public CustInfo getByCId(String cId) {
        return custInfoMapper.getByCId(cId);
    }
    //根据客户名称、手机号查询
    @Override
    public CustInfo getByCustInfo(CustInfo custInfo) {
        return custInfoMapper.getByCustInfo(custInfo);
    }
    //根据主键id修改
    @Override
    public int updateById(CustInfo custInfo) {
        return custInfoMapper.updateById(custInfo);
    }

    //查询手机号是否重复
    @Override
    public int getByCount(String phone) {
        return custInfoMapper.getByCount(phone);
    }

    @Override
    public int deleteCustByIds(String ids) {
        String[] list= Convert.toStrArray(",",ids);
        return custInfoMapper.deleteCustByIds(list);
    }

}
