package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 参数列别对象 og_typeinfo
 *
 * @author ruoyi
 * @date 2020-12-06
 */
@Data
public class OgTypeinfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String typeinfoId;

    /**
     * 分类编号
     */
    private String typeTypeNo;

    /**
     * 分类名称
     */
    private String typeTypeName;

    /**
     * 类别编号
     */
    private String typeNo;

    /**
     * 类别名称
     */
    private String typeName;

    /**
     * 类别描述
     */
    private String typeDescription;

    /**
     * 子类型节点
     */
    private String leaf;

    /**
     * 创建人
     */
    private String createUserId;

    /**
     * 无效标志
     */
    private String invalidationMark;

    /**
     * 序列号
     */
    private Long serial;

    /**
     * 类型等级
     */
    private Long typeLevel;

    /**
     * 父类型
     */
    private String parentId;

    private String updateTime;
    @TableField(exist = false)
    private OgTypeinfo parent;//父级

    @TableField(exist = false)
    private OgTypeinfo  children;

    @TableField(exist = false)
    private String parentTypeName;// 上级类别页面展示用

    @TableField(exist = false)
    private List<OgTypeinfo> childOgTypeInfo;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("typeinfoId", getTypeinfoId())
                .append("typeTypeNo", getTypeTypeNo())
                .append("typeTypeName", getTypeTypeName())
                .append("typeNo", getTypeNo())
                .append("typeName", getTypeName())
                .append("typeDescription", getTypeDescription())
                .append("leaf", getLeaf())
                .append("createUserId", getCreateUserId())
                .append("updateTime", getUpdateTime())
                .append("invalidationMark", getInvalidationMark())
                .append("serial", getSerial())
                .append("typeLevel", getTypeLevel())
                .append("parentId", getParentId())
                .toString();
    }
}
