package com.ruoyi.common.netty.resolver;

import com.gsoft.cps.forward.netty.msg.BaseMsg;
import com.gsoft.cps.forward.netty.msg.client.LoginMsg;
import com.gsoft.cps.forward.netty.msg.server.LoginResMsg;
import com.gsoft.cps.forward.netty.resolver.IResolver;
import com.ruoyi.common.netty.channel.NettyChannelMap;
import com.ruoyi.common.netty.utils.DESUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 登录解析类
 * 
 * @author LiuPeng
 * 
 */
@Component
public class LoginResolver implements IResolver {

	private Log logger = LogFactory.getLog(LoginResolver.class);

	@Value("${netty.client.username}")
	private String username;
	@Value("${netty.client.password}")
	private String password;

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public boolean support(BaseMsg baseMsg) {
		return true;
	}

	@Override
	public boolean run(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) {
		SocketChannel channel = (SocketChannel) channelHandlerContext.channel();
		if (baseMsg.getClientId() == null) {
			logger.info(baseMsg.getClass().getName() + " - clientId为空断开连接...");
			channelHandlerContext.close();
			return false;
		}
		if (baseMsg instanceof LoginMsg) {
			LoginMsg loginMsg = (LoginMsg) baseMsg;
			LoginResMsg loginResMsg = new LoginResMsg();
			try {
				
				String loginPassword = DESUtils.decrypt(loginMsg.getPassword(), "GILE_CPS");
				
				if (username.equals(loginMsg.getUserName()) && DESUtils.decrypt(password).equals(loginPassword)) {
					// 登录成功,把channel存到服务端的map中
					NettyChannelMap.add(loginMsg.getClientId(), channel);
					logger.info("client:" + loginMsg.getClientId() + " 登录成功");
					loginResMsg.setSuccess(true);
				} else {
					logger.info("client:" + loginMsg.getClientId() + " 登录失败");
					loginResMsg.setSuccess(false);
				}
			} catch (Exception e) {
				channelHandlerContext.close();
				loginResMsg.setSuccess(false);
				logger.error("登录处理异常", e);
			}
			channelHandlerContext.writeAndFlush(loginResMsg);
			return false;
		} else {
			Channel conChannel = NettyChannelMap.get(baseMsg.getClientId());
			if (conChannel == null || conChannel != channel) {
				logger.info("client:" + baseMsg.getClientId() + ",未登录断开连接...");
				channelHandlerContext.close();
				return false;
			}
		}
		return true;
	}

}
