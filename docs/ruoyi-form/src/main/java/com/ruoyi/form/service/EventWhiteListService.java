package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.validation.Create;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.EventWhiteList;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface EventWhiteListService extends IService<EventWhiteList> {
    // 查询所有硬件保障白名单
    List<EventWhiteList> searchWhiteListAll(EventWhiteList eventWhiteList);

    // 批量保存硬件保障白名单
    void insertWhiteListBatch(List<EventWhiteList> list);
    String importData(EventWhiteList eventWhiteList);
    List<EventWhiteList> selectByVendor(String vendor);
}
