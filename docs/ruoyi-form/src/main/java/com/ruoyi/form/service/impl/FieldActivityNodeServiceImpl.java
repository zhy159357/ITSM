package com.ruoyi.form.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignFormVersionMapper;
import com.ruoyi.form.mapper.FieldActivityNodeMapper;
import com.ruoyi.form.mapper.FormStepStatusMapper;
import com.ruoyi.form.service.FieldActivityNodeService;
import com.ruoyi.form.service.FormStatusActivityNodeService;
import com.ruoyi.form.service.FormStepStatusService;
import com.ruoyi.framework.config.MybatisPlusConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FieldActivityNodeServiceImpl extends ServiceImpl<FieldActivityNodeMapper, FieldActivityNode> implements FieldActivityNodeService{

    private static final String bizTablePrefix = "design_biz";

    private static final String approveTemplate = "approve_template";

    @Resource
    CustomerFormMapper customerFormMapper;

    @Resource
    FormStatusActivityNodeService formStatusActivityNodeService;

    @Resource
    FormStepStatusService formStepStatusService;

    @Resource
    FormStepStatusMapper formStepStatusMapper;

    @Resource
    DesignFormVersionMapper designFormVersionMapper;

    @Override
    public AjaxResult getFormField(String code,String sid) {

        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        //根据表单版本ID获取当前表单当前版本的字段
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        if (CollectionUtil.isEmpty(formFieldInfos)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getDesc());
        }
        //如果字段状态映射表中没有该节点配置表单的信息时  批量新增
        List<FieldActivityNode> list = this.list(Wrappers.<FieldActivityNode>query().eq(FieldActivityNode.COL_FORM_VERSION_ID, Long.valueOf(currentTableInfo.get("id"))).eq(FieldActivityNode.COL_ACTIVITY_NODE_ID,sid));
        if (CollectionUtil.isEmpty(list)){
            formFieldInfos.stream().forEach(a->{
                FieldActivityNode fieldActivityNode = FieldActivityNode.builder().fieldName(a.get("name"))
                        .activityNodeId(sid)
                        .fieldDesc(a.get("description"))
                        .formVersionId(Long.valueOf(currentTableInfo.get("id")))
                        .build();
                list.add(fieldActivityNode);
            });
            this.saveBatch(list);
            List<FieldActivityNode> fieldActivityNodes = this.list(Wrappers.<FieldActivityNode>query().eq(FieldActivityNode.COL_FORM_VERSION_ID, Long.valueOf(currentTableInfo.get("id"))));
            return AjaxResult.success(fieldActivityNodes);
        }
        return AjaxResult.success(list);
    }


    @Override
    public AjaxResult setFormFieldShowStatus(List<FieldActivityNode> fieldActivityNodes) {
        this.updateBatchById(fieldActivityNodes);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult setFormStatus(String code, List<FormStatusActivityNode> formStatusActivityNodes) {
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        formStatusActivityNodes.forEach(a->{
            a.setFormVersionId(currentTableInfo.get("id"));
        });

        formStatusActivityNodeService.saveOrUpdateBatch(formStatusActivityNodes);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult setFormStep(String code, List<FormStepStatus> formStepStatuses) {
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        formStepStatuses.forEach(a->{
            a.setFormVersionId(currentTableInfo.get("id"));
        });

        formStepStatusService.saveOrUpdate(formStepStatuses.get(0),Wrappers.<FormStepStatus>update()
                .eq(FormStepStatus.COL_FORM_VERSION_ID,currentTableInfo.get("id"))
                .eq(FormStepStatus.COL_BIZ_STATUS_NAME,formStepStatuses.get(0).getBizStatusName())
        );
        return AjaxResult.success();
    }

    @Override
    public AjaxResult getCurrentNodeFormStep(String code, String bizStatusName) {
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        FormStepStatus currentNodeFormStep = formStepStatusService.getOne(Wrappers.<FormStepStatus>query().eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).eq(FormStepStatus.COL_BIZ_STATUS_NAME, bizStatusName));
        return AjaxResult.success(currentNodeFormStep);
    }

    @Override
    public AjaxResult getCurrentNodeFormBizStatus(String code, String nodeId) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FieldActivityNode.COL_ACTIVITY_NODE_ID, nodeId).eq(FieldActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
        return AjaxResult.success(formStatusActivityNode);
    }


    @Override
    public AjaxResult getApproveTemplate() {
        dynamicTableName(approveTemplate);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id,json,template_name"));
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(list);

    }

    /**
     * 配置同步
     * @desc  改功能只为了让开发方便   不对表单版本字段是否变更、流程节点状态是否变更进行考虑
     * @param code 流程key||表单code
     * @param type 同步类型  1、同步表单字段在节点的状态  2、同步节点的状态  3、同步节点状态步骤
     * @return
     */
    @Override
    public AjaxResult synchronize(String code,String type) {
        //获取当前表单所有的版本ID
        List<DesignFormVersion> formVersions = designFormVersionMapper.selectList(Wrappers.<DesignFormVersion>query().eq(DesignFormVersion.COL_CODE, String.format("%s_%s", bizTablePrefix, code)).orderByDesc(DesignFormVersion.COL_ID));
        List<Long> formVersionIds = formVersions.stream().map(DesignFormVersion::getId).collect(Collectors.toList());
        //获取当前已发布的版本
        DesignFormVersion designFormVersion = formVersions.stream().filter(a -> 1==a.getIsCurrent()).findFirst().orElse(null);
        if (ObjectUtil.isEmpty(designFormVersion)){
            throw new BusinessException("该表单当前没有已发布的版本~ 请发布后再次尝试");
        }
        switch (type){
            case "1":
                List<FieldActivityNode> fieldActivityNodes = this.list(Wrappers.<FieldActivityNode>query().select("DISTINCT(form_version_id)").in(FieldActivityNode.COL_FORM_VERSION_ID, formVersionIds).orderByDesc(FieldActivityNode.COL_FORM_VERSION_ID));
                if (CollectionUtil.isEmpty(fieldActivityNodes)|| Objects.equals(fieldActivityNodes.get(0).getFormVersionId(), designFormVersion.getId())){
                    return AjaxResult.warn("当前流程无法自动同步字段，请手动配置~");
                }else {
                    List<FieldActivityNode> sourceList = this.list(Wrappers.<FieldActivityNode>query().eq(FieldActivityNode.COL_FORM_VERSION_ID, fieldActivityNodes.get(0).getFormVersionId()).orderByDesc(FieldActivityNode.COL_FORM_VERSION_ID));
                    sourceList.forEach(a->{
                        a.setFormVersionId(designFormVersion.getId());
                        a.setId(null);
                    });
                    this.saveBatch(sourceList);
                    return AjaxResult.success("同步字段成功");
                }
            case "2":
                List<FormStatusActivityNode> formStatusActivityNodes = formStatusActivityNodeService.list(Wrappers.<FormStatusActivityNode>query().select("DISTINCT(form_version_id)").in(FormStatusActivityNode.COL_FORM_VERSION_ID, formVersionIds).orderByDesc(FormStatusActivityNode.COL_FORM_VERSION_ID));
                if (CollectionUtil.isEmpty(formStatusActivityNodes)||Objects.equals(formStatusActivityNodes.get(0).getFormVersionId(), designFormVersion.getId())){
                    return AjaxResult.warn("当前流程无法自动同步状态，请手动配置~");
                }else {
                    List<FormStatusActivityNode> sourceList = formStatusActivityNodeService.list(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_FORM_VERSION_ID, formStatusActivityNodes.get(0).getFormVersionId()).orderByDesc(FormStatusActivityNode.COL_FORM_VERSION_ID));
                    sourceList.forEach(a->{
                        a.setId(null);
                        a.setFormVersionId(designFormVersion.getId());
                    });
                    formStatusActivityNodeService.saveBatch(sourceList);
                    return AjaxResult.success("同步状态成功");
                }
            case "3":
                List<FormStepStatus> formStepStatuses = formStepStatusService.list(Wrappers.<FormStepStatus>query().select("DISTINCT(form_version_id)").in(FormStepStatus.COL_FORM_VERSION_ID, formVersionIds).orderByDesc(FormStepStatus.COL_FORM_VERSION_ID));
                if (CollectionUtil.isEmpty(formStepStatuses)||Objects.equals(formStepStatuses.get(0).getFormVersionId(), designFormVersion.getId())){
                    return AjaxResult.warn("当前流程无法自动同步步骤，请手动配置~");
                }else {
                    List<FormStepStatus> sourceList = formStepStatusService.list(Wrappers.<FormStepStatus>query().eq(FormStepStatus.COL_FORM_VERSION_ID, formStepStatuses.get(0).getFormVersionId()).orderByDesc(FormStepStatus.COL_FORM_VERSION_ID));
                    sourceList.forEach(a->{
                        a.setId(null);
                        a.setFormVersionId(designFormVersion.getId());
                    });
                    formStepStatusService.saveBatch(sourceList);
                    return AjaxResult.success("同步步骤成功");
                }
        }
        return AjaxResult.error("同步错误~  请联系管理员");
    }

    /**
     * 构造动态表名
     *
     * @param code 表名
     */
    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(code);
    }
}
