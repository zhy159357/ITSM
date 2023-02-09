package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.DutySchedulingMapper;
import com.ruoyi.activiti.mapper.DutySubstituteMapper;
import com.ruoyi.activiti.service.IDutySubstituteService;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.system.mapper.OgPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 值班视图Service业务层处理
 * @author dayong_sun
 * @date 2020-12-06
 */
@Service
public class DutySubstituteServiceImpl implements IDutySubstituteService {

    @Autowired
    private DutySubstituteMapper dutySubstituteMapper;
    @Autowired
    private DutySchedulingMapper dutySchedulingMapper;
    @Autowired
    private OgPersonMapper ogPersonMapper;

    /**
     * 查询替班列表
     * @param dutySubstitute 替班信息
     * @return 替班信息
     */
    @Override
    public List<DutySubstitute> selectSubstituteList(DutySubstitute dutySubstitute) {
        return dutySubstituteMapper.selectSubstituteList(dutySubstitute);
    }

    @Override
    public DutySubstitute selectSubstituteById(String substituteId){
        return dutySubstituteMapper.selectSubstituteById(substituteId);
    }

    /**
     * 修改排班信息
     * @param dutySubstitute 排班信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDutySubstitute(DutySubstitute dutySubstitute) {
        int su = dutySubstituteMapper.updateDutySubstitute(dutySubstitute);
        if("1".equals(dutySubstitute.getStatus())){
            OgPerson ogPerson = ogPersonMapper.selectOgPersonById(dutySubstitute.getTid());
            OgPerson person = ogPersonMapper.selectOgPersonById(dutySubstitute.getPid());
            DutyScheduling dutyScheduling = dutySchedulingMapper.selectDutySchedulingById(dutySubstitute.getSchedulingId());
            if(null!=dutyScheduling){
                DutyScheduling scheduling = new DutyScheduling();
                String rpid = dutyScheduling.getPid().replace(person.getpId(),dutySubstitute.getTid());
                String rphone = dutyScheduling.getMobilephone().replace(person.getMobilPhone(),ogPerson.getMobilPhone());
                String rname = dutyScheduling.getPname().replace(person.getpName(),ogPerson.getpName());
                scheduling.setSchedulingId(dutyScheduling.getSchedulingId());
                scheduling.setPid(rpid);
                scheduling.setMobilephone(rphone);
                scheduling.setPname(rname);
                dutySchedulingMapper.updateDutyScheduling(scheduling);
            }
        }
        return su;
    }

}
