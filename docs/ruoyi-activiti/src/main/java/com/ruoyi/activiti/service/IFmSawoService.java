package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.FmSawo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 【态势感知工单】Service接口
 *
 * @author ruoyi
 * @date 2021-10-12
 */
public interface IFmSawoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param ordId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public FmSawo selectFmSawoById(String ordId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param fmSawo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FmSawo> selectFmSawoList(FmSawo fmSawo);

    /**
     * 新增【请填写功能名称】
     *
     * @param fmSawo 【请填写功能名称】
     * @return 结果
     */
    public int insertFmSawo(FmSawo fmSawo);

    /**
     * 修改【请填写功能名称】
     *
     * @param fmSawo 【请填写功能名称】
     * @return 结果
     */
    public int updateFmSawo(FmSawo fmSawo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmSawoByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param ordId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteFmSawoById(String ordId);

    public void sendHttpClient(FmSawo fmSawo);
}
