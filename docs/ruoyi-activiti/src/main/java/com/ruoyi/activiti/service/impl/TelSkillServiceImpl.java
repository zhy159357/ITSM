package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.TelSkillsOrg;
import com.ruoyi.activiti.mapper.TelSkillMapper;
import com.ruoyi.activiti.service.ITelSkillService;
import com.ruoyi.common.core.domain.entity.TelTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-05-20
 */
@Service
public class TelSkillServiceImpl implements ITelSkillService
{
    @Autowired
    private TelSkillMapper telSkillMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TelSkillsOrg selectTelSkillById(String id)
    {
        return telSkillMapper.selectTelSkillById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telSkillsOrg 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TelSkillsOrg> selectTelSkillList(TelSkillsOrg telSkillsOrg)
    {
        return telSkillMapper.selectTelSkillList(telSkillsOrg);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param telSkillsOrg 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTelSkill(TelSkillsOrg telSkillsOrg)
    {
        return telSkillMapper.insertTelSkill(telSkillsOrg);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param telSkillsOrg 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTelSkill(TelSkillsOrg telSkillsOrg)
    {
        return telSkillMapper.updateTelSkill(telSkillsOrg);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTelSkillByIds(String ids)
    {
        return telSkillMapper.deleteTelSkillByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTelSkillById(String id)
    {
        return telSkillMapper.deleteTelSkillById(id);
    }

    @Override
    public List<TelSkillsOrg> selectTelSkillListtwo(TelSkillsOrg telSkillsOrg) {
        return telSkillMapper.selectTelSkillListtwo(telSkillsOrg);
    }

    /**
     * @param telSkillsOrg
     * @return
     */
    @Override
    public String checkBindInfoUnique(TelSkillsOrg telSkillsOrg) {
        int value=telSkillMapper.checkBindInfoUnique(telSkillsOrg);
        int value1=telSkillMapper.checkBindInfoUniqueServer(telSkillsOrg);
        String flag="0";
        if(value>0){
            flag="1";
        }else if(value1>0){
            flag="2";
        }
        return flag;
    }

    @Override
    public List<TelSkillsOrg> selectTelSkillListByName(TelSkillsOrg telSkillsOrg) {
        return telSkillMapper.selectTelSkillListByName(telSkillsOrg);
    }

    @Override
    public List<TelSkillsOrg> selectTelSkillListByTelName(TelSkillsOrg telSkillsOrg) {
        return telSkillMapper.selectTelSkillListByTelName(telSkillsOrg);
    }

}
