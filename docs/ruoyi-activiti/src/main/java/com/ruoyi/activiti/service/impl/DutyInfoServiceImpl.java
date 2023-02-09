package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.mapper.DutySchedulingMapper;
import com.ruoyi.activiti.mapper.DutyTypeinfoMapper;
import com.ruoyi.activiti.mapper.DutyVersionMapper;
import com.ruoyi.activiti.mapper.DutyViewMapper;
import com.ruoyi.activiti.service.IDutyInfoService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service接口
 * @author dayong_sun
 * @date 2021-07-01
 */
@Service
public class DutyInfoServiceImpl implements IDutyInfoService {

    @Autowired
    private DutySchedulingMapper dutySchedulingMapper;

    @Autowired
    private DutyTypeinfoMapper dutyTypeinfoMapper;

    @Autowired
    private DutyVersionMapper dutyVersionMapper;

    @Autowired
    private DutyViewMapper dutyViewMapper;

    @Autowired
    private IOgUserService iOgUserService;

    //栈
    Stack<String> stack = new Stack<>();


    /**
     * 统一监控调用接口
     * @param startTime 开始值班时间
     * @param endTime 结束值班时间
     * @param typeNo 类编编码
     * @return
     */
    @Override
    public List<Map<String,Object>> selectDutyInfoList(String startTime,String endTime,String typeNo) {
        List<Map<String,Object>> list = new ArrayList<>();
        DutyScheduling scheduling = new DutyScheduling();
        scheduling.setStartTime(startTime);
        scheduling.setEndTime(endTime);
        scheduling.setTypeNo(typeNo);
        List<DutyScheduling> schedulings = dutySchedulingMapper.selectDutyInfoList(scheduling);
        if(!schedulings.isEmpty()){
            for(DutyScheduling dutyScheduling : schedulings){
                Map<String,Object> map = new HashMap<>();
                String typeName = getTypeName(dutyScheduling.getTypeinfoId());
                map.put("typeName",typeName);
                map.put("typeNo",dutyScheduling.getTypeNo());
                map.put("pname",dutyScheduling.getPname());
                map.put("mobilephone",dutyScheduling.getMobilephone());
                map.put("dutyDate",dutyScheduling.getDutyDate());
                map.put("leader",dutyScheduling.getLeader());
                list.add(map);
            }
        }
        return list;
    }



    public String getTypeName(String typeinfoId){
        String bb = "白班";
        DutyScheduling dutyScheduling = dutySchedulingMapper.selectTypeNameByTypeNo(typeinfoId);
        if(null!=dutyScheduling){
            String typeName = dutyScheduling.getTypeName();
            if(bb.equals(typeName) || "夜班".equals(typeName)){
                return typeName;
            }else{
                if(null == dutyScheduling.getParentId()){
                    return bb;
                }
                getTypeName(dutyScheduling.getParentId());
            }
        }
        return bb;
    }

    @Override
    public OgUser selectOgUserByCustNo(String custNo) {
        return dutySchedulingMapper.selectOgUserByCustNo(custNo);
    }

    @Override
    public AjaxResult selectDutyCalendar(String custNo, String dutyDate) {
        DutyVersion dutyVersion = new DutyVersion();

        if (StringUtils.isEmpty(dutyDate)){
            //获取当前时间
            dutyDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM,DateUtils.getNowDate());
        }
        OgUser ogUser = selectOgUserByCustNo(custNo);
        if(ogUser == null){
           return AjaxResult.error("用户为空！",false);
        }

        dutyVersion.setPid(ogUser.getpId());
        dutyVersion.setDutyDate(dutyDate);


        List<DutyVersion>  DutyCalendarList = dutySchedulingMapper.selectDutyCalendar(dutyVersion);

        if(DutyCalendarList.isEmpty()){
           return AjaxResult.success("当月没有值班!",false);
        }

        List<String> collect = DutyCalendarList.stream().map(d -> d.getDutyDate()).collect(Collectors.toList());

