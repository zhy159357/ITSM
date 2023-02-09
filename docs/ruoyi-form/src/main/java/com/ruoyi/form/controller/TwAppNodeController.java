package com.ruoyi.form.controller;


import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.entity.TwAppNode;
import com.ruoyi.form.service.ITwAppNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chw
 * @since 2022-08-08
 */
@Controller
@RequestMapping("/twAppNode")
public class TwAppNodeController extends BaseController {

    @Autowired
    private ITwAppNodeService twAppNodeService;

    /**
     * 查询流程记录列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TwAppNode twAppNode) {
        twAppNode.setWorkNum(twAppNode.getBizId());
        startPage();
        List<TwAppNode> list = twAppNodeService.getTwAppNode(twAppNode);
        return getDataTable(list);

    }
}

