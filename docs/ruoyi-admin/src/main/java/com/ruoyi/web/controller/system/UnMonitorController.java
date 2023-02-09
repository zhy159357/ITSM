package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ruoyi.activiti.utils.RestTemplateUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.exception.user.PersonBlockedException;
import com.ruoyi.common.exception.user.UserDisabledException;
import com.ruoyi.common.utils.CachePersonUtils;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/system/UnMonitor")
@Transactional(rollbackFor = Exception.class)
public class UnMonitorController extends BaseController {

	// 日志记录
	private static final Logger log = LoggerFactory.getLogger(UnMonitorController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IOgUserService ogUserService;
	
	@Autowired
	private IOgPersonService ogPersonService;
	@Value("${sso.url}")
	private String url;

	@Value("${sso.token}")
	private String token;

	@Value("${sso.clientId}")
	private String clientId;

	/**
	 * 统一监控门户对接
	 */
	@RequestMapping("/Monitor")
	public String Monitor(HttpServletRequest request, ModelAndView model) throws ServletException, IOException {

		String loginMes = "";// 错误信息
		String tokenFlag = request.getParameter("tokenFlag");
		String userNo = request.getParameter("userNo");
//            String systemNo = request.getParameter("systemNo");
		String vapToken = request.getParameter("vapToken");
		if ("1".equals(tokenFlag)) {
			vapToken = request.getParameter("token");
		}
		log.info("第一次vapToken-------->" + vapToken );
		log.info("tokenFlag-------->" + tokenFlag );
		log.info("最终vapToken-------->" + vapToken );
		try {
			vapToken = vapToken.replace("\"", "");
			userNo = userNo.replace("\"", "");
			JSONObject json = new JSONObject();
			json.put("token", vapToken);
			json.put("userNo", userNo);
			json.put("systemNo", "2");
			if ("1".equals(tokenFlag)) {
				log.info("==============vapToken===============" + vapToken);
				for (int i = 0; i < 3; i++) {
					try {
						if (StringUtils.isNotEmpty(vapToken)) {

//                         systemNo = systemNo.replace("\"", "");
							HttpHeaders headers = new HttpHeaders();
							MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
							headers.setContentType(type);
							headers.add("Accept", MediaType.APPLICATION_JSON.toString());
							// 获取票据
							String posturl = url;
							HttpEntity<String> requests = new HttpEntity<String>(json.toString(), headers);
							log.info("----------请求url=" + posturl + ",参数----------" + "token:" + vapToken + ",userNo:"
									+ userNo);
							log.info("----------请求url=" + posturl + ",参数----------" + json.toString());
							// url开始路径是https表示生产环境，http表示测试环境
							if (posturl.startsWith("https")) {
								log.debug("--------------统一门户使用http地址----------");
								restTemplate = RestTemplateUtil.restTemplate();
							}

							// 发送请求
							ResponseEntity<String> exchange = restTemplate.postForEntity(posturl, requests,
									String.class);
							if (exchange != null) {
								log.info("------------统一监控拿到数据...------------" + exchange.getBody());
								// 返回码值200表示成功，其他表示失败
								if (exchange.getStatusCodeValue() == 200) {
									Map mapFromJson = JsonUtil.getMapFromJson(exchange.getBody());
									log.info("---------------mapFromJson:" + mapFromJson);
									JSONObject content = (JSONObject) mapFromJson.get("content");
									String account = content.get("data").toString();
									account = account.replace("\"", "");
									if ("OK".equalsIgnoreCase(account)) {
										OgUser ogUser = ogUserService.selectTimeByUsername(userNo);
										log.info("----------用户名查询:" + ogUser);
										log.info("----------oguser:" + ogUser);
										if (ogUser != null) {
											AjaxResult ajaxResult = ajaxLogin(userNo, ogUser.getPassword(), false,
													"portal");
											if (0 != Integer.valueOf(ajaxResult.get("code").toString())) {
												loginMes = ajaxResult.get("msg").toString();
												log.error(loginMes);
											}
										} else {
											loginMes = "用户名错误，请重新输入";
											log.error(loginMes);
										}
									} else {
										loginMes = "统一门户认证失败";
										log.error(loginMes + "\n" + exchange.getBody());
									}

								} else {
									loginMes = "请求统一门户调用失败，失败码值:" + exchange.getStatusCodeValue();
									log.error(loginMes);
								}
								i = 2;
							} else {
								loginMes = "status:null 登录失败,统一门户 Portal验证结果为空";
								log.error(loginMes);
							}
						} else {
							loginMes = "请先登录统一门户 Portal,在Portal通过单点登录方式访问应用";
							log.error(loginMes);
						}
					} catch (Exception e) {
						if (i == 2) {
							loginMes = "统一门户认证失败";
							throw e;
						}
						Thread.sleep(1000);
						if (i == 0) {
							String result = this.doHttpPost(url, json.toString(), "UTF-8", 30000);
							if(!"".equals(result)) {
								Map mapFromJson = JsonUtil.getMapFromJson(result);
								log.info("---------------mapFromJson:" + mapFromJson);
								JSONObject content = (JSONObject) mapFromJson.get("content");
								String account = content.get("data").toString();
								account = account.replace("\"", "");
								if ("OK".equalsIgnoreCase(account)) {
									OgUser ogUser = ogUserService.selectTimeByUsername(userNo);
									log.info("----------用户名查询:" + ogUser);
									log.info("----------oguser:" + ogUser);
									if (ogUser != null) {
										AjaxResult ajaxResult = ajaxLogin(userNo, ogUser.getPassword(), false, "portal");
										if (0 != Integer.valueOf(ajaxResult.get("code").toString())) {
											loginMes = ajaxResult.get("msg").toString();
											log.error(loginMes);
										} else {
											i = 2;
										}
									} else {
										loginMes = "用户名错误，请重新输入";
										log.error(loginMes);
									}
								} else {
									loginMes = "统一门户认证失败";
									log.error(loginMes + "\n" + result);
								}
							}
						}
					} finally {
						i++;
					}
				}
			} else {
				log.info("==============vapToken===============" + vapToken);
				if (StringUtils.isNotEmpty(vapToken)) {
					HttpHeaders headers = new HttpHeaders();
					MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
					headers.setContentType(type);
					headers.add("Accept", MediaType.APPLICATION_JSON.toString());

					// 获取票据
					String posturl = url + token;
					String postClientId = clientId;

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("vapToken", vapToken);
					jsonObject.put("clientId", postClientId);

					HttpEntity<String> requests = new HttpEntity<String>(jsonObject.toString(), headers);
					log.info("----------请求url=" + posturl + ",参数----------" + jsonObject.toString());

					/*
					 * Map<String, String> map = new HashMap(); map.put("token", token);
					 */

					// url开始路径是https表示生产环境，http表示测试环境
					if (posturl.startsWith("https")) {
						log.debug("--------------统一门户使用https地址----------");
						restTemplate = RestTemplateUtil.restTemplate();
					}

					// 发送请求
					ResponseEntity<String> exchange = restTemplate.postForEntity(posturl, requests, String.class);

					// log.info("map==============="+ map);

					if (exchange != null) {
						log.info("------------统一监控拿到数据------------" + exchange.getBody());
						// 返回码值200表示成功，其他表示失败
						if (exchange.getStatusCodeValue() == 200) {
							Map mapFromJson = JsonUtil.getMapFromJson(exchange.getBody());
							log.info("---------------mapFromJson:" + mapFromJson);
							Object account = mapFromJson.get("account");
							log.info("---------------account:" + account);
							String username = account.toString();

							OgUser ogUser = ogUserService.selectTimeByUsername(username);
							log.info("----------用户名查询:" + ogUser);

							// 用户名没有查询到使用手机号再查询一次
							if (ogUser == null) {
								ogUser = ogUserService.selectUserByPhoneNumber(username);
								log.info("----------进入手机号查询:" + ogUser);
								if (ogUser != null) {
									username = ogUser.getUsername();
									log.info("----------username:" + username);
								}
							}

							/*
							 * OgUser ogUserPhone = ogUserService.selectUserByPhoneNumber(username);
							 * log.info("----------进入手机号查询:" + ogUserPhone );
							 */

							log.info("----------oguser:" + ogUser);
							if (ogUser != null) {
								AjaxResult ajaxResult = ajaxLogin(username, ogUser.getPassword(), false, "portal");
								if (0 != Integer.valueOf(ajaxResult.get("code").toString())) {
									loginMes = ajaxResult.get("msg").toString();
									log.error(loginMes);
								}
							} else {
								loginMes = "用户名错误，请重新输入";
								log.error(loginMes);
							}

						} else {
							loginMes = "请求统一门户调用失败，失败码值:" + exchange.getStatusCodeValue();
							log.error(loginMes);
						}
					} else {
						loginMes = "status:null 登录失败,统一门户 Portal验证结果为空";
						log.error(loginMes);
					}
				} else {
					loginMes = "请先登录统一门户 Portal,在Portal通过单点登录方式访问应用";
					log.error(loginMes);
				}
			}
		} catch (Exception e) {
			log.error("------统一门户认证失败，失败原因:", e);
			e.printStackTrace();
			loginMes = "登录异常 " + e.getMessage();
			log.error("--------统一门户跳转登录认证失败，失败原因:" + e.getMessage());
		}

		if (StringUtils.isNotEmpty(loginMes)) {
			String flag = "1";
			model.addObject("flag", flag);
			CacheUtils.remove("vapToken");
			CacheUtils.put("vapToken", flag);
			return redirect("/login");
		} else {
			return redirect("/index");
		}

	}

	public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe, String loginFlag) {
		String msg = "";
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe, loginFlag);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
//			OgPerson ogPerson = new OgPerson();
//			List<Map<String, String>> ogPersonLis = ogPersonService.selectOgPersons(ogPerson);
//			for (Map<String, String> map : ogPersonLis) {
//				CachePersonUtils.put(map.get("value"), map.get("label"));
//			}
		} catch (UserDisabledException | PersonBlockedException e) {
			msg = "账户或人员已禁用请联系管理员！";
		} catch (AuthenticationException e) {
			msg = "用户或密码错误！";
			if (StringUtils.isNotEmpty(e.getMessage())) {
			}
		}
		return StringUtils.isEmpty(msg) ? success() : error(msg);
	}

	private static final String CONTENT = "Content-Type";
	private static final String JSON = "application/json";

	private static String doHttpPost(String url, String jsonstr, String charset, int timeOut) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = null;
		String result = "";
		for (int i = 0;; i++) {
			try {
				httpPost = new HttpPost(url);
				ContentProducer cp = new ContentProducer() {
					public void writeTo(OutputStream outstream) throws IOException {
						Writer writer = new OutputStreamWriter(outstream, charset);
						writer.append(jsonstr);
						writer.flush();
					}
				};
				httpPost.setHeader(CONTENT, JSON + "; charset=" + charset);
				org.apache.http.HttpEntity entity = new EntityTemplate(cp);
				httpPost.addHeader(CONTENT, JSON + "; charset=" + charset);
				httpPost.addHeader("Accept", JSON);
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost);
				if ((response != null) && (response.getStatusLine().getStatusCode() == 200)) {
					org.apache.http.HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						result = EntityUtils.toString(resEntity, charset);
					}
				} else {
					if (response == null) {
						log.error("doHttpPost response is null ");
					} else {
						log.error("doHttpPost status: " + response.getStatusLine().getStatusCode());
					}
				}
				return result;
			} catch (Exception ex) {
				if (i == 2) {
					log.error("call http get user eroor . reTry stop",ex);
					return result;
				}
				log.error("call  http get user eroor  : and it's will retry. retry num is :" + (i + 1));
			}
		}
	}
}
