package com.ruoyi.form.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FormTemplateJsonBean {
    private Map<String, Object> config;
    private List<FormFieldBean> list;

    public static void main(String[] args) {
        String json = "{\n" +
                "\t\"config\": {\n" +
                "\t\t\"labelPosition\": \"left\",\n" +
                "\t\t\"labelWidth\": 15,\n" +
                "\t\t\"autoLabelWidth\": false\n" +
                "\t},\n" +
                "\t\"list\": [{\n" +
                "\t\t\"type\": \"input\",\n" +
                "\t\t\"label\": \"演示字段1\",\n" +
                "\t\t\"options\": {\n" +
                "\t\t\t\"label\": \"输入框\",\n" +
                "\t\t\t\"minWidth\": 10,\n" +
                "\t\t\t\"width\": 100,\n" +
                "\t\t\t\"defaultValue\": \"\",\n" +
                "\t\t\t\"placeholder\": \"请输入\",\n" +
                "\t\t\t\"clearable\": false,\n" +
                "\t\t\t\"hidden\": false,\n" +
                "\t\t\t\"disabled\": false,\n" +
                "\t\t\t\"customStyle\": \"\",\n" +
                "\t\t\t\"dynamicFun\": \"\",\n" +
                "\t\t\t\"eventName\": \"change\",\n" +
                "\t\t\t\"eventNameOptions\": [{\n" +
                "\t\t\t\t\"label\": \"change\",\n" +
                "\t\t\t\t\"value\": \"change\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"click\",\n" +
                "\t\t\t\t\"value\": \"click\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"blur\",\n" +
                "\t\t\t\t\"value\": \"blur\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"dblclick\",\n" +
                "\t\t\t\t\"value\": \"dblclick\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"focus\",\n" +
                "\t\t\t\t\"value\": \"focus\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"mouseout\",\n" +
                "\t\t\t\t\"value\": \"mouseout\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"mouseover\",\n" +
                "\t\t\t\t\"value\": \"mouseover\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"submit\",\n" +
                "\t\t\t\t\"value\": \"submit\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"reset\",\n" +
                "\t\t\t\t\"value\": \"reset\"\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"model\": \"input_1619495679940\",\n" +
                "\t\t\"key\": \"input_1619495679940\",\n" +
                "\t\t\"rules\": [{\n" +
                "\t\t\t\"required\": true,\n" +
                "\t\t\t\"message\": \"必填项\",\n" +
                "\t\t\t\"trigger\": \"blur\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"type\": \"input\",\n" +
                "\t\t\"label\": \"演示字段2\",\n" +
                "\t\t\"options\": {\n" +
                "\t\t\t\"label\": \"输入框\",\n" +
                "\t\t\t\"minWidth\": 10,\n" +
                "\t\t\t\"width\": 100,\n" +
                "\t\t\t\"defaultValue\": \"\",\n" +
                "\t\t\t\"placeholder\": \"请输入\",\n" +
                "\t\t\t\"clearable\": false,\n" +
                "\t\t\t\"hidden\": false,\n" +
                "\t\t\t\"disabled\": false,\n" +
                "\t\t\t\"customStyle\": \"\",\n" +
                "\t\t\t\"dynamicFun\": \"\",\n" +
                "\t\t\t\"eventName\": \"change\",\n" +
                "\t\t\t\"eventNameOptions\": [{\n" +
                "\t\t\t\t\"label\": \"change\",\n" +
                "\t\t\t\t\"value\": \"change\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"click\",\n" +
                "\t\t\t\t\"value\": \"click\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"blur\",\n" +
                "\t\t\t\t\"value\": \"blur\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"dblclick\",\n" +
                "\t\t\t\t\"value\": \"dblclick\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"focus\",\n" +
                "\t\t\t\t\"value\": \"focus\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"mouseout\",\n" +
                "\t\t\t\t\"value\": \"mouseout\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"mouseover\",\n" +
                "\t\t\t\t\"value\": \"mouseover\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"submit\",\n" +
                "\t\t\t\t\"value\": \"submit\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"label\": \"reset\",\n" +
                "\t\t\t\t\"value\": \"reset\"\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"model\": \"input_1619495681108\",\n" +
                "\t\t\"key\": \"input_1619495681108\",\n" +
                "\t\t\"rules\": [{\n" +
                "\t\t\t\"required\": true,\n" +
                "\t\t\t\"message\": \"必填项\",\n" +
                "\t\t\t\"trigger\": \"blur\"\n" +
                "\t\t}]\n" +
                "\t}],\n" +
                "\t\"dynamicKeyList\": []\n" +
                "}";
        FormTemplateJsonBean formTemplateJsonBean = JSON.parseObject(json, FormTemplateJsonBean.class);
        System.out.println(formTemplateJsonBean.getList());
        System.out.println(formTemplateJsonBean.getConfig());
    }
}
