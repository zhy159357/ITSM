package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.TelSkillsOrg;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-05-20
 */
public interface TelSkillMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TelSkillsOrg selectTelSkillById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telSkillsOrg 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelSkillsOrg> selectTelSkillList(TelSkillsOrg telSkillsOrg);

    /**
     * 新增【请填写功能名称】
     * 
     * @param telSkillsOrg 【请填写功能名称】
     * @return 结果
     */
    public int insertTelSkill(TelSkillsOrg telSkillsOrg);

    /**
     * 修改【请填写功能名称】
     * 
     * @param telSkillsOrg 【请填写功能名称】
     * @return 结果
     */
    public int updateTelSkill(TelSkillsOrg telSkillsOrg);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTelSkillById(String id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTelSkillByIds(String[] ids);

    List<TelSkillsOrg> selectTelSkillListtwo(TelSkillsOrg telSkillsOrg);

    int checkBindInfoUnique(TelSkillsOrg telSkillsOrg);

    int checkBindInfoUniqueServer(TelSkillsOrg telSkillsOrg);

    List<TelSkillsOrg> selectTelSkillListByName(TelSkillsOrg telSkillsOrg);

    List<TelSkillsOrg> selectTelSkillListByTelName(TelSkillsOrg telSkillsOrg);
}
