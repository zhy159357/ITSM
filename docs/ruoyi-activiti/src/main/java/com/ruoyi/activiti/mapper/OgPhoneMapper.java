package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.TelBiz;

import java.util.List;

public interface OgPhoneMapper {

    public List<TelBiz> selectPhoneList(TelBiz telBiz);

    int insertOgPhone(TelBiz telBiz);

    TelBiz selectPhoneById(String telid);

    int updatePhone(TelBiz telBiz);


    int deletePhoneByIds(String[] list);

    List<TelBiz> selectPhoneDeptNameList(TelBiz telBiz);
    List<TelBiz> selectPhoneDeptNameListMysql(TelBiz telBiz);

    List<TelBiz> selectPhoneSysIdList(TelBiz telBiz);
    List<TelBiz> selectPhoneSysIdListMysql(TelBiz telBiz);

    List<TelBiz> selectPhoneDeptNameAndSysIdList(TelBiz telBiz);
    List<TelBiz> selectPhoneDeptNameAndSysIdListMysql(TelBiz telBiz);
}
