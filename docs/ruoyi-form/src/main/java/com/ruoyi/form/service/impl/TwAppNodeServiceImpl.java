package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.form.entity.TwAppNode;
import com.ruoyi.form.mapper.TwAppNodeMapper;
import com.ruoyi.form.service.ITwAppNodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chw
 * @since 2022-08-08
 */
@Service
public class TwAppNodeServiceImpl extends ServiceImpl<TwAppNodeMapper, TwAppNode> implements ITwAppNodeService {

    @Resource
    private TwAppNodeMapper twAppNodeMapper;

    @Override
    public List<TwAppNode> getTwAppNode(TwAppNode twAppNode) {
        QueryWrapper<TwAppNode> wrapper = new QueryWrapper<>();
        wrapper.eq("work_num", twAppNode.getWorkNum()).eq("type", twAppNode.getType());
        return twAppNodeMapper.selectList(wrapper);
    }
}
