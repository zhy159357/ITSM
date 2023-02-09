
/**************************************************************************************************
 ***************************************** 本地连接器 **********************************************
 **************************************************************************************************/
//本地chat
// 启动xchat插件连接服务器
var lws = null;
// 心跳检测定时器
var pingLwsInterval = null;

// 本地连接器封装
var localWsConnector = (function (){
  var type, setType, onOpen, onMessage, onClose, onError, wPhoneTimer;
  setType = function(type1) {
    type = type1;
  };
  onOpen = function () {
    Utils.log.info("lws.onopen--------------------------------------->>");
    // if (window.wPhoneTimer) {
    //   clearInterval(window.wPhoneTimer);
    // }
    if (pingLwsInterval) {
      clearInterval(pingLwsInterval);
    }
    pingLwsInterval = setInterval(pingPongLws, 2000);
  };
  onMessage = function(data1) {
    Utils.log.info("lws.onmessage:" + JSON.stringify(data1));
    if (!data1.data && data1.data == 'pong') {
      return;
    }
    var data = {}
    if(Utils.isIE()){
      data = JSON.parse(data1)
    }else{
      data = JSON.parse(data1.data);
    }
    if(data.sendFrom == "WPhone"){
      if ('EventOverWPhone' === data.msgType) {
        xChatWPhoneEnd(data.roomId);
      }
      if ('EventSetMyjson' === data.msgType) {
        webRtcUiKit.myjson = data.myjson
      }
      if ('EventGetAuths' === data.msgType) {
        var obj = new Object()
        obj.msgType = "EventSendAuths"
        obj.sessionId = $("#chatroom").val()
        obj.fromuser = $("#fromuser").val()
        obj.channelType = $("#channeltype").val()
        obj.makeType = makeType
        obj.tenantId = tenantId
        obj.turn = window.CONFIG.RTC.turn
        obj.camera = window.CONFIG.RTC.camera
        var auths = new Object()
        obj.auths = auths
        sendMsgToChrom(obj)
      }
    }
  };
  onClose = function() {
    Utils.log.info("lws.onclose");
    if(pingLwsInterval) {
      clearInterval(pingLwsInterval);
    }
  };
  onError = function() {
    Utils.log.info("lws.onerror");
  };
  sendMsg = function(msg) {
    if(Utils.isIE()){
      chatclient.Send(JSON.stringify(msg));
    }else{
      lws.send(JSON.stringify(msg));
    }
    Utils.log.info("lws send msg:"+JSON.stringify(msg));
  };
  playVideo = function(url) {
    var json = new Object()
    json.msgtype="StartChromePlaying"
    json.content=url
    localWsConnector.sendMsg(json);
  };
  openSetCamera = function() {
    var json = new Object()
    json.msgtype="StartChromePlaying"
    json.content=CONFIG.RTC.setCameraHtml
    localWsConnector.sendMsg(json);
  };

  // 检测是否安装插件
  // wPhoneTimer = function() {
  //   window.wPhoneTimer = setTimeout(function () {
  //     lws && lws.close();
  //     lws = null;
  //     clearTimeout(window.wPhoneTimer);
  //     var msg = '';
  //     if ('share' == type) {
  //       msg = '远程协助';
  //       xChatWPhoneEnd();
  //     } else if ('video' == type) {
  //       msg = '视频';
  //       xChatWPhoneEnd();
  //     }
  //     // 0.5秒后弹出弹框
  //     setTimeout(function(){
  //       globalUiKit.confirm('提示: ', '您未安装<a href="' + window.CONFIG.wPhoneUrl + '" target="_blank" style="color:red">WPhone</a>插件，无法发起' + msg + '请求！');
  //     }, 500);
  //
  //     // 解绑刷新事件
  //     setTimeout(function(){
  //       window.onbeforeunload = null;
  //       window.location.href = 'wphone://closewindow';
  //     },1000)
  //     // 重新绑定刷新事件
  //     setTimeout(onBeforeUnloadAction, 1000);
  //   }, window.CONFIG.xChatCheckTime);
  // };
  return{
    setType: setType,
    onOpen: onOpen,
    onMessage: onMessage,
    onClose: onClose,
    onError: onError,
    playVideo: playVideo,
    sendMsg: sendMsg,
    openSetCamera:openSetCamera
    // wPhoneTimer: wPhoneTimer
  }

})();

