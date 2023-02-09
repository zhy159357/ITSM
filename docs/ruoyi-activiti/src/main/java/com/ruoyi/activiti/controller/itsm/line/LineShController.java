package com.ruoyi.activiti.controller.itsm.line;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.service.LineService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
*
* 系统线条故障（审核）
* */
@Controller
@RequestMapping("line/sh")
public class LineShController extends BaseController
{
    //查询界面路径
    private String prefix_sh = "line/sh";

    @Autowired
    LineService lineService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    ISysDeptService iSysDeptService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_sh + "/line";
    }

    //查询
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgLine ogLine)
    {
        //获取待审核状态
        ogLine.setLineStart("3");
        //提起的方法名称
         ogLine=timeTqTwo(ogLine);
        //分页
        startPage();
        //获取当前登录人id
        String pId = ShiroUtils.getOgUser().getpId();
        //审核页面查询
        ogLine.setpIdTwo(pId);
        List<OgLine> list = lineService.selectLineList(ogLine);
        return getDataTable(list);
    }

    //选取系统的路径
    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix_sh + "/selectOneApplication";
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
     * 去线条故障审核页面
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
        return prefix_sh + "/edit";
    }

    /**
     * 保存线条故障审核
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
        return prefix_sh + "/tree";
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
    public OgLine timeTqTwo(OgLine ogLine){
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
        return ogLine;
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
