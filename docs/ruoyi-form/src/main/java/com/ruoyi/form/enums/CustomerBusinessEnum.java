package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName CustomerBusinessEnum
 * @Description 自定义表单业务异常描述
 * @Author JiaQi Zhang
 * @Date 2022/6/24 10:34
 */
@AllArgsConstructor
@Getter
public enum CustomerBusinessEnum {

    FORM_VERSION_EXCEPTION("901", "当前表单没有可用的版本，请联系管理员确认"),
    FORM_FIELD_EXCEPTION("902", "当前表单版本没有可用的字段值，请联系管理员确认"),
    ACTIVITY_IMAGE_EXCEPTION("903", "获取流程图失败，请联系管理员确认"),
    GROUP_TODO_ID_PARAMS_EXCEPTION("905", "请选择至少一个组"),
    EXPORT_IDS_ARRAY_EXCEPTION("906", "请选择至少一条数据"),
    DEPT_LEADER_IS_NOT_CONFIG("907", "当前登录人没有配置科室领导"),
    BIZ_DATA_NOT_EXIST("801", "不存在当前ID值数据"),
    FORM_NAME_EXCEPTION("904", "当前表单没有可用的表名，请联系管理员确认");


    private String code;
    private String desc;
}
