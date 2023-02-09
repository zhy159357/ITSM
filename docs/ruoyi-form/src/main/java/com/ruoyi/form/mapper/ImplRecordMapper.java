package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.ImplRecord;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-10-04
 */
public interface ImplRecordMapper 
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

    public List<Map<String,Object>> selectImplRecordListAll(ImplRecord implRecord);

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
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteImplRecordById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteImplRecordByIds(String[] ids);

    public List<ImplRecord> selectImplRecordListVo(ImplRecord implRecord);

    void issueImplRecordListVo(String[] implRecord);
    List<String> selectAllEffectUser();

    void updateImplRecordChangeTaskNo(ImplRecord implRecord);
}
