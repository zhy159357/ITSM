package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.http.entegorserver.entity.UserSyncMsg;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysAccountService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.server.SynchronizeUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号信息操作处理
 *
 * @author ruoyi
 */
@Configuration
@Controller
@RequestMapping("/system/account")
public class SysAccountController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SysAccountController.class);

    private String prefix = "system/account";

    @Autowired
    private ISysAccountService accountService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private SysPasswordService sysPasswordService;

    @Autowired
    private SynchronizeUserService synchronizeUserserviceImp;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;


    /**
     * 同步用户开关
     */
    private boolean synchronizeUser = RuoYiConfig.getSynchronizeUser();

    @GetMapping()
    public String operlog() {
        return prefix + "/account";
    }


    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgUser ogUser) {
        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //2.全国中心查看所有
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        if (oneLvOrTwoLv.getLevelCode().contains("/000000000/010000888/")
                || ("/000000000/".equals(oneLvOrTwoLv.getLevelCode()))) {

        } else {
            //3.省中心只看到本省的
           ogUser.setLevelCode(oneLvOrTwoLv.getLevelCode());
        }
        startPage();
        List<OgUser> list = ogUserService.selectAccountList(ogUser);
        return getDataTable(list);


    }

    @PostMapping("/listAccount")
    @ResponseBody
    public TableDataInfo listAccount(OgUser ogUser) {
        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        /*//2.全国中心查看所有
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        if (("/000000000/".equals(oneLvOrTwoLv.getLevelCode()))) {

        } else {
            //3.省中心只看到本省的
            ogUser.setLevelCode(ogOrg.getLevelCode());
        }*/
        startPage();
        List<OgUser> list = ogUserService.selectAccountList(ogUser);
        return getDataTable(list);


    }

    @Log(title = "账号管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgUser ogUser) {
        List<OgUser> list = ogUserService.selectAccountList(ogUser);
        ExcelUtil<OgUser> util = new ExcelUtil<OgUser>(OgUser.class);
        return util.exportExcel(list, "账号数据");
    }

    @Log(title = "账号管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(accountService.deleteAccountByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 新增账号
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return prefix + "/add";
    }

    @PostMapping("/userlist")
    @ResponseBody
    public TableDataInfo userlist(SysUser sysUser, String searchUsername, String searchPhonenumber) {
        startPage();
        SysUser user = new SysUser();
        if (null != searchUsername && !"".equals(searchUsername)) {
            user.setUserName(searchUsername);
        }
        if (null != searchPhonenumber && !"".equals(searchPhonenumber)) {
            user.setPhonenumber(searchPhonenumber);
        }
        user.setUserId(sysUser.getUserId());
        List<SysAccount> userList = accountService.selectUserList(user);
        return getDataTable(userList);
    }

    /**
     * 新增保存账号
     */
    @Log(title = "账号管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgUser ogUser) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserSyncMsg msg = new UserSyncMsg();
        //判断账号是否存在
        if (UserConstants.ACCOUNT_NAME_NOT_UNIQUE.equals(ogUserService.checkAccountNameUnique(ogUser))) {
            return error("新增账号'" + ogUser.getUsername() + "'失败，账号名称已存在");
        }
        //判断柜员号是否已绑定
       /* OgUser user = new OgUser();
        List<OgUser> ogUsers = ogUserService.selectAccountList(user);
        if(StringUtils.isNotEmpty(ogUsers)){
            for(OgUser users : ogUsers){
                if(ogUser.getCustNo().equals(users.getCustNo())){
                    return AjaxResult.error("柜员号已被绑定");
                }
            }
        }*/
        //判断该人员是否已经存在账号
        String res = ogUserService.selectUserIdByPId(ogUser.getpId());
        if (StringUtils.isNotEmpty(res)) {
            return error("该人员已创建账号信息！");
        }

        ogUser.setUserId(ogUser.getpId());
        ogUser.setInvalidationMark("1");
        ogUser.setPassword("Bosc@1234");
        ogUser.setPassword(sysPasswordService.encryptPassword(ogUser.getUsername(), ogUser.getPassword(), ogUser.getUserId()));
        ogUser.setUpdateTime(DateUtils.dateTimeNow());

        try {
            int v = ogUserService.insertOgUser(ogUser);
            //同步数据
            if (synchronizeUser && v > 0) {
                OgUser userPerson = ogUserService.selectUserPersonList(ogUser);
                map.put("userKey", userPerson.getUserId());
                map.put("userName", userPerson.getUsername());
                map.put("state", userPerson.getStatus());
                map.put("department", userPerson.getOrgname());
                map.put("password", "Bosc@1234");
                map.put("email", userPerson.getEmail());
                map.put("telphone", userPerson.getMobilPhone());
                map.put("ideCode", userPerson.getSmsCode());
                map.put("longinName", userPerson.getMobilPhone());
                msg.setParams(map);
                synchronizeUser(userPerson, 0, userPerson.getMobilPhone(), msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("账号保存数据库失败:{}", e);
            throw new BusinessException("账号创建失败");
        }

        OgPerson person = ogPersonService.selectOgPersonById(ogUser.getpId());
        String smsText = "运维管理平台新增账号为" + ogUser.getUsername() + ",密码为Bosc@1234,登录后请到个人设置修改密码";

        try {
            sendSms(smsText, person);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增账号短信发送失败:{}", e);
        }
        return success("操作成功");
    }


    private void synchronizeUser(OgUser user, int type, String loginNames, UserSyncMsg msg) {
        synchronizeUserserviceImp.synchronizeUser(user, type, loginNames, msg);
    }

    /**
     * 修改账号
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") String userId, ModelMap mmap) {
        mmap.put("ogUser", ogUserService.selectOgUserByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 修改保存账号
     */
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgUser ogUser) {
        Map<String, Object> map = new HashMap<String, Object>();
        //判断柜员号是否绑定
        if(StringUtils.isNotEmpty(ogUser.getCustNo())){
            OgUser user = ogUserService.selectOgUserByCustNo(ogUser.getCustNo());
            if(user!=null){
                if(!ogUser.getUserId().equals(user.getUserId())){
                    return AjaxResult.error("柜员号已被绑定");
                }
            }
        }

        if (StringUtils.isNotEmpty(ogUser.getPassword())) {
            ogUser.setPassword(sysPasswordService.encryptPassword(ogUser.getUsername(), ogUser.getPassword(), ogUser.getUserId()));
            //没有失败，更新数据库以前的错误数据为正常
            OgUser unOgUser = new OgUser();
            unOgUser.setUserId(ogUser.getUserId());
            ogUserService.updateOgUserPwdUnLock(unOgUser);
        }

        String pId = ogUser.getpId();
        OgPerson ogPerson = new OgPerson();
        ogPerson.setpId(pId);
        if (StringUtils.isNotEmpty(ogUser.getInvalidationMark())) {
            //如何状态禁用清除和岗位对应的数据
            if ("0".equals(ogUser.getInvalidationMark())) {
                //  ogUserPostService.deletePostByUserId(ogUser.getUserId());
                ogPerson.setInvalidationMark("0");
            } else { //启用状态 更新人员
                ogPerson.setInvalidationMark("1");
            }
        } else {
            ogPerson.setInvalidationMark("1");
        }

        ogPersonService.updateOgPersonStatus(ogPerson);
        ogUser.setUpdateTime(DateUtils.dateTimeNow());

        int num = ogUserService.updateOgUser(ogUser);
        if (num > 0) {
            // 修改密码成功清除当前登录缓存
            if (StringUtils.isNotEmpty(ogUser.getPassword())) {
                removeLoginRecordCache(ogUser);
            }
            if (synchronizeUser) {
                //同步数据
                UserSyncMsg msg = new UserSyncMsg();
                OgUser userPerson = ogUserService.selectUserPersonList(ogUser);
                map.put("userKey", userPerson.getUserId());
                map.put("userName", userPerson.getUsername());
                map.put("state", userPerson.getStatus());
                map.put("department", userPerson.getOrgname());
                map.put("password", "Bosc@1234");
                map.put("email", userPerson.getEmail());
                map.put("telphone", userPerson.getMobilPhone());
                map.put("ideCode", userPerson.getSmsCode());
                map.put("longinName", userPerson.getMobilPhone());
                msg.setParams(map);
                synchronizeUser(userPerson, 0, userPerson.getMobilPhone(), msg);
            }
        }
        return toAjax(num);
    }

    /**
     * 修改密码删除登录缓存
     *
     * @param ogUser
     */
    private void removeLoginRecordCache(OgUser ogUser) {
        String loginName = ogUser.getUsername();
        if (StringUtils.isNotEmpty(loginName)) {
            CacheUtils.remove(ShiroConstants.LOGINRECORDCACHE, loginName);
        }
    }

    /**
     * 校验账号名称
     */
    @PostMapping("/checkAccountNameUnique")
    @ResponseBody
    public String checkAccountNameUnique(SysAccount account) {
        return accountService.checkAccountNameUnique(account);
    }

    /**
     * 账号状态修改
     */
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysAccount account) {
        return toAjax(accountService.changeStatus(account));
    }

    /**
     * 进入授权岗位页
     */
    @GetMapping("/authPost/{id}")
    public String authRole(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("userId", id);
        return prefix + "/authPost";
    }

    /**
     * 查询岗位数据
     */
    @PostMapping("/postlist")
    @ResponseBody
    public TableDataInfo postlist(SysAccount account) {
        startPage();
        List<SysPost> list = accountService.selectAccountsByAccountId(account);
        return getDataTable(list);
    }

    /**
     * 账号分配岗位
     */
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/insertPostAccount")
    @ResponseBody
    public AjaxResult insertPostAccount(Long accountId, Long[] postIds) {
        accountService.insertPostAccount(accountId, postIds);
        return success();
    }

    /**
     * 设置账号IP段
     */
    @GetMapping("/authIp/{userId}")
    public String authIp(@PathVariable("userId") String userId, ModelMap mmap) {
        mmap.put("account", ogUserService.selectOgUserByUserId(userId));
        return prefix + "/authIp";
    }

    /**
     * 修改保存账号
     */
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/autoIpSave")
    @ResponseBody
    public AjaxResult autoIpSave(@Validated SysAccount account) {
        return toAjax(accountService.updateAccount(account));
    }


    @GetMapping("/selectUser")
    public String selectUser() {
        return prefix + "/selectUser";
    }


    @GetMapping("/selectPost")
    public String selectPost() {
        return prefix + "/selectPost"; }


    /**
     * 获取当前登陆人的二级或者是一级机构
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.当前登陆用户的机构就是一级或者是二级机构
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            return ogOrg;
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            String twoLevelCode = split[2];
            return deptService.selectDeptByCode(twoLevelCode);
        }

    }

    /**
     * 发送短信
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }


}
