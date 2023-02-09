package com.ruoyi.form.mapper;

import com.ruoyi.form.entity.TwWorkOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
public interface TwWorkOrderMapper extends BaseMapper<TwWorkOrder> {

    /**
     * 查询twWorkOrder
     *
     * @param twWorkOrder
     * @return
     */
    List<TwWorkOrder> getTwWorkOrder(TwWorkOrder twWorkOrder);

    /**
     * @param twWorkOrder
     * @param workNumList
     * @return
     */
    List<TwWorkOrder> getTwWorkOrderByWorkNumAndTwWorkOrder(@Param("twWorkOrder") TwWorkOrder twWorkOrder, @Param("workNumList") List<String> workNumList);

    /**
     *根据id修改状态
     * @param twWorkOrder 对象
     * @return
     */
    @Select({"UPDATE tw_work_order set `status`=#{twWorkOrder.status},implementor_orgid=#{twWorkOrder.implementorOrgid},implementor_name=\n" +
            "#{twWorkOrder.implementorName},deal_person_id=#{twWorkOrder.dealPersonId},deal_person_name=#{twWorkOrder.dealPersonName}  WHERE id=#{twWorkOrder.id}"})
    Integer updateTwWorkOrderById(@Param("twWorkOrder") TwWorkOrder twWorkOrder);

    List<TwWorkOrder> getTwWorkOrderList(TwWorkOrder twWorkOrder);

    TwWorkOrder selectTwWorkOrderById(String id);

    int toCancle(String id);
}
