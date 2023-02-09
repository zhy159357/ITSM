package com.ruoyi.common.netty.resolver;

import com.alibaba.fastjson.JSON;
import com.gosft.tools.core.thread.ThreadFactoryBuilder;
import com.gsoft.cps.forward.api.Constants;
import com.gsoft.cps.forward.api.data.ApiResData;
import com.gsoft.cps.forward.netty.msg.BaseMsg;
import com.gsoft.cps.forward.netty.msg.ResSyncMsg;
import com.gsoft.cps.forward.netty.msg.client.TranMsg;
import com.gsoft.cps.forward.netty.resolver.IResolver;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.esb.data.ResContext;
import com.ruoyi.common.netty.service.TranService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.Message;
import java.util.concurrent.*;

/**
 * 心跳测试
 *
 * @author LiuPeng
 *
 */
@Component
public class TranResolver implements IResolver {

	private Log logger = LogFactory.getLog(TranResolver.class);

	ThreadFactory tranThreadFactory = ThreadFactoryBuilder.create().setNamePrefix("tran-pool-%d").build();

	ExecutorService pool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(2048), tranThreadFactory,
			new ThreadPoolExecutor.AbortPolicy());


	@Autowired
	private TranService tranService;

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public boolean support(BaseMsg baseMsg) {
		return baseMsg instanceof TranMsg;
	}

	@Override
	public boolean run(final ChannelHandlerContext channelHandlerContext, final BaseMsg baseMsg) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				long s = System.currentTimeMillis();
				TranMsg tranMsg = (TranMsg) baseMsg;

				ApiResData apiResData = new ApiResData(Constants.RESCODE_SUCCESS);
				ResContext<?> res = new ResContext<Record>();
				try {
					res = tranService.excute(tranMsg);
				} catch (BusException e) {
					logger.warn("服务调用BusException:" + e.getMessage());
					res.setMessage(e.getExceptionMessage());
				} catch (Exception e) {
					logger.error("服务调用失败:" + tranMsg.getTrancode(), e);
					res.setMessage(new Message("999999", "服务调用失败"));
				}

				try {
					apiResData.setTotalCount(res.getTotalCount());
					if (res.getMessage() != null) {
						apiResData.setCode(res.getMessage().getCode());
						apiResData.setMsg(res.getMessage().getInfo());
					} else if (res.getRecords() != null) {
//						apiResData.setJsonData(JSON.toJSONString(res.getRecords(), filter));
						apiResData.setJsonData(JSON.toJSONString(res.getRecords()));
					} else if (res.getRecord() != null) {
//						apiResData.setJsonData(JSON.toJSONString(res.getRecord(), filter));
						apiResData.setJsonData(JSON.toJSONString(res.getRecord()));
					}
				} catch (Exception e) {
					logger.error("服务返回值处理失败:" + tranMsg.getTrancode(), e);
					res.setMessage(new Message("999999", "服务返回值处理失败"));
				}

				logger.info(tranMsg.getSyncId() + " - " + tranMsg.getTrancode() + " - 服务调用完成,time:" + (System.currentTimeMillis() - s) + ",code:"
						+ apiResData.getCode());

				ResSyncMsg resSyncMsg = new ResSyncMsg();
				resSyncMsg.setSyncId(tranMsg.getSyncId());
				resSyncMsg.setTrancode(tranMsg.getTrancode());
				resSyncMsg.setApiResData(apiResData);
				channelHandlerContext.writeAndFlush(resSyncMsg);
			}
		};
		pool.submit(thread);

		return false;
	}

}
