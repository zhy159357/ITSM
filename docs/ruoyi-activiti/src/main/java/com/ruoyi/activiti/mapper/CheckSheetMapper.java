package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.CheckSheet;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 隐患排查单Mapper接口
 * 
 * @author zx
 * @date 2021-01-19
 */
@Component
public interface CheckSheetMapper 
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
     * @param  checkSheet
     * @return 隐患排查单
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
     * 删除隐患排查单
     * 
     * @param csId 隐患排查单ID
     * @return 结果
     */
    public int deleteCheckSheetById(String csId);

    /**
     * 批量删除隐患排查单
     * 
     * @param csIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCheckSheetByIds(String[] csIds);

    public CheckSheet selectCheckSheetByNo(String csNo);
}
