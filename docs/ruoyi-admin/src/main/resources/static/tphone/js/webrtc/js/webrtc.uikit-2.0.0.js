var webRtcUiKit = window.webRtcUiKit || {}
var webRtcMsgHandler

var role
var confenceArary = [] //共享状态，1代表开始共享，2代表停止共享
var windowClosed = false

function EventPauseSharing () {
  //给ie通知暂停共享
  var json = JSON.parse('{}')
  json.msgtype = 'EventPauseSharing'
  json.fromuser = webRtcUiKit.getmyjson().fromuser
  json.chatroom = webRtcUiKit.getmyjson().chatroom
  json.content = 'hello，你好'
  webRtcUiKit.xchatkit.Send2WPhone(json)
}

function EventOpenSuccess () {
  //给ie通知视频窗口已打开
  if (window.CONFIG.debugAll) console.log('开始通知ie浏览器，窗口正常打开')
  var json = JSON.parse('{}')
  json.msgtype = 'EventOpenSuccess'
  json.fromuser = webRtcUiKit.getmyjson().fromuser
  json.chatroom = webRtcUiKit.getmyjson().chatroom
  json.content = 'hello，你好' // webRtcUiKit.sendMsg2IE(json);

  webRtcUiKit.xchatkit.Send2WPhone(json)
}

function EventStartSharing () {
  //给ie通知开始共享
  var json = JSON.parse('{}')
  json.msgtype = 'EventStartSharingToIE'
  json.fromuser = webRtcUiKit.getmyjson().fromuser
  json.chatroom = webRtcUiKit.getmyjson().chatroom
  json.content = 'hello，你好' //webRtcUiKit.sendMsg2IE(json);

  webRtcUiKit.xchatkit.Send2WPhone(json)
}

/**
 * Web RTC UI工具库
 *
 * @param {divId} divId - UiKit选项参数设置，传递对象为DOM对象
 * @constructor
 */


