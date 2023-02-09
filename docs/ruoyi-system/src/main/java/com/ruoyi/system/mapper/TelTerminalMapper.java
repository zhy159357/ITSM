package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.TelTerminal;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2020-12-17
 */
public interface TelTerminalMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TelTerminal selectTelTerminalById(String id);


    public List<TelTerminal> selectTelTerminalListTwo(TelTerminal telTerminal);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param telTerminal 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelTerminal> selectTelTerminalList(TelTerminal telTerminal);
    /**
     * 业务人员查看列表
     *
     * @param telTerminal 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelTerminal> selectTelTerminalListShowFlag(TelTerminal telTerminal);

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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTelTerminalById(String id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTelTerminalByIds(String[] ids);

    /**
     * 工號選擇
     * @param telTerminal
     * @return
     */
    public OgPerson selectOgPerson(TelTerminal telTerminal);

    /**
     * 校验
     * @param telTerminal
     * @return
     */
    public int checkBindInfoUnique(TelTerminal telTerminal);

    /**
     * 校验
     * @param telTerminal
     * @return
     */
    public int checkBindInfoUniqueServer(TelTerminal telTerminal);

    List<TelTerminal> selectTelTerminalListThree(TelTerminal telTerminal);

    List<TelTerminal> selectTelTerminalListByJobNumber(TelTerminal telTerminal);
}
