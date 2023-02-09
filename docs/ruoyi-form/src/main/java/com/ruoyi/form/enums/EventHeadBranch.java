package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventHeadBranch {
    /**
     * 总分行时间标记
     * Head：总行事件
     * Branch：分行事件
     */
    Head("Head", "总行事件"),
    Branch("Branch", "分行事件");

    private String code;
    private String info;
}
