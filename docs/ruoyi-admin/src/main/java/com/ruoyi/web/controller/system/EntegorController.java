package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.service.IAutoItsmResultmsgService;
import com.ruoyi.activiti.service.IFmBizScriptService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.AutoItsmResultmsg;
import com.ruoyi.common.core.domain.entity.FmBizScript;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 接收自动化执行返回结果
 *
 * @author 14735
 */
@Controller
@RequestMapping("/entegor")
public class EntegorController extends BaseController {

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private IAutoItsmResultmsgService resultmsgService;

    @Autowired
    private IFmBizScriptService fmBizScriptService;

    @Autowired
    private IOgUserService ogUserService;
    private final String VERSION_PUBLIC = "sus";
    private final String FMBIZ = "fmbiz";
    private final String SyncUser = "syncUser";

    private final Logger logger = LoggerFactory.getLogger(EntegorController.class);

    @PostMapping("/receive")
    public void receive(HttpServletRequest request, HttpServletResponse response)
    {
        String action = request.getParameter("action");
        logger.debug("--------ITSM接收到自动化平台回调，回调方法为："+action);
        // sus代表版本发布
        if (VERSION_PUBLIC.equals(action)) {
            String jsonStr = convertParam(request);
            logger.debug("--------ITSM接收到自动化平台回调，回调参数为："+jsonStr);
            if (StringUtils.isNotEmpty(jsonStr)) {
                Map map = JSON.parseObject(jsonStr);
                String status = (String) map.get("ISTATE");
                String businessNo = (String) map.get("IVERSION");
                String changeReason = (String) map.get("IRUNINSNAME");
                // 判断是否已经保存信息
                AutoItsmResultmsg m = new AutoItsmResultmsg();
                m.setBusinessNo(businessNo);
                m.setChangeReason(changeReason);
                Map<String, Object> pa = m.getParams();
                String[] stateCodeArray = {"1", "2", "3"};
                pa.put("stateCodeList", Arrays.asList(stateCodeArray));
                List<AutoItsmResultmsg> msgList = resultmsgService.selectAutoItsmResultmsgList(m);
                if (!CollectionUtils.isEmpty(msgList)) {
                    // 该结果信息已经保存，不能重复保存数据
                    return;
                }

                AutoItsmResultmsg msg = new AutoItsmResultmsg();
                if (StringUtils.isNotEmpty(businessNo)) {
                    VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByNo(businessNo);
                    msg.setBusinessId(vmBizInfo.getVersionInfoId());
                    msg.setBusinessNo(businessNo);
                }
                msg.setChangeReason(changeReason);
                msg.setStatus(status);
                msg.setStartTime((String) map.get("ISTARTTIME"));
                msg.setEndTime((String) map.get("IENDTIME"));
                msg.setResultMsg(String.valueOf(map.get("stateCode")));
                msg.setResultId(UUID.getUUIDStr());
                logger.debug("-----保存自动化结果信息表auto_itsm_resultmsg----" + msg.toString());
                resultmsgService.insertAutoItsmResultmsg(msg);
                logger.debug("版本单号:" + businessNo + ",变更原因说明:" + map.get("IRUNINSNAME") + ",接收自动化数据成功");
            }
        } else if (FMBIZ.equals(action)) {//如果是脚本服务化走下边
            returnExecutorResult(request, response);
        }else if (SyncUser.equals(action)) {//网络自动化同步用户数据
            String jsonStr = convertParam(request);
            if (StringUtils.isNotEmpty(jsonStr)) {
                Map map = JSON.parseObject(jsonStr);
                String time = (String)map.get("updateTime");
                OgUser ogUser = new OgUser();
                if(StringUtils.isNotEmpty(time)){
                    ogUser.setUpdateTime(time);
                }
                List<OgUser> list = ogUserService.selectAllUserPersonList(ogUser);
                String json = JSON.toJSONString(list);
                response.setContentType("text/json; charset=utf-8");
                response.addHeader("Content-type", "application/json");
                response.addHeader("Accept", "application/json");
                try {
                    response.getOutputStream().write(json.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 自动化返回结果信息使用UTF-8编码
     * @param request
     * @return
     */
    public String convertParam(HttpServletRequest request) {
        StringBuilder sbInfo;
        try {
            ServletInputStream sis = request.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(sis);
            sbInfo = new StringBuilder();
            byte[] contents = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(contents)) != -1) {
                sbInfo.append(new String(contents, 0, bytesRead,"UTF-8"));
            }
            logger.debug(sbInfo.toString());
            bis.close();
            sis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("参数转换异常", e);
        }
        logger.debug("------UTF-8转码结果------" + sbInfo.toString());
        return sbInfo.toString();
    }

    public void returnExecutorResult(HttpServletRequest request, HttpServletResponse response) {
        String jsonStr = convertParam(request);
            if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = JSON.parseObject(jsonStr);
            String fbsId = (String) map.get("fbsid");//根据脚本记录ID更新记录表
            String executorStatus = (String) map.get("executorStatus");//状态
            String executorResult = (String) map.get("executorResult");//返回结果
            String executorEndTime = (String) map.get("executorEndTime");//执行结束时间
            FmBizScript fs = new FmBizScript();
            fs.setFlowId(fbsId);
            List<FmBizScript> fbs = fmBizScriptService.selectFmBizScriptList(fs);
            if (!fbs.isEmpty()) {
                fbs.get(0).setResultStatus(executorStatus);
                fbs.get(0).setExecutorResult(executorResult);
                fbs.get(0).setExecutorEndTime(executorEndTime);
                fmBizScriptService.updateFmBizScript(fbs.get(0));
            }
        }
    }
}
