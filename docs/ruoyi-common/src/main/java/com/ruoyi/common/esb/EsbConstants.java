/*    */ package com.ruoyi.common.esb;
/*    */ 
/*    */ public class EsbConstants
/*    */ {
/*    */   public static final String PUB_PARAM_PREFIX = "esbpub.";
/*    */   public static final String ESB_ERROR_DO_SERVICE = "960001";
/*    */   public static final String ESB_ERROR_CHECK_TOKEN = "960002";
/*    */   public static final String ESB_ERROR_SERVICE_NOTFOUNT = "960003";
/*    */   public static final String ESB_ERROR_SERVICE_NOT_ENOUGH_PARAM = "960004";
/*    */   public static final String HEADER_SYS_CODE = "syscode";
/*    */   public static final String HEADER_CHANNEL = "channel";
/*    */   public static final String HEADER_SERVICE_GROUP = "interfaceName";
/*    */   public static final String HEADER_SERVICE = "serviceName";
/*    */   public static final String HEADER_AUTHORIZATION = "authorization";
/*    */   public static final String HEADER_AUTHORIZATION2 = "Authorization";
/*    */   public static final String HEADER_MD5 = "md5";
/*    */   public static final String HEADER_INSTANCE_ID = "instanceId";
/*    */   public static final String HEADER_REMOTE_IP = "remoteIp";
/*    */   public static final String HEADER_FLOW_NO = "flowno";
/*    */   public static final String HEADER_RESULT_MODE = "resultMode";
/* 46 */   public static final String[] ESB_PUB_HEADER = { "syscode", "interfaceName", "serviceName", "authorization", 
/* 47 */     "md5", "instanceId", "flowno", "resultMode" };
/*    */   public static final String RESULT_MODE_ESB = "esb";
/*    */   public static final String PARAM_WS_REQ_CONFIG_BEAN = "EP_wsReqConfigBean";
/*    */   public static final String ESB_HEADER_PARAM_TOKEN = "token";
/*    */   public static final int LOG_LEVEL_DEBUG = 1;
/*    */   public static final int LOG_LEVEL_INFO = 2;
/*    */   public static final int LOG_LEVEL_WARN = 3;
/*    */   public static final int LOG_LEVEL_ERROR = 4;
/*    */   public static final String FLOW_STEP_START = "start";
/*    */   public static final String FLOW_STEP_NODE_START = "nodeStart";
/*    */   public static final String FLOW_STEP_NODE_End = "nodeEnd";
/*    */   public static final String FLOW_STEP_END = "end";
/*    */   public static final String ESB_BEAN_BeanFactoryChannelResolver = "esbChannelResolver";
/*    */   public static final String ESB_BEAN_MessagingTemplate = "esbMessagingTemplate";
/*    */   public static final String ESB_BEAN_ExceptionTransformer = "esbExceptionTransformer";
/*    */   public static final String ESB_BEAN_EsbExporterCaller = "esbExporterCaller";
/*    */   public static final String ESB_BEAN_EsbWebSecurityRequestRouter = "esbWebSecurityRequestRouter";
/*    */   public static final String ESB_USER_TOKEN_CACHE_NAME = "com.gsoft.framework.esb.router.SecurityRequestRouter";
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.EsbConstants
 * JD-Core Version:    0.6.0
 */