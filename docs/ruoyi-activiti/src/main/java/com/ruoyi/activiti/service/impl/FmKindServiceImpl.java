package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.FmKindMapper;
import com.ruoyi.activiti.service.IFmKindService;
import com.ruoyi.common.core.domain.entity.FmKind;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 事件单系统分类Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-28
 */
@Service("fmkind")
public class FmKindServiceImpl implements IFmKindService {
    @Autowired
    private FmKindMapper fmKindMapper;

    /**
     * 查询事件单系统分类
     *
     * @param fmTypeId 事件单系统分类ID
     * @return 事件单系统分类
     */
    @Override
    public FmKind selectFmKindById(String fmTypeId) {
        return fmKindMapper.selectFmKindById(fmTypeId);
    }

    /**
     * 查询事件单系统分类列表
     *
     * @param fmKind 事件单系统分类
     * @return 事件单系统分类
     */
    @Override
    public List<FmKind> selectFmKindList(FmKind fmKind) {
        return fmKindMapper.selectFmKindList(fmKind);
    }

    /**
     * 新增事件单系统分类
     *
     * @param fmKind 事件单系统分类
     * @return 结果
     */
    @Override
    public int insertFmKind(FmKind fmKind) {
        return fmKindMapper.insertFmKind(fmKind);
    }

    /**
     * 修改事件单系统分类
     *
     * @param fmKind 事件单系统分类
     * @return 结果
     */
    @Override
    public int updateFmKind(FmKind fmKind) {
        fmKind.setUpdateTime(DateUtils.dateTime());
        return fmKindMapper.updateFmKind(fmKind);
    }

    /**
     * 删除事件单系统分类对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmKindByIds(String ids) {
        return fmKindMapper.deleteFmKindByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除事件单系统分类信息
     *
     * @param fmTypeId 事件单系统分类ID
     * @return 结果
     */
    @Override
    public int deleteFmKindById(String fmTypeId) {
        return fmKindMapper.deleteFmKindById(fmTypeId);
    }

    /**
     * 根据系统ID查询对应事件分类
     */
    @Override
    public List<FmKind> selectFmKindBySysid(String sysid) {
        List<FmKind> list = fmKindMapper.selectFmKindBySysid(sysid);
        return list;
    }
}
