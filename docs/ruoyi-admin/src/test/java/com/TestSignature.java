package com;

import cn.hutool.crypto.SecureUtil;

public class TestSignature {
    public static void main(String[] args) {
        String appid = "appId"+"20210003";
        String timestamp = "timestamp"+System.currentTimeMillis();
        String nonce = "nonce"+"aaaaaaaaaaaaaaaaaa";//18位
        String secret = "d9d09b1f57dcb100fe69c98c93f37de4";
        StringBuffer str = new StringBuffer();
        str.append(appid).append(nonce).append(timestamp).append(secret);
        String sign = sign(str.toString());
        System.out.println("timestamp="+timestamp);
        System.out.println("sign="+sign);
    }

    /**
     * 生成sign
     * @param str
     * @return
     */
    private static String sign(String str) {
        return SecureUtil.sha256(str).toUpperCase();
    }
}
