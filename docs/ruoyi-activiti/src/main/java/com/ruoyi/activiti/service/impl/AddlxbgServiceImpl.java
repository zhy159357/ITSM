package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.AddlxbgMapper;
import com.ruoyi.activiti.service.AddlxbgService;
import com.ruoyi.common.core.domain.entity.SmBizFolder;
import com.ruoyi.common.core.domain.entity.SmBizScheduling;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Component
public class AddlxbgServiceImpl implements AddlxbgService {

    @Autowired
    private AddlxbgMapper addlxbgMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     *查询列表
     * @param scheduling
     * @return
     */
    @Override
    public List<SmBizScheduling> selectScheduling(SmBizScheduling scheduling) {
        scheduling.setDbType(dbType);
        return addlxbgMapper.selectScheduling(scheduling);
    }

    /**
     * 根据列表查询
     * @param id
     * @return
     */
    @Override
    public SmBizScheduling selectSchedulingById(String id) {

        return addlxbgMapper.selectSchedulingById(id);
    }

    /**
     * 根据单个id删除
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {

        return addlxbgMapper.deleteById(id);
    }

    /**
     * 添加
     * @param scheduling
     * @return
     */
    @Override
    public int insertLxbg(SmBizScheduling scheduling) {
        if(StringUtils.isNotEmpty(scheduling.getCreateTime())){
            scheduling.setCreateTime(handleTimeYYYYMMDDHHMMSS(scheduling.getCreateTime()));
        }
        if(StringUtils.isNotEmpty(scheduling.getStartTime())){
            scheduling.setStartTime(handleTimeYYYYMMDDHHMMSS(scheduling.getStartTime()));
        }
        return addlxbgMapper.insertLxbg(scheduling);
    }

    /**
     * 修改
     * @param scheduling
     * @return
     */
    @Override
    public int updatelxbg(SmBizScheduling scheduling) {
        if(StringUtils.isNotEmpty(scheduling.getCheckTime())){
            scheduling.setCheckTime(handleTimeYYYYMMDDHHMMSS(scheduling.getCheckTime()));
        }
        if(StringUtils.isNotEmpty(scheduling.getStartTime())){
            scheduling.setStartTime(handleTimeYYYYMMDDHHMMSS(scheduling.getStartTime()));
        }
        if(StringUtils.isNotEmpty(scheduling.getCreateTime())){
            scheduling.setCreateTime(handleTimeYYYYMMDDHHMMSS(scheduling.getCreateName()));
        }
        return addlxbgMapper.updatelxbg(scheduling);
    }

    /**
     * 查询关闭例行变更计划
     * @param scheduling
     * @return
     */
    @Override
    public List<SmBizScheduling> selectCloseLxbgId(SmBizScheduling scheduling) {
        return addlxbgMapper.selectCloseLxbgId(scheduling);
    }

    /**
     *例行变更进展情况页面
     * @param scheduling
     * @return
     */
    @Override
    public List<SmBizScheduling> selectEvoScheduling(SmBizScheduling scheduling) {
        scheduling.setDbType(dbType);
        return addlxbgMapper.selectEvoScheduling(scheduling);
    }

    @Override
    public List<SmBizScheduling> selectSchedulingList(SmBizScheduling scheduling) {
        scheduling.setDbType(dbType);
        return addlxbgMapper.selectSchedulingList(scheduling);
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
