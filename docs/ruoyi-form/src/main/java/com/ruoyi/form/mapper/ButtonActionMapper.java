package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.ButtonAction;

import java.util.Map;

public interface ButtonActionMapper extends BaseMapper<ButtonAction> {


    Map<String,Object> getObj(String sid);
}