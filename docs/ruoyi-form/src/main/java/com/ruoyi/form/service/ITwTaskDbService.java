package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.entity.TwTaskEntity;

import java.util.List;

/**
 * @program: ruoyi
 * @description: 任务
 * @author: ma zy
 * @date: 2022-11-22 10:29
 **/
public interface ITwTaskDbService extends IService<TwTaskEntity> {
    List<TwTaskEntity> getList(String workOrderId);
}
