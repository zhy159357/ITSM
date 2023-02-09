package com.ruoyi.form.monitor;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.DesignBizJsonData;
import com.ruoyi.form.domain.OperationDetails;
import com.ruoyi.form.domain.RelationLog;
import com.ruoyi.form.enums.ChmPJEnum;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.IRelationLogService;
import com.ruoyi.form.service.OperationDetailsService;
import com.ruoyi.form.service.impl.ChmApiServiceImpl;
import com.ruoyi.form.service.impl.CustomerFormServiceImpl;
import com.ruoyi.form.service.impl.DesignBizJsonDataServiceImpl;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.form.domain.DesignBizChm;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.form.mapper.DesignBizChmMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("chmMonitor")
@Transactional
public class chmMonitor implements JavaDelegate, ExecutionListener {
    private ChmApiServiceImpl chmApiService= SpringUtils.getBean(ChmApiServiceImpl.class);

    private CustomerFormServiceImpl customerFormService = SpringUtils.getBean(CustomerFormServiceImpl.class);
    private DesignBizChmMapper designBizChmMapper= SpringUtils.getBean(DesignBizChmMapper.class);
    private TaskService taskService=SpringUtils.getBean(TaskService.class);
    private OperationDetailsService operationDetailsService=SpringUtils.getBean(OperationDetailsService.class);
    private CustomerFormMapper customerFormMapper=SpringUtils.getBean(CustomerFormMapper.class);
    private IRelationLogService iRelationLogService=SpringUtils.getBean(IRelationLogService.class);
    private DesignBizJsonDataServiceImpl designBizJsonDataService=SpringUtils.getBean(DesignBizJsonDataServiceImpl.class);
    protected final Logger logger = LoggerFactory.getLogger(chmMonitor.class);
    @Override
    public void execute(DelegateExecution execution) {
    }

    @Override
    public void notify(DelegateExecution execution) {
    }

