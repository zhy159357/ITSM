package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.PriorityParavalue;
import com.ruoyi.common.core.domain.entity.PubPara;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.enums.PriorityParaValueEnum;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;
import com.ruoyi.system.mapper.PriorityParaValueMapper;
import com.ruoyi.system.mapper.PubParaMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.service.IPubParaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TR
 */
@Service("pubParaValue")
public class PubParaValueImpl implements IPubParaValueService {

    @Autowired
    private PubParaValueMapper pubParaValueMapper;

    @Autowired
    private PubParaMapper pubParaMapper;

    @Resource
    private PriorityParaValueMapper priorityParaValueMapper;

    @Override
    public List<PubParaValue> selectParaValueList(PubParaValue pubParaValue) {
        return pubParaValueMapper.selectParaValueList(pubParaValue);
    }

    @Override
    public List<PubParaValue> selectPubParaValueById(String paraId) {
        return pubParaValueMapper.selectPubParaValueById(paraId);
    }

    @Override
    public int insertParaValueData(PubParaValue pubParaValue) {
        PubPara para = null ;
        if(StringUtils.isNotEmpty(pubParaValue.getParaId())){
             // 通过子集 获取  父级对象信息
             para = pubParaMapper.selectPubParaById(pubParaValue.getParaId());

            CacheUtils.remove(DictUtils.getCacheName(),DictUtils.getCacheKey(para.getParaName()));
        }
          //   枚举  获得 影响范围 和影响 程度 code
        PriorityParaValueEnum priorityParaValueEnum = PriorityParaValueEnum.find(Constants.TEXT_PRIORITY);
             // 判断是否 新增 影响程度子集  用code 变量
        if (StringUtils.equals(para.getParaName(),priorityParaValueEnum.getDegreeCode()) ){



            //scope影响范围  查找子集
            List<PubParaValue> scopeList = pubParaValueMapper.priority(priorityParaValueEnum.getScopeCode());
                    //1*scope.子集数量
            for (PubParaValue paraValue : scopeList) {


                PriorityParavalue priorityParavalue = new PriorityParavalue();

                  priorityParavalue.setCode(Constants.TEXT_PRIORITY);
                // degeree影响程度 子集 id
                priorityParavalue.setPubParavalueIdDegree(pubParaValue.getParaValueId());
                //scope影响范围  子集  id
                priorityParavalue.setPubParavalueIdScope(paraValue.getParaValueId());
                //优先级表 ID
                priorityParavalue.setId(UUID.getUUIDStr());

                //  查询后 数据 新增
                priorityParaValueMapper.insertSave(priorityParavalue);




            }

            }else if (StringUtils.equals(para.getParaName(),priorityParaValueEnum.getScopeCode())) {

                     //  degree影响程度 查找 子集
            List<PubParaValue> degreeList = pubParaValueMapper.priority(priorityParaValueEnum.getDegreeCode());
            //  degree影响程度  遍历
            for (PubParaValue paraValueDe : degreeList) {

                PriorityParavalue priorityParavalue = new PriorityParavalue();

                priorityParavalue.setCode(Constants.TEXT_PRIORITY);
                // degeree影响程度 子集 id
                priorityParavalue.setPubParavalueIdDegree(paraValueDe.getParaValueId());
                //scope影响范围  子集  id
                priorityParavalue.setPubParavalueIdScope(pubParaValue.getParaValueId());
                //优先级表 ID
                priorityParavalue.setId(UUID.getUUIDStr());

                //  查询后 数据 新增
                priorityParaValueMapper.insertSave(priorityParavalue);



            }

        }



        return pubParaValueMapper.insertParaValueData(pubParaValue);
    }

    @Override
    public PubParaValue selectParaValueDataById(String paraValueId) {
        return pubParaValueMapper.selectParaValueDataById(paraValueId);
    }

    @Override
    public int updateParaValueData(PubParaValue pubParaValue) {
        if(StringUtils.isNotEmpty(pubParaValue.getParaId())){
            PubPara para = pubParaMapper.selectPubParaById(pubParaValue.getParaId());
            CacheUtils.remove(DictUtils.getCacheName(),DictUtils.getCacheKey(para.getParaName()));
        }
        return pubParaValueMapper.updateParaValueData(pubParaValue);
    }

