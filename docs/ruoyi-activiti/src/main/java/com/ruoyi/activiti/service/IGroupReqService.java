package com.ruoyi.activiti.service;

import java.util.List;

import com.ruoyi.activiti.domain.GroupReq;
import com.ruoyi.common.core.domain.AjaxResult;

public interface IGroupReqService {
	/**
	 * 根据ID查询工作组申请
	 * @param id
	 * @return
	 */
    public GroupReq selectGroupReqById(String id);
    /**
     * 工作组申请/修改/注销查询
     * @param groupReq
     * @return
     */
    public List<GroupReq> selectGroupReqList(GroupReq groupReq);
    /**
     * 新增工作组申请/修改/注销
     * @param groupReq
     * @return
     */
    public int insertGroupReq(GroupReq groupReq);
    /**
     * 修改工作组申请/修改/注销
     * @param groupReq
     * @return
     */
    public int updateGroupReq(GroupReq groupReq);
    /**
     * 批量删除工作组申请/修改/注销
     * @param ids
     * @return
     */
    public int deleteGroupReqByIds(String ids);
    /**
	 * 检查工作组唯一性
	 * @param groupid
	 * @return
	 */
	public AjaxResult checkUnique(String groupid);

}
