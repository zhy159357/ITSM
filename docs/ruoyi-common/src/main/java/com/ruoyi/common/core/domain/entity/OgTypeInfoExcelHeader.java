package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class OgTypeInfoExcelHeader {

    /**
     * id
     */
    private String schedulingId;

    /**
     * pid
     */
    private String pid;

    /**
     * 姓名
     */
    @Excel(name = "类别名称", width = 30)
    private String typeName;

    /**
     * 联系电话
     */
    @Excel(name = "类别编号", cellType = Excel.ColumnType.STRING, width = 30)
    private String typeNo;




}
