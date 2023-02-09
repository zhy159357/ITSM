package com.ruoyi.activiti.service.impl;


import com.ruoyi.activiti.mapper.DelayLxbgMapper;
import com.ruoyi.activiti.service.DelayLxbgService;
import com.ruoyi.common.core.domain.entity.SmBizLxbgApply;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Component
public class DelayLxbgServiceImpl implements DelayLxbgService {

    @Autowired
    private DelayLxbgMapper delayLxbgMapper;

    @Override
    public int insertDelayLxbg(SmBizLxbgApply smBizLxbgApply) {
        //转换日期格式
        if(StringUtils.isNotEmpty(smBizLxbgApply.getReleaseTime())){
            smBizLxbgApply.setReleaseTime(handleTimeYYYYMMDDHHMMSS(smBizLxbgApply.getReleaseTime()));
        }
        return delayLxbgMapper.insertDelayLxbg(smBizLxbgApply);
    }

    @Override
    public SmBizLxbgApply selectByScId(String schedulingId) {
        return delayLxbgMapper.selectByScId(schedulingId);
    }

    @Override
    public int updateSmBizLxbgapply(SmBizLxbgApply smBizLxbgApply) {
        //转换日期格式
        if(StringUtils.isNotEmpty(smBizLxbgApply.getReleaseTime())){
            smBizLxbgApply.setReleaseTime(handleTimeYYYYMMDDHHMMSS(smBizLxbgApply.getReleaseTime()));
        }
        return delayLxbgMapper.updateSmBizLxbgapply(smBizLxbgApply);
    }

    @Override
    public List<SmBizLxbgApply> selectSmBizLxbgapplyList(SmBizLxbgApply smBizLxbgApply) {
        return delayLxbgMapper.selectSmBizLxbgapplyList(smBizLxbgApply);
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
