package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.DutySchedulingMapper;
import com.ruoyi.activiti.mapper.DutyTypeinfoMapper;
import com.ruoyi.activiti.mapper.DutyViewMapper;
import com.ruoyi.activiti.service.IDutySchedulingService;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ruoyi.common.utils.DateUtils.parseDate;

/**
 * 排班信息Service业务层处理
 * @author dayong_sun
 * @date 2020-12-06
 */
@Service
public class DutySchedulingServiceImpl implements IDutySchedulingService {

    @Autowired
    private DutySchedulingMapper dutySchedulingMapper;
    @Autowired
    private DutyTypeinfoMapper dutyTypeinfoMapper;
    @Autowired
    private DutyViewMapper dutyViewMapper;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    private final String SUCCESS = "1";// 启用状态

    /**
     * 修改值班日志
     */
    @Override
    public int editCheckLog(String id, String content, String updateTime) {
        return dutySchedulingMapper.editCheckLog(id,content,updateTime);
    }

    /**
     * 删除值班日志
     */
    @Override
    public AjaxResult deleteDutyLogByIds(String ids) {
        return dutySchedulingMapper.deleteDutyLogByIds(Convert.toStrArray(ids)) > 0 ? AjaxResult.success() : AjaxResult.error();
    }
    /**
     * 查询值班日志
     */
    @Override
    public List<DutyLog> searchLogForUserId(DutyLog dutyLog) {
        return dutySchedulingMapper.searchLogForUserId(dutyLog);
    }

    /**
     * 新增值班日志
     * @param dutyLog 值班日志信息
     * @return 排班信息
     */
    @Override
    public int addLog(DutyLog dutyLog) {
        dutyLog.setId(UUID.getUUIDStr().replaceAll("-",""));
        return dutySchedulingMapper.addLog(dutyLog);
    }

    /**
     * 查询排班信息列表
     * @return 排班信息
     */
    @Override
    public int selectDutyPersonList(String mobilePhone, String endTime) throws ParseException {
        int isHave = dutySchedulingMapper.selectDutyPersonList(mobilePhone, endTime);
        if(isHave <= 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date =sdf.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            date = calendar.getTime();
            isHave = dutySchedulingMapper.selectDutyPersonList(mobilePhone, sdf.format(date));
        }
        return isHave;
    }

    /**
     * 查询排班信息列表
     * @param dutyScheduling 排班信息
     * @return 排班信息
     */
    @Override
    public List<DutyScheduling> selectDutySchedulingList(DutyScheduling dutyScheduling) {
        return dutySchedulingMapper.selectDutySchedulingList(dutyScheduling);
    }

    /**
     * 查询排班信息
     * @param schedulingId 排班信息ID
     * @return 排班信息
     */
    @Override
    public DutyScheduling selectDutySchedulingById(String schedulingId) {
        return dutySchedulingMapper.selectDutySchedulingById(schedulingId);
    }

    /**
     * 新增排班信息
     * @param dutyScheduling 排班信息
     * @return 结果
     */
    @Override
    public String addCheckSave(DutyScheduling dutyScheduling) {
        // 赋值主键id
        String msg = "success";
        DutyVersion dutyVersion = new DutyVersion();
        dutyVersion.setTypeinfoId(dutyScheduling.getTypeinfoId());
        dutyVersion.setDutyDate(dutyScheduling.getDutyDate().substring(0, 7));
        DutyScheduling ds;
        if("mysql".equals(dbType)){
            ds = dutySchedulingMapper.selectDutyNumberMysql(dutyVersion);
        }else{
            ds = dutySchedulingMapper.selectDutyNumber(dutyVersion);
        }
        if (null == ds) {
            msg = "该类别还没有与版本信息进行绑定";
        } else {
            if (dutyScheduling.getPid().contains(",") && "0".equals(ds.getDutyNumber())) {
                msg = "该类别编码应该绑定单人进行值班";
            }
        }
        dutyScheduling.setMobilephone(null);
        DutyScheduling du;
        if("mysql".equals(dbType)){
            du = dutySchedulingMapper.selectSchedulingMysql(dutyScheduling);
        }else{
            du = dutySchedulingMapper.selectScheduling(dutyScheduling);
        }
        if (null != du) {
            if (null == dutyScheduling.getSchedulingId() || (null != dutyScheduling.getSchedulingId() && !dutyScheduling.getSchedulingId().equals(du.getSchedulingId()))) {
                msg = "该类别下值班时间重复，请重新输入";
            }
        }
        return msg;
    }

