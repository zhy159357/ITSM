package com.ruoyi.activiti.service.impl;


import com.ruoyi.common.core.domain.entity.FmDd;
import com.ruoyi.activiti.mapper.FmDispatchMapper;

import com.ruoyi.activiti.service.FmDispatchService;
import com.ruoyi.common.core.text.Convert;
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
public class FmDispatchServiceImpl implements FmDispatchService{

    @Autowired
    private FmDispatchMapper dispatchMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     *
     * @param fmDd
     * @return
     */
    @Override
    public List<FmDd> selectDispatchList(FmDd fmDd) {
        if("mysql".equals(dbType)){
            return dispatchMapper.selectDispatchListMysql(fmDd);
        }else{
            return dispatchMapper.selectDispatchList(fmDd);
        }
    }


    /**
     * 提交
     * @param fmDd
     * @return
     */
    @Override
    public int insertDispatch(FmDd fmDd) {

        if(StringUtils.isNotEmpty(fmDd.getCreateTime())){
            fmDd.setCreateTime(handleTimeYYYYMMDDHHMMSS(fmDd.getCreateTime()));
        }
        if(StringUtils.isNotEmpty(fmDd.getPlanTime())){
            fmDd.setPlanTime(handleTimeYYYYMMDDHHMMSS(fmDd.getPlanTime()));
        }
        return dispatchMapper.insertDispatch(fmDd);
    }


    /**
     *根据id查询
     * @param id id
     * @return
     */
    @Override
    public FmDd selectFmddById(String id) {

        return dispatchMapper.selectFmddById(id);
    }

    /**
     *
     * @param fmDd 调度信息
     * @return
     */
    @Override
    public int updateDispatch(FmDd fmDd) {

        if(StringUtils.isNotEmpty(fmDd.getCreateTime())){
            fmDd.setCreateTime(handleTimeYYYYMMDDHHMMSS(fmDd.getCreateTime()));
        }
        if(StringUtils.isNotEmpty(fmDd.getPlanTime())){
            fmDd.setPlanTime(handleTimeYYYYMMDDHHMMSS(fmDd.getPlanTime()));
        }

        return dispatchMapper.updateDispatch(fmDd);
    }


    @Override
    public int deleteFmDdByIds(String ids) {

        return dispatchMapper.deleteFmDdByIds(Convert.toStrArray(ids));

    }

    /**
     * 根据订单号查询
     * @param faultNo
     * @return
     */
    @Override
    public FmDd selectFmddByNo(String faultNo) {

        return dispatchMapper.selectFmddByNo(faultNo);
    }

    @Override
    public List<FmDd> selectFmDdListByTask(FmDd fmDd) {
        return dispatchMapper.selectFmDdListByTask(fmDd);
    }

    @Override
    public List<FmDd> selectQueryDispatchList(FmDd fmDd)
    {
        if("mysql".equals(dbType)){
            return dispatchMapper.selectQueryDispatchListMysql(fmDd);
        }else{
            return dispatchMapper.selectQueryDispatchList(fmDd);
        }
    }

    @Override
    public FmDd checkPhoneUnique(String telPhone) {
        return dispatchMapper.checkPhoneUnique(telPhone);
    }

    @Override
    public int updateFmDdByInvalidationMark(String id) {
        return dispatchMapper.updateFmDdByInvalidationMark(id);
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
