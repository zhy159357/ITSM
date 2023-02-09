package com.ruoyi.form.mapper;

import com.ruoyi.common.core.domain.entity.SystemManagementing;
import com.ruoyi.form.domain.PubParaSHYH;

import java.util.List;

public interface PubParaSHYHMapper {

    List<PubParaSHYH> selectPubParaList(PubParaSHYH pubPara);

    PubParaSHYH checkParaNameUnique(String paraName);

    int insertPubPara(PubParaSHYH pubPara);

    PubParaSHYH selectPubParaById(String paraId);

    int updatePubParaById(PubParaSHYH pubPara);
    int deleteByParaId(String paraId);

    List<PubParaSHYH> selectPubParaCustomerList(PubParaSHYH pubParaSHYH);

    PubParaSHYH selectPubParaByparaName(String paraName);


}
