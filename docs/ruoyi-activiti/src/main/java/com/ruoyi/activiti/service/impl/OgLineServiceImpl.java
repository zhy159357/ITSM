package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.mapper.OgLineMapper;
import com.ruoyi.activiti.service.LineService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.OgEmerg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OgLineServiceImpl implements LineService {

    @Autowired
    private OgLineMapper lineMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public List<OgLine> selectLineList(OgLine ogLine) {
        if("mysql".equals(dbType)){
            return lineMapper.selectLineListMysql(ogLine);
        }else{
            return lineMapper.selectLineList(ogLine);
        }
    }

    @Override
    public int insertOgLine(OgLine ogLine) {
        return lineMapper.insertOgLine(ogLine);
    }

    @Override
    public OgLine selectByLindId(String lineId) {
        if("mysql".equals(dbType)){
            return lineMapper.selectByLindIdMysql(lineId);
        }else{
            return lineMapper.selectByLindId(lineId);
        }
    }

    @Override
    public int updateOgLine(OgLine ogLine) {
        return lineMapper.updateOgLine(ogLine);
    }

    @Override
    public int deleteByLindId(String ids){
        String[]  list= Convert.toStrArray(",",ids);
        return lineMapper.deleteByLindId(list);
    }
}
