package com.ruoyi.common.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Md5加密方法
 * 
 * @author ruoyi
 */
public class Md5Utils
{
    private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

    private static byte[] md5(String s)
    {
        MessageDigest algorithm;
        try
        {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes("UTF-8"));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        }
        catch (Exception e)
        {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[])
    {
        if (hash == null)
        {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++)
        {
            if ((hash[i] & 0xff) < 0x10)
            {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s)
    {
        try
        {
            return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
        }
        catch (Exception e)
        {
            log.error("not supported charset...{}", e);
            return s;
        }
    }

    public static String md5(File file) throws Exception {
        BigInteger bi = null;
        byte[] buffer = new byte[8192];
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);

        int len;
        while((len = fis.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }

        fis.close();
        byte[] b = md.digest();
        bi = new BigInteger(1, b);
        return bi.toString(16);
    }

    public static String md5(InputStream fis) throws Exception {
        BigInteger bi = null;
        byte[] buffer = new byte[8192];
        MessageDigest md = MessageDigest.getInstance("MD5");

        int len;
        while((len = fis.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }

        fis.close();
        byte[] b = md.digest();
        bi = new BigInteger(1, b);
        return bi.toString(16);
    }
}