var WebRTCUiKit = function WebRTCUiKit () {

  // 页面绑定事件
  var firstVideoRole // 记录座席A，座席B，客户id

  var jqueryMap = {} // 变量替换

  var self = this
  self.options = {
    rtcType: 'WebRtc',
    browserType: 'Browser',
    role: role,
    videoScreenClass: '',
    btnMap: ['hide'],
    shareSelfFlag: false,
    wphonePage:false
  }
  self.monitorMap = {}
  self.currentChoose = {}
  self.lineStyle = {"fontsize":"1px","color":"#FF0000"}
  /**
   * 计时器
   *
   */

  var intervalTimer = null
  self.myjson = {}
  self.monitormyjson = {}
  self.settingmyjson = {}
  var xchatkit //
  var monitorxchatkit //
  var settingxchatkit //
  var partyNum
  var srcObject
  var joinFlag = false
  var startUpTime = new Date().getTime()
  /**
   * 初始化按钮事件
   *
   */
  /**
   * 模块初始化
   */
  this.initModule = function (options) {
    if(options) {
      this.options.rtcType = options.rtcType;
      this.options.role = options.role;
      this.options.videoScreenClass = options.videoScreenClass;
      this.options.btnMap = options.btnMap;
      this.options.shareSelfFlag = options.shareSelfFlag;
      this.options.browserType = options.browserType;
      this.wphonePage = options.wphonePage;
    }
  }
  this.init = function (parentDivName) {
    // 初始化dom元素绑定
    jqueryMap.hangupBtn = $('#' + parentDivName + ' #hangupBtn')
    jqueryMap.takePhoto = $('#' + parentDivName + ' #takePhotoBtn')
    jqueryMap.headClose = $('#' + parentDivName + ' .head-close')
    jqueryMap.videoBtn = $('#' + parentDivName + ' #videoBtn')
    jqueryMap.voiceBtn = $('#' + parentDivName + ' #voiceBtn')
    jqueryMap.speakBtn = $('#' + parentDivName + ' #speakBtn')
    jqueryMap.timerTxt = $('#' + parentDivName + ' #timerTxt')
    jqueryMap.changeDrawingMode = $('#' + parentDivName + ' #changedrawingmode-btn')
    jqueryMap.fullScreen = $('#' + parentDivName + ' #fullscreen-btn')
    jqueryMap.headHide = $('#' + parentDivName + ' .head-hide')
    jqueryMap.holdBtn = $('#' + parentDivName + ' #holdBtn')
    jqueryMap.underlineBtn = $('#' + parentDivName + ' #underlineBtn')
    jqueryMap.videoHead = $('#' + parentDivName + ' .video-head') // 挂机按钮
    jqueryMap.switchVoiceAndVideoBtn = $('#' + parentDivName + ' #video-voice')
    jqueryMap.startShare = $('#' + parentDivName + ' #share-btn');
    jqueryMap.videoHead = $('#' + parentDivName + ' .video-head');
    jqueryMap.startRecord = $('#' + parentDivName + ' .record-btn');
    jqueryMap.dividedRecord = $('#' + parentDivName + ' .divided-record-btn');
    jqueryMap.startServerRecord = $('#' + parentDivName + ' .server-record-btn');
    jqueryMap.virtualBg = $('#' + parentDivName + ' #virtualbgBtn');
    jqueryMap.whiteboard = $('#' + parentDivName + ' #v-startwhiteboard');
    jqueryMap.switchCamera = $('#' + parentDivName + ' #switchCamera');
    jqueryMap.resetPlayers = $('#' + parentDivName + ' .resetPlayers-btn');
    jqueryMap.openRecordBtn = $('#' + parentDivName + ' #open-record-btn');
    jqueryMap.startRecord.next().bind('click', function (e) {
      jqueryMap.startRecord.trigger("click")
    })
    jqueryMap.dividedRecord.next().bind('click', function (e) {
      jqueryMap.dividedRecord.trigger("click")
    })
    jqueryMap.startServerRecord.next().bind('click', function (e) {
      jqueryMap.startServerRecord.trigger("click")
    })
    var openRecordBtn = jqueryMap.openRecordBtn
    openRecordBtn.bind('click', function (e) {
      var e = event || window.event;
      var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
      var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
      var x = e.pageX || e.clientX;
        $('.record-choose-div').css('left',"50%");
        if($(".record-choose-div").css("display") == "none"){
          $(".record-choose-div").show()
        }else{
          $(".record-choose-div").hide()
        }
    }) // 窗口强制关闭
    var hangupBtn = jqueryMap.hangupBtn
    hangupBtn.bind('click', function () {
      if(webRtcUiKit.options.browserType == "WPhone"){
        webRtcUiKit.leaveConferenceForWPhone(webRtcUiKit.getmyjson().chatroom,'','agentend')
      }else{
        webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom,'','agentend')
      }
      if (window.CONFIG.debugAll) {
        console.log('------' + new Date() + ', log info [主动挂机离开会议 ]')
      }
    }) // 窗口强制关闭

    var headClose = jqueryMap.headClose
    headClose.bind('click', function () {
      if(webRtcUiKit.options.browserType == "WPhone"){
        webRtcUiKit.leaveConferenceForWPhone(webRtcUiKit.getmyjson().chatroom,'','agentend')
      }else{
        webRtcUiKit.leaveConference(webRtcUiKit.getmyjson().chatroom,'','agentend')
      }
    })
    var whiteboard = jqueryMap.whiteboard
    whiteboard.bind('click', function () {
      if(webRtcUiKit.options.browserType == "WPhone"){
        var obj = {}
        obj.msgType = "EventStartWhiteboard"
        obj.fromuser = webRtcUiKit.getmyjson().fromuser
        obj.sessionId = webRtcUiKit.getmyjson().chatroom
        wphoneSendMsgToChat(obj)
      }
    })
    var switchCamera = jqueryMap.switchCamera
    switchCamera.bind('click', function () {
      webRtcUiKit.xchatkit.SwitchCamera()
    })
    var resetPlayers = jqueryMap.resetPlayers
    resetPlayers.bind('click', function () {
      webRtcUiKit.xchatkit.ResetPlayers()
    })
    // 开关本地视频按钮
    jqueryMap.takePhoto.bind('click', function () {
      webRtcUiKit.xchatkit.TakePhotoEx()
/*      var json = JSON.parse("{}");
      json.msgtype = "EventPhotoTaken"
      json.fromuser = webRtcUiKit.getmyjson().fromuser
      json.content = webRtcUiKit.xchatkit.TakePhoto(1, null, 1)
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
        webRtcUiKit.reciveTakePhotoResult(takePhotoResult);
      }*/
    })
    //虚拟背景
    var virtualBtn = jqueryMap.virtualBg
    var virtualBtnClickTimer = null
    virtualBtn.bind('click', function () {
      if (virtualBtnClickTimer === null) {
        virtualBtnClickTimer = setTimeout(virtualBtnClickService, 200)
      }
    })

    function virtualBtnClickService () {
      if (virtualBtnClickTimer) {
        clearTimeout(virtualBtnClickTimer)
        var virtualSpan = $('#' + parentDivName + ' #virtualbgBtn span')
        if (virtualSpan.hasClass('iconshipinchuang-xunibeijing-kai')) {
          virtualSpan.removeClass('iconshipinchuang-xunibeijing-kai').addClass('iconshipinchuang-xunibeijing-guan')
          console.log("------" + new Date() + ", log info [打开虚拟背景] ");
          webRtcUiKit.getmyjson().imagemixer = 1;
          webRtcUiKit.xchatkit.ChangeSettings(webRtcUiKit.getmyjson());
        } else if (virtualSpan.hasClass('iconshipinchuang-xunibeijing-guan')) {
          virtualSpan.removeClass('iconshipinchuang-xunibeijing-guan').addClass('iconshipinchuang-xunibeijing-kai')
          console.log("------" + new Date() + ", log info [关闭虚拟背景]");
          webRtcUiKit.getmyjson().imagemixer = 0;
          webRtcUiKit.xchatkit.ChangeSettings(webRtcUiKit.getmyjson())
        }

        virtualBtnClickTimer = null
      }
    }
    var recordBtn = jqueryMap.startRecord
    var recordBtnClickTimer = null
    recordBtn.bind('click', function () {
      if (recordBtnClickTimer === null) {
        recordBtnClickTimer = setTimeout(recordBtnClickService, 200)
      }
    })

    function recordBtnClickService () {
      if (recordBtnClickTimer) {
        clearTimeout(recordBtnClickTimer)
        var recordSpan = $('#' + parentDivName + ' .record-btn span')
        if (recordSpan.hasClass('iconshipinchuang-luzhi-kai')) {
          recordSpan.removeClass('iconshipinchuang-luzhi-kai').addClass('iconshipinchuang-luzhi-guan')
          console.log("------" + new Date() + ", log info [开始本地录像] ");
          webRtcUiKit.startClientRecording();
          $('#' + parentDivName + ' #open-record-btn svg').remove()
          var svg = "<svg t=\"1606372842679\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7098\" width=\"30\" height=\"30\"><path d=\"M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z\" fill=\"#BDC6CF\" p-id=\"7099\"></path><path d=\"M387.4 662.7c-30 0-54.5-24.5-54.5-54.5v-191c0-30 24.5-54.5 54.5-54.5s54.5 24.5 54.5 54.5v191c0 30-24.6 54.5-54.5 54.5zM636.4 662.7c-30 0-54.5-24.5-54.5-54.5v-191c0-30 24.5-54.5 54.5-54.5s54.5 24.5 54.5 54.5v191c0 30-24.6 54.5-54.5 54.5z\" fill=\"#FF3939\" p-id=\"7100\"></path></svg>"
          $('#' + parentDivName + ' #open-record-btn').append(svg)
        } else if (recordSpan.hasClass('iconshipinchuang-luzhi-guan')) {
          recordSpan.removeClass('iconshipinchuang-luzhi-guan').addClass('iconshipinchuang-luzhi-kai')
          console.log("------" + new Date() + ", log info [停止本地录像 ]");
          webRtcUiKit.xchatkit.StopClientRecording(webRtcUiKit.getmyjson().imagemixer)
          var dividedRecordSpan = $('#' + parentDivName + ' .divided-record-btn span')
          var serverRecordSpan = $('#' + parentDivName + ' .server-record-btn span')
          if(recordSpan.hasClass('iconshipinchuang-luzhi-kai') && dividedRecordSpan.hasClass('iconshipinchuang-luzhi-kai') && serverRecordSpan.hasClass('iconshipinchuang-luzhi-kai')){
            $('#' + parentDivName + ' #open-record-btn svg').remove()
            var svg = "<svg t=\"1606373670517\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7606\" width=\"30\" height=\"30\"><path d=\"M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z\" fill=\"#BDC6CF\" p-id=\"7607\"></path><path d=\"M511.7 511.7m-191.6 0a191.6 191.6 0 1 0 383.2 0 191.6 191.6 0 1 0-383.2 0Z\" fill=\"#FF3939\" p-id=\"7608\"></path></svg>"
            $('#' + parentDivName + ' #open-record-btn').append(svg)
          }
        }

        recordBtnClickTimer = null
      }
    }

    var dividedRecordBtn = jqueryMap.dividedRecord
    var dividedRecordBtnClickTimer = null
    dividedRecordBtn.bind('click', function () {
      if (dividedRecordBtnClickTimer === null) {
        dividedRecordBtnClickTimer = setTimeout(dividedRecordBtnClickService, 200)
      }
    })

    function dividedRecordBtnClickService () {
      if (dividedRecordBtnClickTimer) {
        clearTimeout(dividedRecordBtnClickTimer)
        dividedRecordBtnClickTimer = null
        var dividedRecordSpan = $('#' + parentDivName + ' .divided-record-btn span')
        if (dividedRecordSpan.hasClass('iconshipinchuang-luzhi-kai')) {
          dividedRecordSpan.removeClass('iconshipinchuang-luzhi-kai').addClass('iconshipinchuang-luzhi-guan')
          console.log("------" + new Date() + ", log info [开始分流录像] ");
          webRtcUiKit.xchatkit.StartDividedRecording()
          $('#' + parentDivName + ' #open-record-btn svg').remove()
          var svg = "<svg t=\"1606372842679\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7098\" width=\"30\" height=\"30\"><path d=\"M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z\" fill=\"#BDC6CF\" p-id=\"7099\"></path><path d=\"M387.4 662.7c-30 0-54.5-24.5-54.5-54.5v-191c0-30 24.5-54.5 54.5-54.5s54.5 24.5 54.5 54.5v191c0 30-24.6 54.5-54.5 54.5zM636.4 662.7c-30 0-54.5-24.5-54.5-54.5v-191c0-30 24.5-54.5 54.5-54.5s54.5 24.5 54.5 54.5v191c0 30-24.6 54.5-54.5 54.5z\" fill=\"#FF3939\" p-id=\"7100\"></path></svg>"
          $('#' + parentDivName + ' #open-record-btn').append(svg)
        } else if (dividedRecordSpan.hasClass('iconshipinchuang-luzhi-guan')) {
          dividedRecordSpan.removeClass('iconshipinchuang-luzhi-guan').addClass('iconshipinchuang-luzhi-kai')
          console.log("------" + new Date() + ", log info [停止分流录像 ]");
          webRtcUiKit.xchatkit.StopDividedRecording();
          var serverRecordSpan = $('#' + parentDivName + ' .server-record-btn span')
          var recordSpan = $('#' + parentDivName + ' .record-btn span')
          if(recordSpan.hasClass('iconshipinchuang-luzhi-kai') && dividedRecordSpan.hasClass('iconshipinchuang-luzhi-kai') && serverRecordSpan.hasClass('iconshipinchuang-luzhi-kai')){
            $('#' + parentDivName + ' #open-record-btn svg').remove()
            var svg = "<svg t=\"1606373670517\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7606\" width=\"30\" height=\"30\"><path d=\"M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z\" fill=\"#BDC6CF\" p-id=\"7607\"></path><path d=\"M511.7 511.7m-191.6 0a191.6 191.6 0 1 0 383.2 0 191.6 191.6 0 1 0-383.2 0Z\" fill=\"#FF3939\" p-id=\"7608\"></path></svg>"
            $('#' + parentDivName + ' #open-record-btn').append(svg)
          }
        }
      }
    }

    var serverRecordBtn = jqueryMap.startServerRecord
    var serverRecordBtnClickTimer = null
    serverRecordBtn.bind('click', function () {
      if (serverRecordBtnClickTimer === null) {
        serverRecordBtnClickTimer = setTimeout(serverRecordBtnClickService, 200)
      }
    })

    function serverRecordBtnClickService () {
      if (serverRecordBtnClickTimer) {
        clearTimeout(serverRecordBtnClickTimer)
        var serverRecordSpan = $('#' + parentDivName + ' .server-record-btn span')
        if (serverRecordSpan.hasClass('iconshipinchuang-luzhi-kai')) {
          serverRecordSpan.removeClass('iconshipinchuang-luzhi-kai').addClass('iconshipinchuang-luzhi-guan')
          console.log("------" + new Date() + ", log info [开始服务端录像] ");
          webRtcUiKit.startServerRecording();
          $('#' + parentDivName + ' #open-record-btn svg').remove()
          var svg = "<svg t=\"1606372842679\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7098\" width=\"30\" height=\"30\"><path d=\"M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z\" fill=\"#BDC6CF\" p-id=\"7099\"></path><path d=\"M387.4 662.7c-30 0-54.5-24.5-54.5-54.5v-191c0-30 24.5-54.5 54.5-54.5s54.5 24.5 54.5 54.5v191c0 30-24.6 54.5-54.5 54.5zM636.4 662.7c-30 0-54.5-24.5-54.5-54.5v-191c0-30 24.5-54.5 54.5-54.5s54.5 24.5 54.5 54.5v191c0 30-24.6 54.5-54.5 54.5z\" fill=\"#FF3939\" p-id=\"7100\"></path></svg>"
          $('#' + parentDivName + ' #open-record-btn').append(svg)
        } else if (serverRecordSpan.hasClass('iconshipinchuang-luzhi-guan')) {
          serverRecordSpan.removeClass('iconshipinchuang-luzhi-guan').addClass('iconshipinchuang-luzhi-kai')
          console.log("------" + new Date() + ", log info [停止服务端录像 ]");
          webRtcUiKit.xchatkit.StopServerRecording();
          var dividedRecordSpan = $('#' + parentDivName + ' .divided-record-btn span')
          var recordSpan = $('#' + parentDivName + ' .record-btn span')
          if(recordSpan.hasClass('iconshipinchuang-luzhi-kai') && dividedRecordSpan.hasClass('iconshipinchuang-luzhi-kai') && serverRecordSpan.hasClass('iconshipinchuang-luzhi-kai')){
            $('#' + parentDivName + ' #open-record-btn svg').remove()
            var svg = "<svg t=\"1606373670517\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7606\" width=\"30\" height=\"30\"><path d=\"M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z\" fill=\"#BDC6CF\" p-id=\"7607\"></path><path d=\"M511.7 511.7m-191.6 0a191.6 191.6 0 1 0 383.2 0 191.6 191.6 0 1 0-383.2 0Z\" fill=\"#FF3939\" p-id=\"7608\"></path></svg>"
            $('#' + parentDivName + ' #open-record-btn').append(svg)
          }
        }

        serverRecordBtnClickTimer = null
      }
    }

    var shareBtn = jqueryMap.startShare
    var shareBtnClickTimer = null
    shareBtn.bind('click', function () {
      if (shareBtnClickTimer === null) {
        shareBtnClickTimer = setTimeout(shareBtnClickService, 200)
      }
    })

    function shareBtnClickService () {
      if (shareBtnClickTimer) {
        clearTimeout(shareBtnClickTimer)
        var shareSpan = $('#' + parentDivName + ' .share-btn span')
        if (shareSpan.hasClass('iconshipinchuang-gongxiang-copy')) {
          shareSpan.removeClass('iconshipinchuang-gongxiang-copy').addClass('iconshipinchuang-gongxiang-guan-copy')
          console.log("------" + new Date() + ", log info [共享] ");
          webRtcUiKit.xchatkit.StartSharing();
          jqueryMap.videoBtn.hide();
          jqueryMap.holdBtn.hide();
          jqueryMap.virtualBg.hide();
          jqueryMap.switchVoiceAndVideoBtn.hide();
        } else if (shareSpan.hasClass('iconshipinchuang-gongxiang-guan-copy')) {
          shareSpan.removeClass('iconshipinchuang-gongxiang-guan-copy').addClass('iconshipinchuang-gongxiang-copy')
          console.log("------" + new Date() + ", log info [停止共享 ]");
          webRtcUiKit.xchatkit.StopSharing()
          jqueryMap.videoBtn.show();
          if(webRtcUiKit.options.auths.VideoHold){
            jqueryMap.holdBtn.show();
          }
          if(webRtcUiKit.options.auths.VirtualBackground){
            jqueryMap.virtualBg.show();
          }
          if(webRtcUiKit.options.auths.SwitchVoiceVideo){
            jqueryMap.switchVoiceAndVideoBtn.show();
          }
        }

        shareBtnClickTimer = null
      }
    }
    var switchVoiceAndVideoBtn = jqueryMap.switchVoiceAndVideoBtn
    var switchVoiceAndVideoBtnClickTimer = null
    switchVoiceAndVideoBtn.bind('click', function () {
      if (switchVoiceAndVideoBtnClickTimer === null) {
        switchVoiceAndVideoBtnClickTimer = setTimeout(switchVoiceAndVideoBtnClickService, 200)
      }
    })

    function switchVoiceAndVideoBtnClickService () {
      if (switchVoiceAndVideoBtnClickTimer) {
        clearTimeout(switchVoiceAndVideoBtnClickTimer)
        var switchVoiceAndVideoSpan = $('#' + parentDivName + ' #video-voice span')
        if (switchVoiceAndVideoSpan.hasClass('iconshipinchuang-shipinzhuanyuyin2---')) {
          switchVoiceAndVideoSpan.removeClass('iconshipinchuang-shipinzhuanyuyin2---').addClass('iconshipinchuang-yuyinzhuanshipin')
          $("#videoPlayer"+webRtcUiKit.getmyjson().chatroom).show()
          webRtcUiKit.changeToVideo();
          webRtcUiKit.SetCameraStatus(true)
          $('#video-screen'+ webRtcUiKit.getmyjson().chatroom).removeClass("video-screen-bg");
        } else if (switchVoiceAndVideoSpan.hasClass('iconshipinchuang-yuyinzhuanshipin')) {
          switchVoiceAndVideoSpan.removeClass('iconshipinchuang-yuyinzhuanshipin').addClass('iconshipinchuang-shipinzhuanyuyin2---')
          $("#videoPlayer"+webRtcUiKit.getmyjson().chatroom).hide()
          webRtcUiKit.changeToVoice();
          webRtcUiKit.SetCameraStatus(false)
          $('#video-screen'+ webRtcUiKit.getmyjson().chatroom).addClass("video-screen-bg");
        }

        switchVoiceAndVideoBtnClickTimer = null
      }
    }
    var videoBtn = jqueryMap.videoBtn
    var videoBtnClickTimer = null
    videoBtn.bind('click', function () {
      if (videoBtnClickTimer === null) {
        videoBtnClickTimer = setTimeout(videoBtnClickService, 200)
      }
    })

    function videoBtnClickService () {
      if (videoBtnClickTimer) {
        clearTimeout(videoBtnClickTimer)
        var videoSpan = $('#' + parentDivName + ' #videoBtn span')
        if (videoSpan.hasClass('icon01shipinchuang-shexiangtou-kaiqi')) {
          videoSpan.removeClass('icon01shipinchuang-shexiangtou-kaiqi').addClass('icon01shipinchuang-shexiangtou-guanbi')
          webRtcUiKit.SetCameraStatus(false)
          var json = JSON.parse('{}')
          json.chatroom = webRtcUiKit.getmyjson().chatroom
          json.msgtype = 'EventCameraClose'
          json.fromuser = webRtcUiKit.getmyjson().fromuser
          webRtcUiKit.sendJson(json)
        } else if (videoSpan.hasClass('icon01shipinchuang-shexiangtou-guanbi')) {
          videoSpan.removeClass('icon01shipinchuang-shexiangtou-guanbi').addClass('icon01shipinchuang-shexiangtou-kaiqi')
          webRtcUiKit.SetCameraStatus(true)
          var json = JSON.parse('{}')
          json.chatroom = webRtcUiKit.getmyjson().chatroom
          json.msgtype = 'EventCameraOpen'
          json.fromuser = webRtcUiKit.getmyjson().fromuser
          webRtcUiKit.sendJson(json)
        }

        videoBtnClickTimer = null
      }
    }

    // 开关本地音频按钮

    var voiceBtn = jqueryMap.voiceBtn
    var voiceBtnClickTimer = null
    voiceBtn.bind('click', function () {
      if (voiceBtnClickTimer === null) {
        voiceBtnClickTimer = setTimeout(voiceBtnClickService, 200)
      }
    })

    function voiceBtnClickService () {
      if (voiceBtnClickTimer) {
        clearTimeout(voiceBtnClickTimer)
        var voiceSpan = $('#' + parentDivName + ' #voiceBtn span')
        if (voiceSpan.hasClass('icon03shipinchuang-yuyin-kaiqi1')) {
          voiceSpan.removeClass('icon03shipinchuang-yuyin-kaiqi1').addClass('iconshipinchuang-yuyin-guanbi22222221')
          webRtcUiKit.SetMicphoneStatus(false)
          var json = JSON.parse('{}')
          json.chatroom = webRtcUiKit.getmyjson().chatroom
          json.msgtype = 'EventMicphoneClose'
          json.fromuser = webRtcUiKit.getmyjson().fromuser
          webRtcUiKit.sendJson(json)
        } else if (voiceSpan.hasClass('iconshipinchuang-yuyin-guanbi22222221')) {
          voiceSpan.removeClass('iconshipinchuang-yuyin-guanbi22222221').addClass('icon03shipinchuang-yuyin-kaiqi1')
          var json = JSON.parse('{}')
          json.chatroom = webRtcUiKit.getmyjson().chatroom
          json.msgtype = 'EventMicphoneOpen'
          json.fromuser = webRtcUiKit.getmyjson().fromuser
          webRtcUiKit.sendJson(json)
          webRtcUiKit.SetMicphoneStatus(true)
        }

        voiceBtnClickTimer = null
      }
    }

    // 开关扬声器

    var speakBtn = jqueryMap.speakBtn
    var speakBtnClickTimer = null
    speakBtn.bind('click', function () {
      if (!speakBtnClickTimer) {
        speakBtnClickTimer = setTimeout(speakBtnClickService, 200)
      }
    })

    function speakBtnClickService () {
      if (speakBtnClickTimer) {
        var speakSpan = $('#' + parentDivName + ' #speakBtn span')
        if (speakSpan.hasClass('icon13shipinchuang-laba-kaiqi1')) {
          speakSpan.removeClass('icon13shipinchuang-laba-kaiqi1').addClass('icon13shipinchuang-laba-guanbi1') //webRtcUiKit.SetMicphoneStatus(false);
          webRtcUiKit.setVolume(0.0)
        } else if (speakSpan.hasClass('icon13shipinchuang-laba-guanbi1')) {
          speakSpan.removeClass('icon13shipinchuang-laba-guanbi1').addClass('icon13shipinchuang-laba-kaiqi1')
          webRtcUiKit.setVolume(1.0)
        }

        speakBtnClickTimer = null
      }
    }

    // 开关扬声器

    var holdBtn = jqueryMap.holdBtn
    var holdBtnClickTimer = null
    holdBtn.bind('click', function () {
      if (holdBtnClickTimer === null) {
        holdBtnClickTimer = setTimeout(holdBtnClickService, 200)
      }
    })

    function holdBtnClickService () {
      if (holdBtnClickTimer) {
        var holdSpan = $('#' + parentDivName + ' #holdBtn span')
        if (holdSpan.hasClass('icon04shipinchuang-baochi2')) {
          holdSpan.removeClass('icon04shipinchuang-baochi2').addClass('icon05shipinchuang-quhui')
          webRtcUiKit.xchatkit.HoldCall(webRtcUiKit.getmyjson())
          webRtcUiKit.xchatkit.DisableMicphone(webRtcUiKit.getmyjson())
          webRtcUiKit.xchatkit.DisableCamera(webRtcUiKit.getmyjson())
          var json = JSON.parse('{}')
          json.chatroom = webRtcUiKit.getmyjson().chatroom
          json.msgtype = 'EventPCHoldCall'
          json.fromuser = webRtcUiKit.getmyjson().fromuser
          webRtcUiKit.sendJson(json)
          if (window.CONFIG.debugAll) console.log('------' + new Date() + ', log info [ 保持 ]')
          webRtcUiKit.srcObject = $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).get(0).srcObject
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).removeAttr('autoplay')
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).get(0).srcObject = null
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).get(0).poster = './img/holdavatar.png'
          jqueryMap.startShare.hide();
          jqueryMap.virtualBg.hide();
          jqueryMap.switchVoiceAndVideoBtn.hide();
        } else if (holdSpan.hasClass('icon05shipinchuang-quhui')) {
          holdSpan.removeClass('icon05shipinchuang-quhui').addClass('icon04shipinchuang-baochi2')
          webRtcUiKit.xchatkit.RetrieveCall(webRtcUiKit.getmyjson())
          webRtcUiKit.xchatkit.EnableCamera(webRtcUiKit.getmyjson())
          webRtcUiKit.xchatkit.EnableMicphone(webRtcUiKit.getmyjson())
          var json = JSON.parse('{}')
          json.chatroom = webRtcUiKit.getmyjson().chatroom
          json.msgtype = 'EventPCRetrieveCall'
          json.fromuser = webRtcUiKit.getmyjson().fromuser
          webRtcUiKit.sendJson(json)
          if (window.CONFIG.debugAll) console.log('------' + new Date() + ', log info [ 取回 ]')
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).get(0).srcObject = webRtcUiKit.srcObject
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).attr('autoplay', 'autoplay')
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).get(0).poster = ''
          videoBtn.removeClass('video-btn-s').addClass('video-btn')
          voiceBtn.removeClass('voice-btn-s').addClass('voice-btn')
          jqueryMap.startShare.show();
          jqueryMap.virtualBg.show();
          jqueryMap.switchVoiceAndVideoBtn.show();
        }

        holdBtnClickTimer = null
      }
    }
    // 切换视频布局
    jqueryMap.changeDrawingMode.bind('click', function () {
      webRtcUiKit.xchatkit.ChangeDrawingMode()
    })
    // 全屏
    var fullScreen = jqueryMap.fullScreen
    var fullScreenBtnClickTimer = null
    fullScreen.bind('click', function () {
      if (fullScreenBtnClickTimer === null) {
        fullScreenBtnClickTimer = setTimeout(fullScreenBtnClickService, 200)
      }
    })

    function fullScreenBtnClickService () {
      $('#video-screen' + webRtcUiKit.getmyjson().chatroom).attr('style', '')
      var fullScreenSpan = $('#' + parentDivName + ' #fullscreen-btn span')
      if (fullScreenSpan.hasClass('icon15shipinchuang-quanping-guan')) {
        fullScreenSpan.removeClass('icon15shipinchuang-quanping-guan').addClass('iconshipinchuang-quanping-kai')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom).removeClass('rtc-default-video-screen').addClass('popup-video-screen-div-fullscreen')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' .main-video-change').removeClass('main-video-change').addClass('main-video-change-fullscreen')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' .main-video-box').removeClass('main-video-box').addClass('main-video-box-fullscreen')
        $('.main-video-change').css({'max-height':'100%','max-width':'100%','margin-top':$(".main-video-box").height() / 2 - $(".main-video-change").height() / 2+"px"})
        $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).css('height',$('#video-screen'+webRtcUiKit.getmyjson().chatroom).height()-62+"px")
        $('.main-video-change').addClass("main-video-change-fullscreen").removeClass("main-video-change")
        var underlineSpan = $('#' + parentDivName + ' #underlineBtn span')
        if (underlineSpan.hasClass('iconshipinchuang-huaxian-guan')) {
            $(".popup-video-screen-div-fullscreen").css("height",$(document).height()-62)
        }
      } else {
        fullScreenSpan.removeClass('iconshipinchuang-quanping-kai').addClass('icon15shipinchuang-quanping-guan')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom).removeClass('popup-video-screen-div-fullscreen').addClass('rtc-default-video-screen')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' .main-video-change-fullscreen').removeClass('main-video-change-fullscreen').addClass('main-video-change')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' .main-video-box-fullscreen').removeClass('main-video-box-fullscreen').addClass('main-video-box')
        $('.main-video-change-fullscreen').css({'max-height':'100%','max-width':'100%','margin-top':$(".main-video-box-fullscreen").height() / 2 - $(".main-video-change-fullscreen").height() / 2+"px"})
        $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).css('height',$('#video-screen'+webRtcUiKit.getmyjson().chatroom).height()-62+"px")
        $('.main-video-change-fullscreen').addClass("main-video-change").removeClass("main-video-change-fullscreen")
      }

      fullScreenBtnClickTimer = null
    }

    // 远程协助划线
    var underlineBtn = jqueryMap.underlineBtn
    underlineBtn.bind('click', function () {
      var underlineSpan = $('#' + parentDivName + ' #underlineBtn span')
      if (underlineSpan.hasClass('iconshipinchuang-huaxian-kai')) {
        underlineSpan.removeClass('iconshipinchuang-huaxian-kai').addClass('iconshipinchuang-huaxian-guan')
        $("#colorChoose").show()
        $('div[name=video-screen]').draggable('disable')
        webRtcUiKit.startAssistance(webRtcUiKit.getmyjson())
        var fullScreenSpan = $('#' + parentDivName + ' #fullscreen-btn span')
        if (fullScreenSpan.hasClass('iconshipinchuang-quanping-kai')){
          $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).css('height',$('#video-screen'+webRtcUiKit.getmyjson().chatroom).height()-112+"px")
          $(".main-video-box-fullscreen").css('height',$('#video-screen'+webRtcUiKit.getmyjson().chatroom).height()-110+"px")
        }
      } else {
        underlineSpan.removeClass('iconshipinchuang-huaxian-guan').addClass('iconshipinchuang-huaxian-kai')
        $("#colorChoose").hide()
        $('div[name=video-screen]').draggable('enable')
        webRtcUiKit.stopAssistance(webRtcUiKit.getmyjson())
        $('#videoPlayer' + webRtcUiKit.getmyjson().chatroom).css('height',$('#video-screen'+webRtcUiKit.getmyjson().chatroom).height()-62+"px")
        $(".main-video-box-fullscreen").css('height',$('#video-screen'+webRtcUiKit.getmyjson().chatroom).height()-60+"px")
      }
    })

    // 窗口隐藏
    var headHide = jqueryMap.headHide
    headHide.bind('click', function () {})
    partyNum = new webRtcUiKit.Map()
    self.viewControl()
  }

  // 隐藏的按钮 'hangup', 'close', 'hide', 'hold', 'video', 'voice', 'speak', 'fullScreen', 'CONFIG.answerType', 'change', 'videoHead'
  this.viewControl = function () {
    if (self.options.btnMap) {
      for (var key in self.options.btnMap) {
        switch (self.options.btnMap[key]) {
          case 'hangup':
            jqueryMap.hangupBtn.hide()
            break

          case 'close':
            jqueryMap.headClose.hide()
            break

          case 'hide':
            jqueryMap.headHide.hide()
            break

          case 'hold':
            jqueryMap.holdBtn.hide()
            break

          case 'video':
            jqueryMap.videoBtn.hide()
            break

          case 'voice':
            jqueryMap.voiceBtn.hide()
            break

          case 'speak':
            jqueryMap.speakBtn.hide()
            break

          case 'fullScreen':
            jqueryMap.fullScreen.hide()
            break

          case 'CONFIG.answerType':
            jqueryMap.CONFIG.answerTypeBtn.hide()
            break

          case 'change':
            jqueryMap.switchVoiceAndVideoBtn.hide()
            break

          case 'videoHead':
            jqueryMap.videoHead.hide()
            $('.main-video-box').css('height','calc(100% - 30px)')
            break
          case 'takePhoto':
            jqueryMap.takePhoto.hide()
            break
          case 'changedrawingmode':
            jqueryMap.changeDrawingMode.hide()
            break
          case 'share':
            jqueryMap.startShare.hide()
            break
          case 'startRecord':
            jqueryMap.startRecord.parent().hide()
            break
          case 'divided-record-btn':
            jqueryMap.dividedRecord.parent().hide()
            break
          case 'server-record-btn':
            jqueryMap.startServerRecord.parent().hide()
            break
          case 'virtualBg':
            jqueryMap.virtualBg.hide()
            break
          case 'whiteboard':
            jqueryMap.whiteboard.hide()
            break
          case 'switchCamera':
            jqueryMap.switchCamera.hide()
            break
        }
      }
    }
    if(jqueryMap.startRecord.parent().css("display") == "none" && jqueryMap.dividedRecord.parent().css("display") == "none" && jqueryMap.startServerRecord.parent().css("display") == "none"){
      jqueryMap.openRecordBtn.hide();
    }
    if(webRtcUiKit.getmyjson().msgtype == "voice"){
      jqueryMap.switchCamera.hide()
      jqueryMap.startShare.hide()
      jqueryMap.whiteboard.hide()
      jqueryMap.virtualBg.hide()
      jqueryMap.changeDrawingMode.hide()
      jqueryMap.takePhoto.hide()
      jqueryMap.videoBtn.hide()
      jqueryMap.fullScreen.hide()
      jqueryMap.openRecordBtn.hide();
      jqueryMap.holdBtn.hide()
    }
  }

  this.initVideo = function (fromUser, roomId, parent, role, streamDirection, audioMixer, makeType, userData) {
    if (!roomId) {
      if (window.CONFIG.debugAll) {
        console.log('chat room can not be null or undefined!')
      }
      return false
    }

    if (window.CONFIG.debugAll) {
      console.info('开始建立视频窗口')
    }
   // webRtcUiKit.getParams();
   // if (role.indexOf('Agent') > -1 || role == '1' || role == 1 || role == 'Monitor') {
      userData.isagent = 1
    //} else {
     // userData.isagent = 0
   // }

    this.myjson.mgw = userData.channeltype == 'xiaochengxu' ? window.CONFIG.RTC.MGW_XCX : window.CONFIG.RTC.MGW
    this.myjson.msgtype = makeType
    this.myjson.autoleave = 0
    this.myjson.role = "Agent"
    this.myjson.fromuser = fromUser
    this.myjson.chatroom = roomId
    this.myjson.mp4player = document.getElementById("mp4Player")
    this.myjson.statinfo = 1
    this.myjson.callback = this.oncallback
    this.myjson.player = document.getElementById('videoPlayer' + roomId)
    if(userData.channeltype == 'xiaochengxu' || this.options.browserType == 'WPhone'){
      this.myjson.wphone = window.CONFIG.RTC.MGW_WPhone
    }
    if(userData.channeltype == 'xiaochengxu'){
      this.myjson.flv = window.CONFIG.RTC.flv
      this.myjson.rtmp = window.CONFIG.RTC.rtmp
    }
    if ('video' == makeType) {
      this.myjson.camera = window.CONFIG.RTC.camera
    } else if ('call' == makeType) {
      this.myjson.camera = '-1'
    } else if ('share' == makeType) {
      this.myjson.camera = '-2'
    } else {
      this.myjson.camera = '-1'
    }

    this.myjson.recvonly = 0
    this.myjson.maxwidth = '640'
    if(window.CONFIG.RTC.maxwidth){
      this.myjson.maxwidth = window.CONFIG.RTC.maxwidth
    }
    if(window.CONFIG.RTC.maxheight){
      this.myjson.maxheight = window.CONFIG.RTC.maxheight
    }
    if(window.CONFIG.RTC.maxrecwidth){
      this.myjson.maxrecwidth = CONFIG.RTC.maxrecwidth
    }
    if(window.CONFIG.RTC.timestamp){
      if(window.CONFIG.RTC.timestamp == "add"){
        this.myjson.showtime = "1"
      }
    }
    if(window.CONFIG.RTC.maxbps){
      this.myjson.maxbps = window.CONFIG.RTC.maxbps
    }
    if(window.CONFIG.RTC.videoFileMode){
      if(window.CONFIG.RTC.videoFileMode == "all-in-one"){
        //this.myjson.videomixer = 1
        this.myjson.audiomixer = 1
      }else{
        this.myjson.audiomixer = 0
        //this.myjson.videomixer = 0
      }
    }
    if(window.CONFIG.RTC.pictureWatermarkLocation){
      this.myjson.logopos = window.CONFIG.RTC.pictureWatermarkLocation
    }
    if(window.CONFIG.RTC.pictureWatermark){
      this.myjson.imagelogo = window.CONFIG.RTC.pictureWatermark
      this.myjson.logoratio = 1
    }
    if(window.CONFIG.RTC.textWatermarkLocation){
      this.myjson.textpos = window.CONFIG.RTC.textWatermarkLocation
    }
    if(window.CONFIG.RTC.textWatermark){
      this.myjson.textlogo = window.CONFIG.RTC.textWatermark
    }
    this.myjson.pip = "1"
    if(window.CONFIG.RTC.pip){
      this.myjson.pip = CONFIG.RTC.pip
    }
    if(window.CONFIG.RTC.framerate){
      this.myjson.framerate = CONFIG.RTC.framerate
    }
    if(window.CONFIG.RTC.imagemixer){
      this.myjson.virtualbg = window.CONFIG.RTC.imagemixer
    }
    if(window.CONFIG.RTC.h264){
      this.h264 = window.CONFIG.RTC.h264
    }
    this.myjson.linewidth = "4"
    this.myjson.linecolor = "#FF0000"
    this.myjson.userdata = userData

   // if (role.indexOf('Agent') > -1 || role == '1' || role == 1 || role == 'Monitor') {
      this.myjson.isagent = 1
  //  }

/*    if (audioMixer) {
      this.myjson.audiomixer = CONFIG.RTC.audioMixer;
    }*/
    if(window.CONFIG.RTC.portrait){
      this.myjson.portrait= window.CONFIG.RTC.portrait
    }
    if(window.CONFIG.RTC.turn){
      if(window.CONFIG.RTC.turn != "undefined"){
        this.myjson.turn= window.CONFIG.RTC.turn
      }
    }
    if (window.CONFIG.debugAll) {
      console.info('开始new xchat kit')
    }

    if (this.xchatkit) {
      if (window.CONFIG.debugAll) console.info('xchat已存在，开始销毁')
      this.xchatkit.CloseWS()
    }

    this.xchatkit = new XChatKit(this.myjson)

    if (window.CONFIG.debugAll) {
      console.log('------' + new Date() + ', log info [初始化参数完毕。。。。。 ]')
    }
  }

  this.connectWphone = function (fromuser, chatroom) {
    var myjson = {}
    myjson.wphone = window.CONFIG.WPHONE_URL
    myjson.fromuser = fromuser
    myjson.callback = this.onWphoneCallback
    myjson.player = document.getElementById('videoPlayer' + chatroom)
    this.xchatWphone = new XChatKit(myjson)
    EventOpenSuccess()
  }

  this.selfCheck = function () {
    this.xchatkit.SelfCheck()
  }


  this.SetCameraStatus = function (flag) {
    if (flag) {
      webRtcUiKit.xchatkit.EnableCamera(webRtcUiKit.getmyjson())
    } else {
      webRtcUiKit.xchatkit.DisableCamera(webRtcUiKit.getmyjson())
    }
  }

  this.SetMicphoneStatus = function (flag) {
    if (flag) {
      webRtcUiKit.xchatkit.EnableMicphone(webRtcUiKit.getmyjson())
    } else {
      webRtcUiKit.xchatkit.DisableMicphone(webRtcUiKit.getmyjson())
    }
  }

  this.ChangeDrawingMode = function () {
    webRtcUiKit.xchatkit.ChangeDrawingMode(webRtcUiKit.getmyjson())
  }

  this.getChatRoom = function () {
    return this.myjson.chatroom
  }

  this.getmyjson = function () {
    return this.myjson
  }
  this.setmyjson = function (myjson) {
    this.myjson = myjson
  }


  this.clearmyjson = function () {
    this.myjson = JSON.parse('{}')
  }

  this.getxchatkit = function () {
    return xchatkit
  }

  this.getvideostatus = function () {
    if ($('#video-screen' + this.myjson.chatroom).length > 0) {
      return true
    } else {
      return false
    }
  }

  this.getfirstVideoRole = function () {
    return firstVideoRole
  }

  this.setfirstVideoRole = function (role) {
    firstVideoRole = role
  }

  this.startServerRecording = function () {
    var json = webRtcUiKit.getmyjson();
    json.pip = 0
    this.xchatkit.StartServerRecording(json)
  }
  this.startClientRecording = function () {
    this.xchatkit.StartClientRecording(webRtcUiKit.getmyjson())
  }

  this.startSharing = function () {
    this.xchatkit.StartSharing(this.myjson)
  }
  this.stopSharingView = function () {
    jqueryMap.startShare.removeClass('stopShare-btn').addClass('share-btn')
    console.log("------" + new Date() + ", log info [停止共享 ]");
    jqueryMap.videoBtn.show();
    if(webRtcUiKit.options.auths.VideoHold){
      jqueryMap.holdBtn.show();
    }
    if(webRtcUiKit.options.auths.VirtualBackground){
      jqueryMap.virtualBg.show();
    }
    if(webRtcUiKit.options.auths.SwitchVoiceVideo){
      jqueryMap.switchVoiceAndVideoBtn.show();
    }
  }

  this.StopSharing = function () {
    this.xchatkit.StopSharing(this.myjson)
  }

  this.setPartyNumber = function () {
    if (webRtcUiKit.xchatkit.GetTotalPeers() > 1) {
      $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' #holdBtn').hide()
    } else {
      if(!jqueryMap.startShare.hasClass('stopShare-btn') && !jqueryMap.startShare.hasClass('to-video-btn-s')){
        if(webRtcUiKit.options.auths && webRtcUiKit.options.auths.VideoHold){
          $('#video-screen' + webRtcUiKit.getmyjson().chatroom + ' #holdBtn').show()
        }
      }
    }
  }

  this.oncallback = function (json) {
    webRtcMsgHandler.callback(json)
    webRtcUiKit.setPartyNumber();
  }
  this.onmonitorcallback = function (json) {
    webRtcMsgHandler.monitorcallback(json)
  }
  this.onsettingcallback = function (json) {
    webRtcMsgHandler.onsettingcallback(json)
  }
  this.onWphoneCallback = function (json) {
    if (window.CONFIG.debugAll) console.log('收到wphone回调')
  }

  this.sendJson = function (json) {
    this.xchatkit.SendJSON(json)
  }

  this.sendMsg2IE = function (json) {
    this.xchatWphone.Send2WPhone(json)
  }

  this.setVolume = function (num) {
    webRtcUiKit.xchatkit.SetVolume(num)
  }

  this.startAssistance = function (json) {
    webRtcUiKit.xchatkit.StartAssistance(json)
  }

  this.stopAssistance = function (json) {
    webRtcUiKit.xchatkit.StopAssistance(json)
  }

  this.setBtnDisplay = function (btnId, status) {
    var parentDivName = 'video-screen' + myjson.chatroom
    $('#' + parentDivName + ' #' + btnId).css('display', status)
  }

  this.setStartUpTime = function () {
    this.startUpTime = new Date().getTime()
  }

  this.getStartUpTime = function () {
    return startUpTime
  }
  /**
   * 开始启动计时
   *
   */


  this.setTimer = function () {
    var sec = 0
    var starttime = Date.parse(new Date())

    if (intervalTimer) {
      self.closeTimer(intervalTimer)
      intervalTimer = null
    }

    intervalTimer = setInterval(function () {
      var now = Date.parse(new Date())
      var data = (now - starttime) / 1000
      var hour = parseInt(data / 3600)
      var minute = parseInt(data % 3600 / 60)
      var second = data % 3600 % 60
      jqueryMap.timerTxt.text(two_char(hour) + ':' + two_char(minute) + ':' + two_char(second))
    }, 1000)
  }
  /**
   * 停止窗口计时
   *
   */


  this.closeTimer = function () {
    if (intervalTimer) {
      clearInterval(intervalTimer)
      jqueryMap.timerTxt.text('00:00:00')
      intervalTimer = null
    }
  }
  this.takePhoto = function(event) {
    var json = JSON.parse("{}");
    json.msgtype = "EventPhotoTaken"
    json.fromuser = webRtcUiKit.getmyjson().fromuser
    json.content = webRtcUiKit.xchatkit.TakePhoto(0.5, null, 0.5)
    if(json.content != ''){
      var bizMessage = {
        event: bizConstants.EventSnap[event],
        data: {snapUrl: json.content}
      }
      globalUserData.sendMsgToBusines_SN(bizMessage)
      webRtcUiKit.xchatkit.Send2WPhone(bizMessage)
    }
  }
  this.riskTips = function(obj) {
    console.log("recive riskTips = "+JSON.stringify(obj))
    if(obj.tipType == "video" || obj.tipType == "voice"){
        $("#mp4Player").show()
        $("#closeRisk-btn").show()
        $("#videoPlayer"+webRtcUiKit.getmyjson().chatroom).css("float","left");
        $("#videoPlayer"+webRtcUiKit.getmyjson().chatroom).css("width","50%");
        var myjson = new Object();
        myjson.content = obj.content
        myjson.mixed = 1
        console.log("风险提示json")
        console.log(myjson)
        webRtcUiKit.xchatkit.StartVideoPlaying(myjson)
    }
    if(obj.tipType == "ppt" || obj.tipType == "word"  || obj.tipType == "pdf"){
      if(webRtcUiKit.options.browserType == "WPhone"){
        var fileUrl = window.CONFIG.RTC.command+'://' + obj.content;
        setFileIframeSrc(encodeURI(fileUrl));
      }else{
        window.open(obj.content)
      }
      setTimeout(function () {
        jqueryMap.startShare.trigger("click")
      },500);
    }
  }
  this.setNetworkStatusTips = function(obj) {
    console.log("recive setNetworkStatusTips = "+JSON.stringify(obj))
    $(".video-tips-div").html(obj.content)

  }
  this.closeRisk = function () {
    $("#videoPlayer"+webRtcUiKit.getmyjson().chatroom).css("width","100%");
    $("#mp4Player").hide()
    $("#closeRisk-btn").hide()
    $("#mp4Player").attr("src","")
    webRtcUiKit.xchatkit.StopVideoPlaying()
  }
  this.changeToVideo = function() {
    var msg = {
      fromuser: webRtcUiKit.getmyjson().fromuser,
      chatroom: webRtcUiKit.getmyjson().chatroom,
      msgtype: 'EventChangeToVideo'
    }
    webRtcUiKit.sendJson(msg)
    jqueryMap.videoBtn.show();
    if(webRtcUiKit.options.auths.VideoHold){
      jqueryMap.holdBtn.show();
    }
    if(webRtcUiKit.options.auths.Sharing){
      jqueryMap.startShare.show();
    }
    if(webRtcUiKit.options.auths.SwitchVoiceVideo){
      jqueryMap.switchVoiceAndVideoBtn.show();
    }
  }
  this.changeToVoice = function() {
    var msg = {
      fromuser: webRtcUiKit.getmyjson().fromuser,
      chatroom: webRtcUiKit.getmyjson().chatroom,
      msgtype: 'EventChangeToVoice'
    }
    webRtcUiKit.sendJson(msg)
    jqueryMap.videoBtn.hide();
    jqueryMap.holdBtn.hide();
    jqueryMap.startShare.hide();
    jqueryMap.virtualBg.hide();
  }
  /**
   * 弹出视频窗口
   *
   * @param {string} parent - 父级元素ID
   * @param {string} sessionId - 唯一标识
   */
  this.popupVideoScreen = function (parent, divId, fromUser,msgtype) {
    $('div[name=video-screen]').remove()
    webRtcUiKit.joinFlag = false // pop new screen
    var resetbtn = '';
    //if(webRtcUiKit.getmyjson().userdata.channeltype == 'xiaochengxu'){
      resetbtn = '<div title="重置" class="resetPlayers-btn" id="resetPlayers" style="width: 30px;height:30px;float: right;margin-right: 5px"><span class="icon iconfont iconshuaxin"></span></div>'
    //}
    var colorChooseBtn = "\t\t<div id=\"colorChoose\" style=\"height: 50px;width: 100%;display: none\">\n" +
        "\t\t\t\t\t<div id=\"\" class=\"qipao\" style=\"float: right;\">\n" +
        "\t\t\t<div id=\"linewidth1\" class=\"linewidth\" onclick=\"webRtcUiKit.chooseLineStyle('1','12','linewidth1')\" style=\"float: left;height: 6px;width: 6px;border-radius: 6px;background-color: #C5C6CB;margin-left: 15px;margin-top: 13px;\"></div>\n" +
        "\t\t\t<div id=\"linewidth2\" class=\"linewidth\" onclick=\"webRtcUiKit.chooseLineStyle('1','24','linewidth2')\"style=\"float: left;height: 12px;width: 12px;border-radius: 12px;background-color: #FF0000;margin-left: 15px;margin-top: 10px;\"></div>\n" +
        "\t\t\t<div id=\"linewidth3\" class=\"linewidth\" onclick=\"webRtcUiKit.chooseLineStyle('1','36','linewidth3')\" style=\"float: left;height: 18px;width: 18px;border-radius: 18px;background-color: #C5C6CB;margin-left: 15px;margin-top: 7px;\"></div>\n" +
        "\t\t\t<div id=\"linecolor1\" class=\"linecolor\" onclick=\"webRtcUiKit.chooseLineStyle('0','#FF0000','linecolor1')\" style=\"float: left;height: 20px;width: 20px;background-color: #FF0000;margin-left: 15px;margin-top: 5px;text-align: center;\">\n" +
        "\t\t\t\t<div style=\"height: 10px;width: 10px;background-color: #FFFFFF;margin-left: 5px;margin-top: 5px;\"></div>\n" +
        "\t\t\t</div>\n" +
        "\t\t\t<div id=\"linecolor2\" class=\"linecolor\" onclick=\"webRtcUiKit.chooseLineStyle('0','#FFBE00','linecolor2')\" style=\"float: left;height: 20px;width: 20px;background-color: #FFBE00;margin-left: 15px;margin-top: 5px;\">\n" +
        "\t\t\t\t<div style=\"height: 10px;width: 10px;background-color: #FFFFFF;margin-left: 5px;margin-top: 5px;display: none;\"></div>\n" +
        "\t\t\t</div>\n" +
        "\t\t\t<div id=\"linecolor3\" class=\"linecolor\" onclick=\"webRtcUiKit.chooseLineStyle('0','#1A9BFF','linecolor3')\" style=\"float: left;height: 20px;width: 20px;background-color: #1A9BFF;margin-left: 15px;margin-top: 5px;\">\n" +
        "\t\t\t\t<div style=\"height: 10px;width: 10px;background-color: #FFFFFF;margin-left: 5px;margin-top: 5px;display: none;\"></div>\n" +
        "\t\t\t</div>\n" +
        "\t\t\t<div id=\"linecolor4\" class=\"linecolor\" onclick=\"webRtcUiKit.chooseLineStyle('0','#1AAD19','linecolor4')\" style=\"float: left;height: 20px;width: 20px;background-color: #1AAD19;margin-left: 15px;margin-top: 5px;\">\n" +
        "\t\t\t\t<div style=\"height: 10px;width: 10px;background-color: #FFFFFF;margin-left: 5px;margin-top: 5px;display: none;\"></div>\n" +
        "\t\t\t</div>\n" +
        "\t\t\t<div id=\"linecolor5\" class=\"linecolor\" onclick=\"webRtcUiKit.chooseLineStyle('0','#333333','linecolor5')\" style=\"float: left;height: 20px;width: 20px;background-color: #333333;margin-left: 15px;margin-top: 5px;\">\n" +
        "\t\t\t\t<div style=\"height: 10px;width: 10px;background-color: #FFFFFF;margin-left: 5px;margin-top: 5px;display: none;\"></div>\n" +
        "\t\t\t</div>\n" +
        "\t\t\t<div id=\"linecolor6\" class=\"linecolor\" onclick=\"webRtcUiKit.chooseLineStyle('0','#FFFFFF','linecolor6')\" style=\"float: left;height: 20px;width: 20px;background-color: #FFFFFF;margin-left: 15px;margin-top: 5px;\">\n" +
        "\t\t\t\t<div style=\"height: 10px;width: 10px;background-color: #EEEEEE;margin-left: 5px;margin-top: 5px;display: none;\"></div>\n" +
        "\t\t\t</div>\n" +
        "\t\t</div>\n" +
        "\t\t<div class=\"demo\"></div>\n" +
        "\t\t</div>"
    var popUpHtml = '<div id="video-screen' + divId + '" name="video-screen">' +
      '<div class="video-head">' + '<div class="head-close"></div>' + '<div class="head-hide" ></div>' + '</div>' +
      '<div id="controller-bottom"  class="controller" align="center">' +
      '<div class="controller-inner">' + '<div class="row">' + '<div style="width: 100px; padding-left: 10px;float:left; text-align: left;">' + '<span style="color: #bdc6cf;font-family : Microsoft YaHei;font-size: 12px" id="timerTxt">00:00:00</span>' + '</div>' +
      '<div title="挂断" class="video-opt-btn" id="hangupBtn" style="width: 70px; float: right; margin-right:10px;background-color: red;border-radius:5px " ><span class="icon iconfont iconSIP-guaji"></span></div>' +
      '<div title="画线" class="video-opt-btn" id="underlineBtn" style="width: 30px; float: right;margin-right: 5px" ><span class="icon iconfont iconshipinchuang-huaxian-kai"></span></div>' +
      '<div title="拍照" class="video-opt-btn" id="takePhotoBtn" style="width: 30px;float: right;margin-right: 5px" ><span class="icon iconfont icon02shipinchuang-paizhao"></span></div>' +
      '<div title="切换布局" class="video-opt-btn" id="changedrawingmode-btn" style="width: 30px;float: right;margin-right: 5px"  ><span class="icon iconfont iconshipinchuang-buju"></span></div>' +
      '<div title="全屏" class="video-opt-btn" id="fullscreen-btn" style="width: 30px; float: right;margin-right: 5px"><span class="icon iconfont icon15shipinchuang-quanping-guan"></span></div>' +
      '<div title="保持/取回" class="video-opt-btn" id="holdBtn" style="width: 30px;  float: right;margin-right: 5px"><span class="icon iconfont icon04shipinchuang-baochi2"></span></div>' +
      '<div title="开/关摄像头" class="video-opt-btn" id="videoBtn" style="width: 30px; float: right;margin-right: 5px"><span class="icon iconfont icon01shipinchuang-shexiangtou-kaiqi"></span></div>' +
      '<div title="开/关麦克风" class="video-opt-btn" id="voiceBtn" style="width: 30px; float: right;margin-right: 5px"><span class="icon iconfont icon03shipinchuang-yuyin-kaiqi1"></span></div>' +
      '<div title="开/关扬声器" class="video-opt-btn" id="speakBtn" style="width: 30px; float: right;margin-right: 5px"><span class="icon iconfont icon13shipinchuang-laba-kaiqi1"></span></div>' +
      '<div title="音视频转换" class="video-opt-btn" id="video-voice" style="width: 30px; float: right;margin-right: 5px"><span class="icon iconfont iconshipinchuang-yuyinzhuanshipin"></span></div>' +
      '<div title="共享桌面" class="video-opt-btn" id="shareBtn" style="width: 30px;float: right;margin-right: 5px"><span class="icon iconfont iconshipinchuang-gongxiang-copy"></span></div>'+
      '<div title="录制" class="video-opt-btn-r" id="open-record-btn" style="width: 30px;float: right;margin-right: 5px"><svg t="1606373670517" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7606" width="30" height="30"><path d="M511.9 129.9c51.7 0 101.8 10.1 149 30.1 45.6 19.3 86.5 46.9 121.7 82.1s62.8 76.1 82.1 121.7c19.9 47.2 30.1 97.3 30.1 149s-10.1 101.8-30.1 149c-19.3 45.6-46.9 86.5-82.1 121.7s-76.1 62.8-121.7 82.1c-47.2 19.9-97.3 30.1-149 30.1s-101.8-10.1-149-30.1c-45.6-19.3-86.5-46.9-121.7-82.1s-62.8-76.1-82.1-121.7c-19.9-47.2-30.1-97.3-30.1-149s10.1-101.8 30.1-149c19.3-45.6 46.9-86.5 82.1-121.7s76.1-62.8 121.7-82.1c47.1-20 97.3-30.1 149-30.1m0-64c-246.8 0-446.8 200-446.8 446.8s200 446.8 446.8 446.8 446.8-200 446.8-446.8S758.6 65.9 511.9 65.9z" fill="#BDC6CF" p-id="7607"></path><path d="M511.7 511.7m-191.6 0a191.6 191.6 0 1 0 383.2 0 191.6 191.6 0 1 0-383.2 0Z" fill="#FF3939" p-id="7608"></path></svg></div>'+
        /*      '<div title="分流录像" class="divided-record-btn" style="width: 30px;float: right;" />'+
      '<div title="服务端录像" class="server-record-btn" style="width: 30px;float: right;" />'+*/
      '<div title="开/关虚拟背景" class="video-opt-btn" id="virtualbgBtn" style="width: 30px;float: right;margin-right: 5px"><span class="icon iconfont iconshipinchuang-xunibeijing-kai"></span></div>' +
      '<div title="发起白板" class="video-opt-btn" id="v-startwhiteboard" style="width: 30px;float: right;margin-right: 5px"><span class="icon iconfont icon09shipinchuang-baiban"></span></div>' +
      '<div title="切换摄像头" class="video-opt-btn" id="switchCamera" style="width: 30px;float: right;margin-right: 5px"><span class="icon iconfont icon14shipinchuang-qiehuanshexiangtou"></span></div>' +
      resetbtn+
      '<div title="关闭风险提示" id="closeRisk-btn" class="video-opt-btn"  style="width: 30px;float: right;display: none;margin-right: 5px" onclick="webRtcUiKit.closeRisk()"><span class="icon iconfont iconshipinchuang-fengxian-guan"></span></div>'+
      '</div>' + '</div>' + '</div>' + colorChooseBtn + '</div>'
    $(parent).append(popUpHtml)
    $(parent).addClass('rtc-wrapper')
    var photoPrewDiv = '<div class="photoPrewDiv" style="display: none;">' +
      '<div title="关闭" class="closePic-btn"  style="width: 30px;float:right;" onclick="webRtcUiKit.closePhotoPrew()"><span class="icon iconfont iconguanbi" style="color: #FFFFFF;font-size: 18px"></span></div>'+
      '<div title="下载" class="download-btn"  style="width: 30px;float:right;" onclick="webRtcUiKit.downloadImg()"><span class="icon iconfont iconxiazai" style="color: #FFFFFF;font-size: 18px"></span></div>'+
      '<img class="photoPrew" src="" style="height: 100%;"/></div>'
    var tipsDiv = "<div class=\"video-tips-div\"></div>";
    var recordChooseDiv = "<div class=\"record-choose-div\" style='display: none'>" +
      "<div class=\"record-choose-item\"><div class=\"record-btn\"  style='border-top-left-radius: 5px;border-top-right-radius: 5px;color: red'><span class=\"icon iconfont iconshipinchuang-luzhi-kai\"></span></div><div style='float: left;width: 80%'>本地录制</div></div>"+
      "<div class=\"record-choose-item\"><div class=\"server-record-btn\" style='color: red'><span class=\"icon iconfont iconshipinchuang-luzhi-kai\"></span></div><div style='float: left;width: 80%'>服务端录制</div></div>"+
      "<div class=\"record-choose-item\"><div class=\"divided-record-btn\"  style='border-bottom-left-radius: 5px;border-bottom-right-radius: 5px;color: red'><span class=\"icon iconfont iconshipinchuang-luzhi-kai\"></span></div><div style='float: left;width: 80%'>分流录制</div></div>"+
      "</div>";
    if (self.options.videoScreenClass) {
      $('#video-screen' + divId).addClass(self.options.videoScreenClass)
      var riskVideo ='<video id=\'mp4Player\' name=\'mp4Player\' style="float: left;width: 50%;display: none" class=\'main-video-change\'  poster=\'./avatar.png\' playsinline autoplay controls src=""></video><embed id="pdfReader" type="application/pdf" style="display: none;" src="">'
      var mainVideo = '<div class=\'main-video-box\'><video name=\'videoPlayer\' id=\'videoPlayer' + divId + '\' style="margin-top: 0px !important;width:auto; height:360px;"   class=\'main-video-change\'  poster=\'./avatar.png\' playsinline autoplay muted></video>' +riskVideo +
        '</div>';
      if(msgtype == "voice"){
        mainVideo = '<div class=\'main-video-box voicebg\'><video  name=\'videoPlayer\' id=\'videoPlayer' + divId + '\' style="margin-top: 0px !important;width:auto; height:360px;"   class=\'main-video-change\'  poster=\'./avatar.png\' playsinline autoplay muted></video>' +riskVideo +
          '</div>';
      }
      $('#video-screen' + divId + ' #controller-bottom').before(tipsDiv + photoPrewDiv + mainVideo + recordChooseDiv)
    } else {
      $('#video-screen' + divId).addClass('rtc-default-video-screen')
      var riskVideo ='<video id=\'mp4Player\' name=\'mp4Player\' style="float: left;width: 50%;display: none" class=\'main-video-change\'  poster=\'./avatar.png\' playsinline autoplay controls src=""></video><embed id="pdfReader" type="application/pdf" style="display: none;" src="">'
      var mainVideo = '<div class=\'main-video-box\'><video  name=\'videoPlayer\' id=\'videoPlayer' + divId + '\' style=\'width:auto; height:360px;\'  class=\'main-video-change\'  poster=\'./avatar.png\' playsinline autoplay muted></video>'+riskVideo +
        '</div>'
      if(msgtype == "voice"){
        mainVideo = '<div class=\'main-video-box voicebg\'><video  name=\'videoPlayer\' id=\'videoPlayer' + divId + '\' style=\'width:auto; height:360px;\'  class=\'main-video-change\'  poster=\'./avatar.png\' playsinline autoplay muted></video>'+riskVideo +
          '</div>'
      }
      $('#video-screen' + divId + ' #controller-bottom').before(tipsDiv + photoPrewDiv + mainVideo + recordChooseDiv)
      $("#video-screen" + divId).css({"top": $(parent).height() / 2 - $("#video-screen" + divId).height() / 2 +"px", "left": $(parent).width() / 2 - $("#video-screen" + divId).width() / 2 +"px"});
      $('#video-screen' + divId).draggable({
        containment: parent,
        scroll: false
      })
    }
    $('.main-video-change').css({'max-height':'100%','max-width':'100%','margin-top':$(".main-video-box").height() / 2 - $(".main-video-change").height() / 2+"px"})
    webRtcUiKit.setTimer()
  }
  /**
   * 客户创建视频
   *
   * @param roomId 会议号
   * @param fromUser 用户id
   * @param parent 视频窗展示的父容器id
   * @param role role=Agent表示客服，role=Client表示客户
   * @param audioMixer "0"：不混音（各入会成员音频分开录制为多个文件），"1"：混音（各入会人员音频录制在同一文件）
   * @param makeType
   * @param userData
   */


  this.initWebRtcVideoCall = function (roomId, fromUser, parent, role, audioMixer, makeType, userData) {
    webRtcUiKit.popupVideoScreen('#' + parent, roomId, fromUser,makeType)
    webRtcUiKit.initVideo(fromUser, roomId, parent, role, '', audioMixer, makeType, userData)
    webRtcUiKit.init('video-screen' + roomId)
    webRtcUiKit.setStartUpTime()
    if(makeType == "share"){
      webRtcUiKit.options.btnMap.push('hide')
      webRtcUiKit.options.btnMap.push('hold')
      webRtcUiKit.options.btnMap.push('video')
      webRtcUiKit.options.btnMap.push('voice')
      webRtcUiKit.options.btnMap.push('speak')
      webRtcUiKit.options.btnMap.push('change')
    }
    if(makeType == "voice"){
      webRtcUiKit.options.btnMap.push('hide')
      webRtcUiKit.options.btnMap.push('change')
      webRtcUiKit.options.btnMap.push('underline')
      $('#video-screen'+ parent).addClass("video-screen-bg");
    }
    if(makeType == "video"){
      webRtcUiKit.options.btnMap.push('hide')
      webRtcUiKit.options.btnMap.push('underline')
      $('#video-screen'+ parent).addClass("video-screen-bg");
    }
    if(userData.channeltype == "call"){
      $('#video-screen'+ webRtcUiKit.getmyjson().chatroom).hide()
    }
    $('#video-screen'+ webRtcUiKit.getmyjson().chatroom).removeClass("video-screen-bg");
    //webRtcUiKit.viewControl()
    if(webRtcUiKit.options.opttype && webRtcUiKit.options.opttype == "testCamera"){
      webRtcUiKit.getmyjson().wphone = window.CONFIG.RTC.MGW_WPhone
      webRtcUiKit.xchatkit.CameraTest(webRtcUiKit.getmyjson())
    }else{
      webRtcUiKit.xchatkit.JoinConference(webRtcUiKit.getmyjson())
    }
  }
  this.initWebRtcModule = function (roomId, userId, parentId, role, makeType, userData,options) {
    webRtcMsgHandler = new WebRtcMsgHandler()
    webRtcUiKit = new WebRTCUiKit(roomId)
    webRtcUiKit.options = options
    webRtcUiKit.initWebRtcVideoCall(roomId, userId, parentId, role, '1', makeType, userData)
    webRtcUiKit.viewControl()
  }
  this.initTestCameraModule = function (roomId, userId, parentId, role, makeType, userData,options) {
    webRtcMsgHandler = new WebRtcMsgHandler()
    webRtcUiKit = new WebRTCUiKit(roomId)
    webRtcUiKit.options = options
    webRtcUiKit.initWebRtcVideoCall(roomId, userId, parentId, role, '1', makeType, userData)
    webRtcUiKit.viewControl()
    jqueryMap.holdBtn.hide()
  }
  /**
   * 加入视频会议
   *
   * @param roomId
   * @param userId
   * @param role
   * @param userdata
   */
  this.joinConference = function (roomId, userId, parentId, role, makeType, userData, auths) {
    this.options.browserType = window.CONFIG.RTC.browserType;
    this.options.auths = auths;
    if ('Browser' == this.options.browserType) {
      webRtcMsgHandler = new WebRtcMsgHandler()

      if(auths && !jQuery.isEmptyObject( auths )){
        if(auths.auths){
          auths = auths.auths
          this.options.auths = auths
        }
        if(!jQuery.isEmptyObject( auths )){
          if(!auths.WhiteBoard){
            webRtcUiKit.options.btnMap.push("whiteboard")
          }
          if(!auths.VirtualBackground){
            webRtcUiKit.options.btnMap.push("virtualBg")
          }
          if(!auths.ServerRecording){
            webRtcUiKit.options.btnMap.push("server-record-btn")
          }
          if(!auths.LocalRecording){
            webRtcUiKit.options.btnMap.push("startRecord")
          }
          if(!auths.DevidedRecording){
            webRtcUiKit.options.btnMap.push("divided-record-btn")
          }
          if(!auths.Sharing){
            webRtcUiKit.options.btnMap.push("share")
          }
          if(!auths.SwitchVoiceVideo){
            webRtcUiKit.options.btnMap.push("change")
          }
          if(!auths.TakePhoto){
            webRtcUiKit.options.btnMap.push("takePhoto")
          }
          if(!auths.VideoHold){
            webRtcUiKit.options.btnMap.push("hold")
          }
        }
      }
      webRtcUiKit.initWebRtcVideoCall(roomId, userId, parentId, role, '1', makeType, userData)
      webRtcUiKit.viewControl()
    } else if ('WPhone' == this.options.browserType) {
      window.onbeforeunload = null;
      var webrtcUrl = window.CONFIG.RTC.command+'://' + window.CONFIG.RTC.webrtcHtml + '?fromUser=' + userId;
      webrtcUrl += '&roomId=' + roomId;
      webrtcUrl += '&makeType=' + makeType;
      webrtcUrl += '&rtcType=' + this.options.rtcType;
      setVideoIframeSrc(encodeURI(webrtcUrl));
      // 重新绑定刷新事件
      try {
        onBeforeUnloadAction()
      }catch (e) {

      }
    }
  },
    /**
     * 加入视频会议
     *
     * @param roomId
     * @param userId
     * @param role
     * @param userdata
     */
    this.initSettingXchat = function () {
        webRtcMsgHandler = new WebRtcMsgHandler()
        this.settingmyjson.mgw = window.CONFIG.RTC.MGW
        this.settingmyjson.msgtype = "video"
        this.settingmyjson.autoleave = 0
        this.settingmyjson.role = "Agent"
        this.settingmyjson.fromuser = new Date().getTime()+"-setting"
        this.settingmyjson.chatroom = "setting-room"
        this.settingmyjson.callback = this.onsettingcallback
        this.settingmyjson.player = document.getElementById('videoPlayer')
        this.settingmyjson.camera = '0'
        this.settingmyjson.recvonly = 0
        this.settingmyjson.maxwidth = '640'
        this.settingmyjson.isagent = 1
        console.info('开始初始化设置 xchat kit',JSON.stringify(this.settingmyjson))
        if (this.settingxchatkit) {
          if (window.CONFIG.debugAll) console.info('xchat已存在，开始销毁')
          this.settingxchatkit.CloseWS()
        }
        this.settingxchatkit = new XChatKit(this.settingmyjson)
        this.settingxchatkit.GetCamerasInfo()
        console.log('------' + new Date() + ', log info [监听初始化参数完毕。。。。。 ]')
    },
    /**
     * 监听
     *
     * @param roomId
     * @param userId
     * @param role
     * @param userdata
     */
    this.requestMonitor = function (fromUser , userId, chatRoom, parentId) {
      roomId = "monitor-room"
      if($("#monitor-room").length>0){
        webRtcUiKit.monitorxchatkit.StartMonitoring(userId)
        var btn = '<button id=\"' + chatRoom + '\" onclick="webRtcUiKit.setCurrentMonitorUser(this)">'+userId+'</button>'
        $("#userlist-box").append(btn)
      }else{
        webRtcMsgHandler = new WebRtcMsgHandler()
        var popUpHtml = '<div id="monitor-room" class="popup-video-screen-div" style="max-width:1500px;width: auto;left: 200px;min-width: 500px;top:50px;z-index: 999">' +
          '<div id="controller-bottom"  class="controller" align="center" style="height: 40px">' +
          '<div class="controller-inner" style="line-height: 30px;background-color: #ffffff;border: 1px solid #000000;height: 40px">' +
          '<div class="row">' +
          '<div class="brage-btn" id="brageBtn" onclick="webRtcUiKit.brage()" style="float: left;">强插</div>' +
          '<div class="brage-btn" id="breakBtn" onclick="webRtcUiKit.break()" style="float: left;"> 强拆</div>' +
          '<div class="brage-btn" id="cancelMonitorBtn" onclick="webRtcUiKit.cancelMonitor()" style="float: left;">取消监听</div>' +
          '<div class="brage-btn" id="changeLayoutBtn" onclick="webRtcUiKit.changeLayout()" style="float: left;" >切换布局</div>' +
          '<div class="brage-btn" onclick="webRtcUiKit.close()" style="float: right;" >关闭窗口</div>' +
          '</div>' +
          '</div>' +
          '</div>' +
          '<div class=\'main-video-box\'>' +
          '<video id=\'videoPlayer\'  poster=\'./avatar.png\' playsinline autoplay muted></video>' +
          '</div>'+
          '<div class=\'userlist-box\' id=\'userlist-box\'>' +
          '<span style="color: #ffffff;z-index: 9999">当前视频：</span><span style="color: #ffffff;z-index: 9999" id=\'current-user\'></span>' +
          '<button id=\"' + chatRoom + '\" onclick="webRtcUiKit.setCurrentMonitorUser(this)">'+userId+'</button>' +
          '</div>'+
          '</div>'
        $("#"+parentId).append(popUpHtml)
        $("#"+parentId).addClass('rtc-wrapper')
        $('#monitor-room').draggable({
          containment: $("#parentId"),
          scroll: false
        })
        this.monitormyjson.mgw = window.CONFIG.RTC.MGW
        this.monitormyjson.msgtype = "video"
        this.monitormyjson.autoleave = 0
        this.monitormyjson.role = "Agent"
        this.monitormyjson.fromuser = fromUser+"-monitor"
        this.monitormyjson.chatroom = "monitorchatRoom"
        this.monitormyjson.callback = this.onmonitorcallback
        this.monitormyjson.player = document.getElementById('videoPlayer')
        this.monitormyjson.camera = '0'
        this.monitormyjson.recvonly = 0
        this.monitormyjson.maxwidth = '640'
        this.monitormyjson.isagent = 1
        console.info('开始初始化监听new xchat kit',JSON.stringify(this.monitormyjson))
        if (this.monitorxchatkit) {
          if (window.CONFIG.debugAll) console.info('xchat已存在，开始销毁')
          this.monitorxchatkit.CloseWS()
        }
        this.monitorxchatkit = new XChatKit(this.monitormyjson)
        this.monitorxchatkit.StartMonitoring(userId)
        console.log('------' + new Date() + ', log info [监听初始化参数完毕。。。。。 ]')
      }
    },

    /**
     * 咨询视频
     *
     * @param roomId
     * @param userId
     * @param role
     * @param userdata
     */
    this.requestConsult = function (fromUser , chatRoom, parentId, msgtype) {
        webRtcMsgHandler = new WebRtcMsgHandler()
        var popUpHtml = '<div id="monitor-room" class="popup-video-screen-div" style="width: auto;left: 200px;min-width: 500px;top:50px;z-index: 999">' +
          '<div id="controller-bottom"  class="controller" align="center" style="height: 40px">' +
          '<div class="controller-inner" style="line-height: 30px;background-color: #ffffff;border: 1px solid #000000;height: 40px">' +
          '<div class="row">' +
          '<div class="brage-btn" onclick="webRtcUiKit.closeConsult()" style="float: right;background-color: darkred" >挂断</div>' +
          '</div>' +
          '</div>' +
          '</div>' +
          '<div class=\'main-video-box\'>' +
          '<video id=\'videoPlayer\'  poster=\'./avatar.png\' playsinline autoplay muted></video>' +
          '</div>'+
          '</div>'
        $("#"+parentId).append(popUpHtml)
        $("#"+parentId).addClass('rtc-wrapper')
        $('#monitor-room').draggable({
          containment: $("#parentId"),
          scroll: false
        })
        var camera = (msgtype == "voice") ? '-1' : '0'
        this.monitormyjson.mgw = window.CONFIG.RTC.MGW
        this.monitormyjson.msgtype = msgtype
        this.monitormyjson.autoleave = 0
        this.monitormyjson.role = "Agent"
        this.monitormyjson.fromuser = fromUser+"-consult"
        this.monitormyjson.chatroom = chatRoom
        this.monitormyjson.callback = this.callback
        this.monitormyjson.player = document.getElementById('videoPlayer')
        this.monitormyjson.camera = camera
        this.monitormyjson.recvonly = 0
        this.monitormyjson.maxwidth = '640'
        this.monitormyjson.isagent = 1
        console.info('开始初始化咨询new xchat kit',JSON.stringify(this.monitormyjson))
        if (this.monitorxchatkit) {
          if (window.CONFIG.debugAll) console.info('xchat已存在，开始销毁')
          this.monitorxchatkit.CloseWS()
        }
        this.monitorxchatkit = new XChatKit(this.monitormyjson)
        this.monitorxchatkit.JoinConference(this.monitormyjson)
        console.log('------' + new Date() + ', log info [咨询初始化参数完毕。。。。。 ]')
    },
    /**
     * 离开视频会议
     *
     * @param userId
     * @param roomId
     */
    this.leaveConferenceForWPhone = function (sessionId,isTransfer,endRole) {
      if(!webRtcUiKit.getmyjson().chatroom){
        return;
      }
      if(sessionId == webRtcUiKit.getmyjson().chatroom){
        var obj = {}
        obj.msgType = "EventSetMyjson"
        obj.myjson = {}
        wphoneSendMsgToChat(obj)
        if(webRtcUiKit.options.wphonePage){
          closeWindow(sessionId,isTransfer)
        }else{
            var obj = {}
            obj.msgType = "EventHangupFromIe"
            obj.sessionId = sessionId
            obj.isTransfer = isTransfer
            sendMsgToChrom(obj)
        }

        webRtcUiKit.setfirstVideoRole('-1')
        webRtcUiKit.joinFlag = false
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom).parent().removeClass('wrapper')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom).remove()
        webRtcUiKit.clearmyjson()
        webRtcUiKit.closeTimer()
      }
    }
    /**
     * 离开视频会议
     *
     * @param userId
     * @param roomId
     */
    this.leaveConference = function (sessionId,isTransfer,endRole) {
      if(!webRtcUiKit.getmyjson().chatroom){
        return;
      }
      if(sessionId == webRtcUiKit.getmyjson().chatroom){
        webRtcUiKit.xchatkit.Destroy(webRtcUiKit.getmyjson())
        webRtcUiKit.xchatkit.CloseWS()
        webRtcUiKit.setfirstVideoRole('-1')
        webRtcUiKit.joinFlag = false
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom).parent().removeClass('wrapper')
        $('#video-screen' + webRtcUiKit.getmyjson().chatroom).remove()
        webRtcUiKit.clearmyjson()
        webRtcUiKit.closeTimer()
      }
    }
  this.chooseLineStyle = function (type,value,id) {
      if(type == "0"){
        webRtcUiKit.myjson.linecolor = value
        $(".linecolor div").hide()
        $("#"+id+" div").show()
        if($("#linewidth1").css("background-color") != "rgb(197, 198, 203)" && $("#linewidth1").css("background-color") != "#C5C6CB"){
          $("#linewidth1").css("background-color",webRtcUiKit.myjson.linecolor)
        }
        if($("#linewidth2").css("background-color") != "rgb(197, 198, 203)" && $("#linewidth2").css("background-color") != "#C5C6CB"){
          $("#linewidth2").css("background-color",webRtcUiKit.myjson.linecolor)
        }
        if($("#linewidth3").css("background-color") != "rgb(197, 198, 203)" && $("#linewidth3").css("background-color") != "#C5C6CB"){
          $("#linewidth3").css("background-color",webRtcUiKit.myjson.linecolor)
        }
      }
      if(type == "1"){
        webRtcUiKit.myjson.linewidth = value
        $(".linewidth").css("background-color", "#C5C6CB")
        $("#"+id).css("background-color", webRtcUiKit.myjson.linecolor)
      }
    webRtcUiKit.myjson.samecolor = 0
    webRtcUiKit.startAssistance(webRtcUiKit.getmyjson())
  }
  this.getParams = function () {
    if(!window.CONFIG.restIps){
        return
    }
    var url = window.CONFIG.restIps+"/v1/console/base/data/v_workbench_video_replay/list"
    console.log("获取视频初始化参数url："+url);
    var data = {
      "columns": [

      ],
      "limit": 50,
      "offset": 0,
      "uuid": ""
    }
    CommonApi.ajaxRequestTimeout = 5000
    CommonApi.ajaxAsync = false
    CommonApi.httpPostRequest(url,data,function (data) {
      console.log("获取视频初始化参数："+JSON.stringify(data));
      if(data.code == 200){
        if(data.status == "success"){
          if(data.data.listData.length>0){
            webRtcUiKit.getParamsSuccessHandler(data.data.listData[0])
          }
        }
      }
    },function () {
      console.log("获取视频初始化参数失败");
    });
  }
  this.getParamsSuccessHandler = function (params){
    if(params.videoLayout){
      if(params.videoLayout == "PIP"){
        window.CONFIG.RTC.pip = 1
      }else{
        window.CONFIG.RTC.pip = 0
      }
    }
    if(params.framerate){
      window.CONFIG.RTC.framerate = params.framerate
    }
    if(params.pattern){
      if(params.pattern == "fluency"){
        window.CONFIG.RTC.portrait = 0
      }
      if(params.pattern == "definition"){
        window.CONFIG.RTC.portrait = 1
      }
    }
    if(params.timestampPosition){
      window.CONFIG.RTC.timestampPosition = ""
    }
    if(params.pictureWatermarkLocation){
      window.CONFIG.RTC.pictureWatermarkLocation = params.pictureWatermarkLocation.replace("-","");
    }
    if(params.pictureWatermark){
      window.CONFIG.RTC.pictureWatermark = CONFIG.fileServer + '/get?filename=' + params.pictureWatermark
    }
    if(params.textWatermarkLocation){
      window.CONFIG.RTC.textWatermarkLocation = params.textWatermarkLocation.replace("-","")
    }
    if(params.textWatermark){
      window.CONFIG.RTC.textWatermark = params.textWatermark
    }
    if(params.videoFileMode){
      window.CONFIG.RTC.videoFileMode = params.videoFileMode
    }
    if(params.coderate){
      window.CONFIG.RTC.maxbps = params.coderate
    }
    if(params.timestamp){
      window.CONFIG.RTC.timestamp = params.timestamp
    }
    if(params.virtualBackground){
      window.CONFIG.RTC.imagemixer = CONFIG.fileServer + '/get?filename=' + params.virtualBackground
    }
    if(params.resolution){
      window.CONFIG.RTC.maxwidth = params.resolution.substring(0,params.resolution.indexOf("*"));
      window.CONFIG.RTC.maxheight = params.resolution.substring(params.resolution.indexOf("*")+1,params.resolution.length);
    }
    if(params.automatic){
      window.CONFIG.RTC.automatic = params.automatic
    }
  }

  this.Map = function () {
    /** 存放键的数组(遍历用到) */
    this.keys = new Array()
    /** 存放数据 */

    this.data = new Object()
    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */

    this.put = function (key, value) {
      if (this.data[key] == null) {
        this.keys.push(key)
      }

      this.data[key] = value
    }

    this.containsKey = function (key) {
      return this.data[key] ? true : false
    }
    /**
     * 获取某键对应的值
     * @param {String} key
     * @return {Object} value
     */


    this.get = function (key) {
      return this.data[key]
    }
    /**
     * 删除一个键值对
     * @param {String} key
     */


    this.remove = function (key) {
      //        this.keys.remove(key);
      //        this.data[key] = null;
      this.data[key] = null
      this.keys.pop(key)
    }
    /**
     * 遍历Map,执行处理函数
     *
     * @param {Function} 回调函数 function(key,value,index){..}
     */


    this.each = function (fn) {
      if (typeof fn != 'function') {
        return
      }

      var len = this.keys.length

      for (var i = 0; i < len; i++) {
        var k = this.keys[i]
        fn(k, this.data[k], i)
      }
    }
    /**
     * 获取键值数组(类似Java的entrySet())
     * @return 键值对象{key,value}的数组
     */


    this.entrys = function () {
      var len = this.keys.length
      var entrys = new Array(len)

      for (var i = 0; i < len; i++) {
        entrys[i] = {
          key: this.keys[i],
          value: this.data[i]
        }
      }

      return entrys
    }
    /**
     * 判断Map是否为空
     */


    this.isEmpty = function () {
      return this.keys.length == 0
    }
    /**
     * 获取键值对数量
     */


    this.size = function () {
      return this.keys.length
    }
    /**
     * 重写toString
     */


    this.toString = function () {
      var s = '{'

      for (var i = 0; i < this.keys.length; i++, s += ',') {
        var k = this.keys[i]
        s += k + '=' + this.data[k]
      }

      s += '}'
      return s
    }
  }

  /**
   * 时间显示格式化
   *
   * @param {int} n - 数据显示格式化
   * @returns {string}
   */


  function two_char (n) {
    return n >= 10 ? n : '0' + n
  }
  this.closePhotoPrew = function () {
    $(".photoPrew").attr('src','').hide();
    $(".photoPrewDiv").hide();
    $(".main-video-box").show();
  }
  this.downloadImg = function(content){
    var content = $(".photoPrew").attr('src');
    let blob = this.base64ToBlob(content); //new Blob([content]);
    this.downloadFile(webRtcUiKit.getmyjson().userdata.otherdn?webRtcUiKit.getmyjson().userdata.otherdn:webRtcUiKit.getmyjson().chatroom+'.png', blob);
    $(".photoPrewDiv").hide();
    $(".main-video-box").show();
  }
  //下载
  this.downloadFile = function(fileName, blob) {
    let aLink = document.createElement('a');
    let evt = document.createEvent("HTMLEvents");
    evt.initEvent("click", true, true);//initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
    aLink.download = fileName;
    aLink.href = URL.createObjectURL(blob);
    aLink.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));//兼容火狐
  }
  //base64转blob
  this.base64ToBlob = function(code) {
    let parts = code.split(';base64,');
    let contentType = parts[0].split(':')[1];
    let raw = window.atob(parts[1]);
    let rawLength = raw.length;

    let uInt8Array = new Uint8Array(rawLength);

    for (let i = 0; i < rawLength; ++i) {
      uInt8Array[i] = raw.charCodeAt(i);
    }
    return new Blob([uInt8Array], {type: contentType});
  }

  // 接收视频拍照结果
  this.reciveTakePhotoResult = function (reciveTakePhotoResult) {
    console.log('视频拍照结果数据',reciveTakePhotoResult)
    var imgres  = reciveTakePhotoResult.data.substring(reciveTakePhotoResult.data.indexOf("base64,")+7,reciveTakePhotoResult.data.length)
    var img = Utils.base64ToBlob(imgres);
    var formData = new FormData();
    formData["enctype"] = "multipart/form-data";
    formData.append('imageName', reciveTakePhotoResult.fromuser +"_" +reciveTakePhotoResult.sessionid);
    formData.append('file', img);
    let url = window.CONFIG.fileServer + '/put?filetype=jpeg&filechannel=h5&tenantid=' + (tenantId || Agent.getCurrentAgent().tenantId)
    var fileUploadRequest = $.ajax({
      url: url,
      type: 'post',
      async: true,
      contentType: false,
      processData: false,
      data: formData,
      dataType: 'text',
      timeout: Settings.ajaxRequestTimeout * 3,
      success: function success(data) {
        if (data && data != 'failure') {
          if (Settings.debugAll) {
            console.log(new Date() + ",log info [文件异步上传成功: " + data + "]");

          }
          // 判断返回的值是否包含code
          if(data.indexOf('code') > -1){
            var fileData = JSON.parse(data);
            if(fileData.code){
              // globalUiKit.notify('', '上传文件失败!');
              console.log('上传文件失败!')
            }
          }else{
            console.log("上传时间")
            console.log(data)
            var resUrl =  window.CONFIG.fileServer + '/get?filename='+data
            console.log("resUrl="+resUrl)
          }
        } else {
          // globalUiKit.notify('', '上传文件失败!');
          console.log('上传文件失败!')
        }

        // 调用影响存储
        var jsonData = [{
          "fileTypeCode":"2601000",
          "fileUrl":data,
          "fileType":"视频拍照"
        }];
        var saveImageData = {
          "fileInfos": JSON.stringify(jsonData),
          "modelName": "视频拍照",
          "modeCode": "VIDEOPHOTO",
          "filePartName": "VIDEOPHOTO",
          "sessionId": reciveTakePhotoResult.sessionid
        };
        ThirdApi.savePhotographicRecord(saveImageData)
      },
      error: function error(XMLHttpRequest, textStatus, errorThrown) {
        if (textStatus != 'timeout') {
          if (Settings.debugAll) console.log(new Date() + "上传文件失败 " + textStatus + " : " + errorThrown);
        }
      },
      complete: function complete(XMLHttpRequest, status) {
        // 请求完成后最终执行参数
        if (Settings.debugAll) {
          console.log(new Date() + "异步上传文件完成 : " + status);
        }

        if ('timeout' === status) {
          // 超时,status还有success,error等值的情况
          fileUploadRequest.abort();
        }
      }
    })
  }
  this.getParams = function () {
    var url = window.CONFIG.restIps+"/v1/console/base/data/v_workbench_video_replay/list"
    console.log("获取视频初始化参数url："+url);
    var data = {
      "columns": [

      ],
      "limit": 50,
      "offset": 0,
      "uuid": ""
    }
    CommonApi.ajaxRequestTimeout = 5000
    CommonApi.ajaxAsync = false
    CommonApi.httpPostRequest(url,data,function (data) {
      console.log("获取视频初始化参数："+JSON.stringify(data));
      if(data.code == 200){
        if(data.status == "success"){
            if(data.data.listData.length>0){
              var params = data.data.listData[0]
              if(params.videoLayout){
                if(params.videoLayout == "PIP"){
                  window.CONFIG.RTC.pip = 1
                }else{
                  window.CONFIG.RTC.pip = 0
                }
              }
              if(params.framerate){
                window.CONFIG.RTC.framerate = params.framerate
              }
              if(params.pattern){
                window.CONFIG.RTC.portrait = 0
                /*if(params.pattern == "fluency"){
                  window.CONFIG.RTC.portrait = 0
                }
                if(params.pattern == "definition"){
                  window.CONFIG.RTC.portrait = 1
                }*/
              }
              if(params.timestampPosition){
                window.CONFIG.RTC.timestampPosition = ""
              }
              if(params.pictureWatermarkLocation){
                window.CONFIG.RTC.pictureWatermarkLocation = params.pictureWatermarkLocation.replace("-","");
              }
              if(params.pictureWatermark){
                window.CONFIG.RTC.pictureWatermark = CONFIG.fileServer + '/get?filename=' + params.pictureWatermark
              }
              if(params.textWatermarkLocation){
                window.CONFIG.RTC.textWatermarkLocation = params.textWatermarkLocation.replace("-","")
              }
              if(params.textWatermark){
                window.CONFIG.RTC.textWatermark = params.textWatermark
              }
              if(params.videoFileMode){
                  window.CONFIG.RTC.videoFileMode = params.videoFileMode
              }
              if(params.coderate){
                window.CONFIG.RTC.maxbps = params.coderate
              }
              if(params.timestamp){
                window.CONFIG.RTC.timestamp = params.timestamp
              }
              if(params.virtualBackground){
                window.CONFIG.RTC.imagemixer = CONFIG.fileServer + '/get?filename=' + params.virtualBackground
              }
              if(params.resolution){
                window.CONFIG.RTC.maxwidth = params.resolution.substring(0,params.resolution.indexOf("*"));
                window.CONFIG.RTC.maxheight = params.resolution.substring(params.resolution.indexOf("*")+1,params.resolution.length);
              }
              if(params.automatic){
                window.CONFIG.RTC.automatic = params.automatic
              }
            }
        }
      }
    },function () {
      console.log("获取视频初始化参数失败");
    });
  }
  this.brage = function () {
    var chatroom = webRtcUiKit.monitorMap[webRtcUiKit.currentChoose.fromuser]
    webRtcUiKit.joinConference(chatroom, Agent.getCurrentAgent().agentId, CONFIG.RTC.parentid, 'Agent', 'video', {channeltype:"webchat",otherdn:webRtcUiKit.currentChoose.fromuser,turn:CONFIG.RTC.turn});
  }
  this.break = function () {
    var fromuser = webRtcUiKit.currentChoose.fromuser
    webRtcUiKit.monitorxchatkit.RequestDisconnect(fromuser)
    $("#"+webRtcUiKit.currentChoose.chatroom).remove()
  }
  this.cancelMonitor = function () {
    var fromuser = webRtcUiKit.currentChoose.fromuser
    webRtcUiKit.monitorxchatkit.StopMonitoring(fromuser)
    $("#"+webRtcUiKit.currentChoose.chatroom).remove()
  }
  this.changeLayout = function () {
    webRtcUiKit.monitorxchatkit.ChangeDrawingMode()
  }
  this.close = function () {
    $("#monitor-room").remove()
  }
  this.closeConsult = function () {
    webRtcUiKit.monitorxchatkit.Destroy(webRtcUiKit.monitormyjson)
    webRtcUiKit.monitorxchatkit.CloseWS()
    $("#monitor-room").remove()
  }
  this.setCurrentMonitorUser = function (obj) {
    webRtcUiKit.monitorMap[$(obj).text()] = $(obj).id
    webRtcUiKit.currentChoose = {"fromuser":$(obj).text(),"chatroom":$(obj).id}
    $("#current-user").text($(obj).text())
  }
}

function closeWindow (sessionId,isTransfer) {
  if (webRtcUiKit.options.browserType && 'WPhone' == webRtcUiKit.options.browserType) {
    if(!isTransfer){
        //表示除了自己之外会议中还有1人
        if(webRtcUiKit.xchatkit.GetTotalPeers() <= 1){
          var msg = {
            fromUser: webRtcUiKit.getmyjson().fromuser,
            roomId: sessionId,
            msgType: 'EventOverWPhone',
            sendFrom: "WPhone"
          }
          xws && xws.send(JSON.stringify(msg))
        }
    }
    setTimeout(function () {
      xws && xws.close()
      xws = null
      window.location.href = window.CONFIG.RTC.command+'://closewindow';
      open(location, '_self').close()
      windowClosed = true
    }, 500)
  } else {
    $('div[name=video-screen]').remove()
  }
  webRtcUiKit.xchatkit.Destroy(webRtcUiKit.getmyjson())
  webRtcUiKit.xchatkit.CloseWS()
}
