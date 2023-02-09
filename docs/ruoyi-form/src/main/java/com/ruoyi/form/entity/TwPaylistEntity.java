package com.ruoyi.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: ruoyi
 * @description: 交付清单
 * @author: ma zy
 * @date: 2022-12-07 15:34
 **/
@Data
@TableName(value = "tw_paylist")
@EqualsAndHashCode(callSuper = false)
public class TwPaylistEntity implements Serializable {

    private String id;
    private String workOrderId;
    private String nodeInfo;
    private String sysName;
    private String hostIp;
    private String twType;
    private String userInfo;
    private String remark;
    private String pageName;
    private String pageSize;
    private String type;
    private String dbName;
    private String dbType;
    private String tableSpace;

}
