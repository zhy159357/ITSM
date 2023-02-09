package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.*;

import java.util.Map;

/**
 * 排班信息Service接口
 * @author dayong_sun
 * @date 2020-03-29
 */
public interface IDutyAccountService
{

    /**
     * 值班绑定添加校验
     * @param dutyAccount 信息
     * @return 结果
     */
    public Map<String,Object> addCheck(DutyAccount dutyAccount);

    /**
     * 新增值班绑定
     * @param dutyAccount 信息
     * @return 结果
     */
    public int insertDutyAccount(DutyAccount dutyAccount);

    /**
     * 根据值班账号id查询
     * @param accountId
     * @return
     */
    DutyAccount selectByAccountId(String accountId);
}
