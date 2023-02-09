package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.ChangeTaskScene;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChangeTaskSceneMapper extends BaseMapper<ChangeTaskScene> {
    @Override
    int insert(@Param("changeTaskScene") ChangeTaskScene changeTaskScene);

    int updateById(@Param("changeTaskScene") ChangeTaskScene changeTaskScene);

    @Update("update change_task_scene set orgid=#{orgId} where orgname=#{orgName}")
    void updateOrgIdByOrgName(@Param("orgId") String orgId , @Param("orgName") String orgName);

    @Select("select * from change_task_scene where orgid=#{orgId} and auto_flag=0")
    List<ChangeTaskScene> getListByOrgId(@Param("orgId") String orgId);

    @Select("select * from change_task_scene where orgid=#{orgId} and auto_flag=1")
    List<ChangeTaskScene> getAutoListByOrgId(@Param("orgId") String orgId);

    @Select("select * from change_task_scene where auto_flag=0")
    List<ChangeTaskScene> getAll();

    @Select("select * from change_task_scene where auto_flag=1 and orgId=#{orgId} and name=#{sceneName}")
    ChangeTaskScene getAutoChangeTaskScene(@Param("orgId") String orgId,@Param("sceneName") String sceneName);

    @Select("select * from change_task_scene where auto_flag=1")
    List<ChangeTaskScene> getAutoAll();

    @Select("select * from change_task_scene where code=#{code} and auto_flag=0")
    ChangeTaskScene getChangeTaskSceneByCode(@Param("code") String code);

    @Select("select * from change_task_scene where code=#{code} and auto_flag=1")
    ChangeTaskScene getAutoChangeTaskSceneByCode(@Param("code") String code);
}
