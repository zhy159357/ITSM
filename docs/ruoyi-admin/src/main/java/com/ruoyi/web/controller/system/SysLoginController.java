package com.ruoyi.web.controller.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.annotation.AppMobile;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.exception.user.UserDisabledException;
import com.ruoyi.common.netty.utils.DESUtils;
import com.ruoyi.common.utils.CachePersonUtils;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5OriginalUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.server.Base64;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
@Configuration
public class SysLoginController extends BaseController {

	@Autowired
	private IOgUserService ogUserService;
	@Autowired
	private IOgPersonService ogPersonService;
	@Autowired
	private IPubBizSmsService pubBizSmsService;
	@Autowired
	private RedisTemplate redisTemplate;

	@Value("${forward.encryKey}")
	private String encryKey;

	@Value("${pagehelper.helperDialect}")
	private String dbType;

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		Object obj = CacheUtils.get("vapToken");
		if (!StringUtils.isEmpty(obj)) {
			String vapobj = obj.toString();
			map.put("vapobj", vapobj);
		}
		CacheUtils.remove("vapToken");

		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
		}

		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe,
			// 验证码 String telvalidateCode,
			String loginFlag) {

		OgUser ogUser = null;
		// loginFlag为0标识用户名登录,loginFlag为1标识手机号登录，手机号登录转换为用户名，如果查询不到直接赋值为空，后续会校验报错用户名或密码错误
		if (UserConstants.LOGIN_FLAG_PHONE.equals(loginFlag)) {
			try {
				// 使用手机号查询存在多个用户的情况，mybatis框架selectOne会报错
				ogUser = ogUserService.selectUserByPhoneNumber(username);
			} catch (Exception e) {
				e.printStackTrace();
				return error("通过手机号查询到多个用户信息，请使用用户名方式登录!");
			}
			if (StringUtils.isNotNull(ogUser)) {
				username = ogUser.getUsername();
			} else {
				username = "";
			}
		}

		String msg = "";
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe
		// , telvalidateCode
		);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			cachePassword(subject.getSession().getId(), password);
			// 缓存pd-cuicc 2021-09-11
