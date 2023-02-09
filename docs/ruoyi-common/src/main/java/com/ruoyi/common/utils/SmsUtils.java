package com.ruoyi.common.utils;

import java.text.NumberFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 统一邮件发送工具类
 *
 * @author ruoyi
 * @date 2022/11/08
 */

@Component
@Configuration
public class SmsUtils {
	  private static Logger logger = LoggerFactory.getLogger(SmsUtils.class);
    /**
     * 从配置文件中注入发件人
     */
    @Value("${foreign.sms.ip}")
    private String smsIp;

    /**
     * 从配置文件中注入授权码
     */
    @Value("${foreign.sms.port}")
    private String smsPort;
    @Value("${foreign.sms.encode}")
    private String smsEncode;
    @Value("${foreign.sms.privateFlag}")
    private String privateFlag;
    @Value("${foreign.sms.flag}")
    private String flag;
    @Value("${foreign.sms.businessCode}")
    private String businessCode;

    /**
     * 发送复杂邮件
     *
     * @param receivers  收件人
     * @param subject   邮件标题
     * @param content   邮件内容
     * @param imgPath   图片路径
     * @param filePath1 文件路径
     * @param filePath2 文件路径
     */
    public String sendSmsMessageBySocket(String taskNo,String mobiles, String contents) {
    	logger.info("告警事件推送至短信平台..."+taskNo+" mobiles:"+mobiles+" contents:"+contents );
    	logger.info("========================================================================================");
        // 返回结果
        JSONObject resultInfo = new JSONObject();
        logger.info("发送给 : " + mobiles + ", 内容为 : \n" + contents);
        String[] ips = smsIp.split(",");							//10.240.91.140
        Integer port = Integer.parseInt((smsPort));					//7886
        // 短信发送成功个数
        int sum = 0;
        String[] mobileArray = mobiles.split(",");
        for (String mobile : mobileArray) {
            // 拼接短信报文
            StringBuffer msg = new StringBuffer();
            msg.append("<?xml version=\"1.0\" encoding=\"GB18130\"?>");
            msg.append("<Root>");
            // 对公、对私标识(C-对私)
            msg.append("<Public_PrivateFlag>").append(privateFlag).append("</Public_PrivateFlag>");
            // 系统标识 ：
            msg.append("<SendSystemFlag>").append(flag).append("</SendSystemFlag>");
            // 业务代码
            msg.append("<BusinessCode>").append(businessCode).append("</BusinessCode>");
            // 系统流水号
            String systemSerialNo = UUID.randomUUID().toString();
            msg.append("<SystemSerialNo.>");
            msg.append(systemSerialNo);
            msg.append("</SystemSerialNo.>");
            // 当前系统时间
            Date currentTime = new Date();
            // 发送日期
            msg.append("<SendDate>");
            msg.append(DateUtils.formatDate(currentTime, "yyyyMMdd"));
            msg.append("</SendDate>");
            // 发送时间
            msg.append("<SendTime>");
            msg.append(DateUtils.formatDate(currentTime, "HHmmss"));
            msg.append("</SendTime>");
            // 接口版本(目前为1.0)
            msg.append("<InterfaceVersion>1.0</InterfaceVersion>");
            // 发送手机号
            msg.append("<MobileNumber>");
            msg.append(mobile);
            msg.append("</MobileNumber>");
            // 短信内容
            msg.append("<Sms>");
            // 内容中特殊字符保持原样
            msg.append("<![CDATA[");
            msg.append(contents);
            msg.append("]]>");
            msg.append("</Sms>");
            msg.append("</Root>");
            try {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 是否分组显示数据，即每3位为一个分组，分组间以英文半角逗号分隔，false：不分组，true:分组
                numberFormat.setGroupingUsed(false);
                // 设置数值的整数部分允许的最大位数
                numberFormat.setMaximumIntegerDigits(4);
                // 设置数值的整数部分允许的最小位数
                numberFormat.setMinimumIntegerDigits(4);
                String messageLength = numberFormat.format(msg.toString().getBytes("GBK").length);
                // 定长，4个字节
                msg.insert(0, messageLength);
                // 结束符：回车换行
                msg.append("\r");

                logger.info("发送短信模板内容 : " + msg);

                // 告警事件推送到短信平台无返回结果,无法进行成功与否判断,现默认都成功,有问题可在短信平台进行排查
                logger.info("短信发送IP地址 : %s, 端口 : %s", ips[0], port);
                String rspResult = SocketUtils.socketComm(ips[0], port, 2000, msg.toString(), smsEncode);
                sum++;
                logger.info("短信发送请求返回结果 : " + rspResult);

            } catch (Exception e) {
            	logger.error("========手机号【%s】短信发送请求异常========", mobile);
            	logger.error("短信发送请求异常",e);
            }
        }

        if (sum == mobileArray.length) {
            resultInfo.put("errorMsg", "短信发送成功");
            resultInfo.put("errorCode", "000000");
            resultInfo.put("isSuccess", "0");
            resultInfo.put("smsId", "000000");
        } else {
            resultInfo.put("errorMsg", "短信发送失败");
            resultInfo.put("errorCode", "666666");
            resultInfo.put("isSuccess", "1");
            resultInfo.put("smsId", "None");
        }
        logger.info("========================================================================================");
        return resultInfo.toJSONString();
    }
}