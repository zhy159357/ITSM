package com.ruoyi.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.WebService.WebServerForADPM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统应用管理
 *
 * @author zc
 */
@Controller
@RequestMapping("/system/application")
@Transactional(rollbackFor = Exception.class)
public class SysApplicationManagerController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SysApplicationManagerController.class);


    private String prefix = "system/application";

    @Autowired
    private ISysAppService appService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private OgSysLogService ogSysLogService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    ISysApplicationManagerService sysApplicationManagerService;

    /**cmdb地址*/
    @Value("${cmdb.url}")
    private String cmdb;

    /**cmdb apikey*/
    @Value("${cmdb.apikey}")
    private String apikey;

    @Value("${cmdb.pageNum}")
    private String pageNum;

    @Value("${cmdb.pageSize}")
    private String pageSize;

    @Value("${cmdb.needCount}")
    private String needCount;

    @Value("${cmdb.Field}")
    private String Field;

    @Value("${cmdb.Value}")
    private String Value;

    @Value("${cmdb.ParaId}")
    private String ParaId;

    @Value("${cmdb.valueDetail}")
    private String valueDetail;
    
    @Value("${foreign.adpm.syncDate}")
    private String valueDate;
    /**admp*/
    @Value("${foreign.adpm.sysUrl}")
    private String adpmSysUrl;

    @GetMapping()
    public String application() {
        return prefix + "/application";
    }

    @PostMapping("/listAll")
    @ResponseBody
    public TableDataInfo listAll(OgSys sys) {
        // 查询外围系统时需要全部展示有多选isPage为2不分页
        if (!"2".equals(sys.getIsPage())) {
            startPage();
        }
        sys.setOrgId(resetOrgId(sys.getOrgId(), sys.getOrgName()));
        List<OgSys> list = applicationManagerService.selectOgSysList(sys);
        return getDataTable(list);
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgSys sys) {
        // 查询外围系统时需要全部展示有多选isPage为2不分页
        if (!"2".equals(sys.getIsPage())) {
            // 只有应用系统管理列表查询时才需要控制权限
            if("applicationList".equals(sys.getRemark())){
                OgPerson person = personService.selectOgPersonById(ShiroUtils.getUserId());
                OgOrg org = deptService.selectDeptById(person.getOrgId());
                // 判断当前登录人是否全国中心，是全国中心默认查询所有，不是全国中心使用层级码like查询
//                if (!("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/"))) {
//                    OgOrg ogOrg = new OgOrg();
//                    ogOrg.setLevelCode(org.getLevelCode());
//                    sys.setOgOrg(ogOrg);
//                }
            }
            startPage();
        }
        sys.setOrgId(resetOrgId(sys.getOrgId(), sys.getOrgName()));
        List<OgSys> list = applicationManagerService.selectOgSysList(sys);
        return getDataTable(list);
    }

    /**
     * 新增应用系统管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存应用系统管理
     */
    @Log(title = "新增应用系统管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgSys sys) {
        //查出所有应用系统
        OgSys ogSys = new OgSys();
        List<OgSys> list = applicationManagerService.selectOgSysList(ogSys);
        if(list!=null){
            //遍历
            for(OgSys ogsy : list){
                if(StringUtils.isNotEmpty(ogsy.getCode())){
                    if(ogsy.getCode().equals(sys.getCode())){
                        return AjaxResult.error("系统编码已存在，请重新输入");
                    }
                }
            }
        }
        sys.setAddTime(DateUtils.dateTimeNow());//创建时间
        String userId = ShiroUtils.getUserId();
        //根据userId查询出创建人名称
        OgPerson person = personService.selectOgPersonById(userId);
        if(person != null){
            sys.setAdder(person.getpName());//创建人
        }
        return toAjax(applicationManagerService.insertOgSys(sys));

    }

    /**
     * 修改应用系统
     */
    @GetMapping("/edit/{sysId}")
    public String edit(@PathVariable("sysId") String sysId, ModelMap mmap) {
        mmap.put("application", applicationManagerService.selectOgSysBySysId(sysId));
        return prefix + "/edit";
    }

    /**
     * 修改保存系统管理
     */
    @Log(title = "系统管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgSys sys) {
        //根据传来的数据查出信息
        OgSys ogSys = applicationManagerService.selectOgSysBySysCode(sys.getCode());
        if(ogSys!=null){
            if(!sys.getSysId().equals(ogSys.getSysId())){
                return AjaxResult.error("系统编码已存在，请重新输入");

            }
        }
        sys.setUpdateTime(DateUtils.dateTimeNow());//修改时间
        String userId = ShiroUtils.getUserId();
        //根据userId查询出创建人名称
        OgPerson person = personService.selectOgPersonById(userId);
        if(person != null){
            sys.setModer(person.getpName());//修改人
        }
        return toAjax(applicationManagerService.updateOgSys(sys));
    }


    @Log(title = "系统管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(applicationManagerService.deleteOgSysByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 系统状态修改
     */
    @Log(title = "系统管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysApp app) {
        appService.checkAppAllowed(app);
        return toAjax(appService.changeStatus(app));
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

    /**
     * 根据所属处室查询系统负责人
     *
     * @param orgId
     * @return
     */
    @PostMapping("/selectOgPersonForOrgIdList/{orgId}")
    @ResponseBody
    public TableDataInfo selectOgPersonForOrgIdList(@PathVariable("orgId") String orgId) {
        List<OgPerson> list = applicationManagerService.selectOgPersonForOrgIdList(orgId);
        return getDataTable(list);
    }


    /**
     * 同步应用系统数据 调用外部ADMP系统webService接口
     */
    @RequestMapping("/tongbu")
    @ResponseBody
    public AjaxResult tongbu() {

        AjaxResult ajaxResult = new AjaxResult();
        WebServerForADPM adpm = new WebServerForADPM();

        String methidName = "selectSysinfoToXml";//方法名
        Object[] params = {valueDate};//参数
        Object object = adpm.invokeWebService(adpmSysUrl, methidName, params);//访问链接获取Object

        Map<String, Object> map = adpm.realResultXMLForSys(object);//处理返回数据

        ajaxResult.put("num",map.get("num"));
        ajaxResult.put("msg",map.get("msg"));

        return ajaxResult;
    }

    @PostMapping("/selectOgSysMaps")
    @ResponseBody
    public AjaxResult selectOgSysMaps() {
        OgSys ogSys = new OgSys();
        List<OgSys> sysList = applicationManagerService.selectOgSysList(ogSys);
        List<Map<String, String>> resultList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(sysList)) {
            sysList.forEach(sys -> {
                if(StringUtils.isNotEmpty(sys.getCode()) && StringUtils.isNotEmpty(sys.getCaption())) {
                    Map<String, String> map = new HashMap<>();
                    map.put("value", sys.getCode());
                    map.put("label", sys.getCaption());
                    resultList.add(map);
                }
            });
        }
        return AjaxResult.success(resultList);
    }

    @PostMapping("/sys/all")
    @ResponseBody
    public AjaxResult getAllSys() {
        List<OgSys> list = sysApplicationManagerService.selectOgSysList(new OgSys());
        List<Map<String, String>> result = new ArrayList<>();
        list.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            if (ObjectUtil.isNotEmpty(p.getCaption())) {
                map.put("label", p.getCaption() + "(" + p.getCode() + ")");
                map.put("value", p.getCode());
                result.add(map);
            }
        });
        return AjaxResult.success(result);
    }

}

