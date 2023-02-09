package com.ruoyi.form.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.PriorityParavalue;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.domain.entity.SystemManagementing;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.domain.PubParaSHYH;
import com.ruoyi.form.mapper.PubParaSHYHMapper;
import com.ruoyi.form.service.IPubParaSHYHService;
import com.ruoyi.system.enums.PriorityParaValueEnum;
import com.ruoyi.system.mapper.PriorityParaValueMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


@Service
public class PubParaSHYHServiceImpl implements IPubParaSHYHService {

    private static final Logger log = LoggerFactory.getLogger(PubParaSHYHServiceImpl.class);

    @Resource
    private PubParaSHYHMapper pubParaSHYHMapper;

    @Resource
    private PubParaValueMapper pubParaValueMapper;

    @Resource
    private PriorityParaValueMapper priorityParaValueMapper;

    /**
     * 是否开启字典缓存
     */
    @Value("${itsm.dictCache}")
    private boolean dictCache;

    /**
     * 项目启动时，加载所有字典项列表到缓存
     */
    @PostConstruct
    public void init() {
        log.debug("字典缓存配置开关[dictCache=" + dictCache + "],该项配置为true启动时加载字典项到redis");
        if (dictCache) {
            List<PubParaSHYH> pubParas = selectPubParaList(new PubParaSHYH());
            for (PubParaSHYH para : pubParas) {
                String paraName = para.getParaName();
                List<PubParaValue> values = pubParaValueMapper.selectPubParaValueByParaName(paraName);
                if (StringUtils.isNotEmpty(values)) {
                    DictUtils.setPubParaCache(paraName, values);
                }
            }
        }
    }

    @Override
    public List<PubParaSHYH> selectPubParaList(PubParaSHYH pubPara) {
        return pubParaSHYHMapper.selectPubParaList(pubPara);
    }

