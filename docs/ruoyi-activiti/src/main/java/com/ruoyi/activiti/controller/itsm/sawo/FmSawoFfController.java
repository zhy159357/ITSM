package com.ruoyi.activiti.controller.itsm.sawo;

import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分发【态势感知工单】Controller
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Controller
@RequestMapping("sawo/dispense")
public class FmSawoFfController extends BaseController
{

    public static String FM_SAWO = "00128";

    private String prefix_dispense = "sawo/dispense";

    @Autowired
    private IFmSawoService fmSawoService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @GetMapping()
    public String sawo()
    {
        return prefix_dispense + "/sawo";
    }

    /**
     * 查询【态势感知工单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSawo fmSawo)
    {
        startPage();
        fmSawo.setOrdState("1");
        List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        return getDataTable(list);
    }

    /**
     * 分发【态势感知工单】
     */
    @GetMapping("/edit/{ordId}")
    public String edit(@PathVariable("ordId") String ordId, ModelMap mmap)
    {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        //获取当前登录人id
        OgUser ogUser = ShiroUtils.getOgUser();
        String pid = ogUser.getpId();
        mmap.put("pid",pid);
        mmap.put("nowTime",nowTime);
        mmap.put("fmSawo", fmSawo);
        return prefix_dispense + "/edit";
    }

    /**
     * 分发保存【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmSawo fmSawo)
    {
        if (StringUtils.isNotEmpty(fmSawo.getDealTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getDealTime());
            fmSawo.setDealTime(parseStart);
        }

        if (StringUtils.isNotEmpty(fmSawo.getCreaterTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getCreaterTime());
            fmSawo.setCreaterTime(parseStart);
        }

        //获取当前登录人id
        OgUser ogUser = ShiroUtils.getOgUser();
        String username = ogUser.getUsername();
        String pid = ogUser.getpId();
        fmSawo.setCreateId(pid);

        //流程相关代码
        Map<String, Object> reMap = new HashMap<>();
        //分发处理意见
        String comment = fmSawo.getRem();//审核意见
        reMap.put("processDefinitionKey","FMSAWO");
        reMap.put("businessKey",fmSawo.getOrdId());
        reMap.put("userId",ShiroUtils.getUserId());
        reMap.put("comment",comment);
        if(StringUtils.isNotEmpty(fmSawo.getOrdId())) {
            reMap.put("reCode","0");
            reMap.put("dealId",fmSawo.getDealId());
            try {
                activitiCommService.complete(reMap);
                fmSawoService.updateFmSawo(fmSawo);
                fmSawo.setOrdState("2");
                try{
                    //告警/漏洞/问题名称
                    String gaaName = fmSawo.getGaaName();
                    //处置时间
                    String dealTime = fmSawo.getDealTime();
                    //时
                    String s = dealTime.substring(8, 10);
                    //分
                    String f = dealTime.substring(10, 12);
                    //工单类型
                    String ordType = fmSawo.getOrdType();
                    if(ordType.startsWith("1")){
                        String smsText = "您在态势感知工单中，有漏洞工单需要处理，工单详情："+gaaName+"，请及时处理，发送时间：" + s+"时"+f+"分";//短信内容
                        OgPerson person = personService.selectOgPersonById(fmSawo.getDealId());// 获取短信接收人
                        sendSms(smsText,person);
                    }else if(ordType.startsWith("2")){
                        String smsText = "您在态势感知工单中，有告警工单需要处理，工单详情："+gaaName+"，请及时处理，发送时间：" + s+"时"+f+"分";//短信内容
                        OgPerson person = personService.selectOgPersonById(fmSawo.getDealId());// 获取短信接收人
                        sendSms(smsText,person);
                    }else if (ordType.startsWith("3")){
                        String smsText = "您在态势感知工单中，有问题工单需要处理，工单详情："+gaaName+"，请及时处理，发送时间：" + s+"时"+f+"分";//短信内容
                        OgPerson person = personService.selectOgPersonById(fmSawo.getDealId());// 获取短信接收人
                        sendSms(smsText,person);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    throw new BusinessException("短信发送失败");
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new BusinessException("请刷新页面进行重试");
            }
        }

        return AjaxResult.success("操作成功");
    }

    /**
     * 查询机构里的公司
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org)
    {
        startPage();
        List<OgOrg> list = deptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * 选择处置人
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String orgId, String pflag, String rId, ModelMap mmap) {
        ModelAndView model = new ModelAndView(prefix_dispense+"/selectPerson");
        model.addObject("orgId",orgId);
        return model;
    }



    /**
     * 获取当前登陆人的二级或者是一级机构
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.当前登陆用户的机构就是一级或者是二级机构
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            return ogOrg;
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            String twoLevelCode = split[2];
            return deptService.selectDeptByCode(twoLevelCode);
        }

    }

    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
                                 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix_dispense + "/tree";
    }

    /**
     * 加载所有部门列表树
     */
    @GetMapping("/selectAllTree")
    @ResponseBody
    public List<Ztree> selectAllTree() {
        // 加载机构树增加是否全国中心权限控制
        OgPerson person = personService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg o = deptService.selectDeptById(person.getOrgId());
        OgOrg org = new OgOrg();
        // 如果是第一级默认查询所有
        if("000000000".equals(o.getOrgCode())){
            org.setLevelCode("/000000000/");
        } else {
            // 使用层级码分割然后取出第二级机构码，使用like查询
            String levelCode = o.getLevelCode();
            String[] levelCodeArray = Convert.toStrArray("/", levelCode);
            if(levelCodeArray != null && levelCodeArray.length > 2){
                org.setLevelCode("/000000000/" + levelCodeArray[2]);
            }
        }
        return deptService.selectDeptTree(org);
    }

    /**
     * 加载部门列表树（排除下级）
     */
    @GetMapping("/treeData/{excludeId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) String excludeId) {
        OgOrg dept = new OgOrg();
        dept.setOrgId(excludeId);
        List<Ztree> ztrees = deptService.selectDeptTreeExcludeChild(dept);
        return ztrees;
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        /*List<Ztree> collect = null;
        List<Ztree> ztreesOne = null;
        List<Ztree> ztreesTwo = null;
        //当前用户的人员ID
        String pId = ShiroUtils.getUserId();
        //获取人员信息
        OgPerson ogPerson = personService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构还是二级机构
        OgOrg org = getOneLvOrTwoLv(ogOrg);
        if("1".equals(org.getOrgLv()) || ("2".equals(org.getOrgLv()) && "/000000000/010000888/".equals(org.getLevelCode()))){
            OgOrg orgOne = new OgOrg();
            orgOne.setLevelCode(org.getLevelCode());
            collect  =  deptService.selectDeptTree(orgOne);
        }else{
            //如果是省内查询当前省内及全国中心的
            String orgLevelCode = "/000000000/010000888/";
            OgOrg  orgTwo = new OgOrg();
            orgTwo.setLevelCode(orgLevelCode);
            collect = deptService.selectDeptTree(orgTwo);
            OgOrg  orgThree = new OgOrg();
            orgThree.setLevelCode(org.getLevelCode());
            ztreesTwo  =  deptService.selectDeptTree(orgThree);
            collect.addAll(ztreesTwo);
        }

        return collect;*/
        OgOrg orgOne = new OgOrg();
        List<Ztree> ztrees = deptService.selectDeptTree(orgOne);
        return ztrees;
    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(ShiroUtils.getUserId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

}
