package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventInterfaceStatus {
    /**
     * 状态
     * New：新建
     * Assigned：已指派
     * In Progress：进行中
     * Resolved：已解决
     * Closed：已关闭
     * Cancelled：已取消
     * （状态为“已指派”，则状态显示为“已指派”；
     * 状态为“进行中”或“已解决”，则状态显示为处理中；
     * 状态为“已关闭”，并且满意度调查为空或者“null”，则状态显示为“已解决”；
     * 状态为“已关闭”，并且满意度调差不为空或者“null”，则状态显示为“已关闭”；
     * 客户撤单标识不为空，并且满意度为空或者“null”，则显示“已撤单”状态）
     */
    New("New", "新建"),
    Assigned("Assigned", "已指派"),
    In_Progress("In Progress", "进行中"),
    Resolved("Resolved", "已解决"),
    Closed("Closed", "已关闭"),
    Cancelled("Cancelled", "已取消");

    private String code;
    private String info;
}
