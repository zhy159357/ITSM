package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.OgPersonDuty;
import com.ruoyi.activiti.mapper.OgPersonDutyMapper;
import com.ruoyi.activiti.service.IOgPersonDutyService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysWorkService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控值班Service业务层处理
 *
 * @author ruoyi
 * @date 2021-06-25
 */
@Service
public class OgPersonDutyServiceImpl implements IOgPersonDutyService {
    private static final Logger log = LoggerFactory.getLogger(OgPersonDutyServiceImpl.class);

    @Autowired
    private OgPersonDutyMapper ogPersonDutyMapper;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private ActivitiCommServiceImpl activitiCommService;
    @Autowired
    private ISysWorkService iSysWorkService;

    /**
     * 查询监控值班
     *
     * @param dutyId 监控值班ID
     * @return 监控值班
     */
    @Override
    public OgPersonDuty selectOgPersonDutyById(String dutyId) {
        return ogPersonDutyMapper.selectOgPersonDutyById(dutyId);
    }

    /**
     * 查询监控值班列表
     *
     * @param ogPersonDuty 监控值班
     * @return 监控值班
     */
    @Override
    public List<OgPersonDuty> selectOgPersonDutyList(OgPersonDuty ogPersonDuty) {
        return ogPersonDutyMapper.selectOgPersonDutyList(ogPersonDuty);
    }

    /**
     * 新增监控值班
     *
     * @param ogPersonDuty 监控值班
     * @return 结果
     */
    @Override
    public int insertOgPersonDuty(OgPersonDuty ogPersonDuty) {
        return ogPersonDutyMapper.insertOgPersonDuty(ogPersonDuty);
    }

    /**
     * 修改监控值班
     *
     * @param ogPersonDuty 监控值班
     * @return 结果
     */
    @Override
    public int updateOgPersonDuty(OgPersonDuty ogPersonDuty) {
        return ogPersonDutyMapper.updateOgPersonDuty(ogPersonDuty);
    }

    /**
     * 删除监控值班对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public Map deleteOgPersonDutyByIds(String ids, String userId) {
        Map<String, Object> map = new HashMap<>();
        OgPerson person = personService.selectOgPersonById(userId);
        // 主动退出值班组查询当前操作人还有几个组，若只有一个组需要查询名下是否还有监控事件单
        OgPersonDuty selectDuty = new OgPersonDuty();
        selectDuty.setUserId(userId);
        int dutyCount = ogPersonDutyMapper.selectDutyCountByGroupOrMonitor(selectDuty);
        OgPersonDuty ogPersonDuty = selectOgPersonDutyById(ids);
        //应用二线 A类系统
        boolean  isSysA=isClassificationA(ogPersonDuty);
        if(currentUserTaskEmpty(userId)){
            int duty = ogPersonDutyMapper.deleteOgPersonDutyByIds(Convert.toStrArray(ids));
            map.put("flag", false);
            map.put("dutyDelete", duty > 0);
            return map;
        }
        if(dutyCount <= 1){
            if(!currentUserTaskEmpty(userId)){
                map.put("flag", true);
                map.put("message", "当前操作人【" + person.getpName() + "】名下有未处理的监控事件单，请处理完所有的监控事件单后再退出该值班组！");
                return map;
            }
        }
        boolean count = selectDutyCount(ogPersonDuty);
        // count = true标识值班组只有一个人不可退出
        if (count) {
            map.put("flag", true);
            map.put("message", "当前值班组有且只有一个人，不可退出该值班组！");
            return map;
        }
        int duty = ogPersonDutyMapper.deleteOgPersonDutyByIds(Convert.toStrArray(ids));
        map.put("flag", false);
        map.put("dutyDelete", duty > 0);
        return map;
    }
    /**
     * 删除监控值班对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgPersonDutyByIds(String ids)
    {
        return ogPersonDutyMapper.deleteOgPersonDutyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除监控值班信息
     *
     * @param dutyId 监控值班ID
     * @return 结果
     */
    @Override
    public int deleteOgPersonDutyById(String dutyId) {
        return ogPersonDutyMapper.deleteOgPersonDutyById(dutyId);
    }

    /**
     * 判断组内是否只有一个人  二线A类 及 监控条线
     * @param duty
     * @return
     */
    @Override
    public boolean selectDutyCount(OgPersonDuty duty) {
        if(duty==null){
            return false;
        }
        boolean groupFlag = false;
        boolean monitorFlag = false;
        OgPersonDuty ogPersonDuty = new OgPersonDuty();
        if (!"5".equals(duty.getDutyType())&&!"6".equals(duty.getDutyType())) {
            ogPersonDuty.setGroupId(duty.getGroupId());
            int group = ogPersonDutyMapper.selectDutyCountByGroupOrMonitor(ogPersonDuty);
            if (group > 1) {
                groupFlag = false;
            } else {
                groupFlag = true;
            }
        } else {
            ogPersonDuty.setMonitorTypeAccountId(duty.getMonitorTypeAccountId());
            int monitor = ogPersonDutyMapper.selectDutyCountByGroupOrMonitor(ogPersonDuty);
            if (monitor > 1) {
                monitorFlag = false;
            } else {
                //如果二线A类组内只有一个人
                if(isClassificationA(duty)){
                    monitorFlag = true;
                }
            }
        }
        return groupFlag || monitorFlag;
    }

