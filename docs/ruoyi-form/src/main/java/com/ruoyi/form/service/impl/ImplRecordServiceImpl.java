package com.ruoyi.form.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.GzFaultReport;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.ImplRecordMapper;
import com.ruoyi.form.domain.ImplRecord;
import com.ruoyi.form.service.IImplRecordService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-10-04
 */
@Service
public class ImplRecordServiceImpl implements IImplRecordService 
{
    @Autowired
    private ImplRecordMapper implRecordMapper;
    @Autowired
    Base base;
    @Autowired
    private    CustomerFormMapper customerFormMapper;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    IOgUserService ogUserService;
    /**
     * 查询【请填写功能名称】
     * 
       * @return 【请填写功能名称】
     */
    @Override
    public ImplRecord selectImplRecordById(Long id)
    {
        return implRecordMapper.selectImplRecordById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param implRecord 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ImplRecord> selectImplRecordList(ImplRecord implRecord)
    {
        return implRecordMapper.selectImplRecordList(implRecord);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param implRecord 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertImplRecord(ImplRecord implRecord)
    {
        return implRecordMapper.insertImplRecord(implRecord);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param implRecord 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateImplRecord(ImplRecord implRecord)
    {
        return implRecordMapper.updateImplRecord(implRecord);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteImplRecordByIds(String ids)
    {
        return implRecordMapper.deleteImplRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteImplRecordById(Long id)
    {
        return implRecordMapper.deleteImplRecordById(id);
    }


    public List<Map<String,Object>> getChangeTaskListByStatusAndCurrentUserId(String currentUserId,ChangeTaskStatusEnum changeTaskStatusEnum){
        ImplRecord record = new ImplRecord();
        record.setUserid(currentUserId);
        record.setTaskStatus(changeTaskStatusEnum.getName());
        List<ImplRecord> list = implRecordMapper.selectImplRecordList(record);
        List<Map<String,Object>> resultList = new ArrayList<>();
        list.forEach(p->{
            Map<String,Object> param = new HashMap<>();
            Map<String,String> query = new HashMap<>();
            param.put("*","");
            query.put(ChangeTaskFieldEnum.EXTRA1.getName(),p.getChangeTaskNo());
            Map<String,Object> map = base.selectMap(ChangeTableNameEnum.CHANGE_TASK,param,query);
            resultList.add(map);
        });
        return resultList;
    }
    public List<Map<String,Object>> getChangeTaskListByStatusAndCurrentUserIdAll(String name){
        ImplRecord record = new ImplRecord();
        record.setEffectUser(name);
        List<Map<String,Object>> list = implRecordMapper.selectImplRecordListAll(record);
        return list;
    }
    public  List<ImplRecord> selectImplRecordListVo(ImplRecord implRecord){
      //  Map<String, Object> reMap = new HashMap<>();
        if(StringUtils.isNotEmpty(implRecord.getHoldUser())){
            OgPerson ogPerson=new OgPerson();
            ogPerson.setpName(implRecord.getHoldUser());
            List<OgPerson> ogPersonlist=iOgPersonService.selectList(ogPerson);
            if(!CollectionUtils.isEmpty(ogPersonlist)){
                StringBuffer holduser=new StringBuffer();
                for(OgPerson og:ogPersonlist){
                    holduser.append(og.getpId()+",");
                }
                implRecord.setHoldUser(holduser.substring(0,holduser.length()-1).toString());
            }
        }
        //任务预审人
        if(StringUtils.isNotEmpty(implRecord.getCheckUser())){
            OgUser user = ogUserService.selectOgUserByUserId(implRecord.getCheckUser());
            OgPerson ogPerson= iOgPersonService.selectOgPersonById(implRecord.getCheckUser());
            implRecord.setCheckUser(ogPerson.getpName() + "(" + user.getUsername() + ")");
        }
        //投产支持人
        if(StringUtils.isNotEmpty(implRecord.getHoldUser())){
            OgUser user = ogUserService.selectOgUserByUserId(implRecord.getHoldUser());
            OgPerson ogPerson= iOgPersonService.selectOgPersonById(implRecord.getHoldUser());
            implRecord.setHoldUser(ogPerson.getpName() + "(" + user.getUsername() + ")");
        }
//        //投产实施人
//        if(StringUtils.isNotEmpty(implRecord.getEffectUser())){
//            OgUser user = ogUserService.selectOgUserByUserId(implRecord.getEffectUser());
//            OgPerson ogPerson= iOgPersonService.selectOgPersonById(implRecord.getEffectUser());
//            implRecord.setEffectUser(ogPerson.getpName() + "(" + user.getUsername() + ")");
//        }
        implRecord.setFlag("0");
        List<ImplRecord> list=implRecordMapper.selectImplRecordListVo(implRecord);
        list.forEach(impl -> {
            String changeType=impl.getChangeType();
            impl.setChangeType(changeTypeEnum.getInfo(changeType));
        });
       // reMap.put("data",list);
        return list;
    }

    public void issueImplRecordListVo(String[] implRecord){
        implRecordMapper.issueImplRecordListVo(implRecord);
    }
    public List<String> selectAllEffectUser(){return implRecordMapper.selectAllEffectUser();}
    public void  addReMap(List<Map<String, Object>> topList, List<Map<String, Object>> allList, List<Map<String, Object>> list, Map<String, Object> reMap,Object version,String userName){
        Map<String,Object> map=new HashMap<>();
        for(int i=0;i<list.size();i++){
            Map<String,Object> p=list.get(i);
            if(i==0){
                map.put("effectUser",userName);
                map.put("changTaskNo1",p.get("changeTaskNo"));
                map.put("status1","执行中");
            }
            if(i==1){
                map.put("changTaskNo2",p.get("changeTaskNo"));
                map.put("status2","准备中");
            }
//             * 准备中：waitImpl
//         * 实施中:impling
//         * 已完成：closed
//         * 延迟: delay
            String recodeStatus = p.get("recodeStatus").toString();
            if (recodeStatus.equals(ChangeTaskStatusEnum.valueOf("preApprovalPassed").getName())
                    || recodeStatus.equals(ChangeTaskStatusEnum.valueOf("waitImpl").getName())
                    || recodeStatus.equals(ChangeTaskStatusEnum.valueOf("reviewed").getName())
                    || recodeStatus.equals(ChangeTaskStatusEnum.valueOf("planReviewed").getName())
                    ||recodeStatus.equals(ChangeTaskStatusEnum.valueOf("preApprovaling").getName())) {
                p.put("status", "waitImpl");
            }
            if (recodeStatus.equals(ChangeTaskStatusEnum.valueOf("canceled").getName()) || recodeStatus.equals(ChangeTaskStatusEnum.valueOf("closed").getName())) {
                p.put("status", "closed");
            }
            if (recodeStatus.equals(ChangeTaskStatusEnum.valueOf("impling").getName())) {
                p.put("status", "impling");
            }
            if (recodeStatus.equals(ChangeTaskStatusEnum.valueOf("delay").getName())) {
                p.put("status", "delay");
            }
        }
        topList.add(map);
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userName", userName );
            userMap.put("userId", "");
            userMap.put("version", version);
            userMap.put("taskList", list);
            allList.add(userMap);
        }
        reMap.put("data", allList);
        reMap.put("topData",topList);
    }
    public void updateImplRecordChangeTaskNo(ImplRecord implRecord){
        implRecordMapper.updateImplRecordChangeTaskNo(implRecord);
    }
}
