var Constants = {

    Display: {
        show: 1,
        hide: -1
    },

    Chat_Media_Type : {
        chat : "chat",
        email : "email",
        facebook : "facebook",
        twitter : "twitter"
    },

    EVENT_NAME : {
        AgentStatusChanged   : 'EventStateChanged',           //座席状态和通话状态改变
        sessionStatusChanged : 'EventSessionStatusChanged',   //会话状态改变
        chatMsg              : 'EventChatMsg',                //会话消息事件
        getSkillGroupLst     : 'EventSkillGroupListResponse', //请求座席组列表响应事件
        getAgentListResponse : 'EventAgentListResponse'       //请求空闲座席列表响应事件
    },

    CALL_TYPE : {
        XCALL    : '00',    //语音通话
        XCHAT    : '01',    //消息
        XVIDEO   : '02',    //视频电话
        XPHONE   : '03'     //PSTN电话
    },

    CALL_TAG : {
        DEFAULT : 0,
        SESSION_CALL : 1,
        SESSION_VIDEO : 4,
        PSTN_CALL : 8,   //落地电话
        APP_CALL : 16    //回呼app
    },

    CHANNEL_TYPE : {
        APP       : '01',   //娓娓客服
        XCALL     : '02',   //电话(PSTN电话)
        XWECHAT   : '03',   //微信
        YIXIN     : '04',   //易信
        WEBCHAT   : '05',   //网聊
        SMS       : '06',   //短信
        EMAIL     : '07',   //邮件
        LEAVEWORD : '08'    //留言
    },

    PHONE_STATUS : {
        DOING_CALL            : 0,
        CALLING               : 1,
        ACCEPT                : 2,
        OUT_CALL_ESTABLISHED  : 3,
        OUT_CALL_RELEASED     : 4,
        OUT_CALL_STATUS_BOUND : 5,
        RINGING               : 5,
        IN_CALL_ESTABLISHED   : 6,
        IN_CALL_RELEASED      : 7,
        CALL_STATUS_BOUND     : 8,
        HELD_CALL              : 9,
        RETRIEVE_CALL          : 10,
        MUTE_DEAF_ON          : 11,
        MUTE_DEAF_OFF         : 12,
        MUTE_ON               : 13,
        MUTE_OFF              : 14,
        JOINCHANNEL			  : 15
    },

    CALL_STATUS : {
        STATE_CALL_IVR             : 0,
        STATE_CALL_QUEUED          : 1,
        STATE_CALL_DIVERTED        : 2,
        STATE_CALL_RINGING         : 3,
        STATE_CALL_ESTABLISHED     : 4,
        STATE_CALL_RELEASED        : 5,
        STATE_CALL_HELD            : 6,
        STATE_CALL_DIALING         : 7,
        STATE_CALL_CONSULT_DIALING : 8,
        STATE_CALL_CONSULT         : 9,
        STATE_CALL_CONFERENCE      : 10,
        STATE_CALL_MONITORING      : 11,
        STATE_CALL_MONITORED       : 12,                // agent call status is monitored!
        STATE_CALL_INTRUDING       : 13,                // the monitor call staus is intruding a call
        STATE_CALL_INTRUDED        : 14,                // agent call status is intruded!
        STATE_CALL_SINGLETRANSFER  : 15,                // 请求单转
        STATE_CALL_SINGLERELEASE   : 16,                // 单转挂机
        STATE_CALL_REQUESTCONFERENCE : 17,              // 请求会议
        STATE_CALL_CONSULTED	     : 18,
        STATE_CALL_TO_CONSULTING     : 19,
        STATE_CALL_MUTE_DEAF_ON	     : 20,
        STATE_CALL_MUTE_DEAF_OFF	 : 21,
        STATE_CALL_TO_DISCONNECTED	 : 22,
        STATE_CALL_TO_CONFERENCE	 : 23,
        STATE_CALL_TO_MONITOR	     : 24,
        STATE_CALL_TO_MUTED	         : 25,
        STATE_CALL_TO_UNMUTE	     : 26,
        STATE_SESSION_EXIT           : 27              //会话结束
    },

    AGENT_STATUS : {
        STATE_AGENT_LOGOUT                : 0,
        STATE_AGENT_LOGGEDIN              : 1,
        STATE_AGENT_READY                 : 2,
        STATE_AGENT_NOTREADY              : 3,
        STATE_AGENT_ACW                   : 4,
        STATE_AGENT_NOTREADY_FOR_NEXTCALL : 5,
        STATE_AGENT_HAS_SESSIONS          : 6,
        STATE_AGENT_NO_SESSIONS           : 7
    },

    SESSION_STATUS : {
        STATE_SESSION_IVR         : 0,
        STATE_SESSION_QUEUED      : 1,
        STATE_SESSION_DIVERTED    : 2,
        STATE_SESSION_RINGING     : 3,
        STATE_SESSION_ESTABLISHED : 4,
        STATE_SESSION_RELEASED    : 5,
        STATE_SESSION_HELD        : 6
    },

    FLAG_MAP : {
        FLAG_DEFAULT : 0,
        FLAG_LOGIN 	  : 1,
        FLAG_RINGING    : 2,
        FLAG_MAKECALL   : 4,
        FLAG_TRANSFER   : 8,
        FLAG_CONFERENCE : 16,
        FLAG_HOLD        : 32
    },

    /**
     * XServer XStatServer MSGServer APhone 发送来的事件名称
     */
    eventNameMap : {
        EventAgentStatusChanged     : 'EventAgentStatusChanged',
        EventCallStatusChanged      : 'EventCallStatusChanged',
        EventSessionStatusChanged   : 'SessionStatusChanged',
        EventAgentCstCommunication  : 'AgentCstCommunication',
        EventAgentStatusResponse    : 'AgentStatusResponse',
        EventIdleAgentListResponse  : 'IdleAgentListResponse',
        EventskillGroupListResponse : 'skillGroupListResponse',
        EventAPhoneConnectFailed    : 'EventConnectFailed',
        EventAPhoneClosed	        : 'EventAPhoneClosed',
        EventServerConnect          : 'EventServerConnect'
    },

    /**
     * 通话状态改变时给XServer发送的事件名
     */
    callStatusEventNameMap : {
        Ringing   : 'ringing',
        Talking   : 'talking',
        Release   : 'call_normal_release',
        Hold      : 'holdcall',
        Retrieve  : 'retrievecall',
        Dialing   : 'dialing',
        MakeCall  : 'make_call',
        Consult   : 'consult_call',
        Monitor   : 'monitor_call',
        Intrude   : 'monitor_force_intrude',
        Break     : 'force_break',
        MuteOn    : 'mute_on',
        MuteOff   : 'mute_off',
        Conference     : 'conferencecall',
        SingleTransfer : 'singletransfer',
        MuteDeafOn    : 'mute_deaf_on',
        MuteDeafOff   : 'mute_deaf_off',
        ConsultConference : 'consult_conference',
        ConsultDisconnect : 'consult_disconnect',
        ConsultReconnect  : 'consult_reconnect',
        JoinConference    : 'join_conference'
    },

    sessionStatusEventNameMap : {
        EstablishedVideoCall : 'EstablishedVideoCall',
        ChatAnswer             : 'chatAnswer',
        ChatRelease             : 'chatRelease'
    },

    /**
     * 给APhone发送的请求行为
     */
    requestAphoneCmdMap : {
        Login              : 'RequestLogin',
        Logout             : 'RequestLogout',
        MakeCall           : 'RequestMakeCall',
        AnswerCall         : 'RequestAnswerCall',
        ReleaseCall        : 'RequestReleaseCall',
        HoldCall           : 'RequestHoldCall',
        RetrieveCall       : 'RequestRetrieveCall',
        SingleTransferCall : 'RequestSingleTransferCall',
        ConferenceCall     : 'RequestConferenceCall',
        SessionCall        : 'RequestSessionCall',
        SessionVideo       : 'RequestSessionVideo',
        MonitorCall        : 'RequestMonitorCall',
        IntrudeCall        : 'RequestIntrudeCall',
        ConsultCall        : 'RequestConsultCall',
        MuteUser           : 'RequestMuteUser',
        MuteMyself         : 'RequestMuteMyself',
        JoinChannel        : 'RequestJoinChannel',
        SetNoInviteEnd     : 'RequestSetNoInviteEnd',
        VideoWindows       : 'RequestVideoWindows',
        ShutDown           : 'RequestShutDown',
        SetUrl             : 'RequestSetUrl'
    },

    /**
     * 给前端发送的座席状态和通话状态改变的事件名
     */
    stateStatusMap : {
        logout     : 'Logout',
        login      : 'Login',
        ready      : 'Ready',
        notReady   : 'NotReady',
        busy       : 'Busy',
        holding    : 'Holding',
        ringing    : 'Ringing',
        dialing    : 'Dialing',
        monitering : 'Monitering',
        consulting : 'Consulting',
        intruding  : 'Intruding',
        intruded   : 'Intruded',
        consulted  : 'Consulted',
        monitered  : 'Monitered',
        makecall    : 'MakeCall'
    },

    /**
     * 给前端发送的会话状态改变的事件名
     */
    sessionStatusMap : {
        ringing  : 'Ringing',
        released  : 'Released',
        established : 'Established',
        RingingTimeOut : 'RingingTimeOut'
    },

    // 向socket发送request
    requestNameMap:{
        Normal:'Normal',
        ChatAccept:'ChatAccept',
        ChatConference:'ChatConference',
        ChatTransfer:'ChatTransfer',
        ChatCoach:'ChatCoach',
        ChatQuitCoach:'ChatQuitCoach',
        ChatDone:'ChatDone',
        ChatQuitConference:'ChatQuitConference'

    },

    // globe status
    phoneStatus : {
        connid : "",
        nextconnid : "",
        call : "",
        otherdn : "",
        wechat : undefined,
        webchat : undefined,
        appchat : undefined,
        email : undefined,
        currentOpt : "",
        currentStatus : "Logout",
        reason : "",
        bustype : "",
        callopt : "",
        wechatopt : "",
        webchatopt : "",
        appchatopt : "",
        emailopt : "",
        callpreopt : "",
        wechatpreopt : "",
        webchatpreopt : "",
        appchatpreopt : "",
        emailpreopt : "",
        isacw:"",
        sysopt:"",
        opensoftphone:true,
        isCallBack:false,
        worksheetId:"",
        monitordn:"",
        isSwapCall:false
    },

    //自动接听
    AutomaticAnswer : {
        call:false,
        chat:false,
        robot:false
    },

    //客户标签
    clientTag : {
        tag:""
    },
    //问题
    questionInfo : {
        questionId:""
    },
    consultative:{
        status:"座席组"
    },

    errorCount:{
        RequestAgentLogin:0,
        RequestAgentLogout:0,
        RequestAgentReady:0,
        RequestAgentNotReady:0,
        chatLogin:0,
        chatLogout:0,
        chatReady:0,
        chatNotReady:0
    },

    errorTips:{
        RequestAgentLogin:false,
        RequestAgentLogout:false,
        RequestAgentReady:false,
        RequestAgentNotReady:false,
        chatLogin:false,
        chatLogout:false,
        chatReady:false,
        chatNotReady:false
    },

    systemStatus:{
        status : true
    },

    notifyDelay: 2500,

    intervalPing: 1000,

    tenantInfo: {
        crmUrlInfo: ''
    }
};