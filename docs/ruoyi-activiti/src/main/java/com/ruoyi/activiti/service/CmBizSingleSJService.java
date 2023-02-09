package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.CmBizSingleSJ;
import com.ruoyi.activiti.domain.CmBizSingleSJTools;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;

import java.util.List;

public interface CmBizSingleSJService {

    List<CmBizSingleSJVo> list(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> selectListByTask(CmBizSingleSJVo cmBizSingleSJVo);

    CmBizSingleSJVo selectSjbgById(String changeSingleId);


    CmBizSingleSJVo selectSjbgByChangeCode(String changeCode);

    int insertSjbg(CmBizSingleSJ cmBizSingleSJ);

    int updateById(String id);

    int updateSjbg(CmBizSingleSJ cmBizSingleSJ);

    List<CmBizSingleSJVo> listByOrg(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJ> selectListByFmNo(String fmNo);

    List<CmBizSingleSJVo> selectToolsCountBySys(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> selectToolsCountByCaption(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJTools> selectToolsCountBySys(CmBizSingleSJTools cmBizSingleSJTools);

    List<CmBizSingleSJTools> selectToolsCountByCaption(CmBizSingleSJTools cmBizSingleSJTools);

    int updateEditSjbg(CmBizSingleSJ cmBizSingleSJ);

    List<CmBizSingleSJVo> fmAndCmList(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> sjbgTask(String pname);


}
