package com.ruoyi.activiti.mapper;

import java.util.List;

import com.ruoyi.activiti.domain.GroupReq;

public interface GroupReqMapper {
	public GroupReq selectGroupReqById(String id);
	
	public List<GroupReq> selectGroupReqList(GroupReq groupReq);

	public int insertGroupReq(GroupReq groupReq);

	public int updateGroupReq(GroupReq groupReq);

	public int deleteGroupReqByIds(String[] strArray);

	public int checkGroupReqUnique(GroupReq groupReq);

}