    @Override
    public String checkParaNameUnique(PubParaSHYH pubPara) {
        PubParaSHYH pub_para = pubParaSHYHMapper.checkParaNameUnique(pubPara.getParaName());
        if (StringUtils.isNotNull(pub_para)) {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    @Override
    public int insertPubPara(PubParaSHYH pubPara) {
        return pubParaSHYHMapper.insertPubPara(pubPara);
    }

    @Override
    public PubParaSHYH selectPubParaById(String paraId) {
        return pubParaSHYHMapper.selectPubParaById(paraId);
    }

    @Override
    public int updatePubParaById(PubParaSHYH pubPara) {
        return pubParaSHYHMapper.updatePubParaById(pubPara);
    }

    @Override
    public int deleteByParaId(String paraId) {
        return pubParaSHYHMapper.deleteByParaId(paraId);
    }

    @Override
    public List<PubParaSHYH> selectPubParaCustomerList(PubParaSHYH pubParaSHYH) {
        return pubParaSHYHMapper.selectPubParaCustomerList(pubParaSHYH);
    }


    /**
     * @author jiangfeng
     * @param userList
     * @date 2022-06-17
     */

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void importData(List<SystemManagementing> userList) {

          //判断是否为空文件
        if (!CollectionUtils.isEmpty(userList)) {
            //取出父级
            SystemManagementing systemManagementing = userList.get(0);

            //查数据库（paraName=para_name;）  没有--》插入
            PubParaSHYH pubParaSHYH = pubParaSHYHMapper.selectPubParaByparaName(systemManagementing.getParaName());

            //1新增父类id
            String uuid = UUID.getUUIDStr();
            if (pubParaSHYH == null) {
                //新增父类
                PubParaSHYH pubPara = new PubParaSHYH();
                pubPara.setParaName(systemManagementing.getParaName());
                pubPara.setParaExplain(systemManagementing.getParaExplain());

                pubPara.setParaId(uuid);
                pubPara.setState("1");

                pubParaSHYHMapper.insertPubPara(pubPara);

            }
            //处理子集
            Map<String, Integer> map = new HashMap<>();

            for (int i = 1; i < userList.size(); i++) {
                //根据本轮循环遍历到的索引值获取对应的集合元素
                String key = userList.get(i).getParaName();

                //1.不能有相同的code 从list.get(1)开始 map（code,null） put(code,+1)  map遍历 value>1 重复 失败

                //根据获取到的key拿到对应的value
                Integer value = map.get(key);

                if (value == null) {//之前这个字段没有出现过，次数还是Integer的默认值null
                    map.put(key, 1);//没有出现过，次数就设置为1

                } else {//value不是null走else
                    map.put(key, value + 1);//之前这个字符出现过，次数变为之前的次数+1
                }


            }

            // 是把map中的一对键值对key&value作为一个Entry<K,V>整体放入set

            Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

            Iterator<Map.Entry<String, Integer>> itr = entrySet.iterator();

            String message = "";

            while (itr.hasNext()) {

                //本轮遍历到的一个Entry对象
                Map.Entry<String, Integer> entry = itr.next();

                String keys = entry.getKey();
                Integer values = entry.getValue();//获取Entry中的value

                if (values > 1) {
                    message = message + "  " + keys;

                }

                if (StringUtils.isNotEmpty(message)) {
                    throw new BusinessException("导入失败，包含重复项:" + message);
                }

            }

                 //新增子集数据
            for (int i = 1; i < userList.size(); i++) {
                SystemManagementing sys =  userList.get(i);
                PubParaValue pubParaValue = new PubParaValue();

                pubParaValue.setParaValueId(UUID.getUUIDStr());
                pubParaValue.setValue(sys.getParaName());
                pubParaValue.setValueDetail(sys.getParaExplain());
                pubParaValue.setSerail(1L);
                pubParaValue.setParaId(pubParaSHYH == null?uuid:pubParaSHYH.getParaId());

               pubParaValueMapper.insertParaValueData(pubParaValue);

            }





        }
    }


    /**
     * 查询 事件单优先级
     * @author jiangfeng
     * @param code
     * @date 2022-06-21
     */

    @Override
    public List<PriorityParavalue> priorityData(String code) {
          //  查看数据库表单是否有数据
        List<PriorityParavalue> priorityParavalueList = priorityParaValueMapper.findByCode(code);

         //   如果为空 没有查询，没有新增
        if (priorityParavalueList == null || priorityParavalueList.isEmpty()) {

           PriorityParaValueEnum priorityParaValueEnum = PriorityParaValueEnum.find(code);

            //  degree影响程度 查找 子集
            List<PubParaValue> degreeList = pubParaValueMapper.priority(priorityParaValueEnum.getDegreeCode());

            //scope影响范围  查找子集
            List<PubParaValue> scopeList = pubParaValueMapper.priority(priorityParaValueEnum.getScopeCode());

            // degree 子集 id 和  ValueDetail  放入map 集合
            Map<String, String> degreeMap = degreeList.stream().collect(Collectors.toMap(PubParaValue::getParaValueId, PubParaValue::getValueDetail));
            //  scope 子集 id 和  ValueDetail  放入map 集合
            Map<String, String> scopeMap = scopeList.stream().collect(Collectors.toMap(PubParaValue::getParaValueId, PubParaValue::getValueDetail));
            //  影响范围
            for (PubParaValue pubParaValue : degreeList) {

                for (PubParaValue paraValue : scopeList) {

                    PriorityParavalue priorityParavalue = new PriorityParavalue();

                    priorityParavalue.setCode(code);
                    // degeree影响程度 子集 id
                    priorityParavalue.setPubParavalueIdDegree(pubParaValue.getParaValueId());
                    //scope影响范围  子集  id
                    priorityParavalue.setPubParavalueIdScope(paraValue.getParaValueId());
                    //优先级表 ID
                    priorityParavalue.setId(UUID.getUUIDStr());

                    //  查询后 数据 新增
                    priorityParaValueMapper.insertSave(priorityParavalue);
                    //  影响范围  通过获取 ID 从map  得到  ValueDetail
                    priorityParavalue.setPubParavalueIdScope(scopeMap.get(priorityParavalue.getPubParavalueIdScope()));
                    //  影响程度   通过获取 ID 从map  得到  ValueDetail
                    priorityParavalue.setPubParavalueIdDegree(degreeMap.get(priorityParavalue.getPubParavalueIdDegree()));
                    //  添加 集合里
                    priorityParavalueList.add(priorityParavalue);
                }
            }

            return priorityParavalueList;
        }

        //  如果表中有数据 返回数据
        PriorityParaValueEnum priorityParaValueEnum = PriorityParaValueEnum.find(code);
        if (priorityParaValueEnum == null) {
            return null;
        }
        List<PubParaValue> priority1 = pubParaValueMapper.priority(priorityParaValueEnum.getDegreeCode());
        List<PubParaValue> priority2 = pubParaValueMapper.priority(priorityParaValueEnum.getScopeCode());
        Map<String, String> degreeMap = priority1.stream().collect(Collectors.toMap(PubParaValue::getParaValueId, PubParaValue::getValueDetail));
        Map<String, String> scopeMap = priority2.stream().collect(Collectors.toMap(PubParaValue::getParaValueId, PubParaValue::getValueDetail));

        for (PriorityParavalue priorityParavalue : priorityParavalueList) {
            priorityParavalue.setPubParavalueIdScope(scopeMap.get(priorityParavalue.getPubParavalueIdScope()));
            priorityParavalue.setPubParavalueIdDegree(degreeMap.get(priorityParavalue.getPubParavalueIdDegree()));
        }

        return priorityParavalueList;
    }


    /**
     * 优先级影响程度和范围修改返回页面
     */
    @Override
    public Map<String, String> selectOneById(String id) {
        PriorityParavalue priorityParavalue = priorityParaValueMapper.selectOneById(id);
        PubParaValue degreeValue = pubParaValueMapper.selectParaValueDataById(priorityParavalue.getPubParavalueIdDegree());
        PubParaValue copeValue = pubParaValueMapper.selectParaValueDataById(priorityParavalue.getPubParavalueIdScope());

        HashMap<String, String> map = new HashMap();
        map.put("degreeValueDetail", degreeValue.getValueDetail());
        map.put("scopeValueDetail", copeValue.getValueDetail());
        map.put("priority", priorityParavalue.getPriority());

        return map;
    }

    @Override
    public String selectPriorityParavalue(PriorityParavalue paravalue) {
        return priorityParaValueMapper.selectPriorityParavalue(paravalue);
    }

}