    @Override
    public boolean judgeDutyRepeat(OgPersonDuty ogPersonDuty) {
        OgPersonDuty duty = new OgPersonDuty();
        if (!"5".equals(ogPersonDuty.getDutyType())&&!"6".equals(ogPersonDuty.getDutyType())) {
            duty.setUserId(ogPersonDuty.getUserId());
            duty.setGroupId(ogPersonDuty.getGroupId());
            int group = ogPersonDutyMapper.selectDutyCountByGroupOrMonitor(duty);
            if (group > 0) {
                return true;
            }
        } else {
            duty.setUserId(ogPersonDuty.getUserId());
            duty.setMonitorTypeAccountId(ogPersonDuty.getMonitorTypeAccountId());
            int monitor = ogPersonDutyMapper.selectDutyCountByGroupOrMonitor(duty);
            if (monitor > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查userId对应的任务
     *
     * @param userId
     * @return
     */
    @Override
    public Map checkDutyPerson(String userId) {
        OgPerson person = personService.selectOgPersonById(userId);
        Map<String, Object> map = new HashMap<>();
        OgPersonDuty duty = new OgPersonDuty();
        duty.setUserId(userId);

        List<OgPersonDuty> dutyList = selectOgPersonDutyList(duty);
        if (CollectionUtils.isEmpty(dutyList)) {
            map.put("flag", false);
            log.debug("--------------当前操作人：" + person.getpName() + "未查询到相关值班组信息，可以签退----------------");
            return map;
        }
        map.put("flag", true);
        map.put("message", "已领取的工作组尚未移除！");
        return map;
       /* StringBuilder message = new StringBuilder();
        // 记录当前登录人退出值班组成功的情况
        StringBuilder success = new StringBuilder();
        // 记录当前登录人退出值班组失败的情况
        StringBuilder fail = new StringBuilder();
        boolean checkFlag = false;
        boolean isNotA=true;
        for (OgPersonDuty d : dutyList) {
            boolean count = selectDutyCount(d);
            // 当前值班组只有一个人，不可退出值班组，发短信提示，当前值班组有多人，则退出值班组
            if (count) {
                // 发送短信
                String setSmsText = "当前操作人【" + person.getpName() + "】您好，您所在的值班组名称【" + d.getDutyName() + "】,有且只有您一人，签退时不退出值班组。";
                vmBizInfoService.sendSms(setSmsText, person);
                fail.append(d.getDutyName() + ",");
                checkFlag = true;
            } else {
         *//*       int rows = deleteOgPersonDutyById(d.getDutyId());
                if (rows > 0) {
                    log.debug("--------------当前操作人：" + person.getpName() + "退出值班组：" + d.getDutyName() + "成功----------------");
                }*//*
                success.append(d.getDutyName() + ",");
            }
           // isNotA=isNotClassificationA(d);
        }
        boolean taskIsEmpty=currentUserTaskEmpty(userId);
        if(taskIsEmpty){
            map.put("flag", false);
            map.put("message", "签退成功");
            return map;
        }
        // 判断当前操作人名下的监控事件单
        if(!taskIsEmpty){
            map.put("flag", true);
            map.put("message", "当前操作人【" + person.getpName() + "】名下有未处理的监控事件单，请处理完所有的监控事件单后再点击【签退】按钮！");
            return map;
        }
        if(checkFlag){
            message.append("当前操作值班组");
            if(success.length() > 0){
                success = success.deleteCharAt(success.length() - 1);
                message.append("【" + success.toString() + "】退出成功，");
            }
            if(fail.length() > 0){
                fail = fail.deleteCharAt(fail.length() - 1);
                message.append("【" + fail.toString() + "】退出失败，失败原因为值班组内仅剩一人不允许退出。");
            }
        }*/

    }

    /**
     * 判断当前操作人是否有监控事件单任务
     * @param userId
     * @return
     */
    public boolean currentUserTaskEmpty(String userId){
        List<Task> tasks = activitiCommService.allTasks(userId, "FmYx");
        if(CollectionUtils.isEmpty(tasks)){
            return true;
        }
        return false;
    }
    /**
     * 条件查询所有人员和工作组
     * @param ogPersonDuty
     * @return
     */
    @Override
    public List<OgPersonDuty> selectOgPersonDutyGroupList(OgPersonDuty ogPersonDuty){
        return ogPersonDutyMapper.selectOgPersonDutyGroupList(ogPersonDuty);
    }

    /**
     *判断值班人 是否为应用二线  A类系统  满足返回true 不满足返回flase
     * @param ogPersonDuty
     * @return
     */
    public boolean isClassificationA(OgPersonDuty ogPersonDuty){
        if(ogPersonDuty==null){
            return false;
        }
        String groupId=ogPersonDuty.getGroupId();
        if(StringUtils.isEmpty(groupId)){
            return  false;
        }
        OgGroup ogGroup = iSysWorkService.selectOgGroupById(groupId);
        if(ogGroup==null){
            return  false;
        }
        String groupType=ogGroup.getGroupType();
        OgSys sys=ogGroup.getOgSys();
        String sysType=sys.getSysType();
        if("2".equals(groupType)&&"1".equals(sysType)){
            return  true;
        }
        return false;
    }
}
