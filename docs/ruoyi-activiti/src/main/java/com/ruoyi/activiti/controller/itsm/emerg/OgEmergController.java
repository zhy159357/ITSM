package com.ruoyi.activiti.controller.itsm.emerg;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.service.impl.OgEmergServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgEmerg;
import com.ruoyi.system.domain.OgEmergMark;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应急事件单
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("emerg/xz")
public class OgEmergController extends BaseController
{
    //新增界面路径
    private String prefix_xz = "emerg/xz";

    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private ISysWorkService ISysWorkService;

    @Autowired
    private OgEmergServiceImpl emergService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @GetMapping()
    public String emerg() {
        return prefix_xz + "/emerg";
    }

    //选择系统
    @GetMapping("/selectOneApplication")
    public String selectApplication() {
        return prefix_xz + "/selectOneApplication";
    }

    //新建事件单
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgEmerg emerg)
    {
        startPage();
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userName = ogUser.getUsername();
        if (!"admin".equals(userName)){
            emerg.setPersonemergId(ogUser.getpId());
        }
        List<OgEmerg> list = emergService.selectEmergList(emerg);
        return getDataTable(list);

    }

    /**
     * 新建事件单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap,OgEmerg ogEmerg) {
        //公用的一个生成时间单号
        String bizType = "YJSJ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);
        //生成uuid
        mmap.put("id",UUID.getUUIDStr());
        //获取工作组
        List<OgGroup> ogGroups = ISysWorkService.selectOgGroupList(null);
        mmap.put("groups",ogGroups);
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime",nowTime);
        mmap.put("ogEmerg",ogEmerg);
        return prefix_xz + "/add";
    }

    /*
     * 新建保存事件单
     * */
    @Log(title = "新建事件单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgEmerg emerg)
    {

        /*try {
            if (StringUtils.isEmpty(emerg.getId())) {
                emerg.setId(UUID.getUUIDStr());
                emergService.insertOgEmerg(emerg);
                return AjaxResult.success("操作成功", emerg.getId());
            } else {
                emergService.updateEmerg(emerg);
                return AjaxResult.success("操作成功", emerg.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("暂存失败，单号是：" + emerg.getEmergId());
        }*/
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        //pid直接赋值给personemergId字段
        emerg.setPersonemergId(ogUser.getpId());
        //emerg.setId(UUID.getUUIDStr());
        return toAjax(emergService.insertOgEmerg(emerg));
    }

    /**
     * 获取当前数据提取单的所涉及的系统名称
     * @param id
     * @return
     */
    @GetMapping("/selectEmergById/{id}")
    @ResponseBody
    public AjaxResult selectEmergById(@PathVariable("id") String id){
        AjaxResult ajaxResult  = new AjaxResult();
        OgEmerg ogEmerg = emergService.selectEmergById(id);
        if(StringUtils.isNotEmpty(ogEmerg.getStartSystem()) && StringUtils.isNotNull(ogEmerg.getXtsId())){
            String[] split = ogEmerg.getXtsId().split(",");
            ajaxResult.put("data",split);
        }else{
            ajaxResult.put("data","0");
        }
        return ajaxResult;
    }

    /**
     * 修改事件单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap,OgEmerg ogEmerg)
    {
        //获取工作组
        List<OgGroup> ogGroups = ISysWorkService.selectOgGroupList(null);
        mmap.put("groups",ogGroups);
        mmap.put("OgEmerg", emergService.selectEmergById(id));
        return prefix_xz + "/edit";
    }

    /**
     * 修改保存事件单
     */
    @Log(title = "事件单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgEmerg emerg)
    {
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        //pid直接赋值给personemergId字段
        emerg.setPersonemergId(ogUser.getpId());
        return toAjax(emergService.updateEmerg(emerg));
    }

    /**
     * 删除/作废-事件单
     */
    @Log(title = "事件单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids, OgEmergMark mark)
    {
        try
        {
            return toAjax(emergService.deleteEmergByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        OgEmerg ogEmerg = emergService.selectEmergById(id);
        ajaxResult.put("data",ogEmerg);
        return  ajaxResult;
    }

    //应急系统的存储
    @RequestMapping("/getAllSys")
    @ResponseBody
    public AjaxResult getAllSys(){
        AjaxResult ajaxResult = new AjaxResult();
        List<OgSys> ogSys = applicationManagerService.selectOgSysList(null);
        List<OgSys> list = ogSys.stream().filter(sys -> "1".equals(sys.getInvalidationMark())).collect(Collectors.toList());
        ajaxResult.put("data",list);
        return ajaxResult;
    }

    /**
     * 查看详情事件单
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        //获取工作组
        List<OgGroup> ogGroups = ISysWorkService.selectOgGroupList(null);
        mmap.put("groups",ogGroups);
        mmap.put("OgEmerg", emergService.selectById(id));
        return prefix_xz + "/detail";
    }

    //选择系统加载系统类别
    @PostMapping("/selectFmKindBySysid")
    @ResponseBody
    public OgSys selectFmKindBySysid(String sysId){
        return applicationManagerService.selectOgSysBySysId(sysId);
    }

    /**
     * 响应请求分页数据
     */
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

    /**
     * 设置请求分页数据
     */
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
