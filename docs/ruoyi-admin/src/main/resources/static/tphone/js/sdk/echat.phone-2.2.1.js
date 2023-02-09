var EChat =
    /******/ (function(modules) { // webpackBootstrap
  /******/ 	// The module cache
  /******/ 	var installedModules = {};
  /******/
  /******/ 	// The require function
  /******/ 	function __webpack_require__(moduleId) {
    /******/
    /******/ 		// Check if module is in cache
    /******/ 		if(installedModules[moduleId]) {
      /******/ 			return installedModules[moduleId].exports;
      /******/ 		}
    /******/ 		// Create a new module (and put it into the cache)
    /******/ 		var module = installedModules[moduleId] = {
      /******/ 			i: moduleId,
      /******/ 			l: false,
      /******/ 			exports: {}
      /******/ 		};
    /******/
    /******/ 		// Execute the module function
    /******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
    /******/
    /******/ 		// Flag the module as loaded
    /******/ 		module.l = true;
    /******/
    /******/ 		// Return the exports of the module
    /******/ 		return module.exports;
    /******/ 	}
  /******/
  /******/
  /******/ 	// expose the modules object (__webpack_modules__)
  /******/ 	__webpack_require__.m = modules;
  /******/
  /******/ 	// expose the module cache
  /******/ 	__webpack_require__.c = installedModules;
  /******/
  /******/ 	// define getter function for harmony exports
  /******/ 	__webpack_require__.d = function(exports, name, getter) {
    /******/ 		if(!__webpack_require__.o(exports, name)) {
      /******/ 			Object.defineProperty(exports, name, {
        /******/ 				configurable: false,
        /******/ 				enumerable: true,
        /******/ 				get: getter
        /******/ 			});
      /******/ 		}
    /******/ 	};
  /******/
  /******/ 	// getDefaultExport function for compatibility with non-harmony modules
  /******/ 	__webpack_require__.n = function(module) {
    /******/ 		var getter = module && module.__esModule ?
        /******/ 			function getDefault() { return module['default']; } :
        /******/ 			function getModuleExports() { return module; };
    /******/ 		__webpack_require__.d(getter, 'a', getter);
    /******/ 		return getter;
    /******/ 	};
  /******/
  /******/ 	// Object.prototype.hasOwnProperty.call
  /******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
  /******/
  /******/ 	// __webpack_public_path__
  /******/ 	__webpack_require__.p = "./";
  /******/
  /******/ 	// Load entry module and return exports
  /******/ 	return __webpack_require__(__webpack_require__.s = 1);
  /******/ })
    /************************************************************************/
    /******/ ([
      /* 0 */
      /***/ (function(module, exports) {

        /**
         * 错误码
         */
        ; (function () {
          /**
           * 给前端发送的座席状态和通话状态改变的事件名
           */
          exports.events = {
            /** 连接AgentProxyServer成功 **/
            EventConnectSuccess: 'EventConnectSuccess',
            EventDisConnectSuccess: 'EventDisConnectSuccess',
            EventConnectError: 'EventConnectError',
            /** 已经连接AgentProxy成功，再次连接 **/
            EventAlreadyConnected: 'EventAlreadyConnected',
            EventReconnect: 'Reconnect',
            EventConnected: 'Connected',
            EventDisconnected: 'Disconnected',
            EventAgentStatusChanged: 'AgentStatusChanged',
            EventAddMedia: 'AddMedia',
            EventRegister: 'EventRegistered',
            EventUnRegister: 'UnRegister',
            EventLogin: 'EventAgentLogin',
            EventLogoin: 'EventAgentLogoin',
            EventLogout: 'EventAgentLogout',
            EventTotalReady: 'EventAgentReady',
            EventTotalNotReady: 'EventAgentNotReady',
            EventRevoked: 'Revoked',
            EventChatRinging: 'ChatRinging',
            EventChatMessage: 'ChatMessage',
            EventChatPartyAdded: 'ChatPartyAdded',
            EventChatPartyLeft: 'ChatPartyLeft',
            EventChatTransfer: 'ChatTransfer',
            EventChatCoach: 'ChatCoach',
            EventChatQuitCoach: 'ChatQuitCoach',
            EventChatConference: 'ChatConference',
            EventChatQuitConference: 'ChatQuitConference',
            EventChatDone: 'ChatDone',
            EventEmailRinging: 'EmailRinging',
            EventEmailMessage: 'EmailMessage',
            EventEmailDone: 'EmailDone',
            EventError: 'Error',
            EventChatNotify: 'ChatNotify',

            PhoneEventConnectSuccess: 'EventConnectSuccess',
            PhoneEventDisConnectSuccess: 'EventDisConnectSuccess',
            PhoneEventConnectError: 'PhoneEventConnectError',
            /** 已经连接TProxy成功，再次连接 **/
            PhoneEventAlreadyConnected: 'PhoneEventAlreadyConnected',
            PhoneEventReleased: 'EventReleased',
            PhoneEventHeld: 'EventHeld',
            PhoneEventRinging: 'EventRinging',
            PhoneEventDialing: 'EventDialing',
            PhoneEventEstablished: 'EventEstablished',
            PhoneEventAbandoned: 'EventAbandoned',
            PhoneEventRevoked: 'EventRevoked',
            PhoneEventRegisterDN: 'RequestRegisterDN',
            PhoneEventAgentLogin: 'RequestAgentLogin',
            PhoneEventAgentLogout: 'RequestAgentLogout',
            PhoneEventAgentReady: 'RequestAgentReady',
            PhoneEventAgentNotReady: 'RequestAgentNotReady',
            PhoneEventAgentAfterCallWork: 'RequestAgentAfterCallWork',
            PhoneEventAgentStatusChanged: 'PhoneEventAgentStatusChanged',
            PhoneEventListenIn: 'RequestListenIn',
            PhoneEventMonitoringNextCall: 'EventMonitoringNextCall',
            PhoneEventMonitoringNextCancelled: 'EventMonitoringNextCancelled',
            PhoneEventAllLinkDisconnected: 'EventAllLinkDisconnected',
            PhoneEventForceBreak: 'RequestForceBreak',
            PhoneEventBargeIn: 'RequestBargeIn',
            PhoneEventAttachedDataChanged: 'EventAttachedDataChanged',
            PhoneEventPartyChanged: 'EventPartyChanged',
            PhoneEventPartyAdded: 'EventPartyAdded',
            PhoneEventPartyDeleted: 'EventPartyDeleted',
            PhoneEventDestinationBusy: 'EventDestinationBusy',
            PhoneEventRetrieved: 'EventRetrieved',
            PhoneEventError: 'EventError',
            PhoneEventAgentAfterCallWork: 'RequestAgentAfterCallWork',
            RequestAgentLoginAlreadyElse: 'RequestAgentLoginAlreadyElse'


          }

          /**
           * 座席当前操作
           */
          exports.actions = {
            ActionInit: 'Init',
            ActionRegister: 'Register',
            ActionLogin: 'Login',
            ActionChatReconnect: 'ChatReconnect',
            ActionLogout: 'Logout',
            ActionReady: 'Ready',
            ActionNotReady: 'NotReady',
            ActionChannelReady: 'ChannelReady',
            ActionChannelNotReady: 'ChannelNotReady',
            ActionAddMedia: 'AddMedia',
            ActionRemoveMedia: 'RemoveMedia',
            ActionChatAccept: 'ChatAccept'
          }

          /**
           * 座席当前状态
           */
          exports.status = {
            AgentStatusInit: 'Init',
            AgentStatusLogin: 'Login',
            AgentStatusLogout: 'Logout',
            AgentStatusReady: 'Ready',
            AgentStatusNotReady: 'NotReady',
            MediaStatusRegister: 'Register',
            MediaStatusNotRegister: 'NotRegister',
            MediaStatusReady: 'Ready',
            MediaStatusNotready: 'NotReady',
            CallStatusAfterCallWork:'AfterCallWork',
            CallStatusEstablished:'Established',
            CallStatusRinging:'Ringing',
            CallStatusDialing:'Dialing',
            CallStatusConsultDialing:'ConsultDialing',
            CallStatusConference:'Conference',
            CallStatusConsult:'Consult',
            CallStatusConferenceDialing:'ConferenceDialing',
            CallStatusHeld:'Held',
            CallStatusListenIn:'ListenIn',
            CallStatusMuting:'Muting',
            CallStatusMonitorRinging:'MonitorRinging',
            CallStatusPredict:'Predict',
            CallStatusPreview:'Preview',

          }
          /**
           * 通话类型
           */
          exports.calltype = {
            CalltypeOutbound: 'Outbound',
            CalltypeInbound: 'Inbound',
            CalltypeOutbound: 'Outbound',
            CalltypeConsult: 'Consult'
          }
        })()


        /***/ }),
      /* 1 */
      /***/ (function(module, exports, __webpack_require__) {

        module.exports = __webpack_require__(2);


        /***/ }),
      /* 2 */
      /***/ (function(module, exports, __webpack_require__) {

        "use strict";

        module.exports = __webpack_require__(3)


        /***/ }),
      /* 3 */
      /***/ (function(module, exports, __webpack_require__) {

        "use strict";


        var _utils = __webpack_require__(4).utils
        var Reconnecting = __webpack_require__(5)
        var Message = __webpack_require__(6).message
        var EventEmitter = __webpack_require__(7)
        var Events = __webpack_require__(0).events
        var Actions = __webpack_require__(0).actions
        var Status = __webpack_require__(0).status
        var Sessions = __webpack_require__(8).sessions
        /*var Queue = require('./lib/queue').Queue*/

        var EChat = window.EChat || {}

        var Phone = function (TProxySrvUrl, opt) {
          var options = opt || {}

          /** 变量替换 */
          var self = this

          /** Web Socket 连接地址 */
          this.url = TProxySrvUrl

          /** Whether this instance should log debug messages. */
          var debug = options.debug || false
          var debugAll = options.debugAll || false

          /** Whether or not the web socket should attempt to connect immediately upon instantiation. default true */
          this.automaticOpen = true || options.automaticOpen

          /** The number of milliseconds to delay before attempting to reconnect. */
          this.reconnectInterval = options.reconnectInterval || 1000

          /** The maximum number of milliseconds to delay a reconnection attempt. */
          this.maxReconnectInterval = options.maxReconnectInterval || 30000

          /** The rate of increase of the reconnect delay. Allows reconnect attempts to back off when problems persist. */
          this.reconnectDecay = options.reconnectDecay || 1.5

          /** The maximum time in milliseconds to wait for a connection to succeed before closing and retrying. */
          this.timeoutInterval = options.timeoutInterval || 2000

          /** The maximum number of reconnection attempts to make. Unlimited if null. */
          this.maxReconnectAttempts = options.maxReconnectAttempts || null

          /** The binary type, possible values 'blob' or 'arraybuffer', default 'blob'. */
          this.binaryType = options.binaryType || 'blob'

          /** Heart beat interval time **/
          this.heartBeatWait = options.heartBeatWait || 3500

          /** Whether cache send msg **/
          this.saveLocal = options.saveLocal || false

          /** 消息ack **/
          this.enableImAck = options.enableImAck || false

          /*  /!** Cached Queue **!/
  this.unSendQueue = new Queue()*/

          /** Event Object **/
          this.events = Events

          /** 当前操作事件列表 **/
          this.actions = Actions

          /** 座席状态列表 **/
          this.status = Status

          /** 当前座席信息 **/
          this.currentAgent = Sessions.currentAgent

          /** 自动重连表识符 **/
          var onceFlag = true
          var autoConnectFlag = true
          //是否触发网络事件
          var emitNetworkEvent = true
          var enableAutoRegister = true
          this.enableAutoLogin = options.enableAutoLogin || false
          /** Heartbeat Scheduled Task **/
          var pingInterval = null

          /** EChat Web Socket Object **/
          var ews = null

          /** Event Emitter 以及消息处理回调 **/
          var ee = new EventEmitter()
          var EChatPhoneHandler = []
          /** ocx模式自动重连标记**/
          var reconnectFlag = false
          var channelid = ''

          /**
           * 电话处理模块初始化
           *
           * @param msgHandler
           */
          this.init = function (msgHandler) {
            EChatPhoneHandler= msgHandler
            initEventEmitter()
            if(self.getBrowserInfo()){
              var fso = new ActiveXObject("Scripting.FileSystemObject");
              var date = new Date();
              var month = date.getMonth()+1
              var filename =date.getFullYear()+"-"+month+"-"+date.getDate();
              if(fso.FolderExists("c:\\tphonelogs")){
                if(!fso.FileExists("c:\\tphonelogs\\"+filename+".log")){
                  fso.CreateTextFile("c:\\tphonelogs\\"+filename+".log");
                }
              }else{
                fso.CreateFolder("c:\\tphonelogs");
                fso.CreateTextFile("c:\\tphonelogs\\"+filename+".log")
              }
            }
          }
          this.initWebrtc = function (userdata) {
            //初始化webrtc电话sdk
            userdata = Object.assign(userdata,{"isagent":0,"autoreg":10,"fsHost":userdata.fsHost});
            if(userdata.mgwUrl){
              window.CONFIG.RTC.MGW = userdata.mgwUrl
            }
            webRtcUiKit.joinConference(userdata.conferenceName, phoneSDK.currentAgent.agentId+"_Agent", CONFIG.RTC.parentid, 'Agent', 'voice', {
              channeltype:'call',
              otherdn:'',
              tenantid:phoneSDK.currentAgent.tenantId
            },{auths:{}});
          }

          /**
           *
           * @param msg
           */
          this.onOfflineMsg = function (msg) {
            logDebugMessage('default on offline msg: ' + msg)
          }
          this.getBrowserInfo = function () {
            var agent = navigator.userAgent.toLowerCase();
            /*var regStr_ie = /msie [\d.]+;/gi;
    var regStr_ff = /firefox\/[\d.]+/gi;
    var regStr_chrome = /chrome\/[\d.]+/gi;
    var regStr_saf = /safari\/[\d.]+/gi;*/
            var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1); //判断是否IE<11浏览器
            /*var isEdge = agent.indexOf("edge") > -1 && !isIE; //判断是否IE的Edge浏览器
    var isIE11 = agent.indexOf('trident') > -1 && agent.indexOf("rv:11.0") > -1;*/
            if (isIE) {
              var reIE = new RegExp("msie (\\d+\\.\\d+);");
              reIE.test(agent);
              var fIEVersion = parseFloat(RegExp["$1"]);
              if (fIEVersion == 5 || fIEVersion == 6 ||fIEVersion == 7 || fIEVersion == 8 || fIEVersion == 9) {
                //var object = '<object name="client" classid="clsid:3A936095-1D1B-419F-B57B-FA98E41128CD" style="height:1pt;width:1pt"  codebase="WebSocketClient/WebSocketClient.cab#version=1,0,0,0"></object>'
                //$(body).append(object)
                return true;
              } else {
                return false;
              }
            } //isIE end
            if (!isIE) {
              return false;
            }
          }
          //ie789事件分发
          this.onOpen = function(){
            if (pingInterval) {
              clearInterval(pingInterval);
            }
            pingInterval = setInterval(function(){
              self.sendMsg('ping');
            }, self.heartBeatWait);
            //自动注册
            if (enableAutoRegister  && self.enableAutoLogin ) {
              var userdata = {};
              userdata.platform = CONFIG.platform;
              userdata.dntype = CONFIG.dntype;
              userdata.channelid = self.channelid;
              if(CONFIG.platform == "freelink" || CONFIG.platform == "freeswitch"){
                userdata.platform = "freeswitch";
              }else{
                userdata.platform = CONFIG.platform;
              }
              self.requestRegister(userdata);
            }

            //网络连接成功事件分发
            if (emitNetworkEvent) {
              ee.emitEvent(Events.PhoneEventConnectSuccess);
            }
            autoConnectFlag = true;
            onceFlag = false;
          };
          this.onClose = function(){
            if(!reconnectFlag){
              setTimeout(function(){
                self.connectToTProxy(true);
              },1000);
            } else {
              if (pingInterval) {
                clearInterval(pingInterval);
              }
              //事件分发
              if (emitNetworkEvent) {
                ee.emitEvent(Events.PhoneEventDisConnectSuccess);
              }
            }

          };
          this.onMessage = function(msg){

            //1、ping pong消息

            if (!msg && msg == 'pong') {
              return;
            }

            //2、正常消息格式解析
            var obj;
            try {
              obj = JSON.parse(msg);

            } catch (ex) {
              logDebugMessage(ex.message,"error");
              return;
            }
            if (!obj) {
              return;
            }

            //3.0 正常信令、消息处理
            onReceiveMsg(obj);
          };
          this.onError = function(){
            //事件分发
            if (emitNetworkEvent) {
              ee.emitEvent(Events.PhoneEventConnectError)
            }
          };

          /**
           * 连接
           *
           * @param flag 已经连接AgentProxy成功，再次连接是否触发连接成功回调 true：触发， false 不触发
           */
          this.connectToTProxy = function (flag) {
            try {
              //当前已经连接web socket，不执行重连操作
              if(self.getBrowserInfo()){
                client.Open(self.url);
                return;
              }
              if (ews != null && 1 == ews.readyState) {
                if (!flag && flag == true) {
                  //连接成功回调
                  ee.emitEvent(Events.PhoneEventAlreadyConnected)
                }
                return
              }

              //建立socket连接
              ews = new Reconnecting(self.url)

              //连接成功
              ews.onopen = function (event) {
                if (pingInterval) {
                  clearInterval(pingInterval)
                }
                pingInterval = setInterval(pingPong, self.heartBeatWait)
                //自动注册
                if (enableAutoRegister  && self.enableAutoLogin) {
                  var userdata = {};
                  userdata.platform = CONFIG.platform;
                  userdata.dntype = CONFIG.dntype;
                  userdata.channelid = self.channelid;
                  if(CONFIG.platform == "freelink" || CONFIG.platform == "freeswitch"){
                    userdata.platform = "freeswitch";
                  }else{
                    userdata.platform = CONFIG.platform;
                  }
                  self.requestRegister(userdata)
                }
                //网络连接成功事件分发
                if (emitNetworkEvent) {
                  ee.emitEvent(Events.PhoneEventConnectSuccess)
                }

                autoConnectFlag = true
                onceFlag = false
                logDebugMessage('连接TProxy Server: ' + self.url + ' 成功!!')
              }

              //收到后台消息回调
              ews.onmessage = function (event) {
                //0、参数格式验证
                if (!event && !event.data) {
                  return
                }

                //1、ping pong消息
                var msg = event.data
                if (!msg && msg == 'pong') {
                  return
                }
                //2、正常消息格式解析
                var obj
                try {
                  obj = JSON.parse(msg)
                  if ('ack' == obj.event) {
                    return
                  }
                } catch (ex) {
                  logDebugMessage(ex.message,"error")
                  return
                }
                if (!obj) {
                  return
                }

                //3.0 正常信令、消息处理
                onReceiveMsg(obj)
              }

              //socket断开处理
              ews.onclose = function () {
                if (pingInterval) {
                  clearInterval(pingInterval)
                }
                //事件分发
                if (emitNetworkEvent) {
                  ee.emitEvent(Events.PhoneEventDisConnectSuccess)
                }
                logDebugMessage('TProxy Server断开连接')
              }

              //socket异常处理
              ews.onerror = function () {
                //事件分发
                if (emitNetworkEvent) {
                  ee.emitEvent(Events.PhoneEventConnectError)
                }
                logDebugMessage('TProxy Server连接异常',"error")
              }
            } catch (ex) {
              logDebugMessage(ex.message + 'exception:' + JSON.stringify(ex),"error")
            }
          }

          /**
           *
           * 断开Agent Proxy的连接
           */
          this.disconnectFromTProxy = function () {
            try {
              if(self.getBrowserInfo()){
                reconnectFlag = true;
                client.Close();
                return;
              }
              if (ews) {
                autoConnectFlag = false
                //预留关闭连接的时间
                setTimeout(ews.close(), 50)
                logDebugMessage('[log info: Msg to TProxy Server = 主动关闭')
              }
            } catch (ex) {
              logDebugMessage('[关闭TProxy Server连接异常：' + ex.message + ']',"error")
            }
          }

          /**
           * 发送消息
           *
           * @param msg
           */
          this.sendMsg = function (msg) {
            if(msg!="ping"){
              logDebugMessage('>>>>send msg: ' + msg)
            }
            if(self.getBrowserInfo()){
              client.Send(msg);
              return;
            }
            if (ews != null) {
              try {
                //如果是心跳信息，不管网络是否断开一直ping
                if (msg && 'ping' == msg) {
                  ews.send(msg)
                } else {
                  //通常该事件比程序检测到未连接要快
                  if(navigator.onLine){
                    if (1 === ews.readyState) {
                      ews.send(msg);
                    }
                  }
                }
              } catch (ex) {
                if (msg && msg !== 'ping') {
                  // self.unSendQueue.push(msg)
                }
                logDebugMessage('[log info: send msg: ' + msg + '，exception：' + ex.message + ']')
              }
            } else {
              logDebugMessage('please connect TProxy Server first!!',"error")
            }
          }

          /**
           * 座席电话渠道签入
           *
           * @param agentId
           * @param thisDN
           */
          this.requestPredictOutbound = function (agentId, thisDN,tenantId) {
            agentId = agentId || self.currentAgent.agentId
            thisDN = thisDN || self.currentAgent.extension

            if (!agentId || !thisDN) {
              logDebugMessage('迁入失败：座席工号或者分机号不能为空',"error")
              return
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPredictOutboundJson(agentId, thisDN,tenantId)
            self.sendMsg(data)
          }


          /**
           * 座席电话渠道签入
           *
           * @param agentId
           * @param thisDN
           */
          this.requestPreviewOutbound = function (agentId, thisDN,tenantId) {
            agentId = agentId || self.currentAgent.agentId
            thisDN = thisDN || self.currentAgent.extension

            if (!agentId || !thisDN) {
              logDebugMessage('迁入失败：座席工号或者分机号不能为空',"error")
              return
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPreviewOutboundJson(agentId, thisDN,tenantId)
            self.sendMsg(data)
          }
          /**
           * 座席注册分机
           *
           * @param agentId
           * @param thisDN
           */
          this.requestRegister = function (userData,agentId, thisDN, tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            var data = Message.getPhoneRegisterJson( agentId, thisDN,tenantId,userData)
            self.sendMsg(data)
          }

          /**
           * 座席电话渠道签入
           *
           * @param agentId
           * @param thisDN
           */
          this.requestLogin = function (agentId, thisDN,tenantId,userData) {
            agentId = agentId || self.currentAgent.agentId
            thisDN = thisDN || self.currentAgent.extension

            if (!agentId || !thisDN) {
              logDebugMessage('迁入失败：座席工号或者分机号不能为空',"error")
              return
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPhoneLoginJson(agentId, thisDN,tenantId,userData)
            self.sendMsg(data)
          }

          /**
           * 座席电话渠道签出
           *
           * @param agentId
           * @param thisDN
           */
          this.requestLogout = function (agentId, thisDN,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPhoneLogoutJson(agentId, thisDN,tenantId)
            self.sendMsg(data)
          }

          /**
           * 座席电话渠道就绪
           *
           * @param agentId
           * @param thisDN
           */
          this.requestReady = function (agentId, thisDN,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPhoneReadyJson(agentId, thisDN,tenantId)
            self.sendMsg(data)
          }

          /**
           * 座席电话渠道未就绪
           *
           * @param agentId 座席ID
           * @param thisDN 分机号
           * @param reason 小休原因
           */
          this.requestNotReady = function (reason, agentId, thisDN,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPhoneNotReadyJson(reason,agentId, thisDN,tenantId)
            self.sendMsg(data)
          }

          /**
           * 座席电话渠道进入话后处理（手动操作）
           *
           * @param agentId 座席ID
           * @param thisDN 分机号
           * @param reason 小休原因
           */
          this.requestAgentAfterCallWork = function (reason, agentId, thisDN,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getPhoneAgentAfterCallWorkJson(reason,agentId, thisDN,tenantId)
            self.sendMsg(data)
          }
          /**
           * 座席电话渠道取消话后处理（手动操作）
           *
           * @param agentId 座席ID
           * @param thisDN 分机号
           * @param reason 小休原因
           */
          this.requestCancelAfterCallWork = function(agentId, thisDN, tenantId){
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            var _jsonMsg = Message.getCancelAfterCallWorkJson(agentId, thisDN, tenantId);
            self.sendMsg(_jsonMsg);
          }

          //华为平台转IVR节点
          this.requestTransferIVR = function(otherDN,agentId,thisDN,devicetype,userdata){
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            var _jsonMsg = Message.getTransferIVRJsonOfHW(agentId, thisDN, otherDN,devicetype,userdata);
            self.sendMsg(_jsonMsg);
          }
          this.requestConferenceIVR = function(otherDN,agentId,thisDN,devicetype,userdata){
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            var _jsonMsg = Message.getConferenceIVR(agentId, thisDN, otherDN,devicetype,userdata);
            self.sendMsg(_jsonMsg);
          }
          /**
           * 座席电话接听
           *
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestAnswerCall = function (connId, agentId, thisDN,tenantId) {
            if (!connId) {
              logDebugMessage('座席电话接听 connId不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            if(CONFIG.answerType == "aphone"){
              logDebugMessage("answer call:RequestAnswerCall|"+connId);
              phonekit.AgoraPhone.sendMsgToAgora("RequestAnswerCall|"+connId);
            } else {
              var data = Message.getAnswerCallJson(agentId, thisDN, connId,tenantId)
              self.sendMsg(data)
            }
          }

          /**
           * 座席拨打电话
           *@param otherDN 外呼工号，freelink支持输入工号外呼
           * @param otherDN 外呼号码
           * @param agentId 座席ID
           * @param thisDN 座席号码
           * @param userData自定义随路数据type为freelink平台区分呼内线还是外线“0”为内线，“1”为外线
           */
          this.makeCall = function (otherID,otherDN, agentId, thisDN,tenantId,userData) {
            if (!otherDN && !otherID) {
              logDebugMessage('外呼的分机号或者号码不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getMakeCallJson(otherID,otherDN, agentId, thisDN,tenantId,userData)
            self.sendMsg(data)
          }

          /**
           * 座席挂断电话
           *
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestReleaseCall = function (connId, agentId, thisDN,tenantId) {
            if (!connId) {
              if(CONFIG.platform == "freeswitch" || CONFIG.platform == "freelink"){
                logDebugMessage('挂机connId不能为空!!',"error")
                return
              }
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            /*    if(window.CONFIG.dntype == "webrtc"){
        webRtcUiKit.leaveConference(connId);
    }*/
            var data = Message.getReleaseCallJson(agentId, thisDN, connId,tenantId)
            self.sendMsg(data)
          }
          /**
           * 座席拒接电话
           *
           * @param connId
           * @param agentId
           * @param thisDN
           * @param otherdn
           */
          this.requestRejectCall = function (otherDn,connId, agentId, thisDN,tenantId) {
            if (!connId) {
              if(CONFIG.platform == "freeswitch" || CONFIG.platform == "freelink"){
                logDebugMessage('拒接connId不能为空!!',"error")
                return
              }
            }
            if(!otherDn){
              logDebugMessage('拒接otherdn不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var data = Message.getRejectCallJson(otherDn, connId, agentId, thisDN , tenantId)
            self.sendMsg(data)
          }

          /**
           * 座席保持电话
           *
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestHoldCall = function (connId, agentId, thisDN,tenantId) {
            if (!connId) {
              logDebugMessage('座席电话接听 connId不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getHoldJson(agentId, thisDN,tenantId,connId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 座席电话取回
           *
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestRetrieveCall = function (connId,agentId, thisDN, tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getRetrieveJson(agentId, thisDN, tenantId,connId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 会议时候，座席电话取回
           *
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestReconnectCall = function (connId,agentId, thisDN, tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getReconnectJson(agentId, thisDN, connId,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 电话单步转
           *
           * @param otherDN
           * @param groupId
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestSingleTransfer = function (otherId,otherDN,agentId,thisDN,tenantId,connId,userData) {
            if (!otherDN && !otherId) {
              logDebugMessage('电话单步转 otherDN/otherId不能都为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getSingleTransferJson(otherId,otherDN,agentId,thisDN,connId,tenantId,userData)
            self.sendMsg(_jsonMsg)
          }

          /**
           * ivr验密电话保持
           *
           * @param agentId
           * @param thisDN
           */
          this.requestHoldJson = function (agentId, thisDN,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getHoldCall(agentId, thisDN,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 屏蔽客户声音
           *
           * @param agentId
           * @param thisDN
           * @param thisDN
           */
          /**取回*/
          this.requestListenReconnect = function (otherDn, agentId, thisDN,tenantId) {
            if (!otherDn) {
              logDebugMessage('屏蔽客户声音 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getListenReconnect(agentId, thisDN, otherDn,tenantId)
            self.sendMsg(_jsonMsg)
          }
          /**屏蔽*/
          this.requestListenDisconnect = function (otherDn, agentId, thisDN,tenantId) {
            if (!otherDn) {
              logDebugMessage('屏蔽客户声音 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getListenDisconnect(agentId, thisDN, otherDn,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 监听单步转2
           *
           * @param otherDN
           * @param groupId
           * @param agentId
           * @param thisDN
           */
          //Location: HF_SIPSwitch_agent1
          //Number: agentId
          this.requestMonitorTransfer = function (otherDN, agentId, thisDN,tenantId) {
            if (!otherDN) {
              logDebugMessage('监听 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getMonitorTransferJson(agentId, thisDN, otherDN,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 转满意度
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           */
          this.requestTransferIVRJson = function (otherDN, agentId, thisDN,connId,tenantId,userData) {
            if (!otherDN) {
              logDebugMessage('转满意度路由点不能为空',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getTransferIVRJson(agentId, thisDN, otherDN,connId,tenantId,userData)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 电话双步转
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestDoubleTransfer = function (otherId,otherDN,agentId,thisDN,tenantId,connId,userdata) {
            if (!otherDN && !otherId) {
              logDebugMessage('电话双步转 otherDN/otherId不能都为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getDoubleTransferJson(agentId,thisDN,otherDN,otherId,tenantId,connId,userdata)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 电话完成转接(完成咨询/求助转接)
           *
           * @param otherDN
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestCompleteTransfer = function (otherDN, connId,thisDN, agentId,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getCompleteTransferJson(agentId,thisDN,otherDN,connId,tenantId )
            self.sendMsg(_jsonMsg)
          }

          /**
           * 转满意度、IVR等
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           */
          this.requestTransferJson = function (routeDN, otherDN, agentId, thisDN,tenantId) {
            if (!routeDN) {
              logDebugMessage('转满意度或IVR路由点不能为空!!',"error")
              return
            }
            if (!otherDN) {
              logDebugMessage('转路由点 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getTransferJson(agentId, thisDN, otherDN, routeDN,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 新老平台转接
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           */
          this.requestNewTransferOldJson = function (routeDN, otherDN, agentId, thisDN,tenantId) {
            if (!routeDN) {
              logDebugMessage('新老平台转接号码不能为空!!',"error")
              return
            }
            if (!otherDN) {
              logDebugMessage('转路由点 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getNewTransferOldJson(
                agentId,
                thisDN,
                otherDN,
                routeDN,
                tenantId
            )
            self.sendMsg(_jsonMsg)
          }

          /**
           * 转任意IVR节点
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestTransferIVRJson = function (
              ivrCode,
              routeDN,
              otherDN,
              agentId,
              thisDN,
              connId,tenantId
          ) {
            if (!ivrCode) {
              logDebugMessage('转IVR任意节点编码不能为空!!',"error")
              return
            }
            if (!routeDN) {
              logDebugMessage('转IVR主节点不能为空!!',"error")
            }
            if (!otherDN) {
              logDebugMessage('转路由点 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getTransferIVRJson(
                agentId,
                thisDN,
                otherDN,
                routeDN,
                ivrCode,
                connId,tenantId
            )
            self.sendMsg(_jsonMsg)
          }

          /**
           * 单步会议
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestSingleStepConference = function (
              otherID,
              otherDN,
              agentId,
              thisDN,
              tenantId,
              userData,
              connId
          ) {
            if (!otherDN && !otherID) {
              logDebugMessage('单步会议 otherDN,otherID不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getSingleStepConferenceJson(
                agentId,
                thisDN,
                otherID,
                otherDN,
                userData,
                connId,tenantId
            )
            self.sendMsg(_jsonMsg)
          }

          /**
           * 三方会议
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestDoubleConference = function (otherDN, connId,agentId, thisDN,tenantId) {
            if (!otherDN) {
              logDebugMessage('三方会议 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getDoubleConferenceJson(
                agentId,
                thisDN,
                otherDN,
                connId,tenantId
            )
            self.sendMsg(_jsonMsg)
          }


          /**
           * 会议完成
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           * @param connId
           */
          this.requestCompleteConference = function (otherDN, connId,agentId, thisDN, tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getCompleteConferenceJson(
                agentId,
                thisDN,
                otherDN,
                connId,tenantId
            )
            self.sendMsg(_jsonMsg)
          }

          /**
           * 监听
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           */
          this.requestMonitor = function (otherID,otherDN, agentId, thisDN,tenantId) {
            if (!otherDN && !otherID) {
              logDebugMessage('监听otherDN/otherID不能都为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getMonitorAgentJson(agentId, thisDN, otherID,otherDN,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 取消监听
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           */
          this.requestCancelMonitor = function (otherDN, connId, agentId, thisDN,tenantId) {
            if (!otherDN) {
              logDebugMessage('取消监听 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getCancelSupervisionMonitoring(
                agentId,
                thisDN,
                otherDN,tenantId,connId
            )
            self.sendMsg(_jsonMsg)
          }

          /**
           * 座席强插
           *
           * @param agentId
           * @param thisDN
           */
          this.requestBargeIn = function (agentId, thisDN,tenantId) {
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getBargeInJson(agentId, thisDN,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 电话强拆
           *
           * @param otherDN
           * @param agentId
           * @param thisDN
           */
          this.requestForceBreak = function (otherID,otherDN, agentId, thisDN,tenantId) {
            if (!otherDN) {
              logDebugMessage('座席强拆 otherDN不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.getForceBreakJson(agentId, thisDN, otherID,otherDN,tenantId)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 更新随路数据
           *
           * @param agentId
           * @param thisDN
           * @Param userData
           */
          this.requestUpdateUserData = function (userData, connid ,agentId, thisDN,tenantId) {
            if (!userData) {
              logDebugMessage('随路数据不能为空!!',"error")
              return
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId
            }
            var _jsonMsg = Message.updateUserDataJson(agentId, thisDN, tenantId,userData,connid)
            self.sendMsg(_jsonMsg)
          }

          /**
           * 二次拨号
           *
           * @param agentId
           * @param thisDN
           * @Param dtmfdigits
           */
          this.requestSendDtmf = function (dtmfdigits, connid, agentId, thisDN,tenantId) {
            if (!dtmfdigits) {
              logDebugMessage('随路数据不能为空!!',"error");
              return;
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId;
            }
            var _jsonMsg = Message.sendDtmfJson(dtmfdigits, connid,agentId, thisDN,tenantId)
            self.sendMsg(_jsonMsg);
          }
          /**
           * 静音电话
           *
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestMuteCall = function (connId, agentId, thisDN, tenantId) {
            if (!connId) {
              logDebugMessage('座席电话静音 connId不能为空!!',"error");
              return;
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId;
            }
            var _jsonMsg = Message.getMuteJson(agentId, thisDN, connId, tenantId)
            self.sendMsg(_jsonMsg);
          }
          /**
           * 静音电话
           *
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestUnmuteCall = function (connId, agentId, thisDN,tenantId) {
            if (!connId) {
              logDebugMessage('座席电话取消静音 connId不能为空!!',"error");
              return;
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId;
            }
            var _jsonMsg = Message.getUnmuteJson(agentId, thisDN, connId,tenantId);
            self.sendMsg(_jsonMsg);
          }
          /**
           * 开始音频传输
           *
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestStartMedia = function (connId, userData, agentId, thisDN,tenantId) {
            if (!connId) {
              logDebugMessage('座席电话取消静音 connId不能为空!!',"error");
              return;
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId;
            }
            var _jsonMsg = Message.getStartMediaJson(agentId, thisDN, connId,tenantId,userData);
            self.sendMsg(_jsonMsg);
          }
          /**
           * 停止音频传输
           *
           * @param connId
           * @param agentId
           * @param thisDN
           */
          this.requestStopMedia = function (connId, userData, agentId, thisDN,tenantId) {
            if (!connId) {
              logDebugMessage('座席电话取消静音 connId不能为空!!',"error");
              return;
            }
            if (!agentId) {
              agentId = self.currentAgent.agentId;
            }
            if (!thisDN) {
              thisDN = self.currentAgent.extension;
            }
            if (!tenantId) {
              tenantId = self.currentAgent.tenantId;
            }
            var _jsonMsg = Message.getStopMediaJson(agentId, thisDN, connId,tenantId,userData);
            self.sendMsg(_jsonMsg);
          }

          /**
           * 获取事件分发器
           *
           * @returns {EventEmitter}
           */
          this.getEventEmitter = function () {
            return ee
          }

          /**
           *
           * 网络就绪状态
           * @returns {*}
           */
          this.getReadyState = function () {
            if (!ews) {
              return -1
            }
            return ews.readyState
          }

          /**
           *
           * @param obj
           *   {
           *       agentId: '',
           *       tenantId: '',
           *       placeId: '',
           *       extension: '',
           *       userId: '',
           *       agentName: '',
           *       agentType: '4',  //座席类型
           *       channelType: '',  //渠道类型
           *       mediaType: '', //媒体类型
           *       captainLevel: -1, //是否班长座席
           *       serviceAbility: -1, //服务能力
           *   }
           */
          this.setCurrentAgent = function (obj, successHandler, errorHandler) {
            self.currentAgent.agentId = obj.agentId
            self.currentAgent.tenantId = obj.tenantId
            self.currentAgent.placeId = obj.placeId
            self.currentAgent.extension = obj.extension
            self.currentAgent.userId = obj.userId
            self.currentAgent.agentName = obj.agentName
            self.currentAgent.agentType = obj.agentType || '4'
            self.currentAgent.channelType = obj.channelType
            self.currentAgent.mediaType = obj.mediaType
            self.currentAgent.captainLevel = obj.captainLevel || -1
            self.currentAgent.serviceAbility = obj.serviceAbility || -1
            self.currentAgent.password = obj.password
            self.currentAgent.sipServerInfo = obj.sipServerInfo
            self.currentAgent.dntype = obj.dntype
            if(obj.dntype == "webrtc"){
              self.currentAgent.extension = obj.agentId
              successHandler()
              return
            }
            if(CONFIG.autoGetDn){
              var url = CONFIG.dnpooServer + '/v1/agent/dnpool/' + obj.tenantId + "/" + obj.agentId;
              logDebugMessage("获取分机号请求"+url);
              jQuery.support.cors = true;
              $.ajax({
                type: "post",
                url: url,
                dataType: 'json',
                async: true,
                timeout: 15000,
                data: {
                  tenantId: obj.tenantId,
                  agentId: obj.agentId
                },
                success: function(data) {
                  logDebugMessage("get dn res："+JSON.stringify(data));
                  if (data.retCode == 1) {
                    if(obj.isIntranet){
                      self.currentAgent.extension = data.data.extension + "@" + data.data.sipInnerServer + ":" + data.data.sipInnerPort;
                      self.currentAgent.password = data.data.password;
                      self.currentAgent.sipServerInfo = data.data.sipInnerServer + ":" + data.data.sipInnerPort;
                    }else{
                      self.currentAgent.extension = data.data.extension + "@" + data.data.sipServer + ":" + data.data.sipPort;
                      self.currentAgent.password = data.data.password;
                      self.currentAgent.sipServerInfo = data.data.sipServer + ":" + data.data.sipPort;
                    }
                    successHandler()
                  }else{
                    errorHandler("未获取到分机号")
                  }
                },
                error: function(data) {
                  errorHandler("获取分机号接口异常")
                  logDebugMessage("get dn error:"+JSON.stringify(data),"error");
                }
              })
            }else{
              successHandler()
            }
          }

          /**
           *
           * @param message
           * @private
           */
          var logDebugMessage = function (message,msgtype) {
            if (debug) {
              if(self.getBrowserInfo()){
                var date = new Date();
                var fso = new ActiveXObject("Scripting.FileSystemObject");
                var folderName = fso.FolderExists("c:\\tphonelogs");
                var date = new Date();
                var month = date.getMonth()+1
                var filename =date.getFullYear()+"-"+month+"-"+date.getDate();
                var file = fso.OpenTextFile("c:\\tphonelogs\\"+filename+".log", 8, true);
                file.WriteLine(new Date +"----->"+message)
                file.Close()
              }else{
                if(msgtype && msgtype == "error"){
                  console.log(
                      '%c' + new Date +"----->" + '[debug] ' + (message || ''),
                      'background-color: red'
                  )
                }else{
                  console.log(
                      '%c' + new Date +"----->" + '[debug] ' + (message || ''),
                      'background-color: #90ff1e'
                  )
                }
              }
            }
          }

          /**
           *
           * 心跳信息处理
           */
          var pingPong = function () {
            if (debugAll) {
              logDebugMessage('ping')
            }
            try {
              self.sendMsg('ping')
            } catch (ex) {
              logDebugMessage('ping exception' + ex.message,"error")
            }
          }

          /**
           * 收到历史消息处理
           *
           * @param msg
           */
          var onReceiveMsg = function (jsonObj) {
            if(jsonObj.ret && jsonObj.ret !=0){
              logDebugMessage('onMsgFromTProxy: ' + JSON.stringify(jsonObj),"error")
            }else{
              logDebugMessage('onMsgFromTProxy: ' + JSON.stringify(jsonObj))
            }

            //收到后台消息
            var agentstatus = jsonObj.agentstatus
            if(agentstatus){
              if(jsonObj.stateid != Events.PhoneEventError){
                ee.emitEvent(Events.PhoneEventAgentStatusChanged, [jsonObj])
                ee.emitEvent(agentstatus, [jsonObj])
              }
            }
            var stateId = jsonObj.stateid
            if (stateId) {
              //随路数据发送变更
              if(Events.PhoneEventAttachedDataChanged == stateId){
                ee.emitEvent(Events.PhoneEventAttachedDataChanged, [jsonObj])
              }
              if(Events.EventRegister == stateId){
                ee.emitEvent(Events.EventRegister, [jsonObj])
              }
              if(Events.PhoneEventReleased == stateId){
                ee.emitEvent(Events.PhoneEventReleased, [jsonObj])
              }
              if(Events.PhoneEventMonitoringNextCall == stateId){
                ee.emitEvent(Events.PhoneEventMonitoringNextCall, [jsonObj])
              }
              if(Events.PhoneEventAbandoned == stateId){
                ee.emitEvent(Events.PhoneEventAbandoned, [jsonObj])
              }
              if (Events.PhoneEventPartyDeleted == stateId) {
                ee.emitEvent(Events.PhoneEventPartyDeleted, [jsonObj])
              }
            }
            /*    var stateId = jsonObj.stateid
    if (stateId) {
      //根据座席状态推送来后，更新电话条按钮
      ee.emitEvent(stateId, [jsonObj])
      ee.emitEvent(Events.PhoneEventAgentStatusChanged, [jsonObj])
    }*/

            //后台请求返回
            var responseID = jsonObj.responseid
            if (responseID) {
              if(0 != jsonObj.ret){
                //注册分机
                if (Events.PhoneEventRegisterDN == responseID) {
                  //分机注册成功
                  ee.emitEvent(Events.PhoneEventError, [jsonObj])
                  logDebugMessage('EventRegisterDN error',"error")
                } else if (Events.PhoneEventAgentLogin == responseID || "EventAgentLogoin" == responseID) {
                  //其他异常情况，抛出异常
                  ee.emitEvent(Events.PhoneEventError, [jsonObj])
                } else if (Events.PhoneEventAgentLogout == responseID) {
                  //座席签出
                  ee.emitEvent(Events.PhoneEventError, [jsonObj])
                } else if (Events.PhoneEventAgentReady == responseID) {
                  //座席就绪
                  ee.emitEvent(Events.PhoneEventError, [jsonObj])
                } else if (Events.PhoneEventAgentNotReady == responseID) {
                  //座席小休
                  ee.emitEvent(Events.PhoneEventError, [jsonObj])
                } else {
                  //Events.PhoneEventAgentNotReady, Events.PhoneEventListenIn， Events.PhoneEventForceBreak， Events.PhoneEventBargeIn
                  ee.emitEvent(responseID, [jsonObj])
                }
              } else {
                if (Events.PhoneEventRegisterDN == responseID) {
                  //分机注册成功
                  if (0 == jsonObj.ret) {
                    for(var p = 0; p < EChatPhoneHandler.length; p++){
                      EChatPhoneHandler[p].registerHandler(jsonObj)
                    }
                    if(jsonObj.channelid){
                      self.channelid = jsonObj.channelid
                    }
                    //EChatPhoneHandler.registerHandler(jsonObj)
                    logDebugMessage('EventRegisterDN success')
                  } else {
                    ee.emitEvent(Events.PhoneEventError, [jsonObj])
                    logDebugMessage('EventRegisterDN error',"error")
                  }
                }
              }
            }
          }

          /**
           * 初始化监听事件
           */
          var initEventEmitter = function () {
            if (ee) {
              //网络事件
              //连接TProxy成功
              ee.addListener(Events.PhoneEventConnectSuccess, function () {
                /*EChatPhoneHandler.networkConnectedSuccess()*/
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].networkConnectedSuccess()
                }
              })

              //断开AgentProxy连接
              ee.addListener(Events.PhoneEventDisConnectSuccess, function () {
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].networkDisConnectedSuccess()
                }
                //EChatPhoneHandler.networkDisConnectedSuccess()
              })
              //AgentProxy连接错误自动重连
              ee.addListener(Events.PhoneEventConnectError, function () {
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].networkConnectedError()
                }
                //EChatPhoneHandler.networkConnectedError()
              })

              //座席状态改变
              ee.addListener(Events.PhoneEventAgentStatusChanged, function (obj) {
                //EChatPhoneHandler.agentStatusChangedHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].agentStatusChangedHandler(obj)
                }
              })

              //座席注册
              // ee.addListener(Events.EventRegister, function (obj) {
              //   for(var p = 0; p < EChatPhoneHandler.length; p++){
              //     EChatPhoneHandler[p].registerHandler(obj)
              //   }
              //   //EChatPhoneHandler.registerHandler(obj)
              // })



              //座席签入
              ee.addListener(Status.AgentStatusLogin, function (obj) {
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].loginHandler(obj)
                }
                //EChatPhoneHandler.loginHandler(obj)
              })
              /*      ee.addListener(Events.EventLogoin, function (obj) {
              EChatPhoneHandler.loginHandler(obj)
            })*/

              //座席签出
              ee.addListener(Status.AgentStatusLogout, function (obj) {
                if (obj.stateid != Events.EventRegister) {
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].logoutHandler(obj)
                  }
                  //EChatPhoneHandler.logoutHandler(obj)
                }
              })
              //座席话后
              ee.addListener(Status.CallStatusAfterCallWork, function (obj) {
                //EChatPhoneHandler.afterCallWorkHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].afterCallWorkHandler(obj)
                }
              })
              //座席小休
              ee.addListener(Status.AgentStatusNotReady, function (obj) {
                //EChatPhoneHandler.notReadyHandler(obj)
                //EChatPhoneHandler.totalReadyOrNotReadyHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].notReadyHandler(obj)
                  EChatPhoneHandler[p].totalReadyOrNotReadyHandler(obj)
                }
              })
              //座席小休
              ee.addListener(Status.AgentStatusReady, function (obj) {
                //EChatPhoneHandler.readyHandler(obj)
                //EChatPhoneHandler.totalReadyOrNotReadyHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].readyHandler(obj)
                  EChatPhoneHandler[p].totalReadyOrNotReadyHandler(obj)
                }
              })
              //振铃事件
              ee.addListener(Status.CallStatusRinging, function (obj) {
                if (obj.stateid == Events.PhoneEventRinging ) {
                  //EChatPhoneHandler.chatRingingHandler(obj)
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].chatRingingHandler(obj)
                  }
                }
                /*if(obj.stateid == Events.PhoneEventRinging){
          EChatPhoneHandler.chatRingingHandler(obj)
        }*/
              })

              /*          ee.addListener(Status.CallStatusMonitorRinging, function (obj) {
            if (obj.stateid == Events.PhoneEventRinging ) {
              for(var p = 0; p < EChatPhoneHandler.length; p++){
                EChatPhoneHandler[p].monitoringNextCallHandler(obj)
              }
            }
            /!*if(obj.stateid == Events.PhoneEventRinging){
          EChatPhoneHandler.chatRingingHandler(obj)
        }*!/
          })*/
              ee.addListener(Status.CallStatusListenIn, function (obj) {
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].monitoringNextCallHandler(obj)
                }
              })
              //外呼事件
              ee.addListener(Status.CallStatusDialing, function (obj) {
                if (obj.stateid == Events.PhoneEventDestinationBusy) {
                  //EChatPhoneHandler.abandonedHandler(obj)
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].abandonedHandler(obj)
                  }
                }
                if (obj.stateid == Events.PhoneEventDialing) {
                  //EChatPhoneHandler.chatDialingHandler(obj)
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].chatDialingHandler(obj)
                  }
                  if(CONFIG.dntype == "webrtc" && (CONFIG.platform == "freelink" || CONFIG.platform == "freeswitch")){
                    var userdata = {};
                    try {
                      userdata = JSON.parse(obj.userdata)[0];
                      //if(userdata.enablevideo && userdata.enablevideo == 'true'){
                      self.initWebrtc(userdata)
                      // }
                    }
                    catch (e) {
                      logDebugMessage('json parse error ' + e,"error")
                    }
                  }

                }
              })

              //咨询外呼
              ee.addListener(Status.CallStatusConsultDialing, function (obj) {
                //EChatPhoneHandler.chatConsultDialingHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].chatConsultDialingHandler(obj)
                }
              })

              //振铃超时待确认信令
              ee.addListener(Events.PhoneEventRevoked, function (obj) {
                //EChatPhoneHandler.revokedHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].revokedHandler(obj)
                }
              })
              //三方成员离开信令
              ee.addListener(Events.PhoneEventPartyDeleted, function (obj) {
                for (var p = 0; p < EChatPhoneHandler.length; p++) {
                  EChatPhoneHandler[p].partyDeletedHandler(obj)
                }
              })
              //会话建立事件
              ee.addListener(Status.CallStatusEstablished, function (obj) {
                if (obj.stateid == Events.EventRegister || obj.stateid == Events.PhoneEventEstablished || obj.stateid == Events.PhoneEventRetrieved){
                  //EChatPhoneHandler.establishedHandler(obj)
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].establishedHandler(obj)
                  }
                }
                // if(obj.stateid == Events.PhoneEventPartyDeleted){
                //   //EChatPhoneHandler.partyDeletedHandler(obj)
                //   for(var p = 0; p < EChatPhoneHandler.length; p++){
                //     EChatPhoneHandler[p].partyDeletedHandler(obj)
                //   }
                // }
                if(obj.stateid == Events.PhoneEventAttachedDataChanged){
                  //EChatPhoneHandler.partyChangedHandler(obj)
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].partyChangedHandler(obj)
                  }
                }
              })

              //弃话待确认信令
              /*      ee.addListener(Events.PhoneEventAbandoned, function (obj) {
        EChatPhoneHandler.abandonedHandler(obj)
      })*/

              //随路数据更新 EventAttachedDataChanged
              ee.addListener(Events.PhoneEventAttachedDataChanged, function (obj) {
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].attachedDataChangedHandler(obj)
                }
              })

              //会话成员改变 PhoneEventPartyChanged
              ee.addListener(Status.CallStatusConference, function (obj) {
                if(obj.stateid == Events.PhoneEventPartyAdded){
                  //EChatPhoneHandler.partyAddedHandler(obj)
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].partyAddedHandler(obj)
                  }
                }
                //EChatPhoneHandler.conferencedHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].conferencedHandler(obj)
                }
              })

              //会话成员加入 PhoneEventPartyAdded
              ee.addListener(Status.CallStatusConsult, function (obj) {
                //EChatPhoneHandler.consultHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].consultHandler(obj)
                }
              })

              //会议成员删除（ivr验密）
              ee.addListener(Status.CallStatusConferenceDialing, function (obj) {
                //EChatPhoneHandler.partyDeletedHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].partyDeletedHandler(obj)
                }
              })

              //挂机
              ee.addListener(Events.PhoneEventReleased, function (obj) {
                //EChatPhoneHandler.releasedHandler(obj)
                if(obj.agentstatus != "Held" && obj.agentstatus != "Dialing" ){
                  for(var p = 0; p < EChatPhoneHandler.length; p++){
                    EChatPhoneHandler[p].releasedHandler(obj)
                  }
                }
              })
              ee.addListener(Events.PhoneEventAbandoned, function (obj) {
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].abandonedHandler(obj)
                }
              })
              //保持
              ee.addListener(Status.CallStatusHeld, function (obj) {
                //EChatPhoneHandler.heldHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].heldHandler(obj)
                }
              })

              //监听座席分机号事件
              ee.addListener(Events.PhoneEventMonitoringNextCall, function (obj) {
                //EChatPhoneHandler.monitoringNextCallHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].monitoringNextCallHandler(obj)
                }
              })

              //取消监听座席事件
              ee.addListener(Events.PhoneEventMonitoringNextCancelled, function (obj) {
                //EChatPhoneHandler.monitoringNextCancelledHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].monitoringNextCancelledHandler(obj)
                }
              })

              //泵机
              ee.addListener(Events.PhoneEventAllLinkDisconnected, function (obj) {
                //EChatPhoneHandler.allLinkDisconnectedHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].allLinkDisconnectedHandler(obj)
                }
              })

              //座席异常
              ee.addListener(Events.PhoneEventError, function (obj) {
                //EChatPhoneHandler.phoneEventErrorHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].phoneEventErrorHandler(obj)
                }
              })
              ee.addListener(Events.CallStatusPredict, function (obj) {
                //EChatPhoneHandler.phoneEventErrorHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].phonePredictHandler(obj)
                }
              })
              ee.addListener(Events.CallStatusPreview, function (obj) {
                //EChatPhoneHandler.phoneEventErrorHandler(obj)
                for(var p = 0; p < EChatPhoneHandler.length; p++){
                  EChatPhoneHandler[p].phonePreviewHandler(obj)
                }
              })

              /**old
               *       //座席签入
               ee.addListener(Events.EventLogin, function (obj) {
        EChatPhoneHandler.loginHandler(obj)
      })
               ee.addListener(Events.EventLogoin, function (obj) {
        EChatPhoneHandler.loginHandler(obj)
      })

               //座席签出
               ee.addListener(Events.EventLogout, function (obj) {
        EChatPhoneHandler.logoutHandler(obj)
      })

               //就绪或者未就绪
               ee.addListener(
               Events.EventTotalReady || Events.EventTotalNotReady,
               function (obj) {
          console.log(
            'total ready or total not ready ' + JSON.stringify(obj) + '!!'
          )
          EChatPhoneHandler.totalReadyOrNotReadyHandler(obj)
        }
               )

               //振铃事件
               ee.addListener(Events.PhoneEventRinging, function (obj) {
        EChatPhoneHandler.chatRingingHandler(obj)
      })

               //外呼事件
               ee.addListener(Events.PhoneEventDialing, function (obj) {
        EChatPhoneHandler.chatDialingHandler(obj)
      })

               //振铃超时
               ee.addListener(Events.PhoneEventRevoked, function (obj) {
        EChatPhoneHandler.revokedHandler(obj)
      })

               //会话建立事件
               ee.addListener(Events.PhoneEventEstablished, function (obj) {
        EChatPhoneHandler.establishedHandler(obj)
      })

               //弃话
               ee.addListener(Events.PhoneEventAbandoned, function (obj) {
        EChatPhoneHandler.abandonedHandler(obj)
      })

               //随路数据更新 EventAttachedDataChanged
               ee.addListener(Events.PhoneEventAttachedDataChanged, function (obj) {
        EChatPhoneHandler.attachedDataChangedHandler(obj)
      })

               //会话成员改变 PhoneEventPartyChanged
               ee.addListener(Events.PhoneEventPartyChanged, function (obj) {
        EChatPhoneHandler.partyChangedHandler(obj)
      })

               //会话成员加入 PhoneEventPartyAdded
               ee.addListener(Events.PhoneEventPartyAdded, function (obj) {
        EChatPhoneHandler.partyAddedHandler(obj)
      })

               //会议成员删除（ivr验密）
               ee.addListener(Events.PhoneEventPartyDeleted, function (obj) {
        EChatPhoneHandler.partyDeletedHandler(obj)
      })

               //挂机
               ee.addListener(Events.PhoneEventReleased, function (obj) {
        EChatPhoneHandler.releasedHandler(obj)
      })

               //保持
               ee.addListener(Events.PhoneEventHeld, function (obj) {
        EChatPhoneHandler.heldHandler(obj)
      })

               //监听座席分机号事件
               ee.addListener(Events.PhoneEventMonitoringNextCall, function (obj) {
        EChatPhoneHandler.monitoringNextCallHandler(obj)
      })

               //取消监听座席事件
               ee.addListener(Events.PhoneEventMonitoringNextCancelled, function (obj) {
        EChatPhoneHandler.monitoringNextCancelledHandler(obj)
      })

               //泵机
               ee.addListener(Events.PhoneEventAllLinkDisconnected, function (obj) {
        EChatPhoneHandler.allLinkDisconnectedHandler(obj)
      })

               //座席异常
               ee.addListener(Events.PhoneEventError, function (obj) {
        EChatPhoneHandler.phoneEventErrorHandler(obj)
      })
               * **/
            }
          }
        }

