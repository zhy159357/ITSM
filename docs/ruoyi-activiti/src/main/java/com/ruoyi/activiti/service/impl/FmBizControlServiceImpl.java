package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.FmBizControlMapper;
import com.ruoyi.activiti.service.IFmBizControlService;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizControl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 业务事件单监控Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-11
 */
@Service
public class FmBizControlServiceImpl implements IFmBizControlService {
    @Autowired
    private FmBizControlMapper fmBizControlMapper;

    /**
     * 查询业务事件单监控
     *
     * @param controlId 业务事件单监控ID
     * @return 业务事件单监控
     */
    @Override
    public FmBizControl selectFmBizControlById(String controlId) {
        return fmBizControlMapper.selectFmBizControlById(controlId);
    }

    /**
     * 查询业务事件单监控列表
     *
     * @param fmBizControl 业务事件单监控
     * @return 业务事件单监控
     */
    @Override
    public List<FmBizControl> selectFmBizControlList(FmBizControl fmBizControl) {
        return fmBizControlMapper.selectFmBizControlList(fmBizControl);
    }

    /**
     * 新增业务事件单监控
     *
     * @param fmBizControl 业务事件单监控
     * @return 结果
     */
    @Override
    public int insertFmBizControl(FmBizControl fmBizControl) {
        return fmBizControlMapper.insertFmBizControl(fmBizControl);
    }

    /**
     * 修改业务事件单监控
     *
     * @param fmBizControl 业务事件单监控
     * @return 结果
     */
    @Override
    public int updateFmBizControl(FmBizControl fmBizControl) {
        return fmBizControlMapper.updateFmBizControl(fmBizControl);
    }

    /**
     * 删除业务事件单监控对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizControlByIds(String ids) {
        return fmBizControlMapper.deleteFmBizControlByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务事件单监控信息
     *
     * @param controlId 业务事件单监控ID
     * @return 结果
     */
    @Override
    public int deleteFmBizControlById(String controlId) {
        return fmBizControlMapper.deleteFmBizControlById(controlId);
    }

    @Override
    public void deleteAll() {
        fmBizControlMapper.deleteAll();
    }

    @Override
    public List<FmBiz> getNoEfficientFms(int count) {
        List<FmBiz> list = fmBizControlMapper.getNoEfficientFms(count);
        return list;
    }

    @Override
    public List<FmBiz> getUntimelyResolution() {
        List<FmBiz> list = fmBizControlMapper.getUntimelyResolution();
        return list;
    }

    @Override
    public List<FmBiz> getJjDealFmBizs() {
        List<FmBiz> list = fmBizControlMapper.getJjDealFmBizs();
        return list;
    }

    @Override
    public List<FmBiz> getTimeResolution(int day) {
        String time = DateUtils.dateTimeNow();
        String day5 = DateUtils.getday5(time, day);
        List<FmBiz> list = fmBizControlMapper.getTimeResolution(day5);
        return list;
    }
}
