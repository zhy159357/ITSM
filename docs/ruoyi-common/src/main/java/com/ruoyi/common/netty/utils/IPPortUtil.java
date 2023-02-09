package com.ruoyi.common.netty.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.naming.InitialContext;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Set;

/**
 * 此类主要获取的是win上的ip和端口
 * 
 * @author LiuPeng
 * @date 2019年8月14日
 * 
 */
public class IPPortUtil {

	private static Log logger = LogFactory.getLog(IPPortUtil.class);

	/**
	 * 获取tomcat启动容器的端口
	 * @return
	 */
	public static String getLocalPort() {

		String port = "9999";
		try {
			// 获取tomcat端口
			MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
					Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
			port = objectNames.iterator().next().getKeyProperty("port");
		} catch (Exception e) {
			logger.error("获取tomcat端口失败," + e.getMessage());
			// 其他容器获取端口
		}
		return port;
	}

	/**
	 * @return 获取当前机器的IP
	 */
	public static String getLocalIP() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}
}