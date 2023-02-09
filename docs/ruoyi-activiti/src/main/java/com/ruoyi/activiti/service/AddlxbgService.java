package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.SmBizFolder;
import com.ruoyi.common.core.domain.entity.SmBizScheduling;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AddlxbgService {



    /**
     *查询列表
     * @param scheduling
     * @return
     */
    List<SmBizScheduling> selectScheduling(SmBizScheduling scheduling);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    SmBizScheduling selectSchedulingById(String id);

    /**
     * 根据单个id删除
     * @param id
     * @return
     */
    int deleteById(String id);

    /**
     * 添加
     * @param scheduling
     * @return
     */
    int insertLxbg(SmBizScheduling scheduling);

    /**
     * 修改
     * @param scheduling
     * @return
     */
    int updatelxbg(SmBizScheduling scheduling);

    /**
     * 查询关闭例行变更计划
     * @param scheduling
     * @return
     */
    List<SmBizScheduling> selectCloseLxbgId(SmBizScheduling scheduling);

    /**
     *例行变更进展情况页面
     * @param scheduling
     * @return
     */
    List<SmBizScheduling> selectEvoScheduling(SmBizScheduling scheduling);

    /**
     * 查询待处长审核的例行变更计划
     * @param scheduling
     * @return
     */
    List<SmBizScheduling> selectSchedulingList(SmBizScheduling scheduling);

}
