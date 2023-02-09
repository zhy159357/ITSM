package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubBizSms;
import com.ruoyi.activiti.mapper.PubBizSmsMapper;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OutSystemSmsCode;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信发送Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-09
 */
@Service("pubBizSmsService")
public class PubBizSmsServiceImpl implements IPubBizSmsService, InitializingBean {

    //private static TestCMSLongConnectionAPI tcc;

    @Autowired
    private PubBizSmsMapper pubBizSmsMapper;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IOgUserService ogUserService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IVmBizInfoService vmBizInfoService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private ICmBizSingleService cmBizSingleService;
    @Autowired
    private ICmBizQingqiuService cmBizQingqiuService;

    private static final Logger log = LoggerFactory.getLogger(PubBizSmsServiceImpl.class);

    /*** 发送上行短信使用码值 ***/
    private final String strBusinessCode001 = "311_YTHYW_001";
    /*** 短信转微信使用码值 ***/
    private final String strBusinessCode002 = "311_YTHYW_002";

    /**
     * 短信配置开关
     */
    @Value("${shiro.user.telEnabled}")
    private boolean telEnabled;

    /**
     * 是否测试环境
     */
    @Value("${shiro.user.isTest}")
    private boolean isTest;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 登录短信验证码间隔时间默认1分钟
     */
    private final long smsIntervalTime = 1 * 60 * 1000;

    /**
     * 查询短信发送
     *
     * @param pubBizSmsId 短信发送ID
     * @return 短信发送
     */
    @Override
    public PubBizSms selectPubBizSmsById(String pubBizSmsId) {
        return pubBizSmsMapper.selectPubBizSmsById(pubBizSmsId);
    }

    /**
     * 查询短信发送列表
     *
     * @param pubBizSms 短信发送
     * @return 短信发送
     */
    @Override
    public List<PubBizSms> selectPubBizSmsList(PubBizSms pubBizSms) {
        return pubBizSmsMapper.selectPubBizSmsList(pubBizSms);
    }

    @Override
    public List<PubBizSms> selectPubBizSmsListTwo(PubBizSms pubBizSms) {
        return pubBizSmsMapper.selectPubBizSmsListTwo(pubBizSms);
    }

    /**
     * 新增短信发送
     *
     * @param pubBizSms 短信发送
     * @return 结果
     */
    @Override
    public int insertPubBizSms(PubBizSms pubBizSms) {
        return pubBizSmsMapper.insertPubBizSms(pubBizSms);
    }

    /**
     * 修改短信发送
     *
     * @param pubBizSms 短信发送
     * @return 结果
     */
    @Override
    public int updatePubBizSms(PubBizSms pubBizSms) {
        pubBizSms.setUpdateTime(DateUtils.getNowDate());
        return pubBizSmsMapper.updatePubBizSms(pubBizSms);
    }

    /**
     * 删除短信发送对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePubBizSmsByIds(String ids) {
        return pubBizSmsMapper.deletePubBizSmsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除短信发送信息
     *
     * @param pubBizSmsId 短信发送ID
     * @return 结果
     */
    @Override
    public int deletePubBizSmsById(String pubBizSmsId) {
        return pubBizSmsMapper.deletePubBizSmsById(pubBizSmsId);
    }

