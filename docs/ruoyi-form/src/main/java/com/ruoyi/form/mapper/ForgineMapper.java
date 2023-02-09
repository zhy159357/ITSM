package com.ruoyi.form.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.CustomerFormContent;


public interface ForgineMapper extends BaseMapper<CustomerFormContent> {

    @Select("SELECT a.extra1 as itsmNo,b.changeTaskNo as itsmChildNo ,b.referApp as sysId ,b.deployWay,b.preTaskFlag as hasPretask,b.mergeFlag as hasUnion, as hasDepend,b.planStartDate  as planStartDt，b.implStartDate as actStartDt,b.implEndDate as actEndDt,b.implResultCheck as execResult,b.taskStatus  as  itsmChildStatus,b.created_time  as createDt,b.updated_time   as lastUpStringDt  from design_biz_change a ,design_biz_changetask b where a.extra1=b.changeNo and a.extra1=#{changeNo}")
    List<Map<String, Object>> selectChangeTaskList(@Param("changeNo") String changeNo);
    
    @Select("")
    List<Map<String, Object>> selectChangeStateList(String changeNo);
    
    @Select("SELECT DISTINCT extra1 as quesNo,problem_title as quesTitle,created_time as createTime FROM design_biz_problem where created_time between #{startTime} and #{endTime}")
    List<Map<String, Object>> selectProblemList(@Param("startTime") String startTime,@Param("endTime")String endTime);
    
    @Select("SELECT DISTINCT extra1 as eventNo,event_title as eventTitle,created_time as createTime FROM design_biz_incident where created_time between #{startTime} and #{endTime}")
    List<Map<String, Object>> selectEventList(@Param("startTime")String startTime, @Param("endTime")String endTime);
    
    @Select("SELECT DISTINCT extra1 as changeNo,event_title as title,created_time as createTime,created_by  FROM design_biz_change where status= #{state} and currentProcessor=#{userNo} and created_time between #{startTime} and #{endTime}")
    List<Map<String, Object>> queryChangeList(@Param("changeNo")String changeNo,@Param("userNo")String userNo,@Param("state")String state,@Param("startTime")String startTime,@Param("endTime")String endTime);
    
    @Select("SELECT DISTINCT extra1 as changeNo,created_time,created_by,title,description,type,basis,changeStatus,reason,referApp,startDept,currentProcessor,planStartDate,planCompleteDate,plan,startDeptLeader FROM design_biz_change where extra1= #{changeNo} ")
    Map<String, Object> queryChangeDetail(@Param("changeNo")String changeNo);

    @Select("select changeTaskNo,created_time,created_by ,created_by_name ,updated_time ,updated_by,changeNo ,type ,content,preApproval ,currentProcessor  ,taskStatus ,assignee ,assignedGroup ,approval,backToApprovalFlag   from design_biz_changetask where changeNo=#{changeNo}")
    List<Map<String, Object>> queryChangeTaskDetail(@Param("changeNo")String changeNo);
    
    @Select("")
    List<Map<String, Object>> queryChangeFiles(@Param("changeTaskNo")String changeTaskNo);
    
    @Select("")
    List<Map<String, Object>> confirmChangeTaskResult(@Param("changeTaskNo")String changeTaskNo);
    
    @Select("SELECT DISTINCT extra1 AS eventNo, event_title AS eventTile, event_info as eventDesc, extra5 AS currentUser, created_by AS startUser, created_time AS createTime FROM design_biz_incident where event_title=#{eventTitle} AND created_time between #{startTime} and #{endTime} ")
    List<Map<String, Object>> muliCloseEvent(@Param("eventTitle") String eventTitle,@Param("startTime") String  startTime,@Param("endTime") String  endTime);

    @Select("SELECT id FROM ${tableName} where extra1= #{taskNo}")
    Long getTaskId(@Param("taskNo") String taskNo,@Param("tableName") String tableName);

    @Select("SELECT id,colname as title,colcode as value FROM it_tables_data where service_tablename= #{tableName} order by orders")
    List<Map<String, Object>>  ittablesCol(@Param("tableName") String tableName);
    
    @Select("SELECT * FROM ${tableName} where itsmno= #{itsmNo}")
    List<Map<String, Object>> ittablesData(@Param("itsmNo") String itsmNo,@Param("tableName") String tableName);
    
    @Insert("#{sql}")
    Long insertData(@Param("sql") String sql);
    
    @Delete("delete from ${tableName} where id=#{id}")
    Long delData(@Param("id") String id,@Param("tableName") String tableName);
    
    @Delete("delete from ${tableName} where itsmno=#{itsmNo}")
    Long del(@Param("itsmNo") String itsmNo,@Param("tableName") String tableName);
}
