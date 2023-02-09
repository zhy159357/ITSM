package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 参数设置对象 读取Excel表格 SystemManagementing
 * @author jiangfeng_sun
 * @date 2022-06-16
 */
@Data
public class SystemManagementing implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String managementId;

    /**
     * pid
     */
    private String pid;



    /**
     * 参数项代码
     */
    @Excel(name = "参数项代码", cellType = Excel.ColumnType.STRING, width = 30)
    private String paraName;

    /**
     * 参数项名称
     */
    @Excel(name = "参数项名称", width = 30)
    private String paraExplain;

}









