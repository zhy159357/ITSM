package com.ruoyi.common.utils.security;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Md5加密方法
 *
 * @author ruoyi
 */
public class Md5OriginalUtils {
    private static final Logger log = LoggerFactory.getLogger(Md5OriginalUtils.class);

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes("UTF-8"));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        try {
            return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
            log.error("not supported charset...{}", e);
            return s;
        }
    }

    public static String md5Password(String password) {
        return new Md5Hash(password).toHex();
    }

    public static String md5Password(String password, String salt) {
        return new Md5Hash(password, salt).toHex();
    }

    public static String randomPassword()
    {
        char[] pwdchs = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 'd', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        Random rand = new Random();
        String pwd = "";

        for (int j = 0; j < 6; j++) {
            pwd = pwd + pwdchs[rand.nextInt(62)];
        }
        return pwd;
    }
}
