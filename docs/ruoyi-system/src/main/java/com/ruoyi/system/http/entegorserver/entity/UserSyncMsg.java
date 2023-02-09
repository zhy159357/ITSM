package com.ruoyi.system.http.entegorserver.entity;

import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 
 * @author Administrator
 *
 */

public class UserSyncMsg extends BaseEntity {

	private String action;

	private List<UserSyncApproval> demand;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<UserSyncApproval> getDemand() {
		return demand;
	}

	public void setDemand(List<UserSyncApproval> demand) {
		this.demand = demand;
	}
}
