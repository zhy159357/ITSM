package com.ruoyi.quartz.task;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.service.IAutoItsmFastfileService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.AutoItsmFastfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangchao
 * @date 2021-10-26
 * 新核心对接一体化运维平台介质联动
 */
@Component("operationMediaLinkageTask")
public class OperationMediaLinkageTask {

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private IAutoItsmFastfileService fastfileService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${operationMediaLinkage.url}")
    private String url;

    @Value("${operationMediaLinkage.productLine}")
    private String productLine;

    private final String platformFlag = "ideal";

    private static final Logger log = LoggerFactory.getLogger(OperationMediaLinkageTask.class);

    public void sendMsgToCore() {
        if (taskLockManager.lock("operationMediaLinkageTask")) {
            long start = System.currentTimeMillis();
            try {
                log.debug("############新核心对接一体化运维平台介质联动定时任务执行开始#############");
                this.sendMsg();
                log.debug("############新核心对接一体化运维平台介质联动定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("operationMediaLinkageTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【operationMediaLinkageTask】执行总时长【" + (end - start) + "】毫秒");
        } else {
            log.debug("operationMediaLinkageTask - 任务已有其他服务执行...");
        }
    }

    private void sendMsg() {
        AutoItsmFastfile autoItsmFastfile = new AutoItsmFastfile();
        autoItsmFastfile.setSendStatus("0");// 发送状态是0
        autoItsmFastfile.setHfCenterStatus("1");// 合肥下载完成
        autoItsmFastfile.setYzCenterStatus("1");// 亦庄下载完成
        List<AutoItsmFastfile> list = fastfileService.selectAutoItsmFastfileList(autoItsmFastfile);
        if (!CollectionUtils.isEmpty(list)) {
            // 具体组装发送逻辑
            for (AutoItsmFastfile fast : list) {
                String status = vmBizInfoService.selectStatusByVersionInfoNo(fast.getIitsmno());
                // 只有已发布状态的版本单才能发送新核心通知
                if (VersionStatusConstants.VERSION_STATUS_12.equals(status)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("systemCode", fast.getIsystemabb());// 系统编码
                    map.put("processID", fast.getIitsmno());// 版本单号
                    map.put("platform", platformFlag);// 平台标识，固定值"ideal"
                    map.put("product", productLine);// 产品线，测试环境暂时写"sit1",生产环境写"PROD"
                    ResponseEntity<String> responseEntity = null;
                    try {
                        log.debug("----调用核心对接一体化运维平台介质联动接口开始，参数信息为params=" + map.toString() + "----");
                        responseEntity = restTemplate.postForEntity(url, map, String.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("-----调用核心对接一体化运维平台介质联动接口失败，失败原因" + e.getMessage());
                        return;
                    }
                    if (responseEntity != null && responseEntity.getStatusCodeValue() == 200) {
                        log.debug("----版本单号:" + fast.getIitsmno() + ",系统简称:" + fast.getIsystemabb() + "的版本包发送新核心接口调用成功-----");
                        AutoItsmFastfile fastFile = new AutoItsmFastfile();
                        fastFile.setSendStatus("1");
                        fastFile.setUuid(fast.getUuid());
                        int rows = fastfileService.updateAutoItsmFastfile(fastFile);
                        if (rows > 0) {
                            log.debug("----版本单号:" + fast.getIitsmno() + ",系统简称:" + fast.getIsystemabb() + "的记录更新数据库发送状态成功-----");
                        }
                    } else {
                        log.debug("----版本单号:" + fast.getIitsmno() + ",系统简称:" + fast.getIsystemabb() + "的版本包发送新核心接口调用失败-----");
                    }
                } else {
                    log.debug("-----版本单号versionInfoNo=[" + fast.getIitsmno() + "]的版本单,当前状态是versionStatus=[" + status + "],不执行发送新核心通知的逻辑-----");
                }
            }
        }
    }

}
