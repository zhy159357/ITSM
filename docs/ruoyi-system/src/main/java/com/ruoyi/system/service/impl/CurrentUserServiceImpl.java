package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.ICurrentUserService;
import org.springframework.stereotype.Service;

/**
 * 获取当前登录人信息
 * @author zhangchao
 */
@Service("currentUser")
public class CurrentUserServiceImpl implements ICurrentUserService {

    @Override
    public OgUser getCurrentUser() {
        return ShiroUtils.getOgUser();
    }
}
