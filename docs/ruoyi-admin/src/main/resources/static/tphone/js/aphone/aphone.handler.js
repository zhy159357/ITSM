var AgoraPhoneHandler = {
  receiveRecordOK : true,
  aphoneOK : false,
  xphoneOK : false,
  aphoneClose : false,
  aphoneLoginReq :false,

  onMessageFromAPhone : function (msg) {
    if(typeof(msg) == "string"){
      var str = msg;
      CommonApi.logDebugMessage(str)
      if(str.indexOf("EventAphoneConnected")>-1){
        AgoraPhoneHandler.aphoneConnectedSuccess();
      }
      if(str.indexOf("EventXphoneConnected")>-1){
        AgoraPhoneHandler.xphoneConnectedSuccess();
      }
      if (msg.indexOf("EventLocalIP") != -1){
        AgoraPhoneHandler.ipReciveSuccess();
      }
      if (msg.indexOf("EventAphoneOK") != -1){
        AgoraPhoneHandler.AphoneRegisterSuccess();
      }
      if (msg.indexOf("EventAPhoneClose") != -1){
        AgoraPhoneHandler.AphoneClose();
      }
      if (msg.indexOf("EventAPhoneError") != -1){
        AgoraPhoneHandler.AphoneConntectError();
      }
      if (msg.indexOf("EventConnectFailed") != -1){
        AgoraPhoneHandler.AphoneConnectFailed();
      }
      if (msg.indexOf("EventAPhoneException") != -1){
        AgoraPhoneHandler.aphoneException();
      }
      if (msg.indexOf("recordOK") != -1){
        AgoraPhoneHandler.AphoneRecordOK();
      }
      if (msg.indexOf("CallNotExist") != -1){
        AgoraPhoneHandler.CallNotExist();
      }
      if (msg.indexOf("EventXphoneOK") != -1){
        AgoraPhoneHandler.CallNotExist();
      }
      if (msg.indexOf("CallNotExist") != -1){
        AgoraPhoneHandler.CallNotExist();
      }
    }else{
      AgoraPhoneHandler.onMessageFromXPhone(msg)
    }
  },
  onMessageFromXPhone : function (msg) {
    CommonApi.logDebugMessage(JSON.stringify(msg))
    if(msg.responseid == "RequestAgentLogin"){
        if(msg.ret == 0){
          AgoraPhoneHandler.XphoneRegisterSuccess()
        }
    }
  },
  //Aphone连接成功
  aphoneConnectedSuccess: function () {
    if(AgoraPhoneHandler.aphoneOK){
        return;
    }
    var options = {
      password : phoneSDK.currentAgent.password,
      tenantId:phoneSDK.currentAgent.tenantId,
      agentId:phoneSDK.currentAgent.agentId,
      extension:phoneSDK.currentAgent.extension
      /*"egoo3466""20002""ems""1006@47.99.197.109:9960"*/
    }
    phonekit.AgoraPhone.registerAphone(options);
  },
  //xphone连接成功
  xphoneConnectedSuccess: function () {
    CONFIG.dntype = "xphone"
    phoneSDK.currentAgent.extension = phoneSDK.currentAgent.agentId
    if(AgoraPhoneHandler.xphoneOK){
      return;
    }
    var registXphone = {
      "requestid": "RequestAgentLogin",
      "agentid": phoneSDK.currentAgent.agentId,
      "thisdn": phoneSDK.currentAgent.extension,
      "dnPwd": phoneSDK.currentAgent.password,
      "tenantid": phoneSDK.currentAgent.tenantId
    }
    phonekit.AgoraPhone.sendMsgToAgora(JSON.stringify(registXphone));
  },
  //ip获取成功
  ipReciveSuccess: function () {
  },
  //Aphone注册成功
  AphoneRegisterSuccess: function () {
    //电话注册成功后连接Tproxyserver
    if(!AgoraPhoneHandler.aphoneOK){
      AgoraPhoneHandler.aphoneOK = true;
      if(!phoneSDK.currentAgent.isLogin){
        phoneSDK.requestLogin();
      }
    }
  },
  XphoneRegisterSuccess: function () {
    //电话注册成功后连接Tproxyserver
    if(!AgoraPhoneHandler.xphoneOK){
      AgoraPhoneHandler.xphoneOK = true;
      if(!phoneSDK.currentAgent.isLogin){
        phoneSDK.requestLogin();
      }
    }
  },
  //主动关闭连接成功
  AphoneClose: function () {
    if(!AgoraPhoneHandler.aphoneClose && AgoraPhoneHandler.aphoneOK){
      AgoraPhoneHandler.aphoneClose = true;
      AgoraPhoneHandler.aphoneLoginReq = false
      phoneSDK.requestLogout()
    }
  },
  //网络异常连接中断，自动重连
  AphoneConntectError: function () {
  },
  //Aphone连接失败
  AphoneConnectFailed: function () {
  },
  //Aphone连接成功
  aphoneException: function () {
    alert("1请检查aphone是否安装或正常启动");
  },
  AphoneRecordOK:function () {
    CommonApi.logDebugMessage("录音成功")
  },
  CallNotExist:function () {
    phoneSDK.setCallNotExist();
    CommonApi.logDebugMessage("当前通话不存在")

  }
};
