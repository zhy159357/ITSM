package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.ItsmWorkStatus;

import java.util.List;

public interface ItsmWorkStatusMapper {
    /**
     * 查询
     *
     * @param pid
     * @return
     */
    public ItsmWorkStatus selectItsmWorkStatusByPid(String pid);

    /**
     * 查询列表
     *
     * @param itsmWorkStatus
     * @return 集合
     */
    public List<ItsmWorkStatus> selectItsmWorkStatusList(ItsmWorkStatus itsmWorkStatus);

    /**
     * 新增
     *
     * @param itsmWorkStatus
     * @return 结果
     */
    public int insertItsmWorkStatus(ItsmWorkStatus itsmWorkStatus);

    /**
     * 修改
     *
     * @param itsmWorkStatus
     * @return 结果
     */
    public int updateItsmWorkStatus(ItsmWorkStatus itsmWorkStatus);

    /**
     * 删除
     *
     * @param workId ID
     * @return 结果
     */
    public int deleteItsmWorkStatusById(String workId);

    /**
     * 批量删除
     *
     * @param workIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteItsmWorkStatusByIds(String[] workIds);

    ItsmWorkStatus selectItsmWorkStatusNameById(String workId);


}
