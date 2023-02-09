package com.ruoyi.common.netty.utils;

import com.ruoyi.common.esb.data.PubContext;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录用户工具类
 * @author LiuPeng
 *
 */
public class SecurityUtils {
	
	public static String USER_ID = "userId";
	public static String USER_NAME = "userName";
	
	private static ThreadLocal<Map<String, String>> localSecurityMap = new ThreadLocal<Map<String,String>>();
	
	public static void addSecurityInfo(PubContext pubContext){
		add(SecurityUtils.USER_NAME,
				pubContext.getUsername());
		Map<String, Object> pubContextParams = pubContext.getParams();
		if (pubContextParams != null) {
			for (Map.Entry<String, Object> entry : pubContextParams.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue() instanceof String) {
					add(entry.getKey(), entry.getValue()
							.toString());
				}
			}
		}
	}
	public static void add(String key,String value){
		Map<String, String> securityMap = getSecurityMap();
		securityMap.put(key, value);
	}
	
	public static void addAll(Map<String, String> params){
		Map<String, String> securityMap = getSecurityMap();
		securityMap.putAll(params);
	}
	
	private static Map<String, String> getSecurityMap(){
		Map<String, String> securityMap = localSecurityMap.get();
		if(securityMap==null){
			securityMap = new HashMap<String, String>();
			localSecurityMap.set(securityMap);
		}
		return securityMap;
	}
	
	public static String getUserId() {
		Map<String, String> securityMap = localSecurityMap.get();
		if (securityMap != null) {
			return securityMap.get(USER_ID);
		}
		return null;
	}
	
	public static String getPrincipalConfig(String property){
		Map<String, String> securityMap = localSecurityMap.get();
		if(securityMap!=null){
			return securityMap.get(property);
		}
		return null;
	}
	
}
