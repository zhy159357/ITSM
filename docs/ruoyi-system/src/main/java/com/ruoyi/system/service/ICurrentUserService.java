package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgUser;

public interface ICurrentUserService {

    /**
     * 获取当前登录人信息
     * @return
     */
    OgUser getCurrentUser();
}
