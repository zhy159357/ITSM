package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.ItsmWorkStatus;
import java.util.List;

/**
 * Service接口
 *
 * @author ruoyi
 * @date 2021-06-25
 */
public interface ItsmWorkStatusService
{
    /**
     * 查询
     *
     * @param pid ID
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
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteItsmWorkStatusByIds(String ids);

    /**
     * 删除信息
     *
     * @param workId ID
     * @return 结果
     */
    public int deleteItsmWorkStatusById(String workId);

    /**
     *判断当前登录是否为数据中心或厂商人员
     * @param userId 人员id
     * @return
     */
    public boolean isDataCenter(String userId);

    /**
     *根据代码展示名称
     * @return
     */
    ItsmWorkStatus selectItsmWorkStatusNameById(String workId);
}
