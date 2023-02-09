package com.ruoyi.form.domain;

import lombok.Data;

import java.util.Map;

@Data
public class FormFieldBean {
    private String type;
    private String label;
    private Map<String, Object> options;

}
