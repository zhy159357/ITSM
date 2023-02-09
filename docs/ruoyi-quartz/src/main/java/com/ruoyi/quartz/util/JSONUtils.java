package com.ruoyi.quartz.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * json处理
 * 
 * @author LiuPeng
 * 
 */
public class JSONUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);// 设置可用单引号
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);// 设置字段可以不用双引号包括
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);// 反序列化时，忽略目标对象没有的属性
	}

	/**
	 * 对象转json
	 * 
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toJSONString(Object obj) throws JsonProcessingException {
		if (obj == null) {
			return "";
		}
		return mapper.writeValueAsString(obj);
	}

	/**
	 * json转对象
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T parseObject(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, clazz);
	}

	/**
	 * json转集合对象
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> parseArray(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		CollectionType listType = mapper.getTypeFactory()
				.constructCollectionType(ArrayList.class, clazz);
		return mapper.readValue(json, listType);
	}

}
