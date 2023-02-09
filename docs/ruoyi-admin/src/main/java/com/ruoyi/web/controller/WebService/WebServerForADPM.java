package com.ruoyi.web.controller.WebService;

import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.impl.SysApplicationManagerServiceImpl;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: ruoyi
 * @description:
 * @author: ma zy
 * @date: 2022-09-05 10:26
 **/
public class WebServerForADPM {

    private SysApplicationManagerServiceImpl applicationManagerService=
            SpringUtils.getBean(SysApplicationManagerServiceImpl.class);


    /**
     *
     * @param url           ADMP地址
     * @param methodName    方法名
     * @param params        参数
     * @return
     */
    public Object invokeWebService(String url, String methodName, Object[] params) {

        Object object = new Object();
        try {
            // 创建动态客户端
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(url);

            HTTPConduit http = (HTTPConduit)client.getConduit();
            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
            //连接超时(1分钟)
            httpClientPolicy.setConnectionTimeout(1 * 60000);
            httpClientPolicy.setAllowChunking(false);
            //响应超时(1分钟)
            httpClientPolicy.setReceiveTimeout(1 * 60000);
            http.setClient(httpClientPolicy);
            Object[] objects = new Object[0];

            objects = client.invoke(methodName, params);// invoke("方法名",参数1,参数2,参数3....);
            System.out.println("返回数据:" + objects[0]);
            object = objects[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * ADMP同步应用系统
     * @param object
     * @return
     */
    @Transactional
    public Map<String, Object> realResultXMLForSys(Object object) {

        Document doc = null;
        Map<String, Object> map = new HashMap<>();
        String msg = "同步失败";
        int num = 0;
        if (null != object) {
            Class cla = (Class)object.getClass();
            Field[] fields = cla.getDeclaredFields();
            try {
                for (int i = 0 ; i < fields.length ; i ++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    if (field.getName().endsWith("admpCode") && !((String)field.get(object)).equals("1")) {
                        break;
                    }
                    if (field.getName().endsWith("admpMsg")) {

                        String resultDataXML = (String)field.get(object);
                        if (StringUtils.isNotEmpty(resultDataXML)) {

                            //例子
                            doc = DocumentHelper.parseText(resultDataXML); // 将字符串转为XML
                            Element rootElt = doc.getRootElement(); // 获取根节点
                            System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
                            //多个p
                            Iterator p = rootElt.elementIterator("sysinfo");
                            // 遍历row节点
                            while (p.hasNext()) {
                                Element curP = (Element) p.next();//当前的p标签
                                OgSys ogSys = new OgSys();

                                //系统ID
                                if (StringUtils.isNotEmpty(curP.element("sysId").getTextTrim())) {
                                    ogSys.setSysId(curP.element("sysId").getTextTrim());
                                } else {
                                    continue;
                                }

                                //系统名称
                                if (StringUtils.isNotEmpty(curP.element("sysName").getTextTrim())) {
                                    ogSys.setCaption(curP.element("sysName").getTextTrim());
                                }

                                //系统编码
                                if (StringUtils.isNotEmpty(curP.element("sysMark").getTextTrim())) {
                                    ogSys.setCode(curP.element("sysMark").getTextTrim());
                                }

                                //系统接口负责人ID
                                if (StringUtils.isNotEmpty(curP.element("sysBusiContactAccount").getTextTrim())) {
                                    ogSys.setPid(curP.element("sysBusiContactAccount").getTextTrim());
                                }

                                //系统接口负责人Name
                                if (StringUtils.isNotEmpty(curP.element("sysBusiContactName").getTextTrim())) {
                                    ogSys.setModer(curP.element("sysBusiContactName").getTextTrim());
                                }


                                //红线时间
                                if (StringUtils.isNotEmpty(curP.element("sysServiceTime").getTextTrim())) {
                                    ogSys.setRedlineTime(curP.element("sysServiceTime").getTextTrim() + ":00");
                                } else {
                                    if(curP.element("00:00")!=null){
                                        ogSys.setRedlineTime(curP.element("00:00").getTextTrim());
                                    }

                                }

                                //系统类型
                                if (StringUtils.isNotEmpty(curP.element("sysType").getTextTrim())) {
                                    ogSys.setSysType(curP.element("sysType").getTextTrim());
                                }

                                //所属机构ID
                                if (StringUtils.isNotEmpty(curP.element("sysDept").getTextTrim())) {
                                    ogSys.setOrgId(curP.element("sysDept").getTextTrim());
                                }

                                //备注
                                if (StringUtils.isNotEmpty(curP.element("sysPosition").getTextTrim())) {
                                    ogSys.setMemo(curP.element("sysPosition").getTextTrim());
                                }

                                ogSys.setInvalidationMark("1");
                                ogSys.setAddTime(DateUtils.dateTimeNow());
                                ogSys.setUpdateTime(DateUtils.dateTimeNow());
                                ogSys.setIsExamination("0");
                                ogSys.setIsKeySys("0");
                                ogSys.setIsOutChannel("0");
                                ogSys.setAdder(ShiroUtils.getOgUser().getUsername());

                                // 根据paso号(code)查询
                                OgSys sys = applicationManagerService.selectOgSysBySysCode(ogSys.getCode());
                                if (null != sys) {
                                    // 根据sysid更新
                                    applicationManagerService.updateOgSys(ogSys);
                                } else {
                                    applicationManagerService.insertOgSys(ogSys);
                                }
                                num ++;
                            }
                        }
                    }
                    msg = "同步成功";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("msg", msg);
        map.put("num", num);
        return map;
    }
}
