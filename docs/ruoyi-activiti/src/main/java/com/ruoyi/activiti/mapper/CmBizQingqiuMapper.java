package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.common.core.domain.entity.OgPerson;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 变更请求单Mapper接口
 * 
 * @author zhangxu
 * @date 2020-12-21
 */
@Component
public interface CmBizQingqiuMapper 
{
    /**
     * 查询变更请求单
     * 
     * @param changeId 变更请求单ID
     * @return 变更请求单
     */
    public CmBizQingqiu selectCmBizQingqiuById(String changeId);

    /**
     * 查询变更请求单//待受理
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
     * 删除变更请求单
     * 
     * @param changeId 变更请求单ID
     * @return 结果
     */
    public int deleteCmBizQingqiuById(String changeId);

    /**
     * 批量删除变更请求单
     * 
     * @param changeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmBizQingqiuByIds(String[] changeIds);

    /**
     * 作废变更请求单
     *
     * @param changeId 需要作废的数据ID
     * @return 结果
     */
    public int cmBizQingQiuToCancle(String changeId);

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
     * 查询分管领导
     * @param ogPerson
     * @return
     */
    public List<OgPerson> selectLeader(OgPerson ogPerson);
}
