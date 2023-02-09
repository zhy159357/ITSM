package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBizMaint;

import java.util.List;

public interface FmBizMaintService {
    //查询事件维护列表
    List<FmBizMaint> checkFmBizMaint(FmBizMaint fmBizMaint);

    //事件维护新增
    public int insertFmBizMaint(FmBizMaint fmBizMaint);

    //根据id查询
    public FmBizMaint getByMId(String mId);

    //修改
    public int updateById(FmBizMaint fmBizMaint);

    //删除
    public int deleteByIds(String ids);

    //获取事件标题
    public List<FmBizMaint> selectList(int type);

    //查询标题
    public int getByCount(String mTitle);
}
