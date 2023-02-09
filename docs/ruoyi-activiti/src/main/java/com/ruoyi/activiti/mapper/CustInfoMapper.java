package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.CustInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustInfoMapper {
    /**
     * 查询客户信息
     * @param custInfo
     * @return
     */
    public List<CustInfo> checkCustInfo(CustInfo custInfo);

    //新增
    public int insertCustInfo(CustInfo custInfo);

    //更新
   // public int updateCustInfo(CustInfo custInfo);

    //根据主键信息查询
    public CustInfo getByCId(String cId);

    //根据客户名称、手机查询
    public CustInfo getByCustInfo(CustInfo custInfo);

    //根据主键id修改
    int updateById(CustInfo custInfo);

    //查询手机号是否重复
    int getByCount(String phone);
    //客户信息删除
    int deleteCustByIds(String[] ids);

    //根据手机号查询
    CustInfo checkPhone(String cPhone);
}
