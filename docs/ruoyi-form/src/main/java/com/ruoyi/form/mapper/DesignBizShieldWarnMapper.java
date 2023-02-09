package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.DesignBizShieldWarn;
import java.util.List;

/**
 * 屏蔽告警Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-22
 */
public interface DesignBizShieldWarnMapper 
{
    /**
     * 查询屏蔽告警
     * 
     * @param id 屏蔽告警ID
     * @return 屏蔽告警
     */
    public DesignBizShieldWarn selectDesignBizShieldWarnById(Long id);

    List<DesignBizShieldWarn> selectDesignBizShieldWarnByChangeTaskNo(String changeTaskNo);

    void updateDesignBizShieldWarnByChangeTaskNo(DesignBizShieldWarn designBizShieldWarn);

    /**
     * 查询屏蔽告警列表
     * 
     * @param designBizShieldWarn 屏蔽告警
     * @return 屏蔽告警集合
     */
    public List<DesignBizShieldWarn> selectDesignBizShieldWarnList(DesignBizShieldWarn designBizShieldWarn);

    Integer selectCount(DesignBizShieldWarn designBizShieldWarn);

    /**
     * 新增屏蔽告警
     * 
     * @param designBizShieldWarn 屏蔽告警
     * @return 结果
     */
    public int insertDesignBizShieldWarn(DesignBizShieldWarn designBizShieldWarn);

    /**
     * 修改屏蔽告警
     * 
     * @param designBizShieldWarn 屏蔽告警
     * @return 结果
     */
    public int updateDesignBizShieldWarn(DesignBizShieldWarn designBizShieldWarn);

    /**
     * 删除屏蔽告警
     * 
     * @param id 屏蔽告警ID
     * @return 结果
     */
    public int deleteDesignBizShieldWarnById(Long id);

    /**
     * 批量删除屏蔽告警
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDesignBizShieldWarnByIds(String[] ids);
}
