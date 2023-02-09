package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.form.entity.TwWorkNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.entity.TwWorkNodeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
public interface TwWorkNodeMapper extends BaseMapper<TwWorkNode> {


    @Select({"SELECT  node_type as name, sum(number) as num FROM tw_work_node WHERE (work_order_id =#{id}) GROUP BY node_type"})
    List<TwWorkNodeVO>  getTwWorkNodeCountByTwWorkOrderId(@Param("id") Integer id);

}
