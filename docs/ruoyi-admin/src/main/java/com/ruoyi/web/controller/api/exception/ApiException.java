package com.ruoyi.web.controller.api.exception;

/**
 * api异常
 * 
 * @author LiuPeng
 * 
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 参数异常
	 */
	public final static String PARAM_FAILD = "9900";
	/**
	 * 签名校验失败
	 */
	public final static String VERIFY_FAILD = "9901";
	
	/**
	 * 请求数据转换异常
	 */
	public final static String PARSE_FAILD = "9902";
	
	/**
	 * 异常码
	 */
	private String code;

	public ApiException(String code,String message) {
		super(message);
		this.code = code;
	}
	
	public ApiException(String code,String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
