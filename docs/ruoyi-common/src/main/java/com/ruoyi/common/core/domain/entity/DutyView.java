package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;

/**
 * @author dayong_sun
 * @date 2020-01-19
 */
public class DutyView implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 标题 */
    private String title;

    /** 位置 */
    private String valign = "middle";

    /** 位置 */
    private String align = "center";

    /** 字段 */
    private String field;

    /** 列 */
    private int colspan = 1;

    /** 行 */
    private int rowspan = 1 ;

    /** 类别描述 */
    private String typeDescription;

    /** 主值类别 */
    private String leader;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValign() {
        return valign;
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
