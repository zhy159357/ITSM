package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.validation.Create;
import com.ruoyi.common.annotation.validation.Update;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class EventWhiteList implements Serializable {
    @NotNull(message = "ID不能为空", groups = {Update.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 姓名
    @Excel(name = "姓名")
    @Size(min = 2, max = 16, message = "姓名必须为2~16位", groups = {Create.class, Update.class})
    @NotEmpty(message = "姓名不能为空", groups = {Create.class})
    private String name;
    // 证件类型 1-居民身份证;2-港澳通行证;3-台湾居民来往大陆通行证;4-护照;
    @Excel(name = "证件类型: 1-居民身份证;2-港澳通行证;3-台湾居民来往大陆通行证;4-护照")
    @NotEmpty(message = "证件类型不能为空", groups = {Create.class})
    private String certificatesType;
    // 证件编号
    @Excel(name = "证件编号")
    @NotEmpty(message = "证件编号不能为空", groups = {Create.class})
    @Size(min = 6, max = 64, message = "证件号必须为6~64位置", groups = {Create.class, Update.class})
    private String certificatesNumber;
    @Excel(name = "证件是否有效: 1-有效;0-无效")
    // 是否有效 1-有效；0-无效
    @NotEmpty(message = "是否有效不能为空", groups = {Create.class})
    private String availability;
    // 手机号
    @Excel(name = "手机号")
    @NotEmpty(message = "手机号不能为空", groups = {Create.class})
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "不符合手机号规则", groups = {Create.class, Update.class})
    private String phoneNumber;
    @Excel(name = "所属厂商")
    @NotEmpty(message = "所属厂商不能为空", groups = {Create.class})
    private String vendor;
}
