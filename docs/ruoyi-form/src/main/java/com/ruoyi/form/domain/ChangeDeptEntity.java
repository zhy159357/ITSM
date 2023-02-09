package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @program: ruoyi
 * @description: 变更额度配置实体
 * @author: ma zy
 * @date: 2022-09-23 14:27
 **/
@Data
@TableName(value = "change_dept_system")
public class ChangeDeptEntity {

    /**表主键**/
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**机构id**/
    @TableField(value = "dept_id")
    private String orgId;
    /**机构名称*/
    @TableField(value = "dept_name")
    private String orgName;
    /**默认额度**/
    @TableField(value = "init_size")
    private String initSize;
    /**剩余额度**/
    @TableField(value = "over_size")
    private String overSize;
    /**重置日期**/
    @TableField(value = "replace_time")
    private String replaceTime;
    /**状态**/
    @TableField(value = "start_status")
    private String status;
    /**备注**/
    @TableField(value = "remarks")
    private String remarks;

}
