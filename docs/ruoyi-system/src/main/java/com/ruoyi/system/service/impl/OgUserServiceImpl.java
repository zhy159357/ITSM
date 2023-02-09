package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.mapper.OgUserMapper;
import com.ruoyi.system.service.IOgUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OgUserServiceImpl implements IOgUserService {

    @Autowired
    private OgUserMapper ogUserMapper;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public String selectUserByUsername(String username) {
        return ogUserMapper.selectUserByUsername(username);
    }
    @Override
    public OgUser selectTimeByUsername(String username) {
        return ogUserMapper.selectTimeByUsername(username);
    }

    @Override
    public OgUser selectUserByLoginName(Map<String,Object> map) {
        if("mysql".equals(dbType)){
            return ogUserMapper.selectUserByLoginNameMysql(map);
        }else{
            return ogUserMapper.selectUserByLoginName(map);
        }

    }

    public OgUser selectUserByPhoneNumber(String phoneNumber) {
        if("mysql".equals(dbType)){
            return ogUserMapper.selectUserByPhoneNumberMysql(phoneNumber);
        }else{
            return ogUserMapper.selectUserByPhoneNumber(phoneNumber);
        }

    }

    @Override
    public List<OgUser> selectAccountList(OgUser ogUser) {
        return ogUserMapper.selectAccountList(ogUser);
    }

    @Override
    public int insertOgUser(OgUser ogUser) {
        return ogUserMapper.insertOgUser(ogUser);
    }

    @Override
    public OgUser selectOgUserByUserId(String userId) {
        if("mysql".equals(dbType)){
            return ogUserMapper.selectOgUserByUserIdMysql(userId);
        }else{
            return ogUserMapper.selectOgUserByUserId(userId);
        }
    }

    @Override
    public OgUser selectOgUserByUsername(String username) {
        return ogUserMapper.selectOgUserByUsername(username);
    }

    @Override
    public int updateOgUser(OgUser ogUser) {
        return ogUserMapper.updateOgUser(ogUser);
    }


    public int updateOgUserPwdLockTime(OgUser user) {
        return ogUserMapper.updateOgUserPwdLockTime(user);
    }

    public int updateOgUserPwdUnLock(OgUser user) {
        return ogUserMapper.updateOgUserPwdUnLock(user);
    }

    public int updateOgUserPwdLock(OgUser user) {
        return ogUserMapper.updateOgUserPwdLock(user);
    }

    public int updateOgUserTelValidNum(OgUser user) {
        return ogUserMapper.updateOgUserTelValidNum(user);
    }

    public int updateOgUserSms(OgUser user) {
        return ogUserMapper.updateOgUserSms(user);
    }

    public OgUser selectUserTelValidByUsername(Map<String,Object> map) {
        if("mysql".equals(dbType)){
            return ogUserMapper.selectUserTelValidByUsernameMysql(map);
        }else{
            return ogUserMapper.selectUserTelValidByUsername(map);
        }

    }

    public String checkAccountNameUnique(OgUser user) {
        int count = ogUserMapper.checkAccountNameUnique(user);
        return String.valueOf(count);
    }

    @Override
    public OgUser selectYesUser(OgUser user) {
        return ogUserMapper.selectYesUser(user);
    }

	@Override
	public int deleteOgUserByUserIds(String userIds) {
		return ogUserMapper.deleteOgUserByUserIds(Convert.toStrArray(userIds));
	}

	@Override
	public int deleteOgUserByPIds(String pIds) {
		return ogUserMapper.deleteOgUserByPIds(Convert.toStrArray(pIds));
	}

    @Override
    public String selectUserIdByPId(String pId) {
        return ogUserMapper.selectUserIdByPId(pId);
    }

    @Override
    public OgUser selectUserPersonList(OgUser ogUser) {
        return ogUserMapper.selectUserPersonList(ogUser);
    }

    @Override
    public List<OgUser> selectAllUserPersonList(OgUser ogUser) {
        return ogUserMapper.selectAllUserPersonList(ogUser);
    }

    @Override
    public List<OgUser> selectOgUserList(OgUser ogUser) {
        return ogUserMapper.selectOgUserList(ogUser);
    }

    @Override
    public OgUser selectOgUserByCustNo(String custNo) {
        return ogUserMapper.selectOgUserByCustNo(custNo);
    }
}
