package com.ruoyi.form.util;

import com.ruoyi.common.config.RuoYiConfig;

import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class EnDeTest {
    public static void main(String[] args) {
        try {
//            RSAPublicKey publicKey = RSAUtil.generatePublicKeyX509("C:\\Users\\Administrator\\x509_public.cer");
//            System.out.println("publicKey = " + publicKey);
//            String plain = "{\"serviceFormNo\": \"4444\",\"customerAttribution\": \"总行部室\",\"affiliatedUnit\": \"渠道管理部\",\"branchName\": \"渠道管理部\",\"physicalAddress\": \"上海市浦东新区\",\"contact\": \"4444\",\"contactDetails\": \"4444\",\"equipmentType\": \"自助设备\",\"equipmentName\": \"BTM\",\"equipmentModel\": \"广电BTM\",\"faultDescription\": \"XXXX\"}";
//            String encrypt = RSAUtil.encrypt(plain, "UTF-8");
//            System.out.println("encrypt = " + encrypt);
//            String decrypt = RSAUtil.decrypt( encrypt, "UTF-8");
//            System.out.println("decrypt = " + decrypt);
            String a=Class.class.getClass().getResource("/").getPath();
            System.out.println( System.getProperty("user.dir"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
