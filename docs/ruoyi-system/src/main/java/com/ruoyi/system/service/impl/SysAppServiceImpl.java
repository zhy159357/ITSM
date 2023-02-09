package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.OgUserPost;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ISysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统业务层处理
 *
 * @author dayong_sun
 */
@Service
public class SysAppServiceImpl implements ISysAppService {
    @Autowired
    private SysAppMapper appMapper;

    /**
     * 通过系统ID查询角色
     *
     * @param id 系统ID
     * @return 系统对象信息
     */
    @Override
    public SysApp selectById(String id) {
        return appMapper.selectById(id);
    }

    /**
     * 通过系统ID删除系统
     *
     * @param roleId 系统ID
     * @return 结果
     */
    @Override
    public boolean deleteAppById(String roleId) {
        return appMapper.deleteAppById(roleId) > 0 ? true : false;
    }

    @Override
    public int deleteAppByIds(String ids) throws Exception {
        String[] appIds = Convert.toStrArray(ids);
        return appMapper.deleteAppByIds(appIds);
    }

    /**
     * 新增保存系统信息
     *
     * @param app 系统信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertApp(SysApp app) {
        // 新增角色信息
        return appMapper.insertApp(app);
    }

    /**
     * 修改保存系统信息
     *
     * @param app 系统信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateApp(SysApp app) {
        // 修改角色信息
        return appMapper.updateApp(app);
    }

    /**
     * 校验系统名称是否唯一
     *
     * @param app 系统信息
     * @return 结果
     */
    @Override
    public String checkAppNameUnique(SysApp app) {
        SysApp info = appMapper.checkAppNameUnique(app.getSysname());
        if (StringUtils.isNotNull(info) && info.getId().equals(app.getId())) {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    @Override
    public int changeStatus(SysApp app) {
        return 0;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param app 角色信息
     */
    @Override
    public void checkAppAllowed(SysApp app) {
        if (StringUtils.isNotNull(app.getId()) && app.isAdmin()) {
            throw new BusinessException("不允许操作超级管理员角色");
        }
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param app 系统信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysApp> selectAppList(SysApp app) {
        return appMapper.selectAppList(app);
    }


    /**
     * 根据用户ID查询应用系统
     *
     * @param userId 用户ID
     * @return 应用系统列表
     */
    public List<SysApp> selectAppsByUserId(String userId) {
        List<SysApp> userApps = appMapper.selectAppsByUserId(userId);
//        List<SysApp> apps = selectAppAll();
//        for (SysApp app : apps)
//        {
//            for (SysApp userRole : userApps)
//            {
//                if (app.getId() .equals(userRole.getId()))
//                {
//                    app.setFlag(true);
//                    break;
//                }
//            }
//        }
        return userApps;
    }

    public List<OgRole> selectAppRoleRList(SysApp app) {
        return appMapper.selectAppRoleRList(app);
    }

    public List<OgRole> selectAppRoleLList(SysApp app) {
        return appMapper.selectAppRoleLList(app);
    }

    public int insertSysRole(String appid, String ids) {
        List<SysApp> list = new ArrayList<SysApp>();
        String[] postIds = ids.split(",");
        SysApp sa = null;
        for (String postId : postIds) {
            sa = new SysApp();
            sa.setAppid(appid);
            sa.setRoleid(postId);
            list.add(sa);
        }
        return appMapper.insertSysRole(list);
    }

    public int deleteAppRoleByIds(String appid, String ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] postIds = ids.split(",");
        SysApp sa = null;
        List<String> list = new ArrayList<String>();
        for (String postId : postIds) {
            list.add(postId);
        }
        map.put("sysid", appid);
        map.put("list", list);
        return appMapper.deleteAppRoleByIds(map);
    }

    public int changeSysOrder(String userId, String id, String value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysid", id);
        map.put("userid", userId);
        map.put("order", value);
        String ids = appMapper.selectSysOrder(map);
        if (null != ids && !"".equals(ids)) {
            map.put("id", ids);
            return appMapper.updateSysOrder(map);
        } else {
            map.put("id", UUID.getUUIDStr());
            return appMapper.insertSysOrder(map);
        }
    }

    public List<SysApp> selectAppOrderList(SysApp app) {
        return appMapper.selectAppOrderList(app);
    }
}
