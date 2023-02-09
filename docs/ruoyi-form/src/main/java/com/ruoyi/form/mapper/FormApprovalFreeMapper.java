package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.FormApprovalFree;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-10-31
 */
public interface FormApprovalFreeMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public FormApprovalFree selectFormApprovalFreeById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param formApprovalFree 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FormApprovalFree> selectFormApprovalFreeList(FormApprovalFree formApprovalFree);

    /**
     * 新增【请填写功能名称】
     * 
     * @param formApprovalFree 【请填写功能名称】
     * @return 结果
     */
    public int insertFormApprovalFree(FormApprovalFree formApprovalFree);

    /**
     * 修改【请填写功能名称】
     * 
     * @param formApprovalFree 【请填写功能名称】
     * @return 结果
     */
    public int updateFormApprovalFree(FormApprovalFree formApprovalFree);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteFormApprovalFreeById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFormApprovalFreeByIds(String[] ids);

    void updateFormApprovalFreeByCondition(FormApprovalFree formApprovalFree);
}
