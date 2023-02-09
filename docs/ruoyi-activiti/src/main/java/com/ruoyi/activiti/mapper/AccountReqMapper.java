package com.ruoyi.activiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.activiti.domain.AccountReq;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;

public interface AccountReqMapper {
	
	public AccountReq selectAccountReqById(String id);
	
	public List<AccountReq> selectAccountReqList(AccountReq accountReq);

	public List<AccountReq> selectAccountReqListMysql(AccountReq accountReq);

	public int insertAccountReq(AccountReq accountReq);

	public int insertAccountReqMysql(AccountReq accountReq);

	public int updateAccountReq(AccountReq accountReq);

	public int updateAccountReqMysql(AccountReq accountReq);

	public int deleteAccountReqByIds(String[] strArray);

	public int checkAccountReqPhone(AccountReq accountReq);

	public List<OgOrg> selectAuditDept(String postid);

	public List<OgPerson> selectAuditor(@Param("postid")String postid, @Param("orgid")String orgid);

	public int checkOut(String deptid);

	public int checkAccountReqUnique(AccountReq accountReq);
}
