package com.ruoyi.activiti.listener;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.SjFetchSingle;
import com.ruoyi.activiti.service.ISjFetchSingleService;
import com.ruoyi.activiti.service.impl.SjFetchSingleServiceImpl;
import com.ruoyi.activiti.utils.RestTemplateUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据提取监听类
 * @Author
 */
@Service("DataFetchProcessListener")
public class DataFetchProcessListener  implements JavaDelegate, ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(VersionPublicProcessListener.class);

    //数据提取URL
    @Value("${bigData.url}")
    private String url;

    @Override
    public void notify(DelegateExecution delegateExecution) {

    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

    }


    @Transactional(rollbackFor = Exception.class)
    public void handleFetch(DelegateExecution execution){
        ISjFetchSingleService sjFetchSingleService = SpringUtils.getBean(SjFetchSingleServiceImpl.class);
        RestTemplateUtil restTemplateUtil = SpringUtils.getBean(RestTemplateUtil.class);
        log.info("###############数据提取单监听任务开始################");
        DelegateExecution parent = execution.getParent();
        String businessId = parent.getProcessInstanceBusinessKey();
        Map<String, Object> variable = parent.getVariables();

        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(businessId);

        //当所有的子任务完成进行输出
        String[] split = sjFetchSingle.getSysName().split(",");
        if(split.length>1){
            //1.如果当前是大数据的被分派 需要调用对应的大数据接口
            if("01".equals(sjFetchSingle.getSourceType())){
                //调用接口
                //String url = "http://22.246.192.198:7001/default/com.fjpsbc.drms.process.notice.setOpinionFromMgr.biz.ext";//生产
                //String url = "http://20.152.0.100:9000/default/com.fjpsbc.drms.process.notice.setOpinionFromMgr.biz.ext";//测试
                //String url = "http://20.200.27.83:7001/default/com.fjpsbc.drms.dataReqFlow.dataFlow.setOpinionFromMgr.biz.ext";
                Map<String,String> map = new HashMap<>();
                map.put("processId",sjFetchSingle.getProcessid());//流程实例id
                map.put("businessNumber",sjFetchSingle.getBusinessNumber());//需求单号
                map.put("businessNo",sjFetchSingle.getFetchNo());//业务单号
                map.put("opinion","1");//意见（1-处理完成，2-需求取消）
                JSONObject json = (JSONObject) JSON.toJSON(map);
                Map<String, String> map1 = new HashMap<>();
                map1.put("json", JSONUtils.toJSONString(json));
                String responseResult = restTemplateUtil.sendPost(url, map1);
                log.info("大数据返回值" + responseResult);
                Map msgMap = JSON.parseObject(responseResult);
                String ret = (String)msgMap.get("ret");
                Map mssageMap = JSON.parseObject(ret);
                String code = (String)mssageMap.get("code");
                String message = (String)mssageMap.get("message");
//                if ("0000".equals(code)) {
//                    //更新事件单状态
//                    SjFetchSingle fetchSingle = new SjFetchSingle();
//                    fetchSingle.setFetchId(sjFetchSingle.getFetchId());
//                    fetchSingle.setFetchState("04");
//                    fetchSingle.setFetchStateText("已关闭");
//                    sjFetchSingleService.updateSjFetchSingleStatus(fetchSingle);
//                }
            }
            //更新事件单状态
            SjFetchSingle fetchSingle = new SjFetchSingle();
            fetchSingle.setFetchId(sjFetchSingle.getFetchId());
            fetchSingle.setFetchState("04");
            fetchSingle.setFetchStateText("已关闭");
            sjFetchSingleService.updateSjFetchSingleStatus(fetchSingle);
        }

        log.info("###############数据提取单监听任务结束################");
        //2.如果当前是OA的被分派 不需要操作进行线下反馈
    }




}
