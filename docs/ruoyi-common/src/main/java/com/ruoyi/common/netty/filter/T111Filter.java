package com.ruoyi.common.netty.filter;

import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.esb.data.ResContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class T111Filter extends AbstractTranFilter {

	@Override
	public String getTranName() {
		return "ogUserManager.getPagerOgUsers";
	}

	@Override
	public void reqFilter(Map<String, List<Object>> params) {
		
		
	}

	@Override
	public void resFilter(ResContext<?> res) {
		List<?> records = res.getRecords();
		if (records != null) {
			for (Object record : records) {
				if (record instanceof OgUser) {
					OgUser ogUser = (OgUser) record;
					ogUser.setUsername("哈哈哈");
				}
			}
		}
	}

}
