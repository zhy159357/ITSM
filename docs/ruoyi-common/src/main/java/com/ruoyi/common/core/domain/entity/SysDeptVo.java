package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author hecaili
 * @date 2020-08-29
 */
@Data
public class SysDeptVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 机构id
     */
    private String value;

    /**
     * 机构名称
     */
    private String label;

    /**
     * 子类对象
     */
    @TableField(exist = false)
    private List<SysDeptVo> children;
}
