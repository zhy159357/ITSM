package com.ruoyi.form.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.FormApprovalFreeMapper;
import com.ruoyi.form.domain.FormApprovalFree;
import com.ruoyi.form.service.IFormApprovalFreeService;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-10-31
 */
@Service
public class FormApprovalFreeServiceImpl implements IFormApprovalFreeService 
{
    @Autowired
    private FormApprovalFreeMapper formApprovalFreeMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public FormApprovalFree selectFormApprovalFreeById(Long id)
    {
        return formApprovalFreeMapper.selectFormApprovalFreeById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param formApprovalFree 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FormApprovalFree> selectFormApprovalFreeList(FormApprovalFree formApprovalFree)
    {
        return formApprovalFreeMapper.selectFormApprovalFreeList(formApprovalFree);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param formApprovalFree 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFormApprovalFree(FormApprovalFree formApprovalFree)
    {
        return formApprovalFreeMapper.insertFormApprovalFree(formApprovalFree);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param formApprovalFree 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFormApprovalFree(FormApprovalFree formApprovalFree)
    {
        return formApprovalFreeMapper.updateFormApprovalFree(formApprovalFree);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFormApprovalFreeByIds(String ids)
    {
        return formApprovalFreeMapper.deleteFormApprovalFreeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteFormApprovalFreeById(Long id)
    {
        return formApprovalFreeMapper.deleteFormApprovalFreeById(id);
    }

    @Override
    public AjaxResult updateFormApprovalFreeByCondition(FormApprovalFree formApprovalFree) {
        formApprovalFreeMapper.updateFormApprovalFreeByCondition(formApprovalFree);
        return AjaxResult.success();
    }
}
