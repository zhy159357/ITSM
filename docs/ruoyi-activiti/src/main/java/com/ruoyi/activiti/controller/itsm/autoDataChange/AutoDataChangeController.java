package com.ruoyi.activiti.controller.itsm.autoDataChange;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.server.EntegorServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动化数据变更Controller
 */
@Controller
@RequestMapping("/auto/data")
@Transactional(rollbackFor = Exception.class)
public class AutoDataChangeController extends BaseController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoDataChangeController.class);
    private String prefix = "auto/data";

    @Autowired
    private EntegorServerImpl entegorServer;

    /**
     * 获取自动化对应的token信息
     * {
     "code": 0,
     "mes": "认证成功！",
     "access_token": "1551229262861"
     }
     * @return
     */
    @GetMapping("/show")
    public String show(){
        return prefix + "/show";
    }
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(){
        startPage();
        List<Map<String, String>> dataCommonTaskList = entegorServer.getDataCommonTaskList("Sys234234324324", "逻辑集中系统");
        return getDataTable(dataCommonTaskList);

    }


    @PostMapping("/param/list")
    @ResponseBody
    public TableDataInfo paramList(String scriptUuid,String serviceName){
        List<Map<String, Object>> paramMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("scriptUuid",scriptUuid);
        map.put("serviceName",serviceName);
        paramMap.add(map);
        startPage();
        List<Map<String, String>> serviceParamList = entegorServer.getServiceParamList(paramMap);
        return getDataTable(serviceParamList);

    }

    /**
     * 对接自动化入口
     * @param json
     * @param userName
     * @param itsmNo
     * @param wType
     * @return
     */
    @PostMapping("/startMiddle")
    @ResponseBody
    public Map startMiddle(String json,String userName,String itsmNo,String wType,String token){
       Map<String, Object> paramMap = new HashMap<>();
        paramMap = entegorServer.startAutoEntegorMiddleGround(wType,json, userName,itsmNo,token);
        return paramMap;
    }
    
    @PostMapping("/middleTables")
    @ResponseBody
    public Map middleTables(String userName,String templateName,String token){
       Map<String, Object> paramMap = new HashMap<>();
        paramMap = entegorServer.middleTables(userName,templateName,token);
        return paramMap;
    }
//    @PostMapping("/middleTableParams")
//    @ResponseBody
//    public Map middleTableParams(String json,String userName,String itsmNo,String wType){
//       Map<String, Object> paramMap = new HashMap<>();
//        paramMap = entegorServer.middleTableParams(json, userName,itsmNo);
//        return paramMap;
//    }
    @PostMapping("/middleTaskState")
    @ResponseBody
    public Map middleTaskState(String userName,String itsmNo,String wType,String token){
       Map<String, Object> paramMap = new HashMap<>();
        paramMap = entegorServer.middleTaskState(userName,itsmNo,wType,token);
        return paramMap;
    }
    @PostMapping("/getAutoToken")
    @ResponseBody
    public Map getAutoToken(String userName){
    	Map<String, Object> paramMap = new HashMap<>();
    	paramMap.put("token", entegorServer.getAutoToken(userName));
    	return paramMap;
    }
}