        return AjaxResult.success(collect);
    }

    @Override
    public AjaxResult getDutyTypeInfoChooseList() {

        //根据值班中心编码获取值班信息
        DutyTypeinfo dutyTypeinfo = dutyTypeinfoMapper.checkTypeNoUnique(VersionStatusConstants.ZBZX_NO);
        //根据顶级类查询子类
        List<DutyTypeinfo> dutyTypeInfoList  =dutyTypeinfoMapper.selectDutyTypeInfoListByParentId(dutyTypeinfo.getTypeinfoId());

        if(dutyTypeInfoList.isEmpty()){
            return AjaxResult.error("值班类型异常!",false);
        }

        List<String> collect = dutyTypeInfoList.stream().map(d -> d.getTypeNo()).collect(Collectors.toList());

        return AjaxResult.success(collect);
    }

    /**
     * 获取值班的excel 接口
     * @param dutyDate 月份时间
     * @param typeNo   值班大类型（全部、白班、夜班、领导）
     * @return
     */
    @Override
    public AjaxResult getDutyCalendarExcel(String dutyDate, String typeNo) {

        Map<String,Object> joinMap = new LinkedHashMap<>();

        //四类参数类型
        List<DutyVersion> latelist1 = new ArrayList<>();
        List<DutyVersion> latelist2 = new ArrayList<>();
        List<DutyVersion> latelist3 = new ArrayList<>();
        List<DutyVersion> latelist4 = new ArrayList<>();

        //按照顺序存储所有类别编码
        List<String> typeNoList = new ArrayList<>();
        //存储时间所有数据
        List<String> dataList = new ArrayList<>();

        //根据月份查询当月的所有值班信息
        DutyScheduling dutyScheduling = new DutyScheduling();
        dutyScheduling.setDutyDate(dutyDate);

        //实例化-版本对象
        DutyVersion dutyVersion = new DutyVersion();
        dutyVersion.setDutyDate(dutyDate);
        dutyVersion.setTypeNo(typeNo);
        String endTypeNo = "";

        //查询顶类"值班中心"的值班类型信息
        DutyTypeinfo dutyTypeinfo = dutyTypeinfoMapper.checkTypeNoUnique(VersionStatusConstants.ZBZX_NO);
        //全部值班类别
        List<DutyVersion> dutyVersions = dutyVersionMapper.selectVersionByDutyDate(dutyVersion);

        if(null!=dutyVersions&&dutyVersions.size()>0) {
            //一类
            for(DutyVersion version : dutyVersions){
                if (null != dutyTypeinfo && dutyTypeinfo.getTypeinfoId().equals(version.getParentId())) {
                    latelist1.add(version);
                }
            }
            //二类
            for(DutyVersion late : latelist1){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        latelist2.add(version);
                    }
                }
            }
            //三类
            for(DutyVersion late : latelist2){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        latelist3.add(version);
                    }
                }
            }
            //四类
            for(DutyVersion late : latelist3){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        latelist4.add(version);
                    }
                }
            }

            if(null!=latelist3&&latelist3.size()>0){
                endTypeNo = latelist3.get(latelist3.size() - 1).getTypeNo();
            }

            if(null!=latelist1&&latelist1.size()>0){
                typeNoList.add(latelist1.get(0).getTypeNo());
            }

            //遍历四级类获取所以最末端节点的类型编码
            for(DutyVersion late : latelist4){
                DutyVersion dv = new DutyVersion();
                dv.setDutyDate(dutyDate);
                dv.setParentId(late.getTypeinfoId());
                //根据月份和值班类型查询值班版本的值班类型集合
                List<DutyVersion> versions = dutyViewMapper.selectVersionByParentId(dv);

                if(null==versions||versions.size()==0){
                    if("2".equals(late.getTypeRows())&&"5".equals(late.getTypeColumns())){
                        typeNoList.add(late.getTypeNo());
                    }else {
                        int columns = Integer.valueOf(late.getTypeColumns()==null?"1":late.getTypeColumns());
                        typeNoList.add(late.getTypeNo());
                        if(columns>1){
                            dv.setTypeinfoId(late.getParentId());
                            List<DutyVersion> vtypes = dutyVersionMapper.selectVersionByParentId(dv);
                            for(DutyVersion vt : vtypes){
                                typeNoList.add(vt.getTypeNo());
                            }
                        }
                    }
                }else{
                    for(DutyVersion version : versions){
                        dv.setParentId(version.getTypeinfoId());
                        //根据父id查询下级类别
                        List<DutyVersion> vers = dutyViewMapper.selectVersionByParentId(dv);
                        if(null==vers||vers.size()==0){
                            typeNoList.add(version.getTypeNo());
                        }else{
                            for(DutyVersion ver : vers){
                                for(int i=0;i<Integer.valueOf(ver.getTypeColumns()==null?"1":ver.getTypeColumns());i++){
                                    typeNoList.add(ver.getTypeNo());
                                }

                            }
                        }
                    }
                }
            }
            typeNoList.add(endTypeNo);

            //根据时间和类型递归查询所以子类
            List<String> dutyTypeNoList = dutyVersionMapper.selectVersionByDutyDateAndTypeNo(dutyVersion);

            //全部类型编码与对应传参三大类交集
            typeNoList.retainAll(dutyTypeNoList);


            if(typeNoList.isEmpty()){
                return AjaxResult.error("末端值班类型集合为空",false);
            }else {
                //拼接值班类型名称
                String typeName = "";
                List<DutyVersion> dutyVersionList =  dutyVersionMapper.selectDutyVersionList(dutyDate);
                for (String s:typeNoList) {
                    //根据传入值班列表编码"typeNo"查询值班类型信息
                    DutyVersion dvs = dutyVersionMapper.findDutyVersionByTypeNoAndDate(s,dutyDate);
                    //如果是领导类别，并且查询为空则直接赋值typeName
                    if(s.equals(VersionStatusConstants.LEADER_NO)){
                        typeName = dvs.getTypeName();
                    }else {
                        stack = jointTypeName(dvs.getTypeinfoId(),dutyDate,dutyVersionList);
                        StringBuilder stringBuilder = new StringBuilder();
                        while (!stack.isEmpty()){
                            stringBuilder.append(stack.pop()+"-");
                        }
                        if(stringBuilder.toString().length() >1){
                            typeName = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
                        }
                    }
                    joinMap.put(s,typeName);
                }
            }

            //月份日期设置
            String str[] = dutyDate.split("-");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.valueOf(str[0]));
            cal.set(Calendar.MONTH, Integer.valueOf(str[1]) - 1); // 7月
            int maxDate  =  cal.getActualMaximum(Calendar.DATE);
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(maxDate);
            //存入每月30天日期map
            Map<String,Object> dayMap = new LinkedHashMap<>();

            Map<String,Object> typeNameMap = new LinkedHashMap();

            if(joinMap.isEmpty()){
                AjaxResult.error("标题集合为空",false);
            }else {
                List<DutyScheduling> dutySchedulings1 = dutyViewMapper.selectViewListByDutyDate(dutyScheduling);
                for(String key:joinMap.keySet()){

                    for(int i=0;i<maxDate;i++){
                        cal.set(Calendar.DAY_OF_MONTH, i+1);
                        String day = DateUtils.dateTime(cal.getTime());
                        Map<String,Object> map1 = new LinkedHashMap<>();
                        dataList.add(0,day+dateToWeek(day));
                        dataList.add(1,"");
                        dataList.add(2,"");
                        for (DutyScheduling scheduling :dutySchedulings1) {
                            if(day.equals(scheduling.getDutyDate())){
                                if(key.equals(scheduling.getTypeNo())){
                                    dataList.add(1,scheduling.getPname());
                                    dataList.add(2,scheduling.getMobilephone());
                                    break;
                                }
                            }
                        }
                        map1.put("typeNo",dataList);
                        list.add(map1);
                        dataList = new ArrayList<>();
                    }


                    dayMap.put(joinMap.get(key).toString(),list);
                    typeNameMap.put(key,dayMap);
                    list = new ArrayList<Map<String,Object>>(maxDate);
                    dayMap = new HashMap<String,Object>();
                }
            }
            return AjaxResult.success(typeNameMap);

        }else{
            return AjaxResult.success("当前月份没有绑定值班信息!",false);
        }

    }

    /**
     * 递归获取值班类型名称添加到队列
     * @param typeInfoId
     * @return
     */
    public Stack jointTypeName(String typeInfoId,String dutyDate, List<DutyVersion> dutyVersionList){
        if(StringUtils.isEmpty(typeInfoId)){
            return null;
        }
        for (DutyVersion dutyVersion :dutyVersionList) {
            if(dutyVersion.getTypeinfoId().equals(typeInfoId)){
                if(dutyVersion != null && !StringUtils.isEmpty(dutyVersion.getParentId())){
                    stack.push(dutyVersion.getTypeName());
                    jointTypeName(dutyVersion.getParentId(),dutyDate,dutyVersionList);
                }
            }
        }
        return stack;
    }


    /**
     * 根据日期获取 星期 （2019-05-06 ——> 星期一）
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {

        String[] weekDays = {"(日)", "(一)", "(二)", "(三)", "(四)", "(五)", "(六)"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = DateUtils.parseDate(datetime);
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

}
