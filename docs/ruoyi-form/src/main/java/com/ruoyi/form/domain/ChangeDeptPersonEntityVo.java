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
public class ChangeDeptPersonEntityVo {

    /**表主键**/
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**机构负责人**/
    private String deptPerson;
    private String deptPersonName;
    /**机构分管领导**/
    private String deptLeaderPerson;
    private String deptLeaderPersonName;
    /**备注**/
    private String remarks;
    /**机构id*/
    private String deptId;
    /**机构id*/
    private String orgId;
    /**父机构id*/
    private String parentId;
    /**机构编号*/
    private String orgCode;
    /**机构名称*/
    private String orgName;
    /**机构等级*/
    private String orgLv;
    /**添加人*/
    private String adder;
    /**添加时间*/
    private String addtime;
    /**修改人*/
    private String moder;
    /**修改时间*/
    private String modtime;
    /**修改时间*/
    private String updatetime;
    /**无效标志*/
    private String invalidationMark;
    /**备注*/
    private String memo;
    /**分布标记*/
    private String branchMark;
    /**进出标记*/
    private String inoutsideMark;
    /**等级编号*/
    private String levelCode;
    /**是否叶子节点*/
    private String leaf;
    private String type;
    private String sort;
    private String operaternum;
    /**考核厂商序号*/
    private String khnum;
    /**父部门名称*/
    private String parentName;
    private String userAccount;
    private String userId;
    private String createDate;
}
