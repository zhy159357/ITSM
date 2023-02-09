'use strict';

/**
 * agent proxy server后台消息回调
 */
var CustomizationPhoneAction= {
  /**
   * 锦城外呼
   *phoneNum:外呼号码，displayNum:出局号码,vendor:厂商,callPrefix:呼叫前缀
   */
  makeCall : function (phoneNum,displayNum,vendor,callPrefix) {
    var userdata = new Object();
    userdata.type = "1";
    userdata.displayNum = displayNum
    userdata.vendor = vendor
    userdata.callPrefix = callPrefix
    document.getElementById("callNum"+phoneUiKit.agent.agentId).value=phoneNum
    phoneSDK.makeCall('',phoneNum,'','','',userdata);
    $("#phone_num"+phoneUiKit.agent.agentId).html(phoneNum)
  },
};
