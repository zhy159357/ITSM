package com.ruoyi.activiti.controller.itsm.fmsw;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.constants.FmSwConstants;
import com.ruoyi.activiti.domain.FmSwMb;
import com.ruoyi.activiti.service.IFmSwMbService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
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
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/transmb/mb")
public class FmSwMbController  extends BaseController {
    private String prefix = "transmb/mb";

    @Autowired
    private IFmSwMbService fmSwMbService;


    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IPubAttachmentService iPubAttachmentService;

    @Autowired
    private IOgUserService ogUserService;


    /**
     * ???????????????????????????
     * @param modelMap
     * @return
     */
    @GetMapping()
    public String list(ModelMap modelMap)
    {
        return prefix + "/list";
    }


    /**
     * ???????????????????????????
     * @param fmSwMb
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSwMb fmSwMb){
        List<FmSwMb> fmSwMbListm = null;
        if(StringUtils.isNotEmpty(fmSwMb.getStartCreateTime())){
            String startTime = DateUtils.formatDateStr(fmSwMb.getStartCreateTime(), DateUtils.YYYY_MM_DD, DateUtils.YYYYMMDD);
            fmSwMb.setStartCreateTime(startTime+ FmSwConstants.start_suffix);
        }
        if(StringUtils.isNotEmpty(fmSwMb.getEndCreateTime())){
            String endTime = DateUtils.formatDateStr(fmSwMb.getEndCreateTime(), DateUtils.YYYY_MM_DD, DateUtils.YYYYMMDD);
            fmSwMb.setEndCreateTime(endTime+ FmSwConstants.end_suffix);
        }

        //????????????????????????
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        if("1".equals(oneLvOrTwoLv.getOrgLv())){
            //?????????????????????
            startPage();
            fmSwMbListm = fmSwMbService.selectFmSwMbList(fmSwMb);

        }else{
            fmSwMb.setLevelCode(ogOrg.getLevelCode());
            //?????????????????????
            startPage();
          fmSwMbListm = fmSwMbService.selectFmSwMbList(fmSwMb);
            return getDataTable(fmSwMbListm);
        }
        return getDataTable(fmSwMbListm);
    }

    /**
     * ?????????????????????????????????
     * @param modelMap
     * @return
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap){

        modelMap.put("user",ShiroUtils.getOgUser());
        modelMap.put("swmdId",UUID.getUUIDStr());
        return prefix+"/add";
    }


    /**
     * ?????????????????????????????????
     * @param fmSwMb
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmSwMb fmSwMb){
        if("".equals(fmSwMb.getSwmdId())){
            fmSwMb.setSwmdId(UUID.getUUIDStr());
        }
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        fmSwMb.setCreateId(ogPerson.getpId());
        fmSwMb.setCreateTime(DateUtils.dateTimeNow());
        fmSwMb.setCreateOrgId(ogPerson.getOrgId());
        //???????????????????????????????????????????????????????????????
        //???????????????????????????
        Boolean bool = fmSwMbService.checkExist(fmSwMb.getDealOrgId(),fmSwMb.getFaultKind());
        if(bool){
            return AjaxResult.error("??????????????????????????????????????????????????????");
        }
        int result = fmSwMbService.insertFmSwMb(fmSwMb);

        return toAjax(result);
    }

    /**
     * ?????????????????????????????????
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        return toAjax(fmSwMbService.deleteFmSwMbByIds(ids));
    }

    @GetMapping("/edit/{id}/{flag}")
    public String edit(@PathVariable("id") String id,@PathVariable("flag") String flag, ModelMap modelMap){
        //??????????????????
        FmSwMb fmSwMb = fmSwMbService.selectById(id);
        modelMap.put("fmSwMb",fmSwMb);
        modelMap.put("flag",flag);

        //?????????????????????????????????
        OgOrg ogOrg = deptService.selectDeptById(fmSwMb.getDealOrgId());
        String levelCode = ogOrg.getLevelCode();
        List<OgPerson> list = ogPersonService.selectListByLevelCode(levelCode,FmSwConstants.dealRole);
        modelMap.put("list",list);
        return prefix+"/edit";
    }

    /**
     *?????????????????????????????????
     * @param fmSwMb
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmSwMb fmSwMb){
        //??????????????????????????????????????????????????????????????????
        FmSwMb mb = new FmSwMb();
        mb.setDealOrgId(fmSwMb.getDealOrgId());
        List<FmSwMb> fmSwMbs = fmSwMbService.selectFmSwMbByDealOrgId(fmSwMb);
        List<FmSwMb> collect = fmSwMbs.stream().filter(fmmb -> !fmmb.getSwmdId().equals(fmSwMb.getSwmdId())).collect(Collectors.toList());

        Boolean isHas = false;
        for (FmSwMb c:collect) {
            if(c.getDealOrgId().equals(fmSwMb.getDealOrgId()) && c.getFaultKind().equals(fmSwMb.getFaultKind())){
                isHas = true;
            }
        }

        //????????????????????????
        if(isHas){
            return AjaxResult.error("??????????????????????????????????????????????????????");
        }
        return toAjax(fmSwMbService.updateFmSwMb(fmSwMb));
    }

    /**
     * ??????????????????????????????
     * @param nodeId
     * @param rId
     * @return
     */
    @PostMapping("/levelcodeAndMbInfo")
    @ResponseBody
    public AjaxResult levelcodeAndMbInfo(String nodeId,String rId){
        AjaxResult ajaxResult = new AjaxResult();
        //?????????????????????????????????
        OgOrg ogOrg = deptService.selectDeptById(nodeId);
        String levelCode = ogOrg.getLevelCode();
        String[] split = rId.split(",");

        //??????????????????????????????????????????????????????
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        //?????????
        List<OgPerson>  authorList= ogPersonService.selectListByLevelCode(oneLvOrTwoLv.getLevelCode(),split[0]);
        //?????????
        List<OgPerson>  dealList= ogPersonService.selectListByLevelCode(levelCode,split[1]);

        //????????????
        FmSwMb fmSwMb = new FmSwMb();
        fmSwMb.setDealOrgId(nodeId);
        List<FmSwMb> fmSwMbList = fmSwMbService.selectFmSwMbList(fmSwMb);
        List<Object> list = new ArrayList<>();
        list.add(authorList);
        list.add(dealList);
        list.add(fmSwMbList);
        return ajaxResult.put("data",list);
    }

