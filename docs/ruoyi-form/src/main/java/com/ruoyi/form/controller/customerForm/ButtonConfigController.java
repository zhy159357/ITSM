package com.ruoyi.form.controller.customerForm;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.ButtonConfigInfo;
import com.ruoyi.form.service.ButtonConfigInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ButtonConfigController
 * @Description 按钮配置控制类
 * @Author JiaQi Zhang
 * @Date 2022/10/2 15:53
 */
@RestController
@RequestMapping("/customerForm")
@Api(tags = "按钮配置控制类")
@RequiredArgsConstructor
public class ButtonConfigController {

    private final ButtonConfigInfoService buttonConfigInfoService;

    @PostMapping("/saveButtonConfigInfo")
    @ApiOperation("新增按钮配置信息")
    public AjaxResult saveButtonConfigInfo(@RequestBody ButtonConfigInfo buttonConfigInfo){
        return AjaxResult.success(buttonConfigInfoService.saveOrUpdate(buttonConfigInfo));
    }

    @GetMapping("/getButtonConfigInfo")
    @ApiOperation("获取按钮配置表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "业务表code",required = true),
            @ApiImplicitParam(name = "sid",value = "节点sid",required = true)
    })
    public AjaxResult getButtonConfigInfo(String code,String sid){
        return AjaxResult.success(buttonConfigInfoService.list(Wrappers.<ButtonConfigInfo>query().eq(ButtonConfigInfo.COL_BIZ_PROCESS_CODE,code).eq(ButtonConfigInfo.COL_BIZ_PROCESS_ACTIVITY_NODE_ID,sid)));
    }

}
