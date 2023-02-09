package com.ruoyi.form.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.entity.DbUserAndTableMessageVO;
import com.ruoyi.form.entity.TwUserDb;
import com.ruoyi.form.mapper.TwUserDbMapper;
import com.ruoyi.form.service.ITwUserDbService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Service
public class TwUserDbServiceImpl extends ServiceImpl<TwUserDbMapper, TwUserDb> implements ITwUserDbService {

    @Resource
    private TwUserDbMapper twUserDbMapper;

    @Override
    public List<TwUserDb> getUserByWorkNodeId(String id) {
        QueryWrapper<TwUserDb> wrapper = new QueryWrapper<>();
        wrapper.eq("word_node_id", id);
        wrapper.isNotNull("username");
        return twUserDbMapper.selectList(wrapper);
    }

    @Override
    public List<TwUserDb> getUserDbByWorkNodeId(String id) {
        QueryWrapper<TwUserDb> wrapper = new QueryWrapper<>();
        wrapper.eq("word_node_id", id);
        wrapper.isNotNull("table_space_name");
        return twUserDbMapper.selectList(wrapper);
    }

    @Override
    public List<TwUserDb> getUserSqlDbByWorkNodeId(String id,String classify,String dbType) {
        QueryWrapper<TwUserDb> wrapper = new QueryWrapper<>();
        wrapper.eq("word_node_id", id);
        if(classify.equals("2") && dbType.equals("1")){
            wrapper.eq("db_type","1");
        }else{
            wrapper.eq("db_type","2");
        }
        return twUserDbMapper.selectList(wrapper);
    }


    @Override
    public List<DbUserAndTableMessageVO> getDbUserAndTableMessage(String id) {
        TwUserDb twUserDb = twUserDbMapper.selectById(id);
        if (twUserDb != null && StringUtils.isNotEmpty(twUserDb.getUserDb())) {
            return JSON.parseArray(twUserDb.getUserDb(), DbUserAndTableMessageVO.class);
        }
        return null;
    }

    @Override
    public void addUserDbForDetail(DbUserAndTableMessageVO dbUserAndTableMessageVO) {
        dbUserAndTableMessageVO.setOnlyKey(UUID.getUUIDStr());
        TwUserDb twUserDb = twUserDbMapper.selectById(dbUserAndTableMessageVO.getId());
        //json转对象
        List<DbUserAndTableMessageVO> list = JSON.parseArray(twUserDb.getUserDb(), DbUserAndTableMessageVO.class);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<DbUserAndTableMessageVO>();
        }
        list.add(dbUserAndTableMessageVO);
        String json = JSON.toJSONString(list);
        //修改json值
        twUserDbMapper.updateTwUserDbById(dbUserAndTableMessageVO.getId(), json);
    }

    @Override
    public void deleteUserDbDetail(String id, String onlyKey) {
        TwUserDb twUserDb = twUserDbMapper.selectById(id);
        List<DbUserAndTableMessageVO> list = JSON.parseArray(twUserDb.getUserDb(), DbUserAndTableMessageVO.class);
        List<DbUserAndTableMessageVO> newList = list.stream().filter(dbUserAndTableMessageVO -> !dbUserAndTableMessageVO.getOnlyKey().equals(onlyKey)).collect(Collectors.toList());
        String json = JSON.toJSONString(newList);
        //修改json值
        twUserDbMapper.updateTwUserDbById(id, json);
    }

    @Override
    public TwUserDb selectone(TwUserDb twWorkOrder) {
        return twUserDbMapper.selectById(twWorkOrder.getId());
    }
}
