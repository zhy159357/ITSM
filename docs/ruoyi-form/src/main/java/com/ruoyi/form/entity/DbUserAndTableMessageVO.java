package com.ruoyi.form.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 数据库用户和表空间vo
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DbUserAndTableMessageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String onlyKey;

    private String user;

    private String tableName;

    private String tableSpace;
}