    /**
     * 发送短信验证码
     *
     * @param user
     * @return 返回信息
     */
    @Override
    public String send(OgUser user) {
        String phone = "";
        if (null != user && !"".equals(user.getUserId())) {
            String msg = null;
            phone = user.getMobilPhone();
            if (StringUtils.isEmpty(phone)) {
                return "-1";
            }
            if (null != user.getLastTime()) {
                // 增加发送短息验证码间隔时间控制，默认间隔时间为1分钟
                Map<String,Object> map = new HashMap<>();
                map.put("username",user.getUsername());
                map.put("dbType",dbType);
                OgUser ogUser = ogUserService.selectUserTelValidByUsername(map);
                // 重新查询数据获取数据库的当前时间与lastTime作比较，如果小于1一分钟直接将报错信息提示到页面
                long currentTime = Long.valueOf(ogUser.getCurrtTime());
                long time = user.getLastTime();
                // 当前时间大于lastTime作判断，反之不判断
                if(currentTime > time){
                    if (currentTime - time <= smsIntervalTime) {
                        return "-6";
                    }
                }
            }
            Random random = new Random(System.currentTimeMillis());
            String smsCode = com.ruoyi.common.utils.StringUtils.center(Math.abs(random.nextLong() % 999999) + "", 6, '0');
            log.debug("login.sms.start:" + smsCode);
            String smstext = "运维管理中心登录验证码：" + smsCode + "，账号：" + user.getUsername() + "，24小时内有效";
            // 短信配置开关打开后才发送短信
            if (telEnabled && !isTest) {
                //tcc.SendShortMessage(phone, smstext, strBusinessCode002);
            }
//            int b = tcc.getB();
//            if (b == 1) {
            //记录短信信息记录
            PubBizSms sms = new PubBizSms();
            sms.setSmsTime(DateUtils.getToday("yyyyMMddHHmmss"));
            sms.setName(user.getPname());
            sms.setTelephone(phone);
            sms.setSmsText(smstext);
            sms.setPubBizSmsId(UUID.getUUIDStr());
            sms.setCreaterId(user.getpId());
            sms.setSmsState("0");
            sms.setInvalidationMark("1");
            insertPubBizSms(sms);
            //重新发验证码，更新验证码和时间
            OgUser og = new OgUser();
            og.setUserId(user.getUserId());
            og.setSmsCode(smsCode);
            ogUserService.updateOgUserSms(og);
            msg = "1";
//            } else {
//                msg = "-2";
//            }
            log.debug("-------------------login.sms.end:" + smsCode);
            return msg;
        } else {
            return "-3";
        }
    }

    /**
     * 外系统访问ITSM发送短信验证码方法
     *
     * @param outSystemSmsCode
     * @return
     */
    public String sendSmsOutSystem(OutSystemSmsCode outSystemSmsCode, long effectiveTime) {
        Random random = new Random(System.currentTimeMillis());
        String smsCode = com.ruoyi.common.utils.StringUtils.center(Math.abs(random.nextLong() % 999999) + "", 6, '0');
        String smsText = "系统名称：" + outSystemSmsCode.getSysName() + "，访问运维管理中心获取验证码：" + smsCode + "，手机号：" + outSystemSmsCode.getPhoneNumber() + "，";
        if(effectiveTime > 60 * 60 * 1000){
            effectiveTime = effectiveTime / 60 / 60 / 1000;
            smsText += effectiveTime + "小时内有效";
        } else {
            effectiveTime = effectiveTime / 60 / 1000;
            smsText += effectiveTime + "分钟内有效";
        }
        log.debug("---------------外系统调用ITSM获取短信验证码，短信内容[" + smsText + "]---------------");
        // 短信配置开关打开后才发送短信
        if (telEnabled && !isTest) {
            //tcc.SendShortMessage(outSystemSmsCode.getPhoneNumber(), smsText, strBusinessCode002);
        }
        return smsCode;
    }

    /**
     * 外系统通过ITSM发送短信
     * @param msg
     * @param phone
     */
    public void sendMsgOutSystem(String phone, String msg) {
        log.debug("---------------外系统调用ITSM发送短信，短信内容[" + msg + "]---------------");
        // 短信配置开关打开后才发送短信
        if (telEnabled && !isTest) {
            //tcc.SendShortMessage(phone, msg, strBusinessCode002);
        }
    }

