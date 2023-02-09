package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.OgEmergMapper;
import com.ruoyi.activiti.service.IOgEmergService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgEmerg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class OgEmergServiceImpl implements IOgEmergService
{
    @Autowired
    private OgEmergMapper emergMapper;

    /**
     * 查询事件
     * @param emerg 事件信息
     * @return 事件信息集合
*/
    @Override
    public List<OgEmerg> selectEmergList(OgEmerg emerg)
    {
        return emergMapper.selectEmergList(emerg);
    }

    @Override
    public int deleteEmergByIds(String ids){
        String[]  list= Convert.toStrArray(",",ids);
        return emergMapper.deleteEmergByIds(list);
    }

    /**
     * 新增参数列别
     *
     * @param emerg 参数列别
     * @return 结果
     */
    @Override
    public int insertOgEmerg(OgEmerg emerg)
    {
        return emergMapper.insertOgEmerg(emerg);
    }

    /**
     * 通过事件单ID查询岗位信息
     *
     * @param id 岗位ID
     * @return 角色对象信息
     */
    @Override
    public OgEmerg selectEmergById(String id) {

        return emergMapper.selectEmergById(id);
    }

    /**
     * 修改保存岗位信息
     *
     * @param emerg 岗位信息
     * @return 结果
     */
    @Override
    public int updateEmerg(OgEmerg emerg) {

        return emergMapper.updateEmerg(emerg);
    }

    /**
     * 通过系统ID查询角色
     * @param id 系统ID
     * @return 系统对象信息
     */
    @Override
    public OgEmerg selectById(String id)
    {
        return emergMapper.selectById(id);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public OgEmerg selectOgEmergById(String id)
    {
        return emergMapper.selectOgEmergById(id);
    }


    @Override
    public int updateEmergMark(OgEmerg emerg) {
        return emergMapper.updateEmergMark(emerg);
    }

}
