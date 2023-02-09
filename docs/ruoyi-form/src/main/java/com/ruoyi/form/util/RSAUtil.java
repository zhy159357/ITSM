package com.ruoyi.form.util;

import org.apache.commons.lang.ArrayUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSAUtil {
    private static String path=System.getProperty("user.dir");
    /**
     * 从文件中加载私钥
     * @param in 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception{
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
//			this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
//			this._strPrivateKey = this.getStringPrivateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * x.509公钥证书
     * @param certificateFile
     * @throws Exception
     */
    public static RSAPublicKey generatePublicKeyX509(String certificateFile) throws Exception{
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream fs = new FileInputStream(certificateFile);
        X509Certificate x = (X509Certificate)cf.generateCertificate(fs);
        PublicKey pk = x.getPublicKey();
        return (RSAPublicKey) pk;
//		this._strPublicKey = this.getStringPublicKey();
    }

    public static String encrypt(String orgmsg, String charset) throws Exception{
        return encrypt(orgmsg.getBytes(charset));
    }
    /**
     * 私钥加密过程
     * @param
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt( byte[] plainTextData) throws Exception{
        FileInputStream inputStream = new FileInputStream(path+"\\pkcs8_private_key.pem");
        RSAPrivateKey privateKey = RSAUtil.loadPrivateKey(inputStream);
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            //超过117字节报错，分段加密
            byte[] enbyte=null;
//			StringBuilder sb=new StringBuilder();
            for(int i=0;i<plainTextData.length;i+=64){

                byte[] output= cipher.doFinal(ArrayUtils.subarray(plainTextData,i,i+64));
                enbyte = ArrayUtils.addAll(enbyte, output);
            }

            BASE64Encoder encode = new BASE64Encoder();
            return encode.encodeBuffer(enbyte);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    public static String decrypt(String codeCipherData, String charset) throws Exception{
        byte[] en = decrypt(codeCipherData);
        return new String(en, charset);
    }
    /**
     * 公钥解密过程
     * @param codeCipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[]  decrypt(String codeCipherData) throws Exception{
        RSAPublicKey publicKey = RSAUtil.generatePublicKeyX509(path+"\\x509_public.cer");
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            BASE64Decoder decode = new BASE64Decoder();
            //分段解密
            byte[] debyte=null;
            byte[] data=decode.decodeBuffer(codeCipherData);
            for(int i=0;i<data.length;i+=128){
                byte[] output= cipher.doFinal(ArrayUtils.subarray(data,i,i+128));
                debyte=ArrayUtils.addAll(debyte, output);
            }

            return debyte;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new Exception("密文数据已损坏");
        }
    }
}