    /**
     * 新增排班信息
     * @param schedulingId 排班信息
     * @return 结果
     */
    @Override
    public int editSchedulingCheck(String schedulingId) {
        int result = 0;
        DutyScheduling scheduling = dutySchedulingMapper.selectDutySchedulingById(schedulingId);
        if (null != scheduling) {
            String dutyDate = scheduling.getDutyDate();
            String nowDate = DateUtils.getDate();
            Date date1 = DateUtils.dateTime(DateUtils.YYYY_MM_DD, dutyDate);
            Date date2 = DateUtils.dateTime(DateUtils.YYYY_MM_DD, nowDate);
            int compareTo = date1.compareTo(date2);
            if (compareTo <= 0) {
                result = 1;
            }
        }
        return result;
    }

    /**
     * 新增排班信息
     * @param dutyScheduling 排班信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertDutyScheduling(DutyScheduling dutyScheduling) {
        // 赋值主键id
        dutyScheduling.setSchedulingId(UUID.getUUIDStr());
        DutyScheduling scheduling;
        if("mysql".equals(dbType)){
            scheduling = dutySchedulingMapper.selectPhoneByPidMysql(Convert.toStrArray(dutyScheduling.getPid()));
        }else{
            scheduling = dutySchedulingMapper.selectPhoneByPid(Convert.toStrArray(dutyScheduling.getPid()));
        }
        dutyScheduling.setPname(null == scheduling ? "" : scheduling.getPname());
        if (StringUtils.isEmpty(dutyScheduling.getTypeNo()) && StringUtils.isNotEmpty(dutyScheduling.getTypeinfoId())) {
            DutyTypeinfo dutyTypeinfo = dutyTypeinfoMapper.selectDutyTypeinfoById(dutyScheduling.getTypeinfoId());
            dutyScheduling.setTypeNo(dutyTypeinfo.getTypeNo());
        }
        return dutySchedulingMapper.insertDutyScheduling(dutyScheduling);
    }

    /**
     * 修改排班信息
     * @param dutyScheduling 排班信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDutyScheduling(DutyScheduling dutyScheduling) {

        DutyScheduling scheduling;
        if("mysql".equals(dbType)){
            scheduling = dutySchedulingMapper.selectPhoneByPidMysql(Convert.toStrArray(dutyScheduling.getPid()));
        }else{
            scheduling = dutySchedulingMapper.selectPhoneByPid(Convert.toStrArray(dutyScheduling.getPid()));
        }
        dutyScheduling.setPname(null == scheduling ? "" : scheduling.getPname());
        if (StringUtils.isEmpty(dutyScheduling.getTypeNo()) && StringUtils.isNotEmpty(dutyScheduling.getTypeinfoId())) {
            DutyTypeinfo dutyTypeinfo = dutyTypeinfoMapper.selectDutyTypeinfoById(dutyScheduling.getTypeinfoId());
            dutyScheduling.setTypeNo(dutyTypeinfo.getTypeNo());
        }

        return dutySchedulingMapper.updateDutyScheduling(dutyScheduling);
    }

    /**
     * 删除排班信息对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public AjaxResult deleteDutySchedulingByIds(String ids) {
        List<DutyScheduling> schedulings = dutySchedulingMapper.selectDutySchedulingByIds(Convert.toStrArray(ids));
        for (DutyScheduling scheduling : schedulings) {
            if (DateUtils.dateTime(DateUtils.YYYY_MM_DD, scheduling.getDutyDate()).before(DateUtils.getNowDate())) {
                return AjaxResult.error("当前排版信息已超时，不可删除");
            }
        }

        return dutySchedulingMapper.deleteDutySchedulingByIds(Convert.toStrArray(ids)) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 根据pid查询联系方式
     * @param pids 需要数据ID
     * @return 结果
     */
    @Override
    public String selectPhoneByPid(String pids) {
        DutyScheduling scheduling;
        if("mysql".equals(dbType)){
            scheduling = dutySchedulingMapper.selectPhoneByPidMysql(Convert.toStrArray(pids));
        }else{
            scheduling = dutySchedulingMapper.selectPhoneByPid(Convert.toStrArray(pids));
        }
        return null == scheduling ? "" : scheduling.getMobilephone();
    }

