package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 参数设置对象 优先级 PriorityParavalue
 * @author jiangfeng_sun
 * @date 2022-06-20
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PriorityParavalue implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * pubParavalueIdDegree
     */
    private String pubParavalueIdDegree;



    /**
     * pubParavalueIdScope
     */

    private String pubParavalueIdScope;

    /**
     * priority
     */

    private String priority;


    private String code;


}
