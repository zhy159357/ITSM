package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.EventIncSearchReqVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义表单模块动态sql重写mybatisplus语句
 */
public interface CustomerFormMapper extends BaseMapper<CustomerFormContent> {

    @Override
    int insert(@Param("customerFormContent") CustomerFormContent customerFormContent);

    @Override
    int updateById(@Param("customerFormContent") CustomerFormContent customerFormContent);

    @Override
    @Delete("delete from customer_form_content where id = #{id}")
    int deleteById(@Param("id") Serializable id);

    List<Map<String,Object>> selectcolumnComment(String tableName);


    @Select({"<script>",
            "SELECT id , version FROM design_form_version where code = #{tableName}  ",
            "<when test='version != null'>",
            "AND version=#{version}",
            "</when>",
            "<when test='version == null'>",
            "AND is_current = 1",
            "</when>",
            "</script>"})
    Map<String,Long> getCurrentTableInfo(@Param("tableName") String tableName,@Param("version") Integer version);

    @Select("select name, description , display,record_data_changed,exportable,editable from design_field_version where form_version_id =#{versionId} order by showorder ")
    List<Map<String,String>> getFormFieldInfo(@Param("versionId") Long versionId);


    @Select("select json from design_form_version where id=#{formVersionId} ")
    Map<String,Object> getFormJsonInfo(Long formVersionId);

    @Select("SELECT DISTINCT\n" +
            " a.id id,\n" +
            " a.version version,\n" +
            " a.is_current isCurrent \n" +
            "FROM design_form_version a\n" +
            "RIGHT JOIN design_field_version b ON b.form_version_id = a.id  where a.code=#{tableName}  ")
    List<Map<String,Object>> getCurrentFormAllVersions(String tableName);


    @Update("update customer_form_content set instance_id=#{instanceId} where id =#{businessKey} ")
    int setInstanceId(@Param("instanceId") String instanceId,@Param("businessKey") String businessKey);

    @Select("select id from customer_form_content where instance_id=#{processId} ")
    Integer selectOneByProcessId(String processId);

    @Select("select version from design_form_version where id=#{versionId}")
    Integer getFormVersionById(String versionId);

    @Select("select created_by from customer_form_content where instance_id=#{instanceId} ")
    String  selectApplyUser(String instanceId);

    @Select("select name  from design_form_version where id=#{id}  ")
    String getFormName(Long id);

    List<Map<String, Object>> selectIncidentMapByCondition(@Param("tableName") String tableName, @Param("search") EventIncSearchReqVo vo);

    Long selectIncidentCountByCondition(@Param("tableName") String tableName, @Param("search") EventIncSearchReqVo vo);

    Map<String, Object> selectIncidentMapByCode(@Param("tableName") String tableName, @Param("imCode") String imCode);

    void updateEventIncident(@Param("tableName") String tableName, @Param("params") Map<String,Object> params);

    @Select("select assignedGroup from design_biz_incident where id=#{id}")
    String selectIncidentGroupById(Long id);

    @Select("select extra1 from customer_form_content where instance_id=#{instanceId} ")
    String  selectEventNoByInstanceId(String instanceId);

    @Select("SELECT a.id,a.instance_id, a.extra1,a.status,a.content title,(CASE b.type\n" +
            "\tWHEN '1' THEN\n" +
            "\t\t'普通变更'\n" +
            "\tWHEN '2' THEN\n" +
            "\t  '紧急变更'\n" +
            "\tWHEN '3' THEN\n" +
            "\t  '评审变更'\n" +
            "\tELSE\n" +
            "\t\t''\n" +
            "END) changeType\n" +
            " FROM `design_biz_changetask` a INNER JOIN design_biz_change b on a.changeNo=b.extra1 where a.created_by =#{userId}")
    List<Map<String,Object>> selectChangeTasksByCreated(String userId);


    List<Map<String,Object>> selectChangeTasksByProcessIds(String userId);
}
