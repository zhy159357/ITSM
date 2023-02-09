package com.ruoyi.activiti.mapper;

import java.util.List;

import com.ruoyi.activiti.domain.SysReq;
import com.ruoyi.common.core.domain.entity.OgPerson;

public interface SysReqMapper {
	public SysReq selectSysReqById(String id);
	
	public List<SysReq> selectSysReqList(SysReq sysReq);

	public int insertSysReq(SysReq sysReq);

	public int updateSysReq(SysReq sysReq);

	public int deleteSysReqByIds(String[] strArray);

	public List<OgPerson> selectCharge(String orgid);
}