    /**
     * 发送待发送的短信
     *
     * @param pubBizPresms
     */
    @Override
    public void findPreSmsAndSend(PubBizPresms pubBizPresms) {

        String phone = pubBizPresms.getTelephone();//获取要发送短信的电话号码
        String smstext = pubBizPresms.getSmsText();//获取要发送的短信内容
        try {
            // 短信配置开关打开后才发送短信
            if (telEnabled && !isTest) {
                Object convertFlag = pubBizPresms.getParams().get("convertFlag");
                // 上行短信使用码值311_YTHYW_001
                if("convert".equals(convertFlag)){
                    //发送短信上行短信
                    //tcc.SendShortMessage(phone, smstext, strBusinessCode001);
                } else {
                    //发送短信转微信
                    //tcc.SendShortMessage(phone, smstext, strBusinessCode002);
                }
            }
//            int b = tcc.getB();//获取发送短信后的返回结果
//            if (b == 1) {
            pubBizPresms.setSmsState("1");//状态改为已发送
            pubBizPresmsService.updatePubBizPresms(pubBizPresms);//修改存储表信息
            PubBizSms pbs = new PubBizSms();//创建发送表
            pbs.setPubBizPresmsId(pubBizPresms.getPubBizPresmsId());//保存存储表ID
            pbs.setCreaterId(pubBizPresms.getCreaterId());//保存创建人
            pbs.setName(pubBizPresms.getName());//保存短信接收人姓名
            pbs.setTelephone(pubBizPresms.getTelephone());//发送短信的电话号码
            pbs.setSmsTime(DateUtils.dateTimeNow());//发送时间
            pbs.setSmsText(pubBizPresms.getSmsText());//发送短信内容
            pbs.setUpdateTime(pubBizPresms.getUpdateTime());//修改时间
            pbs.setPubBizSmsId(UUID.getUUIDStr());//发送表ID
            pubBizSmsService.insertPubBizSms(pbs);//保存发送表信息
            // }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("--------------短信发送失败-----------------");
        }
    }

    /**
     * 发送需要回复的审批短信
     *
     * @param taskId               流程Id
     * @param bizId                业务单Id
     * @param person               操作人
     * @param smsPerson            接收短信对象
     * @param processDefinitionKey 流程模板key
     */
    @Override
    public void sendMessage(String taskId, String bizId, OgPerson person, OgPerson smsPerson, String processDefinitionKey) {
        String text = "";//短信内容
        String objs = "";//流程类型
        if ("VmBn".equals(processDefinitionKey)) {//发布管理
            VmBizInfo vm = vmBizInfoService.selectVmBizInfoByIdNoConvert(bizId);
            if (!"13".equals(vm.getVersionStatus())) {//非紧急审批
                int u = vm.getVersionName().length();
                String versionName = "";
                if (u > 40) {
                    versionName = vm.getVersionName().substring(0, 39);
                } else {
                    versionName = vm.getVersionName();
                }
                text = "版本单号：" + vm.getVersionInfoNo() + "，标题：" + vm.getVersionInfoName() + "，升级摘要："
                        + versionName + "，创建人：" + ogPersonService.selectOgPersonById(vm.getVersionProducerId()).getpName() + " ";
            } else {
                text = "尊敬的领导,版本单号：" + vm.getVersionInfoNo() + ",标题：" + vm.getVersionInfoName() + ",系统："
                        + iSysApplicationManagerService.selectOgSysBySysId(vm.getSysId()).getCaption() + ",创建人：" + "" + ogPersonService.selectOgPersonById(vm.getVersionProducerId()).getpName()
                        + ",升级日期：" + "" + DateUtils.formatDateStr(vm.getStartUpgradeTime().substring(0, 10),
                        "yyyyMMddHH", "yyyy年MM月dd日")
                        + ",的版本需进行紧急发布审批";
            }
            objs = "111101";
        }
        if ("CmZy".equals(processDefinitionKey)) {//资源变更
            CmBizSingle ch = cmBizSingleService.selectCmBizSingleByIdById(bizId);
            text = "综合变更单号：" + ch.getChangeCode() + "，标题：" + ch.getChangeSingleName() + "，审批人："
                    + ogPersonService.selectOgPersonById(ch.getCheckerId()).getpName() + " ";
            objs = "111103";
        }
        if ("BGQQ".equals(processDefinitionKey)) {//变更请求
            CmBizQingqiu cq = cmBizQingqiuService.selectCmBizQingqiuById(bizId);
            text = "变更请求单号：" + cq.getChangeCode() + "，标题：" + cq.getChangeSingleName() + "，审批人："
                    + ogPersonService.selectOgPersonById(cq.getImplementorId()).getpName() + " ";
            objs = "111104";
        }
        PubBizPresms p = new PubBizPresms();
        PubBizPresms p1 = new PubBizPresms();
        Map map = new HashMap<String, Object>();
        String b[] = shortUrl(taskId);
        String vfCode = b[1].toLowerCase();//生成验证码
        p.setSmsText(text + ",请登录运维管理平台操作或回复短信【CONFIRM+" + vfCode + "】操作通过，短信回复有效期为5天！");//短信内容
        p.setPubBizPresmsId(UUID.getUUIDStr());
        p.setName(smsPerson.getpName());//接收人
        p.setTelephone(smsPerson.getMobilPhone());//接收手机号
        p.setSmsType("4");//短信类型
        p.setVerificationcode(vfCode);//验证码
        p.setTaskId(taskId);//任务Id
        p.setInspectObject(objs);//任务类型
        p.setInspectObjectId(bizId);//业务单Id
        p.setInspectTime(DateUtils.dateTimeNow());
        p.setCreaterId(person.getpId());
        p.setInvalidationMark("1");
        p.setSmsState("0");
        if ("111101".equals(objs)) {
            p.setDealStatus("0");
        }
        pubBizPresmsService.insertPubBizPresms(p);
        // 发送第二条短信来方便回复
        p1.setSmsText("CONFIRM+" + vfCode);
        p1.setPubBizPresmsId(UUID.getUUIDStr());
        p1.setName(smsPerson.getpName());//接收人
        p1.setTelephone(smsPerson.getMobilPhone());//接收人手机号
        p1.setSmsType("4");
        p1.setInspectObject(objs);
        p1.setInspectObjectId(bizId);
        p1.setInspectTime(DateUtils.getToday("yyyyMMddHHmmss"));
        p1.setCreaterId(person.getpId());
        p1.setInvalidationMark("1");
        p1.setSmsState("0");
        pubBizPresmsService.insertPubBizPresms(p1);
        map.put("convertFlag", "convert");
        p.setParams(map);
        p1.setParams(map);
        findPreSmsAndSend(p);
        findPreSmsAndSend(p1);
    }

