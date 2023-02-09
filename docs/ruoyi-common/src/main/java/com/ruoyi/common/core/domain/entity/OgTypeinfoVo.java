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
public class OgTypeinfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String typeinfoId;

    /**
     * 类别名称
     */
    private String typeName;

    /**
     * 子类对象
     */
    @TableField(exist = false)
    private List<OgTypeinfoVo> childOgTypeInfo;
}
