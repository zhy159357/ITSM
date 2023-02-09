package com.ruoyi.form.openapi;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.framework.config.MybatisPlusConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DateUtils.YYYYMMDD;
import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD;
import static com.ruoyi.form.constants.ProblemConstant.CREATED_TIME;
import static com.ruoyi.form.constants.ProblemConstant.PROBLEM_TITLE;

@RestController
@RequestMapping("/api/problem")
@Api(tags = "问题单对接外部模块接口")
public class EsbProblemController {

    @Autowired
    CustomerFormMapper customerFormMapper;

    @PostMapping(value = "/queryProblemList")
    @ApiOperation("查询问题单列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginDT", value = "问题单创建时间起始日期,格式:yyyyMMdd", required = true),
            @ApiImplicitParam(name = "endDT", value = "问题单创建时间结束日期,格式:yyyyMMdd", required = true)
    })
    public AjaxResult getChangeTaskListAndOverTime(@RequestParam("beginDT") String beginDT, @RequestParam("endDT") String endDT) {

        Date bDate = DateUtils.parseDate(beginDT, YYYYMMDD);
        Date eDate = DateUtils.parseDate(endDT, YYYYMMDD);
        if (bDate.after(eDate)) {
            return AjaxResult.warn("开始时间不能大于结束时间!");
        }
        String s = DateUtils.formatDate(bDate, YYYY_MM_DD);
        Date begDate = DateUtils.parseDate(s, YYYY_MM_DD);
        String se = DateUtils.formatDate(eDate, YYYY_MM_DD);
        Date endDate = DateUtils.parseDate(se, YYYY_MM_DD);
        MybatisPlusConfig.customerTableName.set("design_biz_problem");
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                .select(RedundancyFieldEnum.extra1.name, PROBLEM_TITLE, CREATED_TIME)
                .ge(CREATED_TIME, begDate)
                .lt(CREATED_TIME, org.apache.commons.lang3.time.DateUtils.addDays(endDate, 1))
                .orderByDesc("created_time"));
        MybatisPlusConfig.customerTableName.remove();
        if (list.isEmpty()) {
            return AjaxResult.warn("没有查询到问题单数据,请重新更换时间查询!");
        }
        List<Map<String, Object>> problemList = list.stream().map(stringObjectMap -> {
            Map<String, Object> map = new HashMap<>();
            map.put("quesNo", stringObjectMap.get(RedundancyFieldEnum.extra1.name));
            map.put("quesTitle", stringObjectMap.get(PROBLEM_TITLE));
            map.put("createTime", stringObjectMap.get(CREATED_TIME));
            return map;
        }).collect(Collectors.toList());
        AjaxResult result = AjaxResult.success();
        result.put("resData", problemList);
        return result;
    }
}
