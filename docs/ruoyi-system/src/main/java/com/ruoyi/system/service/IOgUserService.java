package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgUser;

import java.util.List;
import java.util.Map;

public interface IOgUserService {
    String selectUserByUsername(String username);

    OgUser selectTimeByUsername(String username);

    OgUser selectUserByLoginName(Map<String,Object> map);

    public OgUser selectUserByPhoneNumber(String phoneNumber);

    List<OgUser> selectAccountList(OgUser ogUser);

    int insertOgUser(OgUser ogUser);

    OgUser selectOgUserByUserId(String userId);

    OgUser selectOgUserByUsername(String username);

    int updateOgUser(OgUser ogUser);

    public int updateOgUserPwdUnLock(OgUser user);

    public int updateOgUserPwdLockTime(OgUser user);

    public int updateOgUserPwdLock(OgUser user);

    public int updateOgUserTelValidNum(OgUser user);

    public int updateOgUserSms(OgUser user);

    OgUser selectUserTelValidByUsername(Map<String,Object> map);

    String checkAccountNameUnique(OgUser user);

    /**
     * 根据账号密码状态查询用户
     *
     * @param user
     * @return
     */
    public OgUser selectYesUser(OgUser user);
    
    public int deleteOgUserByUserIds(String userIds);
    
    public int deleteOgUserByPIds(String pIds);

    String selectUserIdByPId(String pId);

    /**
     * 查询同步用户所需数据
     * @param ogUser
     * @return
     */
    OgUser selectUserPersonList(OgUser ogUser);

    List<OgUser> selectAllUserPersonList(OgUser ogUser);

    List<OgUser> selectOgUserList(OgUser ogUser);

    OgUser selectOgUserByCustNo(String custNo);
}
