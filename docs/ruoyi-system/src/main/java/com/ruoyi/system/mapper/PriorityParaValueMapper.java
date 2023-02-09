package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.PriorityParavalue;
import com.ruoyi.common.core.domain.entity.SysCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *  @author jiangfeng
 *  優先級別 PriorityParavalue
 *  @date 2022-06-22
 */

public interface PriorityParaValueMapper {


    List<PriorityParavalue> findByCode(@Param("code") String code);

    void insertSave(PriorityParavalue priorityParavalue);

    @Select("select id,pub_paravalue_id_degree,pub_paravalue_id_scope,priority,code from pub_paravalue_priority where id=#{id} ")
    PriorityParavalue selectOneById(String id);

    @Update("update pub_paravalue_priority set priority=#{priority} where id= #{id} ")
    void updatePriorityParavalue(PriorityParavalue priorityParavalue);

    @Delete("delete from pub_paravalue_priority where pub_paravalue_id_scope = #{pubParavalueIdScope} and pub_paravalue_id_degree=#{pubParavalueIdDegree}" )
    void deletePriorityParavalue(PriorityParavalue priorityParavalue);

    @Select("select priority from pub_paravalue_priority where pub_paravalue_id_scope = #{pubParavalueIdScope} and pub_paravalue_id_degree=#{pubParavalueIdDegree}" )
    String selectPriorityParavalue(PriorityParavalue priorityParavalue);
}
