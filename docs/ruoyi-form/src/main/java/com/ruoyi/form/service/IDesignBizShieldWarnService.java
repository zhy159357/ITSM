package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.DesignBizShieldWarn;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * 屏蔽告警Service接口
 * 
 * @author ruoyi
 * @date 2022-11-22
 */
public interface IDesignBizShieldWarnService 
{
    /**
     * 查询屏蔽告警
     * 
     * @param id 屏蔽告警ID
     * @return 屏蔽告警
     */
    public DesignBizShieldWarn selectDesignBizShieldWarnById(Long id);

    /**
     * 查询屏蔽告警列表
     * 
     * @param designBizShieldWarn 屏蔽告警
     * @return 屏蔽告警集合
     */
    public List<DesignBizShieldWarn> selectDesignBizShieldWarnList(DesignBizShieldWarn designBizShieldWarn,Integer pageNum,Integer pageSize);

    Integer count(DesignBizShieldWarn designBizShieldWarn);

    public List<DesignBizShieldWarn> selectDesignBizShieldWarnByChangeTaskNo(String changeTaskNo);

    AjaxResult updateDesignBizShieldWarnByChangeTaskNo(DesignBizShieldWarn designBizShieldWarn);

    AjaxResult parseShieldWarnExcel(String changeTaskNo,  MultipartFile file);

    /**
     * 新增屏蔽告警
     * 
     * @param designBizShieldWarn 屏蔽告警
     * @return 结果
     */
    public AjaxResult insertDesignBizShieldWarn(DesignBizShieldWarn designBizShieldWarn);

    /**
     * 修改屏蔽告警
     * 
     * @param designBizShieldWarn 屏蔽告警
     * @return 结果
     */
    public int updateDesignBizShieldWarn(DesignBizShieldWarn designBizShieldWarn);

    /**
     * 批量删除屏蔽告警
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDesignBizShieldWarnByIds(String ids);

    /**
     * 删除屏蔽告警信息
     * 
     * @param id 屏蔽告警ID
     * @return 结果
     */
    public int deleteDesignBizShieldWarnById(Long id);
}
