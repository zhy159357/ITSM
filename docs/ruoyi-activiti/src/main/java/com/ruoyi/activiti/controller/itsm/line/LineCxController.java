package com.ruoyi.activiti.controller.itsm.line;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.controller.itsm.sawo.FmSawoController;
import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.service.LineService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/*
 *
 * 系统条线故障（查询）
 * */
@Controller
@RequestMapping("line/cx")
public class LineCxController extends BaseController
{

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LineCxController.class);

    //查询界面路径
    private String prefix_cx = "line/cx";

    @Autowired
    LineService lineService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_cx + "/line";
    }

    //查询
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgLine ogLine)
    {

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
        return prefix_cx + "/detail";
    }

    //导出
    @Log(title = "系统线条故障", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgLine ogLine)
    {
        String isCurrentPage = (String) ogLine.getParams().get("currentPage");
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
            /*ogLine = new OgLine();*/
            List<OgLine> list = lineService.selectLineList(ogLine);
            for(OgLine line:list){
                if(StringUtils.isNotEmpty(line.getSysId())){
                    line.setSysId(line.getOgSys().getCaption());
                }
            }
            ExcelUtil<OgLine> util = new ExcelUtil<OgLine>(OgLine.class);
            return util.exportExcel(list, "系统条线故障");
        }
        /*List<OgLine> list = lineService.selectLineList(ogLine);
        for(OgLine line:list){
            if(StringUtils.isNotEmpty(line.getSysId())){
                line.setSysId(line.getOgSys().getCaption());
            }
        }
        ExcelUtil<OgLine> util = new ExcelUtil<OgLine>(OgLine.class);
        return util.exportExcel(list, "系统条线故障");*/
        return null;
    }

    /*
     *
     * 下载导入模板
     * */
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil <OgLine> util = new ExcelUtil <OgLine> ( OgLine.class );
        return util.importTemplateExcel ( "系统条线故障导入模板" );
    }


    @PostMapping ( "/importData" )
    @ResponseBody
    public AjaxResult importData (MultipartFile file , boolean updateSupport ) throws Exception {
        ExcelUtil <OgLine> util = new ExcelUtil <OgLine> ( OgLine.class );
        List <OgLine> list = util.importExcel ( file.getInputStream ( ) );
        String message = importTSylx ( list, updateSupport );
        return AjaxResult.success ( message );
    }

    public String importTSylx ( List <OgLine> list , Boolean isUpdateSupport ) {
        if ( StringUtils.isNull ( list ) || list.size ( ) == 0 ) {
            throw new BusinessException( "系统条线故障数据不能为空！" );
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder ( );
        StringBuilder failureMsg = new StringBuilder ( );
        for ( OgLine ogLine : list ) {

            log.warn ( ogLine.toString () );

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

            ogLine.setLineId(UUID.getUUIDStr());
            lineService.insertOgLine(ogLine);

        }
        if ( failureNum > 0 ) {
            failureMsg.insert ( 0 , "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：" );
            throw new BusinessException( failureMsg.toString() );
        } else {
            successMsg.insert ( 0 , "恭喜您，数据已全部导入成功！共 " + list.size () + " 条，数据如下：" +"\n"+list.toString());
        }
        return successMsg.toString ( );
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

}
