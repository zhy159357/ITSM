package com.ruoyi.activiti.controller.itsm.sawo;

import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.activiti.service.impl.FmBizServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证【态势感知工单】Controller
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Controller
@RequestMapping("sawo/verify")
public class FmSawoYzController extends BaseController {
    private String prefix_verify = "sawo/verify";
    private String processDefinitionKey = "FMSAWO";
    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FmSawoYzController.class);

    private String USER_ID="59cf1fd945a2418cacb83fb6741771f0";

    @Autowired
    private IFmSawoService fmSawoService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private RestTemplate restTemplate;

//    @Value("${sawo.url}")
//    private String url;
//
//    @Value("${sawo.token}")
//    private String token;
//
//    @Value("${sawo.clientId}")
//    private String clientId;

    @GetMapping()
    public String sawo() {
        return prefix_verify + "/sawo";
    }

    /**
     * 查询【态势感知工单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSawo fmSawo) {
        startPage();
        fmSawo.setOrdState("3");
        OgUser ogUser = ShiroUtils.getOgUser();
        String username = ogUser.getUsername();
        if(!"admin".equals(username)){
            fmSawo.setCreateId(ogUser.getpId());
        }

        List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        return getDataTable(list);
    }

    /**
     * 验证【态势感知工单】
     */
    @GetMapping("/edit/{ordId}")
    public String edit(@PathVariable("ordId") String ordId, ModelMap mmap) {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        if (StringUtils.isNotEmpty(fmSawo.getN1())) {
            String orgId = fmSawo.getN1();
            //String org = orgId.substring(0,orgId.length() - 1);
            OgOrg ogOrg = deptService.selectDeptById(orgId);
            if (ogOrg != null) {
                List<OgPerson> list = personService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "666668");
                if (list != null) {
                    mmap.put("pidList", list);
                }
            }
        }
        mmap.put("fmSawo", fmSawo);
        return prefix_verify + "/edit";
    }

    /**
     * 验证通过保存【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @Transactional
    @PostMapping("/edit")
    public AjaxResult editSave(FmSawo fmSawo) {

        //我这边实现的方法
        String ordId = fmSawo.getOrdId();
        FmSawo fs = new FmSawo();
        fs.setOrdId(ordId);
        fs.setOrdState("4");

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", ordId);
        reMap.put("processDefinitionKey", processDefinitionKey);
        reMap.put("reCode", "0");
        try {
            activitiCommService.complete(reMap);
            fmSawoService.updateFmSawo(fs);
            fmSawo.setOrdState("4");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("请刷新页面进行重试");
        }
        try {
            fmSawoService.sendHttpClient(fmSawoService.selectFmSawoById(ordId));
        }catch (Exception e){
            throw new BusinessException("调用态势感知系统接口出现错误！");
        }

        return AjaxResult.success("操作成功");
    }

    @Autowired
    FmBizServiceImpl fmBizService;

    /*String date, String transferTime,
                                  String onelineName, String sysName, String no,
                                  String ifRogger, String roggerTime, String fixPerson,*/

    /**
     * 验证退回保存【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @PostMapping("/verify")
    @ResponseBody
    public AjaxResult verify(FmSawo fmSawo) {
        if (StringUtils.isNotEmpty(fmSawo.getDealTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getDealTime());
            fmSawo.setDealTime(parseStart);
        }

        if (StringUtils.isNotEmpty(fmSawo.getCreaterTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getCreaterTime());
            fmSawo.setCreaterTime(parseStart);
        }
        //流程相关代码
        Map<String, Object> reMap = new HashMap<>();
        if (StringUtils.isNotEmpty(fmSawo.getOrdId())) {
            reMap.put("reCode", "1");
            reMap.put("dealId", fmSawo.getDealId());
            reMap.put("processDefinitionKey", "FMSAWO");
            reMap.put("businessKey", fmSawo.getOrdId());
            reMap.put("userId", ShiroUtils.getUserId());
            try {
                activitiCommService.complete(reMap);
                fmSawoService.updateFmSawo(fmSawo);
                fmSawo.setOrdState("3");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("请刷新页面进行重试");
            }
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 转发对应子页面
     *
     * @return
     */
    @GetMapping("/saverepeatIn/{ordId}")
    public String saverepeatIn(@PathVariable("ordId") String ordId, ModelMap mmap) {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        /*OgUser u = ShiroUtils.getOgUser();//获取当前登陆人
        if (!u.getpId().equals(fmSawo.getDealId())) {
            throw new BusinessException("该工单处置人已更改，请刷新后检查列表重新操作。");
        }*/
        mmap.put("fmSawo", fmSawo);
        return prefix_verify + "/repeatIn";
    }

    /**
     * 查询机构里的公司
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org) {
        startPage();
        List<OgOrg> list = deptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * 选择处置人
     *
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String orgId, String pflag, String rId, ModelMap mmap) {
        ModelAndView model = new ModelAndView(prefix_verify + "/selectPerson");
        model.addObject("orgId", orgId);
        return model;
    }

}
