package com.ruoyi.activiti.service.forward.impl;

import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.forward.OgSysService;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.system.mapper.OgSysMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class OgSysServiceImpl implements OgSysService {
    @Autowired
    private ICommonService commonService;
    private static final Logger logger = LoggerFactory.getLogger(OgSysServiceImpl.class);

    /**
     * 810015-查询业务经理
     * @param params
     * @return
     */
    @Override
    @EsbServiceMapping
    public List<OgPerson> getbbss(Map<String,Object> params){
        logger.info("==============810015-查询业务经理开始==============");
        String businessOrg=null;
        for(Map.Entry<String,Object> entry:params.entrySet()){
            if(entry.getKey().startsWith("businessOrg")){
                businessOrg=entry.getValue().toString();
                break;
            }
        }
        OgPerson ogPerson = new OgPerson();
            ogPerson.setrId("2302");
            ogPerson.setOrgId(businessOrg);
        List<OgPerson> list = commonService.selectPersonByOrgAndRole(ogPerson);
        logger.info("==============810015-查询业务经理结束==============");
        return list;
    }
}
