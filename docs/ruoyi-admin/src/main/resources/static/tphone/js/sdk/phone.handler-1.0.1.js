'use strict';

/**
 * agent proxy server后台消息回调
 */
var EChatPhoneMsgHandler = {


    /**
     * 连接proxy server成功回调
     */
    networkConnectedSuccess : function () {
    },

    /**
     * 断开proxy server回调
     */
    networkDisConnectedSuccess : function () {
    },
    /**
     * proxy server异常回调
     */
    networkConnectedError : function () {
    },

  /**
     * 座席状态改变
     *
     * @param obj
     */
    agentStatusChangedHandler: function (obj) {
        phoneUiKit.statusChanged(obj.agentstatus);
        if(obj.agentstatus == "Consult" || obj.agentstatus == "Established"){
          Agent.getCallInfo().callId = obj.connid;
          Agent.getCallInfo().otherconnid = obj.otherconnid;
        }
        if(obj.agentstatus){
          Agent.getCallInfo().agentstatus = obj.agentstatus;
        }
    },

    /**
     * 坐席注册Tproxyserver成功
     * @param obj
     */
    registerHandler: function (obj) {
        if (!obj) {
            return;
        }
      if(window.CONFIG.dntype == "softphone" || window.CONFIG.dntype == "xphone"){
        if(!AgoraPhoneHandler.aphoneOK && !AgoraPhoneHandler.xphoneOK){
          phonekit.AgoraPhone.connectToAgora(AgoraPhoneHandler.onMessageFromAPhone,AgoraPhoneHandler.onMessageFromXPhone);
          var myIframe = document.getElementById("myIframe");
          if (!myIframe) {
            myIframe = document.createElement("iframe");
            document.body.appendChild(myIframe);
            myIframe.id = "myIframe";
            myIframe.width = 0;
            myIframe.height = 0;
            myIframe.style.display = "none";
            myIframe.style.zIndex = -1000;
          }
          myIframe.src = "APhone://";
        }
      }
      if(window.CONFIG.dntype != "softphone" && window.CONFIG.dntype != "xphone"){
        if(!phoneSDK.currentAgent.isLogin){
          phoneSDK.requestLogin();
        }
      }
      if(AgoraPhoneHandler.aphoneOK || AgoraPhoneHandler.xphoneOK){
        if(!phoneSDK.currentAgent.isLogin){
          phoneSDK.requestLogin();
        }
      }
    },

    /**
     * 座席签入回调
     *
     * @paran obj
     */
    loginHandler: function (obj) {
      if(obj.stateid != "RequestRegisterDN"){
        phoneSDK.currentAgent.isLogin = true;
      }
    },

    /**
     * 座席签出回调
     *
     * @param obj
     */
    logoutHandler: function (obj) {
      phoneSDK.currentAgent.isLogin = false;
      AgoraPhoneHandler.aphoneOK = false;
      AgoraPhoneHandler.xphoneOK = false
      //签出成功后退出aphone,关闭软电话ws连接
      if(CONFIG.dntype == "softphone"){
        phonekit.AgoraPhone.logoutAphone();
        setTimeout(function () {
          phonekit.AgoraPhone.disconnectFromAgora();
        },200)
      }
      if(CONFIG.dntype == "xphone"){
        var logoutJson = {
          "requestid": "RequestAgentLogout",
          "agentid": phoneSDK.currentAgent.agentId,
          "tenantid": phoneSDK.currentAgent.tenantId
        }
        phonekit.AgoraPhone.sendMsgToAgora(JSON.stringify(logoutJson));
        setTimeout(function () {
          phonekit.AgoraPhone.disconnectFromAgora();
        },200)
      }
      phoneSDK.disconnectFromTProxy();
    },

    /**
     * 座席就绪and非就绪回调
     *
     * @paran obj
     */
    totalReadyOrNotReadyHandler: function (obj) {

    },
    /**
     * 座席就绪回调
     *
     * @paran obj
     */
    readyHandler: function (obj) {
      if (obj.stateid == "EventRegistered" && CONFIG.phone.autoReady == 'NotReady') {
        setTimeout(function () {
          phoneSDK.requestReady();
        },500)
      }
    },
    /**
     * 座席小休回调
     *
     * @paran obj
     */
    notReadyHandler: function (obj) {
      if (obj.stateid == "EventRegistered" && CONFIG.phone.autoReady == 'Ready') {
        setTimeout(function () {
          phoneSDK.requestNotReady(CONFIG.phone.notReadyReason);
        },500)
      }
    },
    /**
     * 话后处理回调
     *
     * @paran obj
     */
    afterCallWorkHandler: function (obj) {
      //录音标记初始化
      phonekit.AgoraPhone.sendRecord = false
      webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom,'','agentend')
    },

    /**
     * 座席振铃事件
     *
     * @param obj
     */
    chatRingingHandler: function (obj) {
        var userdata = {};
        try {
            userdata = JSON.parse(obj.userdata)[0];
        }
        catch(e) {
        }
/*      if(CONFIG.dntype == "webrtc" && (CONFIG.platform == "freelink" || CONFIG.platform == "freeswitch")){
        var userdata = {};
        try {
          userdata = JSON.parse(obj.userdata)[0];
          if(userdata.answerType == "2"){
            self.initWebrtc(userdata.conferenceName,userdata.fsHost,userdata.mgwUrl)
          }
        }
        catch (e) {
        }
      }*/
        // 进线号码,主叫号码
        Agent.getCallInfo().callNo =  obj.customernumber || obj.otherdn;
        Agent.getCallInfo().callId = obj.connid;
        Agent.getCallInfo().callingType = obj.calltype;
        Agent.getCallInfo().mobilPhone = obj.otherdn;
        Agent.getCallInfo().userdata = userdata;
        $("#phone_num"+phoneSDK.currentAgent.agentId).html(Agent.getCallInfo().callNo)
        if(obj.calltype == "Consult"){
          //坐席咨询来电
        }
        if(CONFIG.phone.autoAnswer){
            setTimeout(function () {
              phoneSDK.requestAnswerCall(obj.connid);
            },500)
        }
    },

    /**
     * 座席外呼拨号中
     *
     * @param obj
     */
    chatDialingHandler: function (obj) {
        // 座席外呼标志
      var userdata = {};
      try {
        userdata = JSON.parse(obj.userdata)[0];
      }
      catch(e) {
      }
      if(window.CONFIG.dntype == "xphone"){
        try {
          var joinConference = {
            "requestid": "RequestJoinConference",
            "conferenceName":"conf_"+obj.connid,
            "conferenceId":obj.connid,
            "fsAddress":userdata.fsHost,
            "agentid": phoneSDK.currentAgent.agentId,
            "tenantid": phoneSDK.currentAgent.tenantId,
            "recording": "true",
          }
            phonekit.AgoraPhone.sendMsgToAgora(JSON.stringify(joinConference));
        }
        catch(e) {
        }
      }
      if (obj.agentstatus == 'Dialing') {
        Agent.getCallInfo().callNo = obj.otherdn;
        Agent.getCallInfo().callId = obj.connid;
        Agent.getCallInfo().callingType = obj.calltype;
        Agent.getCallInfo().mobilPhone = obj.otherdn;
        Agent.getCallInfo().userdata = userdata;
      }
    },
  /**
   * 座席咨询拨号中事件
   *
   * @param obj
   */
  chatConsultDialingHandler: function (obj) {
    // 座席外呼标志
    var userdata = {};
    try {
      userdata = JSON.parse(obj.userdata)[0];
    }
    catch(e) {
    }
    if (obj.stateid == 'EventDialing') {
      Agent.getCallInfo().callNo = obj.otherdn;
      Agent.getCallInfo().callId = obj.otherconnid;
      Agent.getCallInfo().callingType = obj.calltype;
      Agent.getCallInfo().mobilPhone = obj.otherdn;
      Agent.getCallInfo().userdata = userdata;
    }
  },

    /**
     * 电话建立事件回调处理
     */
    establishedHandler: function (obj) {

      var userdata = {};
        try {
            userdata = JSON.parse(obj.userdata)[0];
        }
        catch(e) {
        }
        if(CONFIG.phone.freelink.startRecord){
          phonekit.AgoraPhone.startRecording()
        }
    },

      /**
       * 三方会议事件回调处理
       */
      conferencedHandler: function (obj) {

        var userdata = {};
        try {
          userdata = JSON.parse(obj.userdata)[0];
        }
        catch(e) {
        }
        if ( userdata.con_flag == '1' ) {
          //phoneSDK.requestHoldJson();
        }
      },

    /**
     * 求助电话建立事件回调处理
     */
    consultHandler: function (obj) {

      var userdata = {};
      try {
        userdata = JSON.parse(obj.userdata)[0];
      }
      catch(e) {
      }
      if ( userdata.con_flag == '1' ) {
       // phoneSDK.requestHoldJson();
      }
      Agent.getCallInfo().callId = obj.otherconnid;
    },

    /**
     * 系统随路数据
     */
    attachedDataChangedHandler: function (obj) {
      //Consult
      console.log("attachedDataChangedHandler")
      },

    /**
     * 三方验密会话成员离开
     */
    partyDeletedHandler: function (obj) {
        var agentStatus = obj.agentstatus;
        var con_flag = (JSON.parse(obj.userdata)[0]).con_flag;
        // 验密获取密码
        if ('Established' === agentStatus || 'Held' === agentStatus) {
            if ( con_flag == '1' && CONFIG.platform == "genesys") {
                //1、取回
               // phoneSDK.requestRetrieveCall();
                //2、更新随路数据
                var verifyData = {
                    'con_flag' : '0',
                    'r_hold' : '8'
                }
                phoneSDK.requestUpdateUserData(verifyData,obj.connid);
            }
        }
    },

    /**
     * 会话成员改变
     */
    partyChangedHandler: function (obj) {
        // 先咨询 后会议 或者 先咨询 后转移 会话成员改变
      console.log("partyChangedHandler")
        var userdata = {};
        try {
            userdata = JSON.parse(obj.userdata)[0];
        }
        catch(e) {
        }
    },

    /**
     * 会话成员加入
     */
    partyAddedHandler: function (obj) {
        var otherDn = obj.otherdn;
        var con_flag = (JSON.parse(obj.userdata)[0]).con_flag;
        // 三方会议
        if ('Conference' == obj.agentstatus) {
            if ( con_flag == '1' ) {
            }
        }
    },

    /**
     * 超时弃话
     *
     */
    revokedHandler: function (obj) {
        // 振铃超时，移除弹屏
    },

    /**
     * 主动弃话
     *
     */
    abandonedHandler: function (obj) {
        Agent.resetCallInfo();
    },

    /**
     * 挂机处理
     * @param obj
     */
    releasedHandler: function (obj) {
      if(window.CONFIG.dntype == "webrtc"){
        webRtcUiKit.leaveConference(obj.connid)
      }
    },

    /**
     * 保持事件
     * @param obj
     */
    heldHandler: function (obj) {
    },

    /**
     * 监听座席
     * @param obj
     */
    monitoringNextCallHandler: function (obj) {
    },
    /**
     * 预测
     * @param obj
     */
    phonePredictHandler: function (obj) {
    },
    /**
     * 预览
     * @param obj
     */
    phonePreviewHandler: function (obj) {

    },

    /**
     * 异常信息回调处理
     *
     * @param obj
     */
    phoneEventErrorHandler: function (obj) {
        if (!obj) {
            return;
        }
        // 签入失败，固定分机模式下分机不存在提示登录分机
        // {"ret":58,"data":"","errmsg":"Out of service","responseid":"RequestAgentLogin"}
        if (58 === obj.ret || 'Out of service' === obj.errmsg) {
            // 判断是否第二次连接tproxyserver失败
            // initPhoneSDK(Constants.tProxyServer);
        }
        // {"ret":59,"data":"","errmsg":"DN is not configured in CME","responseid":"RequestRegisterDN"}
        else if (59 === obj.ret) {
        }
        // {"ret":185,"data":"","errmsg":"Set is in wrong state for invocation","responseid":"RequestAgentReady"}
        else if (185 === obj.ret) {
        }
        // {"ret":192,"data":"","errmsg":"Agent ID is invalid","responseid":"RequestAgentLogin"}
        else if(192 === obj.ret) {
        }
        // {"ret":506,"data":"","errmsg":"Request received in an invalid state","responseid":"RequestAgentReady"} 分机不正确
        else if (506 === obj.ret) {
        }
        // 分机占用中 {"ret":1141,"data":"","errmsg":"Request incompatible with object","responseid":"RequestAgentLogin"}
        else if (1141 === obj.ret) {
        }
        // {"ret":9996,"data":"","errmsg":"Sign-in number is already registered with agent boc_5100186","responseid":"RequestRegisterDN"}
        else if (9996 === obj.ret) {
        }
        else if (1001 === obj.ret) {
        }
        else if (10000 === obj.ret) {
        }
        else if (10001 === obj.ret) {
        }
        else if ("000-003" === obj.ret) {
          //no right to invite resouce
        }
        else if ("7991" === obj.ret) {
          //agent login elsewhere.
        }
        else if ("8991" === obj.ret) {
          //request to server failed.
        }
        else {

        }
    }
};
