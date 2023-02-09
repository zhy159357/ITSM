package com.ruoyi.common.netty.filter;

import com.ruoyi.common.esb.data.ResContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 交易过滤
 * 
 * @author LiuPeng
 * 
 */
public abstract class AbstractTranFilter {

	/**
	 * 获取交易名称
	 * 
	 * @return
	 */
	public abstract String getTranName();

	/**
	 * 请求参数filter
	 * 
	 * @param
	 */
	public abstract void reqFilter(Map<String, List<Object>> params);

	/**
	 * 返回filter
	 * 
	 * @param res
	 */
	public abstract void resFilter(ResContext<?> res);

	/**
	 * 获取参数
	 * 
	 * @param params
	 * @param key
	 * @return
	 */
	public String getParamStr(Map<String, List<Object>> params, String key) {
		List<Object> valueList = params.get(key);
		if (valueList != null && valueList.size() > 0) {
			return valueList.get(0).toString();
		}
		return null;
	}

	/**
	 * 设置参数
	 * 
	 * @param params
	 * @param key
	 * @param value
	 */
	public void setParam(Map<String, List<Object>> params, String key, String value) {
		List<Object> valueList = new ArrayList<Object>(1);
		valueList.add(value);
		params.put(key, valueList);
	}
	
	@Override
	public String toString() {
		return getTranName();
	}
	
}
