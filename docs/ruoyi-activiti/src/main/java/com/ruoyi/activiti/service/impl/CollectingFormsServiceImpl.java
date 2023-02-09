package com.ruoyi.activiti.service.impl;


import com.ruoyi.activiti.mapper.CollectingFormsMapper;
import com.ruoyi.activiti.service.CollectingFormsService;
import com.ruoyi.common.core.domain.entity.CollectingFormsInspection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

    /**
     * @ProjectName: ruoyi
     * @Package: com.ruoyi.activiti.service.impl
     * @Description: 作用类描述
     * @Author:
     * @CreatDate: 2022/3/8 16:20
     * Copyright: Copyright (c) 2022
     */
    @Service
    public class CollectingFormsServiceImpl implements CollectingFormsService {

        @Autowired
        private CollectingFormsMapper collectingFormsMapper;

        /**
         * 日常巡检记录单列表
         * @param collectingFormsInspection
         * @return
         */
        @Override
        public List<CollectingFormsInspection> selectCollectingList(CollectingFormsInspection collectingFormsInspection) {
            return collectingFormsMapper.selectCollectingList(collectingFormsInspection);
        }

        /**
         * 根据id删除日常巡检记录
         * @param id
         * @return
         */
        @Override
        public int deleteCollectingById(String id) {
            return collectingFormsMapper.deleteCollectingById(id);
        }

        /**
         * 新增记录
         * @param collectingFormsInspection
         * @return
         */
       @Override
        public int insertCollecting(CollectingFormsInspection collectingFormsInspection) {
            return collectingFormsMapper.insertCollecting(collectingFormsInspection);
        }

        /**
         * 修改记录
         * @param collectingFormsInspection
         * @return
         */
        @Override
        public int updateCollecting(CollectingFormsInspection collectingFormsInspection) {
            return collectingFormsMapper.updateCollecting(collectingFormsInspection);
        }

        /**
         * 根据id查询巡检记录
         * @param id
         * @return
         */
        @Override
        public CollectingFormsInspection selectCollectingById(String id){
            return collectingFormsMapper.selectCollectingById(id);
        }
    }


