package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.BizLeaveVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 请假业务Mapper接口
 *
 * @author gggggg Tech
 * @date 2019-10-11
 */
@Component
public interface BizLeaveMapper {
    /**
     * 查询请假业务
     *
     * @param id 请假业务ID
     * @return 请假业务
     */
    public BizLeaveVo selectBizLeaveById(Long id);
    /**
     * 根据流程查询请假业务
     *
     * @param instanceId 请假业务ID
     * @return 请假业务
     */
    public BizLeaveVo selectBizLeaveByInstanceId(String instanceId);
    /**
     * 查询请假业务列表
     *
     * @param bizLeave 请假业务
     * @return 请假业务集合
     */
    public List<BizLeaveVo> selectBizLeaveList(BizLeaveVo bizLeave);

    /**
     * 新增请假业务
     *
     * @param bizLeave 请假业务
     * @return 结果
     */
    public int insertBizLeave(BizLeaveVo bizLeave);

    /**
     * 修改请假业务
     *
     * @param bizLeave 请假业务
     * @return 结果
     */
    public int updateBizLeave(BizLeaveVo bizLeave);

    /**
     * 删除请假业务
     *
     * @param id 请假业务ID
     * @return 结果
     */
    public int deleteBizLeaveById(Long id);

    /**
     * 批量删除请假业务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizLeaveByIds(String[] ids);
}
