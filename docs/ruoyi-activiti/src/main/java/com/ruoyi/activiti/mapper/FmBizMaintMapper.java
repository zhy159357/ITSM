package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmBizMaint;

import java.util.List;

public interface FmBizMaintMapper {
    //查询列表
    List<FmBizMaint> checkFmBizMaint(FmBizMaint fmBizMaint);
    //新增
    int insertFmBizMaint(FmBizMaint fmBizMaint);
    //根据id查询
    FmBizMaint getByMId(String mId);
    //修改
    int updateById(FmBizMaint fmBizMaint);
    //删除
    int deleteByIds(String[] list);
    //获取事件标题
    List<FmBizMaint> selectList(int type);
    //查询标题是否存在
    int getByCount(String mTitle);
}
