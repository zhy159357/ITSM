package com.ruoyi.form.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName FormInfoButtonVo
 * @Description 详情按钮信息对象
 * @Author JiaQi Zhang
 * @Date 2022/9/16 9:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormInfoButtonVo {
    /*按钮名称*/
    private String buttonName;
    /*按钮触发的接口URL*/
    private String buttonUrlPath;
}
