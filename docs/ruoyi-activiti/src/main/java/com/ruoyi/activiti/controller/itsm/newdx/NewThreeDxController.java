package com.ruoyi.activiti.controller.itsm.newdx;

import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubBizSms;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsReceiveService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/*
 *
 * 发送短信
 * */
@Controller
@RequestMapping("new/newThree")
public class NewThreeDxController extends BaseController {

    //查询界面路径
    private String prefix_dx = "new/newThree";

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsReceiveService pubBizSmsReceiveService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_dx + "/newThree";
    }

    //信息展示
    /*@PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubBizSms pubBizSms)
    {
        startPage();
        List<PubBizSms> list = pubBizSmsService.selectPubBizSmsList(pubBizSms);
        return getDataTable(list);
    }*/
    /*
     * 发送短信
     * */
    @Log(title = "消息发送", businessType = BusinessType.INSERT)
    @PostMapping("/newThree")
    @ResponseBody
    public void addSave(PubBizPresms pubBizPresms)
    {
        if (StringUtils.isNotEmpty(pubBizPresms.getTelephone())){
            if (pubBizPresms.getTelephone() !="" && pubBizPresms.getTelephone() != null){

                //添加UUID  id
                pubBizPresms.setPubBizPresmsId(UUID.getUUIDStr());

                //添加的时候时间自动添加   发送时间
                Date nowDate = DateUtils.getNowDate();
                String nowTime = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, nowDate);
                pubBizPresms.setInspectTime(nowTime);
                //获取发送人
                OgUser ogUser = ShiroUtils.getOgUser();
                String userName = ogUser.getUsername();
                pubBizPresms.setName(userName);
                //创建人id
                String pId = ogUser.getpId();
                pubBizPresms.setCreaterId(pId);

                pubBizPresmsService.insertPubBizPresms(pubBizPresms);


                //查询id
                /*PubBizPresms pubBizPresms = pubBizPresmsService.selectPubBizPresmsById(pubBizSms.getPubBizPresmsId());
                String pubBizPresmsId = pubBizPresms.getPubBizPresmsId();
                pubBizSms.setPubBizPresmsId(pubBizPresmsId);*/

                pubBizSmsService.findPreSmsAndSend(pubBizPresms);

            }else {
                System.out.println("短信发送失败");
            }
        }
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
