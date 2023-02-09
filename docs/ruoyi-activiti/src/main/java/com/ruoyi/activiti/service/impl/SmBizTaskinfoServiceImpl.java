package com.ruoyi.activiti.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.activiti.mapper.SmBizTaskinfoMapper;
import com.ruoyi.activiti.service.ISmBizTaskinfoService;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfo;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfoThree;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfoTwo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
@Service
public class SmBizTaskinfoServiceImpl implements ISmBizTaskinfoService
{
    @Autowired
    private SmBizTaskinfoMapper smBizTaskinfoMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;
    /**
     * 查询【请填写功能名称】
     * 
     * @param taskFormId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SmBizTaskinfo selectSmBizTaskinfoById(String taskFormId)
    {
        return smBizTaskinfoMapper.selectSmBizTaskinfoById(taskFormId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SmBizTaskinfo> selectSmBizTaskinfoList(SmBizTaskinfo smBizTaskinfo)
    {
        return smBizTaskinfoMapper.selectSmBizTaskinfoList(smBizTaskinfo);
    }


    /**
     * 查询【请填写功能名称】列表
     *
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SmBizTaskinfo> selectSmBizTaskinfoListtwo(SmBizTaskinfo smBizTaskinfo)
    {
        smBizTaskinfo.setDbType(dbType);
        return smBizTaskinfoMapper.selectSmBizTaskinfoListtwo(smBizTaskinfo);
    }


    /**
     * 新增【请填写功能名称】
     * 
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSmBizTaskinfo(SmBizTaskinfo smBizTaskinfo)
    {
        if(StringUtils.isNotEmpty(smBizTaskinfo.getGenerateTime())){
            smBizTaskinfo.setGenerateTime(handleTimeYYYYMMDDHHMMSS(smBizTaskinfo.getGenerateTime()));
        }
        if(StringUtils.isNotEmpty(smBizTaskinfo.getUpdateTime())){
            smBizTaskinfo.setUpdateTime(handleTimeYYYYMMDDHHMMSS(smBizTaskinfo.getUpdateTime()));
        }
        return smBizTaskinfoMapper.insertSmBizTaskinfo(smBizTaskinfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSmBizTaskinfo(SmBizTaskinfo smBizTaskinfo)
    {
        return smBizTaskinfoMapper.updateSmBizTaskinfo(smBizTaskinfo);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSmBizTaskinfoByIds(String ids)
    {
        return smBizTaskinfoMapper.deleteSmBizTaskinfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param taskFormId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSmBizTaskinfoById(String taskFormId)
    {
        return smBizTaskinfoMapper.deleteSmBizTaskinfoById(taskFormId);
    }

    /**
     *
     * @param
     * @return
     */
    @Override
    public List<SmBizTaskinfoTwo> selectSmBizTaskinfoSList(SmBizTaskinfoTwo smBizTaskinfoTwo) {
        smBizTaskinfoTwo.setDbType(dbType);
        return smBizTaskinfoMapper.selectSmBizTaskinfoSList(smBizTaskinfoTwo);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public SmBizTaskinfo selectSmBizTaskinfoSelById(String id) {
        return smBizTaskinfoMapper.selectSmBizTaskinfoSelById(id);
    }

    @Override
    public List<SmBizTaskinfo> selectPubLishTask() {
        return smBizTaskinfoMapper.selectPubLishTask();
    }

    /**
     * 查询任务列表
     * @param smBizTaskinfoThree
     * @return
     */
    @Override
    public List<SmBizTaskinfoThree> selectSmBizTaskinfoListEvo(SmBizTaskinfoThree smBizTaskinfoThree) {
        smBizTaskinfoThree.setDbType(dbType);
        return smBizTaskinfoMapper.selectSmBizTaskinfoListEvo(smBizTaskinfoThree);
    }

    @Override
    public int deleteSmBizTaskinfoZyById(String taskId) {
        return smBizTaskinfoMapper.deleteSmBizTaskinfoZyById(taskId);
    }

    @Override
    public SmBizTaskinfo selectSmBizTaskinfoByTaskId(String taskId) {
        return smBizTaskinfoMapper.selectSmBizTaskinfoByTaskId(taskId);
    }

    @Override
    public List<SmBizTaskinfoTwo> selectSmBizTaskinfoTwoSList(SmBizTaskinfoTwo smBizTaskinfoTwo) {
        return smBizTaskinfoMapper.selectSmBizTaskinfoTwoSList(smBizTaskinfoTwo);
    }

    @Override
    public int updateSmBizTaskinfoByTaskId(SmBizTaskinfo smBizTaskinfo) {
        return smBizTaskinfoMapper.updateSmBizTaskinfoByTaskId(smBizTaskinfo);
    }

    @Override
    public List<SmBizTaskinfo> selectFillLxbgList(SmBizTaskinfo smBizTaskinfo) {
        return smBizTaskinfoMapper.selectFillLxbgList(smBizTaskinfo);
    }


    /**
     * 将yyyy-MM-dd HH:mm:ss类型的时间格式化为yyyyMMddHHmmss
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDDHHMMSS(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, date);
        return formatDateStr;
    }

}
