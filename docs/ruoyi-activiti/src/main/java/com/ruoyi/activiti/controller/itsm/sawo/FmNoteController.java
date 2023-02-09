package com.ruoyi.activiti.controller.itsm.sawo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.ruoyi.activiti.domain.FmNote;
import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.service.IFmNoteService;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.servlet.ModelAndView;

/**
 * 【通知工单】Controller
 * 
 * @author ruoyi
 * @date 2021-11-29
 */
@Controller
@RequestMapping("sawo/noteNewly")
public class FmNoteController extends BaseController
{
    private String prefix = "sawo/noteNewly";

    @Autowired
    private IFmNoteService fmNoteService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @GetMapping()
    public String note()
    {
        return prefix + "/note";
    }

    /**
     * 查询【通知工单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmNote fmNote)
    {
        startPage();
        List<FmNote> list = fmNoteService.selectFmNoteList(fmNote);
        return getDataTable(list);
    }

    /**
     * 新增【通知工单】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {

        //公用的一个生成单号
        String bizType = "TZGD"; //YJSJ   BB
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime", nowTime);

        mmap.put("noteId", UUID.getUUIDStr());

        return prefix + "/add";
    }

    /**
     * 新增保存【通知工单】
     */
    @Log(title = "【通知工单】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmNote fmNote)
    {

        if (StringUtils.isNotEmpty(fmNote.getStartTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmNote.getStartTime());
            fmNote.setStartTime(parseStart);
        }
        if (StringUtils.isNotEmpty(fmNote.getFeedbackAbortTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmNote.getFeedbackAbortTime());
            fmNote.setFeedbackAbortTime(parseStart);
        }

        //根据机构生成执行机构id
        String masterOrgId=fmNote.getParams().get("masterOrgId")==null?"":fmNote.getParams().get("masterOrgId").toString();
        String[] masterOrgIds=masterOrgId.split(",");

        String orgId = fmNote.getParams().get("masterOrgId").toString();
        String orgName = fmNote.getParams().get("masterOrgIdName").toString();
        //根据机构生成
        fmNote.setOrgid(orgId);
        fmNote.setN1(orgName);

        return toAjax(fmNoteService.insertFmNote(fmNote));
    }

    /**
     * 接手【通知工单】
     */
    @PostMapping("/sj/{noteId}")
    @ResponseBody
    public AjaxResult sj(@PathVariable("noteId") String noteId)
    {
        FmNote fmNote = fmNoteService.selectFmNoteById(noteId);

        FmNote note = new FmNote();
        note.setNoteId(noteId);
        note.setAcceptingState("2");

        return toAjax(fmNoteService.updateFmNote(note));
    }

    /**
     * 反馈【通知工单】
     */
    @GetMapping("/edit/{noteId}")
    public String edit(@PathVariable("noteId") String noteId, ModelMap mmap)
    {
        FmNote fmNote = fmNoteService.selectFmNoteById(noteId);
        String feedbackAbortTime = fmNote.getFeedbackAbortTime();
        mmap.put("feedbackAbortTime",feedbackAbortTime);
        mmap.put("fmNote", fmNote);
        //添加反馈时间
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime", nowTime);

        return prefix + "/edit";
    }

    /**
     * 反馈保存【通知工单】
     */
    @Log(title = "【通知工单】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmNote fmNote)
    {

        if (StringUtils.isNotEmpty(fmNote.getFeedbackTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(fmNote.getFeedbackTime());
            fmNote.setFeedbackTime(parseStart);
        }

        //超时的判断
        try {
            //反馈截止时间
            String feedbackAbortTime = fmNote.getFeedbackAbortTime();
            //反馈时间
            String feedbackTime = fmNote.getFeedbackTime();
            long a = Long.parseLong(feedbackAbortTime);
            long b = Long.parseLong(feedbackTime);
            if(a<b){
                //超时的时候显示超时时间
                //先把long类型转换成date类型
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date oneTime = simpleDateFormat.parse(feedbackAbortTime);
                Date twoTime = simpleDateFormat.parse(feedbackTime);
                //在调用Dateutils里面的一个方法 计算时间差
                String datePoor = DateUtils.getDatePoor(twoTime, oneTime);
                //然后set值就可以
                fmNote.setDisposeTime(datePoor);
            }else {
                fmNote.setDisposeTime("未超时");
            }

        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("反馈时间耗时计算出错");
        }

        return toAjax(fmNoteService.updateFmNote(fmNote));
    }

    /**
     * 删除【通知工单】
     */
    @Log(title = "【通知工单】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(fmNoteService.deleteFmNoteByIds(ids));
    }

    /**
     * 导出【通知工单】列表
     */
    @Log(title = "【通知工单】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmNote fmNote)
    {
        List<FmNote> list = fmNoteService.selectFmNoteList(fmNote);
        ExcelUtil<FmNote> util = new ExcelUtil<FmNote>(FmNote.class);
        return util.exportExcel(list, "note");
    }





















    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String noteId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        FmNote fmNote = fmNoteService.selectFmNoteById(noteId);
        ajaxResult.put("data",fmNote);
        return ajaxResult;
    }

    /**
     * 选择处置人
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String orgId, String pflag, String rId, ModelMap mmap) {
        ModelAndView model = new ModelAndView(prefix+"/selectPerson");
        model.addObject("orgId",orgId);
        return model;
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
        return prefix + "/tree";
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

}
