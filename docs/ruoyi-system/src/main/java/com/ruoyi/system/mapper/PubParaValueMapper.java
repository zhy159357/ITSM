package com.ruoyi.system.mapper;


import com.ruoyi.common.core.domain.entity.PubParaValue;

import java.util.List;
import java.util.Map;

public interface PubParaValueMapper {

    List<PubParaValue> selectParaValueList(PubParaValue pubParaValue);

    List<PubParaValue> selectPubParaValueById(String paraId);

    int insertParaValueData(PubParaValue pubParaValue);

    PubParaValue selectParaValueDataById(String paraValueId);

    int updateParaValueData(PubParaValue pubParaValue);

    int deleteParaValueDataByIds(PubParaValue pubParaValue);

    List<PubParaValue> selectPubParaValueByParaName(String paraName);

    List<PubParaValue> selectPubParaValueByVersionType(String[] ids);

    PubParaValue selectParaPaValueDetail(PubParaValue pubParaValue);

    List<PubParaValue> selectOrgNameByParaValue(Map map);

    String selectPubParaValueByNameValue(Map<String, String> map);

    PubParaValue selectParaPaValue(Map<String, String> map);

    //优先级
    List<PubParaValue> priority(String code);

    List<Map<String,String>> selectAllParams();

}
