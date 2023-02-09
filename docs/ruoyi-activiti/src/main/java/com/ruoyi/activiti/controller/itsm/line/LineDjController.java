package com.ruoyi.activiti.controller.itsm.line;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.LineService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.FmDd;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
*
* 系统线条故障（登记）
* */
@Controller
@RequestMapping("line/dj")
public class LineDjController extends BaseController
{
    //查询界面路径
    private String prefix_dj = "line/dj";

    @Autowired
    LineService lineService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    ISysDeptService iSysDeptService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_dj + "/line";
    }

    //查询
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgLine ogLine)
    {
        //提起的方法名称
        timeTqTwo(ogLine);
        //分页
        startPage();
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userName = ogUser.getUsername();
        if (!"admin".equals(userName)){
            ogLine.setpId(ogUser.getpId());
        }
        List<OgLine> list = lineService.selectLineList(ogLine);
        return getDataTable(list);
    }

    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix_dj + "/selectOneApplication";
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
     * 新建线条故障
     */
    @GetMapping("/add")
    public String add(ModelMap mmap,OgLine ogLine)
    {
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime",nowTime);

        String companies = ogLine.getCompanies();
        OgOrg ogOrg = deptService.selectDeptById(companies);
        if(ogOrg!=null){
            String orgName = ogOrg.getOrgName();
            if(StringUtils.isNotEmpty(orgName)){
                ogLine.setCompanies(orgName);
            }
        }

        String manufacturer = ogLine.getManufacturer();
        OgOrg ogOrg1 = deptService.selectDeptById(manufacturer);
        if(ogOrg1!=null){
            String orgName1 = ogOrg1.getOrgName();
            if(StringUtils.isNotEmpty(orgName1)){
                ogLine.setManufacturer(orgName1);
            }
        }

        mmap.put("ogLine",ogLine);
        return prefix_dj + "/add";
    }

    /*
     * 保存线条故障
     * */
    @Log(title = "新建线条故障", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgLine ogLine)
    {
        //提取的方法名称
        timeTqOne(ogLine);
        //UUID
        ogLine.setLineId(UUID.getUUIDStr());
        return toAjax(lineService.insertOgLine(ogLine));
    }

    /**
     * 选择审批部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectCheckDeptTree/{deptId}", "/selectCheckDeptTree/{deptId}/{excludeId}"})
    public String selectCheckDeptTree(@PathVariable("deptId") String deptId,
                                      @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix_dj + "/checkOrgTree";
    }

    /**
     * 加载审批部门列表树
     */
    @GetMapping("/treeDataCheck")
    @ResponseBody
    public List<Ztree> treeDataCheck() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        list.addAll(deptService.selectDeptList(org));
        List<Ztree> ztrees = initZtree(list);
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

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList) {
        return initZtree(deptList, null);
    }

    private final String SUCCESS = "1";// 启用状态

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (OgOrg dept : deptList) {
            if (SUCCESS.equals(dept.getInvalidationMark())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getOrgId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getOrgName());
                ztree.setTitle(dept.getOrgName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getOrgId() + dept.getOrgName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 选择审批人   可以看请求变更单
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String orgId, String pflag, String rId, ModelMap mmap) {
        ModelAndView model = new ModelAndView(prefix_dj+"/selectPerson");
        model.addObject("orgId",orgId);
        return model;
    }

    /**
     * 修改线条故障
     */
    @GetMapping("/edit/{lineId}")
    public String edit(@PathVariable("lineId") String lineId, ModelMap mmap)
    {
        OgLine line = lineService.selectByLindId(lineId);

        String companies = line.getCompanies();
        OgOrg ogOrg = deptService.selectDeptById(companies);
        if(ogOrg!=null){
            String orgName = ogOrg.getOrgName();
            if(StringUtils.isNotEmpty(orgName)){
                line.setCompanies(orgName);
            }
        }

        String manufacturer = line.getManufacturer();
        OgOrg ogOrg1 = deptService.selectDeptById(manufacturer);
        if(ogOrg1!=null){
            String orgName1 = ogOrg1.getOrgName();
            if(StringUtils.isNotEmpty(orgName1)){
                line.setManufacturer(orgName1);
            }
        }

        if (StringUtils.isNotEmpty(line.getOrgId())) {
            OgOrg ogOrg2 = iSysDeptService.selectDeptById(line.getOrgId());//回显审批人
            if(ogOrg2!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg2.getOrgId(), "666666");
                if(list!=null){
                    mmap.put("pidTwoList", list);
                }
            }

        }

        mmap.put("line",line);
        return prefix_dj + "/edit";
    }

    /**
     * 修改保存事件单
     */
    @Log(title = "线条故障", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgLine ogLine)
    {
        //提取的方法名称
        timeTqOne(ogLine);
        return toAjax(lineService.updateOgLine(ogLine));
    }

    /**
     * 删除/作废
     */
    @Log(title = "线条故障", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(lineService.deleteByLindId(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String lineId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        OgLine line = lineService.selectByLindId(lineId);
        ajaxResult.put("data",line);
        return  ajaxResult;
    }

    //查看详情
    @GetMapping("/detail/{lineId}")
    public String detail(@PathVariable("lineId") String lineId, ModelMap mmap)
    {
        OgLine line = lineService.selectByLindId(lineId);
        if (StringUtils.isNotEmpty(line.getOrgId())) {
            OgOrg ogOrg2 = iSysDeptService.selectDeptById(line.getOrgId());//回显审批人
            if(ogOrg2!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg2.getOrgId(), "666666");
                if(list!=null){
                    mmap.put("pidTwoList", list);
                }
            }

        }
        mmap.put("line",lineService.selectByLindId(lineId));
        return prefix_dj + "/detail";
    }

    //导出
    @Log(title = "系统线条故障", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgLine ogLine)
    {
        /*String isCurrentPage = (String) ogLine.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<OgLine> list = lineService.selectLineList(ogLine);
            for(OgLine line:list){
                if(StringUtils.isNotEmpty(line.getSysId())){
                    line.setSysId(line.getOgSys().getCaption());
                }
            }
            ExcelUtil<OgLine> util = new ExcelUtil<OgLine>(OgLine.class);
            return util.exportExcel(list, "系统条线故障");
        } else if ("all".equals(isCurrentPage)) {
            List<OgLine> list = lineService.selectLineList(ogLine);
            for(OgLine line:list){
                if(StringUtils.isNotEmpty(line.getSysId())){
                    line.setSysId(line.getOgSys().getCaption());
                }
            }
            ExcelUtil<OgLine> util = new ExcelUtil<OgLine>(OgLine.class);
            return util.exportExcel(list, "系统条线故障");
        }
        return null;*/
        String isCurrentPage = (String)ogLine.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            startPage();
            List<OgLine> list = lineService.selectLineList(ogLine);
            ExcelUtil<OgLine> util = new ExcelUtil<OgLine>(OgLine.class);
            return util.exportExcel(list, "系统条线故障");
        }else {
            List<OgLine> list = lineService.selectLineList(ogLine);
            ExcelUtil<OgLine> util = new ExcelUtil<OgLine>(OgLine.class);
            return util.exportExcel(list, "系统条线故障");
        }
    }

    //响应请求分页数据
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    //设置请求分页数据
    @Override
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 公司部门和是否第三方维保厂商选择部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
                                 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix_dj + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = deptService.selectDeptListByDJ();
        return ztrees;
    }

    //提取的方法
    public void timeTqTwo(OgLine ogLine){
        if(StringUtils.isNotEmpty(ogLine.getStartTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(ogLine.getStartTime());
            ogLine.setStartTime(parseStart);
        }
        if(StringUtils.isNotEmpty(ogLine.getPresentTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(ogLine.getPresentTime());
            ogLine.setPresentTime(parseStart);
        }
        if(StringUtils.isNotEmpty(ogLine.getEndTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(ogLine.getEndTime());
            ogLine.setEndTime(parseStart);
        }
    }

    //提取的方法
    public void timeTqOne(OgLine ogLine){
        if(StringUtils.isNotEmpty(ogLine.getStartTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(ogLine.getStartTime());
            ogLine.setStartTime(parseStart);
        }
        if(StringUtils.isNotEmpty(ogLine.getPresentTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(ogLine.getPresentTime());
            ogLine.setPresentTime(parseStart);
        }
        if(StringUtils.isNotEmpty(ogLine.getEndTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(ogLine.getEndTime());
            ogLine.setEndTime(parseStart);
        }
    }

}
