package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.entity.SmBizLxbgApply;

import java.util.List;


public interface DelayLxbgService {


    /**
     * 新增延期表
     * @param smBizLxbgApply
     * @return
     */
    public int insertDelayLxbg(SmBizLxbgApply smBizLxbgApply);

    /**
     * 根据计划单ID查询延期表信息
     * @param schedulingId
     * @return
     */
    SmBizLxbgApply selectByScId(String schedulingId);

    /**
     * 修改申请延期
     * @param smBizLxbgApply
     * @return
     */
    int updateSmBizLxbgapply(SmBizLxbgApply smBizLxbgApply);

    /**
     * 查询延期申请表
     * @param smBizLxbgApply
     * @return
     */
    List<SmBizLxbgApply> selectSmBizLxbgapplyList(SmBizLxbgApply smBizLxbgApply);

}
