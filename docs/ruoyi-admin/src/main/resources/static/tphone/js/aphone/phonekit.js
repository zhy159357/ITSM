"use strict";
var phonekit = (function () {
    var initModule,  canReadyInterface = null, sbcIp = null,AgoraPhoneHandler = null;
    var setLoginClickEvent;

    var AgoraPhone = {
        pingInterval : null,
        ws: null,
        AphoneMsgHandler: null,
        XphoneMsgHandler: null,
        currentState : "open",
        sendRecord : false,
        tpconnectFlag : false,
        installFlag : false,
        receiveRecordOK : false,

        /*
         * 连接APhone
         */
        connectToAgora: function (amsgHandler,xmsgHandler) {
            AgoraPhone.AphoneMsgHandler = amsgHandler;
            AgoraPhone.XphoneMsgHandler = xmsgHandler;
                if(AgoraPhone.getBrowserInfo()){
                  if(!client1.object){
                    AgoraPhone.AphoneMsgHandler("EventAPhoneException");
                    return;
                  }
                  client1.Open(CONFIG.phone.freelink.aphoneUrl);
                  return;
                }
                try {
                    AgoraPhone.ws = new ReconnectingWebSocket(CONFIG.phone.freelink.aphoneUrl);
                    console.log("调用 ReconnectingWebSocket")
                    AgoraPhone.ws.onopen = function (event) {
                      //AgoraPhone.pingInterval = setInterval(AgoraPhone.sendPing,5000);
                      AgoraPhone.installFlag = true
                      CommonApi.logDebugMessage("onMessageFromAPhone onopen ")
                        if (AgoraPhone.currentState != 'ok') {
                              AgoraPhone.onOpen();
                        }
                    };

                    /*
                     * 获取到 APhone 发送来的消息
                     */
                    AgoraPhone.ws.onmessage = function (event) {
                        var str = event.data;
                        try {
                          if(typeof(JSON.parse(str)) == "object")
                          AgoraPhone.XphoneMsgHandler(JSON.parse(str));
                        }catch (e) {

                        }

                        if(str.indexOf("EventLocalIP")>-1){
                        	var localIp = str.substring(13,str.length);
                        	//phoneSDK.saveLocalIp(localIp);
                        }
                        if (str.indexOf("EventAphoneOK") != -1){
                          if(!AgoraPhone.tpconnectFlag){
                            AgoraPhone.tpconnectFlag = true;
                            AgoraPhone.AphoneMsgHandler(str);
                            //phoneSDK.connectToTProxy();
                          }
			                  }
                        if (str.indexOf("recordOK") != -1){
                          AgoraPhone.AphoneMsgHandler("recordOK");
                          AgoraPhone.recordResult = true
                        }
                        if (str.indexOf("CallNotExist") != -1){
                          AgoraPhone.AphoneMsgHandler("CallNotExist");
                        }
                        if (str.indexOf("EventXphoneConnected") > -1){
                          AgoraPhone.AphoneMsgHandler("EventXphoneConnected");
                        }
                    };

                    AgoraPhone.ws.onclose = function (event) {
						            AgoraPhone.onClose();
						            AgoraPhone.tpconnectFlag = false;
                        clearInterval(AgoraPhone.pingInterval);
                    };

                    AgoraPhone.ws.onerror = function (event) {
                    	AgoraPhone.tpconnectFlag = false;
                      AgoraPhone.onError();
                      clearInterval(AgoraPhone.pingInterval);
                    };
                } catch (ex) {
                  CommonApi.logDebugMessage(new Date +ex.message)
                }
                //5s后检测与aphone是否建立了ws连接
                if(!AgoraPhone.getBrowserInfo() && CONFIG.phone.freelink.checkAphoneTimeLong){
                  setTimeout(function () {
                      if(!AgoraPhone.installFlag){
                        AgoraPhone.AphoneMsgHandler("EventAPhoneException");
                      }
                  },CONFIG.phone.freelink.checkAphoneTimeLong*1000)
                }
        },

        /*
         * 断开 APhone 的连接
         */
        disconnectFromAgora: function () {

					if (AgoraPhone.getBrowserInfo()){
              client1.Close();
              CommonApi.logDebugMessage(new Date +"aphone断开连接")
              return;
          }
          if (AgoraPhone.ws != null) {
              AgoraPhone.ws.close();
              CommonApi.logDebugMessage(new Date +"aphone断开连接")
          }

        },

        /*
         * 发送消息给 APhone
         */
        sendMsgToAgora: function (msg) {
          CommonApi.logDebugMessage("sendMsgToAgora:"+msg)
            if (AgoraPhone.getBrowserInfo()){
                client1.Send(msg);
            }else {
                if (AgoraPhone.ws != null) {
                    try {
                        AgoraPhone.ws.send(msg);
                    }
                    catch (ex) {
                        CommonApi.logDebugMessage(new Date +ex.message)
                    }
                }
            }
        },

        onOpen : function () {
            AgoraPhone.installFlag = true
			      if (AgoraPhone.currentState == 'ok') {
                return;
            }
            AgoraPhone.AphoneMsgHandler("EventAphoneConnected");
            AgoraPhone.pingInterval = setInterval(AgoraPhone.sendPing,5000);
        },
        onClose : function () {
            clearInterval(AgoraPhone.pingInterval);
            AgoraPhone.AphoneMsgHandler("EventAPhoneClose");
        },
        onError : function () {
          AgoraPhone.AphoneMsgHandler("EventAPhoneError");
        },

        sendPing : function (){
          if (AgoraPhone.getBrowserInfo()){
                client1.Send("ping");
            }else {
                if (AgoraPhone.ws != null) {
                    try {
                        AgoraPhone.ws.send("ping");
                    }
                    catch (ex) {
                      CommonApi.logDebugMessage(new Date +ex.message)
                    }
                }
            }
        },

       registerAphone : function (options) {
         var password = options.password;
          var agentId = options.agentId;
          var tenantId = options.tenantId;
          var extension = options.extension;
          if(agentId.indexOf("_") !== -1){
            agentId = agentId.slice(agentId.indexOf("_")+1);
          }
          if(extension!= null && extension != ''&& extension != undefined){
            var atIndex = extension.indexOf("@");
            var maohaoIndex = extension.indexOf(":");
            var ip = extension.slice(atIndex+1,maohaoIndex);
            var port =  extension.slice(maohaoIndex+1);
            var getextension = extension.slice(0,extension.indexOf("@"));
            AgoraPhone.sendMsgToAgora('RequestLogin|1|'+getextension+'|'+password+'|'+ip+'|'+port+'|'+tenantId+'|'+agentId);
          }
        },
        logoutAphone : function () {
          AgoraPhone.sendMsgToAgora("RequestLogout");
          phonekit.AgoraPhone.tpconnectFlag = false;
        },
        runAphone : function () {
          window.location.href = "APhone://"
        },
        startRecording : function () {
          if(!phonekit.AgoraPhone.sendRecord){
            phonekit.AgoraPhone.sendRecord = true
            AgoraPhone.sendMsgToAgora(  'CallEstablished|' + Agent.getCallInfo().callId + '|true');
            phonekit.AgoraPhone.checkRecordSuccess();
          }
        },
      checkRecordSuccess : function () {
        phonekit.AgoraPhone.receiveRecordOK = false;

        setTimeout(function () {
          if (phonekit.AgoraPhone.receiveRecordOK){
            console.log("checkRecordSuccess : true");
          } else {
            phonekit.AgoraPhone.startRecording()
          }
        },2000);
      },
       getBrowserInfo : function(){
/*         var agent = navigator.userAgent.toLowerCase();
         var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1); //判断是否IE<11浏览器
         if (isIE) {
           var reIE = new RegExp("msie (\\d+\\.\\d+);");
           reIE.test(agent);
           var fIEVersion = parseFloat(RegExp["$1"]);
           if (fIEVersion == 7 || fIEVersion == 8 || fIEVersion == 9) {
             return true;
           }else{
             return false;
           }
         }
         if (!isIE) {
           return false;
         }*/
         var isIE =  !!window.ActiveXObject || "ActiveXObject" in window
         if (isIE) {
           return true;
         }else {
           return false;
         }
      }

    };





    var durationTimer = null;
    var callDuration = 0;

    var skillGroupListMap = new Array();
    var loginTag          = 0;
    var agentLock         = false;
    var heartBeatID       = 1000;



    return {
        initModule : initModule,
        AgoraPhone : AgoraPhone,
        AgoraPhoneHandler : AgoraPhoneHandler,
        canReadyInterface : canReadyInterface
    };
}());
