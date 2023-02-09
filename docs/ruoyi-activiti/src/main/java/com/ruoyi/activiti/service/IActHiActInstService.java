package com.ruoyi.activiti.service;


import com.ruoyi.activiti.domain.ActHiActinst;

import java.util.List;

/**
 * create by:hecaili
 * Date:2022/8/3 17:19
 */
public interface IActHiActInstService {

    /**
     * 根据流程实例id查询历史流程列表
     *
     * @param instanceIds
     * @return
     */
    List<ActHiActinst> listActHiActinst(List<String> instanceIds);
}
