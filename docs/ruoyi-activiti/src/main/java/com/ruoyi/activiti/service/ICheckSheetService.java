package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.CheckSheet;
import java.util.List;

/**
 * 隐患排查单Service接口
 * 
 * @author zx
 * @date 2021-01-19
 */
public interface ICheckSheetService 
{
    /**
     * 查询隐患排查单
     * 
     * @param csId 隐患排查单ID
     * @return 隐患排查单
     */
    public CheckSheet selectCheckSheetById(String csId);
    /**
     * 查询隐患排查单
     *
     * @param csNo 隐患排查单ID
     * @return 隐患排查单
     */
    public CheckSheet selectCheckSheetByNo(String csNo);

    /**
     *
     * @param checkSheet
     * @return
     */
    public CheckSheet selectCheckSheet(CheckSheet checkSheet);
    /**
     * 查询隐患排查单列表
     * 
     * @param checkSheet 隐患排查单
     * @return 隐患排查单集合
     */
    public List<CheckSheet> selectCheckSheetList(CheckSheet checkSheet);

    /**
     * 新增隐患排查单
     * 
     * @param checkSheet 隐患排查单
     * @return 结果
     */
    public int insertCheckSheet(CheckSheet checkSheet);

    /**
     * 修改隐患排查单
     * 
     * @param checkSheet 隐患排查单
     * @return 结果
     */
    public int updateCheckSheet(CheckSheet checkSheet);

    /**
     * 批量删除隐患排查单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCheckSheetByIds(String ids);

    /**
     * 删除隐患排查单信息
     * 
     * @param csId 隐患排查单ID
     * @return 结果
     */
    public int deleteCheckSheetById(String csId);
}
