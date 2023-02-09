package com.ruoyi.form.controller.customerForm;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ApproveTempleteController
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2023/2/1 13:44
 */
@RestController
@RequestMapping("/customerForm")
@Api(tags = "自定义表单模块")
@CrossOrigin
public class ApproveTempleteController {

    @Resource
    IOgPersonService iOgPersonService;

    @PostMapping("/queryWorkOrderPersonal")
    @ApiOperation("查询工单模块的相关审批人")
    public AjaxResult queryWorkOrderPersonal(){
        List<OgPerson> ogPeople = iOgPersonService.selectOgPersonListByOrgId(CustomerBizInterceptor.currentUserThread.get().getOrgId());
        List<Object> ogPeopleMapList = ogPeople.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            mapPeople.put("value", op.getpId());
            mapPeople.put("label", op.getpName());
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }
}