//			OgPerson ogPerson = new OgPerson();
//			List<Map<String, String>> ogPersonLis = ogPersonService.selectOgPersons(ogPerson);
//			for (Map<String, String> map : ogPersonLis) {
//				CachePersonUtils.put(map.get("value"), map.get("label"));
//			}
			CacheUtils.put(UserConstants.GETPD + username, Base64.encode4login(password));
		} catch (UserDisabledException e) {
			msg = "账户已禁用请联系管理员";
		} catch (AuthenticationException e) {
			msg = "用户或密码错误";
			if (StringUtils.isNotEmpty(e.getMessage())) {
				msg = e.getMessage();
			}
		}
		return StringUtils.isEmpty(msg) ? success() : error(msg);
	}

	@GetMapping("/unauth")
	public String unauth() {
		return "error/unauth";
	}

	/**
	 *
	 * @param loginName 用户名｜手机号码
	 * @param loginFlag 登录标识 0-用户名 1-手机号码
	 * @return 1 发送短信验证码成功 -1 该用户没有设置电话号码 -2 短信发送失败，请联系管理员！ -3 该用户不存在 -4
	 *         通过手机号查询到多个用户信息，请选择用户名登录方式获取验证码 -5 输入手机号与用户注册的手机号不匹配（首页忘记密码用） -6
	 *         短信验证码间隔时间（1分钟）内不可重复获取
	 */
	@PostMapping("/sendcode")
	@ResponseBody
	public String sendcode(String loginName, String password, String phoneNumber, String loginFlag) {
		OgUser user = null;
		if (UserConstants.LOGIN_FLAG_USERNAME.equals(loginFlag)) {
			Map<String, Object> map = new HashMap<>();
			map.put("username", loginName);
			map.put("dbType", dbType);
			user = ogUserService.selectUserByLoginName(map);
		} else if (UserConstants.LOGIN_FLAG_PHONE.equals(loginFlag)) {
			try {
				// 使用手机号查询存在多个用户的情况，mybatis框架selectOne会报错
				user = ogUserService.selectUserByPhoneNumber(loginName);
			} catch (Exception e) {
				e.printStackTrace();
				return "-4";
			}
		}
		if (StringUtils.isNotNull(user)) {
			// 校验账号是否停用，停用提示该用户不存在
			if ("0".equals(user.getInvalidationMark())) {
				return "-3";
			}
			if (StringUtils.isNotEmpty(password)) {
				if (!user.getPassword().equals(Md5OriginalUtils.md5Password(password))) {
					return "-3";
				}
			} else {
				// 忘记密码功能判断手机号是否正确
				if (!user.getMobilPhone().equals(phoneNumber)) {
					return "-5";
				}
			}
		}
		return pubBizSmsService.send(user);
	}

	@GetMapping("/forget")
	public String forget() {
		return "forget";
	}

	@GetMapping("/forgetEditPassword/{userId}")
	public String forgetEditPassword(@PathVariable("userId") String userId, ModelMap map) {
		map.put("ogUser", ogUserService.selectOgUserByUserId(userId));
		return "edit-password";
	}

	/**
	 * 忘记密码功能，验证手机号+短信验证码，成功后需要跳转到修改密码页面提示修改密码，修改密码成功后跳转到登录首页
	 * 
	 * @param username
	 * @param telvalidateCode
	 * @return
	 */
	@PostMapping("/forget")
	@ResponseBody
	public AjaxResult forget(String username, String phoneNumber, String telvalidateCode) {
		String msg = "";
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("dbType", dbType);
		OgUser ogUser = ogUserService.selectUserByLoginName(map);
		if (StringUtils.isNotNull(ogUser)) {
			if (phoneNumber.equals(ogUser.getMobilPhone())) {
				String smsCode = ogUser.getSmsCode();
				if (!telvalidateCode.equals(smsCode)) {
					msg = "短信验证码填写错误!";
				}
			} else {
				msg = "手机号填写错误!";
			}
		} else {
			msg = "用户名填写错误!";
		}
		return StringUtils.isEmpty(msg) ? success(ogUser.getUserId()) : error(msg);
	}

	/**
	 * 登录成功后将密码放入缓存做密码强度校验，因为密码是存储的密文数据，后续无法拿到真实的值
	 * 
	 * @param id
	 * @param password
	 */
	public void cachePassword(Serializable id, String password) {
		String key = "sys:login:password:" + id;
		CacheUtils.put(key, password);
	}

	private final String appTokenKey = "ITSM_MOBILE_APP";

	// 该路径现阶段这么写，待确定后要写到配置文件
	private final String appUrl = "http://20.200.84.91:9998/itsm/dist";

	/**
	 * @param request
	 * @param response custNo 柜员号 timeStamp 时间戳
	 */
	@AppMobile
	@PostMapping("/appMobile/getToken")
	public void getToken(HttpServletRequest request, HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		Map<String, Object> map = super.getAppParamsMap(request);
		String custNo = (String) map.get("custNo");
		long timeStamp = Long.valueOf((String) map.get("timeStamp"));
		long timeMillis = System.currentTimeMillis() / 1000;
		if (timeMillis - timeStamp > 30) {
			ajaxResult = error("登录获取token信息超时！");
		} else {
			OgUser ogUser = ogUserService.selectOgUserByCustNo(custNo);
			if (StringUtils.isNull(ogUser)) {
				ajaxResult = error("登录柜员号[" + custNo + "]信息不存在！");
			} else {
				Map<Object, Object> resultMap = new HashMap<>();
				resultMap.put("custNo", custNo);
				resultMap.put("appTokenKey", appTokenKey);
				// 加密形成token
				String token = DESUtils.encrypt(encryKey, JSON.toJSONString(resultMap));
				ajaxResult.put("token", token);
				ajaxResult.put("appUrl", appUrl);
				redisTemplate.opsForValue().set(custNo, token, 10 * 60L, TimeUnit.SECONDS);
			}
		}
		super.renderString(response, ajaxResult, encryKey);
	}
}
