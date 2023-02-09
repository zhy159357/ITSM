package com.ruoyi.form.domain;

import lombok.Data;

/**
 * @program: ruoyi
 * @description: 变更机构数据配置
 * @author: ma zy
 * @date: 2022-09-23 14:27
 **/
@Data
public class ChangeDeptPersonVo {
    /**机构id**/
    private String orgId;
    /**机构负责人id**/
    private String deptPersonId;
    /**机构分管领导id**/
    private String deptLeaderPersonId;
    /**机构负责人name**/
    private String deptPersonName;
    /**机构分管领导name**/
    private String deptLeaderPersonName;
    private String userAccount;
    private String userId;

}
