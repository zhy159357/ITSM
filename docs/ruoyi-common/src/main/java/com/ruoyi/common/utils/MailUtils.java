package com.ruoyi.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 统一邮件发送工具类
 *
 * @author ruoyi
 * @date 2022/11/08
 */

@Component
@Configuration
public class MailUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MailUtils.class);

    /**
     * 从配置文件中注入是否需要身份验证
     */
    @Value("${mail.properties.mail.smtp.auth}")
    private String auth;

    /**
     * 从配置文件中注入邮箱类型
     */
    @Value("${mail.host}")
    private String emailHost;

    /**
     * 从配置文件中注入发件人
     */
    @Value("${mail.username}")
    private String fromEmail;

    /**
     * 从配置文件中注入授权码
     */
    @Value("${mail.password}")
    private String password;

    public static final String MESSAGE_SEND_SUCCESS = "yes";
    public static final String MESSAGE_SEND_FAIL = "no";

    public static final String PERSONAL = "运维管理";

    /**
     * 发送复杂邮件
     *
     * @param receivers  收件人
     * @param copys      抄送人
     * @param subject   邮件标题
     * @param content   邮件内容
     * @param imgPath   图片路径
     * @param filePath1 文件路径
     * @param filePath2 文件路径
     */
    public String sendComplexMail(String taskNo,String receivers, String copys, String subject, String content, String imgPath, String filePath1, String filePath2) {
        try {
        	log.info("开始发送邮件"+" taskNo:"+taskNo+" receivers"+receivers+" subject:"+subject);
            Properties props = new Properties();
            // 表示SMTP发送邮件，需要进行身份验证
            props.put("mail.smtp.auth", auth);
            props.put("mail.smtp.host", emailHost);
            // 发件人的账号
            props.put("mail.user", fromEmail);
            // 访问SMTP服务时需要提供的密码
            props.put("mail.password", password);

            // 1.Session对象.连接(与邮箱服务器连接)
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                	log.info("发送邮件PasswordAuthentication is fail ");
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            // 2.构建邮件信息
            Message message = new MimeMessage(session);
            // 发件人邮箱
            message.setFrom(new InternetAddress(fromEmail, PERSONAL));

            // Message.RecipientType.TO：消息接受者
            // Message.RecipientType.CC：消息抄送者
            // Message.RecipientType.BCC：匿名抄送接收者(其他接受者看不到这个接受者的姓名和地址)

            // 判断接收人个数
            if (receivers != null && receivers != "") {
                String[] receiverArray = receivers.split(",");
                if (receiverArray.length == 1) {
                    // 一对一发送邮件
                    message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiverArray[0]));
                } else {
                    // 一对多发送邮件
                    // 构建一个群发地址数组
                    String[] receiverArry = receivers.split(",");
                    InternetAddress[] adr = new InternetAddress[receiverArry.length];
                    for (int i = 0; i < receiverArry.length; i++) {
                        adr[i] = new InternetAddress(receiverArry[i]);
                    }
                    // Message的setRecipients方法支持群发。。注意:setRecipients方法是复数和点 到点不一样
                    message.setRecipients(Message.RecipientType.TO, adr);
                }
            }

            // 判断抄送人
            if(StringUtils.isNotEmpty(copys)) {
                String[] copysArray = copys.split(",");
                if(copysArray.length == 1) {
                    // 一对一抄送邮件
                    message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(copysArray[0]));
                } else {
                    // 一对多抄送邮件
                    // 构建一个群发地址数组
                    InternetAddress[] adr = new InternetAddress[copysArray.length];
                    for (int i = 0; i < copysArray.length; i++) {
                        adr[i] = new InternetAddress(copysArray[i]);
                    }
                    message.setRecipients(Message.RecipientType.CC, adr);
                }
            }

            // 发送日期
            message.setSentDate(new Date());
            // 设置标题
            message.setSubject(subject);
            // 3.6.拼接附件
            MimeMultipart allFile = new MimeMultipart();
            MimeBodyPart text = new MimeBodyPart();
            
			if(null!=imgPath&&!imgPath.isEmpty()) {
			    // 3.准备邮件内容
			    // 3.1.准备图片数据
			    MimeBodyPart image = new MimeBodyPart();
			    FileDataSource imageSource = new FileDataSource(imgPath);
			    DataHandler handler = new DataHandler(imageSource);
			    image.setDataHandler(handler);
			    image.setFileName(imageSource.getName());
			    // 创建图片的一个表示用于显示在邮件中显示
			    image.setContentID(imageSource.getName());
			    // 3.2准备本文本数据
			    text.setContent("<img src='cid:" + image.getContentID() + "'/>" + "<h4>" + content + "</h4>", "text/html;charset=utf-8");
			    // 3.4.拼装邮件正文
	            MimeMultipart mimeMultipart = new MimeMultipart();
	            mimeMultipart.addBodyPart(image);
	            mimeMultipart.addBodyPart(text);
	            // 文本和图片内嵌成功
	            mimeMultipart.setSubType("related");
	            // 3.5.将拼装好的正文内容设置为主体
	            MimeBodyPart contentText = new MimeBodyPart();
	            contentText.setContent(mimeMultipart);
	            // 附件
	            allFile.addBodyPart(image);
	            // 正文
	            allFile.addBodyPart(contentText);
			}else {
				    // 3.准备邮件内容font－family：宋体；font－weight：normal；
				    // 3.2准备本文本数据
				    text.setContent("<h4 style=\"font-family:'宋体';font-size:18px;font-weight:bold;\">" + content + "</h4>", "text/html;charset=utf-8");
				    // 3.4.拼装邮件正文
		            MimeMultipart mimeMultipart = new MimeMultipart();
		            mimeMultipart.addBodyPart(text);
		            // 3.5.将拼装好的正文内容设置为主体
		            MimeBodyPart contentText = new MimeBodyPart();
		            contentText.setContent(mimeMultipart);
		            // 正文
		            allFile.addBodyPart(contentText);
			}
			if(null!=filePath1&&!filePath1.isEmpty()) {
	            // 3.3.准备附件数据
	            MimeBodyPart appendix1 = new MimeBodyPart();
	            FileDataSource fileSource1 = new FileDataSource(filePath1);
	            appendix1.setDataHandler(new DataHandler(fileSource1));
	            appendix1.setFileName(fileSource1.getName());
	            allFile.addBodyPart(appendix1);
			}
			if(null!=filePath2&&!filePath2.isEmpty()) {
				 MimeBodyPart appendix2 = new MimeBodyPart();
		            FileDataSource fileSource2= new FileDataSource(filePath2);
		            appendix2.setDataHandler(new DataHandler(fileSource2));
		            appendix2.setFileName(fileSource2.getName());
		            allFile.addBodyPart(appendix2);
			}
            // 正文和附件都存在邮件中，所有类型设置为mixed
            allFile.setSubType("mixed");

            // 3.7.放到Message消息中
            message.setContent(allFile);
        	log.info("开始发送邮件.send"+" taskNo:"+taskNo+" receivers"+receivers+" subject:"+subject);
            // 4.发送邮件信息
            Transport.send(message);
            log.info("发送邮件成功"+" taskNo:"+taskNo+" receivers"+receivers+" subject:"+subject);
            return MESSAGE_SEND_SUCCESS;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("发送邮件失败",e);
            return MESSAGE_SEND_FAIL;
        }
    }
    
}