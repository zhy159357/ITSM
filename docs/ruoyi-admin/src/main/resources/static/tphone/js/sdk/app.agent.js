/*
 * agent.js
 * Model Agent
*/
Agent = (function() {
    //---------------- BEGIN MODULE SCOPE VARIABLES --------------
    var
        currentAgent = {
            userId      : "",
            agentName   : "",
            agentId     : "",
            tenantId    : "",
            placeId     : "",
            agentType   : "4",
            extension   : "",
            chatMediaType : "", //
            channelType : "",  //支持的渠道信息
            captainLevel: -1, //是否班长座席
            serviceAbility: -1,
            skillList: null
        },

        actionFlag = "",

        //语音、视频、电话状态信息
        callInfo = {
            targetAgoraDn      : '',
            globalUUID           : '',

            //当前通话中的信息
            callingUID           : '',
            callingChannelName  : '',
            callingOtherAgoraDn : '',
            callingSessionId     : '',

            channelType : null,
            callTag : '',
            userid       : '',
            userAgoraDn : '',
            userImAccount : '',
            callMediaType : '',
            callType      : '',
            channelType : '',
            ext            : null
        },

        //座席im账号信息
       imAccountInfo = {
            imAccount: '',
            password : '',
            mgwAccount: '',
            serviceName : '',
            host : '',
            port : -1
       },

        //座席声网账号信息
       agoraAcountInfo = {
            agoraToken : '',
            agorApdTime : '',
            agoradn : ''
       },

       genesysAcountInfo = {
            thisdn : "300052",
            password : "1234",
            sipServer : "222.73.213.186",
            port : 39360,
            agoraToken : '',
            agorApdTime :''
       },

       getCurrentAgent, setCurrentAgent, getImAccountInfo, getAgoraAcountInfo, initAgent, getAgentId, getCallInfo,
       setCallInfo, resetCallInfo, getActionFlag, setActionFlag, getGenesysAcountInfo, setGenesysAcountInfo;

    //------------------- BEGIN PUBLIC METHODS ------------------
    //------------------- BEGIN UTILITY METHODS -----------------
        getActionFlag = function() {
            return actionFlag;
        };

        setActionFlag = function(flag) {
            actionFlag = flag;
        };

        getCurrentAgent = function() {
            return currentAgent;
        };

        setCurrentAgent = function(currentAgentInfo) {
            currentAgent = currentAgentInfo;
        };

        // End DOM method /setCurrentAgentInfo/
        getCallInfo=function() {
            return callInfo;
        };

        setCallInfo=function(sessionId, targetAgoraDn, callingUID, channelName, otherAgoraDn, channelType, callTag) {
            callInfo.callingSessionId = sessionId,

            callInfo.targetAgoraDn = targetAgoraDn;
            //当前通话中的信息
            callInfo.callingUID = callingUID,
            callInfo.callingChannelName = channelName,
            callInfo.callingOtherAgoraDn = otherAgoraDn,

            callInfo.channelType = channelType,
            callInfo.callTag = callTag;
        };

        //恢复初始值
        resetCallInfo = function() {
            callInfo.callNo = '';
            callInfo.callId = '';
            callInfo.callingType = '';
            callInfo.mobilPhone = '';

            //当前通话中的信息
            callInfo.callingUID = '',
            callInfo.callingChannelName = '',
            callInfo.callingOtherAgoraDn = '',
            callInfo.callingSessionId = '',
            callInfo.channelType = null,
            callInfo.callTag = "";

            //user info
            callInfo.userid = "";
            callInfo.userAgoraDn = "";
            callInfo.userImAccount = "";
        };

        getImAccountInfo = function() {
            return imAccountInfo;
        };

        getAgoraAcountInfo = function() {
            return agoraAcountInfo;
        };

        getGenesysAcountInfo = function() {
            return genesysAcountInfo;
        };

        setGenesysAcountInfo = function(thisdn, password, sipServer, port) {

        };

        // Begin DOM method /setCurrentAgentInfo/
        initModule = function(userId, agentId, tenantId, channelType, media) {
             currentAgent["userId"] = userId;
             currentAgent["agentId"] = agentId;
             currentAgent["tenantId"] = tenantId;
             currentAgent["chanelType"] = channelType;
             currentAgent["media"] = media;
        }
        // Begin DOM method /setCurrentAgentInfo/

    //------------------- BEGIN PUBLIC METHODS ------------------
    //------------------- BEGIN UTILITY METHODS -----------------

    // return public methods
      return {
        getCurrentAgent : getCurrentAgent,
        setCurrentAgent : setCurrentAgent,
        getImAccountInfo : getImAccountInfo,
        getAgoraAcountInfo : getAgoraAcountInfo,
        setCallInfo : setCallInfo,
        getCallInfo : getCallInfo,
        resetCallInfo : resetCallInfo,
        getGenesysAcountInfo : getGenesysAcountInfo,
        setGenesysAcountInfo : setGenesysAcountInfo,
        initModule   : initModule,
        getActionFlag : getActionFlag,
        setActionFlag : setActionFlag
      };
    //------------------- END PUBLIC METHODS ---------------------

}());
