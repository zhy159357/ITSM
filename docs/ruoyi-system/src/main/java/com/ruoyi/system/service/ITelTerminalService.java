package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.TelTerminal;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2020-12-17
 */
public interface ITelTerminalService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TelTerminal selectTelTerminalById(String id);

    /*
    * 电话接入
    * */
    public List<TelTerminal> selectTelTerminalListTwo(TelTerminal telTerminal);
    /**
     * 查询【请填写功能名称】列表
     *
     * @param telTerminal 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelTerminal> selectTelTerminalList(TelTerminal telTerminal);

    /**
     * 新增【请填写功能名称】
     *
     * @param telTerminal 【请填写功能名称】
     * @return 结果
     */
    public int insertTelTerminal(TelTerminal telTerminal);

    /**
     * 修改【请填写功能名称】
     *
     * @param telTerminal 【请填写功能名称】
     * @return 结果
     */
    public int updateTelTerminal(TelTerminal telTerminal);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTelTerminalByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTelTerminalById(String id);

    /**
     *
     * @param telTerminal
     * @return
     */
    public OgPerson selectOgPerson(TelTerminal telTerminal);

    /**
     *
     * @param telTerminal
     * @return
     */
    public String checkBindInfoUnique(TelTerminal telTerminal);

    List<TelTerminal> selectTelTerminalListThree(TelTerminal telTerminal);

    List<TelTerminal> selectTelTerminalListByJobNumber(TelTerminal tt);
}
