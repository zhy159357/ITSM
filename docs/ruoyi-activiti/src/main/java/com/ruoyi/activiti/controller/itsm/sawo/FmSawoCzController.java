package com.ruoyi.activiti.controller.itsm.sawo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 处置【态势感知工单】Controller
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Controller
@RequestMapping("sawo/dispose")
public class FmSawoCzController extends BaseController
{
    private String prefix_dispose = "sawo/dispose";

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

    @Value("${tsgz.userId}")
    private String USER_ID;

    private String prefix_dapt = "system/dept";

    @GetMapping()
    public String sawo()
    {
        return prefix_dispose + "/sawo";
    }

    /**
     * 查询【态势感知工单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSawo fmSawo)
    {
        startPage();
        fmSawo.setOrdState("2");
        //String dealId = fmSawo.getDealId();
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userName = ogUser.getUsername();
        if (!"admin".equals(userName)) {
            fmSawo.setDealId(ogUser.getpId());
        }
        List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        return getDataTable(list);
    }

    /**
     * 处置【态势感知工单】
     */
    @GetMapping("/edit/{ordId}")
    public String edit(@PathVariable("ordId") String ordId, ModelMap mmap)
    {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        String creatTwoId = fmSawo.getCreateId();
        mmap.put("creatTwoId",creatTwoId);
        mmap.put("fmSawo", fmSawo);
        return prefix_dispose + "/edit";
    }

    /**
     * 处置保存【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmSawo fmSawo)
    {
        if (StringUtils.isNotEmpty(fmSawo.getCreaterTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getCreaterTime());
            fmSawo.setCreaterTime(parseStart);
        }
        if (StringUtils.isNotEmpty(fmSawo.getGaaTypeValue())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getGaaTypeValue());
            fmSawo.setGaaTypeValue(parseStart);
        }
        if (StringUtils.isNotEmpty(fmSawo.getDealTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getDealTime());
            fmSawo.setDealTime(parseStart);
        }
        //处置超时的判断
        try {
            //处置完成时间
            String dealTime = fmSawo.getDealTime();
            //处置成功时间
            String gaaTypeValue = fmSawo.getGaaTypeValue();
            long a = Long.parseLong(dealTime);
            long b = Long.parseLong(gaaTypeValue);
            if(a<b){
                //超时的时候显示超时时间
                //先把long类型转换成date类型
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date oneTime = simpleDateFormat.parse(dealTime);
                Date twoTime = simpleDateFormat.parse(gaaTypeValue);
                //在调用Dateutils里面的一个方法 计算时间差
                String datePoor = DateUtils.getDatePoor(twoTime, oneTime);
                //然后set值就可以
                fmSawo.setN3(datePoor);
            }else {
                fmSawo.setN3("未超时");
            }

        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("处置时间耗时计算出错");
        }
        //流程相关代码
        Map<String, Object> reMap = new HashMap<>();
        String processDefinitionKey="FMSAWO";
        String comment = fmSawo.getRem();//审核意见
        reMap.put("userId",ShiroUtils.getUserId());
        reMap.put("businessKey", fmSawo.getOrdId());
        reMap.put("processDefinitionKey",processDefinitionKey);
        reMap.put("comment",comment);
        if(StringUtils.isNotEmpty(fmSawo.getOrdId())) {
            reMap.put("reCode","0");
            reMap.put("closeId",fmSawo.getCreateId());   //分发时获取的创建人id
            try {
                activitiCommService.complete(reMap);
                fmSawoService.updateFmSawo(fmSawo);
                fmSawo.setOrdState("3");
            }catch (Exception e){
                e.printStackTrace();
                throw new BusinessException("请刷新页面进行重试");
            }
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 转发【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @PostMapping("/editZf")
    @ResponseBody
    public AjaxResult editZfSave(FmSawo fmSawo)
    {
        if (StringUtils.isNotEmpty(fmSawo.getDealTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getDealTime());
            fmSawo.setDealTime(parseStart);
        }

        if (StringUtils.isNotEmpty(fmSawo.getCreaterTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getCreaterTime());
            fmSawo.setCreaterTime(parseStart);
        }
        //流程相关代码
        String processDefinitionKey="FMSAWO";
        String comment = fmSawo.getRem();//审核意见
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId",ShiroUtils.getUserId());
        reMap.put("businessKey", fmSawo.getOrdId());
        reMap.put("comment",comment);
        if(StringUtils.isNotEmpty(fmSawo.getOrdId())) {
            reMap.put("reCode","1");
            reMap.put("createId", ShiroUtils.getUserId());
            reMap.put("dealId",fmSawo.getDealId());   //处置人id
            reMap.put("processDefinitionKey",processDefinitionKey);
            try {
                activitiCommService.complete(reMap);
                fmSawoService.updateFmSawo(fmSawo);
                fmSawo.setOrdState("3");
            }catch (Exception e){
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
        OgPerson ogPerson = new OgPerson();
        ogPerson.setOrgId(fmSawo.getN1());
        List<OgPerson> personList = personService.selectList(ogPerson);
        mmap.put("personList",personList);
        mmap.put("fmSawo",fmSawo);
        return prefix_dispose + "/repeatIn";
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
        ModelAndView model = new ModelAndView(prefix_dispose+"/selectPerson");
        model.addObject("orgId",orgId);
        return model;
    }

    /**
     * 选择部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
                                 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix_dispose + "/tree";
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
        OgOrg orgOne = new OgOrg();
        List<Ztree> ztrees = deptService.selectDeptTree(orgOne);
        return ztrees;
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
}
