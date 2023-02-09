package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.OperationDetails;
import com.ruoyi.form.mapper.OperationDetailsMapper;
import com.ruoyi.form.service.OperationDetailsService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class OperationDetailsServiceImpl extends ServiceImpl<OperationDetailsMapper, OperationDetails> implements OperationDetailsService{

    @Autowired
    private IOgUserService ogUserService;

    @Override
    public int saveOperationDetails(String bizNo, String operationType, String description, Date date) {
        OperationDetails details = OperationDetails.builder().bizNo(bizNo).operationType(operationType).
                description(description).createdTime(date).build();
        return baseMapper.insert(details);
    }

    @Override
    public int saveOperationDetailsforChange(OperationDetails operationDetails) {
        return baseMapper.insert(operationDetails);
    }
}
