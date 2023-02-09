package com.ruoyi.form.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: ruoyi
 * @description: 任务表
 * @author: ma zy
 * @date: 2022-11-22 10:18
 **/
@Data
@TableName(value = "tw_task")
public class TwTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**表主键**/
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @TableField(value = "work_node_id")
    private String workNodeId;
    @TableField(value = "work_order_id")
    private String workOrderId;
    @TableField(value = "vm_ip")
    private String vmIp;
    @TableField(value = "vm_cpu")
    private String vmCpu;
    @TableField(value = "vm_memory")
    private String vmMemory;
    @TableField(value = "vm_disk")
    private String vmDisk;
    @TableField(value = "vm_gateway")
    private String vmGateway;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "vm_hostname")
    private String vmHostname;
    @TableField(value = "bridge")
    private String bridge;
    @TableField(value = "agent_ip")
    private String agentIp;
    @TableField(value = "template_version")
    private String templateVersion;
    @TableField(value = "cpu_warn")
    private String cpuWarn;
    @TableField(value = "mem_warn")
    private String memWarn;
    @TableField(value = "cj_change")
    private String cjChange;
    @TableField(value = "task_name")
    private String taskName;


}
