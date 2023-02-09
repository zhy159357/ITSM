package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.ChangeTaskOrg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IChangeTaskOrgMapper extends BaseMapper<ChangeTaskOrg> {
    @Override
    int insert(@Param("changeTaskOrg") ChangeTaskOrg changeTaskOrg);

    int updateById(@Param("changeTaskOrg")ChangeTaskOrg changeTaskOrg);

    @Select("select * from change_task_org")
    List<ChangeTaskOrg> getAll();

    @Update("update change_task_org set orgid=#{orgId} where orgname=#{orgName}")
    int updateOrgIdByOrgName(@Param("orgId") String orgId,@Param("orgName") String orgName);

    @Select("select * from change_task_org where orgName=#{orgName}")
    ChangeTaskOrg getByOrgName(@Param("orgName") String orgName);

    @Select("select * from change_task_org where orgid=#{orgId}")
    ChangeTaskOrg getByOrgid(@Param("orgId") String orgId);
}
