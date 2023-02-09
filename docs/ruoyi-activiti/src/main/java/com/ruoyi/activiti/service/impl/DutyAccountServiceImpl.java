package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.DutyAccountMapper;
import com.ruoyi.activiti.service.IDutyAccountService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.mapper.OgUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 值班绑定信息Service业务层处理
 * @author ruoyi
 * @date 2020-03-29
 */
@Service
public class DutyAccountServiceImpl implements IDutyAccountService {

    @Autowired
    private DutyAccountMapper dutyAccountMapper;
    @Autowired
    private OgUserMapper ogUserMapper;
    @Autowired
    private SysPasswordService sysPasswordService;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    private final String SUCCESS = "1";// 启用状态

    /**
     * 值班绑定添加校验
     * @param dutyAccount 排班信息
     * @return 结果
     */
    @Override
    @Transactional
    public Map<String,Object> addCheck(DutyAccount dutyAccount) {
        String msg ="";
        Map<String,Object> map = new HashMap<>();
        int flag = 0;
        String username = dutyAccount.getUsername();
        String password = dutyAccount.getPassword();
        String oldPassword = "";
        String userId = "";
        String accountPid = "";
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            flag = 1;
            msg = "用户或密码不能为空";
        }
        Map<String,Object> map1 = new HashMap<>();
        map1.put("username",username);
        map1.put("dbType",dbType);
        OgUser tmUser = null;
        if("mysql".equals(dbType)){
            tmUser = ogUserMapper.selectUserByLoginNameMysql(map1);
        }else{
            tmUser = ogUserMapper.selectUserByLoginName(map1);
        }
        if (tmUser == null) {
            OgUser  ogUser = null;
            if("mysql".equals(dbType)){
                ogUser = ogUserMapper.selectUserByPhoneNumberMysql(username);
            }else{
                ogUser = ogUserMapper.selectUserByPhoneNumber(username);
            }
            if (null == ogUser) {
                flag = 1;
                msg = "用户名不存在";
            }else{
                username = ogUser.getUsername();
                oldPassword = ogUser.getPassword();
                userId = ogUser.getUserId();
                accountPid = ogUser.getpId();
            }
        }else{
            oldPassword = tmUser.getPassword();
            userId = tmUser.getUserId();
            accountPid = tmUser.getpId();
        }
        String newPassword = sysPasswordService.encryptPassword(username, password, userId);
        if(!oldPassword.equals(newPassword)){
            flag = 1;
            msg = "密码错误";
        }
        map.put("flag",flag);
        map.put("msg",msg);
        map.put("accountPid",accountPid);
        return map;
    }

    /**
     * 新增值班绑定
     * @param dutyAccount 排班信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertDutyAccount(DutyAccount dutyAccount) {
        int result = 1;
        String pid = dutyAccount.getPid();
        DutyAccount da = dutyAccountMapper.selectDutyAccountByPid(pid);
        if(null!=da){
            if(!da.getAccountPid().equals(dutyAccount.getAccountPid())){
                da.setAccountPid(dutyAccount.getAccountPid());
                result = dutyAccountMapper.updateDutyAccount(da);
            }
        }else{
            DutyAccount account = new DutyAccount();
            account.setId(UUID.getUUIDStr());
            account.setPid(pid);
            account.setAccountPid(dutyAccount.getAccountPid());
            result = dutyAccountMapper.insertDutyAccount(account);
        }
        return result;
    }

    /**
     * 根据值班账号id查询
     * @param accountId
     * @return
     */
    @Override
    public DutyAccount selectByAccountId(String accountId){
        return dutyAccountMapper.selectDutyAccountByAccountId(accountId);
    }
}
