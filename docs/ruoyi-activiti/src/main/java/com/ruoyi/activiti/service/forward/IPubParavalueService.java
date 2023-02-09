package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.exception.BusinessException;

import java.util.List;

public interface IPubParavalueService {
    /**
     * 610001数据字典查询（公共）
     *
     * @param paraName
     * @return
     */
    List getPubParavaluesByParaName(String paraName) throws BusinessException;
}
