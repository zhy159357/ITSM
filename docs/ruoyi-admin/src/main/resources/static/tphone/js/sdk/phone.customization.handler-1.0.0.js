'use strict';

/**
 * agent proxy server后台消息回调
 */
var CustomizationPhoneMsgHandler = {
    mediaTransferOpt:false,
    mediaTransferOptTip:"init",
    userdata:{},
  taketimestart:0,

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
    },

    /**
     * 坐席注册Tproxyserver成功
     * @param obj
     */
    registerHandler: function (obj) {
        if (!obj) {
            return;
        }
    },

    /**
     * 座席签入回调
     *
     * @paran obj
     */
    loginHandler: function (obj) {
    },

    /**
     * 座席签出回调
     *
     * @param obj
     */
    logoutHandler: function (obj) {
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
    },
    /**
     * 座席小休回调
     *
     * @paran obj
     */
    notReadyHandler: function (obj) {
    },
    /**
     * 话后处理回调
     *
     * @paran obj
     */
    afterCallWorkHandler: function (obj) {
      CustomizationPhoneMsgHandler.mediaTransferOpt = false
      if(CustomizationPhoneMsgHandler.taketimestart !=0){
        var curtime = new Date().getTime()
        var talkTime = parseInt((curtime-CustomizationPhoneMsgHandler.taketimestart)/1000)
        console.log("通话时长："+talkTime)
      }
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
    },

    /**
     * 座席外呼拨号中
     *
     * @param obj
     */
    chatDialingHandler: function (obj) {
    },
  /**
   * 座席咨询拨号中事件
   *
   * @param obj
   */
  chatConsultDialingHandler: function (obj) {

  },

    /**
     * 电话建立事件回调处理
     */
    /*establishedHandler: function (obj) {
      if(CustomizationPhoneMsgHandler.taketimestart ==0){
        CustomizationPhoneMsgHandler.taketimestart = new Date().getTime()
      }
    },*/

    /**
     * 电话建立事件回调处理
     */
    establishedHandler: function (obj) {
        if(CustomizationPhoneMsgHandler.taketimestart ==0){
            CustomizationPhoneMsgHandler.taketimestart = new Date().getTime()
        }
        var userdata = {};
        try {

            userdata = JSON.parse(obj.userdata)[0];
            console.log(JSON.stringify(userdata));
            if(userdata && userdata.g_ani){
                var aa=userdata.g_ani;
                open_telBiz_page(userdata);
            }

        }
        catch(e) {
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
          $("#logs").append("<br>"+'json parse error ' + e);
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
    },

    /**
     * 系统随路数据
     */
    attachedDataChangedHandler: function (obj) {
      },

    /**
     * 三方验密会话成员离开
     */
    partyDeletedHandler: function (obj) {
    },

    /**
     * 会话成员改变
     */
    partyChangedHandler: function (obj) {
        // 先咨询 后会议 或者 先咨询 后转移 会话成员改变
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

    },

    /**
     * 超时弃话
     *
     */
    revokedHandler: function (obj) {

    },

    /**
     * 主动弃话
     *
     */
    abandonedHandler: function (obj) {

    },

    /**
     * 挂机处理
     * @param obj
     */
    releasedHandler: function (obj) {
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
          alert("账号不存在");
        }
        // {"ret":506,"data":"","errmsg":"Request received in an invalid state","responseid":"RequestAgentReady"} 分机不正确
        else if (506 === obj.ret) {
        }
        // 分机占用中 {"ret":1141,"data":"","errmsg":"Request incompatible with object","responseid":"RequestAgentLogin"}
        else if (1141 === obj.ret) {
        }
        // {"ret":9996,"data":"","errmsg":"Sign-in number is already registered with agent boc_5100186","responseid":"RequestRegisterDN"}
        else if (9996 === obj.ret) {
          alert("账号已在别处登录");
          phoneSDK.disconnectFromTProxy();
        }
        else if (1001 === obj.ret) {
          alert("呼叫频繁，请稍后再呼叫");
        }
        else if (10000 === obj.ret) {
          alert("CPD Connect Failed");
        }
        else if (10001 === obj.ret) {
          alert("CPD ACK Timeout");
        }
        else {

        }
    }
};
