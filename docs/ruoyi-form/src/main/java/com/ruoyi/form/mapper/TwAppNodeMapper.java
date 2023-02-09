package com.ruoyi.form.mapper;

import com.ruoyi.form.entity.TwAppNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chw
 * @since 2022-08-08
 */
public interface TwAppNodeMapper extends BaseMapper<TwAppNode> {


    List<TwAppNode> getTwAppNode(TwAppNode twAppNode);

}
