package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.OgSysEmergencyphone;
import com.ruoyi.activiti.mapper.OgSysEmergencyphoneMapper;
import com.ruoyi.activiti.service.IOgSysEmergencyphoneService;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.OgSysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.DateUtils.parseDate;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-07-28
 */
@Service
public class OgSysEmergencyphoneServiceImpl implements IOgSysEmergencyphoneService
{
    @Autowired
    private OgSysEmergencyphoneMapper ogSysEmergencyphoneMapper;

    @Autowired
    private OgSysMapper ogSysMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param emId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OgSysEmergencyphone selectOgSysEmergencyphoneById(String emId)
    {
        return ogSysEmergencyphoneMapper.selectOgSysEmergencyphoneById(emId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ogSysEmergencyphone 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OgSysEmergencyphone> selectOgSysEmergencyphoneList(OgSysEmergencyphone ogSysEmergencyphone)
    {
        return ogSysEmergencyphoneMapper.selectOgSysEmergencyphoneList(ogSysEmergencyphone);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param ogSysEmergencyphone 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOgSysEmergencyphone(OgSysEmergencyphone ogSysEmergencyphone)
    {
        return ogSysEmergencyphoneMapper.insertOgSysEmergencyphone(ogSysEmergencyphone);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param ogSysEmergencyphone 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOgSysEmergencyphone(OgSysEmergencyphone ogSysEmergencyphone)
    {
        ogSysEmergencyphone.setUpdateTime(DateUtils.getNowDate());
        return ogSysEmergencyphoneMapper.updateOgSysEmergencyphone(ogSysEmergencyphone);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgSysEmergencyphoneByIds(String ids)
    {
        return ogSysEmergencyphoneMapper.deleteOgSysEmergencyphoneByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param emId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOgSysEmergencyphoneById(String emId)
    {
        return ogSysEmergencyphoneMapper.deleteOgSysEmergencyphoneById(emId);
    }

    /**
     * 查询拥有工作组的系统
     *
     * @return 结果
     */
    public List<OgSys> selectOgSysTreeForEmergencyPhone(){
        return ogSysEmergencyphoneMapper.selectOgSysTreeForEmergencyPhone();
    }

    /**
     * 是否是组长
     *
     * @return 结果
     */
    public List<String> ifGroupLeader(OgSysEmergencyphone ogSysEmergencyphone){
        return ogSysEmergencyphoneMapper.ifGroupLeader(ogSysEmergencyphone);
    }

    /**
     * 导入用户数据
     * @param schedulingList 用户数据列表
     * @param operName       操作用户
     * @return 结果
     */
    @Override
    public String importEmergencyphone(List<OgSysEmergencyphone> schedulingList, String operName) {
        Map map = importList(schedulingList, operName);
        String message = map.get("message").toString();
        if ("1".equals(map.get("flag"))) {
            message = map.get("message").toString();
        } else {
            List<OgSysEmergencyphone> list = (List) map.get("list");
            ogSysEmergencyphoneMapper.importEmergencyphone(list);
        }
        return message;
    }

    public Map importList(List<OgSysEmergencyphone> schedulingList, String updatePerson) {
        Map<String, Object> map = new HashMap<>();
        List<OgSysEmergencyphone> list = new ArrayList<>();
        if (StringUtils.isNull(schedulingList) || schedulingList.size() == 0) {
            map.put("flag", "1");
            map.put("message", "导入用户数据不能为空");
            return map;
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (OgSysEmergencyphone ogSysEmergencyphone : schedulingList) {
            try {
                String sysId = "";
                String sysName = ogSysEmergencyphone.getSysName();
                OgSys ogSys = new OgSys();
                ogSys.setCaption(sysName);
                ogSys.setInvalidationMark("1");
                List<OgSys> ogSysList = ogSysMapper.selectOgSysListForCaption(ogSys);
                if (null != ogSysList && ogSysList.size() > 0) {
                    sysId = ogSysList.get(0).getSysId();
                }
                ogSysEmergencyphone.setSysId(sysId);
                ogSysEmergencyphone.setAddTime(DateUtils.getTime());
                ogSysEmergencyphone.setEmId(UUID.getUUIDStr());
                ogSysEmergencyphone.setInvalipationMark("1");
                ogSysEmergencyphone.setUpdatePerson(ShiroUtils.getUserId());
                list.add(ogSysEmergencyphone);
                successNum++;
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、姓名 " + ogSysEmergencyphone.getPname() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            map.put("flag", "1");
            map.put("message", failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条");
            map.put("flag", "0");
            map.put("message", successMsg.toString());
            map.put("list", list);
        }
        return map;
    }
}
