package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.form.entity.TwUserDb;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkNodeVO;
import com.ruoyi.form.enums.TwWorkEnum;
import com.ruoyi.form.mapper.TwUserDbMapper;
import com.ruoyi.form.mapper.TwWorkNodeMapper;
import com.ruoyi.form.service.ITwUserDbService;
import com.ruoyi.form.service.ITwWorkNodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Service
public class TwWorkNodeServiceImpl extends ServiceImpl<TwWorkNodeMapper, TwWorkNode> implements ITwWorkNodeService {

    @Autowired
    private ITwUserDbService twUserDbService;
    @Resource
    private TwUserDbMapper twUserDbMapper;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Resource
    private TwWorkNodeMapper twWorkNodeMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(TwWorkNode twWorkNode) {
        this.save(twWorkNode);
        //添加子节点
        if (!CollectionUtils.isEmpty(twWorkNode.getChildren())) {
            twUserDbService.saveBatch(twWorkNode.getChildren());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TwWorkNode twWorkNode) {
        this.updateById(twWorkNode);
        //修改子节点
        //先删除再添加
//        QueryWrapper<TwUserDb> wrapper = new QueryWrapper<>();
//        wrapper.eq("word_node_id", twWorkNode.getId());
//        twUserDbMapper.delete(wrapper);
//        twUserDbService.saveBatch(twWorkNode.getChildren());
    }

    @Override
    public void saveOrSubmit(Integer type, String workNum) {
        //提交才做操作，绑定流程提交
        if (TwWorkEnum.SUBMIT.getCode().equals(type)) {
            //判断是否该工单流程已启动，如果已经启动无需重复提交
            HashMap<String, Object> map = new HashMap<>();
            map.put("one", "8b8080f457fffe39015800015ce60006");
            //启动流程
            activitiCommService.startProcess(workNum, "TEST0628", map);
        }


    }

    @Override
    public List<TwWorkNode> getWorkNodeByWorkOrderId(String id) {
        QueryWrapper<TwWorkNode> wrapper = new QueryWrapper<>();
        wrapper.eq("work_order_id", id);
        return twWorkNodeMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(String  id) {
        TwWorkNode twWorkNode = twWorkNodeMapper.selectById(id);
        twWorkNode.setId(null);
        //先添加node表
        twWorkNodeMapper.insert(twWorkNode);
        QueryWrapper<TwUserDb> wrapper = new QueryWrapper<>();
        wrapper.eq("word_node_id", id);
        List<TwUserDb> twUserDbs = twUserDbMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(twUserDbs)) {
            List<TwUserDb> list = twUserDbs.stream().peek(twUserDb -> {
                twUserDb.setId(null);
                twUserDb.setWordNodeId(twWorkNode.getId());
            }).collect(Collectors.toList());
            //再添加userdb
            twUserDbService.saveBatch(list);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        twWorkNodeMapper.deleteById(id);
        QueryWrapper<TwUserDb> wrapper = new QueryWrapper<>();
        wrapper.eq("word_node_id", id);
        twUserDbMapper.delete(wrapper);
    }



}
