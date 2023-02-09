package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.PubPara;

import java.util.List;

public interface IPubParaService {

    List<PubPara> selectPubParaList(PubPara pubPara);

    String checkParaNameUnique(PubPara pubPara);

    int insertPubPara(PubPara pubPara);

    PubPara selectPubParaById(String paraId);

    int updatePubParaById(PubPara pubPara);
    int deleteByParaId(String paraId);
}
