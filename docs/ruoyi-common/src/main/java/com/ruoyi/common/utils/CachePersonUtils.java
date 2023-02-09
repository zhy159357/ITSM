package com.ruoyi.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Cache工具类
 *
 * @author ruoyi
 */
public class CachePersonUtils {
	public static Map<String, String> catchePerson = new HashMap<String, String>();

	public CachePersonUtils() {
		if (getInstance == null) {
			getInstance = new CachePersonUtils();
		}
	}

	public CachePersonUtils getInstance = null;

	public static void put(String key, String value) {
		catchePerson.put(key, value);
	}

	public static String get(String key) {
		return catchePerson.get(key);
	}
	public static String remove(String key) {
		return catchePerson.remove(key);
	}
	public static int size() {
		return catchePerson.size();
	}
}
