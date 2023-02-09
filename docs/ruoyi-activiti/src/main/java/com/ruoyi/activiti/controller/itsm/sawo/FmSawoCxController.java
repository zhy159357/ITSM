package com.ruoyi.activiti.controller.itsm.sawo;

import com.github.pagehelper.util.StringUtil;
import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询【态势感知工单】Controller
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Controller
@RequestMapping("sawo/query")
public class FmSawoCxController extends BaseController
{

    private String processDefinitionKey = "FMSAWO";
    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FmSawoYzController.class);

    private String USER_ID="59cf1fd945a2418cacb83fb6741771f0";

    private String prefix_query = "sawo/query";

    @Autowired
    private IFmSawoService fmSawoService;


    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private ActivitiCommService activitiCommService;

    private String prefix_dapt = "system/dept";

    @GetMapping()
    public String sawo()
    {
        return prefix_query + "/sawo";
    }

    /**
     * 查询【态势感知工单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSawo fmSawo)
    {
        startPage();
        List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        return getDataTable(list);
    }

    /**
     * 查看详情页面【态势感知工单】
     */
    @GetMapping("/edit/{ordId}")
    public String edit(@PathVariable("ordId") String ordId, ModelMap mmap)
    {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        mmap.put("fmSawo", fmSawo);
        return prefix_query + "/edit";
    }

    /**
     * 导出【态势感知工单】列表
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmSawo fmSawo)
    {
        /*List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        ExcelUtil<FmSawo> util = new ExcelUtil<FmSawo>(FmSawo.class);
        return util.exportExcel(list, "态势感知工单");*/
        String isCurrentPage = (String) fmSawo.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
            ExcelUtil<FmSawo> util = new ExcelUtil<FmSawo>(FmSawo.class);
            return util.exportExcel(list, "态势感知工单");
        } else if ("all".equals(isCurrentPage)) {
            List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
            ExcelUtil<FmSawo> util = new ExcelUtil<FmSawo>(FmSawo.class);
            return util.exportExcel(list, "态势感知工单");
        }
        return null;
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String ordId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        ajaxResult.put("data",fmSawo);
        return ajaxResult;
    }

    //去重新发送页面【态势感知工单】列表
    @GetMapping("/retry/{ordId}")
    public String retry(@PathVariable("ordId") String ordId, ModelMap mmap)
    {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        mmap.put("fmSawo", fmSawo);
        return prefix_query + "/retry";
    }

    /*@Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @Transactional
    @PostMapping("/retry")
    public AjaxResult retrySave(FmSawo fmSawo,String orderStatus,String dealWithName,String orderId,
                                String dealWithOrganization,String disposalTime,String remark)
    {
        if (StringUtils.isNotEmpty(fmSawo.getDealTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmSawo.getDealTime());
            fmSawo.setDealTime(parseStart);
        }

        String ordId = fmSawo.getOrdId();
        FmSawo fs = new FmSawo();
        fs.setOrdId(ordId);
        fs.setOrdState("4");

        fmSawoService.sendHttpClient(fmSawoService.selectFmSawoById(ordId));

        return AjaxResult.success("操作成功");
    }*/

    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @Transactional
    @PostMapping("/retry")
    public AjaxResult retrySave(FmSawo fmSawo)
    {
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

    /**
     * 流程撤回
     *
     * @param ordId
     * @return
     */
    @PostMapping("/rollBack")
    @ResponseBody
    @Transactional
    public AjaxResult rollBack(String ordId) {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        if (fmSawo != null && "2".equals(fmSawo.getOrdState())) {
            String userId = ShiroUtils.getUserId();
            try {
                //activitiCommService.revokeCreateTask(ordId, userId, "FMSAWO");
                activitiCommService.revokeTask(ordId, userId, "FMSAWO");
                fmSawo.setOrdState("1");
                fmSawoService.updateFmSawo(fmSawo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return error("态势感知工单不存在或者流程已被处置。");
        }
        return success("态势感知工单撤回成功。");
    }

    /**
     * 查看流程【态势感知工单】列表
     */
    @GetMapping("/flow/{ordId}")
    public String flow(@PathVariable("ordId") String ordId, ModelMap mmap)
    {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        if(fmSawo != null){
            mmap.put("fmSawo", fmSawo);
        }
        return prefix_query + "/edit";
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
        mmap.put("fmSawo",fmSawo);
        return prefix_query + "/repeatIn";
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
        ModelAndView model = new ModelAndView(prefix_query+"/selectPerson");
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
        /*OgUser user = ShiroUtils.getOgUser();//获取当前登陆人
        String isCenter = deptService.getIsCenter();
        //进去就是全国中心
        if("1".equals(isCenter)){
            mmap.put("dept", deptService.selectDeptById(deptId));
        }else{
            String getpId = user.getpId();
            OgPerson ogPerson = personService.selectOgPersonById(getpId);
            String orgId = ogPerson.getOrgId();
            List<OgOrg> list = deptService.selectAllChildOrg(orgId);
            for(OgOrg ogOrg : list){
                //mmap.put()
            }
        }
*/
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix_dapt + "/tree";
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
        List<Ztree> collect = null;
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

        return collect;
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
