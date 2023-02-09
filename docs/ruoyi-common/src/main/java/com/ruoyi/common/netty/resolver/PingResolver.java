package com.ruoyi.common.netty.resolver;

import io.netty.channel.ChannelHandlerContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.gsoft.cps.forward.netty.msg.BaseMsg;
import com.gsoft.cps.forward.netty.msg.PingMsg;
import com.gsoft.cps.forward.netty.resolver.IResolver;

/**
 * 心跳测试
 * 
 * @author LiuPeng
 * 
 */
@Component
public class PingResolver implements IResolver {

	private Log logger = LogFactory.getLog(PingResolver.class);

	@Override
	public int getOrder() {
		return 200;
	}
	
	@Override
	public boolean support(BaseMsg baseMsg) {
		return baseMsg instanceof PingMsg;
	}

	@Override
	public boolean run(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) {
		logger.info("receive 心跳测试 from " + baseMsg.getClientId());
		return false;
	}

}
