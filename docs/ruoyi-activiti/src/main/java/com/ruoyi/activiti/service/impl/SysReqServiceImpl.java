package com.ruoyi.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.activiti.domain.SysReq;
import com.ruoyi.activiti.mapper.SysReqMapper;
import com.ruoyi.activiti.service.ISysReqService;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;

@Service("sysReq")
@Transactional
public class SysReqServiceImpl implements ISysReqService{

	@Autowired
	private SysReqMapper sysReqMapper;
	
	@Override
	public SysReq selectSysReqById(String id) {
		return sysReqMapper.selectSysReqById(id);
	}

	@Override
	public List<SysReq> selectSysReqList(SysReq sysReq) {
		return sysReqMapper.selectSysReqList(sysReq);
	}

	@Override
	public int insertSysReq(SysReq sysReq) {
		return sysReqMapper.insertSysReq(sysReq) ;
	}
	
	@Override
	public int deleteSysReqByIds(String ids) {
		return sysReqMapper.deleteSysReqByIds(Convert.toStrArray(ids));
	}
	
	@Override
	public int updateSysReq(SysReq sysReq) {
		return sysReqMapper.updateSysReq(sysReq) ;
	}

	@Override
	public Map<String, Object> selectCharge(String orgid) {
		List<OgPerson> list = sysReqMapper.selectCharge(orgid);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", list);
		return map;
	}

}