function localWebSocket(type) {
  try {
    Utils.log.info("localWebSocket isie=" + Utils.isIE());
    localWsConnector.setType(type);
    // localWsConnector.wPhoneTimer();
    if (Utils.isIE()) {
      lws && lws.close();
      lws = null;
      if (!lws) {
        Utils.log.info("localWebSocket CONFIG.RTC.MGW_WPhone=" + CONFIG.RTC.MGW_WPhone);
        chatclient.Open(CONFIG.RTC.MGW_WPhone);
        lws = chatclient;
      }
    } else {
      lws && lws.close();
      lws = null;
      if (!lws) {
        Utils.log.info("localWebSocket CONFIG.RTC.MGW_WPhone=" + CONFIG.RTC.MGW_WPhone);
        lws = new ReconnectingWebSocket(CONFIG.RTC.MGW_WPhone);
      }
      lws.onopen = function () {
        localWsConnector.onOpen();
      }

      lws.onmessage = function (data) {
        localWsConnector.onMessage(data);
      }

      lws.onconnecting = function (event) {
        Utils.log.info("lws connecting");
      }

      lws.onclose = function () {
        localWsConnector.onClose();
      }

      lws.onerror = function () {
        localWsConnector.onError();
      }
    }
  }
  catch (e) {
    console.error(e)
  }
}

function pingPongLws() {
  try {
    if(Utils.isIE()){
      chatclient.Send('ping');
    }else{
      lws.send('ping');
    }
    console.log('ping');
  } catch(ex) {
    Utils.log.info('发送心跳信息异常：' + ex.message);
  }
}
function sendMsgToChrom(obj) {
  try {
    if(CONFIG.RTC.browserType == "WPhone"){
      if(lws){
        obj.sendFrom = "chat"
        if(Utils.isIE()){
          chatclient.Send(JSON.stringify(obj));
        }else{
          lws.send(JSON.stringify(obj));
        }
        console.log('sendMsgToChrom='+JSON.stringify(obj));
      }
    }
  }catch (e) {

  }
}
function wphoneSendMsgToChat(obj) {
  if(CONFIG.RTC.browserType == "WPhone"){
    obj.sendFrom = "WPhone"
    if(xws){
      try {
        xws.send(JSON.stringify(obj));
        console.log('wphoneSendMsgToCha='+JSON.stringify(obj));
      }catch (e) {

      }
    }
  }
}


function xChatWPhoneEnd(sessionId) {
  // 座席端提示，并发起结束视频消息
  console.log('xChatWPhoneEnd:'+sessionId);
  try{
    var curUser = Model.people.get_by_sid(sessionId);
    if(!curUser){
      return;
    }
      var sessionId = curUser.sid;
      var userId = curUser.uid;
      var chatType = curUser.chatType;
      var chatMode = 'normal';
      var agentId = chatSDK.currentAgent.agentId;
      var messageText = new Object();
      messageText.msgtype = 'text';
      messageText.content = '';
      messageText.touser = userId;
      messageText.fromuser = agentId;
      messageText.channeltype = chatType;
      messageText.interactionid = sessionId;
      messageText.chatmode = chatMode;
      messageText.enablevideo = 'hangup';
      var userData = new Object();
      userData.readid = new Date().getTime();
      userData.msgreaded =  false;
      messageText.userdata = userData;
      chatSDK.sendChatMsg(sessionId, messageText);
  }catch(e){
    //TODO handle the exception
  }
}

function setVideoIframeSrc(url) {
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
  myIframe.src = url;
}
function setFileIframeSrc(url) {
  var myIframe = document.getElementById("myFileIframe");
  if (!myIframe) {
    myIframe = document.createElement("iframe");
    document.body.appendChild(myIframe);
    myIframe.id = "myFileIframe";
    myIframe.width = 0;
    myIframe.height = 0;
    myIframe.style.display = "none";
    myIframe.style.zIndex = -1000;
  }
  myIframe.src = url;
}

/**************************************************************************************************
 ***************************************** 本地连接器 **********************************************
 **************************************************************************************************/
