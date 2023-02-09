package com.ruoyi.activiti.mapper;
import com.ruoyi.activiti.domain.CmBizSingleSJ;
import com.ruoyi.activiti.domain.CmBizSingleSJTools;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;

import java.util.List;

public interface CmBizSingleSJToolsMapper {


    List<CmBizSingleSJTools> selectToolsCountBySys(CmBizSingleSJTools cmBizSingleSJTools);
    List<CmBizSingleSJTools> selectToolsCountBySysMysql(CmBizSingleSJTools cmBizSingleSJTools);

    List<CmBizSingleSJTools> selectToolsCountByCaption(CmBizSingleSJTools cmBizSingleSJTools);
    List<CmBizSingleSJTools> selectToolsCountByCaptionMysql(CmBizSingleSJTools cmBizSingleSJTools);
}
