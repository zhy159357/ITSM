package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/cloud")
public class SysCloudController {

    private String prefix = "system/cloud";

    @Value("${itsm.cloudViewUrl}")
    private String cloudViewUrl;
    @GetMapping()
    public String coludView(Model model){

        model.addAttribute("cloudViewUrl",cloudViewUrl);
        return prefix + "/cloudView";
    }
}
