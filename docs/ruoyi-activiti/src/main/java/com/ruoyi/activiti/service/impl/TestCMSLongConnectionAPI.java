//package com.ruoyi.activiti.service.impl;
//
//import com.boco.common.UtilDate;
//import com.boco.vas.msplatform.api.partner.CMSLongConnectionAPI;
//import com.boco.vas.msplatform.api.partner.IMSLongConnectionAPIEvent;
//import com.boco.vas.msplatform.api.partner.command.*;
//import com.boco.vas.msplatform.common.CMSLongConnectionAPIErrorInfo;
//import com.boco.vas.msplatform.common.partnerprotocol.mms.CMSMultimediaMessage;
//import com.ruoyi.activiti.domain.CMSContext;
//import com.ruoyi.activiti.domain.PubBizPresms;
//import com.ruoyi.activiti.domain.PubBizSmsReceive;
//import com.ruoyi.activiti.service.IPubBizPresmsService;
//import com.ruoyi.activiti.service.IPubBizSmsReceiveService;
//import com.ruoyi.activiti.service.IPubBizSmsService;
//import com.ruoyi.common.utils.DateUtils;
//import com.ruoyi.common.utils.StringUtils;
//import com.ruoyi.common.utils.spring.SpringUtils;
//import com.ruoyi.common.utils.uuid.UUID;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 消息服务平台异步长连接API测试程序
// *
// * @author Administrator
// */
//public class TestCMSLongConnectionAPI implements IMSLongConnectionAPIEvent {
//
//    private final Logger logger = LoggerFactory.getLogger(TestCMSLongConnectionAPI.class);
//
//    public static TestCMSLongConnectionAPI api = new TestCMSLongConnectionAPI();
//    private int b;
//
//    public int getB() {
//        return b;
//    }
//
//    public void setB(int b) {
//        this.b = b;
//    }
//
//    CMSLongConnectionAPI pApi = null;
//    CMSContext cmsContext = null;
//
//    private TestCMSLongConnectionAPI() {
//        // 异步长连接api
//        pApi = new CMSLongConnectionAPI();
//        // 使用者自己定义的上下文管理对象
//        cmsContext = new CMSContext();
//        //获取tomcat的路径
//        String path = System.getProperty("catalina.home");
//        logger.debug("-------------------tomcat的路径为：" + path + "-------------------");
//        if(StringUtils.isEmpty(path) || path.contains("emp")){
//            path = "config/api.ini";
//        } else {
//            path = path +"/config/api.ini";
//        }
//        logger.debug("-------------------短信的完整配置路径为：" + path + "-------------------");
//        int iState = pApi.start(this, path, "client");
//        logger.debug("----ITSM链接短信平台状态描述信息:【"+CMSLongConnectionAPIErrorInfo.getErrorDescription(iState)+"】");
//    }
//
//    public static TestCMSLongConnectionAPI getInstace() {
//        return api;
//    }
//
//    /**
//     * 发送mt短信
//     *
//     * @return
//     */
//    public void SendShortMessage(String phone, String smstext,
//                                 String strBusinessCode) {
//        // 从本地的上下文管理对象中申请一个，上下文id
//        int iContext = cmsContext.nextContextId();
//        // mt消息的基本属性
//        CMSMTMessage MessageProperty = new CMSMTMessage();
//        MessageProperty.strDesMsisdn = phone;// 手机号
//        MessageProperty.strBusinessCode = strBusinessCode;// 参数(不变)
//        MessageProperty.strFeeMsisdn = phone;// 确认手机号
//        MessageProperty.strLinkId = "";
//        MessageProperty.bMediaType = 1;
//        MessageProperty.strLongCode = "";
//        MessageProperty.strServiceCode = "";
//        // MessageProperty.strSendTime = "2011-12-12 12:12:12";
//        // 命令的上下文
//        CMSCommandContext context = new CMSCommandContext();
//        // 上下为id，本属性是有使用者自己生成维护的
//        context.iContextId = iContext;
//        // 代理号，一般代理号与接入号一致
//        context.iProxyClientId = 311;// 客户帐号(配置文件中的ClientId)
//        // 调用查询黑名单命令的超时时间
//        context.sSecondsOfTimeout = 20;
//        MessageProperty.setContext(context);
//
//        // 短消息
//        CMSShortMessage objShortMessage = new CMSShortMessage();
//        objShortMessage.bContentType = 15;
//        objShortMessage.bPKNumber = 1;
//        objShortMessage.bPKTotal = 1;
//        objShortMessage.bTpPid = 0;
//        objShortMessage.bTpUdhi = 0;
//        objShortMessage.bLongSMS = 1;
//        objShortMessage.strContent = smstext;// 发送短信内容
//
//        // 将"mt消息的基本属性对象"添加到本地的上下文管理对象
//        cmsContext.addCommand(iContext, MessageProperty);
//        // 发送短消息
//        int iState = pApi.MSSendShortMessage(MessageProperty, objShortMessage);
//        if (iState == CMSLongConnectionAPIErrorInfo.CMSLONGCONNECTIONAPI_DISPOSE_SUCCEED) {
//            // 由于本api采用异步方式传输数据，所以此处返回成功，不能表示数据已经成功发送到消息服务平台，只能表示成功的发送MT短信的命令已经被成功添加
//            // 要想知道发送短信的结果可以在
//            // OnMSLongConnectionAPIEvent_SendMessageRet方法中获得具体结果
//        } else {
//            cmsContext.removeCommand(iContext);
//            //获取tomcat的路径
//            String path = System.getProperty("catalina.home");
//            logger.debug("-----tomcat的路径为：" + path);
//            if(StringUtils.isEmpty(path) || path.contains("emp")){
//                path = "config/api.ini";
//            } else {
//                path = path +"/config/api.ini";
//            }
//            pApi.start(this, path, "client");
//            this.SendShortMessage(phone, smstext, strBusinessCode);
//            logger.debug("-----添加发送MT短信命令出现错误,错误代码是："
//                            + iState
//                            + " 错误信息是："
//                            + CMSLongConnectionAPIErrorInfo
//                            .getErrorDescription(iState));
//        }
//        // }
//    }
//
//    /**
//     * 发送MT消息的处理结果事件
//     */
//    public void OnMSLongConnectionAPIEvent_SendMessageRet(
//            CMSMTMessageRet messageRet) {
//        // 从本地的上下文管理对象中，取出 "MT消息的基本属性"
//        CMSMTMessage cmd = (CMSMTMessage) cmsContext.removeCommand(messageRet
//                .getIContextId());
//        if (messageRet.iResult == CMSLongConnectionAPIErrorInfo.CMSLONGCONNECTIONAPI_DISPOSE_SUCCEED) {
//            // MT消息已经被成功的发送到了消息服务平台
//            logger.debug("-----发送到:" + cmd.getStrDesMsisdn() + "的消息已经被成功发送!!"
//                    + messageRet.getStrMessageId());
//            // pApi.stop();
//            b = 1;
//        } else {
//            logger.debug("-----发送到:" + cmd.getStrDesMsisdn()
//                    + "的消息，在发送过程中出现错误，错误代码是：" + messageRet.getIResult()
//                    + " 错误信息是：" + messageRet.getStrErrorDescription());
//            b = 2;
//        }
//    }
//
//    // 接收短信
//    public void OnMSLongConnectionAPIEvent_ReceiveShortMessage(int serviceId,
//                                                               CMSMOMessage message, CMSShortMessage shortMessage) {
//        // 此方法用于接收MO短信
//        // logger.debug("接收到的短信内容为------："+shortMessage.strContent);
//        // logger.debug("发送手机号为-----："+message.getStrMsisdn());
//        // logger.debug("发送时间是："+DateUtils.getToday("yyyyMMddHHmmss"));
//        String telephone = message.getStrMsisdn();
//        String Smstexts = shortMessage.strContent;
//        String setSmstext = Smstexts.toLowerCase();
//        pApi.MSSetCommandDisposeResult(message.getIContextId(), 0, "");
//        try {
//
//            String a[] = setSmstext.split("\\+");
//
//            String vfCode = a[1];
//
//            receiveFlow(vfCode, telephone, setSmstext);
//
//        } catch (Exception e) {
//            logger.warn("-----流程处理失败" + setSmstext + ":" + e.getMessage());
//            PubBizPresms pbp = new PubBizPresms();
//            pbp.setPubBizPresmsId(UUID.getUUIDStr());
//            pbp.setTelephone(telephone);
//            pbp.setSmsType("4");
//            pbp.setSmsState("0");
//            pbp.setInvalidationMark("1");
//            pbp.setInspectTime(DateUtils.dateTimeNow());
//            pbp.setSmsText("短信格式输入有误或流程已处理，请检查后重新发送!");
//            IPubBizPresmsService pubBizPresmsService = SpringUtils.getBean(IPubBizPresmsService.class);
//            pubBizPresmsService.insertPubBizPresms(pbp);//保存存储短信表
//            IPubBizSmsService pubBizSmsManager = SpringUtils.getBean(IPubBizSmsService.class);
//            Map<String, Object> map = new HashMap<>();
//            map.put("convertFlag", "convert");
//            pbp.setParams(map);
//            pubBizSmsManager.findPreSmsAndSend(pbp);//发送短信
//        }
//    }
//
//    public void receiveFlow(String vfCode, String telephone, String setSmstext) {
//        PubBizSmsReceive pbsr = new PubBizSmsReceive();
//        pbsr.setId(UUID.getUUIDStr());
//        pbsr.setVerificationcode(vfCode);//验证码
//        pbsr.setTelephone(telephone);//手机号
//        pbsr.setSendTime(DateUtils.dateTimeNow());//短信收到时间
//        pbsr.setSmsText(setSmstext);//短信内容
//        IPubBizSmsReceiveService pubBizSmsReceiveService = SpringUtils.getBean(IPubBizSmsReceiveService.class);
//        pubBizSmsReceiveService.insertPubBizSmsReceive(pbsr);//保存接收短信
//
//        IPubBizPresmsService pubBizPresmsService = SpringUtils.getBean(IPubBizPresmsService.class);
//        IPubBizSmsService pubBizSmsManager = SpringUtils.getBean(IPubBizSmsService.class);
//        if ("all".equals(vfCode.toLowerCase())) {
//            PubBizPresms pbAll = new PubBizPresms();
//            pbAll.setTelephone(telephone);
//            pbAll.setDealStatus("0");
//            List<PubBizPresms> pbps = pubBizPresmsService.selectPubBizPresmsList(pbAll);//查询没有处理完的任务
//            if (!pbps.isEmpty()) {
//                logger.debug("-------手机号【"+telephone+"】回复上行关键字【all】查询到的版本单总数为:" + pbps.size());
//                logger.debug("--------查询到的版本单集合为:" + pbps);
//                int i = 0;
//                for (PubBizPresms pbp : pbps) {
//                    try {
//                        boolean flag = pubBizSmsManager.ReceiveFlow(pbp.getVerificationcode(), pbp.getTelephone());
//                        // 返回如果是版本短信上行审批，则计数i+1，否则不计数
//                        if(flag){
//                            i++;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        logger.warn("-----流程批处理失败" + e.getMessage());
//                    }
//                }
//                Map<String, Object> map = new HashMap<>();
//                PubBizPresms pp = new PubBizPresms();
//                pp.setTelephone(telephone);
//                pp.setSmsType("4");
//                pp.setSmsState("0");
//                pp.setInvalidationMark("1");
//                pp.setInspectTime(DateUtils.dateTimeNow());
//                pp.setSmsText("已成功审批" + i + "个单子.");
//                pp.setPubBizPresmsId(UUID.getUUIDStr());
//                pubBizPresmsService.insertPubBizPresms(pp);
//
//                map.put("convertFlag", "convert");
//                pp.setParams(map);
//                pubBizSmsManager.findPreSmsAndSend(pp);
//
//            } else {
//                Map<String, Object> map = new HashMap<>();
//                // 如果回check all 没有需要审批的单子 回复短信给用户
//                PubBizPresms pp = new PubBizPresms();
//                pp.setTelephone(telephone);
//                pp.setSmsType("4");
//                pp.setSmsState("0");
//                pp.setInvalidationMark("1");
//                pp.setInspectTime(DateUtils.dateTimeNow());
//                pp.setSmsText("目前没有需要审批的版本单.");
//                pp.setPubBizPresmsId(UUID.getUUIDStr());
//                pubBizPresmsService.insertPubBizPresms(pp);
//
//                map.put("convertFlag", "convert");
//                pp.setParams(map);
//                pubBizSmsManager.findPreSmsAndSend(pp);
//            }
//        } else {
//            pubBizSmsManager.ReceiveFlow(vfCode, telephone);
//        }
//    }
//
//    public void OnMSLongConnectionAPIEvent_AddWhiteMsisdnRet(
//            CMSRequestRet result) {
//        // 从本地的上下文管理对象中，取出 添加黑名单协议的命令
//        CMSAddWhiteMsisdnCommand cmd = (CMSAddWhiteMsisdnCommand) cmsContext
//                .removeCommand(result.getIContextId());
//        if (result.iResult == CMSLongConnectionAPIErrorInfo.CMSLONGCONNECTIONAPI_DISPOSE_SUCCEED) {
//            // 此处表示黑名单已经被成功的添加到消息服务平台了
//            logger.debug("-----手机号是:" + cmd.getStrMsisdn()
//                    + "的白名单命令已经被成功的添加到消息服务平台了!!");
//        } else {
//            logger.debug("-----手机号是:" + cmd.getStrMsisdn()
//                    + "的白名单命令在添加过程中出现了错误，错误代码是：" + result.getIResult()
//                    + " 错误信息是：" + result.getStrErrorInfo());
//        }
//    }
//
//    public void OnMSLongConnectionAPIEvent_QueryWhiteMsisdnRet(
//            CMSRequestRet result) {
//        // 从本地的上下文管理对象中，取出 添加白名单协议的命令
//        CMSAddWhiteMsisdnCommand cmd = (CMSAddWhiteMsisdnCommand) cmsContext
//                .removeCommand(result.getIContextId());
//        if (result.iResult == CMSLongConnectionAPIErrorInfo.CMSLONGCONNECTIONAPI_DISPOSE_SUCCEED) {
//            // 此处表示白名单已经被成功的添加到消息服务平台了
//            logger.debug("-----手机号是:" + cmd.getStrMsisdn()
//                    + "的白名单命令已经被成功的添加到消息服务平台了!!");
//        } else {
//            logger.debug("-----手机号是:" + cmd.getStrMsisdn()
//                    + "的白名单命令在添加过程中出现了错误，错误代码是：" + result.getIResult()
//                    + " 错误信息是：" + result.getStrErrorInfo());
//        }
//    }
//
//    @Override
//    public void OnMSLongConnectionAPIEvent_AddBlackMsisdnRet(
//            CMSRequestRet result) {
//        // 从本地的上下文管理对象中，取出 添加黑名单协议的命令
//        CMSAddBlackMsisdnCommand cmd = (CMSAddBlackMsisdnCommand) cmsContext
//                .removeCommand(result.getIContextId());
//        if (result.iResult == CMSLongConnectionAPIErrorInfo.CMSLONGCONNECTIONAPI_DISPOSE_SUCCEED) {
//            // 此处表示黑名单已经被成功的添加到消息服务平台了
//            logger.debug("-----手机号是:" + cmd.getStrMsisdn()
//                    + "的黑名单命令已经被成功的添加到消息服务平台了!!");
//        } else {
//            logger.debug("-----手机号是:" + cmd.getStrMsisdn()
//                    + "的黑名单命令在添加过程中出现了错误，错误代码是：" + result.getIResult()
//                    + " 错误信息是：" + result.getStrErrorInfo());
//        }
//    }
//
//    public void OnMSLongConnectionAPIEvent_ActiveSubscribeRet(
//            CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_PauseSubscribeRet(
//            CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_RequestCancelAllRet(
//            CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_RequestCancelRet(CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_RequestSubscribeRet(
//            CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_SYNCancelRet(CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_SYNSubscribeRet(CMSRequestRet result) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_CloseAllConnections(int serviceId) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_ConnectStatusNotify(int serviceId,
//                                                               int result, String strDescription) {
//        logger.debug("-----" + UtilDate.getDateTime(new Date()) + ":状态通知:"
//                + strDescription);
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_CreateConnection(int serviceId) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_Timer(int serviceId) {
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_DeleteWhiteMsisdnRet(
//            CMSRequestRet result) {
//        // TODO Auto-generated method stub
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_SendBillRet(CMSBillRet result) {
//        // TODO Auto-generated method stub
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_ReceiveSubscribeNotify(
//            int serviceId, CMSSubscriberNotify subscriberNotify) {
//        // TODO Auto-generated method stub
//
//    }
//
//    public void OnMSLongConnectionAPIEvent_ManageCommandRet(
//            CMSManageCommandRet commandRet) {
//        logger.debug("-----收到管理命令返回strAdditionalInformation:"
//                + commandRet.strAdditionalInformation + "strErrorInfo"
//                + commandRet.strErrorInfo);
//
//    }
//
//    @Override
//    public void OnMSLongConnectionAPIEvent_DeleteBlackMsisdnRet(
//            CMSRequestRet arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void OnMSLongConnectionAPIEvent_QueryBlackMsisdnRet(
//            CMSRequestRet arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void OnMSLongConnectionAPIEvent_ReceiveMultimediaMessage(int arg0,
//                                                                    CMSMOMessage arg1, CMSMultimediaMessage arg2) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void OnMSLongConnectionAPIEvent_ReceiveStatusReport(int arg0,
//                                                               CMSStatusReport arg1) {
//        // TODO Auto-generated method stub
//
//    }
//}
