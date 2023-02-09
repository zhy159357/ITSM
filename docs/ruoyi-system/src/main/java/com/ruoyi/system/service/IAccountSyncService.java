package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgUser;

import java.util.List;

public interface IAccountSyncService {

    /**
     * ITSM同步账号信息到门户（新增｜修改）
     * @param ogUserList
     * @return
     */
    public boolean syncAccount(List<OgUser> ogUserList, String flag);

    /**
     * ITSM同步账号信息到门户（新增｜修改）
     * @param ogUserList
     * @return
     */
    public boolean syncAccountTb(List<OgUser> ogUserList, String flag);

    /**
     * ITSM同步账号信息到门户（启用｜禁用）
     * @param userId
     * @param invalidationMark
     * @return
     */
    public boolean enabledUser(String userId, String invalidationMark);
}
