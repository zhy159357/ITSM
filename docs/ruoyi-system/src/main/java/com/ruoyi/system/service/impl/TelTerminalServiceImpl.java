package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.TelTerminal;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.TelTerminalMapper;
import com.ruoyi.system.service.ITelTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-17
 */
@Service
public class TelTerminalServiceImpl implements ITelTerminalService {
    @Autowired
    private TelTerminalMapper telTerminalMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TelTerminal selectTelTerminalById(String id) {
        return telTerminalMapper.selectTelTerminalById(id);
    }

    /*
    * 电话接入
    * */
    @Override
    public List<TelTerminal> selectTelTerminalListTwo(TelTerminal telTerminal) {
        return telTerminalMapper.selectTelTerminalListTwo(telTerminal);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param telTerminal 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TelTerminal> selectTelTerminalList(TelTerminal telTerminal) {
        String showFlag=telTerminal.getShowFlag();
        if("1".equals(showFlag)){
            return telTerminalMapper.selectTelTerminalListShowFlag(telTerminal);
        }else{
            return telTerminalMapper.selectTelTerminalList(telTerminal);
        }
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param telTerminal 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTelTerminal(TelTerminal telTerminal) {
        return telTerminalMapper.insertTelTerminal(telTerminal);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param telTerminal 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTelTerminal(TelTerminal telTerminal) {
        return telTerminalMapper.updateTelTerminal(telTerminal);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTelTerminalByIds(String ids) {
        return telTerminalMapper.deleteTelTerminalByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTelTerminalById(String id) {
        return telTerminalMapper.deleteTelTerminalById(id);
    }

    @Override
    public OgPerson selectOgPerson(TelTerminal telTerminal) {
        return telTerminalMapper.selectOgPerson(telTerminal);
    }

    /**
     * @param telTerminal
     * @return
     */
    @Override
    public String checkBindInfoUnique(TelTerminal telTerminal) {
        int value=telTerminalMapper.checkBindInfoUnique(telTerminal);
        int value1=telTerminalMapper.checkBindInfoUniqueServer(telTerminal);
        String flag="0";
        if(value>0){
            flag="1";
        }else if(value1>0){
            flag="2";
        }
        return flag;
    }

    @Override
    public List<TelTerminal> selectTelTerminalListThree(TelTerminal telTerminal) {
        return telTerminalMapper.selectTelTerminalListThree(telTerminal);
    }

    @Override
    public List<TelTerminal> selectTelTerminalListByJobNumber(TelTerminal telTerminal) {
        return telTerminalMapper.selectTelTerminalListByJobNumber(telTerminal);
    }
}
