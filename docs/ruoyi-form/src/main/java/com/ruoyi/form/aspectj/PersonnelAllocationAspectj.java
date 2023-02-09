package com.ruoyi.form.aspectj;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.ChangeDeptPersonEntity;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.ProblemConstant.*;
import static com.ruoyi.form.util.GeneralQueryUtil.dynamicTableName;

/**
 * @ClassName PersonnelAllocationAspectj
 * @Description
 * @Author hecaili
 * @Date 2022/8/8 14:36
 */
@Aspect
@Component
@Slf4j
public class PersonnelAllocationAspectj {
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Resource
    CustomerFormMapper customerFormMapper;
    @Resource
    IChangePersonService iChangePersonService;
    @Pointcut("@annotation(com.ruoyi.common.annotation.PersonnelAllocation)")
    public void personnelAllocationPointCut() {

    }

    @Around("personnelAllocationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] param = joinPoint.getArgs();
        StringBuilder stringBuilder=new StringBuilder("增强方法的入参值：");
        Arrays.stream(param).forEach(a->{
            stringBuilder.append(a+",");
        });
        log.info("增强方法的入参值："+stringBuilder.toString());

        //继续执行方法
        Object proceed = joinPoint.proceed();
        log.info("目标方法执行完后得到返回值并进行相应对象转化：");
        AjaxResult result = (AjaxResult) proceed;
        buildFormJsonInfo(result, param);
        return result;
    }

    /**
     * 根据不同业务添加需要自动反显的字段值
     *
     * @param result
     */
    private void buildFormJsonInfo(AjaxResult result, Object[] param) {
        String code = (String) param[0];
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            Map<String, Object> map = (Map<String, Object>) result.get("data");
            Map<String, Object> formJsonInfoList = (Map<String, Object>) map.get("formJsonInfo");
            String json = (String)formJsonInfoList.get("json");
            // 查询发起人信息
            String userId=CustomerBizInterceptor.currentUserThread.get().getUserId();
            OgPerson op = iOgPersonService.selectOgPersonById(userId);
            List<OgGroup> listGroup=iSysWorkService.selectGroupByUserId(userId);
            Boolean isAdmin=false;
            for(OgGroup ogp:listGroup){
                if (Arrays.asList(CustomerFlowConstants.PROBLEM_ADMIN,
                        CustomerFlowConstants.TINGJING_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.CHENDU_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.NANJIN_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.SUZHOU_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.NINGBO_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.BEIJIN_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.SHENZHENG_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.HANZHOU_BRANCH_PROBLEM_ADMIN
                ).contains(ogp.getGroupId())) {
                    isAdmin=true;
                    break;
                }
            }
            List<Map<String, String>> personList = iOgPersonService.selectOgPersons(null);
            // 将问题单发起人放置到json中
            String s = VueDataJsonUtil.analysisDataJsonSelect(json, personList, userId);
            // 如果当前登录人非管理员则问题发起人禁用
            if (isAdmin) {
                s = VueDataJsonUtil.setDisable(s, false);
            }
            // 判断是总行或分行人员,如果分行则需要特殊处理
            List<PubParaValue> bankList = pubParaValueService.selectPubParaValueById("60a02cbc224344749c4d1d0ec65f6d5a");
            // 获取到各分行id
            List<String> orgidList = bankList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList());
            String orgId = op.getOrgId();
            OgOrg ogOrg = iSysDeptService.selectDeptById(orgId);
            if (orgidList.contains(ogOrg.getParentId())) {
                orgId = ogOrg.getParentId();
            }

            QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dept_id", orgId);
            List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
            // 问题单发起部室
            Map<String, Object> oriDeptMap = new HashMap<>();
            Map<String, Object> oriDMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(changeDeptPersonEntityList)) {
                String deptPersonId = changeDeptPersonEntityList.get(0).getDeptPerson();
                OgPerson opdept = iOgPersonService.selectOgPersonById(deptPersonId);
                if (opdept != null) {
                    // 设置问题发起部室
                    JSONArray json1 = JSON.parseArray(s);
                    JSONObject o = (JSONObject) json1.get(0);
                    oriDMap.put("label", changeDeptPersonEntityList.get(0).getOrgName());
                    oriDMap.put("value", changeDeptPersonEntityList.get(0).getOrgId());
                    List<Map<String, Object>> list = Lists.newArrayList();
                    list.add(oriDMap);
                    VueDataJsonUtil.analysisDataJsonForProblemName(o, list, ORI_DEP_ID);
                    s = json1.toString();

                    // 设置问题发起部室
                    JSONArray json2 = JSON.parseArray(s);
                    JSONObject o2 = (JSONObject) json2.get(0);
                    oriDeptMap.put("label", opdept.getpName());
                    oriDeptMap.put("value", opdept.getpId());
                    List<Map<String, Object>> list2 = Lists.newArrayList();
                    list2.add(oriDeptMap);
                    VueDataJsonUtil.analysisDataJsonForProblemName(o2, list2, ORI_DEP_MANAGER_ID);
                    s = json2.toString();
                }
            }
            // 设置新的值
            formJsonInfoList.put("json", s);
            map.put("formJsonInfo", formJsonInfoList);
            result.put("data", map);
            CustomerBizInterceptor.currentUserThread.remove();
        }
        if(code.equals(WorkOrderInformation.problem_task.getCode())){
            String problemNo = (String) param[1];
            Map<String, Object> map = (Map<String, Object>) result.get("data");
            Map<String, Object> formJsonInfoList = (Map<String, Object>) map.get("formJsonInfo");
            String json = (String)formJsonInfoList.get("json");
            // 查询问题单已关联过的其他子任务编号
            dynamicTableName(code);
            JSONArray jsonArray = null;
            List<Map<String, Object>> taskNoList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq(PROBLEM_NO, problemNo));
            List<Map<String, Object>> nos = Lists.newArrayList();
            if (!org.springframework.util.CollectionUtils.isEmpty(taskNoList)) {
                for (Map<String, Object> no : taskNoList) {
                    Map<String, Object> mapPeople = new HashMap<>();
                    mapPeople.put("value", no.get(RedundancyFieldEnum.extra1.name).toString());
                    mapPeople.put("label", no.get(RedundancyFieldEnum.extra1.name).toString());
                    nos.add(mapPeople);
                }
                jsonArray = JSON.parseArray(json);
                JSONObject object = (JSONObject) jsonArray.get(0);

                VueDataJsonUtil.analysisDataJsonForProblemName(object, nos,"pre_task_num");
            } else {
                Map<String, Object> mapPeople = new HashMap<>();
                mapPeople.put("value", "无");
                mapPeople.put("label", "无");
                nos.add(mapPeople);
                jsonArray = JSON.parseArray(json);
                JSONObject object = (JSONObject) jsonArray.get(0);
                VueDataJsonUtil.analysisDataJsonForProblemName(object, nos,"pre_task_num");
            }
            // 查询发起人信息
//            String userId=CustomerBizInterceptor.currentUserThread.get().getUserId();
//            OgPerson op = iOgPersonService.selectOgPersonById(userId);
//            Map<String,Object> tMap=new HashMap<>();
//            tMap.put(op.getpName(),op.getpId());
//            tMap.put("default",op.getpId());
//            Map<String,Object> mp=new HashMap<>();
//            mp.put("originator_id",tMap);
            // 人员id转换成名称
//            String s1 = VueDataJsonUtil.analysisDataJsonSelect(!CollectionUtils.isEmpty(jsonArray) ? jsonArray.toJSONString() : json, mp);
            // 设置新的值
            formJsonInfoList.put("json", !CollectionUtils.isEmpty(jsonArray) ? jsonArray.toJSONString() : json);
            map.put("formJsonInfo", formJsonInfoList);
            result.put("data", map);
            CustomerBizInterceptor.currentUserThread.remove();
        }
        if(code.equals(WorkOrderInformation.chm_task.getCode())){
            String userId=CustomerBizInterceptor.currentUserThread.get().getUserId();
            OgPerson op = iOgPersonService.selectOgPersonById(userId);
            Map<String,Object> tMap=new HashMap<>();
            tMap.put(op.getpName(),op.getpId());
            tMap.put("default",op.getpId());
            Map<String,Object> mp=new HashMap<>();
            mp.put("created_by",tMap);
            Map<String, Object> map = (Map<String, Object>) result.get("data");
            Map<String, Object> formJsonInfoList = (Map<String, Object>) map.get("formJsonInfo");
            String json = (String)formJsonInfoList.get("json");
            String s1 = VueDataJsonUtil.analysisDataJsonSelect(json, mp);
            // 设置新的值
            formJsonInfoList.put("json", s1);
            map.put("formJsonInfo", formJsonInfoList);
            result.put("data", map);
            CustomerBizInterceptor.currentUserThread.remove();
        }
    }

}
