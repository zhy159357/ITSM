package com.ruoyi.activiti.service.impl;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.domain.TelSkillsOrg;
import com.ruoyi.activiti.mapper.OgPhoneMapper;
import com.ruoyi.activiti.service.IOgPhoneService;
import com.ruoyi.activiti.service.ITelSkillService;
import com.ruoyi.common.core.domain.entity.TelTerminal;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ITelTerminalService;

@Service
public class OgPhoneServiceImpl implements IOgPhoneService
{
    @Autowired
    private OgPhoneMapper phoneMapper;
    @Autowired
    private ITelTerminalService telTerminalService;
    @Autowired
    private ITelSkillService skillService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public List<TelBiz> selectPhoneList(TelBiz telBiz) {
        return phoneMapper.selectPhoneList(telBiz);
    }

    @Override
    public int insertOgPhone(TelBiz telBiz) {
        return phoneMapper.insertOgPhone(telBiz);
    }

    @Override
    public TelBiz selectPhoneById(String telid) {
        return  phoneMapper.selectPhoneById(telid);
    }

    @Override
    public int updatePhone(TelBiz telBiz) {
        return phoneMapper.updatePhone(telBiz);
    }

    @Override
    public int deletePhoneByIds(String ids){
        String[] list= Convert.toStrArray(",",ids);
        return phoneMapper.deletePhoneByIds(list);
    }

    @Override
    public List<TelBiz> selectPhoneDeptNameList(TelBiz telBiz) {
        List<TelBiz> list;
        if("mysql".equals(dbType)){
            list = phoneMapper.selectPhoneDeptNameListMysql(telBiz);
        }else{
            list = phoneMapper.selectPhoneDeptNameList(telBiz);
        }
        return list;
    }

    @Override
    public List<TelBiz> selectPhoneSysIdList(TelBiz telBiz) {
        List<TelBiz> list;
        if("mysql".equals(dbType)){
            list = phoneMapper.selectPhoneSysIdListMysql(telBiz);
        }else{
            list = phoneMapper.selectPhoneSysIdList(telBiz);
        }
        return list;
    }

    @Override
    public List<TelBiz> selectPhoneDeptNameAndSysIdList(TelBiz telBiz) {
        List<TelBiz> list;
        if("mysql".equals(dbType)){
            list = phoneMapper.selectPhoneDeptNameAndSysIdListMysql(telBiz);
        }else{
            list = phoneMapper.selectPhoneDeptNameAndSysIdList(telBiz);
        }
        return list;
    }
    @Override
    public String getGroupNo(){
            String groupNo = "";
            String userId = ShiroUtils.getOgUser().getUserId();
            TelTerminal tt = new TelTerminal();
            tt.setJobNumber(userId);
            List<TelTerminal> ttList = telTerminalService.selectTelTerminalListByJobNumber(tt);
            if (!ttList.isEmpty()) {
                String skillId = ttList.get(0).getSkillId();
                if (StringUtils.isNotEmpty(skillId)) {
                    TelSkillsOrg tsOrg = skillService.selectTelSkillById(skillId);
                    if (!StringUtils.isEmpty(tsOrg)) {
                        return  tsOrg.getSkillsGroupName();
                    }
                }
            }
            return groupNo;
    }
}
