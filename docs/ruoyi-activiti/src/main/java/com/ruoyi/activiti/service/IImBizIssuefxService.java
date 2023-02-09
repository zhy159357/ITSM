package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.es.domain.KnowledgeTitle;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * 账号信息 服务层
 * @author dayong_sun
 */
public interface IImBizIssuefxService
{
    /**
     * 查询数据集合
     * @param issue 问题信息
     * @return 问题数据集合
     */
    public List<ImBizIssuefx> selectIssueList(ImBizIssuefx issue);

    /**
     * 查询关联事件单List
     * @param issue 问题信息
     * @return 问题数据集合
     */
    public List<ImBizIssuefx> selectRelationFmbizList(ImBizIssuefx issue);

    /**
     * 通过问题ID查询问题信息
     * @param issueinfoId 问题ID
     * @return 问题信息
     */
    public ImBizIssuefx selectImBizIssuefxById(String issueinfoId);

    /**
     * 批量删除问题信息
     * @param issueIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteIssueByIds(String issueIds);

    /**
     * 批量作废问题信息
     * @param issueIds 需要作废的数据ID
     * @return 结果
     */
    public int updateInBatchById(String issueIds);

    /**
     * 修改问题信息
     * @param issue 问题信息
     * @return 结果
     */
    public int updateIssue(ImBizIssuefx issue);

    /**
     * 新增问题信息
     * @param issue 问题信息
     * @return 结果
     */
    public int insertIssue(ImBizIssuefx issue);

    /**
     * 修改问题信息
     * @param issue 问题信息
     * @return 结果
     */
    public int changeStatus(ImBizIssuefx issue);

    /**
     * 根据条件查询用户列表
     * @param ogPerson 用户信息
     * @return 用户信息集合信息
     */
    public List<OgPerson> selectOgPersonList(OgPerson ogPerson);

    public String selectOgPersonById(String pId);

    /**
     * 查询应用系统
     */
    public List<OgSys> selectOgSysList(OgSys sys);

    /**
     * 查询协同评估人
     */
    public List<OgPerson> selectMultiusers(String userId,OgPerson ogPerson);
    /**
     * 查询数据中心人员
     */
    public List<OgPerson> selectMultiusers(OgPerson ogPerson);

    /**
     * 查询字典项
     */
    public List<PubParaValue> selectIssuesourceList(String source);

    /**
     * 查询提出机构
     */
    public List<OgOrg> selectOgOrgList();
    /**
     * 详情
     * @param issuefxId
     * @param mmap
     * @return
     * zx
     */
    public ModelMap view(String issuefxId, ModelMap mmap);
    /**
     * 批量删除wenti
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteImBizIssuefxByIds(String ids);

    /**
     * 查看建设单位报表
     * @return
     */
    public List<Map<Object, Object>> selectBulidList(Map<String,Object> map);

    /**
     * 查看数据中心报表
     * @return
     */
    public List<Map<Object, Object>> statementDataList(Map<String,Object> map);

    /**
     * 查询我处理过的任务历史
     * @param issue
     * @return
     */
    List<ImBizIssuefx> selectIssueHistroyList(ImBizIssuefx issue);

    List<ImBizIssuefx> selectTofmbiz(ImBizIssuefx imBizIssuefx);

    List<OgPerson> selectIssueByReviewer(OgPerson ogPerson);

    List<ImBizIssuefx>  selectImBizIssuefxByFmNo(String fmId);

    //查询分类集合
    public ModelMap getTypeList(ImBizIssuefx imBizIssuefx,ModelMap mmap);

    /**
     * 查询数据集合 事件单关联问题单时的查询
     * @param issue 问题信息
     * @return 问题数据集合
     */
    public List<ImBizIssuefx> listAllForEventSheet(ImBizIssuefx issue);

    //维护分类
    public List<Ztree> selectTitleTree(KnowledgeTitle knowledgeTitle);

    /**
     * 查询问题单关联事件单的个数
     *
     * @param issuefxId 问题单ID
     * @return 结果
     */
    public int selectRelationFmbizCount(String issuefxId);
}
