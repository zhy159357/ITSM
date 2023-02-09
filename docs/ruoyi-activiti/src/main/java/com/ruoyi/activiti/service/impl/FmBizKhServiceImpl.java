package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.FmBizKh;
import com.ruoyi.activiti.mapper.FmBizKhMapper;
import com.ruoyi.activiti.service.IFmBizKhService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * 2021业务事件考核Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-15
 */
@Service
public class FmBizKhServiceImpl implements IFmBizKhService {
    @Autowired
    private FmBizKhMapper fmBizKhMapper;

    /**
     * 查询2021业务事件考核
     *
     * @param kid 2021业务事件考核ID
     * @return 2021业务事件考核
     */
    @Override
    public FmBizKh selectFmBizKhById(String kid) {
        return fmBizKhMapper.selectFmBizKhById(kid);
    }

    /**
     * 查询2021业务事件考核列表
     *
     * @param fmBizKh 2021业务事件考核
     * @return 2021业务事件考核
     */
    @Override
    public List<FmBizKh> selectFmBizKhList(FmBizKh fmBizKh) {
        return fmBizKhMapper.selectFmBizKhList(fmBizKh);
    }

    /**
     * 新增2021业务事件考核
     *
     * @param fmBizKh 2021业务事件考核
     * @return 结果
     */
    @Override
    public int insertFmBizKh(FmBizKh fmBizKh) {
        return fmBizKhMapper.insertFmBizKh(fmBizKh);
    }

    /**
     * 修改2021业务事件考核
     *
     * @param fmBizKh 2021业务事件考核
     * @return 结果
     */
    @Override
    public int updateFmBizKh(FmBizKh fmBizKh) {
        return fmBizKhMapper.updateFmBizKh(fmBizKh);
    }

    /**
     * 删除2021业务事件考核对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizKhByIds(String ids) {
        return fmBizKhMapper.deleteFmBizKhByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除2021业务事件考核信息
     *
     * @param kid 2021业务事件考核ID
     * @return 结果
     */
    @Override
    public int deleteFmBizKhById(String kid) {
        return fmBizKhMapper.deleteFmBizKhById(kid);
    }

    @Override
    public String getDateTime(String dateTime) {
        if (StringUtils.isNotEmpty(dateTime)) {
            dateTime = DateUtils.handleTimeYYYYMM(dateTime);
            return dateTime;
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            return "" + year + (month < 10 ? "0" + month : month);
        }
    }
    public FmBizKh getFmBizSatisfaction(FmBizKh fmBizKh){
        return fmBizKhMapper.getFmBizSatisfaction(fmBizKh);
    }
}
