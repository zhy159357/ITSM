package syncproblem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.alibaba.fastjson.JSON;

public class SyncProblemData {
	public static void main(String[] arg) throws ClientProtocolException, IOException {
		if (null == arg || arg.length == 0) {
			System.out.println("two params: 1,content;2,0or1(0:+;1:-)");
			return;
		}
		String content = arg[0];
		String flag = arg[1];

		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		// 配置文件中配置如下的算法
		standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
		// 配置文件中配置的password
		standardPBEStringEncryptor.setPassword("EWRREWRERWECCCXC");
		// 要加密的文本
		String password = standardPBEStringEncryptor.encrypt(content);
		if ("1".equals(flag)) {
			System.out.println("-");
			password = standardPBEStringEncryptor.decrypt(content);
		} else {
			System.out.println("+");
			password = standardPBEStringEncryptor.encrypt(content);
		}
		String no = "'2022120300";
		Map<String, String> m = new HashMap<String, String>();
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		 String responseContent= "";
		for (int i = 0; i < 100; i++) {
			m = new HashMap<String, String>();
			CloseableHttpResponse response = null;
			try {
				m.put("eventNo", no+i);
				m.put("eventSource", "Monitor");
				m.put("transDate", "2022-12-03 15:55:50");
				m.put("priority", "P4");
				m.put("assignedGroup", "运维平台部1组");
				m.put("systemName", "监控系统");
				m.put("evenTile", "测试XXXXXXXXXXXXX"+i);
				m.put("evenTdesc", "测试XXXXXXXXXXXXXDESC"+i);
				m.put("emergencyLevel", "紧急");
				m.put("workContent", "事件单测试数据65554522212");
				m.put("businessCategory", "业务分类大类");
				m.put("businessSubCategory", "业务分小类");
				m.put("operatorNo", "311936");
				m.put("files", "http://127.0.0.1:9999/itsm/image1");
				String str=JSON.toJSONString(m);
				// 创建Post请求
				HttpPost httpPost = new HttpPost("http://localhost:9999/foreign/event/createEvent");
	            httpPost.addHeader("Content-Type","application/json");
	            httpPost.setEntity(new StringEntity(str,"application/json","UTF-8"));

	             response=httpClient.execute(httpPost);
	            HttpEntity entity=response.getEntity();
	            responseContent= EntityUtils.toString(entity,"UTF-8");
				// 从响应模型中获取响应实体
				HttpEntity responseEntity = response.getEntity();
				System.out.println("响应状态为:" + response.getStatusLine());
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (response != null) {
						response.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 将加密的文本写到配置文件中
		System.out.println(password);
	}
}
