package com.ruoyi.activiti.mapper;
import com.ruoyi.activiti.domain.CmBizSingleSJ;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;

import java.util.List;

public interface CmBizSingleSJMapper {

    List<CmBizSingleSJVo> list(CmBizSingleSJVo cmBizSingleSJVo);
    List<CmBizSingleSJVo> listMysql(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> selectListByTask(CmBizSingleSJVo cmBizSingleSJVo);
    List<CmBizSingleSJVo> selectListByTaskMysql(CmBizSingleSJVo cmBizSingleSJVo);

    CmBizSingleSJVo selectSjbgById(String changeSingleId);
    CmBizSingleSJVo selectSjbgByIdMysql(String changeSingleId);

    int insertSjbg(CmBizSingleSJ cmBizSingleSJ);

    int updateById(String id);

    int updateSjbg(CmBizSingleSJ cmBizSingleSJ);

    int updateEditSjbg(CmBizSingleSJ cmBizSingleSJ);

    /**
     * 根据事件单号查询变更单信息
     * @param fmNo
     * @return
     */
    public List<CmBizSingleSJ> selectCmAndFmBiz(String fmNo);
    public List<CmBizSingleSJ> selectCmAndFmBizMysql(String fmNo);

    List<CmBizSingleSJVo> listByOrg(CmBizSingleSJVo cmBizSingleSJVo);
    List<CmBizSingleSJVo> listByOrgMysql(CmBizSingleSJVo cmBizSingleSJVo);

    CmBizSingleSJVo selectSjbgByChangeCode(String changeCode);
    CmBizSingleSJVo selectSjbgByChangeCodeMysql(String changeCode);

    List<CmBizSingleSJ> selectListByFmNo(String fmNo);

    List<CmBizSingleSJ> selectListByFmNo2(String fmNo);
    List<CmBizSingleSJ> selectListByFmNo2Mysql(String fmNo);

    List<CmBizSingleSJVo> selectToolsCountBySys(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> selectToolsCountByCaption(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> fmAndCmList(CmBizSingleSJVo cmBizSingleSJVo);
    List<CmBizSingleSJVo> fmAndCmListMysql(CmBizSingleSJVo cmBizSingleSJVo);

    List<CmBizSingleSJVo> sjbgTask(String pname);


}
