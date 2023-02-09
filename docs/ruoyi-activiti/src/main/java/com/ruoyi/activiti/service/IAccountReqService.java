package com.ruoyi.activiti.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.domain.AccountReq;
import com.ruoyi.common.core.domain.AjaxResult;

public interface IAccountReqService {
	
	/**
	 * 根据ID查询账号申请
	 * @param id
	 * @return
	 */
	public AccountReq selectAccountReqById(String id);
	/**
	 * 账号申请/修改/注销查询
	 * @param accountReq
	 * @return
	 */
	public List<AccountReq> selectAccountReqList(AccountReq accountReq);
	/**
	 * 新增账号申请/修改/注销
	 * @param accountReq
	 * @return
	 */
	public int insertAccountReq(AccountReq accountReq);
	/**
	 * 修改账号申请/修改/注销
	 * @param accountReq
	 * @return
	 */
	public int updateAccountReq(AccountReq accountReq);
	/**
	 * 批量删除账号申请/修改/注销
	 * @param ids
	 * @return
	 */
	public int deleteAccountReqByIds(String ids);
	/**
	 * 检查手机号唯一性
	 * @param mobPhone
	 * @param id
	 * @param pid
	 * @return
	 */
	public boolean checkPhone(String mobPhone, String id, String pid);
	/**
	 * 选择审核/处理部门
	 * @param deptid
	 * @return
	 */
	public Map<String,Object> selectAuditDept(String deptid);
	/**
	 * 选择审核/处理人
	 * @param deptid
	 * @param orgid
	 * @return
	 */
	public Map<String, Object> selectAuditor(String deptid, String orgid);
	/**
	 * 检查账号唯一性
	 * @param pid
	 * @return
	 */
	public AjaxResult checkUnique(String pid);

}
