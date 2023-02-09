package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.FmSwMb;

import java.util.List;

public interface IFmSwMbService {

    public int insertFmSwMb(FmSwMb fmSwMb);
    public List<FmSwMb> selectFmSwMbList(FmSwMb fmSwMb);

    int deleteFmSwMbByIds(String ids);

    FmSwMb selectById(String id);

    int updateFmSwMb(FmSwMb fmSwMb);

    Boolean checkExist(String dealOrgId, String faultKind);

    FmSwMb selectFmSwMbByDealOrgIdAndFaultKind(FmSwMb fmSwMb);


    List<FmSwMb> selectFmSwMbByDealOrgId(FmSwMb fmSwMb);

}
