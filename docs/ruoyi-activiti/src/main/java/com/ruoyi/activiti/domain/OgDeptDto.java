package com.ruoyi.activiti.domain;

import lombok.Data;

/**
 * @program: ruoyi
 * @description: 机构同步数据对象
 * @author: ma zy
 * @date: 2022-10-31 08:50
 **/
@Data
public class OgDeptDto {
    //机构编码
    private String orgcode;
    //机构名称
    private String orgname;
    //机构类型
    private String orgtype;
    //机构层级
    private String orglevel;
    //机构状态
    private String orgstatus;
    //父机构编码
    private String p_orgcode;
    //父机构名称
    private String p_orgname;
    //创建时间
    private String create_time;
    //更新时间
    private String update_time;
    //备注
    private String remark;
}
