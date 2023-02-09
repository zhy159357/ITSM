/**
 * web rtc消息处理回调
 *
 * @constructor
 */
WebRtcMsgHandler = function () {
  var closeFlag = false

  this.callback = function (json) {
    // 小程序和freeswitch视频有EventMemeberChanged，没有EventPartyAdded；coturn视频有EventPartyAdded，没有EventMemeberChanged
   if("EventStatInfo" != json.msgtype){
     console.log('receive mgw msg' + JSON.stringify(json));
   }
    if ("EventVideoHangup" === json.msgtype) {

    }
    if ("EventStatInfo" === json.msgtype) {
      //网络状况提示
      if(parseInt(json.vin)+parseInt(json.in) >0 && parseInt(json.vin)+parseInt(json.in) < 50){
        $(".video-tips-div").html(json.fromuser+"当前网络不佳")
      }else{
        $(".video-tips-div").html("")
      }
    }
    if ("EventDeviceError" === json.msgtype && webRtcUiKit.getmyjson().msgtype!="share" ) {
      if(window.CONFIG.debugAll) {
        console.log("EventDeviceError");
      }
      alert("未检测到摄像头");
      webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom);
    }
    if ("EventStartSharing" === json.msgtype) {
      if(window.CONFIG.debugAll)  console.log("收到开始共享回调");
      webRtcUiKit.getmyjson().shareStatus =1;
      EventStartSharing();
      var json = JSON.parse("{}");
      json.msgtype = "HideWindow";
      json.fromuser = webRtcUiKit.getmyjson().fromuser;
      // webRtcUiKit.xchatkit.Send2WPhone(json);
    }
    if("currentPageIdIsTure" ===json.msgtype){
      //收到隐私页事件
      if(webRtcUiKit.getmyjson().shareStatus==1){
        webRtcUiKit.getmyjson().shareStatus=2;
        webRtcUiKit.StopSharing();
      }
    }
    if("currentPageIdIsFalse" ===json.msgtype){
      //收到正常页事件
      if(webRtcUiKit.getmyjson().shareStatus==2){
        webRtcUiKit.startSharing();
      }
    }
    if ("EventStopSharing" === json.msgtype) {
      if(window.CONFIG.debugAll) {
        console.log("收到共享停止回调");
      }
/*      if(webRtcUiKit.getmyjson().shareStatus==2){
        var json = JSON.parse("{}");
        json.msgtype = "ShowWindow";
        json.fromuser = webRtcUiKit.getmyjson().fromuser;
        webRtcUiKit.xchatkit.Send2WPhone(json);
        EventPauseSharing();
      }else{
        closeStatus=1;
        webRtcUiKit.leaveConference();//用户触发，挂机
      }*/

    }
    if ("EventRecordInfo" === json.msgtype) {
    }
    if ("EventAgentLeave" === json.msgtype) {
      if(window.CONFIG.debugAll)  console.log("收到客服离开通知，开始判断视频人数");
      if(confenceArary.length ==1){
        closeStatus=2;
        webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom);
      }else if(confenceArary.length ==2 && confenceArary.indexOf(json.leaveAgent)>-1){
        closeStatus=2;
        webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom);
      }
    }
    if ("EventRegistered" === json.msgtype) {
      //注册成功
      if (!webRtcUiKit.joinFlag) {
        webRtcUiKit.joinFlag = true;
      }
    }
    if ("EventSelfCheck" === json.msgtype) {
      //环境监测完成
      setTimeout(function () {
        alert("达到视频环境要求");
        webRtcUiKit.leftConference();
      }, 5000)

    }
    if ("EventError" === json.msgtype) {
      //异常信息
      //alert('EventError: '+json.content);
    }
    if ("EventJoinSucc" === json.msgtype) {
      //入会成功：邀请别人入会请求，需要再自己入会成功后发送。
      //如下示例判断为，当前入会成功人为自己，并且视频发起方为自己，若为座席则webRtcUiKit.getfirstVideoRole() === "Agent"，若为客户则webRtcUiKit.getfirstVideoRole() === "Client"

      if (json.fromuser == webRtcUiKit.getmyjson().fromuser && webRtcUiKit.getfirstVideoRole() === "Agent") {
        //发送videostart入会请求方法
        sendvideostart();
        //请求发送后清空入会发起方
        webRtcUiKit.setfirstVideoRole("");

      }
      if (json.fromuser == webRtcUiKit.getmyjson().fromuser && webRtcUiKit.getfirstVideoRole() === "Client") {
        //客户端发送videostart
        $(window.parent.document).contents().find("#content")[0].contentWindow.sendVideoMsg();
        webRtcUiKit.setfirstVideoRole("");
      }
    }
    if ("EventPartyConnected" === json.msgtype) {

    }
    if ("EventPartyAdded" === json.msgtype) {
      if(window.CONFIG.debugAll)  console.log(json);
      if (json.fromuser != webRtcUiKit.getmyjson().fromuser && json.fromuser.indexOf("recorder-server") < 0) {
        if (json.streamtype == "video") {
          var speakBtn = $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' #speakBtn');
          speakBtn.removeClass('speak-btn-s').addClass('speak-btn');
          webRtcUiKit.setVolume(1.0);
          if(window.CONFIG.debugAll)  console.info("====开始录像!!!!GetTotalPeers=" + webRtcUiKit.xchatkit.GetTotalPeers);
          if (webRtcUiKit.xchatkit.GetTotalPeers === 1) {
            if(window.CONFIG.debugAll)  console.info("====开始录像");
            if(window.CONFIG.RTC.automatic == "true") {
              webRtcUiKit.startServerRecording();
            }
          }
        }
      }
      if(webRtcUiKit.getmyjson().msgtype!="share" && webRtcUiKit.options.role == 'Agent'){
        //座席音视频需要录像，协同不录像
        if(window.CONFIG.debugAll)  console.info("====开始录像");
        if(window.CONFIG.RTC.automatic == "true") {
          webRtcUiKit.startServerRecording();
        }
      }
      if(webRtcUiKit.getmyjson().msgtype=="share" && webRtcUiKit.options.role == 'Agent' && json.streamtype == "audio"){
        if(window.CONFIG.debugAll)  console.log("收到EventPartyAdded事件");
        webRtcUiKit.xchatkit.ChangeLayout("1");
        //webRtcUiKit.xchatkit.ChangeSpeaker();
        if (webRtcUiKit.options.shareSelfFlag) {
          webRtcUiKit.startSharing();
        }
      }
      var sendjson = JSON.parse("{}");
      sendjson.msgtype = "EventSendJsonData";
      sendjson.touser = json.fromuser;
      sendjson.role = webRtcUiKit.getmyjson().userdata.role;
      sendjson.fromuser = webRtcUiKit.getmyjson().fromuser;
      webRtcUiKit.sendJson(sendjson);
      if(window.CONFIG.debugAll)  console.log("发送json：" + JSON.stringify(sendjson));
    }
    if ("EventMembersChanged" === json.msgtype) {
      if(window.CONFIG.debugAll)  console.log("收到Change");
      if(window.CONFIG.debugAll)  console.log(json);
      var len = json.content.length;
      var jsonParam = JSON.parse("{}");
      jsonParam.fromuser = webRtcUiKit.getmyjson().fromuser;
      jsonParam.touser = webRtcUiKit.getmyjson().fromuser;
      jsonParam.msgtype = "MembersChanged";
      jsonParam.partyNums = len;
      webRtcUiKit.xchatkit.Send2WPhone(jsonParam);
      confenceArary = [];
      confenceArary = json.content;
      if(json.content.length == 2){
        setTimeout(function () {
          if(window.CONFIG.RTC.automatic == "true") {
            webRtcUiKit.startServerRecording();
          }
        },0)
      }
    }
    if ("EventLocalStream" === json.msgtype) {

    }
    if ("EventPartyRemoved" === json.msgtype) {
      if(window.CONFIG.debugAll)  {
        console.log("EventPartyRemoved");
      }
    }
    if ("EventPartyClosed" === json.msgtype) {
      if(window.CONFIG.debugAll)  {
        console.log("EventPartyClosed");
      }
    }
    if ("EventWindowClosed" === json.msgtype) {
      if(window.CONFIG.debugAll)  {
        console.log("EventWindowClosed");
      }
/*      if(!this.closeFlag ){
        chatUiKit.sendVideoHangupMsg(json.chatroom)
        this.closeFlag = true
      }*/
/*      var userVideo = Model.people.get_by_sid(json.chatroom);
      if(userVideo) {
        if(userVideo.chatState == Constants.chatState.Normal || userVideo.chatState == "normal" || userVideo.chatState == "transfer"){
          var messageText = new Object();
          messageText.msgtype = "text";
          messageText.touser = userVideo.uid;
          messageText.fromuser = Agent.getCurrentAgent().agentId;
          messageText.channeltype = userVideo.chatType;
          messageText.interactionid = sessionId;
          messageText.chatmode = "normal";
          messageText.issilentagent = "false";
          messageText.enablevideo = 'hangup';
          chatSDK.sendChatMsg(sessionId, messageText,visibility);
        }
      }*/
/*      webRtcUiKit.setfirstVideoRole('-1')
      webRtcUiKit.joinFlag = false
      $('#video-screen' + json.chatroom).parent().removeClass('wrapper')
      $('#video-screen' + json.chatroom).remove()
      webRtcUiKit.clearmyjson()
      webRtcUiKit.closeTimer()*/
    }
    if ("EventPartyDeleted" === json.msgtype) {
      if(window.CONFIG.debugAll)  {
        console.log("EventPartyDeleted");
      }
    }
    if ("EventReleased" === json.msgtype) {
      if(window.CONFIG.debugAll) {
        console.log("EventReleased");
      }
    }
    if ("EventHangUpFromIe" === json.msgtype) {
      if(window.CONFIG.debugAll)  {
        console.log("EventReleased");
      }
      closeStatus = 2;
      webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom);
    }
    if ("EventChangeToVideo" === json.msgtype) {
      webRtcUiKit.SetCameraStatus(true)
    }
    if ("EventChangeToVoice" === json.msgtype ) {
      webRtcUiKit.SetCameraStatus(false)
    }
    if ("EventStopSharing" === json.msgtype ) {
        webRtcUiKit.stopSharingView();
    }
    if ("EventDisconnected" === json.msgtype ) {
      webRtcUiKit.leaveConference(json.chatroom,'','agentend')
    }
    if ("EventPhotoTaken" === json.msgtype ) {
      if(json.content != ''){
        $(".photoPrewDiv").show();
        $(".main-video-box").hide();
        $(".photoPrew").attr('src',json.content).show();
        var takePhotoResult = {
          event: 'EventTakePhotoSuccess',
          data: json.content,
          sessionid: webRtcUiKit.getmyjson().chatroom,
          fromuser: webRtcUiKit.getmyjson().fromuser,
          channeltype: webRtcUiKit.getmyjson().channelType
        }
      //  webRtcUiKit.reciveTakePhotoResult(takePhotoResult);
      }
      var msg = {
        fromuser: webRtcUiKit.getmyjson().fromuser,
        chatroom: webRtcUiKit.getmyjson().chatroom,
        msgtype: 'EventClientTakePhoto'
      }
      webRtcUiKit.sendJson(msg)
    }
  }
  this.monitorcallback = function (json) {
    if ("EventPeerClicked" === json.msgtype ) {
      webRtcUiKit.monitorMap[json.fromuser] = json.chatroom
      webRtcUiKit.currentChoose = json
    }
    if ("EventHangup" === json.msgtype ) {
     // console.log("被监听坐席收到EventHangup")
     // webRtcUiKit.leaveConference(json.chatroom)
    }
  }
  this.onsettingcallback = function (json) {
    if ("EventCamerasInfo" === json.msgtype ) {
      //{"msgtype":"EventCamerasInfo","count":3,"cameras":["ChromaCam","ManyCam Virtual Webcam (pleV:ideo)","FaceTime高清摄像头（内建） (05ac:8514)"],"supported":["1280x720","640x480","320x240","160x120"]}
      window.CONFIG.RTC.cameraInfos = json.cameras
      window.CONFIG.RTC.supported = json.supported
      window.globalUiKit.vm.$emit('EventCamerasInfo', json);
    }
  }
}

