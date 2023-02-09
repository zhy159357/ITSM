package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.common.core.domain.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 问题信息 数据层
 * @author dayong_sun
 */
@Component
public interface ImBizIssuefxMapper
{
    /**
     * 查询数据集合
     * @param issue 问题信息
     * @return 问题数据集合
     */
    public List<ImBizIssuefx> selectIssueList(ImBizIssuefx issue);


    /**
     * 通过问题ID查询问题信息
     * @param issueId 账号ID
     * @return 问题信息
     */
    public ImBizIssuefx selectIssueListById(String issueId);

    /**
     * 批量删除问题信息
     * @param issueIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteIssueByIds(Long[] issueIds);

    /**
     * 批量作废问题信息
     * @param issueIds 需要作废的数据ID
     * @return 结果
     */
    public int updateInBatchById(Long[] issueIds);

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

    /**
     * 查询应用系统
     */
    public List<OgSys> selectOgSysList(OgSys sys);

    /**
     * 查询评估人
     */
    public List<OgPerson> selectMultiusers(OgPerson ogPerson);
    List<OgPerson> selectMultiusersMysql(OgPerson ogPerson);

    public String selectOgPersonById(String pId);

    public List<PubParaValue> selectIssuesourceList(String source);

    public List<OgOrg> selectOgOrg();
    /**
     * 查询wenti
     *
     * @param issuefxId wentiID
     * @return wenti
     */
    public ImBizIssuefx selectImBizIssuefxById(String issuefxId);
    public ImBizIssuefx selectImBizIssuefxByIdMysql(String issuefxId);

    /**
     * 查询wenti列表
     *
     * @param imBizIssuefx wenti
     * @return wenti集合
     */
    public List<ImBizIssuefx> selectImBizIssuefxList(ImBizIssuefx imBizIssuefx);
    public List<ImBizIssuefx> selectImBizIssuefxListMysql(ImBizIssuefx imBizIssuefx);

    /**
     * 查询关联事件单接口
     *
     * @param imBizIssuefx wenti
     * @return wenti集合
     */
    public List<ImBizIssuefx> selectRelationFmbizList(ImBizIssuefx imBizIssuefx);
    public List<ImBizIssuefx> selectRelationFmbizListMysql(ImBizIssuefx imBizIssuefx);

    /**
     * 新增wenti
     *
     * @param imBizIssuefx wenti
     * @return 结果
     */
    public int insertImBizIssuefx(ImBizIssuefx imBizIssuefx);

    /**
     * 修改wenti
     *
     * @param imBizIssuefx wenti
     * @return 结果
     */
    public int updateImBizIssuefx(ImBizIssuefx imBizIssuefx);

    /**
     * 删除wenti
     *
     * @param issuefxId wentiID
     * @return 结果
     */
    public int deleteImBizIssuefxById(String issuefxId);

    /**
     * 批量删除wenti
     *
     * @param issuefxIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImBizIssuefxByIds(String[] issuefxIds);

    /**
     * 查询建设单位报表
     * @return
     */
    public List<Map<Object, Object>> selectBulidList(Map<String,Object> map);

    /**
     * 查询数据中心报表
     * @param map
     * @return
     */
    List<Map<Object, Object>> selectDataList(Map<String, Object> map);

    List<ImBizIssuefx> selectIssueHistroyList(ImBizIssuefx issue);

    List<ImBizIssuefx> selectTofmbiz(ImBizIssuefx imBizIssuefx);

    List<OgPerson> selectIssueByReviewer(OgPerson ogPerson);

    List<ImBizIssuefx> selectImBizIssuefxByFmNo(String fmNo);


    /**
     * 查询wenti列表 事件单关联问题单时的查询
     *
     * @param imBizIssuefx wenti
     * @return wenti集合
     */
    public List<ImBizIssuefx> listAllForEventSheet(ImBizIssuefx imBizIssuefx);
    public List<ImBizIssuefx> listAllForEventSheetMysql(ImBizIssuefx imBizIssuefx);

    /**
     * 查询问题单关联事件单的个数11
     *
     * @param issuefxId 问题单ID
     * @return 结果
     */
    public int selectRelationFmbizCount(String issuefxId);
}
