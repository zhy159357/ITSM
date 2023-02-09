package com.ruoyi.activiti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.activiti.domain.AccountReq;
import com.ruoyi.activiti.domain.GroupReq;
import com.ruoyi.activiti.mapper.GroupReqMapper;
import com.ruoyi.activiti.service.IGroupReqService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxResult.Type;
import com.ruoyi.common.core.text.Convert;

@Service("groupReq")
@Transactional
public class GroupReqServiceImpl implements IGroupReqService{

	@Autowired
	private GroupReqMapper groupReqMapper;
	
	@Override
	public GroupReq selectGroupReqById(String id) {
		return groupReqMapper.selectGroupReqById(id);
	}

	@Override
	public List<GroupReq> selectGroupReqList(GroupReq groupReq) {
		return groupReqMapper.selectGroupReqList(groupReq);
	}

	@Override
	public int insertGroupReq(GroupReq groupReq) {
		return groupReqMapper.insertGroupReq(groupReq) ;
	}
	
	@Override
	public int deleteGroupReqByIds(String ids) {
		return groupReqMapper.deleteGroupReqByIds(Convert.toStrArray(ids));
	}
	
	@Override
	public int updateGroupReq(GroupReq groupReq) {
		return groupReqMapper.updateGroupReq(groupReq) ;
	}

	@Override
	public AjaxResult checkUnique(String groupid) {
		GroupReq groupReq = new GroupReq();
		groupReq.setGroupid(groupid);
		int result = groupReqMapper.checkGroupReqUnique(groupReq);
		if(result==0){
			return new AjaxResult(Type.SUCCESS,"true");//不存在占用该账号的申请单
		}else{
			return new AjaxResult(Type.SUCCESS,"false");
		}
	}
}
