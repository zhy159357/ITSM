package com.ruoyi.common.netty.channel;

import com.gsoft.cps.forward.netty.resolver.IResolver;
import com.ruoyi.common.netty.utils.IPPortUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author LiuPeng
 * 
 */
@Component
public class NettyServerBootstrap implements InitializingBean, ApplicationContextAware {

	private Log logger = LogFactory.getLog(NettyServerBootstrap.class);

	private int port;

	private List<IResolver> resolvers;

	private void bind() {
		logger.info("init netty server...");

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.childOption(ChannelOption.AUTO_READ, true);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					ChannelPipeline p = socketChannel.pipeline();
					p.addLast(new ObjectEncoder());
					p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					p.addLast(new NettyServerHandler(resolvers));
				}
			});
			ChannelFuture f = bootstrap.bind(port).sync();
			if (f.isSuccess()) {
				logger.info("netty server start ...");
			}
		} catch (Exception e) {
			logger.info("netty server start faild ..." + e.getMessage(), e);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		initResolvers(applicationContext);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initResolvers(ApplicationContext applicationContext) {
		if (this.resolvers == null) {
			Map<String, IResolver> resolverMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, IResolver.class, true, false);
			if (resolverMap != null) {
				this.resolvers = new ArrayList(resolverMap.values());
				Collections.sort(this.resolvers, new OrderComparator());
			}
		}
		logger.info("netty-加载消息解析类:" + resolvers);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String webPort = IPPortUtil.getLocalPort();
		port = Integer.parseInt(webPort) + 500;
		logger.info("netty-server 获取容器端口为:" + webPort + ",设置netty端口为:" + port);
		bind();
	}
}
