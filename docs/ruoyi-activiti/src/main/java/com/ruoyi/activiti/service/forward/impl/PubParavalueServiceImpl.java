package com.ruoyi.activiti.service.forward.impl;


import com.ruoyi.activiti.service.forward.IPubParavalueService;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IPubParaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 跳板机接口查询参数
 * @author 14735
 */
@Service("pubParavalueManager")
public class PubParavalueServiceImpl implements IPubParavalueService {

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Override
    @EsbServiceMapping
    public List getPubParavaluesByParaName(@ServiceParam(name = "paraName")String paraName) throws BusinessException {
        List<Map> mapList = new ArrayList<>();
        if (StringUtils.isEmpty(paraName)) {
            throw new BusinessException("1004", "所传参数名称不能为空");
        }
        List<PubParaValue> values = pubParaValueService.selectPubParaValueByParaName(paraName);
        for (PubParaValue value : values) {
            Record map = new Record();
            map.put("value", value.getValue());
            map.put("valueDetail", value.getValueDetail());
            mapList.add(map);
        }
        return mapList;
    }
}
