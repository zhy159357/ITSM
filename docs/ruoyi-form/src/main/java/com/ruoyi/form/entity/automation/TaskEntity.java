package com.ruoyi.form.entity.automation;


import lombok.Data;

import java.io.Serializable;

/**
 * isystemId	long	日常操作模板id
 * sysName	string	日常操作模板名称
 */
@Data
public class TaskEntity implements Serializable {

    private Long isystemId;

    private String sysName;
}
