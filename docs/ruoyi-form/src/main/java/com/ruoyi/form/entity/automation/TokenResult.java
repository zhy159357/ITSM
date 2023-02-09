package com.ruoyi.form.entity.automation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * {
 * "message": "获取token成功！",
 * " token": "1533540271770",
 * "isOk": true,
 * "resultList": null
 * }
 */
@Data
public class TokenResult implements Serializable {

    private String message;

    private String token;

    private Boolean isOk;

    private List resultList;

}
