package com.ruoyi.common.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketUtils {
	private static Logger logger = LoggerFactory.getLogger(SmsUtils.class);

	private final static String DEFAULT_CHARSET="GBK";
	/**
	 * Socket请求(通讯方式：Socket短连接)
	 * 
	 * @param ip
	 *            Socket服务端IP
	 * @param port
	 *            Socket服务端口
	 * @param timeout
	 *            响应超时时间(毫秒),小于等于0时默认为2000毫秒
	 * @param reqMsg
	 *            请求报文
	 * @param encoding
	 *            字符编码
	 * @return
	 * @throws Exception 
	 */
	public static String socketComm(String ip, int port, int timeout, String reqMsg, String encoding) throws Exception {
		// 响应报文
		String rspResult = "";
		if (null == reqMsg && "".equals(reqMsg)) {
			logger.info("请求报文不能为空,不进行Socket请求操作!");
			return rspResult;
		}
		if (null == encoding && "".equals(encoding)) {
			encoding = DEFAULT_CHARSET;
		}
		int timeOut = timeout > 0 ? timeout : 2000;
		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		StringBuffer sb = new StringBuffer();
		try {
			logger.info("*************************发送报文开始*************************");
			// 与服务端建立连接
			socket = new Socket(ip, port);
			// 设置超时毫秒数
			socket.setSoTimeout(timeOut);
			// 因为先发送再接收，所以可以禁止Linger
			socket.setSoLinger(true, 0);
			logger.info("socket : " + socket);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.write(reqMsg.getBytes(encoding));
			dos.flush();
			socket.shutdownOutput();
			dis = new DataInputStream(socket.getInputStream());
			int readLength = 0;
			do {
				byte[] bytes = new byte[dis.available()];
				readLength = dis.read(bytes);
				if (readLength > 0) {
					// 注意指定编码格式(发送方和接收方一定要统一)
					sb.append(new String(bytes, 0, readLength, encoding));
				}
			} while (readLength > 0);
			rspResult = sb.toString();
			logger.info("*************************发送报文结束*************************");
		} catch (Exception e) {
			logger.error("Socket通讯异常!",e);
			throw e;
		} finally {
			// 释放资源
			if (dis != null) {
				try {
					dis.close();
				} catch (Exception e) {
					logger.error("关闭DataInputStream异常!");
				}
			}
			if (dos != null) {
				try {
					dos.close();
				} catch (Exception e) {
					logger.error("关闭DataOutputStream异常!");
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
					logger.error("关闭Socket异常!");
				}
			}
		}
		return rspResult;
	}
}