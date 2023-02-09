package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.core.domain.entity.OgPerson;
import java.util.List;
import java.util.Map;
public interface OgSysService {
    /**
     * 810015-查询业务经理
     * @param params
     * @return
     */
    public List<OgPerson> getbbss(Map<String,Object> params);

}
