package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;

import java.util.List;

/**
 * 变更请求单Service接口
 * 
 * @author zhangxu
 * @date 2020-12-21
 */
public interface ICmBizQingqiuService 
{
    /**
     * 查询变更请求单
     * 
     * @param changeId 变更请求单ID
     * @return 变更请求单
     */
    public CmBizQingqiu selectCmBizQingqiuById(String changeId);

    /**
     * 查询变更请求单 //待受理
     *
     * @param changeId 变更请求单ID
     * @return 变更请求单
     */
    public CmBizQingqiu selectCmBizQingqiuAcceptanceById(String changeId);

    /**
     * 查询变更请求单//待审批//待受理
     *
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单
     */
    public CmBizQingqiu selectBGQQVO(CmBizQingqiu cmBizQingqiu);

    /**
     * 变更请求单列表
     * 
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单集合
     */
    public List<CmBizQingqiu> selectCmBizQingqiuList(CmBizQingqiu cmBizQingqiu);

    /**
     * 查询变更请求单列表
     *
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单集合
     */
    public List<CmBizQingqiu> selectQingqiuList(CmBizQingqiu cmBizQingqiu);

    /**
     * 新增变更请求单
     * 
     * @param cmBizQingqiu 变更请求单
     * @return 结果
     */
    public int insertCmBizQingqiu(CmBizQingqiu cmBizQingqiu);

    /**
     * 修改变更请求单
     * 
     * @param cmBizQingqiu 变更请求单
     * @return 结果
     */
    public int updateCmBizQingqiu(CmBizQingqiu cmBizQingqiu);

    /**
     * 批量删除变更请求单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmBizQingqiuByIds(String ids);

    /**
     * 删除变更请求单信息
     * 
     * @param changeId 变更请求单ID
     * @return 结果
     */
    public int deleteCmBizQingqiuById(String changeId);

    /**
     * 作废变更请求单
     *
     * @param id 需要作废的数据ID
     * @return 结果
     */
    public int cmBizQingQiuToCancle(String id);

    /**
     * 710012查询分管领导方法
     * @param cmBizQingqiu
     * @return
     */
    public List<OgPerson> secondaryDeptLeader(CmBizQingqiu cmBizQingqiu);

    /**
     * 查询变更请求单列表(排除待提交的)
     *
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单集合
     */
    public List<CmBizQingqiu> selectCmBizQingqiuListNotNew(CmBizQingqiu cmBizQingqiu);

    /**
     * 查询协同受理人
     *
     * @param cmBizQingqiu 变更请求单
     * @return 协同受理人集合
     */
    public List<OgPerson> togetherAcceptancePerson(CmBizQingqiu cmBizQingqiu);

    /**
     * 查询分管领导方法
     * @param ogPerson
     * @return
     */
    public List<OgPerson> selectLeader(OgPerson ogPerson);

    /**
     * 根据机构id获取下级机构（包括父级机构）
     * @param orgId
     * @return
     */
    List<OgOrg> selectAllChildOrg(String orgId);

    /**
     * 移动APP分管领导审批变更请求单
     * @param cmBizQingqiu
     * @param userId
     * @param trend
     * @return
     */
    public AjaxResult checkCmBizQingQiu(CmBizQingqiu cmBizQingqiu, String userId, String trend);

    /**
     * 移动APP分管领导查看近一月已办
     * @param bizIds
     * @return
     */
    public List<CmBizQingqiu> selectQingQiuListApp(List<String> bizIds, String search);
}
