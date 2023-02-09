package com.ruoyi.common.netty.channel;

import com.gsoft.cps.forward.netty.msg.BaseMsg;
import com.gsoft.cps.forward.netty.resolver.IResolver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * 
 * @author LiuPeng
 * 
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

	private Log logger = LogFactory.getLog(NettyServerHandler.class);

	private List<IResolver> resolvers;

	public NettyServerHandler(List<IResolver> resolvers) {
		this.resolvers = resolvers;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyChannelMap.remove((SocketChannel) ctx.channel());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
		for (IResolver resolver : resolvers) {
			if (resolver.support(baseMsg)) {
				boolean next = resolver.run(channelHandlerContext, baseMsg);
				if(!next){
					ReferenceCountUtil.release(baseMsg);
					return;
				}
			}
		}
		logger.error("unknow requst tyle :" + baseMsg.getClass().getName());
		ReferenceCountUtil.release(baseMsg);
	}
}
