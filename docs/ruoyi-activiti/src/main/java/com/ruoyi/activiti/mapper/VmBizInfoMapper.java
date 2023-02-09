package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.VmBizInfo;

import java.util.List;

/**
 * 【版本发布】Mapper接口
 * 
 * @author ruoyi
 * @date 2020-12-15
 */
public interface VmBizInfoMapper 
{
    /**
     * 查询列表
     * 
     * @param vmBizInfo
     * @return 集合
     */
    public List<VmBizInfo> selectVmBizInfoList(VmBizInfo vmBizInfo);

    /**
     * 根据id查询
     *
     * @param id
     * @return 集合
     */
    public VmBizInfo selectVmBizInfoById(String id);

    /**
     * 根据id查询
     *
     * @param ids
     * @return 集合
     */
    public List<VmBizInfo> selectVmBizInfoByIdList(List<String> ids);

    /**
     * 根据版本号查询
     *
     * @param versionInfoNo
     * @return 集合
     */
    public VmBizInfo selectVmBizInfoByNo(String versionInfoNo);

    /**
     * 根据条件查询
     *
     * @param vmBizInfo
     * @return
     */
    public VmBizInfo selectVmBizInfoByCondition(VmBizInfo vmBizInfo);

    /**
     * 定时任务查询用
     * @param id
     * @return
     */
    public VmBizInfo selectVmBizInfoByQuartz(String id);

    /**
     * 根据版本单号查询总数
     * @param versionInfoNo
     * @return
     */
    public int selectCountByVersionInfoNo(String versionInfoNo);

    /**
     * 新增
     * 
     * @param vmBizInfo
     * @return 结果
     */
    public int insertVmBizInfo(VmBizInfo vmBizInfo);

    /**
     * 修改
     * 
     * @param vmBizInfo
     * @return 结果
     */
    public int updateVmBizInfo(VmBizInfo vmBizInfo);

    /**
     * 批量删除
     * 
     * @param ids
     * @return 结果
     */
    public int deleteVmBizInfoByIds(String[] ids);

    /**
     * 版本发布单作废并结束工作流
     * 作废状态为2、3、4、6、7、8
     * @param status
     * @return
     */
    public List<VmBizInfo> selectVmBizInfoByVersionStatus(List<String> status);

    /**
     * 根据单号查询状态
     * @param versionInfoNo
     * @return
     */
    public String selectStatusByVersionInfoNo(String versionInfoNo);
}
