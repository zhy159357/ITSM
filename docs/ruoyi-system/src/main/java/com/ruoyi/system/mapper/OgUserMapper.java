package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OgUserMapper {
    String selectUserByUsername(String username);
    
    String selectPersonByUsername(String username);

    OgUser selectTimeByUsername(String username);

    List<OgUser> selectAccountList(OgUser ogUser);

    OgUser selectUserByLoginName(Map<String,Object> map);

    OgUser selectUserByLoginNameMysql(Map<String,Object> map);

    public OgUser selectUserByPhoneNumber(String phoneNumber);

    public OgUser selectUserByPhoneNumberMysql(String phoneNumber);

    int insertOgUser(OgUser ogUser);

    OgUser selectOgUserByUserId(String userId);

    OgUser selectOgUserByUserIdMysql(String userId);

    OgUser selectOgUserByUsername(String username);

    int updateOgUser(OgUser ogUser);

    List<OgUser> selectAllocatedListPost(OgUser user);

    public int updateOgUserPwdUnLock(OgUser user);

    public int updateOgUserPwdLockTime(OgUser user);

    public int updateOgUserPwdLock(OgUser user);

    public int updateOgUserTelValidNum(OgUser user);

    public int updateOgUserSms(OgUser user);

    public OgUser selectUserTelValidByUsername(Map<String,Object> map);

    public OgUser selectUserTelValidByUsernameMysql(Map<String,Object> map);

    public int checkAccountNameUnique(OgUser user);

    /**
     * 根据账号密码状态查询用户
     *
     * @param user
     * @return
     */
    public OgUser selectYesUser(OgUser user);

    public int deleteOgUserByUserIds(String[] strArray);

    public int deleteOgUserByPIds(String[] strArray);

    String selectUserIdByPId(String pId);

    OgUser selectUserPersonList(OgUser user);

    List<OgUser> selectAllUserPersonList(OgUser user);

    List<OgUser> selectPostAndOrgByUser(OgUser user);
    List<OgUser> selectPostByUser(OgUser user);

    List<OgUser> selectOgUserList(OgUser ogUser);

    OgUser selectOgUserByCustNo(String custNo);

    List<OgUser> selectAllocatedListAll(OgUser user);

    int insertOgUserBatch(@Param("list") List<OgUser> list);

    int updateOgUserBatch(@Param("list")  List<OgUser> list);
    List<OgUser> selectCurrentOrgUsers(String userId);
    List<OgUser> selectCurrentOrgLeaders(String userId);
}
