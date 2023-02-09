package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.entity.FmDd;
import java.util.List;

public interface FmDispatchService {


    /**
     *
     * @param fmDd
     * @return
     */
    List<FmDd> selectDispatchList(FmDd fmDd);



    /**
     *
     * @param fmDd
     * @return
     */
    int insertDispatch(FmDd fmDd);


    FmDd selectFmddById(String id);

    /**
     * 修改
     * @param fmDd 调度信息
     * @return
     */
    int updateDispatch(FmDd fmDd);


    int deleteFmDdByIds(String ids);

    /**
     *
     * @param faultNo
     * @return
     */
    FmDd selectFmddByNo(String faultNo);

    List<FmDd> selectFmDdListByTask(FmDd fmDd);


    /**
     * 查询我的调度事件单
     * @param fmDd
     * @return
     */
    List<FmDd> selectQueryDispatchList(FmDd fmDd);

    /**
     * 校验联系方式
     * @param telPhone
     * @return
     */
    FmDd checkPhoneUnique(String telPhone);

    /**
     * 根据id更新有效无效标识
     * @param id
     * @return
     */
    int updateFmDdByInvalidationMark(String id);
}
