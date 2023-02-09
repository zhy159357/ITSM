package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.SelfServiceQuery;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-05
 */
public interface SelfServiceQueryMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SelfServiceQuery selectSelfServiceQueryById(String id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SelfServiceQuery> selectSelfServiceQueryList(SelfServiceQuery selfServiceQuery);

    /**
     * 新增【请填写功能名称】
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 结果
     */
    public int insertSelfServiceQuery(SelfServiceQuery selfServiceQuery);

    /**
     * 修改【请填写功能名称】
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 结果
     */
    public int updateSelfServiceQuery(SelfServiceQuery selfServiceQuery);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSelfServiceQueryById(String id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSelfServiceQueryByIds(String[] ids);

    public List<SelfServiceQuery> selectSelfServiceQueryMyList(SelfServiceQuery selfServiceQuery);

    public List<SelfServiceQuery> selectSelfList(SelfServiceQuery selfServiceQuery);

    public List<SelfServiceQuery> selectSelfServiceQueryCloseList(String deadline);
}
