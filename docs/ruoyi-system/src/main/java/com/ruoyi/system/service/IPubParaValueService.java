package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;

import java.util.List;
import java.util.Map;

/**
 * @author TR
 */
public interface IPubParaValueService {

    List<PubParaValue> selectParaValueList(PubParaValue pubParaValue);

    List<PubParaValue> selectPubParaValueById(String paraId);

    int insertParaValueData(PubParaValue pubParaValue);

    PubParaValue selectParaValueDataById(String paraValueId);

    int updateParaValueData(PubParaValue pubParaValue);

    int deleteParaValueDataByIds(PubParaValue pubParaValue);

    List<PubParaValue> selectPubParaValueByParaName(String paraName);

    String selectPubParaValueByParaNameApp(String paraName, String value);

    List<PubParaValue> selectPubParaValueByVersionType(String[] vs);

    /**
     * 字典翻译  入参 描述paraName+码值paravalue
     * @param
     * @return
     */
    public String selectPubParaValueByNameValue(String paraName,String paraValue);

    /**
     * 跳板机查询业务审核人｜业务主管审核人
     * @param map
     * @return
     */
    public List<PubParaValue> selectOrgNameByParaValue(Map map);

    PubParaValue selectParaPaValueDetail(PubParaValue pubParaValue);

    /**
     * 根据字典名称 码值 查询子集 元素
     * @param paraName
     * @param value
     * @return
     */
    public PubParaValue selectPubParaValue(String paraName,String value);


    /**
     * 查询参数接口
     * @param paraName
     * @return
     */
    List<LabelValue> selectPubParaValueForTinyWeb(String paraName);
}
