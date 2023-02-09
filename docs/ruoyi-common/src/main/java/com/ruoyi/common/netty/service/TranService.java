package com.ruoyi.common.netty.service;

import com.gsoft.cps.forward.netty.msg.client.TranMsg;
import com.ruoyi.common.esb.data.ResContext;

/**
 * 外部交易服务
 * 
 * @author LiuPeng
 * 
 */
public interface TranService {

	/**
	 * tran服务调用
	 * 
	 * @param tranMsg
	 * @return
	 */
	public ResContext<?> excute(TranMsg tranMsg) throws Exception;

}
