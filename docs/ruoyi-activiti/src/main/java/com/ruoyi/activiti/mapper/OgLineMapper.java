package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.OgLine;

import java.util.List;

public interface OgLineMapper {

    List<OgLine> selectLineList(OgLine ogLine);

    List<OgLine> selectLineListMysql(OgLine ogLine);

    int insertOgLine(OgLine ogLine);

    OgLine selectByLindId(String lineId);

    OgLine selectByLindIdMysql(String lineId);

    int updateOgLine(OgLine ogLine);

    int deleteByLindId(String[] list);
}
