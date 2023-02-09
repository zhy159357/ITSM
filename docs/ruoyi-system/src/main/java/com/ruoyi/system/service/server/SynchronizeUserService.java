package com.ruoyi.system.service.server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.system.service.ISynchronizeUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SynchronizeUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.http.entegorserver.entity.UserSyncMsg;

@Service
public class SynchronizeUserService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ISynchronizeUserService synchronizeUserService;

	public static final Logger logger = LoggerFactory.getLogger(SynchronizeUserService.class);
	private final String STATUS = "status";
	private final String MESSAGE = "message";

	private final String SUCCESS_CODE = "200";
	private final String ERROR_CODE = "500";
	public int type;
	public SysUser user;
	List<SynchronizeUser> users;
	public String loginNames;

	public void save(int type, OgUser user, List<SynchronizeUser> users, String loginNames) {
		String contentsstr = Base64.encodeString(user.getUserName());
		String password = Base64.encodeString(user.getPassword());
		for (SynchronizeUser synchronizeUseruser : users) {
			try {
				String sUrl = synchronizeUseruser.getServiceurl();
				String[] urls = sUrl.split(";");
				for (int j = 0; j < urls.length; j++) {
					try {
						URL url = new URL(urls[j] + "/saveordeluser.do?type=" + type + "&username=" + ""
								+ user.getLoginName() + "" + "&displayName=" + contentsstr + "&password=" + password
								+ "&email=" + user.getEmail() + "&telephone=" + user.getPhonenumber() + "&strJson="
								+ loginNames
//                            + "&iid=" + user.getUserId()==null?"":String.valueOf(user.getUserId())
						);
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setDoOutput(true);
						con.setDoInput(true);
						con.setUseCaches(false);// 忽略缓存
						con.setRequestMethod("POST");// 设置URL请求方法
						// 可设置请求头
						con.setRequestProperty("Content-Type", "application/octet-stream");
						con.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
						con.setRequestProperty("Charset", "UTF-8");
						con.connect();
						int code = con.getResponseCode();
						logger.info("adduser code ==============" + code);
						if (code == 200) {
							break;
						}
						Thread.sleep(1000);
					} catch (Exception e) {
						throw e;
					}
				}
			} catch (Exception e) {
				logger.error("adduser error:", e);
				e.printStackTrace();
			}
		}
	}

	public class SynchronizeUserThread extends Thread {
		public int type;
		public OgUser user;
		List<SynchronizeUser> services;
		public String loginNames;
		public UserSyncMsg msg;

		public SynchronizeUserThread(int type, OgUser user, List<SynchronizeUser> services, String loginNames,UserSyncMsg msg) {
			this.type = type;
			this.user = user;
			this.services = services;
			this.loginNames = loginNames;
			this.msg=msg;
		}

		public void run() {

			String userKey = user.getUserId();
			String userName = user.getUsername();
			String password = user.getPassword();
			String email = user.getEmail();
			String telphone = user.getPhonenumber();
			String department = user.getOrgname();
			String locked = user.getStatus();
			int ideCode = type;

			Map<String, Boolean> map = new HashMap<String, Boolean>();
			for (SynchronizeUser synchronizeUser : services) {
				String servicesFlag = synchronizeUser.getJsonParam();
				String url = synchronizeUser.getServiceurl();
				boolean execFlag = false;
				for (int i = 0; i < 10; i++) {
					try {
						if (!map.containsKey(servicesFlag)
								|| (map.containsKey(servicesFlag) && !map.get(servicesFlag))) {
							sendMessageSyncUser(url,msg);
							//save(type,user,services,loginNames);
						}
						execFlag = true;
						break;
					} catch (Exception e) {
						logger.error("user.sync is error:", e);
						e.printStackTrace();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					} finally {
						map.put(servicesFlag, execFlag);
					}
				}
			}

		}
	}

	public void synchronizeUser(OgUser user, int type, String loginNames,UserSyncMsg msg) {
		List<SynchronizeUser> services = synchronizeUserService.selectList();
		SynchronizeUserThread synchronizeUserThread = new SynchronizeUserThread(type, user, services, loginNames,msg);
		synchronizeUserThread.start();
	}
	public Map<String, Object> sendMessageSyncUser(String serverUrl, UserSyncMsg msg) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> uriVariables = new HashMap();
		List list = new ArrayList();
		String suffix_path = serverUrl
				+ "/pubAomsRecordJson.do?action=syncUser&modultype=0&model=save&access_token={token}";
		String token = getToken(serverUrl);
		if (StringUtils.isEmpty(token)) {
			returnMap.put(STATUS, ERROR_CODE);
			returnMap.put(MESSAGE, "获取token失败！");
			return returnMap;
		}

		uriVariables.put("token", token);

		String ideCode="-1";
		uriVariables.put("userKey", msg.getParams().get("userKey"));
		uriVariables.put("userName", msg.getParams().get("userName"));
		uriVariables.put("department", msg.getParams().get("department"));
		uriVariables.put("passCode", msg.getParams().get("password"));
		uriVariables.put("email", msg.getParams().get("email")!=null ? msg.getParams().get("email") : "");
		uriVariables.put("telphone", msg.getParams().get("telphone"));
		//uriVariables.put("ideCode", msg.getParams().get("ideCode"));
		uriVariables.put("state", msg.getParams().get("state"));
		uriVariables.put("ideCode", ideCode);
		uriVariables.put("longinName", msg.getParams().get("longinName"));
		list.add(uriVariables);
		String json = JSON.toJSONString(list);
		logger.debug("同步用户接口请求体json参数为:" + json);
		String message = "";
		String status = "";
		String changedesc = "";
		String sysname = "";
		ResponseEntity<String> result = null;
		try {
			json = URLEncoder.encode(json, "GBK");
			result = this.getResponseEntity(suffix_path, json, uriVariables);
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put(STATUS, ERROR_CODE);
			returnMap.put(MESSAGE, "用户同步接口调用失败！");
			return returnMap;
		}
		if (StringUtils.isNotEmpty(result.getBody())) {
			logger.debug("返回结果的请求体:"+result.getBody());
			Map jsonObject = JSONObject.parseObject(result.getBody());
			message = (String) jsonObject.get("mes");
			status = (String) jsonObject.get("code");
			if ("0".equals(status)) {
				status = SUCCESS_CODE;
				changedesc = (String) jsonObject.get("changedesc");
				logger.info("用户同步完成" + message);
			} else {
				status = ERROR_CODE;
				logger.error("用户同步失败,失败原因为:" + message);
			}
		} else {
			status = ERROR_CODE;
		}
		returnMap.put(MESSAGE, message);
		returnMap.put(STATUS, status);
		return returnMap;
	}

	/**
	 * 获取ResponseEntity返回信息
	 *
	 * @param suffix_path
	 * @param json
	 * @param uriVariables
	 * @return
	 */
	public ResponseEntity<String> getResponseEntity(String suffix_path, String json, Map uriVariables) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.APPLICATION_JSON_UTF8;
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity request = new HttpEntity(json, headers);
		return restTemplate.postForEntity(suffix_path, request, String.class, uriVariables);
	}

	public String getToken(String serverUrl) {
		String token = "";
		String suffix_path = serverUrl+"/pubAomsRecordJson.do?action=login&modultype=0&user=ideal";
		Map<String, Object> params = new HashMap<>();
		ResponseEntity<String> entity = null;
		try {
			entity = restTemplate.getForEntity(suffix_path, String.class, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
			String body = entity.getBody();
			Map tokenMap = JSONObject.parseObject(body);
			token = (String) tokenMap.get("access_token");
		}
		return token;
	}

}
