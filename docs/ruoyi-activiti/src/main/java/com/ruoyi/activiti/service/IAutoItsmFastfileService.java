package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.entity.AutoItsmFastfile;

import java.util.List;

/**
 *
 * @author ruoyi
 * @date 2021-03-21
 */
public interface IAutoItsmFastfileService {
    /**
     *
     * @param uuid
     * @return
     */
    public AutoItsmFastfile selectAutoItsmFastfileById(String uuid);

    /**
     *
     * @param autoItsmFastfile
     * @return 集合
     */
    public List<AutoItsmFastfile> selectAutoItsmFastfileList(AutoItsmFastfile autoItsmFastfile);

    /**
     *
     * @param autoItsmFastfile
     * @return 结果
     */
    public int insertAutoItsmFastfile(AutoItsmFastfile autoItsmFastfile);

    /**
     *
     * @param autoItsmFastfile
     * @return 结果
     */
    public int updateAutoItsmFastfile(AutoItsmFastfile autoItsmFastfile);

    /**
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAutoItsmFastfileByIds(String ids);

    /**
     *
     * @param uuid
     * @return 结果
     */
    public int deleteAutoItsmFastfileById(String uuid);
}
