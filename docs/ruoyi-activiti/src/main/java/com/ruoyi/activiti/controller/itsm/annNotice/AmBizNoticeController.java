package com.ruoyi.activiti.controller.itsm.annNotice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.visitor.functions.Substring;
import com.ruoyi.activiti.domain.AmBizNotice;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUserPost;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserPostService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;

import com.ruoyi.activiti.service.IAmBizNoticeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公告通知 数据中心内部使用
 *
 * @author dongdongLiu
 * @date 2021-10-19
 */
@Controller
@RequestMapping("/activiti/notice")
public class AmBizNoticeController extends BaseController{

    private String prefix = "amBizNotice";

    @Autowired
    private IAmBizNoticeService amBizNoticeService;

    @Autowired
    private IOgUserPostService ogUserPostService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private ISysDeptService deptService;

    @GetMapping("/myList")
    public String myNoticeList()
    {
        return prefix + "/myList";
    }

    @GetMapping("/checkList")
    public String checkNoticeList()
    {
        return prefix + "/checkList";
    }

    @GetMapping("/receiveList")
    public String receiveList()
    {
        return prefix + "/receiveList";
    }

    @GetMapping("/selectList")
    public String selectList()
    {
        return prefix + "/selectList";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AmBizNotice amBizNotice)
    {
        setTime(amBizNotice);
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(ShiroUtils.getUserId());
        String sendRanges = "3";//1 数据中心 2 厂商+数据中心（72号院） 3 所有用户
        List<OgUserPost> postslist = ogUserPostService.selectPostByUserId(ogUserPost);
        for (OgUserPost ogUserPost1 : postslist) {
            if ("0010".equals(ogUserPost1.getPostId()) || "0011".equals(ogUserPost1.getPostId()) ||"0012".equals(ogUserPost1.getPostId())) {
                sendRanges = "1,2,3";
                break;
            }
            if ("0002".equals(ogUserPost1.getPostId())) {
                sendRanges = "2,3";
            }
        }
        //myIdentity 1我接收的 2自己创建的
        if ("1".equals(amBizNotice.getParams().get("myIdentity"))) {
            amBizNotice.setCurrentStatus("04");
            amBizNotice.setSendRanges(sendRanges.split(","));
        } else if ("2".equals(amBizNotice.getParams().get("myIdentity"))) {
            amBizNotice.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<AmBizNotice> list = amBizNoticeService.selectAmBizNoticeList(amBizNotice);
        return getDataTable(list);
    }

    /**
     * 待审核公告通知列表List
     */
    @PostMapping("/checkList")
    @ResponseBody
    public TableDataInfo checkList(AmBizNotice amBizNotice)
    {
        setTime(amBizNotice);
        amBizNotice.setCurrentStatus("02");
        amBizNotice.setCheckerId(ShiroUtils.getUserId());
        List<AmBizNotice> list = amBizNoticeService.selectAmBizNoticeList(amBizNotice);
        return getDataTable(list);
    }

    /**
     * 查看公告通知列表List（接收）
     */
    @PostMapping("/receiveList")
    @ResponseBody
    public TableDataInfo receiveList(AmBizNotice amBizNotice)
    {
        setTime(amBizNotice);
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(ShiroUtils.getUserId());
        String sendRanges = "3";//1 数据中心 2 厂商+数据中心（72号院） 3 所有用户
        List<OgUserPost> postslist = ogUserPostService.selectPostByUserId(ogUserPost);
        for (OgUserPost ogUserPost1 : postslist) {
            if ("0010".equals(ogUserPost1.getPostId()) || "0011".equals(ogUserPost1.getPostId()) ||"0012".equals(ogUserPost1.getPostId())) {
                sendRanges = "1,2,3";
                break;
            }
            if ("0002".equals(ogUserPost1.getPostId())) {
                sendRanges = "2,3";
            }
        }
        amBizNotice.setCurrentStatus("04");
        amBizNotice.setSendRanges(sendRanges.split(","));
        startPage();
        List<AmBizNotice> list = amBizNoticeService.selectAmBizNoticeList(amBizNotice);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【导出公告通知】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AmBizNotice amBizNotice)
    {
        List<AmBizNotice> list = amBizNoticeService.selectAmBizNoticeList(amBizNotice);
        ExcelUtil<AmBizNotice> util = new ExcelUtil<AmBizNotice>(AmBizNotice.class);
        return util.exportExcel(list, "notice");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        String bizType = "GGTZ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("amCode", "GGTZ" + nowDateStr + numStr);
        mmap.put("amBizId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * 新增保存【新增公告通知】
     */
    @Log(title = "【新增公告通知】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AmBizNotice amBizNotice)
    {
        amBizNotice.setCreateId(ShiroUtils.getUserId());
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        amBizNotice.setReleaseOrg(ogPerson.getOrgId());
        amBizNotice.setInvalidationMark("1");
        amBizNotice.setAddTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        amBizNotice.setTopTime(handleTimeYYYYMMDDHHMMSS(amBizNotice.getTopTime()));
        if ("02".equals(amBizNotice.getCurrentStatus())) {
            String text = "公告通知标题：" + amBizNotice.getAmTitle() + "，需要您审批，请登录运维管理平台查看";
            OgPerson person = ogPersonService.selectOgPersonById(amBizNotice.getCheckerId());// 获取短信接收人
            if (person == null) {
                return AjaxResult.error("审批人不能为空");
            }
            sendSms(text, person);
        }

//        Date date = new Date();
//        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //1、一天 2、一周 3、一个月 4、三个月 5、六个月 6、一年
//        if("0".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMinutes(date, 5);
//            amBizNotice.setAmOfflineTime(dateformat1.format(date));
//        }else if("1".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addDays(date, 1);
//        }else if("2".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addDays(date, 7);
//        }else if("3".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMonths(date, 1);
//        }else if("4".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMonths(date, 3);
//        }else if("5".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMonths(date, 6);
//        }else if("6".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addYears(date, 1);
//        }
//        amBizNotice.setAmOfflineTime(dateformat1.format(date));
        return toAjax(amBizNoticeService.insertAmBizNotice(amBizNotice));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{amBizId}")
    public String edit(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        AmBizNotice amBizNotice = amBizNoticeService.selectAmBizNoticeById(amBizId);
        amBizNotice.setTopTime(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getTopTime()));
        mmap.put("amBizNotice", amBizNotice);
        return prefix + "/edit";
    }

    /**
     * 修改保存【修改公告通知】
     */
    @Log(title = "【修改公告通知】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AmBizNotice amBizNotice)
    {
        if (StringUtils.isNotEmpty(amBizNotice.getTopTime())) {
            amBizNotice.setTopTime(handleTimeYYYYMMDDHHMMSS(amBizNotice.getTopTime()));
        }
        if ("02".equals(amBizNotice.getCurrentStatus())) {
            String text = "公告通知标题：" + amBizNotice.getAmTitle() + "，需要您审批，请登录运维管理平台查看";
            OgPerson person = ogPersonService.selectOgPersonById(amBizNotice.getCheckerId());// 获取短信接收人
            if (person == null) {
                return AjaxResult.error("审批人不能为空");
            }
            sendSms(text, person);
        }
//        Date date = new Date();
//        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //1、一天 2、一周 3、一个月 4、三个月 5、六个月 6、一年
//        if("0".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMinutes(date, 5);
//            amBizNotice.setAmOfflineTime(dateformat1.format(date));
//        }else if("1".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addDays(date, 1);
//        }else if("2".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addDays(date, 7);
//        }else if("3".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMonths(date, 1);
//        }else if("4".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMonths(date, 3);
//        }else if("5".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addMonths(date, 6);
//        }else if("6".equals(amBizNotice.getAmOffline())){
//            date = DateUtils.addYears(date, 1);
//        }
//        amBizNotice.setAmOfflineTime(dateformat1.format(date));
        return toAjax(amBizNoticeService.updateAmBizNotice(amBizNotice));
    }

    /**
     * 删除【公告通知】
     */
    @Log(title = "【删除公告通知】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(amBizNoticeService.deleteAmBizNoticeByIds(ids));
    }

    /**
     * 撤回【公告通知】为作废状态
     */
    @Log(title = "【撤回公告通知】", businessType = BusinessType.UPDATE)
    @PostMapping( "/back")
    @ResponseBody
    public AjaxResult back(String id)
    {
        AmBizNotice amBizNotice = new AmBizNotice();
        amBizNotice.setAmBizId(id);
        amBizNotice.setCurrentStatus("06");//作废
        amBizNotice.setIsTop("0");//取消置顶
        return toAjax(amBizNoticeService.updateAmBizNotice(amBizNotice));
    }

    /**
     * 公告详情
     */
    @GetMapping("/details/{amBizId}")
    public String details(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        AmBizNotice amBizNotice = amBizNoticeService.selectAmBizNoticeById(amBizId);
        amBizNotice.setTopTime(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getTopTime()));
        mmap.put("ambiz", amBizNotice);

//        String appid = "appId"+"20210003";
//        String timestamp = "timestamp"+System.currentTimeMillis();
//        String nonce = "nonce"+"aaaaaaaaaaaaaaaaaa";//18位
//        String secret = "100af4e620024b40bbfc49214ea66508";
//        String str =appid+nonce+timestamp+secret;
//        String sign = SecureUtil.sha256(str).toUpperCase();
//        System.out.println("timestamp="+timestamp);
//        System.out.println("Test=="+sign);
        return prefix + "/detail";
    }

    /**
     * 审批按钮对应页面
     */
    @GetMapping("/checkDetail/{amBizId}")
    public String checkDetail(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        AmBizNotice amBizNotice = amBizNoticeService.selectAmBizNoticeById(amBizId);
        amBizNotice.setTopTime(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getTopTime()));
        mmap.put("amBizNotice", amBizNotice);
        return prefix + "/checkDetail";
    }

    /**
     * 审核公告通知
     */
    @Log(title = "【审核公告通知】", businessType = BusinessType.UPDATE)
    @PostMapping("/checkNotice")
    @ResponseBody
    public AjaxResult checkNotice(AmBizNotice amBizNotice)
    {
        String checkFlag = amBizNotice.getCheckerFlag();// 0 审核通过 1 退回
        String currentStatus = "";
        if ("0".equals(checkFlag)) {
            currentStatus = "04";
//            AmBizNotice amBizNotice1 = amBizNoticeService.selectAmBizNoticeById(amBizNotice.getAmBizId());
//            if ("1".equals(amBizNotice1.getIsTop())) {
//                Date date = new Date();
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(date);//设置起时间
//                if ("1".equals(amBizNotice1.getTopTimeFlag())) {
//                    cal.add(Calendar.DATE, 2);//增加1天
//                } else if ("2".equals(amBizNotice1.getTopTimeFlag())) {
//                    cal.add(Calendar.DATE, 4);//增加3天
//                } else if ("3".equals(amBizNotice1.getTopTimeFlag())) {
//                    cal.add(Calendar.WEEK_OF_MONTH, 1);//增加一周
//                } else if ("4".equals(amBizNotice1.getTopTimeFlag())) {
//                    cal.add(Calendar.MONTH, 1);//增加1个月
//                } else if ("5".equals(amBizNotice1.getTopTimeFlag())) {
//                    cal.add(Calendar.YEAR, 1);//增加一年
//                }
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                String dateStr = sdf.format(cal.getTime());
//                amBizNotice.setTopTime(handleTimeYYYYMMDD(dateStr) + "000000");
//            }
            AmBizNotice amBizNotice1 = amBizNoticeService.selectAmBizNoticeById(amBizNotice.getAmBizId());
            /*审核通过更新下线时间*/
            Date date = new Date();
//            SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMddHHmmss");

            //1、一天 2、一周 3、一个月 4、三个月 5、六个月 6、一年
            if("0".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addMinutes(date, 5);
                amBizNotice.setAmOfflineTime(dateformat1.format(date));
            }else if("1".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addDays(date, 1);
            }else if("2".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addDays(date, 7);
            }else if("3".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addMonths(date, 1);
            }else if("4".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addMonths(date, 3);
            }else if("5".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addMonths(date, 6);
            }else if("6".equals(amBizNotice1.getAmOffline())){
                date = DateUtils.addYears(date, 1);
            }
            amBizNotice.setAmOfflineTime(dateformat1.format(date));
        } else if ("1".equals(checkFlag)) {
            currentStatus = "03";
        }
        amBizNotice.setCurrentStatus(currentStatus);
        amBizNotice.setCheckerTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        amBizNotice.setReleaseDate(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        return toAjax(amBizNoticeService.updateAmBizNotice(amBizNotice));
    }

    /**
     * 查询公告通知列表
     */
    @PostMapping("/selectList")
    @ResponseBody
    public TableDataInfo selectList(AmBizNotice amBizNotice)
    {
        setTime(amBizNotice);
        startPage();
        List<AmBizNotice> list = amBizNoticeService.selectAmBizNoticeList(amBizNotice);
        return getDataTable(list);
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
        AmBizNotice ambiz = amBizNoticeService.selectAmBizNoticeById(id);
        ajaxResult.put("data",ambiz);
        return  ajaxResult;
    }

    /**
     * 跳转克隆公告页面
     */
    @GetMapping("/clone/{amBizId}")
    public String noticeClone(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        AmBizNotice amBizNotice = amBizNoticeService.selectAmBizNoticeById(amBizId);
        amBizNotice.setAmBizId(UUID.getUUIDStr());
        String bizType = "BGQQ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        amBizNotice.setAmCode("GGTZ" + nowDateStr + numStr);
        mmap.put("amBiz", amBizNotice);
        return prefix + "/amBizClone";
    }

    //时间改格式
    private AmBizNotice setTime(AmBizNotice amBizNotice) {
        if (amBizNotice.getParams().containsKey("beginTime")) {
            if (StringUtils.isNotEmpty(amBizNotice.getParams().get("beginTime").toString())) {
                amBizNotice.getParams().put("beginTime",handleTimeYYYYMMDDHHMMSS(amBizNotice.getParams().get("beginTime").toString()));
            }
        }
        if (amBizNotice.getParams().containsKey("endTime")) {
            if (StringUtils.isNotEmpty(amBizNotice.getParams().get("endTime").toString())) {
                amBizNotice.getParams().put("endTime",handleTimeYYYYMMDD(amBizNotice.getParams().get("endTime").toString()) + "240000");
            }
        }
        return amBizNotice;
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
}