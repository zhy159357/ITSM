package com.ruoyi.form.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TwUserDb implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    /**
     * 节点id
     */
    private String wordNodeId;

    /**
     * 0:应用用户，1：数据库信息
     */
    private String type;

    /**
     * 用户名
     */
    private String username;

    /**
     * 主组
     */
    private String primaryGroup;

    /**
     * 副组
     */
    private String secondaryGroup;

    /**
     * 挂载目录
     */
    private String mountDirectory;

    /**
     * 空间大小
     */
    private String spaceSize;

    /**
     * shell脚本
     */
    private String shell;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 字符集
     */
    private String characterSet;

    /**
     * 表空间大小
     */
    private String tableSpaceSize;

    /**
     * 表空间名称
     */
    private String tableSpaceName;

    /**
     * 用户和表空间，表名称的json
     */
    private String userDb;
    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * 实例名
     */
    private String spaceName;



}
