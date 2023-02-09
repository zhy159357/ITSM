package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.entity.TwTaskEntity;
import com.ruoyi.form.entity.TwUserDb;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkOrder;
import com.ruoyi.form.mapper.TwTaskMapper;
import com.ruoyi.form.service.ITwTaskDbService;
import com.ruoyi.form.service.ITwUserDbService;
import com.ruoyi.form.service.ITwWorkNodeService;
import com.ruoyi.form.service.ITwWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ruoyi
 * @description:
 * @author: ma zy
 * @date: 2022-11-22 10:30
 **/
@Service
public class TwTaskServiceImpl extends ServiceImpl<TwTaskMapper, TwTaskEntity> implements ITwTaskDbService {

    @Autowired
    private TwTaskMapper twTaskMapper;
    @Autowired ITwTaskDbService iTwTaskDbService;

    @Autowired
    private ITwWorkNodeService iTwWorkNodeService;

    @Autowired
    private ITwWorkOrderService iTwWorkOrderService;

    @Override
    public List<TwTaskEntity> getList(String workOrderId) {
        QueryWrapper<TwTaskEntity> queryWrapper = new QueryWrapper<TwTaskEntity>();
        queryWrapper.eq("work_order_id",workOrderId);
        List<TwTaskEntity> taskList = iTwTaskDbService.list();
        List<TwTaskEntity> taskLists = taskList.stream().filter(p->p.getWorkOrderId().equals(workOrderId)).collect(Collectors.toList());
        taskLists.stream().forEach(j->{
            if(StringUtils.isNotEmpty(j.getWorkOrderId())){
                TwWorkNode twWorkNodeVo = iTwWorkNodeService.getById(j.getWorkNodeId());
                if(StringUtils.isNotEmpty(twWorkNodeVo.getClassify()) && twWorkNodeVo.getClassify().equals("1")){
                    j.setWorkNodeId("应用服务类");
                }else{
                    j.setWorkNodeId("数据类");
                }
            }
        });
        return taskLists;
    }
}
