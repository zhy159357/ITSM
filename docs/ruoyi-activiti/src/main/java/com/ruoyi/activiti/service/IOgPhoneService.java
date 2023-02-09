package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.common.core.domain.entity.OgOrg;

import java.util.List;

public interface IOgPhoneService {

    public List<TelBiz> selectPhoneList(TelBiz telBiz);

    public int insertOgPhone(TelBiz telBiz);

    public TelBiz selectPhoneById(String telid);

    int updatePhone(TelBiz telBiz);

    int deletePhoneByIds(String ids);

    public List<TelBiz> selectPhoneDeptNameList(TelBiz telBiz);

    public List<TelBiz> selectPhoneSysIdList(TelBiz telBiz);

    public List<TelBiz> selectPhoneDeptNameAndSysIdList(TelBiz telBiz);

    /**
     * 根据当前登录人ID获取对应组号
     * @return
     */
    public String getGroupNo();
}
