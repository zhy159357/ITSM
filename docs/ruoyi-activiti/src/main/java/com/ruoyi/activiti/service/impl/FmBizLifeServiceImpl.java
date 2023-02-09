package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.FmBizLifeMapper;
import com.ruoyi.activiti.service.IFmBizLifeService;
import com.ruoyi.common.core.domain.entity.FmBizLife;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件单效率Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-21
 */
@Service
public class FmBizLifeServiceImpl implements IFmBizLifeService {
    @Autowired
    private FmBizLifeMapper fmBizLifeMapper;

    /**
     * 查询事件单效率
     *
     * @param fmLifeId 事件单效率ID
     * @return 事件单效率
     */
    @Override
    public FmBizLife selectFmBizLifeById(String fmLifeId) {
        return fmBizLifeMapper.selectFmBizLifeById(fmLifeId);
    }

    /**
     * 查询事件单效率列表
     *
     * @param fmBizLife 事件单效率
     * @return 事件单效率
     */
    @Override
    public List<FmBizLife> selectFmBizLifeList(FmBizLife fmBizLife) {
        return fmBizLifeMapper.selectFmBizLifeList(fmBizLife);
    }

    /**
     * 新增事件单效率
     *
     * @param fmBizLife 事件单效率
     * @return 结果
     */
    @Override
    public int insertFmBizLife(FmBizLife fmBizLife) {
        return fmBizLifeMapper.insertFmBizLife(fmBizLife);
    }

    /**
     * 修改事件单效率
     *
     * @param fmBizLife 事件单效率
     * @return 结果
     */
    @Override
    public int updateFmBizLife(FmBizLife fmBizLife) {
        return fmBizLifeMapper.updateFmBizLife(fmBizLife);
    }

    /**
     * 删除事件单效率对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizLifeByIds(String ids) {
        return fmBizLifeMapper.deleteFmBizLifeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除事件单效率信息
     *
     * @param fmLifeId 事件单效率ID
     * @return 结果
     */
    @Override
    public int deleteFmBizLifeById(String fmLifeId) {
        return fmBizLifeMapper.deleteFmBizLifeById(fmLifeId);
    }

    @Override
    public List<FmBizLife> getSysCount() {
        return fmBizLifeMapper.getSysCount();
    }

    @Override
    public List<FmBizLife> getSysDealCount(String eStartTime, String sStartTime, String tStartTime, String endTime) {
        FmBizLife fbl = new FmBizLife();
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("eStartTime", eStartTime);
        reMap.put("sStartTime", sStartTime);
        reMap.put("tStartTime", tStartTime);
        reMap.put("endTime", endTime);
        fbl.setParams(reMap);
        return fmBizLifeMapper.getSysDealCount(fbl);
    }
    @Override
    public void deleteAll(){
        fmBizLifeMapper.deleteAll();
    }
}