    /**
     * 删除排班信息信息
     * @param schedulingId 排班信息ID
     * @return 结果
     */
    @Override
    public int deleteDutySchedulingById(String schedulingId) {
        return dutySchedulingMapper.deleteDutySchedulingById(schedulingId);
    }

    /**
     * 校验类别编码是否唯一
     * @param dutyTypeinfo 角色信息
     * @return 结果
     */
    @Override
    public String checkTypeNoUnique(DutyTypeinfo dutyTypeinfo) {
        String typeNo = StringUtils.isNull(dutyTypeinfo.getTypeNo()) ? "" : dutyTypeinfo.getTypeNo();
        DutyTypeinfo info = dutySchedulingMapper.checkTypeNoUnique(dutyTypeinfo.getTypeNo());
        if (StringUtils.isNotNull(info) && info.getTypeNo().equals(typeNo)) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 查询值班人员
     * @param ogPerson 排班信息ID
     * @return 排班信息
     */
    @Override
    public List<OgPerson> selectDutyUserList(OgPerson ogPerson) {
        return dutySchedulingMapper.selectDutyUserList(ogPerson);
    }

    /**
     * 查询值班人员
     * @param orgId 机构ID
     * @return 排班信息
     */
    @Override
    public List<DutyPerson> selectOgPersonList(String orgId) {
        return dutySchedulingMapper.selectOgPersonList(orgId);
    }

    /**
     * 导出岗位为数据中心人员，数据中心处长，厂商人员('0002','0010','0011')人员
     * @return 人员信息
     */
    @Override
    public List<DutyPerson> exportOgPersonList() {
        return dutySchedulingMapper.exportOgPersonList();
    }

    /**
     * 校验月份是否唯一
     * @return 版本信息
     */
    @Override
    public String checkDutyDateUnique(DutyScheduling dutyScheduling) {
        String dutyDate = StringUtils.isNull(dutyScheduling.getDutyDate()) ? "" : dutyScheduling.getDutyDate();
        String[] month = dutyDate.split(",");
        for (String str : month) {
            DutyScheduling info = dutySchedulingMapper.checkDutyDateUnique(str);
            if (StringUtils.isNotNull(info) && info.getDutyDate().contains(str)) {
                return UserConstants.ROLE_KEY_NOT_UNIQUE;
            }
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 导入用户数据
     * @param schedulingList 用户数据列表
     * @param operName       操作用户
     * @return 结果
     */
    @Override
    public String importScheduling(List<DutyScheduling> schedulingList, String operName) {
        Map map = importList(schedulingList);
        String message = map.get("message").toString();
        if ("1".equals(map.get("flag"))) {
            message = map.get("message").toString();
        } else {
            List<DutyScheduling> list = (List) map.get("list");
            dutySchedulingMapper.insertSchedulings(list);
        }
        return message;
    }

    public Map importList(List<DutyScheduling> schedulingList) {
        Map<String, Object> map = new HashMap<>();
        List<DutyScheduling> list = new ArrayList<>();
        if (StringUtils.isNull(schedulingList) || schedulingList.size() == 0) {
            map.put("flag", "1");
            map.put("message", "导入用户数据不能为空");
            return map;
        }
        int successNum = 0;
        int failureNum = 1;
        int rowsNum = 1;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (DutyScheduling scheduling : schedulingList) {
            rowsNum++;
            try {
                String pname = scheduling.getPname();
                String mobilephone = scheduling.getMobilephone();
                String dutyDate = scheduling.getDutyDate();
                String typeNo = scheduling.getTypeNo();
                if (StringUtils.isEmpty(pname)) {
                    failureNum++;
                    failureMsg.append("<br/>第" + rowsNum + "行，姓名 " + pname + " 不能为空");
                    break;
                }
                if (StringUtils.isEmpty(mobilephone)) {
                    failureNum++;
                    failureMsg.append("<br/>第" + rowsNum + "行，联系电话 " + mobilephone + " 不能为空");
                    break;
                }
                if (StringUtils.isEmpty(dutyDate)) {
                    failureNum++;
                    failureMsg.append("<br/>第" + rowsNum + "行，值班时间 " + dutyDate + " 不能为空");
                    break;
                }
                if (StringUtils.isEmpty(typeNo)) {
                    failureNum++;
                    failureMsg.append("<br/>第" + rowsNum + "行，类别编码 " + typeNo + " 不能为空");
                    break;
                }
                DutyTypeinfo info = dutyTypeinfoMapper.checkTypeNoUnique(typeNo);
                if(null!=info){
                    scheduling.setTypeinfoId(info.getTypeinfoId());
                }
                //根据联系电话查询信息
                if(mobilephone.contains(",")){
                    String namePhone = "<br/>第" + rowsNum + "行，姓名和联系电话 " + mobilephone + " 不匹配";
                    int mint = mobilephone.split(",").length;
                    if(pname.contains(",")){
                        if(pname.split(",").length != mint) {
                            failureNum++;
                            failureMsg.append(namePhone);
                        }else{
                            String [] mphone = mobilephone.split(",");
                            String [] nname = pname.split(",");
                            boolean flag = true;
                            String dps = "";
                            if(mint>10){
                                failureNum++;
                                failureMsg.append("<br/>第" + rowsNum + "行，值班人员超出10人");
                                break;
                            }else{
                                for(int k=0;k<mint;k++) {
                                    DutyScheduling mds = new DutyScheduling();
                                    mds.setMobilephone(mphone[k]);
                                    DutyPerson dp = dutySchedulingMapper.selectPersonByMobilephone(mds);
                                    if (StringUtils.isNull(dp)) {
                                        failureNum++;
                                        failureMsg.append("<br/>第" + rowsNum + "行，联系电话 " + mobilephone + " 不存在");
                                        break;
                                    } else if (!dp.getPname().equals(nname[k])) {
                                        failureNum++;
                                        failureMsg.append("<br/>第" + rowsNum + "行，姓名和联系电话 " + mobilephone + " 不匹配");
                                        break;
                                    } else {
                                        dps = dp.getPid() + ",";
                                    }
                                }
                            }
                            dps = dps.substring(0,dps.length() - 1);
                            if(flag){
                                DutyVersion dv = dutySchedulingMapper.selectVersionByDate(dutyDate.substring(0, 7));
                                if (StringUtils.isNull(dv)) {
                                    failureNum++;
                                    failureMsg.append("<br/>第" + rowsNum + "行，值班时间" + dutyDate + " 未绑定版本信息，无法导入");
                                } else {
                                    //根据类别编码查询信息
                                    DutyVersion dutyVersion = new DutyVersion();
                                    dutyVersion.setVersionId(dv.getVersionId());
                                    dutyVersion.setTypeNo(typeNo);
                                    DutyScheduling ds = dutySchedulingMapper.selectTypeinfoByTypeNo(dutyVersion);
                                    if (StringUtils.isNull(ds)) {
                                        failureNum++;
                                        failureMsg.append("<br/>第" + rowsNum + "行，类别编码 " + typeNo + " 不存在");
                                    } else {
                                        DutyScheduling du;
                                        if("mysql".equals(dbType)){
                                            du = dutySchedulingMapper.selectSchedulingMysql(scheduling);
                                        }else{
                                            du = dutySchedulingMapper.selectScheduling(scheduling);
                                        }
                                        if (StringUtils.isNotNull(du)) {
                                            successNum ++;
                                            list.add(du);
                                            dutySchedulingMapper.deleteDutyScheduling(scheduling);
//                                            failureNum++;
//                                            failureMsg.append("<br/>" + failureNum + "、值班时间重复，无法导入");
                                        } else {
                                            long nowDate = parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM)).getTime();
                                            long dDate = DateUtils.dateTime(DateUtils.YYYY_MM, dutyDate).getTime();
                                            if (nowDate > dDate) {
                                                failureNum++;
                                                failureMsg.append("<br/>第" + rowsNum + "行，当前月份大于值班月份，无法导入");
                                            } else {
                                                if (StringUtils.isNull(list) || list.size() == 0) {
                                                    scheduling.setSchedulingId(UUID.getUUIDStr());
                                                    scheduling.setPid(dps);
                                                    if(selectTypeinfoEndTypeNo(typeNo,dutyDate)){
                                                        list.add(scheduling);
                                                    }else{
                                                        failureNum++;
                                                        failureMsg.append("<br/>第" + rowsNum + "行，当前类别编码不可导入");
                                                    }
                                                } else {
                                                    int slint = 0;
                                                    for (DutyScheduling sl : list) {
                                                        if (sl.getDutyDate().equals(scheduling.getDutyDate()) && sl.getMobilephone().equals(scheduling.getMobilephone()) && sl.getTypeNo().equals(scheduling.getTypeNo())) {
                                                            slint++;
                                                        }
                                                    }
                                                    if (0 == slint) {
                                                        scheduling.setSchedulingId(UUID.getUUIDStr());
                                                        scheduling.setPid(dps);
                                                        if(selectTypeinfoEndTypeNo(typeNo,dutyDate)){
                                                            list.add(scheduling);
                                                        }else{
                                                            failureNum++;
                                                            failureMsg.append("<br/>第" + rowsNum + "行，当前类别编码不可导入");
                                                        }
                                                    } else {
                                                        failureNum++;
                                                        failureMsg.append("<br/>第" + rowsNum + "行，导入的数据重复，无法导入");
                                                    }
                                                }
                                                successNum++;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        failureNum++;
                        failureMsg.append(namePhone);
                    }
                }else {
                    DutyPerson dp = dutySchedulingMapper.selectPersonByMobilephone(scheduling);
                    if (StringUtils.isNull(dp)) {
                        failureNum++;
                        failureMsg.append("<br/>第" + rowsNum + "行，联系电话 " + mobilephone + " 不存在");
                    } else if (!dp.getPname().equals(pname)) {
                        failureNum++;
                        failureMsg.append("<br/>第" + rowsNum + "行，姓名和联系电话 " + mobilephone + " 不匹配");
                    } else {
                        DutyVersion dv = dutySchedulingMapper.selectVersionByDate(dutyDate.substring(0, 7));
                        if (StringUtils.isNull(dv)) {
                            failureNum++;
                            failureMsg.append("<br/>第" + rowsNum + "行，值班时间" + dutyDate + " 未绑定版本信息，无法导入");
                        } else {
                            //根据类别编码查询
                            DutyVersion dutyVersion = new DutyVersion();
                            dutyVersion.setVersionId(dv.getVersionId());
                            dutyVersion.setTypeNo(typeNo);
                            DutyScheduling ds = dutySchedulingMapper.selectTypeinfoByTypeNo(dutyVersion);
                            if (StringUtils.isNull(ds)) {
                                failureNum++;
                                failureMsg.append("<br/>第" + rowsNum + "行，类别编码 " + typeNo + " 不存在");
                            } else {
                                DutyScheduling du;
                                if("mysql".equals(dbType)){
                                    du = dutySchedulingMapper.selectSchedulingMysql(scheduling);
                                }else{
                                    du = dutySchedulingMapper.selectScheduling(scheduling);
                                }
                                if (StringUtils.isNotNull(du)) {
                                    successNum ++;
                                    list.add(du);
                                    dutySchedulingMapper.deleteDutyScheduling(scheduling);
//                                    failureNum++;
//                                    failureMsg.append("<br/>" + failureNum + "、值班时间重复，无法导入");
                                } else {
                                    long nowDate = parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM)).getTime();
                                    long dDate = DateUtils.dateTime(DateUtils.YYYY_MM, dutyDate).getTime();
                                    if (nowDate > dDate) {
                                        failureNum++;
                                        failureMsg.append("<br/>第" + rowsNum + "行，当前月份大于值班月份，无法导入");
                                    } else {
                                        if (StringUtils.isNull(list) || list.size() == 0) {
                                            scheduling.setSchedulingId(UUID.getUUIDStr());
                                            scheduling.setPid(dp.getPid());
                                            if(selectTypeinfoEndTypeNo(typeNo,dutyDate)){
                                                list.add(scheduling);
                                            }else{
                                                failureNum++;
                                                failureMsg.append("<br/>第" + rowsNum + "行，当前类别编码不可导入");
                                            }
                                        } else {
                                            int slint = 0;
                                            for (DutyScheduling sl : list) {
                                                if (sl.getDutyDate().equals(scheduling.getDutyDate()) && sl.getMobilephone().equals(scheduling.getMobilephone()) && sl.getTypeNo().equals(scheduling.getTypeNo())) {
                                                    slint++;
                                                }
                                            }
                                            if (0 == slint) {
                                                scheduling.setSchedulingId(UUID.getUUIDStr());
                                                scheduling.setPid(dp.getPid());
                                                if(selectTypeinfoEndTypeNo(typeNo,dutyDate)){
                                                    list.add(scheduling);
                                                }else{
                                                    failureNum++;
                                                    failureMsg.append("<br/>第" + rowsNum + "行，当前类别编码不可导入");
                                                }
                                            } else {
                                                failureNum++;
                                                failureMsg.append("<br/>第" + rowsNum + "行，导入的数据重复，无法导入");
                                            }
                                        }
                                        successNum++;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、姓名 " + scheduling.getPname() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 1) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + (failureNum-1) + " 条数据格式不正确，错误如下：");
            map.put("flag", "1");
            map.put("message", failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条");
            map.put("flag", "0");
            map.put("message", successMsg.toString());
            map.put("list", list);
        }
        return map;
    }

    /**
     * 查询值班人
     * @param dutyScheduling
     * @return
     */
    @Override
    public List<DutyScheduling> selectDutySchedulingByTypeNo(DutyScheduling dutyScheduling) {
        return dutySchedulingMapper.selectDutySchedulingByTypeNo(dutyScheduling);
    }



    public boolean selectTypeinfoEndTypeNo(String typeNo,String dutyDate){
        DutyVersion dvs = new DutyVersion();
        dvs.setDutyDate(dutyDate.substring(0, 7));
        DutyTypeinfo dutyinfo = dutyTypeinfoMapper.checkTypeNoUnique(typeNo);
        if(null!=dutyinfo){
            dvs.setParentId(dutyinfo.getTypeinfoId());
            List<DutyVersion> versions = dutyViewMapper.selectVersionByParentId(dvs);
            if(null!=versions&&versions.size()>0){
                return false;
            }
        }
        return true;
    }

}
