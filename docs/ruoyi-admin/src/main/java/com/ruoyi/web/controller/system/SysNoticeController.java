package com.ruoyi.web.controller.system;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.IAmBizParaService;
import com.ruoyi.activiti.service.IAmBizParaValueService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 *
 * @author dongdong_liu
 */
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    private String prefix = "system/notice";

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private IAmBizParaValueService amBizParaValueService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IOgPersonService ogPersonService;

    @GetMapping()
    public String notice()
    {
        return prefix + "/notice";
    }

    /**
     * 查询公告列表  接收&创建
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice)
    {
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        for (int i = 0 ; i < list.size() ; i ++) {
            String addTime = list.get(i).getAddTime();
            list.get(i).setAddTime(handleTimeYYYY_MM_DD_HH_MM_SS(addTime));
        }
        list = list.stream().sorted(Comparator.comparing(SysNotice::getAddTime).reversed()).collect(Collectors.toList());
        return getDataTable_ideal(list);
    }

    /**
     * 查询公告通知列表
     */
    @PostMapping("/selectList")
    @ResponseBody
    public TableDataInfo selectList(SysNotice notice)
    {
        notice.setMyIdentity("2");
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        for (int i = 0 ; i < list.size() ; i ++) {
            String releaseDate = list.get(i).getReleaseDate();
            list.get(i).setReleaseDate(handleTimeYYYY_MM_DD_HH_MM_SS(releaseDate));
        }
        list = list.stream().sorted(Comparator.comparing(SysNotice::getAddTime).reversed()).collect(Collectors.toList());
        return getDataTable_ideal(list);
    }

    /**
     * 查询监控公告列表
     */
    @PostMapping("/monitorNotice")
    @ResponseBody
    public TableDataInfo monitorNotice(SysNotice notice)
    {
        notice.setMyIdentity("3");
        notice.setNoticeStatus("03");
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        for (int i = 0 ; i < list.size() ; i ++) {
            String releaseDate = list.get(i).getReleaseDate();
            list.get(i).setReleaseDate(handleTimeYYYY_MM_DD_HH_MM_SS(releaseDate));
        }
        list = list.stream().sorted(Comparator.comparing(SysNotice::getAddTime).reversed()).collect(Collectors.toList());
        return getDataTable_ideal(list);
    }

    /**
     * 跳转新增公告页面
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap)
    {
        modelMap.put("sendRangeList", getSengRangeList());
        modelMap.put("amBizId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * 新增保存公告
     */
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNotice notice)
    {
        getReceiveDeptGroupName(notice);
        int num = noticeService.insertNotice(notice);
        if (num == 0) {
            return AjaxResult.error("该工作通知暂无接收人员，请重新选择!");
        }
        if ("02".equals(notice.getCurrentStatus())) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(notice.getCheckerId());
            String smsText = "工作通知编号：" + notice.getAmCode() + "，标题：" + notice.getAmTitle() + "的通知已提交，请登录运维管理平台审核。";
            sendSms(smsText, ogPerson);
        }
        return toAjax(num);
    }

    /**
     * selectByid
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        SysNotice notice = noticeService.selectNoticeById(id);
        ajaxResult.put("data",notice);
        return  ajaxResult;
    }

    /**
     * 跳转修改公告页面
     */
    @GetMapping("/edit/{amBizId}")
    public String edit(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        mmap.put("sendRangeList", getSengRangeList());
        mmap.put("notice", noticeService.selectNoticeById(amBizId));
        return prefix + "/edit";
    }

    /**
     * 修改保存公告
     */
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotice notice)
    {
        getReceiveDeptGroupName(notice);
        //判断是否审核通过 审核通过获得所有接收人的id 发送短信
        if (StringUtils.isNotEmpty(notice.getCheckerFlag()) && "0".equals(notice.getCheckerFlag()) && "1".equals(notice.getSms())){
            Set set = insertSysnoticeReceive(notice);
            Iterator<Object> it = set.iterator();
            while (it.hasNext()) {
                OgPerson ogPerson = ogPersonService.selectOgPersonById((String)it.next());
                String text = "工作通知标题：" +notice.getAmTitle()+ "; 工作通知单号："+ notice.getAmCode() +"；请尽快登陆运维管理平台查看并处理。";
                sendSms(text, ogPerson);
            }
        }
        int num = noticeService.updateNotice(notice);
        if (num == 0) {
            return AjaxResult.error("该工作通知暂无接收人员，请重新选择!");
        }
        if ("02".equals(notice.getCurrentStatus())) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(notice.getCheckerId());
            String smsText = "工作通知编号：" + notice.getAmCode() + "，标题：" + notice.getAmTitle() + "的通知已提交，请登录运维管理平台审核。";
            sendSms(smsText, ogPerson);
        }
        return toAjax(num);
    }

    /**
     * 审核通过逐一发送短信//获取短信接收人id
     *
     * @param notice
     * @return 结果
     */
    private Set insertSysnoticeReceive(SysNotice notice) {
        String[] postIds = notice.getReceiveRoleId().split(",");
        String[] receiveDeptGroupIds = null;
        notice.setPostId(postIds);//接收岗位postIds-----------------------
        List<OgPerson> personList = null;
        String receivePersons = "";//接收人员列表
        int num  = 0;
        if (StringUtils.isNotEmpty(notice.getReceiveDeptId())) {
            receiveDeptGroupIds = notice.getReceiveDeptId().split(",");
            for (int i = 0 ; i < receiveDeptGroupIds.length ; i++) {

                notice.setOrgId(receiveDeptGroupIds[i]);//接收机构orgId-----------------------
                personList = noticeService.selectReceivePersonDept(notice);
                for (OgPerson ogPerson : personList) {
                    if ("1".equals(ogPerson.getInvalidationMark())) {
                        receivePersons += ogPerson.getpId() + ",";
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(notice.getReceiveGroupId())) {
            receiveDeptGroupIds = notice.getReceiveGroupId().split(",");
            for (int i = 0 ; i < receiveDeptGroupIds.length ; i++) {
                notice.setGroupId(receiveDeptGroupIds[i]);//接收工作组orgId-----------------------
                personList = noticeService.selectReceivePersonGroup(notice);
                for (OgPerson ogPerson : personList) {
                    if ("1".equals(ogPerson.getInvalidationMark())) {
                        receivePersons += ogPerson.getpId() + ",";
                    }
                }
            }
        }
        String[] arr = receivePersons.split(",");
        Set set = new HashSet();
        for (int i = 0 ; i < arr.length ; i++) {
            set.add(arr[i]);
        }
        return set;
    }

    /**
     * 跳转克隆公告页面
     */
    @GetMapping("/clone/{amBizId}")
    public String noticeClone(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        mmap.put("sendRangeList", getSengRangeList());
        SysNotice notice = noticeService.selectNoticeById(amBizId);
        notice.setAmBizId(UUID.getUUIDStr());
        mmap.put("notice", notice);
        return prefix + "/noticeClone";
    }

    /**
     * 克隆公告保存
     */
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/clone")
    @ResponseBody
    public AjaxResult noticeClonesave(SysNotice notice)
    {
        getReceiveDeptGroupName(notice);
        int num = noticeService.insertNotice(notice);
        if (num == 0) {
            return AjaxResult.error("该工作通知暂无接收人员，请重新选择!");
        }
        return toAjax(num);
    }

    /**
     * 删除公告
     */
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        int i = noticeService.deleteNoticeByIds(ids);
        if (i == 0) {
            return AjaxResult.error("该工作通知当前状态不可删除，请重新选择!");
        }
        return AjaxResult.success("已删除");
    }

    /**
     * 公告详情
     */
    @GetMapping("/details/{amBizId}")
    public String details(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        mmap.put("sendRangeList", getSengRangeList());
        mmap.put("notice", noticeService.selectNoticeById(amBizId));
        return prefix + "/noticeDetails";
    }

    /**
     * 查询接收工作组
     */
    @GetMapping("/selectNoticeReceiveGroup")
    public String selectNoticeReceiveGroup(String rid, ModelMap mmap) {
        return prefix + "/selectNoticeReceiveGroup";
    }

    /**
     * 选择接收工作组
     * @return
     */
    @PostMapping("/selectNoticeReceiveGroupList")
    @ResponseBody
    public TableDataInfo selectNoticeReceiveGroupList(OgGroup ogGroup) {
        startPage();
        List<OgGroup> values = noticeService.selectNoticeReceiveGroupList(ogGroup);
        return getDataTable_ideal(values);
    }

    /**
     * 查询接收机构
     */
    @GetMapping("/selectNoticeReceiveDept")
    public String selectReceiveDept() {
        return prefix + "/selectNoticeReceiveDept";
    }

    /**
     * 选择接收机构
     * @return
     */
    @PostMapping("/selectNoticeReceiveDeptList")
    @ResponseBody
    public TableDataInfo selectReceiveDeptList(OgOrg ogOrg) {

        startPage();
        List<OgOrg> list = noticeService.selectNoticeReceiveDeptList(ogOrg);
        return getDataTable(list);
    }

    /**
     * 查询接收岗位
     */
    @GetMapping("/selectReceptionPost")
    public String selectReceptionPost(String rid, ModelMap mmap) {
        mmap.put("rid", rid);
        return prefix + "/selectReceptionPost";
    }

    /**
     * 选择接收岗位List
     * @param sysNotice 公告实体传rid 与 postName
     * @return
     */
    @PostMapping("/selectReceptionPostList")
    @ResponseBody
    public TableDataInfo selectReceptionPostList(SysNotice sysNotice) {
        startPage();
        List<OgRole> values = noticeService.selectReceptionPostList(sysNotice);
        return getDataTable(values);
    }

    //获取发送范围下拉框
    private List<Map<String, String>> getSengRangeList() {

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        return reList;
    }

    //获取公告编码
    private String getAmCode() {

        String nowDate = DateUtils.dateTimeNow("yyyyMMdd");
        SysNotice sysNotice = noticeService.selectNoticeMaxAmCode();
        Integer index = 1;
        if (sysNotice != null) {
            String amCodeDate = sysNotice.getAmCode().substring(2,10);
            if ( nowDate.equals(amCodeDate) ) {
                index = Integer.valueOf(sysNotice.getAmCode().substring(11)) + 1;
            }
        }
        String amCode = "GG" + nowDate + StringUtils.leftPad(index.toString(), 4, "0");
        return amCode;
    }

    /**
     * 获取接收机构/工作组
     *
     */
    private SysNotice getReceiveDeptGroupName(SysNotice notice) {

        if (StringUtils.isNotEmpty(notice.getSendRange())) {
            String sendRange = notice.getSendRange();
            if ("1".equals(sendRange)) {//接收范围为机构
                notice.setReceiveDeptId(notice.getReceiveDeptGroupId());
                notice.setReceiveDeptName(notice.getReceiveDeptGroupName());
                notice.setReceiveGroupId("");
                notice.setReceiveGroupName("");
            } else if ("2".equals(sendRange)) {
                notice.setReceiveGroupId(notice.getReceiveDeptGroupId());
                notice.setReceiveGroupName(notice.getReceiveDeptGroupName());
                notice.setReceiveDeptId("");
                notice.setReceiveDeptName("");
            } else {
                AmBizParaValue amBizParaValue = new AmBizParaValue();
                amBizParaValue.setAmParaId(notice.getSendRange());
                List<AmBizParaValue> list = amBizParaValueService.
                        selectAmBizParaValueList(amBizParaValue);
                OgOrg ogOrg = new OgOrg();
                OgGroup ogGroup = new OgGroup();
                String noticeReceiveDeptId = "";
                String noticeReceiveDeptName = "";
                String noticeReceiveGroupId = "";
                String noticeReceiveGroupName = "";
                for (AmBizParaValue am : list) {
                    if (StringUtils.isNotEmpty(am.getReceivedeptid())) {
                        ogOrg = deptService.selectDeptById(am.getReceivedeptid());
                        noticeReceiveDeptId += am.getReceivedeptid() + ",";
                        noticeReceiveDeptName += ogOrg.getOrgName() + ",";
                    } else {
                        ogGroup = workService.selectOgGroupById(am.getReceivegroupid());
                        noticeReceiveGroupId += am.getReceivegroupid() + ",";
                        noticeReceiveGroupName += ogGroup.getGrpName() + ",";
                    }
                }
                notice.setReceiveDeptId(noticeReceiveDeptId);
                notice.setReceiveDeptName(noticeReceiveDeptName);
                notice.setReceiveGroupId(noticeReceiveGroupId);
                notice.setReceiveGroupName(noticeReceiveGroupName);
            }
        }
        return notice;
    }

    /**
     * 导出公告通知列表
     */
    @Log(title = "公告通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysNotice notice)
    {
        String isCurrentPage = (String) notice.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        } else  if ("all".equals(isCurrentPage)) {
            //notice = new SysNotice();
        }
        notice.setMyIdentity("2");
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        return util.exportExcel(list, "公告通知");
    }

    /**
     * 导出公告通知列表
     */
    @Log(title = "公告通知", businessType = BusinessType.EXPORT)
    @PostMapping("/monItorExport")
    @ResponseBody
    public AjaxResult monItorExport(SysNotice notice)
    {
        String isCurrentPage = (String) notice.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        } else  if ("all".equals(isCurrentPage)) {
            //notice = new SysNotice();
        }
        notice.setMyIdentity("2");
        notice.setNoticeStatus("03");
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        return util.exportExcel(list, "公告通知");
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
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 加载所有部门列表树
     */
    @GetMapping("/selectAllTree")
    @ResponseBody
    public List<Ztree> selectAllTree(String type, String deptIds) {
        OgOrg ogOrg = new OgOrg();
        ogOrg.setInvalidationMark("1");
        //勾选已选的机构Id
        if (StringUtils.isNotEmpty(deptIds)) {
            String[] deptIdarr = deptIds.split(",");
            List<String> deptIdList = new ArrayList<>();
            for (int i = 0 ; i <deptIdarr.length ; i ++) {
                deptIdList.add(deptIdarr[i]);
            }
            List<OgOrg> deptList = deptService.selectDeptList(ogOrg);//全部机构信息
            List<Ztree> ztrees = initZtree(deptList, deptIdList, true);
            return ztrees;
        }
        //默认初始化加载所有机构
        if ("0".equals(type)) {
            ogOrg.setLevelCode("/000000000/");
            return deptService.selectDeptTree(ogOrg);
        }
        //点击省分行或者信息技术局勾选相应机构
        String[] types = type.split(",");
        List<OgOrg> list = new ArrayList<>();
        List<String> orgIdList = new ArrayList<>();//查询到的机构id集合
        for (int i = 0 ; i < types.length ; i ++) {
            if (!"0".equals(types[i])) {
                if ("4".equals(types[i])) {
                    ogOrg.setOrgName("分行");
                    ogOrg.setType("01,02");
                } else if ("5".equals(types[i])) {
                    ogOrg.setOrgName("技术局");
                    ogOrg.setType("01,02");
                } else if ("6".equals(types[i])) {
                    ogOrg.setOrgName("");
                    ogOrg.setType("03");
                }
                list.addAll(noticeService.DeptTreeBankch(ogOrg));
            }
        }
        if (list.size() > 0) {
            for (OgOrg org : list) {
                orgIdList.add(org.getOrgId());
            }
        }
        OgOrg ogOrg1 = new OgOrg();
        List<OgOrg> deptList = deptService.selectDeptList(ogOrg1);//全部机构信息
        List<Ztree> ztrees = new ArrayList<Ztree>();
        ztrees = initZtree(deptList, orgIdList, true);
        return ztrees;
    }

    /**
     * 对象转菜单树
     *
     * @param orgList 全部机构信息
     * @param orgIdList 查询到的机构id集合
     * @param permsFlag 是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> orgList, List<String> orgIdList, boolean permsFlag)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(orgIdList);
        for (OgOrg ogOrg : orgList) {
            Ztree ztree = new Ztree();
            ztree.setId(ogOrg.getOrgId()+"");
            ztree.setpId(ogOrg.getParentId()+"");
            ztree.setName(transOrgName(ogOrg, permsFlag));
            ztree.setTitle(ogOrg.getOrgName());
            if (isCheck) {
                ztree.setChecked(orgIdList.contains(ogOrg.getOrgId().toString()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public String transOrgName(OgOrg ogOrg, boolean permsFlag)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(ogOrg.getOrgName());
        return sb.toString();
    }

    /**
     * 选择XXX审批人
     *
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public String selectBusinessAudit(String pflag, String rId, ModelMap mmap) {
        mmap.put("rId", rId);
        mmap.put("pflag", pflag);
        return prefix + "/selectNoticeAuditor";
    }

}
