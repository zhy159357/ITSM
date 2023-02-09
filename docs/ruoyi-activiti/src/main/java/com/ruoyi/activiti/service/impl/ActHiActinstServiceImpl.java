package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.ActHiActinst;
import com.ruoyi.activiti.mapper.ActHiActinstMapper;
import com.ruoyi.activiti.service.IActHiActInstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by:hecaili
 * Date:2022/8/3 17:19
 */

@Service
public class ActHiActinstServiceImpl implements IActHiActInstService {

    @Autowired
    private ActHiActinstMapper actHiActinstMapper;

    /**
     * 根据流程实例id查询历史流程列表
     *
     * @param instanceIds
     * @return
     */
    @Override
    public List<ActHiActinst> listActHiActinst(List<String> instanceIds) {
        return actHiActinstMapper.listActHiActinst(instanceIds);
    }
}
