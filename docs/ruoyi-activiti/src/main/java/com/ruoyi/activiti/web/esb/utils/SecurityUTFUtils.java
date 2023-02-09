package com.ruoyi.activiti.web.esb.utils;

import com.ruoyi.activiti.web.esb.entity.SecurityUTF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SecurityUTFUtils {
    public static SecurityUTF getSecurityUTF(byte[] securityBytes) {
        try {
            if (securityBytes.length == 0) {
                return null;
            }
            ByteArray ba = new ByteArray(securityBytes);

            String sysCode = ba.readUTF();
            String token = ba.readUTF();
            SecurityUTF securityUTF = SecurityUTF.newSecurityUTF(sysCode, token);
            return securityUTF;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getByteArrayBody(SecurityUTF securityUTF) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(byteOut);
        try {
            writer.writeUTF(securityUTF.getSysCode());
            writer.writeUTF(securityUTF.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteOut.toByteArray();
    }
}
