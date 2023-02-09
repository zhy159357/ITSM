package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.FmBizMaintMapper;
import com.ruoyi.activiti.service.FmBizMaintService;
import com.ruoyi.common.core.domain.entity.FmBizMaint;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FmBizMaintServiceImpl implements FmBizMaintService {
    @Autowired
    private FmBizMaintMapper fmBizMaintMapper;
    @Override
    public List<FmBizMaint> checkFmBizMaint(FmBizMaint fmBizMaint) {
        return fmBizMaintMapper.checkFmBizMaint(fmBizMaint);
    }

    @Override
    public int insertFmBizMaint(FmBizMaint fmBizMaint) {
        return fmBizMaintMapper.insertFmBizMaint(fmBizMaint);
    }

    @Override
    public FmBizMaint getByMId(String mId) {
        return fmBizMaintMapper.getByMId(mId);
    }

    @Override
    public int updateById(FmBizMaint fmBizMaint) {
        return fmBizMaintMapper.updateById(fmBizMaint);
    }

    @Override
    public int deleteByIds(String ids) {
        String[] list= Convert.toStrArray(",",ids);
        return fmBizMaintMapper.deleteByIds(list);
    }

    @Override
    public List<FmBizMaint> selectList(int type) {
        return fmBizMaintMapper.selectList(type);
    }

    @Override
    public int getByCount(String mTitle) {
        return fmBizMaintMapper.getByCount(mTitle);
    }
}
