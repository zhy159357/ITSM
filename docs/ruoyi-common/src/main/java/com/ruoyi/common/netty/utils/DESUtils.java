package com.ruoyi.common.netty.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

/**
 * DES 加密工具
 * 
 * @author LiuPeng
 * @date 2017年10月17日
 * 
 */
public class DESUtils {

	private static final String ALGORITHM_DESEDE = "DESede";

	private static final String DECRYPT_ENCODING = "UTF-8";

	private static final String KEY = "gsoft-desede-key";

	/**
	 * 加密
	 * 
	 * @param info
	 * @return
	 */
	public static String encrypt(String info) {
		return encrypt(KEY, info);
	}

	/**
	 * 加密信息
	 * 
	 * @param key
	 * @param info
	 * @return
	 */
	public static String encrypt(String key, String info) {
		byte[] desKey;
		try {
			desKey = build3DesKey(key);
		} catch (Exception e) {
			throw new RuntimeException("秘钥构建失败：" + e.getMessage());
		}

		SecretKey secretKey = new SecretKeySpec(desKey, ALGORITHM_DESEDE);
		Cipher cipher = null;
		byte[] byteResult = null;
		try {
			cipher = Cipher.getInstance(ALGORITHM_DESEDE);
			if (cipher != null) {
				try {
					cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				} catch (InvalidKeyException e) {
					throw new RuntimeException("encrypt error", e);
				}

				byteResult = cipher.doFinal(info.getBytes(DECRYPT_ENCODING));
			}
			if (byteResult == null) {
				return "";
			}
			return byte2HexStr(byteResult);
		} catch (Exception e) {
			throw new RuntimeException("DESede加密失败", e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param dest
	 * @return
	 */
	public static String decrypt(String dest) {
		return decrypt(dest, KEY);
	}

	/**
	 * 解密信息
	 * 
	 * @param dest
	 * @param key
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static String decrypt(String dest, String key) {
		try {
			SecretKey secretKey = new SecretKeySpec(build3DesKey(key), ALGORITHM_DESEDE);

			Cipher cipher = null;
			cipher = Cipher.getInstance(ALGORITHM_DESEDE);

			byte[] byteResult = null;
			if (cipher != null) {
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				byteResult = cipher.doFinal(str2ByteArray(dest));
			}

			return new String(byteResult, DECRYPT_ENCODING);

		} catch (Exception e) {
			throw new RuntimeException("DESede解密失败:" + dest, e);
		}
	}

	/**
	 * 字节数组转化为大写16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2HexStr(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String s = Integer.toHexString(b[i] & 0xFF);
			if (s.length() == 1) {
				sb.append("0");
			}

			sb.append(s.toUpperCase());
		}

		return sb.toString();
	}

	/**
	 * 字符串转字节数组
	 * 
	 * @param s
	 * @return
	 */
	private static byte[] str2ByteArray(String s) {
		int byteArrayLength = s.length() / 2;
		byte[] b = new byte[byteArrayLength];
		for (int i = 0; i < byteArrayLength; i++) {
			byte b0 = (byte) Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16).intValue();
			b[i] = b0;
		}
		return b;
	}

	/**
	 * 构造3DES加解密方法key
	 * 
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	private static byte[] build3DesKey(String keyStr) throws Exception {
		byte[] key = new byte[24];
		byte[] temp = keyStr.getBytes("UTF-8");
		if (key.length > temp.length) {
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException {

		String key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJNiOGqfM1q8VfeZDE7ZNaON7elmJk3e0KchUg8b2iJoDQ79DSrN5vrvRLCjlUfK9K9yjQwWeTYPJOkW4CDwTZkESAlEXtd1jJWfnMo5Al5n9PvgFj8FfQqvWYm51f6nspJ2Sa9h4y8TJTJNLLKyhzbEzIAndmcfUTdvAgMBAAECgYBRptTCnTiCSF0IM05SUJwa5IKuD92gvpSyQwHN1LRM8XQrKMyE3zL6PKiJYnlBVDLh9eVwUIIos38JKQG8O2yZ9pkM0LRokSRSeaw94TvOxuVas77niX9VeawykLueUfKTKLlcsAvYj8BW5YnceeBjVgfGeAPct9KO4QJBANcisLwOi3NbveG5nieGyQEsqzJlHIGO4ehYGuzEtyBO6GQtsVR2YKT8DrdMVvg5aKIoETm3l1LYLZHVZu7sCQQCvYPpapog8EhTBQbExmufWzB9xgHEWvAqkqXJU1UZEDberIgaGXtsk1xvUtKZlDu2jOYDLEIXgb7P6QuM1oXdAkA29v7Syt2SZjI7u5OWrY5xnWGpE7eBimYbOPfRjGb9P2d8mADsyHr9bxUBTScAaoVMKO6327KVTbfLKw33jX2RAkEAjTo0AOHQHUeWLRYggcizX3aa74S2DM6ZmUJa6UfZBgSe3hGVXYlvQZhzkfMzd3fxB5sbyupwVI6SQ2pBshQJBAMFfu0XsE0V8h3zga7fKifnOFXqMtDSSxzl2BDQSLlERQi24GwuqCS2DsD3bticvS2n88qAFI799prrxUxM";
		Map<String,String> info = new HashMap<String,String>();
		info.put("phoneNumber", "13838384438");
		info.put("sysCode", "AOMS");
		info.put("sysName", "一体化运维平台");

		String encryptInfo = DESUtils.encrypt(key, JSON.toJSONString(info, false));
		System.out.println(encryptInfo);

        String decrypt = DESUtils.decrypt(encryptInfo, key);
        JSONObject jsonObject = JSONObject.parseObject(decrypt);
        System.out.println(jsonObject);


		System.out.println(DESUtils.decrypt(
				"5BA18D58A2E49722509977A43E6E33200D5E9E3A7950DF60BB854B263E91D0D061D80D1DD3E2EDCEDBF055737E586C3A8D07056FC51EA9576508BEA493F4BEB9",
				"sysChannel"));

	}
}
