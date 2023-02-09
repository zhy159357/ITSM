package com.ruoyi.activiti.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.CheckSheetMapper;
import com.ruoyi.activiti.domain.CheckSheet;
import com.ruoyi.activiti.service.ICheckSheetService;
import com.ruoyi.common.core.text.Convert;

/**
 * 隐患排查单Service业务层处理
 * 
 * @author zx
 * @date 2021-01-19
 */
@Service
public class CheckSheetServiceImpl implements ICheckSheetService 
{
    @Autowired
    private CheckSheetMapper checkSheetMapper;

    /**
     * 查询隐患排查单
     * 
     * @param csId 隐患排查单ID
     * @return 隐患排查单
     */
    @Override
    public CheckSheet selectCheckSheetById(String csId)
    {
        return checkSheetMapper.selectCheckSheetById(csId);
    }

    /**
     *
     * @param checkSheet
     * @return
     */
    @Override
    public CheckSheet selectCheckSheet(CheckSheet checkSheet)
    {
        return checkSheetMapper.selectCheckSheet(checkSheet);
    }
    /**
     * 查询隐患排查单列表
     * 
     * @param checkSheet 隐患排查单
     * @return 隐患排查单
     */
    @Override
    public List<CheckSheet> selectCheckSheetList(CheckSheet checkSheet)
    {
        return checkSheetMapper.selectCheckSheetList(checkSheet);
    }

    /**
     * 新增隐患排查单
     * 
     * @param checkSheet 隐患排查单
     * @return 结果
     */
    @Override
    public int insertCheckSheet(CheckSheet checkSheet)
    {
        checkSheet.setCreateTime(DateUtils.dateTimeNow());
        return checkSheetMapper.insertCheckSheet(checkSheet);
    }

    /**
     * 修改隐患排查单
     * 
     * @param checkSheet 隐患排查单
     * @return 结果
     */
    @Override
    public int updateCheckSheet(CheckSheet checkSheet)
    {
        checkSheet.setUpdateTime(DateUtils.dateTimeNow());
        return checkSheetMapper.updateCheckSheet(checkSheet);
    }

    /**
     * 删除隐患排查单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCheckSheetByIds(String ids)
    {
        return checkSheetMapper.deleteCheckSheetByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除隐患排查单信息
     * 
     * @param csId 隐患排查单ID
     * @return 结果
     */
    @Override
    public int deleteCheckSheetById(String csId)
    {
        return checkSheetMapper.deleteCheckSheetById(csId);
    }
    /**
     * 查询隐患排查单
     *
     * @param csNo 隐患排查单ID
     * @return 隐患排查单
     */
    public CheckSheet selectCheckSheetByNo(String csNo){
        return checkSheetMapper.selectCheckSheetByNo(csNo);
    }
}
