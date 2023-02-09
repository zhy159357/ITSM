package com.ruoyi.activiti.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.domain.SysReq;

public interface ISysReqService {
	/**
	 * 根据ID查询应用系统申请
	 * @param id
	 * @return
	 */
	public SysReq selectSysReqById(String id);
	/**
	 * 应用系统申请/修改/注销查询
	 * @param sysReq
	 * @return
	 */
	public List<SysReq> selectSysReqList(SysReq sysReq);
	/**
	 * 新增应用系统申请/修改/注销
	 * @param sysReq
	 * @return
	 */
	public int insertSysReq(SysReq sysReq);
	/**
	 * 修改应用系统申请/修改/注销
	 * @param sysReq
	 * @return
	 */
	public int updateSysReq(SysReq sysReq);
	/**
	 * 批量删除应用系统申请/修改/注销
	 * @param ids
	 * @return
	 */
	public int deleteSysReqByIds(String ids);
	/**
	 * 选择所属负责人
	 * @param orgid
	 * @return
	 */
	public Map<String, Object> selectCharge(String orgid);
}