    /**
     * ??????id??????????????????
     * @param mbId
     * @param fmId
     * @return
     */
    @PostMapping("/selectMbInfoById")
    @ResponseBody
    public AjaxResult selectMbInfoById(String mbId,String fmId){
        AjaxResult ajaxResult = new AjaxResult();
        FmSwMb fmSwMb = fmSwMbService.selectById(mbId);
        Attachment attachment = new Attachment();
        attachment.setOwnerId(mbId);
        List<Attachment> list = iPubAttachmentService.selectAttachmentList(attachment);
        for(Attachment att : list){
            att.setAtId(UUID.getUUIDStr());
            att.setCreateId(ShiroUtils.getUserId());
            att.setOwnerId(fmId);
            att.setFileTime(DateUtils.dateTimeNow());
            iPubAttachmentService.insertAttachment(att);
        }

        return ajaxResult.put("fmSwMb",fmSwMb);
    }

    /**
     * ???????????????????????????????????????????????????
     * @param faultKind
     * @param dealOrgId
     * @return
     */
    @PostMapping("/selectMbByDealOrgIdAndFaultKind")
    @ResponseBody
    public AjaxResult selectMbByDealOrgIdAndFaultKind(String faultKind,String dealOrgId){
        AjaxResult ajaxResult = new AjaxResult();
        FmSwMb fmSwMb = new FmSwMb();
        fmSwMb.setDealOrgId(dealOrgId);
        fmSwMb.setFaultKind(faultKind);
        FmSwMb mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
        if(mb==null){
            ajaxResult.put("result",0);
        }else{
            ajaxResult.put("result",1);
        }
        return ajaxResult;
    }

    /**
     * ?????????????????????????????????
     * @param fmSwMb
     * @return
     */
    @Log(title = "?????????????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmSwMb fmSwMb) {
        if(StringUtils.isNotEmpty(fmSwMb.getStartCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(fmSwMb.getStartCreateTime());
            fmSwMb.setStartCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(fmSwMb.getEndCreateTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(fmSwMb.getEndCreateTime());
            fmSwMb.setEndCreateTime(parseEnd+"240000");
        }

        String isCurrentPage = (String) fmSwMb.getParams().get("currentPage");
        //????????????????????????
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        fmSwMb.setCreateId(pId);
        if ("currentPage".equals(isCurrentPage)) {
            if(!"1".equals(oneLvOrTwoLv.getOrgLv())){
                fmSwMb.setLevelCode(ogOrg.getLevelCode());
            }
            startPage();
            List<FmSwMb> list = fmSwMbService.selectFmSwMbList(fmSwMb);
            ExcelUtil<FmSwMb> util = new ExcelUtil<FmSwMb>(FmSwMb.class);
            return util.exportExcel(list, "?????????????????????");
        } else {
            if(!"1".equals(oneLvOrTwoLv.getOrgLv())){
                fmSwMb.setLevelCode(ogOrg.getLevelCode());
            }
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<FmSwMb> list = fmSwMbService.selectFmSwMbList(fmSwMb);
            ExcelUtil<FmSwMb> util = new ExcelUtil<FmSwMb>(FmSwMb.class);
            return util.exportExcel(list, "?????????????????????");
        }

    }

    /**
     * ??????????????????????????????
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        ajaxResult.put("uname", ShiroUtils.getOgUser().getUsername());
        return ajaxResult;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.????????????????????????????????????????????????????????????
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

