// 远程桌面
var phoneSDK = null
/**
 * 电话条UI工具库
 *
 * @param {object} options - UiKit选项参数设置，传递对象为DOM对象
 * @constructor
 */
var PhoneUiKit = function () {



  // 变量替换
  var self = this
  self.options = {
    uimode: 'onpage',
    parentid: "main-container",
    transferparentid:"main-container",
    hidebtns: [],
    tproxyserver: "",
    agentStatusClassArray:["login","logout", "ready", "notready"],
    callStatusClassArray:["btnReleaseCall","btnAnswerCall", "btnHoldCall", "btnRetrieveCall", "btnMuteCall", "btnUnmuteCall","handleOver","emotion", "listen-show", "transfer_consult", "btnTransferIVR", "btnCompleteTransfer", "btnCompleteConference", "btnBargeIn", "btnForceBreak","dialingNum","btnRejectCall","btnCancelMonitoring","btnIntercept","btnYanMiConference"],
    outBoundModeClassArray:["preview","predict"]
  }
  self.second = 0
  self.tt = 0
  self.currenticbcid = "2000100000000"
  self.currentStatus = ""
  self.currentInterval = 0
  self.agent = {}
  self.size = 10
  self.days = 1
  self.loginTimeLong = 0
  self.loginopt = false
  self.transferInfo = {}
  self.currentOutBoundMode = ""
  self.transferInfo = {
    userdata:{},
    otherdn:"",
    transferMode:"agentid"
  },
    self.isMonitor = false

  /**
   * 事件分发器
   * @type {null}
   */
  this.ee = null

  //************************************************************
  //*************************** 公有方法 ************************
  //************************************************************
  /**
   * 初始化按钮事件
   *
   */
  this.init = function (obj) {
    //initsdk
    var options = {
      debug: CONFIG.debug,
      debugAll: false,
      enableAutoLogin :true
    }
    phoneUiKit.options.hidebtns = obj.hidebtns
    if(CONFIG.platform == "genesys"){
      phoneUiKit.options.hidebtns.push("btnMuteCall")
    }
    phoneUiKit.options.parentid = obj.parentid
    phoneUiKit.options.transferparentid = obj.transferparentid
    phoneUiKit.options.uimode = obj.uimode
    phoneUiKit.options.userdata = obj.userdata
    phoneUiKit.options.connecttproxyserver = obj.connecttproxyserver
    phoneUiKit.agent = obj.agent
    phoneUiKit.size = obj.size?obj.size:10
    phoneUiKit.days = obj.days?obj.days:1
    if(obj.connecttproxyserver){
      if(obj.agent.dntype){
        window.CONFIG.dntype = obj.agent.dntype
        if(window.CONFIG.dntype == "softphone"){
          window.CONFIG.answerType = "aphone"
        }
        if(window.CONFIG.dntype == "hardphone"){
          window.CONFIG.answerType = ""
        }
      }
      phoneSDK = new EChat.phone(obj.tproxyserver, options);
      phoneSDK.init([EChatPhoneMsgHandler,CustomizationPhoneMsgHandler]);
      var agent = obj.agent
      phoneSDK.setCurrentAgent(agent,function () {
        if(!phoneSDK.currentAgent.extension){
          return;
        }
        if(CONFIG.phone.autoLogin){
          phoneSDK.connectToTProxy()
        }
      },function () {
          console.log("获取分机号失败")
      });
    }
    phoneUiKit.loginTimeLong = new Date()
/*    if(CONFIG.pageStyle.skin){
      if(CONFIG.pageStyle.skin == "1"){
        CommonApi.includeLinkStyle("css/phonebar1.css")
      }
      if(CONFIG.pageStyle.skin == "2"){
        CommonApi.includeLinkStyle("css/phonebar2.css")
      }
    }else{
      CommonApi.includeLinkStyle("css/phonebar1.css")
    }*/
    //top-l,top-m,top-r,middle-l,middle-m,middle-r,bottom-l,bottom-m,bottom-r
    var styleStr = CONFIG.pageStyle.phoneUiPosition
    var floatDiv = ""
    var callMode = "  <select class= \"tphone-outboundmode\"  onclick=\"phoneUiKit.stopmoveui();\" style=\"width: 70px;float: left;height: 32px;font-size: 12px;border: none;border-radius: 5px\" onchange='phoneUiKit.setOutboundMode()'>\n" +
      "    <option value=\"outline\" selected>外线</option>\n" +
      "    <option value=\"agentid\">工号</option>\n" +
      "    <option value=\"agentdn\">分机号</option>\n" +
      "    <option value=\"videocall\">视频电话</option>\n" +
      "    <option value=\"secondDial\">二次拨号</option>\n" +
      "  </select>"
    var tphoneUI = "<div class=\"radius\" id=\"tphone"+obj.agent.agentId+"\" style=\"cursor: move; position: absolute;"+styleStr+"\">\n" +
/*      "<div style=\"width: 100%;height: 60px;background-color:grey;position: absolute;text-align: center;line-height: 60px;\">\n" +
      "正在签入。。。。\n" +
      "</div>\n" +*/
      "      <div title='最小化' id=\"hide-tphone"+obj.agent.agentId+"\" class='hide-tphone' onclick=\"phoneUiKit.hideTPhone()\" style=\"position: absolute;right: 30px;top: 6px;cursor: pointer\">\n" +
      "        <span class=\"icon iconfont iconjian\" style=\"font-size: 14px;color: #CCCCCC;\"></span>\n" +
      "      </div>\n" +
      "      <div title='设置' id=\"setting-line"+obj.agent.agentId+"\"  class='setting-line' onclick=\"settingUiKit.showSettingPage()\" style=\"position: absolute;right: 10px;top: 6px;cursor: pointer\">\n" +
      "        <span class=\"icon iconfont iconshezhi\" style=\"font-size: 14px;color: #CCCCCC;\"></span>\n" +
      "      </div>\n" +
      "      <div title='通话记录' id=\"btnCalllist"+obj.agent.agentId+"\" class='btnCalllist' onclick=\"phoneUiKit.showCallList()\" style=\"position: absolute;right: 52px;top: 6px;cursor: pointer\">\n" +
      "        <span class=\"icon iconfont icon-history\" style=\"font-size: 14px;color: #CCCCCC;\"></span>\n" +
      "      </div>\n" +
      "    <div id=\"current_status"+obj.agent.agentId+"\" style=\"height: 30px;text-align: left;padding-left: 15px;width: 340px\">\n" +
      "      <div id=\"current_dnis"+obj.agent.agentId+"\" class='current_dnis' style=\"color: #FF3939;font-size: 14px;font-weight:400;float: left;font-family: Microsoft YaHei;margin-right: 10px;display: none\">\n" +
      "        <span id=\"call-dir\" class=\"icon iconfont icon-callout\" style=\"color: red;font-weight: bold;font-size: 16px\"></span>\n" +
      "<span id=\"phone_num"+obj.agent.agentId+"\"></span>\n" +
      "      </div>\n" +
      "      <div id=\"status_duration"+obj.agent.agentId+"\" style=\"color: #FF3939;font-size: 14px;font-weight:400;float: left;font-family: Microsoft YaHei; float: left;\">\n" +
      "        <span class=\"cur_status\">&nbsp;</span><span>:&nbsp;</span><span class=\"duration-time\">00:00:00</span>\n" +
      "      </div>\n" +
      "    </div>\n" +
      "    <div style=\"float: left;height: 20px;margin-top: 5px\">\n" +
      "      <!--坐席状态-->\n" +
      "      <div id=\"login"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('login','enter')\" onmouseleave=\"phoneUiKit.changeCss('login','leave')\" onclick=\"phoneUiKit.login()\" class=\"phoneAction login\" style=\"display: block; font-size: 15px;\">" +
      "        <span class=\"icon iconfont iconSIP-qianru2\">签入</span>\n" +
      "</div>\n" +
      "      <div id=\"logout"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('logout','enter')\" onmouseleave=\"phoneUiKit.changeCss('logout','leave')\" onclick=\"phoneUiKit.logout()\" class=\"phoneAction logout\" style=\"display: none;\">" +
      "        <span class=\"icon iconfont iconSIP-qianchu\">签出</span>\n" +
      "</div>\n" +
      "      <div id=\"ready"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('ready','enter')\" onmouseleave=\"phoneUiKit.changeCss('ready','leave')\" onclick=\"phoneUiKit.ready()\" class=\"phoneAction ready\" style=\"display: none;\">" +
      "        <span class=\"icon iconfont iconSIP-jiuxu\">就绪</span>\n" +
      "</div>\n" +
      "      <div id=\"notready"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('notReady','enter')\" onmouseleave=\"phoneUiKit.changeCss('notReady','leave')\" onclick=\"phoneUiKit.notready()\" class=\"phoneAction notready\" style=\"display: none;\">" +
      "<span class=\"icon iconfont icon-status-notready\">小休</span></div>\n" +
      /*       "      <div id=\"aftercallwork"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('aftercallwork','enter')\" onmouseleave=\"phoneUiKit.changeCss('aftercallwork','leave')\" onclick=\"phoneUiKit.aftercallwork()\" class=\"phoneAction aftercallwork\">" +
           "<span class=\"icon iconfont icon-status-notready\">话后</span></div>\n" +
            "      <div id=\"cancelaftercallwork"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('cancelaftercallwork','enter')\" onmouseleave=\"phoneUiKit.changeCss('cancelaftercallwork','leave')\" onclick=\"phoneUiKit.cancelaftercallwork()\" class=\"phoneAction cancelaftercallwork\">" +
            "<span class=\"icon iconfont icon-status-notready\">取消话后</span></div>\n" +*/
      "      <!--电话状态-->\n" +
      "      <div id=\"btnReleaseCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnReleaseCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnReleaseCall','leave')\" onclick=\"phoneUiKit.releasecall()\" class=\"phoneAction btnReleaseCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-guaji\">挂断</span></div>\n" +
      "      <div id=\"btnRejectCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnRejectCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnRejectCall','leave')\" onclick=\"phoneUiKit.releasecall()\" class=\"phoneAction btnRejectCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-guaji\">挂断</span></div>\n" +
      "      <div id=\"btnAnswerCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnAnswerCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnAnswerCall','leave')\" onclick=\"phoneUiKit.answercall()\" class=\"phoneAction btnAnswerCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-jieting\">接听</span></div>\n" +
      "      <div id=\"handleOver"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('handleOver','enter')\" onmouseleave=\"phoneUiKit.changeCss('handleOver','leave')\" onclick=\"phoneUiKit.ready()\" class=\"phoneAction handleOver\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconhuahouchuli\">处理结束</span></div>\n" +
      "      <div id=\"btnHoldCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnHoldCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnHoldCall','leave')\" onclick=\"phoneUiKit.hold()\" class=\"phoneAction btnHoldCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-baochi\">保持</span></div>\n" +
      "      <div id=\"btnRetrieveCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnRetrieveCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnRetrieveCall','leave')\" onclick=\"phoneUiKit.retrieve()\" class=\"phoneAction btnRetrieveCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconsipquhui\">取回</span></div>\n" +
      "      <div id=\"btnMuteCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnMuteCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnMuteCall','leave')\" onclick=\"phoneUiKit.mute()\" class=\"phoneAction btnMuteCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconshipinchuang-yuyin-guanbi22222221\">静音</span></div>\n" +
      "      <div id=\"btnUnmuteCall"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnUnmuteCall','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnUnmuteCall','leave')\" onclick=\"phoneUiKit.unmute()\" class=\"phoneAction btnUnmuteCall\" style=\"display: none;\">" +
      "<span class=\"icon iconfont icon03shipinchuang-yuyin-kaiqi1\">取消静音</span></div>\n" +
      "      <div id=\"listen-show"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('listen-show','enter')\" onmouseleave=\"phoneUiKit.changeCss('listen-show','leave')\" onclick=\"phoneUiKit.transfer_consult(true)\" class=\"phoneAction listen-show\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-jianting1\">监听</span></div>\n" +
      "      <div id=\"transfer_consult"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('transfer_consult','enter')\" onmouseleave=\"phoneUiKit.changeCss('transfer_consult','leave')\" onclick=\"phoneUiKit.transfer_consult(false)\" class=\"phoneAction transfer_consult\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-zixunqiuzhu\">转接/咨询</span></div>\n" +
      "      <div id=\"btnTransferIVR"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnTransferIVR','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnTransferIVR','leave')\" onclick=\"phoneUiKit.tansferIvr()\" class=\"phoneAction btnTransferIVR\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-zhuanIVR\">转IVR</span></div>\n" +
      "      <div id=\"emotion"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('emotion','enter')\" onmouseleave=\"phoneUiKit.changeCss('emotion','leave')\" onclick=\"phoneUiKit.tansferEmotion()\" class=\"phoneAction emotion\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-manyidu\">转满意度</span></div>\n" +
      "      <div id=\"btnCompleteTransfer"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnCompleteTransfer','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnCompleteTransfer','leave')\" onclick=\"phoneUiKit.completetransfer()\" class=\"phoneAction btnCompleteTransfer\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-zhuanjie1\">转移</span></div>\n" +
      "      <div id=\"btnCompleteConference"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnCompleteConference','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnCompleteConference','leave')\" onclick=\"phoneUiKit.completeconference()\" class=\"phoneAction btnCompleteConference\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-huiyi\">会议</span></div>\n" +
      "      <div id=\"btnYanMiConference"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnYanMiConference','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnYanMiConference','leave')\" onclick=\"phoneUiKit.yanMiconference()\" class=\"phoneAction btnYanMiConference\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-huiyi\">验密</span></div>\n" +
      "      <div id=\"btnBargeIn"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnBargeIn','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnBargeIn','leave')\" onclick=\"phoneUiKit.brigein()\" class=\"phoneAction btnBargeIn\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-qiangcha2\">强插</span></div>\n" +
      "      <div id=\"btnForceBreak"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnForceBreak','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnForceBreak','leave')\" onclick=\"phoneUiKit.forcebreak()\" class=\"phoneAction btnForceBreak\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-qiangcha\">强拆</span></div>\n" +
      "      <div id=\"btnCancelMonitoring"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnCancelMonitoring','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnCancelMonitoring','leave')\" onclick=\"phoneUiKit.cancelmonitoring()\" class=\"phoneAction btnCancelMonitoring\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconSIP-guaji\">取消监听</span></div>\n" +
      "      <div id=\"preview"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('preview','enter')\" onmouseleave=\"phoneUiKit.changeCss('preview','leave')\" onclick=\"phoneUiKit.preview()\" class=\"phoneAction preview\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconyulan1\">预览</span></div>\n" +
      "      <div id=\"predict"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('predict','enter')\" onmouseleave=\"phoneUiKit.changeCss('predict','leave')\" onclick=\"phoneUiKit.predict()\" class=\"phoneAction predict\" style=\"display: none;\">" +
      "<span class=\"icon iconfont iconyuce1\">预测</span></div>\n" +
      // "      <div id=\"btnIntercept"+obj.agent.agentId+"\" onmouseenter=\"phoneUiKit.changeCss('btnIntercept','enter')\" onmouseleave=\"phoneUiKit.changeCss('btnIntercept','leave')\" onclick=\"phoneUiKit.cancelmonitoring()\" class=\"phoneAction btnIntercept\" style=\"display: none;\"><em></em><span>拦截</span></div>\n" +
      "    </div>\n" +
      "    <div class=\"dialingNum\" id=\"dialingNum"+obj.agent.agentId+"\" style=\"display: none;\">\n" +callMode+

      "      <input id=\"callNum"+obj.agent.agentId+"\" onclick=\"this.focus();\">" +
      "<span class=\"icon iconfont iconhujiao\" style=\"color: #CCCCCC;font-size: 18px;line-height: 25px;\"  onclick=\"phoneUiKit.makecall();\"></span>\n" +
      "    </div>\n" +
      "  </div>"
    $("#"+obj.parentid).append(tphoneUI)
    var ringingAudio = "        <audio id=\"audio\" controls=\"controls\" style=\"display: none\">\n" +
      "          <source src=\"ringing/ring.mp3\">\n" +
      "        </audio>";
    $("#"+obj.parentid).append(ringingAudio)
    var tphoneSmall = "  <div style='display: none' ondblclick=\"phoneUiKit.showTPhone();\" class=\"tphoneSmall\" id=\"tphone-small"+obj.agent.agentId+"\">\n" +
      "    <div class=\"cur_status_div\">\n" +
      "      <span class=\"cur_status\">签出</span>\n" +
      "    </div>\n" +
      "    <div class=\"duration-time-div\">\n" +
      "    <span class=\"duration-time\">00:00:00</span>\n" +
      "    </div>\n" +
      "  </div>"
    $("#"+obj.parentid).append(tphoneSmall)
    phoneUiKit.statusChanged("Logout");
    $(".cur_status").html("签出");
    if(!phoneUiKit.options.connecttproxyserver){
      $("#tphone"+obj.agent.agentId).hide()
    }
    webRtcUiKit = new WebRTCUiKit();
    var webRtcMsgHandler = new WebRtcMsgHandler()
    if(obj.uimode == "onpage"){
      document.getElementById("tphone"+obj.agent.agentId).onmouseover = function () {
        CommonApi.moveObj(this)
      }
      document.getElementById("tphone-small"+obj.agent.agentId).onmouseover = function () {
        CommonApi.moveObj(this)
      }
    }
   // CommonApi.logDebugMessage('tphone version 1.0.1 by tianrui');
    //CommonApi.logDebugMessage('tphone version 1.0.2 by tianrui');
    //CommonApi.logDebugMessage('tphone version 1.0.3 by tianrui');
    //CommonApi.logDebugMessage('tphone version 1.0.4 by tianrui');
    CommonApi.logDebugMessage('tphone version 1.0.6 by tianrui');
  }
  this.statusChanged = function(status){
    if (typeof(status) === "undefined") {
      return;
    }
    if(status != phoneUiKit.currentStatus){
      if(status == "Logout" || status == "Ready" || status == "NotReady" ||  status == "Ringing" || status === "Dialing" || status === "Established" || status === "ListenIn" || status === "AfterCallWork"){
        phoneUiKit.timeCount()
        phoneUiKit.currentStatus = status
      }
    }
    if (status === 'Logout') {
      phoneUiKit.changeAgentStatusBtn.call(this, "10000");
      phoneUiKit.changeCallStatusBtn.call(this, "00000000000000000000");
      phoneUiKit.changeOutBoundModeBtn.call(this, "00");
      document.getElementById("dialingNum"+phoneUiKit.agent.agentId).style.display="none";
      $(".cur_status").html("签出");
    } else if (status === 'Login') {
      phoneUiKit.changeAgentStatusBtn.call(this, "01000");
      phoneUiKit.changeCallStatusBtn.call(this, "00000000000000000000");
/*      if(window.CONFIG.phone.outBoundMode == "preview" && window.CONFIG.platform == "freelink"){
        phoneSDK.requestPreviewOutbound()
      }else{
        phoneSDK.requestPredictOutbound()
      }*/
      document.getElementById("dialingNum"+phoneUiKit.agent.agentId).style.display="block"
      $("#current_dnis"+phoneUiKit.agent.agentId).hide()
      $(".cur_status").html("签入")
    } else if (status === 'Ready') {
      phoneUiKit.changeAgentStatusBtn.call(this, "01010");
      phoneUiKit.changeCallStatusBtn.call(this, "00000000100000000000");//监听
      document.getElementById("dialingNum"+phoneUiKit.agent.agentId).style.display="block"
      $("#current_dnis"+phoneUiKit.agent.agentId).hide()
      $(".cur_status").html("就绪")
      if(phoneUiKit.currentOutBoundMode == "Preview"){
        phoneUiKit.changeOutBoundModeBtn.call(this, "01");
      }
      if(phoneUiKit.currentOutBoundMode == "Predict"){
        phoneUiKit.changeOutBoundModeBtn.call(this, "10");
      }
      $(".tphone-outboundmode").empty()
      var options =   "    <option value=\"outline\" selected>外线</option>\n" +
        "    <option value=\"agentid\">工号</option>\n" +
        "    <option value=\"agentdn\">分机号</option>\n" +
        "    <option value=\"videocall\">视频电话</option>\n"
      $(".tphone-outboundmode").append(options)
    }else if (status === 'NotReady') {
      phoneUiKit.changeAgentStatusBtn.call(this, "01100");
      phoneUiKit.changeCallStatusBtn.call(this, "00000000100000000000");//监听
      document.getElementById("dialingNum"+phoneUiKit.agent.agentId).style.display="block"
      $("#current_dnis"+phoneUiKit.agent.agentId).hide()
      $(".cur_status").html("小休")
      if(phoneUiKit.currentOutBoundMode == "Preview"){
        phoneUiKit.changeOutBoundModeBtn.call(this, "01");
      }
      if(phoneUiKit.currentOutBoundMode == "Predict"){
        phoneUiKit.changeOutBoundModeBtn.call(this, "10");
      }
      $(".tphone-outboundmode").empty()
      var options =   "    <option value=\"outline\" selected>外线</option>\n" +
        "    <option value=\"agentid\">工号</option>\n" +
        "    <option value=\"agentdn\">分机号</option>\n" +
        "    <option value=\"videocall\">视频电话</option>\n"
      $(".tphone-outboundmode").append(options)
    } else if (status === 'Ringing') {
      $("#tphone"+phoneUiKit.agent.agentId).show()
      phoneUiKit.changeAgentStatusBtn.call(this, "00000");
      phoneUiKit.changeCallStatusBtn.call(this, "01000000000000001000");
      $("#call-dir").removeClass("icon-callout").addClass("iconhuru-qudao")
      //$("#current_dnis"+phoneUiKit.agent.agentId+" em").css("background-position","0px 30px")
      $(".cur_status").html("来电振铃")
      $("#current_dnis"+phoneUiKit.agent.agentId).show()
      phoneUiKit.changeOutBoundModeBtn.call(this, "00");
      var audioEle = $("#audio")[0];
      audioEle.play();
    } else if (status === 'Dialing') {
      phoneUiKit.changeCallStatusBtn.call(this, "10000000000000000000")
      phoneUiKit.changeAgentStatusBtn.call(this, "00000");
      $(".cur_status").html("拨号中")
      $("#current_dnis"+phoneUiKit.agent.agentId).show()
      $("#call-dir").removeClass("iconhuru-qudao").addClass("icon-callout")
      //$("#current_dnis"+phoneUiKit.agent.agentId+" em").css("background-position","0px 56px")
      $("#tphone"+phoneUiKit.agent.agentId).show()
      phoneUiKit.changeOutBoundModeBtn.call(this, "00");
    } else if (status === 'ConsultDialing') {
      phoneUiKit.changeCallStatusBtn.call(this, "00010000000000000000");
      Agent.getCallInfo().agentstatus == "ConsultDialing"
      $(".cur_status").html("咨询拨号")
    } else if (status === 'Conference') {
      phoneUiKit.changeCallStatusBtn.call(this, "10000000000000000000");
      $(".cur_status").html("通话中")
    } else if (status === 'Consult') {
      phoneUiKit.changeCallStatusBtn.call(this, "10010000000110000000");
      Agent.getCallInfo().agentstatus == "Consult"
      $(".cur_status").html("通话中")
    } else if (status === 'ConferenceDialing') {
      phoneUiKit.changeCallStatusBtn.call(this, "10000000000000000000");
    } else if (status === 'Established') {
      phoneUiKit.changeCallStatusBtn.call(this, "10101001011000000001");
      phoneUiKit.changeAgentStatusBtn.call(this, "00000");
      document.getElementById("callNum"+phoneUiKit.agent.agentId).value=""
      $(".cur_status").html("通话中")
      phoneUiKit.changeOutBoundModeBtn.call(this, "00");
      $(".tphone-outboundmode").empty()
      var options = "<option value=\"secondDial\" selected>二次拨号</option>\n"
      $(".tphone-outboundmode").append(options)
      $("#dialingNum"+phoneUiKit.agent.agentId).show()
      var audioEle = $("#audio")[0];
      audioEle.pause()
    } else if (status === 'Held') {
      phoneUiKit.changeCallStatusBtn.call(this, "00010000000000000000");
      phoneUiKit.changeAgentStatusBtn.call(this, "00000");
      $(".cur_status").html("通话中")
    } else if (status === 'ListenIn') {
      phoneUiKit.changeCallStatusBtn.call(this, "00000000000001100110");
      phoneUiKit.changeAgentStatusBtn.call(this, "00000");
      $("#phone_num"+phoneUiKit.agent.agentId).html(Agent.getCallInfo().callNo)
      $(".cur_status").html("监听中")
    } else if (status === 'AfterCallWork') {
      phoneUiKit.changeCallStatusBtn.call(this, "00000000100000000000");//监听
      if(window.CONFIG.platform == "huawei"){
        phoneUiKit.changeCallStatusBtn.call(this, "00000010100000000000");
      }
      phoneUiKit.changeAgentStatusBtn.call(this, "01100");
      document.getElementById("dialingNum"+phoneUiKit.agent.agentId).style.display="block"
      $("#phone_num"+phoneUiKit.agent.agentId).html("")
      $(".cur_status").html("话后")
      $("#current_dnis"+phoneUiKit.agent.agentId).hide()
      document.getElementById("callNum"+phoneUiKit.agent.agentId).value=""
      if(!phoneUiKit.options.connecttproxyserver){
        $("#tphone"+phoneUiKit.agent.agentId).hide()
        $("#tphone-small"+phoneUiKit.agent.agentId).hide()
      }
      if(phoneUiKit.currentOutBoundMode == "Preview"){
        phoneUiKit.changeOutBoundModeBtn.call(this, "01");
      }
      if(phoneUiKit.currentOutBoundMode == "Predict"){
        phoneUiKit.changeOutBoundModeBtn.call(this, "10");
      }
      $(".tphone-outboundmode").empty()
      var options =   "    <option value=\"outline\" selected>外线</option>\n" +
        "    <option value=\"agentid\">工号</option>\n" +
        "    <option value=\"agentdn\">分机号</option>\n" +
        "    <option value=\"videocall\">视频电话</option>\n"
      $(".tphone-outboundmode").append(options)
      var audioEle = $("#audio")[0];
      audioEle.pause()
    } else if (status === 'Disconnect') {
    } else if (status === 'Muting') {
      phoneUiKit.changeCallStatusBtn.call(this, "10000100000000000000");
      phoneUiKit.changeAgentStatusBtn.call(this, "00000");
      $(".cur_status").html("通话中")
    }else if(status === 'Preview'){
      phoneUiKit.changeOutBoundModeBtn.call(this, "01");
      phoneUiKit.currentOutBoundMode = 'Preview'
    }
    else if(status === 'Predict'){
      phoneUiKit.changeOutBoundModeBtn.call(this, "10");
      phoneUiKit.currentOutBoundMode = 'Predict'
    }
    phoneUiKit.viewControl();
  }
  this.changeStatusAndDuration = function (status) {

  }
  this.changeAgentStatusBtn = function (sStatusArray) {
    sStatusArray = sStatusArray.split("");
    for (var i = 0; i < sStatusArray.length; i++) {
      var active = sStatusArray[i] != "0";
      var phone_btn = document.getElementById(phoneUiKit.options.agentStatusClassArray[i]+phoneUiKit.agent.agentId)
      if(phone_btn){
        if (active) {
          phone_btn.style.display = "block"
        } else {
          phone_btn.style.display = "none"
        }
      }
    }
  }
  this.changeCallStatusBtn = function (sStatusArray) {
    sStatusArray = sStatusArray.split("");
    for (var i = 0; i < sStatusArray.length; i++) {
      var active = sStatusArray[i] !== "0";
      var phone_btn = document.getElementById(phoneUiKit.options.callStatusClassArray[i]+phoneUiKit.agent.agentId)
      if(phone_btn){
        if (active) {
          phone_btn.style.display = "block"
        } else {
          phone_btn.style.display = "none"
        }
      }
    }
  }
  this.changeOutBoundModeBtn = function (sStatusArray) {
    sStatusArray = sStatusArray.split("");
    for (var i = 0; i < sStatusArray.length; i++) {
      var active = sStatusArray[i] !== "0";
      var phone_btn = document.getElementById(phoneUiKit.options.outBoundModeClassArray[i]+phoneUiKit.agent.agentId)
      if(phone_btn){
        if (active) {
          phone_btn.style.display = "block"
        } else {
          phone_btn.style.display = "none"
        }
      }
    }
  }
  this.changeCss = function(objid,flag){
    if(document.getElementById(objid+phoneUiKit.agent.agentId)){
      if(flag == "enter"){
        //document.getElementById(objid+phoneUiKit.agent.agentId).style.color = "red";
        document.getElementById(objid+phoneUiKit.agent.agentId).style.fontSize = "16px"
      }
      if(flag == "leave"){
        document.getElementById(objid+phoneUiKit.agent.agentId).style.color = "#0C85E2";
        if(CONFIG.pageStyle.skin == "2"){
          document.getElementById(objid+phoneUiKit.agent.agentId).style.color = "#5D86BD";
        }
        document.getElementById(objid+phoneUiKit.agent.agentId).style.fontSize = "15px"
      }
    }
  }
  this.logout = function () {
    phoneSDK.requestLogout();
  }
  this.login = function () {
    phoneSDK.connectToTProxy()
  }
  this.ready = function () {
    phoneSDK.requestReady();
  }
  this.notready = function () {
    phoneSDK.requestNotReady(CONFIG.phone.notReadyReason);
  }
  this.aftercallwork = function () {
    phoneSDK.requestAgentAfterCallWork('200');
  }
  this.cancelaftercallwork = function () {
    phoneSDK.requestCancelAfterCallWork();
  }
  this.answercall = function () {
    if(window.CONFIG.dntype == "xphone"){
      var joinConference = {
        "requestid": "RequestJoinConference",
        "conferenceName":"conf_"+Agent.getCallInfo().callId,
        "conferenceId":Agent.getCallInfo().callId,
        "fsAddress":phoneSDK.currentAgent.sipServerInfo,
        "agentid": phoneSDK.currentAgent.agentId,
        "tenantid": phoneSDK.currentAgent.tenantId,
        "recording": "true"
      }
      phonekit.AgoraPhone.sendMsgToAgora(JSON.stringify(joinConference));
    }
    else if(window.CONFIG.dntype == 'webrtc'){
        if(Agent.getCallInfo().userdata.mgwUrl){
          window.CONFIG.RTC.MGW = Agent.getCallInfo().userdata.mgwUrl
        }
        webRtcUiKit.joinConference('conf_'+Agent.getCallInfo().callId, phoneSDK.currentAgent.agentId+"_Agent", CONFIG.RTC.parentid, 'Agent', 'voice', {
          channeltype:'call',
          otherdn:Agent.getCallInfo().callNo,
          tenantid:phoneSDK.currentAgent.tenantId,
          variable_channel_name:Agent.getCallInfo().userdata.variable_channel_name?Agent.getCallInfo().userdata.variable_channel_name:""
        },{auths:{}});
    }else{
      phoneSDK.requestAnswerCall(Agent.getCallInfo().callId)
    }
  }
  this.releasecall = function () {
    if(CONFIG.platform == "genesys"){
      phoneSDK.requestReleaseCall()
    }else{
      phoneSDK.requestReleaseCall(Agent.getCallInfo().callId)
      if(CONFIG.dntype == "xphone"){
        var releaseJson = {
          "requestid": "RequestReleaseCall",
          "conferenceName":"conf_"+Agent.getCallInfo().callId,
          "fsAddress":phoneSDK.currentAgent.sipServerInfo,
          "agentid": phoneSDK.currentAgent.agentId,
          "thisdn": phoneSDK.currentAgent.extension,
          "connid": Agent.getCallInfo().callId,
          "reason": "agent",
          "tenantid": phoneSDK.currentAgent.tenantId
        }
       // phonekit.AgoraPhone.sendMsgToAgora(JSON.stringify(releaseJson));
      }
    }
  }
  this.rejectcall = function () {
    phoneSDK.requestRejectCall(Agent.getCallInfo().callNo,Agent.getCallInfo().callId);
  }
  this.makecall = function (dialNumber,userdata) {
    if(!dialNumber){
      dialNumber =  document.getElementById("callNum"+phoneUiKit.agent.agentId).value
    }
    if($(".cur_status").html() == "通话中"){
      phoneSDK.requestSendDtmf(dialNumber,Agent.getCallInfo().callId);
      return;
    }
    if(!userdata){
      userdata = new Object();
    }
    if(!userdata.outBoundType){
      userdata.outBoundType = CONFIG.LINEINFO.outBoundType
    }
      var outboundmode = $(".tphone-outboundmode").val()
      if(outboundmode == "agentid"){
        userdata.type = "0"
        phoneSDK.makeCall(dialNumber,'','','','',userdata);
        return
      }
      if(outboundmode == "agentdn"){
        userdata.type = "0"
        phoneSDK.makeCall('',dialNumber,'','','',userdata);
        return
      }
      if(outboundmode == "outline"){
        userdata.type = "1"
      }
      if(outboundmode == "videocall"){
        userdata.type = "1"
        userdata.enablevideo = "true"
        userdata.dntype = "webrtc"
      }
      if(CONFIG.LINEINFO.outBoundType != "0"){
        //phoneUiKit.getPhoneAttribution(dialNumber,userdata)
        phoneUiKit.getPhoneAttribution(dialNumber,userdata,function (callNum,userData) {
          phoneSDK.makeCall('',callNum,'','','',userData);
          $("#phone_num"+phoneUiKit.agent.agentId).html(dialNumber)
        },function (msg) {
          console.log(msg)
        })
      }else{
        userdata.type = "1";
        phoneSDK.makeCall('',dialNumber,'','','',userdata);
        $("#phone_num"+phoneUiKit.agent.agentId).html(dialNumber)
      }

  }
  this.getMakecallPrefix = function (outBoundType,userData) {
    var prefix = ""
    if(outBoundType == "1" || outBoundType == "3"){
      prefix = CONFIG.LINEINFO.makecallPrefix
    }
    else if(outBoundType == "2" || outBoundType == "4"){
      var lineinfo = settingUiKit.getLineInfo()
      if(lineinfo){
        if(lineinfo.prefix){
          prefix = lineinfo.prefix
        }
      }
    }
    return prefix;
  }
  this.getPhoneAttribution = function (dialNumber,userData,successHandler,errorHanlder) {
    var callNum = dialNumber
    if(userData && userData.type == 0){
      if(userdata.outboundMode == "agentid"){
        userData.originalDialNumber = Agent.getCurrentAgent().agentId
      }
      if(userdata.outboundMode == "agentdn"){
        userData.originalDialNumber = Agent.getCurrentAgent().extension
      }
      successHandler(callNum,userData)
      return
    }
    var prefix = window.CONFIG.LINEINFO.makecallPrefix
      if(!userData){
        userData = new Object()
      }
      var lineinfo = {}
      if(userData.outBoundType == "2" || userData.outBoundType == "2"){
        lineinfo = settingUiKit.getLineInfo()
      }
      if(lineinfo){
        if(lineinfo.prefix){
          prefix = lineinfo.prefix
        }
        userData.displayNum = lineinfo.appearance
        userData.vendor = lineinfo.vendor
        userData.callPrefix = lineinfo.prefix
      }
    if (dialNumber == '') {
      return;
    }
    //12位不以0打头的号码直接呼叫
    if(dialNumber.length == 12 && dialNumber.indexOf("0") !=0){
      successHandler(prefix+callNum,userData)
      return
    }
    //11位不以0或1打头的提示号码错误并呼叫
    if(dialNumber.length == 11 && dialNumber.indexOf("0") !=0 && dialNumber.indexOf("1") !=0){
      successHandler(prefix+callNum,userData)
      errorHanlder("您输入的号码可能不正确，请检查后再次发起呼叫。")
      return
    }
    if(dialNumber.length == 7 || dialNumber.length == 8){
      successHandler(prefix + callNum,userData)
      return
    }
    if(dialNumber.length < 7){
      successHandler(callNum,userData)
      return
    }
    if(dialNumber.length > 12 || dialNumber.length == 9 || dialNumber.length == 10){
      successHandler(prefix + callNum,userData)
      return
    }else{
      //是否本地
      var url = window.CONFIG.restIp + '/v1/customer/phone_attr/' + dialNumber + '?timeStamp=' + new Date().getTime()
      CommonApi.httpGetRequest(url,function (response) {
        if (response.data) {
          var attribution = response.data.attribution;
          if(dialNumber.length == 12 && dialNumber.indexOf("0") == 0){
            if (attribution.indexOf(window.CONFIG.LINEINFO.attribution) > -1) { //本地 prefix+（原始号码-区号）
              callNum = prefix + dialNumber.substring(response.data.areaCode.length, dialNumber.length)
              successHandler(callNum,userData)
            } else { //外地 prefix+原始号码
              callNum = prefix + dialNumber
              successHandler(callNum,userData)
            }
          }
          if(dialNumber.length == 11 && dialNumber.indexOf("1") == 0){
            if (attribution.indexOf(window.CONFIG.LINEINFO.attribution) > -1) { //本地 prefix+（原始号码-区号）
              callNum = prefix + dialNumber
              successHandler(callNum,userData)
            } else { //外地 prefix+原始号码
              callNum = prefix + CONFIG.LINEINFO.outOfTownPrefix + dialNumber
              successHandler(callNum,userData)
            }
          }
          if(dialNumber.length == 11 && dialNumber.indexOf("0") == 0){
            if (attribution.indexOf(window.CONFIG.LINEINFO.attribution) > -1) { //本地 prefix+（原始号码-区号）
              callNum = prefix + dialNumber.substring(response.data.areaCode.length, dialNumber.length)
              successHandler(callNum,userData)
            } else { //外地 prefix+原始号码
              callNum = prefix + dialNumber
              successHandler(callNum,userData)
            }
          }
          if(dialNumber.length == 11 && dialNumber.indexOf("0") != 0 && dialNumber.indexOf("1") != 0){
            successHandler(callNum,userData)
          }
        }else{
          callNum = prefix + dialNumber
          successHandler(callNum,userData)
          errorHanlder("该号码归属地未知，外地手机请在号码前加0，外地座机请用区号+座机号码。")
        }
      },function () {
        callNum = prefix + dialNumber
        successHandler(callNum,userData)
        errorHanlder("该号码归属地未知，外地手机请在号码前加0，外地座机请用区号+座机号码。")
      })
    }
  }
  this.transfer_consult = function (isMonitor) {
    phoneUiKit.isMonitor = isMonitor
    //弹出面板
    var transferMode = "  <select class= \"dial-plat-outboundmode\" style=\"width: 60px;float: left;height: 32px;font-size: 12px;border: none;border-radius: 5px\" onchange='phoneUiKit.setTransfertransferMode()'>\n" +
      "    <option value=\"agentgroup\">座席组</option>\n" +
      "    <option value=\"agentid\">工号</option>\n" +
      "    <option value=\"agentdn\">分机号</option>\n" +
      "  </select>"
    var transferPanel = "  <div  class=\"through-dial-page-main\">\n" +
      "    <div class=\"dial-plate\" style=\"float: left\">\n" +
      "      <div class=\"dial-plate-text-div\" style=\"background-color:#D2DEEAFF\">\n" +transferMode+
      "        <input type=\"text\" class=\"dial-plate-text\" style=\"float: left\" onclick=\"this.focus();\"  oninput=\"phoneUiKit.changeCurrentNum(this);\">\n" +
      "        <em onclick=\"phoneUiKit.addNum('-1');\" style=\"float: left\"></em>\n" +
      "      </div>\n" +
      "      <div style=\"height: 100%;width: 100%;\">\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('1');\">1</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('2');\">2</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('3');\">3</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('4');\">4</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('5');\">5</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('6');\">6</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('7');\">7</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('8');\">8</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('9');\">9</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('*');\">*</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('0');\">0</div>\n" +
      "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('#');\">#</div>\n" +
      "      </div>\n" +
      "      <div style=\"height: 100%;width: 100%;\">\n" +
      "        <div class=\"col-xs-12 dial-function-button\"  onclick=\"phoneUiKit.transferOpt('transfer');\">转接</div>\n" +
      "        <div class=\"col-xs-12 dial-function-button\" onclick=\"phoneUiKit.transferOpt('conference');\">会议</div>\n" +
      "        <div class=\"col-xs-12 dial-function-button\" onclick=\"phoneUiKit.transferOpt('consult');\">咨询</div>\n" +
      "        <div class=\"col-xs-12 dial-function-button dial-cancel-button\" onclick=\"phoneUiKit.transferOpt('cancel');\">取消</div>\n" +
      "      </div>\n" +
      "  </div>\n" +
      "    <div class=\"through-type-choose\"  style=\"float: left;width:200px\">\n" +
      "      <div class=\"through-dial-tab-nav\">" +
      "         <div class= \"choose-nav\" style=\"width:200px\"> 座席组 </div>" +
/*      "         <div class= \"choose-nav\" style=\"display: none\"> 求助方 </div>" +*/
      "</div>\n" +
      "      <div class=\"through-dial-tab-content tab-content\">\n" +
      "        <div class=\"groups\" tabindex=\"5\" data=\"\" class=\"tab-pane fade active in through-dial-content\" style=\"overflow-y: auto; outline: none;\">\n" +
      "        </div>\n" +
      "      </div>\n" +
      "    </div>\n" +
      "    <div class=\"through-agent-choose\"  style=\"float: left\">\n" +
      "      <div class=\"through-dial-tab-nav\">\n" +
      "        <div class= \"choose-nav\" style=\"border-right: 1px solid #B0C0D0FF;\">工号</div>\n" +
      "        <div class= \"choose-nav\" style=\"border-right: 1px solid #B0C0D0FF;\">分机</div>\n" +
      "        <div class= \"choose-nav\">座席姓名</div>\n" +
      "      </div>\n" +
      "      <div tabindex=\"6\" class=\"through-dial-tab-content through-dial-content tphoneagents\" style=\"overflow-y: auto; outline: none;\">\n" +
      "        <div class=\"through-dial-content-div\" onclick=\"this.setCurrentNum(this)\">\n" +
      "        </div>\n" +
      "      </div>\n" +
      "    </div>\n" +
      "  </div>"
    if(phoneUiKit.isMonitor){
      transferPanel = "  <div  class=\"through-dial-page-main\" style=\"margin-left: -195px; margin-top: 50px;border: 2px solid #ababab;\">\n" +
        "    <div class=\"dial-plate\" style=\"float: left\">\n" +
        "      <div class=\"dial-plate-text-div\" style=\"background-color:#a3a3a3\">\n" +
        "        <input type=\"text\" class=\"dial-plate-text\" style=\"float: left;width: 140px\" onclick=\"this.focus();\" onchange=\"phoneUiKit.changeCurrentNum(this);\">\n" +
        "        <em onclick=\"phoneUiKit.addNum('-1');\" style=\"float: left\"></em>\n" +
        "      </div>\n" +
        "      <div style=\"height: 100%;width: 100%;\">\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('1');\">1</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('2');\">2</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('3');\">3</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('4');\">4</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('5');\">5</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('6');\">6</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('7');\">7</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('8');\">8</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('9');\">9</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('*');\">*</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('0');\">0</div>\n" +
        "        <div class=\"col-xs-12 dial-num-button\"  onclick=\"phoneUiKit.addNum('#');\">#</div>\n" +
        "      </div>\n" +
        "      <div style=\"height: 100%;width: 100%;\">\n" +
        "        <div class=\"col-xs-12 dial-function-button\"  onclick=\"phoneUiKit.transferOpt('monitor');\">监听</div>\n" +
        "        <div class=\"col-xs-12 dial-function-button dial-cancel-button\" onclick=\"phoneUiKit.transferOpt('cancel');\">取消</div>\n" +
        "      </div>\n" +
        "  </div>\n" +
        "    <div class=\"through-type-choose\"  style=\"float: left\">\n" +
        "      <ul class=\"through-dial-tab-nav\"><li data-v-4611867c=\"\"> 座席组 </li></ul>\n" +
        "      <div class=\"through-dial-tab-content tab-content\">\n" +
        "        <div class=\"groups\" tabindex=\"5\" data=\"\" class=\"tab-pane fade active in through-dial-content\" style=\"overflow-y: auto; outline: none;\">\n" +
              // "          <div class=\"through-dial-content-div\"> 111</div>\n" +
              // "          <div class=\"through-dial-content-div\"> 222</div>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "    </div>\n" +
        "    <div class=\"through-agent-choose\"  style=\"float: left\">\n" +
        "      <ul class=\"through-dial-tab-nav\">\n" +
        "        <div class= \"choose-nav\" style=\"border-right: 1px solid #B0C0D0FF;width: 65px\">工号</div>\n" +
        "        <div class= \"choose-nav\" style=\"border-right: 1px solid #B0C0D0FF;width: 65px\">分机</div>\n" +
        "        <div class= \"choose-nav\">座席姓名</div>\n" +
        "      </ul>\n" +
        "      <div tabindex=\"6\" class=\"through-dial-tab-content through-dial-content tphoneagents\" style=\"overflow-y: auto; outline: none;\">\n" +
        "      </div>\n" +
        "    </div>\n" +
        "  </div>"
    }
    $("#"+phoneUiKit.options.transferparentid).append(transferPanel)
    var requestUrl = window.CONFIG.monitorIp
    var jsonObj = {"requestid":"RequestGetAgentReadyGroups","tenantid":phoneSDK.currentAgent.tenantId, "agentid": phoneSDK.currentAgent.agentId}
    if(CONFIG.phone.transferGroupType == "all"){
      jsonObj = {"requestid":"RequestGetAgentGroups","tenantid":phoneSDK.currentAgent.tenantId, "agentid": phoneSDK.currentAgent.agentId}
    }
    if(phoneUiKit.isMonitor){
      jsonObj = {"requestid":"RequestGetAgentBusyGroups","tenantid":phoneSDK.currentAgent.tenantId, "agentid": phoneSDK.currentAgent.agentId}
    }
    CommonApi.httpPostRequest(requestUrl, jsonObj, function (data) {
      if(data.retCode == 0){
        var groups = data.data
        $(".groups").empty()
        for(var i=0;i<groups.length;i++){
          var groupdesc = groups[i].groupdesc?groups[i].groupdesc:groups[i].groupname;
          var groupitem = "<div class=\"through-dial-content-div\" groupname=\""+groups[i].groupname+"\" routepoint = \""+groups[i].routepoint+"\"  groupdesc=\""+groups[i].groupdesc+"\" ismonitor=\""+phoneUiKit.isMonitor+"\"  onclick=\"phoneUiKit.getAgents(this);\"> "+groupdesc +"</div>";
          $(".groups").append(groupitem)
        }
      }
    }, function () {
      CommonApi.logDebugMessage('获取坐席组信息失败！')
    })
  }
  this.getAgents = function (obj) {
    var requestUrl = window.CONFIG.monitorIp;
    var groupname = $(obj).attr("groupname")
    var routepoint = $(obj).attr("routepoint");
    $(".dial-plate-text").val(groupname)
    phoneUiKit.transferInfo.routepoint = routepoint
    phoneUiKit.transferInfo.otherdn = groupname
    phoneUiKit.transferInfo.transferMode = "agentgroup"
    phoneUiKit.transferInfo.userdata = {"r_flag":"2"}
    $(".dial-plat-outboundmode option[value='agentgroup']").attr("selected","selected");
    var jsonObj = {"requestid":"RequestGetReadyAgents","groupname":groupname,"tenantid":phoneSDK.currentAgent.tenantId, "agentid": phoneSDK.currentAgent.agentId}
    if(phoneUiKit.isMonitor==true){
      jsonObj = {"requestid":"RequestGetBusyAgents","groupname":groupname,"tenantid":phoneSDK.currentAgent.tenantId, "agentid": phoneSDK.currentAgent.agentId}
    }
    CommonApi.httpPostRequest(requestUrl, jsonObj, function (data) {
      if(data.retCode == 0){
        var agentInfos = data.data
        $(".tphoneagents").empty()
        for(var i=0;i<agentInfos.length;i++){
          var agentid = agentInfos[i].agentid;
          var agentname = agentInfos[i].agentname?agentInfos[i].agentname:agentInfos[i].agentid;
          var dn = agentInfos[i].dn;
          var agent = "        <div class=\"through-dial-content-div\" agentid=\""+agentid+"\" dn=\""+dn+"\" onclick=\"phoneUiKit.setCurrentNum(this)\">" +
                      "          <div class=\"currentAgent\">" +agentid+"</div>" +
                      "          <div class=\"currentAgent\">" +dn+"</div>" +
                      "          <div class=\"currentAgent\">" +agentname +"</div>" +
                      "        </div>" ;
          $(".tphoneagents").append(agent)
        }
      }
    }, function () {
      CommonApi.logDebugMessage('获取坐席组信息失败！')
    })
  }
  this.setCurrentNum = function (obj) {
    var dn = $(obj).attr("dn")
    var groupname = $(obj).attr("groupname")
    $(".dial-plate-text").val($(obj).attr("agentid"))
    var agentid = $(".dial-plate-text").val()
    $(".dial-plate-text select").val("agentid")
    phoneUiKit.transferInfo.otherdn = agentid
    phoneUiKit.transferInfo.otherId = agentid
    phoneUiKit.transferInfo.groupname = groupname
    phoneUiKit.transferInfo.dn = dn
    phoneUiKit.transferInfo.transferMode = "agentid"
    $(".dial-plat-outboundmode option[value='agentid']").attr("selected","selected");
    phoneUiKit.transferInfo.userdata = {"r_flag":"1"}
  }
  this.changeCurrentNum = function (obj) {
    var agentid = $(obj).val()
    phoneUiKit.transferInfo.otherdn = agentid
    phoneUiKit.transferInfo.otherId = ""
    phoneUiKit.transferInfo.groupname = ""
    phoneUiKit.transferInfo.dn = ''
  }
  this.setTransfertransferMode = function () {
    phoneUiKit.transferInfo.transferMode = $(".dial-plat-outboundmode").val()
  }
  this.transferOpt = function (opt) {
    var otherAgentId = phoneUiKit.transferInfo.otherId ||""
    var otherDn = phoneUiKit.transferInfo.dn||""
    var userdata = phoneUiKit.transferInfo.userdata;
    if(CONFIG.platform == "genesys" || CONFIG.platform == "huawei"){
      if(phoneUiKit.transferInfo.transferMode == "agentgroup"){
        userdata.groupname = phoneUiKit.transferInfo.routepoint;
        otherDn = phoneUiKit.transferInfo.routepoint
        userdata.r_flag = "2"
        userdata.r_group = phoneUiKit.transferInfo.otherdn
      }else{
        otherDn = phoneUiKit.transferInfo.otherdn
        userdata.r_flag = "1"
      }
    }else{
      if(phoneUiKit.transferInfo.transferMode == "agentid"){
        otherAgentId = phoneUiKit.transferInfo.otherId || phoneUiKit.transferInfo.otherdn
        otherDn = ''
        userdata.r_flag = "1"
      }
      if(phoneUiKit.transferInfo.transferMode == "agentdn"){
        otherAgentId = ''
        otherDn = phoneUiKit.transferInfo.dn || phoneUiKit.transferInfo.otherdn
        userdata.r_flag = "5"
      }
      if(phoneUiKit.transferInfo.transferMode == "agentgroup"){
        otherDn = phoneUiKit.transferInfo.otherdn
        userdata.groupname = phoneUiKit.transferInfo.otherdn;
        userdata.r_flag = "2"
      }
    }
    if(opt == "transfer"){
      userdata.transferType = "1"
      phoneSDK.requestSingleTransfer(otherAgentId,otherDn,'','','',Agent.getCallInfo().callId,userdata)
    }
    if(opt == "consult"){
      userdata.r_agent = otherAgentId
      userdata.r_function = "consult"
      phoneSDK.requestDoubleTransfer(otherAgentId,otherDn, '', '','',Agent.getCallInfo().callId,userdata)
    }
    if(opt == "conference"){
      userdata.con_flag = "0";
      phoneSDK.requestSingleStepConference(otherAgentId,otherDn,'','','',userdata,Agent.getCallInfo().callId);
    }
    if(opt == "monitor"){
      phoneSDK.requestMonitor(otherAgentId,otherDn);
    }
    Agent.getCallInfo().MonitoredDN = otherDn
    Agent.getCallInfo().monitorAgent = otherAgentId
    $(".through-dial-page-main").remove()
  }
  this.addNum = function(num){
    if(num == "-1"){
      $(".dial-plate-text").val($(".dial-plate-text").val()+num)
    }
      $(".dial-plate-text").val($(".dial-plate-text").val()+num)
  }
  this.tansferEmotion = function () {
    var transferNum = CONFIG.phone.emotion;
    var userdata = {};
    userdata.con_flag = "0";
    userdata.r_flag = "3";
    phoneSDK.requestSingleTransfer('',transferNum,'','','',Agent.getCallInfo().callId,userdata)
  }
  this.tansferIvr = function () {
    var transferNum = CONFIG.phone.transferIVR;
    if(CONFIG.platform == 'huawei'){
      phoneSDK.requestTransferIVR(transferNum,'','','',{})
    }else{
      var userdata = {};
      userdata.con_flag = "0";
      userdata.r_flag = "3";
      phoneSDK.requestSingleTransfer('',transferNum,'','','',Agent.getCallInfo().callId,userdata)
    }
  }
  this.singlestepconference = function (con_flag) {
    if(window.CONFIG.platform == "huawei"){
      phoneSDK.requestConferenceIVR(transferNum,'','','',{})
    }else{
      var conferenceNum = $(".transfer-number").val();
      var transferAgent = $(".transfer-agent").val();
      var userdata = {};
      userdata.con_flag = "1";
      userdata.p_verifytype = "02";
      phoneSDK.requestSingleStepConference(transferAgent,conferenceNum,'','','',userdata,Agent.getCallInfo().callId);
    }
  }
  this.initconsult = function () {
    var transferNum = $(".transfer-number").val();
    var transferAgent = $(".transfer-agent").val();
    var userdata = new Object();
    userdata.r_flag = "1";//咨询坐席
    phoneSDK.requestDoubleTransfer(transferAgent, transferNum, '', '','', Agent.getCallInfo().callId,userdata)
  }
  this.completetransfer = function () {
    var connid = Agent.getCallInfo().callId
    if(CONFIG.platform == "genesys"  || CONFIG.platform == "huawei"){
      connid = ""
    }
    phoneSDK.requestCompleteTransfer(Agent.getCallInfo().MonitoredDN, connid);
  }
  this.completeconference = function () {
    var connid = Agent.getCallInfo().callId
    if(CONFIG.platform == "genesys"  || CONFIG.platform == "huawei"){
      connid = ""
    }
    phoneSDK.requestCompleteConference(Agent.getCallInfo().MonitoredDN, connid);
  }
  this.yanMiconference = function () {
    if(CONFIG.platform == "genesys" || CONFIG.platform == "huawei"){
      connid = ""
    }
    var userdata = {};
    userdata.con_flag = "1";
    phoneSDK.requestSingleStepConference('',666666,'','','',userdata,Agent.getCallInfo().callId);
  }
  this.monitor = function () {
    phoneSDK.requestMonitor(Agent.getCallInfo().monitorAgent,Agent.getCallInfo().MonitoredDN);
  }
  this.brigein = function () {
    phoneSDK.requestBargeIn();
  }
  this.forcebreak = function () {
    phoneSDK.requestForceBreak(Agent.getCallInfo().monitorAgent,Agent.getCallInfo().MonitoredDN);
  }
  this.cancelmonitoring = function () {
    if(CONFIG.platform == "genesys" || CONFIG.platform == "huawei"){
      phoneSDK.requestReleaseCall()
    }else{
      phoneSDK.requestReleaseCall(Agent.getCallInfo().callId)
    }
  }
  this.mute = function () {
    phoneSDK.requestMuteCall(Agent.getCallInfo().callId);
  }
  this.unmute = function () {
    phoneSDK.requestUnmuteCall(Agent.getCallInfo().callId);
  }
  this.hold = function () {
    phoneSDK.requestHoldCall(Agent.getCallInfo().callId);
  }
  this.retrieve = function () {
    if((Agent.getCallInfo().agentstatus == "ConsultDialing" || Agent.getCallInfo().agentstatus == "Consult") && (CONFIG.platform == "genesys"  || CONFIG.platform == "huawei")){
      phoneSDK.requestReconnectCall(Agent.getCallInfo().callId)
    }else{
      phoneSDK.requestRetrieveCall(Agent.getCallInfo().callId)
    }
  }
  this.predict = function () {
    phoneSDK.requestPredictOutbound();
  }
  this.preview = function () {
    phoneSDK.requestPreviewOutbound()
  }
  // 隐藏的按钮
  this.viewControl = function () {
      for (var key in phoneUiKit.options.hidebtns) {
        if(document.getElementById(phoneUiKit.options.hidebtns[key]+phoneUiKit.agent.agentId)){
          document.getElementById(phoneUiKit.options.hidebtns[key]+phoneUiKit.agent.agentId).style.display = "none"
        }
      }
  }
  this.getLoginLisense = function (tenantId) {
    phoneSDK.connectToTProxy()
    if(CONFIG.customized.ICBC.loginLisense){
      var restUrl = window.CONFIG.customized.ICBC.customizedServer+"/customizedserver/tenant/check?tenantId="+tenantId
      var data = {
        tenantId:tenantId
      }
      CommonApi.httpPostRequest(restUrl,data,function (response) {
        if(response && response.retCode == 1){
          if(response.data){
            if(response.data.allowNum - response.data.loginNum > 0){
              phoneSDK.connectToTProxy()
            }else{
              alert("已超过允许登录限制，请稍后再登");
            }
          }
        }
      },function (response) {
        CommonApi.logDebugMessage(new Date +'获取登录数失败')
      })
    }
  }
  this.timeCount = function () {
    var date1=new Date();
    clearInterval(this.currentInterval)
    this.currentInterval = setInterval(function () {
      var date2=new Date()
      var date3=date2.getTime()-date1.getTime()
      var days=Math.floor(date3/(24*3600*1000))
      var leave1=date3%(24*3600*1000)
      var hours=Math.floor(leave1/(3600*1000))
      var leave2=leave1%(3600*1000)
      var minutes=Math.floor(leave2/(60*1000))
      var leave3=leave2%(60*1000)
      var seconds=Math.round(leave3/1000)
      if(hours<10){
        hours = "0"+hours
      }
      if(minutes<10){
        minutes = "0"+minutes
      }
      if(seconds<10){
        seconds = "0"+seconds
      }
      $(".duration-time").html(hours+":"+minutes+":"+seconds)
    },1000)
  }
  this.stopCount = function() {
    setTimeout(function () {
      $(".duration-time").html("00:00:00");
    }, 0);
  }
  this.hideTPhone = function () {
    $(".tphoneSmall").show()
    $("#tphone"+phoneUiKit.agent.agentId).hide()
  }
  this.showTPhone = function () {
    $(".tphoneSmall").hide()
    $("#tphone"+phoneUiKit.agent.agentId).show()
  }
  this.stopmoveui = function () {
      document.getElementById("tphone"+phoneUiKit.agent.agentId).onmouseover = null
      document.getElementById("tphone"+phoneUiKit.agent.agentId).onmousedown = null
  }
  this.setOutboundMode = function () {
    setTimeout(function () {
      if(phoneUiKit.options.uimode == "onpage"){
        CommonApi.moveObj(document.getElementById("tphone"+phoneUiKit.agent.agentId))
        CommonApi.moveObj(document.getElementById("tphone"+phoneUiKit.agent.agentId))
      }
    },1000)
  }
  this.showCallList = function () {
    callListUiKit = new CallListUiKit();
    var obj = new Object();
    obj.uimode = phoneUiKit.options.uimode;//inpage表示嵌入到集成页面里，onpage表示悬浮在页表上
    obj.parentid = phoneUiKit.options.parentid;//需要嵌入或悬浮的页面区域id
    obj.agent = phoneUiKit.agent;
    obj.size = phoneUiKit.size;
    obj.days = phoneUiKit.days;
    obj.independent = false;//false表示在电话条内调用，true表示在电话条外调用
    callListUiKit.init(obj)
  }
  this.showSettingPage = function () {
    settingUiKit = new SettingUiKit();
    var obj = new Object();
    obj.uimode = phoneUiKit.options.uimode;//inpage表示嵌入到集成页面里，onpage表示悬浮在页表上
    obj.parentid = phoneUiKit.options.parentid;//需要嵌入或悬浮的页面区域id
    obj.agent = phoneUiKit.agent;
    obj.size = phoneUiKit.size;
    obj.days = phoneUiKit.days;
    obj.independent = false;//false表示在电话条内调用，true表示在电话条外调用
    settingUiKit.init(obj)
  }
}
/* 离开页面时提示 */
window.onbeforeunload = function() {
  if((typeof phonekit == undefined || typeof phonekit == "undefined" || phonekit == null || phonekit == "") && (typeof phoneSDK == undefined || typeof phoneSDK == "undefined" || phoneSDK == null || phoneSDK == "")){
  }else{
    if(phoneSDK){
      phonekit.AgoraPhone.logoutAphone()
      phonekit.AgoraPhone.disconnectFromAgora()
      phoneSDK.requestLogout()
    }
  }
}
