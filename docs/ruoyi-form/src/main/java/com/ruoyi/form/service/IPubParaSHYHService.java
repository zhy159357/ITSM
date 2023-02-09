package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.entity.PriorityParavalue;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.domain.entity.SystemManagementing;
import com.ruoyi.form.domain.PubParaSHYH;

import java.util.List;
import java.util.Map;

public interface IPubParaSHYHService {

    List<PubParaSHYH> selectPubParaList(PubParaSHYH pubPara);

    String checkParaNameUnique(PubParaSHYH pubPara);

    int insertPubPara(PubParaSHYH pubPara);

    PubParaSHYH selectPubParaById(String paraId);

    int updatePubParaById(PubParaSHYH pubPara);

    int deleteByParaId(String paraId);

    List<PubParaSHYH> selectPubParaCustomerList(PubParaSHYH pubParaSHYH);


    void importData(List<SystemManagementing> userList);

    List<PriorityParavalue> priorityData(String code);

    Map<String,String> selectOneById(String id);

    String selectPriorityParavalue(PriorityParavalue paravalue);

}
