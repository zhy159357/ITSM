package com.ruoyi.web.controller.tool;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;

import io.swagger.annotations.Api;

@Api("日志下载")
@RestController
@RequestMapping("/downLoadLog/dir")
public class DownLoadLogController extends BaseController{
	   private String prefix = "/downloadlog/dir";
    @GetMapping()
    public String index()
    {
        return  prefix + "/downloadlog";
    }
}
