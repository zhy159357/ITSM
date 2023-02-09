package com.ruoyi.form.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
/*
List<SysinfoBean> 实体类
 */
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class AdpmAppVo implements Serializable {
    //  应用ID
    private String sysId;
    //  父应用ID
    private String parentSysId;
    // 父应用标识
    private String parentSysMark;
    // 父应用名称
    private String parentSysName;
    // 子应用类型
    private String sonSysType;
    // 应用编号
    private String sysCode;
    // 应用标识
    private String sysMark;
    //应用名称
    private String sysName;
    //英文名称
    private String sysNameEn;
    //  应用类型 111111
    private String sysType;
    // 重要等级
    private String sysImpoortantLevel;
    // 应用所属层
    private String sysLayerLevel;
    // 应用所属组
    private String sysGroup;
    //数据等级
    private String dataLevel;
    // 应用状态
    private String sysStatus;
    // 行龄
    private String cyear;
    // 应用定位
    private String sysPosition;
    // 自主可控  1：国产  2：非国产
    private String status;
    //  开发主管部室- 部门名称
    private String sysDevDeptName;




}
