package com.ruoyi.form.domain;

import com.ruoyi.common.core.domain.entity.PubParaValue;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 参数管理参数表
 */
@Data
public class PubParaSHYH implements Serializable {


    private static final long serialVersionUID = 1L;

    private String paraId;

    private String paraName;

    private String paraExplain;

    private String state;

    private String enable;

    private String updateFlag;

    private List<PubParaValue> childrenList;

}
