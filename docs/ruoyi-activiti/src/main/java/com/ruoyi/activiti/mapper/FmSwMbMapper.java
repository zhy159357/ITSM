package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.FmSwMb;

import java.util.List;

public interface FmSwMbMapper {

    public int insertFmSwMb(FmSwMb fmSwMb);

    public List<FmSwMb> selectFmSwMbList(FmSwMb fmSwMb);

    public List<FmSwMb> selectFmSwMbListMysql(FmSwMb fmSwMb);

    int deleteFmSwMbByIds(String[] ids);

    FmSwMb selectById(String id);

    int updateFmSwMb(FmSwMb fmSwMb);


    FmSwMb selectFmSwMbByDealOrgIdAndFaultKind(FmSwMb fmSwMb);

    List<FmSwMb> selectFmSwMbByDealOrgId(FmSwMb fmSwMb);
}
