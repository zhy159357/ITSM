package com.ruoyi.form.vo;

import com.ruoyi.form.domain.ButtonConfigInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DetailsPageVO
 * @Description 页面详情展示对象
 * @Author JiaQi Zhang
 * @Date 2022/10/2 17:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "页面详情实体类对象")
public class DetailsPageVO {
    @ApiModelProperty("按钮配置信息")
    private List<ButtonConfigInfo> buttonConfigInfos;
    @ApiModelProperty("步骤信息")
    private Map<String, Object> formStepMap;
    @ApiModelProperty("表单追加信息")
    private List<Map<String, String>> appendJsonMap;
    @ApiModelProperty("业务表单json")
    private String formJson;
}
