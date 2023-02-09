package com.ruoyi.web.controller.demo.controller;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
public class JasyptTest {
    public static void main(String[] arg) {

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        //配置文件中配置如下的算法
        standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        //配置文件中配置的password
        standardPBEStringEncryptor.setPassword("EWRREWRERWECCCXC");
        //要加密的文本
        String name = standardPBEStringEncryptor.encrypt("ry");
        String password = standardPBEStringEncryptor.encrypt("Hello123#");
        String passworddesc = standardPBEStringEncryptor.decrypt("tt3vy+qil80e/n2k9hEUwQ==");
        //将加密的文本写到配置文件中
        System.out.println("name=" + name);
        System.out.println("password=" + password);
        System.out.println("passworddesc=" + passworddesc);
    }
}