//wphone 弹窗
var xws = null;
function localWPhoneSocket(roomId,makeType,opttype) {
  var heartBeatTimer = null;
  xws = new ReconnectingWebSocket(CONFIG.RTC.MGW_WPhone);
  xws.onopen = function () {
    Utils.log.info('xws.onopen');
    heartBeatTimer = setInterval(function () {
      xws.send('ping');
    }, 3000);
       //获取视频按钮权限
      var obj = {}
      obj.msgType = "EventGetAuths"
      obj.sessionId = roomId
      obj.makeType = makeType
      obj.opttype = opttype
      wphoneSendMsgToChat(obj)
  }

  xws.onmessage = function (data) {
    console.log('xws.onmessage: ' + JSON.stringify(data.data));
    data = JSON.parse(data.data);
    if(data.sendFrom == "chat"){
      if ('EventHangupFromIe' === data.msgType) {
        webRtcUiKit.leaveConferenceForWPhone(data.sessionId,data.isTransfer,'closeWPhone')
        xws && xws.close();
        xws = null;
      }
      if ('EventRiskTip' === data.msgType) {
        webRtcUiKit.riskTips(data)
      }
      if ('EventNetworkStatus' === data.msgType) {
        webRtcUiKit.setNetworkStatusTips(data)
      }
      if ('EventSendAuths' === data.msgType) {
        if(data.opttype){
          const options = {
            rtcType: rtcType,
            browserType: 'WPhone',
            role: "Agent",
            videoScreenClass: 'rtc-wphone-video-screen',
            // 隐藏的按钮 'hangup', 'close', 'hide', 'hold', 'video', 'voice', 'speak', 'fullScreen'
            btnMap: ['hide', 'close', 'fullScreen','videoHead','hold', 'video', 'voice', 'speak','change','takePhoto','changedrawingmode','share','startRecord','virtualBg','whiteboard','hold'],
            shareSelfFlag: false,
            wphonePage: true,
            opttype:"testCamera"
          }
          webRtcUiKit.initTestCameraModule(data.sessionId, data.fromuser, window.CONFIG.RTC.parentid, "Agent", data.makeType, {"channeltype":data.channelType,otherdn:data.fromuser,turn:CONFIG.RTC.turn,auths:auths}, options);
        }else{
          const options = {
            rtcType: rtcType,
            browserType: 'WPhone',
            role: "Agent",
            videoScreenClass: 'rtc-wphone-video-screen',
            // 隐藏的按钮 'hangup', 'close', 'hide', 'hold', 'video', 'voice', 'speak', 'fullScreen'
            btnMap: ['hide', 'close', 'fullScreen','videoHead','switchCamera'],
            shareSelfFlag: false,
            wphonePage: true,
            tenantId: tenantId,
            turn: turn,
            auths:data.auths
          }
          if(!data.auths.WhiteBoard){
            options.btnMap.push("whiteboard")
          }
          if(!data.auths.VirtualBackground){
            options.btnMap.push("virtualBg")
          }
          if(!data.auths.ServerRecording){
            options.btnMap.push("server-record-btn")
          }
          if(!data.auths.LocalRecording){
            options.btnMap.push("startRecord")
          }
          if(!data.auths.DevidedRecording){
            options.btnMap.push("divided-record-btn")
          }
          if(!data.auths.Sharing){
            options.btnMap.push("share")
          }
          if(!data.auths.SwitchVoiceVideo){
            options.btnMap.push("change")
          }
          if(!data.auths.TakePhoto){
            options.btnMap.push("takePhoto")
          }
          if(!data.auths.VideoHold){
            options.btnMap.push("hold")
          }

          window.CONFIG.RTC.turn = turn
          webRtcUiKit.initWebRtcModule(data.sessionId, data.fromuser, window.CONFIG.RTC.parentid, "Agent", data.makeType, {"channeltype":data.channelType,otherdn:data.fromuser,turn:data.turn,auths:data.auths}, options);
            setTimeout(function () {
              var obj = {}
              obj.msgType = "EventSetMyjson"
              obj.myjson = webRtcUiKit.myjson
              wphoneSendMsgToChat(obj)
            },1000)
        }
      }
    }
  }

  xws.onclose = function () {
    Utils.log.info('xws.onclose');
    clearInterval(heartBeatTimer);
  }

  xws.onerror = function () {
    Utils.log.info('xws.onerror');
  }
}
