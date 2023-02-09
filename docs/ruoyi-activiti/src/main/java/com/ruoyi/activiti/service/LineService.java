package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.OgLine;

import java.util.List;

public interface LineService {

    /**
     * 查询事件
     *
     * @param ogLine 事件信息
     * @return 信息集合
     */
    List<OgLine> selectLineList(OgLine ogLine);

    int insertOgLine(OgLine ogLine);

    OgLine selectByLindId(String lineId);

    int updateOgLine(OgLine ogLine);

    int deleteByLindId(String ids);
}