//公开方法
        EChat.phone = Phone
        module.exports = EChat


        /***/ }),
      /* 4 */
      /***/ (function(module, exports) {

        (function () {

          var utils = {
            // hasFormData: _hasFormData,
            //
            // hasBlob: _hasBlob,
            //
            // isCanSetRequestHeader: _isCanSetRequestHeader,
            //
            // hasOverrideMimeType: _hasOverrideMimeType,
            //
            // isCanUploadFileAsync: _isCanUploadFileAsync,
            //
            // isCanUploadFile: _isCanUploadFile,
            //
            // isCanDownLoadFile: _isCanDownLoadFile,

            isSupportWss: (function () {
              var notSupportList = [
                //1: QQ browser X5 core
                /MQQBrowser[/]5([.]\d+)?\sTBS/,

                //2: etc.
                //...
              ];

              if (!window.WebSocket) {
                return false;
              }

              var ua = window.navigator.userAgent;
              for (var i = 0, l = notSupportList.length; i < l; i++) {
                if (notSupportList[i].test(ua)) {
                  return false;
                }
              }
              return true;
            }()),

            getIEVersion: (function () {
              var ua = navigator.userAgent, matches, tridentMap = {'4': 8, '5': 9, '6': 10, '7': 11};

              matches = ua.match(/MSIE (\d+)/i);

              if (matches && matches[1]) {
                return +matches[1];
              }
              matches = ua.match(/Trident\/(\d+)/i);
              if (matches && matches[1]) {
                return tridentMap[matches[1]] || null;
              }
              return null;
            }()),
            getBrowserInfo: (function () {
              var agent = navigator.userAgent.toLowerCase();
              var regStr_ie = /msie [\d.]+;/gi;
              var regStr_ff = /firefox\/[\d.]+/gi;
              var regStr_chrome = /chrome\/[\d.]+/gi;
              var regStr_saf = /safari\/[\d.]+/gi;
              var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1); //判断是否IE<11浏览器
              var isEdge = agent.indexOf("edge") > -1 && !isIE; //判断是否IE的Edge浏览器
              var isIE11 = agent.indexOf('trident') > -1 && agent.indexOf("rv:11.0") > -1;
              if (isIE) {
                var reIE = new RegExp("msie (\\d+\\.\\d+);");
                reIE.test(agent);
                var fIEVersion = parseFloat(RegExp["$1"]);
                if (fIEVersion == 7 || fIEVersion == 8 || fIEVersion == 9) {
                  //var object = '<object name="client" classid="clsid:3A936095-1D1B-419F-B57B-FA98E41128CD" style="height:1pt;width:1pt"  codebase="WebSocketClient/WebSocketClient.cab#version=1,0,0,0"></object>'
                  //$(body).append(object)
                  return true;
                }else{
                  return false;
                }
              } //isIE end
              if (!isIE) {
                return false;
              }
            }()),

            stringify: function (json) {
              if (typeof JSON !== 'undefined' && JSON.stringify) {
                return JSON.stringify(json);
              } else {
                var s = '',
                    arr = [];

                var iterate = function (json) {
                  var isArr = false;

                  if (Object.prototype.toString.call(json) === '[object Array]') {
                    arr.push(']', '[');
                    isArr = true;
                  } else if (Object.prototype.toString.call(json) === '[object Object]') {
                    arr.push('}', '{');
                  }

                  for (var o in json) {
                    if (Object.prototype.toString.call(json[o]) === '[object Null]') {
                      json[o] = 'null';
                    } else if (Object.prototype.toString.call(json[o]) === '[object Undefined]') {
                      json[o] = 'undefined';
                    }

                    if (json[o] && typeof json[o] === 'object') {
                      s += ',' + (isArr ? '' : '"' + o + '":' + (isArr ? '"' : '')) + iterate(json[o]) + '';
                    } else {
                      s += ',"' + (isArr ? '' : o + '":"') + json[o] + '"';
                    }
                  }

                  if (s != '') {
                    s = s.slice(1);
                  }

                  return arr.pop() + s + arr.pop();
                };
                return iterate(json);
              }
            },
            // login: function (opt) {
            //     var options = opt || {};
            //     var suc = options.success || EMPTYFN;
            //     var err = options.error || EMPTYFN;
            //
            //     var appKey = options.appKey || '';
            //     var devInfos = appKey.split('#');
            //     if (devInfos.length !== 2) {
            //         err({
            //             type: _code.ECHAT_CONNCTION_APPKEY_NOT_ASSIGN_ERROR
            //         });
            //         return false;
            //     }
            //
            //     var orgName = devInfos[0];
            //     var appName = devInfos[1];
            //     var https = https || options.https;
            //     var user = options.user || '';
            //     var pwd = options.pwd || '';
            //
            //     var apiUrl = options.apiUrl;
            //
            //     var loginJson = {
            //         grant_type: 'password',
            //         username: user,
            //         password: pwd,
            //         timestamp: +new Date()
            //     };
            //     var loginfo = utils.stringify(loginJson);
            //
            //     var options = {
            //         url: apiUrl + '/' + orgName + '/' + appName + '/token',
            //         dataType: 'json',
            //         data: loginfo,
            //         success: suc,
            //         error: err
            //     };
            //     return utils.ajax(options);
            // },

            // getFileUrl: function (fileInputId) {
            //     var uri = {
            //         url: '',
            //         filename: '',
            //         filetype: '',
            //         data: ''
            //     };
            //
            //     var fileObj = typeof fileInputId === 'string' ? document.getElementById(fileInputId) : fileInputId;
            //
            //     if (!utils.isCanUploadFileAsync || !fileObj) {
            //         return uri;
            //     }
            //     try {
            //         if (window.URL.createObjectURL) {
            //             var fileItems = fileObj.files;
            //             if (fileItems.length > 0) {
            //                 var u = fileItems.item(0);
            //                 uri.data = u;
            //                 uri.url = window.URL.createObjectURL(u);
            //                 uri.filename = u.name || '';
            //             }
            //         } else { // IE
            //             var u = document.getElementById(fileInputId).value;
            //             uri.url = u;
            //             var pos1 = u.lastIndexOf('/');
            //             var pos2 = u.lastIndexOf('\\');
            //             var pos = Math.max(pos1, pos2);
            //             if (pos < 0)
            //                 uri.filename = u;
            //             else
            //                 uri.filename = u.substring(pos + 1);
            //         }
            //         var index = uri.filename.lastIndexOf('.');
            //         if (index != -1) {
            //             uri.filetype = uri.filename.substring(index + 1).toLowerCase();
            //         }
            //         return uri;
            //
            //     } catch (e) {
            //         throw e;
            //     }
            // },
            //
            // getFileSize: function (file) {
            //     var fileSize = this.getFileLength(file);
            //     if (fileSize > 10000000) {
            //         return false;
            //     }
            //     var kb = Math.round(fileSize / 1000);
            //     if (kb < 1000) {
            //         fileSize = kb + ' KB';
            //     } else if (kb >= 1000) {
            //         var mb = kb / 1000;
            //         if (mb < 1000) {
            //             fileSize = mb.toFixed(1) + ' MB';
            //         } else {
            //             var gb = mb / 1000;
            //             fileSize = gb.toFixed(1) + ' GB';
            //         }
            //     }
            //     return fileSize;
            // },
            //
            // getFileLength: function (file) {
            //     var fileLength = 0;
            //     if (file) {
            //         if (file.files) {
            //             if (file.files.length > 0) {
            //                 fileLength = file.files[0].size;
            //             }
            //         } else if (file.select && 'ActiveXObject' in window) {
            //             file.select();
            //             var fileobject = new ActiveXObject('Scripting.FileSystemObject');
            //             var file = fileobject.GetFile(file.value);
            //             fileLength = file.Size;
            //         }
            //     }
            //     return fileLength;
            // },
            //
            // hasFlash: _hasFlash,
            //
            // trim: function (str) {
            //
            //     str = typeof str === 'string' ? str : '';
            //
            //     return str.trim
            //         ? str.trim()
            //         : str.replace(/^\s|\s$/g, '');
            // },
            //
            // parseEmoji: function (msg) {
            //     if (typeof EChat.Emoji === 'undefined' || typeof EChat.Emoji.map === 'undefined') {
            //         return msg;
            //     } else {
            //         var emoji = EChat.Emoji,
            //             reg = null;
            //
            //         for (var face in emoji.map) {
            //             if (emoji.map.hasOwnProperty(face)) {
            //                 while (msg.indexOf(face) > -1) {
            //                     msg = msg.replace(face, '<img class="emoji" src="' + emoji.path + emoji.map[face] + '" />');
            //                 }
            //             }
            //         }
            //         return msg;
            //     }
            // },
            //
            // parseLink: function (msg) {
            //
            //     var reg = /(https?\:\/\/|www\.)([a-zA-Z0-9-]+(\.[a-zA-Z0-9]+)+)(\:[0-9]{2,4})?\/?((\.[:_0-9a-zA-Z-]+)|[:_0-9a-zA-Z-]*\/?)*\??[:_#@*&%0-9a-zA-Z-/=]*/gm;
            //
            //     msg = msg.replace(reg, function (v) {
            //
            //         var prefix = /^https?/gm.test(v);
            //
            //         return "<a href='"
            //             + (prefix ? v : '//' + v)
            //             + "' target='_blank'>"
            //             + v
            //             + "</a>";
            //
            //     });
            //
            //     return msg;
            //
            // },
            //
            // parseJSON: function (data) {
            //
            //     if (window.JSON && window.JSON.parse) {
            //         return window.JSON.parse(data + '');
            //     }
            //
            //     var requireNonComma,
            //         depth = null,
            //         str = utils.trim(data + '');
            //
            //     return str && !utils.trim(
            //         str.replace(/(,)|(\[|{)|(}|])|"(?:[^"\\\r\n]|\\["\\\/bfnrt]|\\u[\da-fA-F]{4})*"\s*:?|true|false|null|-?(?!0\d)\d+(?:\.\d+|)(?:[eE][+-]?\d+|)/g
            //             , function (token, comma, open, close) {
            //
            //                 if (requireNonComma && comma) {
            //                     depth = 0;
            //                 }
            //
            //                 if (depth === 0) {
            //                     return token;
            //                 }
            //
            //                 requireNonComma = open || comma;
            //                 depth += !close - !open;
            //                 return '';
            //             })
            //     )
            //         ? (Function('return ' + str))()
            //         : (Function('Invalid JSON: ' + data))();
            // },
            //
            // parseUploadResponse: function (response) {
            //     return response.indexOf('callback') > -1 ? //lte ie9
            //         response.slice(9, -1) : response;
            // },
            //
            // parseDownloadResponse: function (response) {
            //     return ((response && response.type && response.type === 'application/json')
            //     || 0 > Object.prototype.toString.call(response).indexOf('Blob'))
            //         ? this.url + '?token=' : window.URL.createObjectURL(response);
            // },
            //
            // uploadFile: function (options) {
            //     var options = options || {};
            //     options.onFileUploadProgress = options.onFileUploadProgress || EMPTYFN;
            //     options.onFileUploadComplete = options.onFileUploadComplete || EMPTYFN;
            //     options.onFileUploadError = options.onFileUploadError || EMPTYFN;
            //     options.onFileUploadCanceled = options.onFileUploadCanceled || EMPTYFN;
            //
            //     var acc = options.accessToken || this.context.accessToken;
            //     if (!acc) {
            //         options.onFileUploadError({
            //             type: _code.ECHAT_UPLOADFILE_NO_LOGIN
            //             , id: options.id
            //         });
            //         return;
            //     }
            //
            //     var orgName, appName, devInfos;
            //     var appKey = options.appKey || this.context.appKey || '';
            //
            //     if (appKey) {
            //         devInfos = appKey.split('#');
            //         orgName = devInfos[0];
            //         appName = devInfos[1];
            //     }
            //
            //     if (!orgName && !appName) {
            //         options.onFileUploadError({
            //             type: _code.ECHAT_UPLOADFILE_ERROR
            //             , id: options.id
            //         });
            //         return;
            //     }
            //
            //     var apiUrl = options.apiUrl;
            //     var uploadUrl = apiUrl + '/' + orgName + '/' + appName + '/chatfiles';
            //
            //     if (!utils.isCanUploadFileAsync) {
            //         if (utils.hasFlash && typeof options.flashUpload === 'function') {
            //             options.flashUpload && options.flashUpload(uploadUrl, options);
            //         } else {
            //             options.onFileUploadError({
            //                 type: _code.ECHAT_UPLOADFILE_BROWSER_ERROR
            //                 , id: options.id
            //             });
            //         }
            //         return;
            //     }
            //
            //     var fileSize = options.file.data ? options.file.data.size : undefined;
            //     if (fileSize > ECHAT_FILESIZE_LIMIT) {
            //         options.onFileUploadError({
            //             type: _code.ECHAT_UPLOADFILE_ERROR
            //             , id: options.id
            //         });
            //         return;
            //     } else if (fileSize <= 0) {
            //         options.onFileUploadError({
            //             type: _code.ECHAT_UPLOADFILE_ERROR
            //             , id: options.id
            //         });
            //         return;
            //     }
            //
            //     var xhr = utils.xmlrequest();
            //     var onError = function (e) {
            //         options.onFileUploadError({
            //             type: _code.ECHAT_UPLOADFILE_ERROR,
            //             id: options.id,
            //             xhr: xhr
            //         });
            //     };
            //     if (xhr.upload) {
            //         xhr.upload.addEventListener('progress', options.onFileUploadProgress, false);
            //     }
            //     if (xhr.addEventListener) {
            //         xhr.addEventListener('abort', options.onFileUploadCanceled, false);
            //         xhr.addEventListener('load', function (e) {
            //             try {
            //                 var json = utils.parseJSON(xhr.responseText);
            //                 try {
            //                     options.onFileUploadComplete(json);
            //                 } catch (e) {
            //                     options.onFileUploadError({
            //                         type: _code.ECHAT_CONNCTION_CALLBACK_INNER_ERROR
            //                         , data: e
            //                     });
            //                 }
            //             } catch (e) {
            //                 options.onFileUploadError({
            //                     type: _code.ECHAT_UPLOADFILE_ERROR,
            //                     data: xhr.responseText,
            //                     id: options.id,
            //                     xhr: xhr
            //                 });
            //             }
            //         }, false);
            //         xhr.addEventListener('error', onError, false);
            //     } else if (xhr.onreadystatechange) {
            //         xhr.onreadystatechange = function () {
            //             if (xhr.readyState === 4) {
            //                 if (ajax.status === 200) {
            //                     try {
            //                         var json = utils.parseJSON(xhr.responseText);
            //                         options.onFileUploadComplete(json);
            //                     } catch (e) {
            //                         options.onFileUploadError({
            //                             type: _code.ECHAT_UPLOADFILE_ERROR,
            //                             data: xhr.responseText,
            //                             id: options.id,
            //                             xhr: xhr
            //                         });
            //                     }
            //                 } else {
            //                     options.onFileUploadError({
            //                         type: _code.ECHAT_UPLOADFILE_ERROR,
            //                         data: xhr.responseText,
            //                         id: options.id,
            //                         xhr: xhr
            //                     });
            //                 }
            //             } else {
            //                 xhr.abort();
            //                 options.onFileUploadCanceled();
            //             }
            //         }
            //     }
            //
            //     xhr.open('POST', uploadUrl);
            //
            //     xhr.setRequestHeader('restrict-access', 'true');
            //     xhr.setRequestHeader('Accept', '*/*');// Android QQ browser has some problem with this attribute.
            //     xhr.setRequestHeader('Authorization', 'Bearer ' + acc);
            //
            //     var formData = new FormData();
            //     formData.append('file', options.file.data);
            //     // fix: ie8 status error
            //     window.XDomainRequest && (xhr.readyState = 2)
            //     xhr.send(formData);
            // },

            // download: function (options) {
            //     options.onFileDownloadComplete = options.onFileDownloadComplete || EMPTYFN;
            //     options.onFileDownloadError = options.onFileDownloadError || EMPTYFN;
            //
            //     var accessToken = options.accessToken || this.context.accessToken;
            //     if (!accessToken) {
            //         options.onFileDownloadError({
            //             type: _code.ECHAT_DOWNLOADFILE_NO_LOGIN,
            //             id: options.id
            //         });
            //         return;
            //     }
            //
            //     var onError = function (e) {
            //         options.onFileDownloadError({
            //             type: _code.ECHAT_DOWNLOADFILE_ERROR,
            //             id: options.id,
            //             xhr: xhr
            //         });
            //     };
            //
            //     if (!utils.isCanDownLoadFile) {
            //         options.onFileDownloadComplete();
            //         return;
            //     }
            //     var xhr = utils.xmlrequest();
            //     if ('addEventListener' in xhr) {
            //         xhr.addEventListener('load', function (e) {
            //             options.onFileDownloadComplete(xhr.response, xhr);
            //         }, false);
            //         xhr.addEventListener('error', onError, false);
            //     } else if ('onreadystatechange' in xhr) {
            //         xhr.onreadystatechange = function () {
            //             if (xhr.readyState === 4) {
            //                 if (ajax.status === 200) {
            //                     options.onFileDownloadComplete(xhr.response, xhr);
            //                 } else {
            //                     options.onFileDownloadError({
            //                         type: _code.ECHAT_DOWNLOADFILE_ERROR,
            //                         id: options.id,
            //                         xhr: xhr
            //                     });
            //                 }
            //             } else {
            //                 xhr.abort();
            //                 options.onFileDownloadError({
            //                     type: _code.ECHAT_DOWNLOADFILE_ERROR,
            //                     id: options.id,
            //                     xhr: xhr
            //                 });
            //             }
            //         }
            //     }
            //
            //     var method = options.method || 'GET';
            //     var resType = options.responseType || 'blob';
            //     var mimeType = options.mimeType || 'text/plain; charset=x-user-defined';
            //     xhr.open(method, options.url);
            //     if (typeof Blob !== 'undefined') {
            //         xhr.responseType = resType;
            //     } else {
            //         xhr.overrideMimeType(mimeType);
            //     }
            //
            //     var innerHeaer = {
            //         'X-Requested-With': 'XMLHttpRequest',
            //         'Accept': 'application/octet-stream',
            //         'share-secret': options.secret,
            //         'Authorization': 'Bearer ' + accessToken
            //     };
            //     var headers = options.headers || {};
            //     for (var key in headers) {
            //         innerHeaer[key] = headers[key];
            //     }
            //     for (var key in innerHeaer) {
            //         if (innerHeaer[key]) {
            //             xhr.setRequestHeader(key, innerHeaer[key]);
            //         }
            //     }
            //     // fix: ie8 status error
            //     window.XDomainRequest && (xhr.readyState = 2)
            //     xhr.send(null);
            // },
            //
            // parseTextMessage: function (message, faces) {
            //     if (typeof message !== 'string') {
            //         return;
            //     }
            //
            //     if (Object.prototype.toString.call(faces) !== '[object Object]') {
            //         return {
            //             isemoji: false,
            //             body: [
            //                 {
            //                     type: 'txt',
            //                     data: message
            //                 }
            //             ]
            //         };
            //     }
            //
            //     var receiveMsg = message;
            //     var emessage = [];
            //     var expr = /\[[^[\]]{2,3}\]/mg;
            //     var emoji = receiveMsg.match(expr);
            //
            //     if (!emoji || emoji.length < 1) {
            //         return {
            //             isemoji: false,
            //             body: [
            //                 {
            //                     type: 'txt',
            //                     data: message
            //                 }
            //             ]
            //         };
            //     }
            //     var isemoji = false;
            //     for (var i = 0; i < emoji.length; i++) {
            //         var tmsg = receiveMsg.substring(0, receiveMsg.indexOf(emoji[i])),
            //             existEmoji = EChat.Emoji.map[emoji[i]];
            //
            //         if (tmsg) {
            //             emessage.push({
            //                 type: 'txt',
            //                 data: tmsg
            //             });
            //         }
            //         if (!existEmoji) {
            //             emessage.push({
            //                 type: 'txt',
            //                 data: emoji[i]
            //             });
            //             continue;
            //         }
            //         var emojiStr = EChat.Emoji.map ? EChat.Emoji.path + existEmoji : null;
            //
            //         if (emojiStr) {
            //             isemoji = true;
            //             emessage.push({
            //                 type: 'emoji',
            //                 data: emojiStr
            //             });
            //         } else {
            //             emessage.push({
            //                 type: 'txt',
            //                 data: emoji[i]
            //             });
            //         }
            //         var restMsgIndex = receiveMsg.indexOf(emoji[i]) + emoji[i].length;
            //         receiveMsg = receiveMsg.substring(restMsgIndex);
            //     }
            //     if (receiveMsg) {
            //         emessage.push({
            //             type: 'txt',
            //             data: receiveMsg
            //         });
            //     }
            //     if (isemoji) {
            //         return {
            //             isemoji: isemoji,
            //             body: emessage
            //         };
            //     }
            //     return {
            //         isemoji: false,
            //         body: [
            //             {
            //                 type: 'txt',
            //                 data: message
            //             }
            //         ]
            //     };
            // },

            parseUri: function () {
              var pattern = /([^?|&])\w+=([^&]+)/g;
              var uri = {};
              if (window.location.search) {
                var args = window.location.search.match(pattern);
                for (var i in args) {
                  var str = args[i];
                  var eq = str.indexOf('=');
                  var key = str.substr(0, eq);
                  var value = str.substr(eq + 1);
                  uri[key] = value;
                }
              }
              return uri;
            },

            parseHrefHash: function () {
              var pattern = /([^#|&])\w+=([^&]+)/g;
              var uri = {};
              if (window.location.hash) {
                var args = window.location.hash.match(pattern);
                for (var i in args) {
                  var str = args[i];
                  var eq = str.indexOf('=');
                  var key = str.substr(0, eq);
                  var value = str.substr(eq + 1);
                  uri[key] = value;
                }
              }
              return uri;
            },

            // xmlrequest: _xmlrequest,
            //
            //
            // getXmlFirstChild: function (data, tagName) {
            //     var children = data.getElementsByTagName(tagName);
            //     if (children.length == 0) {
            //         return null;
            //     } else {
            //         return children[0];
            //     }
            // },
            // ajax: function (options) {
            //     var dataType = options.dataType || 'text';
            //     var suc = options.success || EMPTYFN;
            //     var error = options.error || EMPTYFN;
            //     var xhr = utils.xmlrequest();
            //
            //     xhr.onreadystatechange = function () {
            //         if (xhr.readyState === 4) {
            //             var status = xhr.status || 0;
            //             if (status === 200) {
            //                 try {
            //                     switch (dataType) {
            //                         case 'text': {
            //                             suc(xhr.responseText);
            //                             return;
            //                         }
            //                         case 'json': {
            //                             var json = utils.parseJSON(xhr.responseText);
            //                             suc(json, xhr);
            //                             return;
            //                         }
            //                         case 'xml': {
            //                             if (xhr.responseXML && xhr.responseXML.documentElement) {
            //                                 suc(xhr.responseXML.documentElement, xhr);
            //                             } else {
            //                                 error({
            //                                     type: _code.ECHAT_CONNCTION_AJAX_ERROR,
            //                                     data: xhr.responseText
            //                                 });
            //                             }
            //                             return;
            //                         }
            //                     }
            //                     suc(xhr.response || xhr.responseText, xhr);
            //                 } catch (e) {
            //                     error({
            //                         type: _code.ECHAT_CONNCTION_AJAX_ERROR,
            //                         data: e,
            //                     });
            //                 }
            //                 return;
            //             } else {
            //                 error({
            //                     type: _code.ECHAT_CONNCTION_AJAX_ERROR,
            //                     data: xhr.responseText,
            //                 });
            //                 return;
            //             }
            //         }
            //         if (xhr.readyState === 0) {
            //             error({
            //                 type: _code.ECHAT_CONNCTION_AJAX_ERROR,
            //                 data: xhr.responseText,
            //             });
            //         }
            //     };
            //
            //     if (options.responseType) {
            //         if (xhr.responseType) {
            //             xhr.responseType = options.responseType;
            //         }
            //     }
            //     if (options.mimeType) {
            //         if (utils.hasOverrideMimeType) {
            //             xhr.overrideMimeType(options.mimeType);
            //         }
            //     }
            //
            //     var type = options.type || 'POST',
            //         data = options.data || null,
            //         tempData = '';
            //
            //     if (type.toLowerCase() === 'get' && data) {
            //         for (var o in data) {
            //             if (data.hasOwnProperty(o)) {
            //                 tempData += o + '=' + data[o] + '&';
            //             }
            //         }
            //         tempData = tempData ? tempData.slice(0, -1) : tempData;
            //         options.url += (options.url.indexOf('?') > 0 ? '&' : '?') + (tempData ? tempData + '&' : tempData) + '_v=' + new Date().getTime();
            //         data = null;
            //         tempData = null;
            //     }
            //     xhr.open(type, options.url, utils.isCanSetRequestHeader);
            //
            //     if (utils.isCanSetRequestHeader) {
            //         var headers = options.headers || {};
            //         for (var key in headers) {
            //             if (headers.hasOwnProperty(key)) {
            //                 xhr.setRequestHeader(key, headers[key]);
            //             }
            //         }
            //     }
            //     // fix: ie8 status error
            //     window.XDomainRequest && (xhr.readyState = 2)
            //     xhr.send(data);
            //     return xhr;
            // },

            ts: function () {
              var d = new Date();
              var Hours = d.getHours(); // 获取当前小时数(0-23)
              var Minutes = d.getMinutes(); // 获取当前分钟数(0-59)
              var Seconds = d.getSeconds(); // 获取当前秒数(0-59)
              var Milliseconds = d.getMilliseconds(); //获取当前毫秒
              return (Hours < 10 ? '0' + Hours : Hours) + ':' + (Minutes < 10 ? '0' + Minutes : Minutes) + ':' + (Seconds < 10 ? '0' + Seconds : Seconds) + ':' + Milliseconds + ' ';
            },

            getObjectKey: function (obj, val) {
              for (var key in obj) {
                if (obj[key] == val) {
                  return key;
                }
              }
              return '';
            },

            sprintf: function () {
              var arg = arguments,
                  str = arg[0] || '',
                  i, len;
              for (i = 1, len = arg.length; i < len; i++) {
                str = str.replace(/%s/, arg[i]);
              }
              return str;
            },

            setCookie: function (name, value, days) {
              var cookie = name + '=' + encodeURIComponent(value);
              if (typeof days == 'number') {
                cookie += '; max-age: ' + (days * 60 * 60 * 24);
              }
              document.cookie = cookie;
            },

            getAllCookie: function () {
              var allCookie = {};
              var all = document.cookie;
              if (all === '') {
                return allCookie;
              }
              var list = all.split('; ');
              for (var i = 0; i < list.length; i++) {
                var cookie = list[i];
                var p = cookie.indexOf('=');
                var name = cookie.substring(0, p);
                var value = cookie.substring(p + 1);
                value = decodeURIComponent(value);
                allCookie[name] = value;
              }
              return allCookie;
            },

            getCookie: function (cookieName) {
              var allCookie = {};
              var all = document.cookie;
              if (all === '') {
                return allCookie;
              }
              var list = all.split('; ');
              for (var i = 0; i < list.length; i++) {
                var cookie = list[i];
                var p = cookie.indexOf('=');
                var name = cookie.substring(0, p);
                if (name && name === cookieName) {
                  var value = cookie.substring(p + 1);
                  value = decodeURIComponent(value);
                }
              }
              return null;
            }
          };

          exports.utils = utils;
        }());


        /***/ }),
      /* 5 */
      /***/ (function(module, exports, __webpack_require__) {

        var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;// MIT License:
//
// Copyright (c) 2010-2012, Joe Walnes
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

// https://github.com/joewalnes/reconnecting-websocket
        /**
         * This behaves like a WebSocket in every way, except if it fails to connect,
         * or it gets disconnected, it will repeatedly poll until it successfully connects
         * again.
         *
         * It is API compatible, so when you have:
         *   ws = new WebSocket('ws://....');
         * you can replace with:
         *   ws = new ReconnectingWebSocket('ws://....');
         *
         * The event stream will typically look like:
         *  onconnecting
         *  onopen
         *  onmessage
         *  onmessage
         *  onclose // lost connection
         *  onconnecting
         *  onopen  // sometime later...
         *  onmessage
         *  onmessage
         *  etc...
         *
         * It is API compatible with the standard WebSocket API, apart from the following members:
         *
         * - `bufferedAmount`
         * - `extensions`
         * - `binaryType`
         *
         * Latest version: https://github.com/joewalnes/reconnecting-websocket/
         * - Joe Walnes
         *
         * Syntax
         * ======
         * var socket = new ReconnectingWebSocket(url, protocols, options);
         *
         * Parameters
         * ==========
         * url - The url you are connecting to.
         * protocols - Optional string or array of protocols.
         * options - See below
         *
         * Options
         * =======
         * Options can either be passed upon instantiation or set after instantiation:
         *
         * var socket = new ReconnectingWebSocket(url, null, { debug: true, reconnectInterval: 4000 });
         *
         * or
         *
         * var socket = new ReconnectingWebSocket(url);
         * socket.debug = true;
         * socket.reconnectInterval = 4000;
         *
         * debug
         * - Whether this instance should log debug messages. Accepts true or false. Default: false.
         *
         * automaticOpen
         * - Whether or not the websocket should attempt to connect immediately upon instantiation. The socket can be manually opened or closed at any time using ws.open() and ws.close().
         *
         * reconnectInterval
         * - The number of milliseconds to delay before attempting to reconnect. Accepts integer. Default: 1000.
         *
         * maxReconnectInterval
         * - The maximum number of milliseconds to delay a reconnection attempt. Accepts integer. Default: 30000.
         *
         * reconnectDecay
         * - The rate of increase of the reconnect delay. Allows reconnect attempts to back off when problems persist. Accepts integer or float. Default: 1.5.
         *
         * timeoutInterval
         * - The maximum time in milliseconds to wait for a connection to succeed before closing and retrying. Accepts integer. Default: 2000.
         *
         */
        (function (global, factory) {
          if (true) {
            !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
                __WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
                    (__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
            __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
          } else if (typeof module !== 'undefined' && module.exports){
            module.exports = factory();
          } else {
            global.ReconnectingWebSocket = factory();
          }
        })(this, function () {

          if (!('WebSocket' in window)) {
            return;
          }

          function ReconnectingWebSocket(url, protocols, options) {

            // Default settings
            var settings = {

              /** Whether this instance should log debug messages. */
              debug: false,

              /** Whether or not the websocket should attempt to connect immediately upon instantiation. */
              automaticOpen: true,

              /** The number of milliseconds to delay before attempting to reconnect. */
              reconnectInterval: 1000,
              /** The maximum number of milliseconds to delay a reconnection attempt. */
              maxReconnectInterval: 10000,
              /** The rate of increase of the reconnect delay. Allows reconnect attempts to back off when problems persist. */
              reconnectDecay: 1.5,

              /** The maximum time in milliseconds to wait for a connection to succeed before closing and retrying. */
              timeoutInterval: 2000,

              /** The maximum number of reconnection attempts to make. Unlimited if null. */
              maxReconnectAttempts: null,

              /** The binary type, possible values 'blob' or 'arraybuffer', default 'blob'. */
              binaryType: 'blob'
            }
            if (!options) { options = {}; }

            // Overwrite and define settings with options if they exist.
            for (var key in settings) {
              if (typeof options[key] !== 'undefined') {
                this[key] = options[key];
              } else {
                this[key] = settings[key];
              }
            }

            // These should be treated as read-only properties

            /** The URL as resolved by the constructor. This is always an absolute URL. Read only. */
            this.url = url;

            /** The number of attempted reconnects since starting, or the last successful connection. Read only. */
            this.reconnectAttempts = 0;

            /**
             * The current state of the connection.
             * Can be one of: WebSocket.CONNECTING, WebSocket.OPEN, WebSocket.CLOSING, WebSocket.CLOSED
             * Read only.
             */
            this.readyState = WebSocket.CONNECTING;

            /**
             * A string indicating the name of the sub-protocol the server selected; this will be one of
             * the strings specified in the protocols parameter when creating the WebSocket object.
             * Read only.
             */
            this.protocol = null;

            // Private state variables

            var self = this;
            var ws;
            var forcedClose = false;
            var timedOut = false;
            var eventTarget = document.createElement('div');

            // Wire up "on*" properties as event handlers

            eventTarget.addEventListener('open',       function(event) { self.onopen(event); });
            eventTarget.addEventListener('close',      function(event) { self.onclose(event); });
            eventTarget.addEventListener('connecting', function(event) { self.onconnecting(event); });
            eventTarget.addEventListener('message',    function(event) { self.onmessage(event); });
            eventTarget.addEventListener('error',      function(event) { self.onerror(event); });

            // Expose the API required by EventTarget

            this.addEventListener = eventTarget.addEventListener.bind(eventTarget);
            this.removeEventListener = eventTarget.removeEventListener.bind(eventTarget);
            this.dispatchEvent = eventTarget.dispatchEvent.bind(eventTarget);

            /**
             * This function generates an event that is compatible with standard
             * compliant browsers and IE9 - IE11
             *
             * This will prevent the error:
             * Object doesn't support this action
             *
             * http://stackoverflow.com/questions/19345392/why-arent-my-parameters-getting-passed-through-to-a-dispatched-event/19345563#19345563
             * @param s String The name that the event should use
             * @param args Object an optional object that the event will use
             */
            function generateEvent(s, args) {
              var evt = document.createEvent("CustomEvent");
              evt.initCustomEvent(s, false, false, args);
              return evt;
            };

            this.open = function (reconnectAttempt) {
              ws = new WebSocket(self.url, protocols || []);
              ws.binaryType = this.binaryType;

              if (reconnectAttempt) {
                if (this.maxReconnectAttempts && this.reconnectAttempts > this.maxReconnectAttempts) {
                  return;
                }
              } else {
                eventTarget.dispatchEvent(generateEvent('connecting'));
                this.reconnectAttempts = 0;
              }

              if (self.debug || ReconnectingWebSocket.debugAll) {
                console.debug('ReconnectingWebSocket', 'attempt-connect', self.url);
              }

              var localWs = ws;
              var timeout = setTimeout(function() {
                if (self.debug || ReconnectingWebSocket.debugAll) {
                  console.debug('ReconnectingWebSocket', 'connection-timeout', self.url);
                }
                timedOut = true;
                localWs.close();
                timedOut = false;
              }, self.timeoutInterval);

              ws.onopen = function(event) {
                clearTimeout(timeout);
                if (self.debug || ReconnectingWebSocket.debugAll) {
                  console.debug('ReconnectingWebSocket', 'onopen', self.url);
                }
                self.protocol = ws.protocol;
                self.readyState = WebSocket.OPEN;
                self.reconnectAttempts = 0;
                var e = generateEvent('open');
                e.isReconnect = reconnectAttempt;
                reconnectAttempt = false;
                eventTarget.dispatchEvent(e);
              };

              ws.onclose = function(event) {
                clearTimeout(timeout);
                ws = null;
                if (forcedClose) {
                  self.readyState = WebSocket.CLOSED;
                  eventTarget.dispatchEvent(generateEvent('close'));
                } else {
                  self.readyState = WebSocket.CONNECTING;
                  var e = generateEvent('connecting');
                  e.code = event.code;
                  e.reason = event.reason;
                  e.wasClean = event.wasClean;
                  eventTarget.dispatchEvent(e);
                  if (!reconnectAttempt && !timedOut) {
                    if (self.debug || ReconnectingWebSocket.debugAll) {
                      console.debug('ReconnectingWebSocket', 'onclose', self.url);
                    }
                    eventTarget.dispatchEvent(generateEvent('close'));
                  }

                  var timeout = self.reconnectInterval * Math.pow(self.reconnectDecay, self.reconnectAttempts);
                  setTimeout(function() {
                    self.reconnectAttempts++;
                    self.open(true);
                  }, timeout > self.maxReconnectInterval ? self.maxReconnectInterval : timeout);
                }
              };
              ws.onmessage = function(event) {
                if (self.debug || ReconnectingWebSocket.debugAll) {
                  console.debug('ReconnectingWebSocket', 'onmessage', self.url, event.data);
                }
                var e = generateEvent('message');
                e.data = event.data;
                eventTarget.dispatchEvent(e);
              };
              ws.onerror = function(event) {
                if (self.debug || ReconnectingWebSocket.debugAll) {
                  console.debug('ReconnectingWebSocket', 'onerror', self.url, event);
                }
                eventTarget.dispatchEvent(generateEvent('error'));
              };
            }

            // Whether or not to create a websocket upon instantiation
            if (this.automaticOpen == true) {
              this.open(false);
            }

            /**
             * Transmits data to the server over the WebSocket connection.
             *
             * @param data a text string, ArrayBuffer or Blob to send to the server.
             */
            this.send = function(data) {
              if (ws) {
                if (self.debug || ReconnectingWebSocket.debugAll) {
                  console.debug('ReconnectingWebSocket', 'send', self.url, data);
                }
                return ws.send(data);
              } else {
                throw 'INVALID_STATE_ERR : Pausing to reconnect websocket';
              }
            };

            /**
             * Closes the WebSocket connection or connection attempt, if any.
             * If the connection is already CLOSED, this method does nothing.
             */
            this.close = function(code, reason) {
              // Default CLOSE_NORMAL code
              if (typeof code == 'undefined') {
                code = 1000;
              }
              forcedClose = true;
              if (ws) {
                ws.close(code, reason);
              }
            };

            /**
             * Additional public API method to refresh the connection if still open (close, re-open).
             * For example, if the app suspects bad data / missed heart beats, it can try to refresh.
             */
            this.refresh = function() {
              if (ws) {
                ws.close();
              }
            };
          }

          /**
           * An event listener to be called when the WebSocket connection's readyState changes to OPEN;
           * this indicates that the connection is ready to send and receive data.
           */
          ReconnectingWebSocket.prototype.onopen = function(event) {};
          /** An event listener to be called when the WebSocket connection's readyState changes to CLOSED. */
          ReconnectingWebSocket.prototype.onclose = function(event) {};
          /** An event listener to be called when a connection begins being attempted. */
          ReconnectingWebSocket.prototype.onconnecting = function(event) {};
          /** An event listener to be called when a message is received from the server. */
          ReconnectingWebSocket.prototype.onmessage = function(event) {};
          /** An event listener to be called when an error occurs. */
          ReconnectingWebSocket.prototype.onerror = function(event) {};

          /**
           * Whether all instances of ReconnectingWebSocket should log debug messages.
           * Setting this to true is the equivalent of setting all instances of ReconnectingWebSocket.debug to true.
           */
          ReconnectingWebSocket.debugAll = false;

          ReconnectingWebSocket.CONNECTING = WebSocket.CONNECTING;
          ReconnectingWebSocket.OPEN = WebSocket.OPEN;
          ReconnectingWebSocket.CLOSING = WebSocket.CLOSING;
          ReconnectingWebSocket.CLOSED = WebSocket.CLOSED;

          return ReconnectingWebSocket;
        });


        /***/ }),
      /* 6 */
      /***/ (function(module, exports) {

        /* global CONFIG */
        ; (function () {
          'use strict'

          var Message = {
            /**
             * 电话注册
             *
             * @param agentId
             * @param thisDN
             */
            getPhoneRegisterJson: function ( agentId, thisDN, tenantid, userData) {
              var _registerJson = {
                requestid: 'RequestRegisterDN',
                agentid: agentId,
                thisdn: thisDN,
                userdata: userData,
                tenantid : tenantid
              }

              return JSON.stringify(_registerJson)
            },

            /**
             * 预览外呼
             *
             * @param agentId
             * @param thisDN
             */
            getPreviewOutboundJson: function ( agentId, thisDN, tenantid) {
              var _registerJson = {
                requestid: 'RequestAgentPreview',
                agentid: agentId,
                thisdn: thisDN,
                tenantid : tenantid
              }

              return JSON.stringify(_registerJson)
            },
            /**
             * 预测外呼
             *
             * @param agentId
             * @param thisDN
             */
            getPredictOutboundJson: function ( agentId, thisDN, tenantid) {
              var _registerJson = {
                requestid: 'RequestAgentPredict',
                agentid: agentId,
                thisdn: thisDN,
                tenantid : tenantid
              }

              return JSON.stringify(_registerJson)
            },

            /**
             * 电话登录
             *
             * @param agentid
             * @param thisdn
             */
            getPhoneLoginJson: function (agentId, thisDN,tenantid,userData) {
              var _login = {
                requestid: 'RequestAgentLogin',
                agentid: agentId,
                thisdn: thisDN,
                tenantid : tenantid,
                userdata:{
                  dntype:window.CONFIG.dntype
                },
              }
              if(userData && userData.agentIp){
                _login.agentIp = userData.agentIp;
              }
              return JSON.stringify(_login)
            },

            /**
             * 电话签出
             *
             * @param agentid
             * @param thisdn
             */
            getPhoneLogoutJson: function (agentId, thisDN,tenantid) {
              var _logout = {
                requestid: 'RequestAgentLogout',
                agentid: agentId,
                thisdn: thisDN,
                tenantid :tenantid
              }
              return JSON.stringify(_logout)
            },

            /**
             * 电话就绪
             *
             * @param agentId
             * @param thisDN
             */
            getPhoneReadyJson: function (agentId, thisDN,tenantid) {
              var _ready = {
                requestid: 'RequestAgentReady',
                agentid: agentId,
                thisdn: thisDN,
                tenantid :tenantid
              }
              return JSON.stringify(_ready)
            },

            /**
             * 电话未就绪
             *
             * @param agentId
             * @param thisDN
             * @param reason
             */
            getPhoneNotReadyJson: function (reason,agentId, thisDN,tenantid) {
              var _notReady = {
                requestid: 'RequestAgentNotReady',
                agentid: agentId,
                thisdn: thisDN,
                reason: reason,
                tenantid : tenantid
              }
              return JSON.stringify(_notReady)
            },

            /**
             * 电话渠道进入话后处理（手动操作）
             *
             * @param agentId
             * @param thisDN
             * @param reason
             */
            getPhoneAgentAfterCallWorkJson: function (reason,agentId, thisDN,tenantid) {
              var _notReady = {
                requestid: 'RequestAgentAfterCallWork',
                agentid: agentId,
                thisdn: thisDN,
                reason: reason,
                tenantid : tenantid
              }
              return JSON.stringify(_notReady)
            },
            /**
             * 电话渠道取消话后处理（手动操作）
             *
             * @param agentId
             * @param thisDN
             * @param reason
             */
            getCancelAfterCallWorkJson: function(agentId, thisDN, tenantId){
              var _cancelAfterCallWork = {
                requestid : 'RequestCancelAfterCallWork',
                agentid : agentId,
                thisdn : thisDN,
                tenantid : tenantId
              }
              return JSON.stringify(_cancelAfterCallWork);
            },
            getTransferIVRJsonOfHW:function(agentId, thisDN, otherDN,devicetype,userdata){
              var _transferIVR = {
                requestid : 'RequestTransferIVR',
                agentid : agentId,
                thisdn : thisDN,
                otherdn : otherDN,
                userdata : userdata
              }
              return JSON.stringify(_transferIVR);
            },
            getConferenceIVR:function(agentId, thisDN, otherDN,devicetype,userdata){
              var _transferIVR = {
                requestid : 'RequestConferenceIVR',
                agentid : agentId,
                thisdn : thisDN,
                otherdn : otherDN,
                userdata: userdata
              }
              return JSON.stringify(_transferIVR);
            },

            /**
             * 电话Make Call
             *
             * @param agentId
             * @param thisDN
             * @param otherID
             * @param otherDN
             * @param userData自定义随路数据type为freelink平台区分呼内线还是外线“0”为内线，“1”为外线
             */

            getMakeCallJson: function (otherID,otherDN, agentId, thisDN,tenantid,userData) {
              var _makeCall = {
                requestid: 'RequestMakeCall',
                agentid: agentId,
                thisdn: thisDN,
                otherAgentId : otherID,
                otherdn: otherDN,
                userdata:{
                  type:"1",
                  dntype:window.CONFIG.dntype
                },
                tenantid:tenantid
              }
              if(userData && !userData.type){
                userData.type =  _makeCall.userdata.type;
              }
              if(userData && !userData.dntype){
                userData.dntype =  _makeCall.userdata.dntype;
              }
              _makeCall.userdata = userData;

              return JSON.stringify(_makeCall)
            },

            /**
             * 电话应答
             *
             * @param agentId
             * @param thisDN
             * @param connId
             */
            getAnswerCallJson: function (agentId, thisDN, connId,tenantid) {
              var _answerCall = {
                requestid: 'RequestAnswerCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                tenantid :tenantid
              }
              return JSON.stringify(_answerCall)
            },

            /**
             * 电话挂机
             *
             * @param agentId
             * @param thisDN
             * @param connId
             */
            getReleaseCallJson: function (agentId, thisDN, connId,tenantid) {
              var _releaseCall = {
                requestid: 'RequestReleaseCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                // reason: 'agent',
                tenantid :tenantid
              }
              if(!connId){
                _releaseCall = {
                  requestid: 'RequestReleaseCall',
                  agentid: agentId,
                  thisdn: thisDN,
                  // reason: 'agent',
                  tenantid :tenantid
                }
              }
              return JSON.stringify(_releaseCall)
            },
            /**
             * 电话拒接
             *
             * @param agentId
             * @param thisDN
             * @param connId
             */
            getRejectCallJson: function (otherdn,connId,agentId, thisDN ,tenantId) {
              var _rejectCall = {
                requestid: 'RequestRejectCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                tenantid :tenantId,
                otherdn: otherdn
              }
              if(!connId){
                _rejectCall = {
                  requestid: 'RequestRejectCall',
                  agentid: agentId,
                  thisdn: thisDN,
                  tenantid :tenantId,
                  otherdn: otherdn
                }
              }
              return JSON.stringify(_rejectCall)
            },

            /**
             * 电话保持
             *
             * @param agentId
             * @param thisDN
             * @param connID
             */
            getHoldJson: function (agentId, thisDN, tenantid,connId) {
              var _phoneHold = {
                requestid: 'RequestHoldCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                extension: {
                  r_hold: '1'
                },
                tenantid :tenantid
              }
              return JSON.stringify(_phoneHold)
            },

            /**
             * 电话取回
             *
             * @param agentId
             * @param thisDN
             */
            getRetrieveJson: function (agentId, thisDN, tenantid,connId) {
              var _retrieveCall = {
                requestid: 'RequestRetrieveCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                tenantid :tenantid
              }
              return JSON.stringify(_retrieveCall)
            },

            /**
             * 电话连接断开重连事件
             *
             * @param agentId
             * @param thisDN
             */
            getReconnectJson: function (agentId, thisDN, connId,tenantid) {
              var _reconnectCall = {
                requestid: 'RequestReconnectCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                tenantid :tenantid
              }
              return JSON.stringify(_reconnectCall)
            },

            /**
             * 电话单步转
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             * @param groupId -- 转座席组 or 转个人
             * @param userData 标记转组还是转个人//r_flag 1代表转坐席 2代表转坐席组,技能组或ivr transferTyped：0代表盲转1代表成功转
             * @returns {string}
             */
            getSingleTransferJson: function (otherId,otherDN,agentId,thisDN,connId,tenantid,userData) {
              var flag = '1'
              if(userData && userData.r_flag){
                flag = userData.r_flag;
              }
              var devicetype = 1
              if(flag == 1 ){
                devicetype = 2
              }
              var _singleTransfer = {
                requestid: 'RequestSingleStepTransfer',
                agentid: agentId,
                thisdn: thisDN,
                otherAgentId: otherId,
                otherdn: otherDN,
                connid: connId,
                devicetype:devicetype,
                userdata: {
                  r_agent: otherId,
                  r_group: otherId,
                  r_flag: flag,
                  r_function: 'singletransfer',
                  transferType:"0"
                },
                extension: {
                  r_transfer: '2'
                },
                tenantid :tenantid
              }
              _singleTransfer.userdata = Object.assign(_singleTransfer.userdata,userData)
              return JSON.stringify(_singleTransfer)
            },

            /**
             * 屏蔽客户声音
             *
             * @param
             * @param agentId
             * @param thisDN
             * @param otherDN
             * @param requestid
             * @returns {string}
             */
            /**取回*/
            getListenReconnect: function (agentId, thisDN, otherDn,tenantid) {
              var returnListenReconnect = ''
              var _ListenReconnectCall = {
                agentid: agentId,
                thisdn: thisDN,
                otherdn: otherDn,
                requestid: 'RequestListenReconnect',
                tenantid :tenantid
              }
              returnListenReconnect = JSON.stringify(_ListenReconnectCall)
              return returnListenReconnect
            },
            /**屏蔽*/
            getListenDisconnect: function (agentId, thisDN, otherDn,tenantid) {
              var returnListenDisconnect = ''
              var _ListenDisconnectCall = {
                agentid: agentId,
                thisdn: thisDN,
                otherdn: otherDn,
                requestid: 'RequestListenDisconnect',
                tenantid :tenantid
              }
              returnListenDisconnect = JSON.stringify(_ListenDisconnectCall)
              return returnListenDisconnect
            },

            /**
             * 电话保持
             *
             * @param
             * @param
             * @param otherDN
             * @param groupId -- 转座席组 or 转个人
             * @returns {string}
             */
            getHoldCall: function (agentId, thisDN,tenantid) {
              var returnJson = ''
              var _holdCall = {
                requestid: 'RequestHoldCall',
                agentid: agentId,
                thisdn: thisDN,
                extension: {
                  music: 'music/on_hold.wav',
                  r_hold: '8'
                },
                tenantid:tenantid
              }
              returnJson = JSON.stringify(_holdCall)
              return returnJson
            },

            /**
             * 监听单步转2
             *                   'r_agent' : otherId,
             *                   'r_function' : 'monitor',
             *                   'MonitorMode' : 'mute',
             *                   'MonitorScope' : 'call',
             */
            getMonitorTransferJson: function (agentId, thisDN, otherDN,tenantid) {
              // otherId = otherId.replace('#', '');
              var _monitorAgent = {
                requestid: 'RequestListenIn',
                agentid: agentId,
                thisdn: thisDN,
                otherdn: otherDN,
                userdata: {
                  Number: otherDN
                  //                    'Location' : 'HF_SIPSwitch_Agent1'
                },
                tenantid:tenantid
              }
              return JSON.stringify(_monitorAgent)
            },

            /**
             * 电话转IVR、满意度等
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getTransferJson: function (agentId, thisDN, otherDN, routeDN,tenantid) {
              var _transfer = {
                requestid: 'RequestSingleStepTransfer',
                agentid: agentId,
                thisdn: thisDN,
                otherdn: otherDN,
                userdata: {
                  r_transferRP: routeDN,
                  i_thisdn: thisDN,
                  r_flag: '3',
                  r_function: 'singletransfer'
                },
                extension: {
                  r_transfer: '1'
                },
                tenantid:tenantid
              }
              return JSON.stringify(_transfer)
            },

            /**
             * 新老平台转接
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getNewTransferOldJson: function (agentId, thisDN, otherDN, routeDN,tenantid) {
              var _transfer = {
                requestid: 'RequestSingleStepTransfer',
                agentid: agentId,
                thisdn: thisDN,
                otherdn: otherDN,
                userdata: {
                  r_transferRP: routeDN,
                  i_thisdn: thisDN,
                  r_flag: '4',
                  r_function: 'singletransfer'
                },
                tenantid:tenantid
              }
              return JSON.stringify(_transfer)
            },

            /**
             * 电话转任意IVR节点
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getTransferIVRJson: function (agentId,thisDN,otherDN,connId,tenantid,userData) {
              var _transferIVR = {
                requestid: 'RequestSingleStepTransfer',
                agentid: agentId,
                thisdn: thisDN,
                connid: connId,
                otherdn: otherDN,
                userdata: {
                  r_transferRP: "",
                  a_ivrnode_transfer: "",
                  r_flag: '3',
                  r_function: 'singletransfer'
                },
                tenantid:tenantid
              }
              if(userData){
                userData.r_flag = _transferIVR.userdata.r_flag;
                userData.r_function = _transferIVR.userdata.r_function;
              }
              _transferIVR.userdata= userData;
              return JSON.stringify(_transferIVR)
            },

            /**
             * 电话双步转
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getDoubleTransferJson: function (agentId, thisDN, otherDN, otherId, tenantid,connId,userData) {
              var flag = '1'
              if(userData && userData.r_flag){
                flag = userData.r_flag;
              }
              var _doubleTransfer = {
                requestid: 'RequestInitiateTransfer',
                agentid: agentId,
                otherAgentId: otherId,
                otherdn: otherDN,
                thisdn: thisDN,
                connid: connId,
                userdata: {
                  r_agent: otherId,
                  r_group: otherId,
                  r_flag: flag,
                  r_function: 'consult'
                },
                tenantid:tenantid
              }
              if(userData){
                userData.r_agent = _doubleTransfer.userdata.r_agent;
                userData.r_group = _doubleTransfer.userdata.r_group;
                userData.r_flag = _doubleTransfer.userdata.r_flag;
                userData.r_function = _doubleTransfer.userdata.r_function;
              }
              _doubleTransfer.userdata = userData;
              return JSON.stringify(_doubleTransfer)
            },

            /**
             * 完成电话转接
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             * @param connId
             */
            getCompleteTransferJson: function (agentId, thisDN, otherDN, connId,tenantid) {
              var _completeTransfer = {
                requestid: 'RequestCompleteTransfer',
                agentid: agentId,
                otherdn: otherDN,
                thisdn: thisDN,
                connid: connId,
                tenantid :tenantid
              }
              return JSON.stringify(_completeTransfer)
            },

            /**
             * 单步会议
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getSingleStepConferenceJson: function (agentId,thisDN,otherID,otherDN,userData,connId,tenantid) {
              if(!userData.con_flag){
                userData.con_flag = '0'
              }
              var devicetype = 1
              if(userData.con_flag == 0 ){
                devicetype = 2
              }
              var _doubleConference = {
                requestid: 'RequestSingleStepConference',
                agentid: agentId,
                otherAgentId : otherID,
                otherdn: otherDN,
                thisdn: thisDN,
                connid: connId,
                userdata: userData,
                tenantid:tenantid,
                devicetype:devicetype
              }
              return JSON.stringify(_doubleConference)
            },

            /**
             * 三方会议
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getDoubleConferenceJson: function (agentId, thisDN, otherDN,tenantid) {
              var _doubleConference = {
                requestid: 'RequestInitiateConference',
                agentid: agentId,
                otherdn: otherDN,
                thisdn: thisDN,
                tenantid:tenantid
              }
              return JSON.stringify(_doubleConference)
            },

            /**
             * 会议完成
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getCompleteConferenceJson: function (agentId, thisDN, otherDN, connId,tenantid) {
              var _completeConference = {
                requestid: 'RequestCompleteConference',
                agentid: agentId,
                otherdn: otherDN,
                thisdn: thisDN,
                connid: connId,
                tenantid:tenantid
              }
              return JSON.stringify(_completeConference)
            },

            /**
             *
             *
             * @param agentId
             * @param thisDN
             * @param connID
             * @param otherConnID
             */
            getSwapCall: function (agentId, thisDN, connID, otherConnID,tenantid) {
              var _swapCall = {
                requestid: 'RequestSwapCall',
                agentid: agentId,
                thisdn: thisDN,
                connid: connID,
                otherconnid: otherConnID,
                tenantid:tenantid
              }
              return JSON.stringify(_swapCall)
            },

            /**
             * 电话重连
             *
             * @param agentId
             * @param thisDN
             */
            getReconnectCallJson: function (agentId, thisDN,tenantid,connid) {
              var _reconnectCall = {
                requestid: 'RequestReconnectCall',
                agentid: agentId,
                thisdn: thisDN,
                tenantid:tenantid,
                connid :connid
              }
              return JSON.stringify(_reconnectCall)
            },

            /**
             * 电话强插
             *
             * @param agentId
             * @param thisDN
             */
            getBargeInJson: function (agentId, thisDN,tenantid) {
              var _bargeIn = {
                requestid: 'RequestBargeIn',
                agentid: agentId,
                thisdn: thisDN,
                userdata: {
                  MonitorMode: 'connect'
                },
                tenantid:tenantid
              }
              return JSON.stringify(_bargeIn)
            },

            /**
             * 电话强拆
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getForceBreakJson: function (agentId, thisDN,otherID, otherDN,tenantid) {
              var _forceBreak = {
                requestid: 'RequestForceBreak',
                agentid: agentId,
                otherAgentId:otherID,
                otherdn: otherDN,
                thisdn: thisDN,
                tenantid :tenantid
              }
              return JSON.stringify(_forceBreak)
            },

            /**
             * 取消监听
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getCancelSupervisionMonitoring: function (agentId, thisDN, otherDN,tenantid,connId) {
              var _cancelSupervisionMonitoring = {
                requestid: 'RequestCancelSupervisionMonitoring',
                agentid: agentId,
                otherdn: otherDN,
                thisdn: thisDN,
                tenantid :tenantid,
                connid: connId
              }
              if(!connId){
                _cancelSupervisionMonitoring = {
                  requestid: 'RequestCancelSupervisionMonitoring',
                  agentid: agentId,
                  otherdn: otherDN,
                  thisdn: thisDN,
                  tenantid :tenantid
                }
              }
              return JSON.stringify(_cancelSupervisionMonitoring)
            },

            /**
             * 电话监听
             *
             * @param agentId
             * @param thisDN
             * @param otherDN
             */
            getMonitorAgentJson: function (agentId, thisDN, otherID,otherDN,tenantid) {
              var _monitorAgent = {
                requestid: 'RequestListenIn',
                agentid: agentId,
                otherAgentId :otherID,
                otherdn: otherDN,
                thisdn: thisDN,
                tenantid :tenantid
              }
              return JSON.stringify(_monitorAgent)
            },

            /**
             * 更新随路数据
             *
             * @param agentId
             * @param thisDN
             * @param userData
             */
            updateUserDataJson: function (agentId, thisDN, tenantid,userData,connId) {
              var _updateUserData = {
                requestid: 'RequestUpdateUserData',
                agentid: agentId,
                thisdn: thisDN,
                userdata: userData,
                tenantid :tenantid,
                connid:connId
              }
              return JSON.stringify(_updateUserData)
            },
            /**
             * 二次拨号
             *
             * @param agentId
             * @param thisDN
             * @param dtmfdigits
             */
            sendDtmfJson : function (dtmfdigits, connid, agentId, thisDN, tenantId) {
              var _sendDtmf = {
                requestid : 'RequestSendDtmf',
                agentid : agentId,
                thisdn : thisDN,
                dtmfdigits : dtmfdigits,
                connid :connid,
                tenantid :tenantId
              };
              return JSON.stringify(_sendDtmf);
            },
            /**
             * 电话静音
             *
             * @param agentId
             * @param thisDN
             * @param connID
             */
            getMuteJson : function (agentId, thisDN, connID, tenantId) {
              var _phoneMute = {
                requestid : 'RequestMute',
                agentid : agentId,
                thisdn : thisDN,
                connid : connID,
                tenantid :tenantId
              }
              return JSON.stringify(_phoneMute);
            },
            /**
             * 电话取消静音
             *
             * @param agentId
             * @param thisDN
             * @param connID
             */
            getUnmuteJson : function (agentId, thisDN, connID,tenantId) {
              var _phoneMute = {
                requestid : 'RequestUnmute',
                agentid : agentId,
                thisdn : thisDN,
                connid : connID,
                tenantid :tenantId
              }
              return JSON.stringify(_phoneMute);
            },
            /**
             * 开始音频传输
             *
             * @param agentId
             * @param thisDN
             * @param connID
             */
            getStartMediaJson : function (agentId, thisDN, connID,tenantId,userData) {
              var _startMedia = {
                requestid : 'RequestStartMedia',
                agentid : agentId,
                thisdn : thisDN,
                connid : connID,
                tenantid :tenantId,
                userdata : userData
              };
              return JSON.stringify(_startMedia);
            },
            /**
             * 停止音频传输
             *
             * @param agentId
             * @param thisDN
             * @param connID
             */
            getStopMediaJson : function (agentId, thisDN, connID,tenantId,userData) {
              var _stopMedia = {
                requestid : 'RequestStopMedia',
                agentid : agentId,
                thisdn : thisDN,
                connid : connID,
                tenantid :tenantId,
                userdata : userData
              }
              return JSON.stringify(_stopMedia);
            }
          }


          exports.message = Message
        })()


        /***/ }),
      /* 7 */
      /***/ (function(module, exports, __webpack_require__) {

        var __WEBPACK_AMD_DEFINE_RESULT__;/*!
 * EventEmitter v5.2.4 - git.io/ee
 * Unlicense - http://unlicense.org/
 * Oliver Caldwell - http://oli.me.uk/
 * @preserve
 */

        ;(function (exports) {
          'use strict';

          /**
           * Class for managing events.
           * Can be extended to provide event functionality in other classes.
           *
           * @class EventEmitter Manages event registering and emitting.
           */
          function EventEmitter() {}

          // Shortcuts to improve speed and size
          var proto = EventEmitter.prototype;
          var originalGlobalValue = exports.EventEmitter;

          /**
           * Finds the index of the listener for the event in its storage array.
           *
           * @param {Function[]} listeners Array of listeners to search through.
           * @param {Function} listener Method to look for.
           * @return {Number} Index of the specified listener, -1 if not found
           * @api private
           */
          function indexOfListener(listeners, listener) {
            var i = listeners.length;
            while (i--) {
              if (listeners[i].listener === listener) {
                return i;
              }
            }

            return -1;
          }

          /**
           * Alias a method while keeping the context correct, to allow for overwriting of target method.
           *
           * @param {String} name The name of the target method.
           * @return {Function} The aliased method
           * @api private
           */
          function alias(name) {
            return function aliasClosure() {
              return this[name].apply(this, arguments);
            };
          }

          /**
           * Returns the listener array for the specified event.
           * Will initialise the event object and listener arrays if required.
           * Will return an object if you use a regex search. The object contains keys for each matched event. So /ba[rz]/ might return an object containing bar and baz. But only if you have either defined them with defineEvent or added some listeners to them.
           * Each property in the object response is an array of listener functions.
           *
           * @param {String|RegExp} evt Name of the event to return the listeners from.
           * @return {Function[]|Object} All listener functions for the event.
           */
          proto.getListeners = function getListeners(evt) {
            var events = this._getEvents();
            var response;
            var key;

            // Return a concatenated array of all matching events if
            // the selector is a regular expression.
            if (evt instanceof RegExp) {
              response = {};
              for (key in events) {
                if (events.hasOwnProperty(key) && evt.test(key)) {
                  response[key] = events[key];
                }
              }
            }
            else {
              response = events[evt] || (events[evt] = []);
            }

            return response;
          };

          /**
           * Takes a list of listener objects and flattens it into a list of listener functions.
           *
           * @param {Object[]} listeners Raw listener objects.
           * @return {Function[]} Just the listener functions.
           */
          proto.flattenListeners = function flattenListeners(listeners) {
            var flatListeners = [];
            var i;

            for (i = 0; i < listeners.length; i += 1) {
              flatListeners.push(listeners[i].listener);
            }

            return flatListeners;
          };

          /**
           * Fetches the requested listeners via getListeners but will always return the results inside an object. This is mainly for internal use but others may find it useful.
           *
           * @param {String|RegExp} evt Name of the event to return the listeners from.
           * @return {Object} All listener functions for an event in an object.
           */
          proto.getListenersAsObject = function getListenersAsObject(evt) {
            var listeners = this.getListeners(evt);
            var response;

            if (listeners instanceof Array) {
              response = {};
              response[evt] = listeners;
            }

            return response || listeners;
          };

          function isValidListener (listener) {
            if (typeof listener === 'function' || listener instanceof RegExp) {
              return true
            } else if (listener && typeof listener === 'object') {
              return isValidListener(listener.listener)
            } else {
              return false
            }
          }

          /**
           * Adds a listener function to the specified event.
           * The listener will not be added if it is a duplicate.
           * If the listener returns true then it will be removed after it is called.
           * If you pass a regular expression as the event name then the listener will be added to all events that match it.
           *
           * @param {String|RegExp} evt Name of the event to attach the listener to.
           * @param {Function} listener Method to be called when the event is emitted. If the function returns true then it will be removed after calling.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.addListener = function addListener(evt, listener) {
            if (!isValidListener(listener)) {
              throw new TypeError('listener must be a function');
            }

            var listeners = this.getListenersAsObject(evt);
            var listenerIsWrapped = typeof listener === 'object';
            var key;

            for (key in listeners) {
              if (listeners.hasOwnProperty(key) && indexOfListener(listeners[key], listener) === -1) {
                listeners[key].push(listenerIsWrapped ? listener : {
                  listener: listener,
                  once: false
                });
              }
            }

            return this;
          };

          /**
           * Alias of addListener
           */
          proto.on = alias('addListener');

          /**
           * Semi-alias of addListener. It will add a listener that will be
           * automatically removed after its first execution.
           *
           * @param {String|RegExp} evt Name of the event to attach the listener to.
           * @param {Function} listener Method to be called when the event is emitted. If the function returns true then it will be removed after calling.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.addOnceListener = function addOnceListener(evt, listener) {
            return this.addListener(evt, {
              listener: listener,
              once: true
            });
          };

          /**
           * Alias of addOnceListener.
           */
          proto.once = alias('addOnceListener');

          /**
           * Defines an event name. This is required if you want to use a regex to add a listener to multiple events at once. If you don't do this then how do you expect it to know what event to add to? Should it just add to every possible match for a regex? No. That is scary and bad.
           * You need to tell it what event names should be matched by a regex.
           *
           * @param {String} evt Name of the event to create.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.defineEvent = function defineEvent(evt) {
            this.getListeners(evt);
            return this;
          };

          /**
           * Uses defineEvent to define multiple events.
           *
           * @param {String[]} evts An array of event names to define.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.defineEvents = function defineEvents(evts) {
            for (var i = 0; i < evts.length; i += 1) {
              this.defineEvent(evts[i]);
            }
            return this;
          };

          /**
           * Removes a listener function from the specified event.
           * When passed a regular expression as the event name, it will remove the listener from all events that match it.
           *
           * @param {String|RegExp} evt Name of the event to remove the listener from.
           * @param {Function} listener Method to remove from the event.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.removeListener = function removeListener(evt, listener) {
            var listeners = this.getListenersAsObject(evt);
            var index;
            var key;

            for (key in listeners) {
              if (listeners.hasOwnProperty(key)) {
                index = indexOfListener(listeners[key], listener);

                if (index !== -1) {
                  listeners[key].splice(index, 1);
                }
              }
            }

            return this;
          };

          /**
           * Alias of removeListener
           */
          proto.off = alias('removeListener');

          /**
           * Adds listeners in bulk using the manipulateListeners method.
           * If you pass an object as the first argument you can add to multiple events at once. The object should contain key value pairs of events and listeners or listener arrays. You can also pass it an event name and an array of listeners to be added.
           * You can also pass it a regular expression to add the array of listeners to all events that match it.
           * Yeah, this function does quite a bit. That's probably a bad thing.
           *
           * @param {String|Object|RegExp} evt An event name if you will pass an array of listeners next. An object if you wish to add to multiple events at once.
           * @param {Function[]} [listeners] An optional array of listener functions to add.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.addListeners = function addListeners(evt, listeners) {
            // Pass through to manipulateListeners
            return this.manipulateListeners(false, evt, listeners);
          };

          /**
           * Removes listeners in bulk using the manipulateListeners method.
           * If you pass an object as the first argument you can remove from multiple events at once. The object should contain key value pairs of events and listeners or listener arrays.
           * You can also pass it an event name and an array of listeners to be removed.
           * You can also pass it a regular expression to remove the listeners from all events that match it.
           *
           * @param {String|Object|RegExp} evt An event name if you will pass an array of listeners next. An object if you wish to remove from multiple events at once.
           * @param {Function[]} [listeners] An optional array of listener functions to remove.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.removeListeners = function removeListeners(evt, listeners) {
            // Pass through to manipulateListeners
            return this.manipulateListeners(true, evt, listeners);
          };

          /**
           * Edits listeners in bulk. The addListeners and removeListeners methods both use this to do their job. You should really use those instead, this is a little lower level.
           * The first argument will determine if the listeners are removed (true) or added (false).
           * If you pass an object as the second argument you can add/remove from multiple events at once. The object should contain key value pairs of events and listeners or listener arrays.
           * You can also pass it an event name and an array of listeners to be added/removed.
           * You can also pass it a regular expression to manipulate the listeners of all events that match it.
           *
           * @param {Boolean} remove True if you want to remove listeners, false if you want to add.
           * @param {String|Object|RegExp} evt An event name if you will pass an array of listeners next. An object if you wish to add/remove from multiple events at once.
           * @param {Function[]} [listeners] An optional array of listener functions to add/remove.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.manipulateListeners = function manipulateListeners(remove, evt, listeners) {
            var i;
            var value;
            var single = remove ? this.removeListener : this.addListener;
            var multiple = remove ? this.removeListeners : this.addListeners;

            // If evt is an object then pass each of its properties to this method
            if (typeof evt === 'object' && !(evt instanceof RegExp)) {
              for (i in evt) {
                if (evt.hasOwnProperty(i) && (value = evt[i])) {
                  // Pass the single listener straight through to the singular method
                  if (typeof value === 'function') {
                    single.call(this, i, value);
                  }
                  else {
                    // Otherwise pass back to the multiple function
                    multiple.call(this, i, value);
                  }
                }
              }
            }
            else {
              // So evt must be a string
              // And listeners must be an array of listeners
              // Loop over it and pass each one to the multiple method
              i = listeners.length;
              while (i--) {
                single.call(this, evt, listeners[i]);
              }
            }

            return this;
          };

          /**
           * Removes all listeners from a specified event.
           * If you do not specify an event then all listeners will be removed.
           * That means every event will be emptied.
           * You can also pass a regex to remove all events that match it.
           *
           * @param {String|RegExp} [evt] Optional name of the event to remove all listeners for. Will remove from every event if not passed.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.removeEvent = function removeEvent(evt) {
            var type = typeof evt;
            var events = this._getEvents();
            var key;

            // Remove different things depending on the state of evt
            if (type === 'string') {
              // Remove all listeners for the specified event
              delete events[evt];
            }
            else if (evt instanceof RegExp) {
              // Remove all events matching the regex.
              for (key in events) {
                if (events.hasOwnProperty(key) && evt.test(key)) {
                  delete events[key];
                }
              }
            }
            else {
              // Remove all listeners in all events
              delete this._events;
            }

            return this;
          };

          /**
           * Alias of removeEvent.
           *
           * Added to mirror the node API.
           */
          proto.removeAllListeners = alias('removeEvent');

          /**
           * Emits an event of your choice.
           * When emitted, every listener attached to that event will be executed.
           * If you pass the optional argument array then those arguments will be passed to every listener upon execution.
           * Because it uses `apply`, your array of arguments will be passed as if you wrote them out separately.
           * So they will not arrive within the array on the other side, they will be separate.
           * You can also pass a regular expression to emit to all events that match it.
           *
           * @param {String|RegExp} evt Name of the event to emit and execute listeners for.
           * @param {Array} [args] Optional array of arguments to be passed to each listener.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.emitEvent = function emitEvent(evt, args) {
            var listenersMap = this.getListenersAsObject(evt);
            var listeners;
            var listener;
            var i;
            var key;
            var response;

            for (key in listenersMap) {
              if (listenersMap.hasOwnProperty(key)) {
                listeners = listenersMap[key].slice(0);

                for (i = 0; i < listeners.length; i++) {
                  // If the listener returns true then it shall be removed from the event
                  // The function is executed either with a basic call or an apply if there is an args array
                  listener = listeners[i];

                  if (listener.once === true) {
                    this.removeListener(evt, listener.listener);
                  }

                  response = listener.listener.apply(this, args || []);

                  if (response === this._getOnceReturnValue()) {
                    this.removeListener(evt, listener.listener);
                  }
                }
              }
            }

            return this;
          };

          /**
           * Alias of emitEvent
           */
          proto.trigger = alias('emitEvent');

          /**
           * Subtly different from emitEvent in that it will pass its arguments on to the listeners, as opposed to taking a single array of arguments to pass on.
           * As with emitEvent, you can pass a regex in place of the event name to emit to all events that match it.
           *
           * @param {String|RegExp} evt Name of the event to emit and execute listeners for.
           * @param {...*} Optional additional arguments to be passed to each listener.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.emit = function emit(evt) {
            var args = Array.prototype.slice.call(arguments, 1);
            return this.emitEvent(evt, args);
          };

          /**
           * Sets the current value to check against when executing listeners. If a
           * listeners return value matches the one set here then it will be removed
           * after execution. This value defaults to true.
           *
           * @param {*} value The new value to check for when executing listeners.
           * @return {Object} Current instance of EventEmitter for chaining.
           */
          proto.setOnceReturnValue = function setOnceReturnValue(value) {
            this._onceReturnValue = value;
            return this;
          };

          /**
           * Fetches the current value to check against when executing listeners. If
           * the listeners return value matches this one then it should be removed
           * automatically. It will return true by default.
           *
           * @return {*|Boolean} The current value to check for or the default, true.
           * @api private
           */
          proto._getOnceReturnValue = function _getOnceReturnValue() {
            if (this.hasOwnProperty('_onceReturnValue')) {
              return this._onceReturnValue;
            }
            else {
              return true;
            }
          };

          /**
           * Fetches the events object and creates one if required.
           *
           * @return {Object} The events storage object.
           * @api private
           */
          proto._getEvents = function _getEvents() {
            return this._events || (this._events = {});
          };

          /**
           * Reverts the global {@link EventEmitter} to its previous value and returns a reference to this version.
           *
           * @return {Function} Non conflicting EventEmitter class.
           */
          EventEmitter.noConflict = function noConflict() {
            exports.EventEmitter = originalGlobalValue;
            return EventEmitter;
          };

          // Expose the class either via AMD, CommonJS or the global object
          if (true) {
            !(__WEBPACK_AMD_DEFINE_RESULT__ = (function () {
              return EventEmitter;
            }).call(exports, __webpack_require__, exports, module),
            __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
          }
          else if (typeof module === 'object' && module.exports){
            module.exports = EventEmitter;
          }
          else {
            exports.EventEmitter = EventEmitter;
          }
        }(this || {}));

        /***/ }),
      /* 8 */
      /***/ (function(module, exports) {

        /**
         * 座席会话状态信息
         */
        ; (function () {
          'use strict'
          exports.sessions = {
            currentAgent: {
              agentId: '',
              tenantId: '',
              placeId: '',
              extension: '',
              userId: '',
              agentName: '',
              agentType: '4', // 座席类型
              channelType: '', // 渠道类型
              mediaType: '', // 媒体类型
              captainLevel: -1, // 是否班长座席
              serviceAbility: -1 // 服务能力
            }
          }
        })()


        /***/ })
      /******/ ]);
