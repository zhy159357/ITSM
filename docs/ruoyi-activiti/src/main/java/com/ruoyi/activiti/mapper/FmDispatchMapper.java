package com.ruoyi.activiti.mapper;


import com.ruoyi.common.core.domain.entity.FmDd;
import com.ruoyi.common.core.domain.entity.PubPara;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface FmDispatchMapper {

    /**
     *
     * @param fmDd
     * @return
     */
    List<FmDd> selectDispatchList(FmDd fmDd);

    List<FmDd> selectDispatchListMysql(FmDd fmDd);

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    FmDd selectFmddById(String id);

    /**
     *新增
     * @param fmDd
     * @return
     */
    int insertDispatch(FmDd fmDd);

    /**
     *修改
     * @param fmDd
     * @return
     */
    int updateDispatch(FmDd fmDd);


    int deleteFmDdByIds(String[] ids);

    /**
     *
     * @param faultNo
     * @return
     */
    FmDd selectFmddByNo(String faultNo);

    List<FmDd> selectFmDdListByTask(FmDd fmDd);

    /**
     *
     * @param fmDd
     * @return
     */
    List<FmDd> selectQueryDispatchList(FmDd fmDd);

    List<FmDd> selectQueryDispatchListMysql(FmDd fmDd);

    FmDd checkPhoneUnique(String telPhone);

    /**
     *
     * @param fmddId
     * @return
     */
    int updateFmDdByInvalidationMark(String fmddId);
}
