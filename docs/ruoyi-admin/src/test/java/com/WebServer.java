//package com;
//
//import com.ruoyi.common.utils.StringUtils;
//import org.apache.commons.io.IOUtils;
//import org.apache.cxf.endpoint.Client;
//import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
//import org.apache.cxf.transport.http.HTTPConduit;
//import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
///**
// * @program: ruoyi
// * @description:
// * @author: ma zy
// * @date: 2022-09-05 10:26
// **/
//public class WebServer {
//
//    public static void invokeService_2(){
//        // 创建动态客户端
//        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//        Client client = dcf.createClient("http://120.48.87.177:8099/services/ws/api?wsdl");
//        HTTPConduit http = (HTTPConduit)client.getConduit();
//        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
//        //连接超时(1分钟)
//        httpClientPolicy.setConnectionTimeout(1 * 60000);
//        httpClientPolicy.setAllowChunking(false);
//        //响应超时(1分钟)
//        httpClientPolicy.setReceiveTimeout(1 * 60000);
//        http.setClient(httpClientPolicy);
//        Object[] objects = new Object[0];
//        try {
//            // invoke("方法名",参数1,参数2,参数3....);
//            objects = client.invoke("emrService", "test");
//            System.out.println("返回数据:" + objects[0]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