    @Override
    public int deleteParaValueDataByIds(PubParaValue pubParaValue) {
        PubPara para =null;

        if(StringUtils.isNotEmpty(pubParaValue.getParaValueId())){

            //  value = yichang  valueDetail = "异常"
            PubParaValue value = pubParaValueMapper.selectParaValueDataById(pubParaValue.getParaValueId());
             para = pubParaMapper.selectPubParaById(value.getParaId());
            CacheUtils.remove(DictUtils.getCacheName(),DictUtils.getCacheKey(para.getParaName()));

        }

        //   枚举  获得 影响范围 和影响 程度 code
        PriorityParaValueEnum priorityParaValueEnum = PriorityParaValueEnum.find(Constants.TEXT_PRIORITY);
        // 删除子集 判断通过 code 是否 为  影响程度子集 是 删除 优先级关联
        if(StringUtils.equals(para.getParaName(),priorityParaValueEnum.getDegreeCode())){
            //  通过 影响范围code  查找 影响范围子集  1*影响范围子集数
            List<PubParaValue> paraValueList = pubParaValueMapper.priority(priorityParaValueEnum.getScopeCode());
            // 遍历  影响范围 子集 拿去到 Id 填入 对象当中
            for (PubParaValue paraValue : paraValueList) {

                    PriorityParavalue priorityParavalue = new PriorityParavalue();

                    priorityParavalue.setPubParavalueIdDegree(pubParaValue.getParaValueId()) ;

                    priorityParavalue.setPubParavalueIdScope(paraValue.getParaValueId());

                priorityParaValueMapper.deletePriorityParavalue(priorityParavalue);


        }
        }

        // 删除子集 判断通过 code 是否 为  影响范围子集 是 删除 优先级关联
        if(StringUtils.equals(para.getParaName(),priorityParaValueEnum.getScopeCode())){
            //  通过 影响程度code  查找 影响程度子集  1*影响程度子集数
            List<PubParaValue> paraValueListDe = pubParaValueMapper.priority(priorityParaValueEnum.getDegreeCode());
            // 遍历  影响程度子集 拿去到 Id 填入 对象当中
            for (PubParaValue paraValueDe : paraValueListDe) {

                PriorityParavalue priorityParavalue = new PriorityParavalue();
                // 影响范围 ID  1个
                priorityParavalue.setPubParavalueIdScope(pubParaValue.getParaValueId());

                //影响程度  ID  多个
                priorityParavalue.setPubParavalueIdDegree(paraValueDe.getParaValueId()) ;

                priorityParaValueMapper.deletePriorityParavalue(priorityParavalue);


            }
        }



        return pubParaValueMapper.deleteParaValueDataByIds(pubParaValue);
    }

    /**
     * 根据字典项名称查询字典列表，先走缓存，缓存没有去查库，然后再放入缓存
     *
     * @param paraName
     * @return
     */
    @Override
    public List<PubParaValue> selectPubParaValueByParaName(String paraName) {
        List<PubParaValue> paraValues = DictUtils.getParaValueCache(paraName);
        if (StringUtils.isNotEmpty(paraValues)) {
            return paraValues;
        }
        paraValues = pubParaValueMapper.selectPubParaValueByParaName(paraName);
        if (StringUtils.isNotEmpty(paraValues)) {
            DictUtils.setPubParaCache(paraName, paraValues);
            return paraValues;
        }
        return paraValues;
    }

    /**
     * app前端翻译字典方法，未匹配到返回null
     * @param paraName
     * @param value
     * @return
     */
    @Override
    public String selectPubParaValueByParaNameApp(String paraName, String value) {
        List<PubParaValue> pubParaValues = this.selectPubParaValueByParaName(paraName);
        for(PubParaValue pubParaValue : pubParaValues){
            if(value.equals(pubParaValue.getValue())){
                return pubParaValue.getValueDetail();
            }
        }
        return null;
    }

    @Override
    public List<PubParaValue> selectPubParaValueByVersionType(String[] vs) {
        return pubParaValueMapper.selectPubParaValueByVersionType(vs);
    }

    @Override
    public String selectPubParaValueByNameValue(String paraName, String paraValue) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(paraName)) {
            paraName = "";
        }
        if (StringUtils.isEmpty(paraValue)) {
            paraValue = "0";
        }
        map.put("paraName", paraName);
        map.put("paravalue", paraValue);
        String result = pubParaValueMapper.selectPubParaValueByNameValue(map);
        if (StringUtils.isEmpty(result)) {
            return "";
        }
        return result;

    }

    @Override
    public List<PubParaValue> selectOrgNameByParaValue(Map map) {
        return pubParaValueMapper.selectOrgNameByParaValue(map);
    }

    @Override
    public PubParaValue selectParaPaValueDetail(PubParaValue pubParaValue) {
        return pubParaValueMapper.selectParaPaValueDetail(pubParaValue);

    }
    /**
     * 根据字典名称 码值 查询子集 元素
     * @param paraName
     * @param value
     * @return
     */
    @Override
    public PubParaValue selectPubParaValue(String paraName,String value){
        if(StringUtils.isEmpty(paraName)||StringUtils.isEmpty(value)){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("paraName", paraName);
        map.put("paravalue", value);
        return pubParaValueMapper.selectParaPaValue(map);
    }

    /**
     * 查询参数接口
     * @param paraName
     * @return
     */
    @Override
    public List<LabelValue> selectPubParaValueForTinyWeb(String paraName) {

        List<PubParaValue> list = new ArrayList<>();
        List<LabelValue> labelValueList = new ArrayList<>();

        try {

            if (StringUtils.isNotEmpty(paraName)) {

                list = pubParaValueMapper.selectPubParaValueByParaName(paraName);
                if (!CollectionUtils.isEmpty(list)) {

                    for (PubParaValue paraValue : list) {

                        LabelValue labelValue = new LabelValue();
                        labelValue.setValue(paraValue.getValue());
                        labelValue.setLabel(paraValue.getValueDetail());

                        labelValueList.add(labelValue);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return labelValueList;
    }

}
