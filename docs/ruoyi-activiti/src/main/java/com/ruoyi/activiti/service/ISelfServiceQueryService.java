package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SelfServiceQuery;
import org.apache.tomcat.jni.Mmap;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2021-11-05
 */
public interface ISelfServiceQueryService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SelfServiceQuery selectSelfServiceQueryById(String id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SelfServiceQuery> selectSelfServiceQueryList(SelfServiceQuery selfServiceQuery);

    /**
     * 新增【请填写功能名称】
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 结果
     */
    public int insertSelfServiceQuery(SelfServiceQuery selfServiceQuery);

    /**
     * 修改【请填写功能名称】
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 结果
     */
    public int updateSelfServiceQuery(SelfServiceQuery selfServiceQuery);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSelfServiceQueryByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSelfServiceQueryById(String id);

    /**
     * 查询申请
     *
     * @param selfServiceQuery
     * @return
     */
    public List<SelfServiceQuery> selectSelfServiceQueryMyList(SelfServiceQuery selfServiceQuery);

    /**
     * 暂存申请单
     *
     * @param selfServiceQuery
     * @return
     */
    public AjaxResult saveDesc(SelfServiceQuery selfServiceQuery);

    /**
     * 提交申请单
     *
     * @param selfServiceQuery
     * @return
     */
    public AjaxResult addSave(SelfServiceQuery selfServiceQuery);

    /**
     * 审计查询
     *
     * @param selfServiceQuery
     * @return
     */
    public List<SelfServiceQuery> selectSelfList(SelfServiceQuery selfServiceQuery);

    /**
     * 格式化查询条件
     *
     * @param selfServiceQuery
     * @return
     */
    public SelfServiceQuery formatSelf(SelfServiceQuery selfServiceQuery);

    /**
     * 根据传入时间查询未关闭的结束申请单
     * @param deadline
     * @return
     */
    public List<SelfServiceQuery> selectSelfServiceQueryCloseList(String deadline);

    public String saveToAuto(String id, ModelMap mmap);

    public void completeSelf(SelfServiceQuery selfServiceQuery);

    public String saveToAutoEdit(String id, ModelMap mmap);
}
