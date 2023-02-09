package com.ruoyi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class RuoYiApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  ITSM启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }

    @Value("${https.port}")
    private Integer port;
    @Value("${https.ssl.key-store-password}")
    private String key_store_password;
    @Value("${https.ssl.keyStoreType}")
    private String keyStoreType;

    /*@Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector()); // 添加http
        return tomcat;
    }

    *//* -------------------请按照自己spring boot版本选择 end---------------------- *//*
    // 配置https
    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            ClassPathResource resource = new ClassPathResource("youi.keystore");
            *//**
             * 此处主要是解决Linux环境下jar包方式无法通过ClassPathResource获取文件
             *//*
            File keystore = null;
            try {
                keystore = resource.getFile();
            }catch (IOException e){
                keystore = new File("config/youi.keystore");
            }
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(port);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(key_store_password);
            protocol.setKeystoreType(keyStoreType);
            return connector;
        } catch (Exception ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }*/

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // RestTemplate设置连接超时1分钟，数据返回超时时间设置成10分钟
        return builder
                .setConnectTimeout(Duration.ofMinutes(1))
                .setReadTimeout(Duration.ofMinutes(10))
                .build();
    }
}