    /**
     * 关闭硬件报障单
     * @param execution
     */
    public void closechm(DelegateExecution execution){
        DelegateExecution parent=execution.getParent();
        String businessKey= (String) parent.getProcessInstanceBusinessKey();
        String[] sl=businessKey.split("_");
        dynamicTableName(WorkOrderInformation.incident.getCode());
        Map<String, Object> incidentFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                .select(RedundancyFieldEnum.extra1.name)
                .eq("id", sl[2])).get(0);
        if(incidentFieldMap==null){
            return;
        }
        String incidentCode=incidentFieldMap.get(RedundancyFieldEnum.extra1.name)==null?"":incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString();
        if(StringUtils.isNotEmpty(incidentCode)){
            QueryWrapper<RelationLog> wrapper=Wrappers.<RelationLog>query()//
                    .select("*")
                    .eq("request_no",incidentCode);
            List<RelationLog> relationLogList=  iRelationLogService.list(wrapper);
            if(CollectionUtil.isNotEmpty(relationLogList)){
                for(RelationLog relationLog:relationLogList){
                    QueryWrapper<DesignBizChm> wrapper2=Wrappers.<DesignBizChm>query()//
                            .select("*")
                            .eq(RedundancyFieldEnum.extra1.name,relationLog.getRelationNo());
                    DesignBizChm designBizChm=  designBizChmMapper.selectOne(wrapper2);
                    if(designBizChm!=null){
                        Task task=taskService.createTaskQuery().processInstanceId(designBizChm.getInstanceId()).singleResult();
                        if(task!=null){
                            designBizChm.setStatus("已关闭");
                            OgUser ogUser=CustomerBizInterceptor.currentUserThread.get();
                            designBizChm.setRealDealUser(ogUser.getPname());
                            designBizChm.setCurrentHandlerId(ogUser.getPname());
                            designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
                            CustomerFormContent customerFormContent=new CustomerFormContent();
                            customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
                            Map<String,Object> mapFiles=new HashMap<>();
                            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                                    .eq(DesignBizJsonData.COL_BIZ_ID, designBizChm.getId())
                                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
                            String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
                            customerFormContent.setDataJson(dataJson);
                            mapFiles.put("id",designBizChm.getId());
                            mapFiles.put("status","已关闭");
                            customerFormContent.setFields(mapFiles);
                            Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
                            taskService.complete(task.getId());
                            OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("发起的事件单<"+incidentCode+">已关闭,硬件报障单关闭").build();
                            operationDetailsService.save(details);
                            logger.info("========由硬件报障单：<"+designBizChm.getExtra1()+">发起的事件<"+incidentCode+">已被接单，硬件事件单关闭");
                        }
                    }
                }
            }
        }
    }
    /**
     * 合胜开单
     * @param execution
     */
    public void createOpenApi(DelegateExecution execution) throws Exception{
        OgUser ogUser=CustomerBizInterceptor.currentUserThread.get();
        DelegateExecution parent=execution.getParent();
        String businessKey= (String) parent.getProcessInstanceBusinessKey();
        logger.info("========调用OPENAPI接口发起合胜硬件保障单："+businessKey);
        JSONObject js= chmApiService.createHeshengOpenApi(businessKey.split("_")[2],ogUser,businessKey);
        if(StringUtils.isEmpty(js)||!"0000".equals(js.getString("code"))){
            throw new BusinessException("厂商接口调用失败！");
        }

    }
    /**
     * 创建人更新 （it服务台处理人处理时间 厂商使用）
     */
    public void updateCreatedUser(DelegateExecution execution){
        OgUser ogUser=CustomerBizInterceptor.currentUserThread.get();
        DelegateExecution parent=execution.getParent();
        String businessKey= (String) parent.getProcessInstanceBusinessKey();
        String[] sl=businessKey.split("_");
        String Id=sl[2];
        DesignBizChm designBizChm=new DesignBizChm();
        designBizChm.setId(Long.valueOf(Id));
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sf.format(new Date());
        designBizChm.setItDealTime(date);
        designBizChm.setItDealUser(ogUser.getPname());
        designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
        DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, Id).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", "chm")));
        Map<String, Object> jsonMap = new HashMap<>();
        String jsonData = designBizJsonData1.getJsonData();
        Map<String,Object> map=new HashMap<>();
        map.put("arrive_time",date);
        String jd=VueDataJsonUtil.setJsonValue(jsonData,map);
        designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", "chm")).eq(DesignBizJsonData.COL_BIZ_ID, Id).set("json_data", jd));
    }
    /**
     * 完成时间更新
     */
    public void updateCompeleteTime(DelegateExecution execution){
        try {
            DelegateExecution parent=execution.getParent();
            String businessKey= (String) parent.getProcessInstanceBusinessKey();
            String[] sl=businessKey.split("_");
            String Id=sl[2];
            DesignBizChm designBizChm=new DesignBizChm();
            designBizChm.setId(Long.valueOf(Id));
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date=sf.format(new Date());
            designBizChm.setCompleteTime(date);
            designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
            DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, Id).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", "chm")));
            Map<String, Object> jsonMap = new HashMap<>();
            String jsonData = designBizJsonData1.getJsonData();
            dynamicTableName(WorkOrderInformation.chm_task.getCode());
            Map<String,Object> map=new HashMap<>();
            map.put("complete_time",date);
            String jd=VueDataJsonUtil.setJsonValue(jsonData,map);
            designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", "chm")).eq(DesignBizJsonData.COL_BIZ_ID, Id).set("json_data", jd));
        }catch (Exception e){
            logger.error("==============硬件报障结束，更新完成时间报错============");
            e.printStackTrace();
        }
    }
   /**
     * 接单时间更新
     */
    public void updateOrderTime(DelegateExecution execution){

        DelegateExecution parent=execution.getParent();
        String businessKey= (String) parent.getProcessInstanceBusinessKey();
        String[] sl=businessKey.split("_");
        String Id=sl[2];
        DesignBizChm designBizChm=new DesignBizChm();
        designBizChm.setId(Long.valueOf(Id));
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sf.format(new Date());
        designBizChm.setOrderTime(date);
        designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
        DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, Id).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", "chm")));
        Map<String, Object> jsonMap = new HashMap<>();
        String jsonData = designBizJsonData1.getJsonData();
        Map<String,Object> map=new HashMap<>();
        map.put("order_time",date);
        String jd=VueDataJsonUtil.setJsonValue(jsonData,map);
        designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", "chm")).eq(DesignBizJsonData.COL_BIZ_ID, Id).set("json_data", jd));
    }
    /**
     * 更新复核时间
     */
    public void updateFuheTime(DelegateExecution execution){
        DelegateExecution parent=execution.getParent();
        String businessKey= (String) parent.getProcessInstanceBusinessKey();
        String[] sl=businessKey.split("_");
        String Id=sl[2];
        DesignBizChm designBizChm=new DesignBizChm();
        designBizChm.setId(Long.valueOf(Id));
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sf.format(new Date());
        designBizChm.setFuHeTime(date);
        designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
    }
    /**
     * 更新硬件报障实际处理人
     */
    public void updateRealDealUser(DelegateExecution execution){
        OgUser ogUser=CustomerBizInterceptor.currentUserThread.get();
        DelegateExecution parent=execution.getParent();
        String businessKey= (String) parent.getProcessInstanceBusinessKey();
        String[] sl=businessKey.split("_");
        String Id=sl[2];
        DesignBizChm designBizChm=new DesignBizChm();
        designBizChm.setId(Long.valueOf(Id));
        designBizChm.setRealDealUser(ogUser.getPname());
        designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
    }

    /**
     * 构造动态表名
     *
     * @param code 表名
     */
    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
    }

}
