package com.ruoyi.form.domain;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName CustomerBaseEntity
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/10/19 9:40
 */
@Data
public class CustomerBaseEntity {
    private Long id;
    private String instanceId;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;
    private String extra5;
    private String status;
    private String createdTime;
    private String createdBy;
    private String updatedTime;
    private String updatedBy;
    private Map<String,Object> fieldInfos;
}