    /**
     * 接收短信后处理流程
     *
     * @param vfCode    验证码
     * @param telephone 发送手机号
     */
    @Override
    @Transactional
    public boolean ReceiveFlow(String vfCode, String telephone) {
        // 是否版本发布上行短信标识
        boolean versionPublicFlag = false;
        OgPerson person = ogPersonService.checkPhoneUnique(telephone);
        String userId = person.getpId();// 获取人员ID
        PubBizPresms pbp = new PubBizPresms();
        pbp.setVerificationcode(vfCode);
        pbp.setTelephone(telephone);
        List<PubBizPresms> pbp2 = pubBizPresmsService.selectPubBizPresmsList(pbp);//根据验证码+手机号拿到对应短信对象
        PubBizPresms pbp1 = pbp2.get(0);
        if (pbp1 != null) {
            try {
                String taskId = pbp1.getTaskId();// 根据短信对象获取流程ID
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();//根据taskId拿到任务
                String flowName = task.getDescription();//获取任务描述
                Map<String, Object> reMap = new HashMap<>();
                String objs = pbp1.getInspectObject();
                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
                ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId()).singleResult();
                reMap.put("reCode", "0");//流程走向
                reMap.put("userId", userId);//流程处理人
                reMap.put("taskId", taskId);//流程任务ID
                reMap.put("comment", "同意。");//操作意见
                reMap.put("businessKey", processInstance.getBusinessKey());//业务单ID
                log.debug("------------接收到上行短信手机号【"+telephone+"】，验证码【"+vfCode+"】开始处理流程--------------");
                if ("111103".equals(objs)) {//资源变更单
                    CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(pbp1.getInspectObjectId());
                    cbs.setChangeSingleStauts("0500");
                    cmBizSingleService.updateCmBizSingle(cbs);
                    reMap.put("processDefinitionKey", "CmZy");
                    activitiCommService.complete(reMap);
                    cmBizSingleService.sendCheck(cbs);//判断是否自动化
                }
                if ("111104".equals(objs)) {//变更请求单
                    CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(pbp1.getInspectObjectId());
                    CmBizQingqiu qingqiu = new CmBizQingqiu();
                    qingqiu.setChangeId(cmBizQingqiu.getChangeId());
                    qingqiu.setStauts("0400");
                    String dealIdList = cmBizQingqiu.getDealIdList();
                    qingqiu.setDealIdList(dealIdList + cmBizQingqiu.getFucheckerId() + ",");
                    cmBizQingqiuService.updateCmBizQingqiu(qingqiu);
                    reMap.put("processDefinitionKey", "BGQQ");
                    activitiCommService.complete(reMap);
                }
                if ("111101".equals(objs)) {//版本发布
                    reMap.put("processDefinitionKey", "VmBn");
                    if ("业务主管审核".equals(flowName)) {//判断是否多人任务
                        activitiCommService.usersComplete(reMap);
                    } else {//单人任务
                        activitiCommService.complete(reMap);
                        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByIdNoConvert(processInstance.getBusinessKey());
                        vmBizInfo.getParams().put("isMsgAppoval", true);
                        vmBizInfoService.saveVmBizTaskInfo(vmBizInfo);
                    }
                    pbp1.setDealStatus("1");//修改为处理状态
                    versionPublicFlag = true;
                }
                log.debug("------------接收到上行短信手机号【"+telephone+"】，验证码【"+vfCode+"】流程处理结束--------------");

                pubBizPresmsService.updatePubBizPresms(pbp1);

                Map map = new HashMap<String, Object>();
                PubBizPresms pbp3 = new PubBizPresms();
                pbp3.setTelephone(telephone);
                pbp3.setSmsType("4");
                pbp3.setSmsState("0");
                pbp3.setInvalidationMark("1");
                pbp3.setInspectTime(DateUtils.dateTimeNow());
                pbp3.setSmsText("关于验证码为：" + vfCode + "的单子，您的操作已生效!");
                pbp3.setPubBizPresmsId(UUID.getUUIDStr());
                pubBizPresmsService.insertPubBizPresms(pbp3);
                map.put("convertFlag", "convert");
                pbp3.setParams(map);
                findPreSmsAndSend(pbp3);

            } catch (Exception e) {
                e.printStackTrace();
                log.error("短信处理任务失败！！！");
            }
        }
        return versionPublicFlag;
    }

    /**
     * 短链接生成器
     *
     * @param url
     * @return
     */
    public static String[] shortUrl(String url) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "javanotes.cn";
        // 要使用生成 URL 的字符
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
                "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        // 对传入网址进行 MD5 加密
        String sMD5EncryptResult = md5(key + url);
        String hex = sMD5EncryptResult;
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
            // long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

    /**
     * 加密
     *
     * @param source
     * @return
     */
    public static String md5(String source) {

        StringBuffer sb = new StringBuffer(32);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes("utf-8"));

            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    public boolean getFlag(CmBizSingle cbs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean flag = true;
        try {
            Date date1 = sdf.parse(DateUtils.timeToTimeMillis(cbs.getExpectStartTime()));
            flag = DateUtils.getNowDate().getTime() >= date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("-------------------短信配置开关:shiro.user.telEnabled=" + telEnabled + "," +
                "是否测试环境开关:shiro.user.isTest=" + isTest + ",配置telEnabled=true&isTest=false会链接短信--------------");
        if (telEnabled && !isTest) {
            log.debug("-------------------ITSM运维管理平台初始化短信链接开始------------------");
            //tcc = TestCMSLongConnectionAPI.getInstace();
            log.debug("-------------------ITSM运维管理平台初始化短信链接结束------------------");
        }
    }

}
