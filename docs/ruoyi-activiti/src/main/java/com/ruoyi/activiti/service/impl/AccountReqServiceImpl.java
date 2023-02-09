package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.activiti.domain.AccountReq;
import com.ruoyi.activiti.mapper.AccountReqMapper;
import com.ruoyi.activiti.service.IAccountReqService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxResult.Type;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;

@Service("accountReq")
@Transactional
public class AccountReqServiceImpl implements IAccountReqService {

	@Autowired
	private AccountReqMapper accountReqMapper;

	@Value("${pagehelper.helperDialect}")
	private String dbType;
	
	@Override
	public AccountReq selectAccountReqById(String id) {
		return accountReqMapper.selectAccountReqById(id);
	}

	@Override
	public List<AccountReq> selectAccountReqList(AccountReq accountReq) {
		if("mysql".equals(dbType)){
			return accountReqMapper.selectAccountReqListMysql(accountReq);
		}else{
			return accountReqMapper.selectAccountReqList(accountReq);
		}
	}

	@Override
	public int insertAccountReq(AccountReq accountReq) {
		if("mysql".equals(dbType)){
			return accountReqMapper.insertAccountReqMysql(accountReq) ;
		}else{
			return accountReqMapper.insertAccountReq(accountReq) ;
		}
	}
	
	@Override
	public int deleteAccountReqByIds(String ids) {
		return accountReqMapper.deleteAccountReqByIds(Convert.toStrArray(ids));
	}
	
	@Override
	public int updateAccountReq(AccountReq accountReq) {
		if("mysql".equals(dbType)){
			return accountReqMapper.updateAccountReqMysql(accountReq) ;
		}else{
			return accountReqMapper.updateAccountReq(accountReq) ;
		}
	}
	
	@Override
	public boolean checkPhone(String mobPhone, String id, String pid) {
		if(StringUtils.isEmpty(mobPhone)){
			return false;
		}
		AccountReq accountReq = new AccountReq();
		accountReq.setId(id);
		accountReq.setPid(pid);
		accountReq.setMobPhone(mobPhone);
		return accountReqMapper.checkAccountReqPhone(accountReq) > 0;
	}
	
	/*test*/
	public List<AccountReq> selectAuditor(){
		List list = new ArrayList();
		AccountReq a = new AccountReq();
		a.setId("901l");
		a.setName("AAA");
		list.add(a);
		AccountReq b = new AccountReq();
		b.setId("909l");
		b.setName("BBB");
		list.add(a);
		return list;
	}

	@Override
	public Map<String, Object> selectAuditDept(String deptid) {
		//根据 deptid 获取 postid 0010或0011		
		int isOutDept = accountReqMapper.checkOut(deptid);
		String postid = "";
		if(isOutDept == 0){
			postid = "0011";
		}else{
			postid = "0010";
		}
		List<OgOrg> list = accountReqMapper.selectAuditDept(postid);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", list);
		return map;
	}

	@Override
	public Map<String, Object> selectAuditor(String deptid,String orgid) {
		//根据 deptid 获取 postid 0010或0011
		int isOutDept = accountReqMapper.checkOut(deptid);
		String postid = "";
		if(isOutDept == 0){
			postid = "0011";
		}else{
			postid = "0010";
		}
		List<OgPerson> list = accountReqMapper.selectAuditor(postid,orgid);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", list);
		return map;
	}

	@Override
	public AjaxResult checkUnique(String pid) {
		AccountReq accountReq = new AccountReq();
		accountReq.setPid(pid);
		int result = accountReqMapper.checkAccountReqUnique(accountReq);
		if(result==0){
			return new AjaxResult(Type.SUCCESS,"true");//不存在占用该账号的申请单
		}else{
			return new AjaxResult(Type.SUCCESS,"false");
		}
	}






}
