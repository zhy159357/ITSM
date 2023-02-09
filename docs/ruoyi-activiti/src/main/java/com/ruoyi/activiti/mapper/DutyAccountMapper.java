package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.*;
import org.springframework.stereotype.Component;

/**
 * 值班绑定Mapper接口
 * @author ruoyi
 * @date 2020-03-29
 */
@Component
public interface DutyAccountMapper
{

    /**
     * 查询参数列表
     * @param pid 参数类表ID
     * @return 参数类表
     */
    public DutyAccount selectDutyAccountByPid(String pid);

    /**
     * 插入人员账号信息
     * @param dutyAccount
     */
    public int insertDutyAccount(DutyAccount dutyAccount);

    /**
     * 更新人员账号信息
     * @param dutyAccount
     */
    public int updateDutyAccount(DutyAccount dutyAccount);

    DutyAccount selectDutyAccountByAccountId(String accountId);
}
