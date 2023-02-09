/*jslint browser: true, devel: true*/
/*global window*/
(function() {
    'use strict';

    var profile = 'test';
    var configMap = {
        test:{
            debug: true,
            autoGetDn: false, // false:直接获取固定分机 true：从dnpool获取分机
            dnpooServer:"https://devcc.egooccs.com/server/cfg-api/",//测试环境//"http://49.235.229.66/server/cfg-api",//http://10.50.5.125:8099/cfg-api，https://devcc.egooccs.com/cfg-api
            monitorIp:"http://11.105.60.6:9998/moniS",//"http://10.50.5.125:8099/cfg-api/v1/platform/agent/status/",//https://devcc.egooccs.com/server/cfg-api
            restIp:"https://devcc.egooccs.com/dbsrv",//dbserver地址,
            internalManagement:"https://devcc.egooccs.com/siteurm/", //内管获取号码池信息接口地址
            logDir:"C:\\tphonelogs1",
            platform:"genesys",//"freeswitch",//freeswitch/genesys/huawei
            dntype:"genesyssipphone",//"softphone", //softphone/genesyssipphone/webrtc/hardphone/xphone
            answerType : "", //来电接听方式，aphone表示走软电话接听
            LINEINFO:{
              lines:[],
              suppliers:[
                {"id":"1","supplierName":"容联"},
                {"id":"2","supplierName":"江海"},
                {"id":"3","supplierName":"移动"},
              ],//外呼厂商
              outBoundType:"0", //0:直接呼叫,1:configprefix+attr+number,2:dnpoolprefix+attr+number,3:configprefix+number,4:dnpoolprefix+number
              attribution: '上海', //sdc本地地区
              outOfTownPrefix:"0", //外地号码前缀
              appearanceType:"fixedAppearance", //外显号码模式，fixedAppearance固定外线，numberPoolAppearance号码池随机外线
              fixedAppearanceInfo:{},//当前外线信息
              makecallPrefix:"9",//外呼出局前缀
            },
          phone:{
            outBoundMode:"preview",//preview:预览，predict:预测
            transferGroupType:"ready",//ready:空闲,all:所有坐席组
            autoLogin:false, //是否自动签入
            autoAnswer : false, //是否自动应答
            emotion : "99999", //转满意度ivr节点号码
            transferIVR : "666666", //转指定ivr节点号码
            freelink:{
              aphoneUrl : "ws://127.0.0.1:5050", //本地aphone软电话ws连接地址，不用修改
              checkAphoneTimeLong:15, //检测aphone是否安装时长
              startRecord:false, //是否进行本地调用aphone录音功能
            },//freelink平台相关配置
            notReadyReason:"9",
            autoReady:"NotReady",
          },
          customized:{
              ICBC:{
                customizedServer:"http://47.99.195.195:30084",
                ICBCCallCenter:"http://122.19.141.212:10778/ICBCCallCenter/csr/sysfunc/OpenServiceServlet",
                ICBCID:false,
                loginLisense:false,
                mediaTransfer:false,
              } //工行定制功能
          },
          pageStyle:{
            skin:"1",//1白底蓝边，2、灰底灰蓝边
            calllistMode:"maximize",//minimize:最小化,maximize最大化，默认模式最小化还是最大化
            phoneUiPosition:"top:40%;left:35%", //电话条相对容器显示位置
            calllistPostion:"top:10%;left:20%",//通话记录相对容器显示位置
          },
          RTC: {
            MGW: 'wss://49.235.229.66/xchat',
            // 视频模式（WPhone、Browser）
            browserType: 'Browser',
            // WebRtc，LTRtc腾讯小视频RTC解决方案
            rtcType: 'WebRtc',
            parentid:'main-container'
          },
        }
    };
    window.CONFIG = configMap[profile];
}());
