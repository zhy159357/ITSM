package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.domain.TelSkillsOrg;
import com.ruoyi.activiti.service.ITelSkillService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.TelTerminal;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ITelTerminalService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 话机Controller
 *
 * @author
 * @date 2020-12-17
 */
@Controller
@RequestMapping("/system/terminal")
public class TelTerminalController extends BaseController {
    private String prefix = "system/terminal";

    @Autowired
    private ITelTerminalService telTerminalService;
    @Value("${jnz.getAllByHttpClient}")
    private String getAllByHttpClientUrl;
    @Value("${jnz.SaveOrUpdateAgentInfo}")
    private String saveOrUpdateAgentInfoUrl;
    @Autowired
    private ITelSkillService iTelSkillService;

    @GetMapping()
    public String terminal() {
        return prefix + "/terminal";
    }

    @GetMapping("/show")
    public String show() {
        return prefix + "/terminalshow";
    }

    @GetMapping("/showbuss")
    public String showBus(ModelMap mmap) {
        TelTerminal telTerminal = new TelTerminal();
       // telTerminal.setShowFlag("1");
        mmap.put("telTerminal", telTerminal);
        return prefix + "/terminalshowbuss";
    }

    @GetMapping("/two")
    public String two(ModelMap mmap) {
        TelTerminal telTerminal = new TelTerminal();
        telTerminal.setShowFlag("1");
        mmap.put("telTerminal", telTerminal);
        return prefix + "/terminaltwo";
    }

    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TelTerminal telTerminal) {
        if ("1".equals(telTerminal.getShowFlag())) {
            String userId = ShiroUtils.getUserId();
            telTerminal.setJobNumber(userId);
        }
        startPage();
        List<TelTerminal> list = telTerminalService.selectTelTerminalList(telTerminal);
        return getDataTable(list);
    }


    /**
     * 电话接入
     * */
    @PostMapping("/main")
    @ResponseBody
    public TableDataInfo listTwo() {
        String userId = ShiroUtils.getOgUser().getpId();
        TelTerminal telTerminal = new TelTerminal();
        telTerminal.setJobNumber(userId);
        //获取当前登录人
        List<TelTerminal> list = telTerminalService.selectTelTerminalListTwo(telTerminal);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelTerminal telTerminal) {
        String isCurrentPage = (String) telTerminal.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<TelTerminal> list = telTerminalService.selectTelTerminalList(telTerminal);
            for (TelTerminal terminal : list) {
                terminal.setpName(terminal.getOgPerson().getpName());
                terminal.setUsername(terminal.getOgUser().getUsername());
            }
            ExcelUtil<TelTerminal> util = new ExcelUtil<TelTerminal>(TelTerminal.class);
            return util.exportExcel(list, "话机绑定");
        } else if ("all".equals(isCurrentPage)) {
            List<TelTerminal> list = telTerminalService.selectTelTerminalList(telTerminal);
            for (TelTerminal terminal : list) {
                terminal.setpName(terminal.getOgPerson().getpName());
                terminal.setUsername(terminal.getOgUser().getUsername());
            }
            ExcelUtil<TelTerminal> util = new ExcelUtil<TelTerminal>(TelTerminal.class);
            return util.exportExcel(list, "话机绑定");
        }
        return null;
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        TelTerminal telTerminal = new TelTerminal();
        String userId = ShiroUtils.getUserId();
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telTerminal.setCreateTime(nowTime);
        telTerminal.setCreateId(userId);
        OgPerson ogPerson = telTerminalService.selectOgPerson(telTerminal);
        telTerminal.setOgPerson(ogPerson);
        mmap.put("telTerminal", telTerminal);
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "绑定", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TelTerminal telTerminal) {
        String value = telTerminalService.checkBindInfoUnique(telTerminal);
        if (UserConstants.TEL_NAME_UNIQUE.equals(value)) {
            return error("该员工号已经与其他IP地址绑定，请选择其他工号");
        } else if (UserConstants.TEL_SERVERIP_UNIQUE.equals(value)) {
            return error("新增话机电话银行工号已存在");
        }
        OgUser ogUser = ShiroUtils.getOgUser();
        telTerminal.setCreateId(ogUser.getUserId());
        telTerminal.setId(UUID.getUUIDStr());
        return toAjax(telTerminalService.insertTelTerminal(telTerminal));
    }

    /**
     * 修改话机绑定
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        TelTerminal telTerminal = telTerminalService.selectTelTerminalById(id);
        mmap.put("telTerminal", telTerminal);
        return prefix + "/edit";
    }

    /**
     * 修改保存话机绑定
     */
    @Log(title = "保存", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TelTerminal telTerminal) {
        return toAjax(telTerminalService.updateTelTerminal(telTerminal));
    }

    /**
     * 修改技能组绑定
     */
    @GetMapping("/edittwo/{id}")
    public String edittwo(@PathVariable("id")String id, ModelMap mmap) {
        TelTerminal telTerminal = telTerminalService.selectTelTerminalById(id);
        mmap.put("telTerminal", telTerminal);
        return prefix + "/edittwo";
    }

    /**
     * 修改保存技能组
     */
    @Log(title = "保存", businessType = BusinessType.UPDATE)
    @PostMapping("/edittwo")
    @ResponseBody
    public AjaxResult editSavetwo(TelTerminal telTerminal) {
        //调用查询接口获取uuid的值
        String serviceIp = telTerminal.getServiceIp();
        Map uuidMap = getUUIDByHttpClient("Agent", serviceIp, "0");
        //调用查询接口获取技能组code
        TelSkillsOrg telSkillsOrg=iTelSkillService.selectTelSkillById(telTerminal.getSkillId());
        String code = getCodeByHttpClient("Skill", telSkillsOrg.getSkillsGroupTelname(), "0");
        Boolean b = saveOrUpdateAgentInfo(uuidMap,code,telTerminal.getIfskillno(),telSkillsOrg.getSkillsGroupTelname());
        if (b){
            return toAjax(telTerminalService.updateTelTerminal(telTerminal));
        }
        throw new BusException("保存失败");
    }
    /**
     * 查询电话银行接口绑定的技能组uuid
     *
     * @param value 电话银行工号
     * @return
     */
    private Map getUUIDByHttpClient(String dataType, String value, String type) {
        String url = getAllByHttpClientUrl;
        JSONObject parameters = new JSONObject();
        /**
         * {
         * "dataType": "Agent",
         * "offset": "0",
         * "limit": "10",
         * "filters": [{
         *     "name": "agentId",
         *     "type": "0",
         *     "value": ""
         * }]
         * }
         */
        try {
            parameters.put("dataType", dataType);
            parameters.put("offset", "0");
            parameters.put("limit", "10");
            List list = new ArrayList();
            Map map = new HashMap<>();
            map.put("name", "agentId");
            map.put("type", type);
            map.put("value", value);
            list.add(map);
            parameters.put("filters", list);
        } catch (Exception e) {
            throw new BusException("查询电话银行接口JSON数据异常");
        }

        HttpURLConnection con = null;
        try {
            URL realUrl = new URL(url);
            con = (HttpURLConnection) realUrl.openConnection();
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json;charset=utf-8");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
            writer.print(parameters.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = reader.readLine();
            Map map = new HashMap();
            if (line != null && !"".equals(line)) {
                net.sf.json.JSONObject aa = net.sf.json.JSONObject.fromObject(line);
                net.sf.json.JSONObject info = aa.getJSONObject("info");
                JSONArray data = info.getJSONArray("data");
                for (Object datum : data) {
                    com.alibaba.fastjson.JSONObject json = JSON.parseObject(datum.toString());
                    Long uuid = Long.parseLong(json.get("uuid").toString());//uuid
                    Object channelmaxline = json.get("channelmaxline");
                    Object capacityrule = json.get("capacityrule");
                    Object taptainlevel = json.get("taptainlevel");
                    Object orgCode = json.get("orgCode");
                    Object channeltype = json.get("channeltype");
                    Object bussinesstype = json.get("bussinesstype");
                    Object active = json.get("active");
                    Object destsys = json.get("destsys");
                    Object canroute = json.get("canroute");
                    Object userref = json.get("userref");
                    Object agentId = json.get("agentId");
                    Object code = json.get("code");

                    map.put("uuid", uuid);
                    map.put("channelmaxline", channelmaxline);
                    map.put("capacityrule", capacityrule);
                    map.put("taptainlevel", taptainlevel);
                    map.put("orgCode", orgCode);
                    map.put("channeltype", channeltype);
                    map.put("bussinesstype", bussinesstype);
                    map.put("active", active);
                    map.put("destsys", destsys);
                    map.put("canroute", canroute);
                    map.put("userref", userref);
                    map.put("agentId", agentId);
                    map.put("code", code);
                }
                net.sf.json.JSONObject head = aa.getJSONObject("head");
                String errorCode = head.get("errorCode").toString();
                //0代表成功
                if ("0".equals(errorCode) && map.size() != 0) {
                    return map;
                } else if (map.size() == 0) {
                    throw new BusException("查询电话银行接口失败：无该工号");
                } else {
                    throw new Exception("查询电话银行接口失败：" + head.getString("errorMessage"));
                }
            }
            reader.close();
            con.disconnect();
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            throw new BusException("电话银行接口报错：" + e.getMessage());
        }
        return null;
    }
    private String getCodeByHttpClient(String dataType, String skillsGroupTelName, String type) {
        String url = getAllByHttpClientUrl;
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("dataType", dataType);
            parameters.put("offset","0");
            parameters.put("limit", "10");
            List list = new ArrayList();
            Map map = new HashMap<>();
            map.put("name","name");
            map.put("type","0");
            map.put("value",skillsGroupTelName);
            list.add(map);
            parameters.put("filters",list);
            logger.info("電話銀行查詢code-------->"+parameters.toString());
        } catch (Exception e) {
            throw new BusException("send电话银行接口JSON数据异常");
        }

        HttpURLConnection con = null;
        try {
            URL realUrl = new URL(url);
            con = (HttpURLConnection) realUrl.openConnection();
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type","application/json;charset=utf-8");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
            writer.print(parameters.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = reader.readLine();
            String code = null;
            if(line !=null && !"".equals(line)){
                net.sf.json.JSONObject aa=net.sf.json.JSONObject.fromObject(line);
                net.sf.json.JSONObject info = aa.getJSONObject("info");
                JSONArray data = info.getJSONArray("data");
                for (Object datum : data) {
                    com.alibaba.fastjson.JSONObject json =JSON.parseObject(datum.toString());
                    code = json.get("code").toString();
                }
                net.sf.json.JSONObject head = aa.getJSONObject("head");
                String errorCode = head.get("errorCode").toString();
                //0代表成功
                if("0".equals(errorCode) && code != null){
                    return code;
                }else if(code == null){
                    throw new BusException("send电话银行接口失败：电话银行未配置该技能组");
                }else {
                    throw new BusException("send电话银行接口失败："+head.getString("errorCodeI18n"));
                }
            }
            reader.close();
            con.disconnect();
        }catch (BusException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusException("电话银行接口报错："+e.getMessage());
        }
        return "0";
    }
    private Boolean saveOrUpdateAgentInfo(Map uuidMap,String code,String ifSkillNo,String skillsGroupName){
        String url = saveOrUpdateAgentInfoUrl;
        JSONObject parameters = new JSONObject();
        try {
            Map headMap = new HashMap<>();
            headMap.put("orgId","");
            headMap.put("orgCode","");
            headMap.put("vers","");
            headMap.put("reqId","");
            parameters.put("head", headMap);

            Map infoMap = new HashMap<>();
            infoMap.put("dataType","Agent");
            List dataList = new ArrayList();
            Map dataMap = new HashMap<>();
            //修改需要opType为2，uuid不能为空
            infoMap.put("opType","2");
            dataMap.put("UUID",uuidMap.get("uuid"));
            if ("1".equals(uuidMap.get("active"))){
                dataMap.put("Active",true);
            }else{
                dataMap.put("Active",false);
            }
            dataMap.put("AgentId",uuidMap.get("agentId"));
            List agentSkillList = new ArrayList();
            Map agentSkillMap = new HashMap<>();
            agentSkillMap.put("Skill",code);
            agentSkillMap.put("SkillLevel","1");
            //是否添加默认技能组
            if ("1".equals(ifSkillNo)){
                Map agentSkillDefaultMap = new HashMap<>();
                agentSkillDefaultMap.put("SkillLevel","2");
                agentSkillDefaultMap.put("Skill","ALL");
                agentSkillList.add(agentSkillDefaultMap);
            }
            agentSkillList.add(agentSkillMap);
            dataMap.put("AgentSkill",agentSkillList);
            dataMap.put("Bussinesstype",uuidMap.get("bussinesstype"));
            dataMap.put("Deptgroup",skillsGroupName);
            if ("1".equals(uuidMap.get("canroute"))){
                dataMap.put("Canroute",true);
            }else{
                dataMap.put("Canroute",false);
            }

            dataMap.put("Channelmaxline",uuidMap.get("channelmaxline"));
            dataMap.put("Channeltype",uuidMap.get("channeltype"));
            dataMap.put("Capacityrule",uuidMap.get("capacityrule"));
            dataMap.put("Destsys",uuidMap.get("destsys"));
            dataMap.put("OrgCode",uuidMap.get("orgCode"));
            dataMap.put("Taptainlevel",uuidMap.get("taptainlevel"));
            dataMap.put("UserRef",uuidMap.get("userref"));
            dataMap.put("Code",uuidMap.get("code"));
            dataList.add(dataMap);
            infoMap.put("data",dataList);
            parameters.put("info",infoMap);
        }catch (Exception e) {
            throw new BusException("修改电话银行接口JSON数据异常");
        }
        HttpURLConnection con = null;
        BufferedReader reader = null;
        try {
            URL realUrl = new URL(url);
            con = (HttpURLConnection) realUrl.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type","application/json;charset=utf-8");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
            writer.print(parameters.toString());
            writer.close();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = reader.readLine();
            if(line !=null && !"".equals(line)){
                net.sf.json.JSONObject aa=net.sf.json.JSONObject.fromObject(line);
                net.sf.json.JSONObject head = aa.getJSONObject("head");
                String errorCodeI18n = head.get("errorCodeI18n").toString();
                reader.close();
                //0代表成功
                if("成功".equals(errorCodeI18n)){
                    return true;
                }else {
                    throw new BusException("修改失败："+head.getString("errorCodeI18n"));
                }
            }
        }catch (BusException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            con.disconnect();
        }
        return false;
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        TelTerminal telTerminal = telTerminalService.selectTelTerminalById(id);
        mmap.put("telTerminal", telTerminal);
        return prefix + "/showdetail";
    }

    /**
     * 删除
     */
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(telTerminalService.deleteTelTerminalByIds(ids));
    }

    @GetMapping("/addlist")
    public String addlist(ModelMap mmap) {
        return prefix + "/addlist";
    }

    //选择技能组
    @GetMapping("/selectApplication/{number}")
    public String selectApplication(@PathVariable("number") String number,ModelMap mmap) {
        mmap.put("pId",number);
        return prefix + "/selectApplication";
    }

}
