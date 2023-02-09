package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.PubPara;

import java.util.List;

public interface PubParaMapper {

    List<PubPara> selectPubParaList(PubPara pubPara);

    PubPara checkParaNameUnique(String paraName);

    int insertPubPara(PubPara pubPara);

    PubPara selectPubParaById(String paraId);

    int updatePubParaById(PubPara pubPara);
    int deleteByParaId(String paraId);
}
