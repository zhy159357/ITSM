package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.RelationLog;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-30
 */
@Component
public interface RelationLogMapper extends BaseMapper<RelationLog> {
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RelationLog selectRelationLogById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param relationLog 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RelationLog> selectRelationLogList(RelationLog relationLog);

    /**
     * 新增【请填写功能名称】
     * 
     * @param relationLog 【请填写功能名称】
     * @return 结果
     */
    public int insertRelationLog(RelationLog relationLog);

    /**
     * 修改【请填写功能名称】
     * 
     * @param relationLog 【请填写功能名称】
     * @return 结果
     */
    public int updateRelationLog(RelationLog relationLog);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRelationLogById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRelationLogByIds(String[] ids);
}
