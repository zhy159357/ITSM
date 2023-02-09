package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.CmBizSingleSJ;
import com.ruoyi.activiti.domain.CmBizSingleSJTools;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;
import com.ruoyi.activiti.mapper.CmBizSingleSJMapper;
import com.ruoyi.activiti.mapper.CmBizSingleSJToolsMapper;
import com.ruoyi.activiti.service.CmBizSingleSJService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmBizSingleSJServiceImpl implements CmBizSingleSJService {

    @Autowired
    private CmBizSingleSJMapper cmBizSingleSJMapper;

    @Autowired
    private CmBizSingleSJToolsMapper cmBizSingleSJToolsMapper;

    @Value("${pagehelper.helperDialect}")
    private String dataSourceType;

    @Override
    public List<CmBizSingleSJVo> list(CmBizSingleSJVo cmBizSingleSJVo) {
        if (StringUtils.isNotEmpty(cmBizSingleSJVo.getChangeSingleStatus())) {
            String[] split = cmBizSingleSJVo.getChangeSingleStatus().split(",");
            cmBizSingleSJVo.setStatusArray(split);
        }
        List<CmBizSingleSJVo> list;
        if("mysql".equals(dataSourceType)){
            list = cmBizSingleSJMapper.listMysql(cmBizSingleSJVo);
        }else{
            list = cmBizSingleSJMapper.list(cmBizSingleSJVo);
        }
        return list;
    }

    @Override
    public List<CmBizSingleSJVo> selectListByTask(CmBizSingleSJVo cmBizSingleSJVo) {
        if("mysql".equals(dataSourceType)){
            return cmBizSingleSJMapper.selectListByTaskMysql(cmBizSingleSJVo);
        }
        return cmBizSingleSJMapper.selectListByTask(cmBizSingleSJVo);
    }

    @Override
    public CmBizSingleSJVo selectSjbgById(String changeSingleId) {
        CmBizSingleSJVo css;
        if("mysql".equals(dataSourceType)){
            css = cmBizSingleSJMapper.selectSjbgByIdMysql(changeSingleId);
        }else{
            css = cmBizSingleSJMapper.selectSjbgById(changeSingleId);
        }
        return css;
    }

    @Override
    public CmBizSingleSJVo selectSjbgByChangeCode(String changeCode) {
        CmBizSingleSJVo css;
        if("mysql".equals(dataSourceType)){
            css = cmBizSingleSJMapper.selectSjbgByChangeCodeMysql(changeCode);
        }else{
            css = cmBizSingleSJMapper.selectSjbgByChangeCode(changeCode);
        }
        return css;
    }

    @Override
    public int insertSjbg(CmBizSingleSJ cmBizSingleSJ) {
        return cmBizSingleSJMapper.insertSjbg(cmBizSingleSJ);
    }

    @Override
    public int updateById(String id) {
        return cmBizSingleSJMapper.updateById(id);
    }

    @Override
    public int updateSjbg(CmBizSingleSJ cmBizSingleSJ) {
        return cmBizSingleSJMapper.updateSjbg(cmBizSingleSJ);
    }

    @Override
    public List<CmBizSingleSJVo> listByOrg(CmBizSingleSJVo cmBizSingleSJVo) {
        List<CmBizSingleSJVo> list;
        if("mysql".equals(dataSourceType)){
            list = cmBizSingleSJMapper.listByOrgMysql(cmBizSingleSJVo);
        }else{
            list = cmBizSingleSJMapper.listByOrg(cmBizSingleSJVo);
        }
        return list;
    }

    @Override
    public List<CmBizSingleSJ> selectListByFmNo(String fmNo) {
        return cmBizSingleSJMapper.selectListByFmNo(fmNo);
    }

    @Override
    public List<CmBizSingleSJVo> selectToolsCountBySys(CmBizSingleSJVo cmBizSingleSJVo) {
        return cmBizSingleSJMapper.selectToolsCountBySys(cmBizSingleSJVo);
    }

    @Override
    public List<CmBizSingleSJVo> selectToolsCountByCaption(CmBizSingleSJVo cmBizSingleSJVo) {
        return cmBizSingleSJMapper.selectToolsCountByCaption(cmBizSingleSJVo);
    }

    @Override
    public List<CmBizSingleSJTools> selectToolsCountBySys(CmBizSingleSJTools cmBizSingleSJTools) {
        if("mysql".equals(dataSourceType)){
            return cmBizSingleSJToolsMapper.selectToolsCountBySysMysql(cmBizSingleSJTools);
        }
        return cmBizSingleSJToolsMapper.selectToolsCountBySys(cmBizSingleSJTools);
    }

    @Override
    public List<CmBizSingleSJTools> selectToolsCountByCaption(CmBizSingleSJTools cmBizSingleSJTools) {
        if("mysql".equals(dataSourceType)){
            return cmBizSingleSJToolsMapper.selectToolsCountByCaptionMysql(cmBizSingleSJTools);
        }
        return cmBizSingleSJToolsMapper.selectToolsCountByCaption(cmBizSingleSJTools);
    }

    @Override
    public int updateEditSjbg(CmBizSingleSJ cmBizSingleSJ) {
        return cmBizSingleSJMapper.updateEditSjbg(cmBizSingleSJ);
    }

    @Override
    public List<CmBizSingleSJVo> fmAndCmList(CmBizSingleSJVo cmBizSingleSJVo) {
        List<CmBizSingleSJVo> list;
        if("mysql".equals(dataSourceType)){
            list = cmBizSingleSJMapper.fmAndCmListMysql(cmBizSingleSJVo);
        }else{
            list = cmBizSingleSJMapper.fmAndCmList(cmBizSingleSJVo);
        }
        return list;
    }

    @Override
    public List<CmBizSingleSJVo> sjbgTask(String pname) {
        return cmBizSingleSJMapper.sjbgTask(pname);
    }


}
