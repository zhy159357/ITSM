package com.ruoyi.form.service;

import com.ruoyi.form.domain.OperationDetails;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

public interface OperationDetailsService extends IService<OperationDetails>{

    int saveOperationDetails(String bizNo, String operationType, String description, Date createTime);

    int saveOperationDetailsforChange(OperationDetails operationDetails);
}
