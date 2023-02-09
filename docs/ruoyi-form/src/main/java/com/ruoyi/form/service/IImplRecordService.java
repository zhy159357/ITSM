package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.ImplRecord;
import com.ruoyi.form.enums.ChangeTaskStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-10-04
 */
public interface IImplRecordService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ImplRecord selectImplRecordById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param implRecord 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ImplRecord> selectImplRecordList(ImplRecord implRecord);

    /**
     * 新增【请填写功能名称】
     * 
     * @param implRecord 【请填写功能名称】
     * @return 结果
     */
    public int insertImplRecord(ImplRecord implRecord);

    /**
     * 修改【请填写功能名称】
     * 
     * @param implRecord 【请填写功能名称】
     * @return 结果
     */
    public int updateImplRecord(ImplRecord implRecord);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteImplRecordByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteImplRecordById(Long id);
    List<Map<String,Object>> getChangeTaskListByStatusAndCurrentUserId(String currentUserId, ChangeTaskStatusEnum changeTaskStatusEnum);
    List<Map<String,Object>> getChangeTaskListByStatusAndCurrentUserIdAll(String userId);
    List<ImplRecord> selectImplRecordListVo(ImplRecord implRecord);

    public void issueImplRecordListVo(String[] implRecord);
    public List<String> selectAllEffectUser();
    void addReMap(List<Map<String, Object>> topList, List<Map<String, Object>> allList, List<Map<String, Object>> list, Map<String, Object> reMap,Object version,String userName);

    void updateImplRecordChangeTaskNo(ImplRecord implRecord);
}
