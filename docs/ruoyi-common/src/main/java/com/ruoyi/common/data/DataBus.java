package com.ruoyi.common.data;



/**
 * 数据总线
 * @author 周扬
 *
 */
public class DataBus implements IDataBus ,java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -947746710325917348L;
	private JSONObject in;	// 输入数据
	private JSONObject out;		// 输出数据
	static private JSONObject common = new JSONObject();		// 公共数据
	private JSONObject custom;	// 自定义数据
	/**
	 * 获取输入数据
	 * @return
	 */
	public JSONObject getIn() {
		return in;
	}
	/**
	 * 设置输入数据
	 * @return
	 */
	public void setIn(JSONObject in) {
		this.in = in;
	}

	/**
	 * 获取输出数据
	 * @return
	 */
	public JSONObject getOut() {
		return out;
	}

	/**
	 * 设置输出数据
	 * @return
	 */
	public void setOut(JSONObject out) {
		this.out = out;
	}

	/**
	 * 获取自定义数据
	 * @return
	 */
	public JSONObject getCustom() {
		return custom;
	}

	/**
	 * 设置自定义数据
	 * @return
	 */
	public void setCustom(JSONObject custom) {
		this.custom = custom;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
	/**
	 * @param common the common to set
	 */
	public static void setCommon(JSONObject common) {
		DataBus.common = common;
	}
	/**
	 * @return the common
	 */
	public static JSONObject getCommon() {
		return common;
	}

}

