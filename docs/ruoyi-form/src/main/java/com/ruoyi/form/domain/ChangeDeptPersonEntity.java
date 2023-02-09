package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: ruoyi
 * @description: 变更机构数据配置
 * @author: ma zy
 * @date: 2022-09-23 14:27
 **/
@Data
@TableName(value = "change_dept_person")
public class ChangeDeptPersonEntity {

    /**表主键**/
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**机构id**/
    @TableField(value = "dept_id")
    private String orgId;
    /**机构名称*/
    @TableField(value = "dept_name")
    private String orgName;
    /**机构负责人**/
    @TableField(value = "dept_person")
    private String deptPerson;
    /**机构分管领导**/
    @TableField(value = "dept_leader_person")
    private String deptLeaderPerson;
    /**备注**/
    @TableField(value = "remarks")
    private String remarks;

    /**接口用户帐号**/
    @TableField(value = "user_account")
    private String userAccount;
    /**用户id**/
    @TableField(value = "user_id")
    private String userId;
    /**创建时间**/
    @TableField(value = "create_date")
    private String createDate;
}
