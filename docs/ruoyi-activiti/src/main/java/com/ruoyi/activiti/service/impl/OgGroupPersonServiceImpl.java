package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.OgGroupPersonMapper;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作组人员信息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-18
 */
@Service
public class OgGroupPersonServiceImpl implements IOgGroupPersonService
{
    @Autowired
    private OgGroupPersonMapper ogGroupPersonMapper;

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;



    /**
     * 查询工作组人员信息
     *
     * @param gpid 工作组人员信息ID
     * @return 工作组人员信息
     */
    @Override
    public List<OgGroupPerson> selectOgGroupPersonById(String gpid)
    {
        return ogGroupPersonMapper.selectOgGroupPersonById(gpid);
    }

    /**
     * 查询工作组人员信息列表
     *
     * @param ogGroupPerson 工作组人员信息
     * @return 工作组人员信息
     */
    @Override
    public List<OgGroupPerson> selectOgGroupPersonList(OgGroupPerson ogGroupPerson)
    {
        return ogGroupPersonMapper.selectOgGroupPersonList(ogGroupPerson);
    }

    /**
     * 新增工作组人员信息
     *
     * @param ogGroupPerson 工作组人员信息
     * @return 结果
     */
    @Override
    public int insertOgGroupPerson(OgGroupPerson ogGroupPerson)
    {
        return ogGroupPersonMapper.insertOgGroupPerson(ogGroupPerson);
    }

    /**
     * 修改工作组人员信息
     *
     * @param ogGroupPerson 工作组人员信息
     * @return 结果
     */
    @Override
    public int updateOgGroupPerson(OgGroupPerson ogGroupPerson)
    {
        return ogGroupPersonMapper.updateOgGroupPerson(ogGroupPerson);
    }

    /**
     * 删除工作组人员信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgGroupPersonByIds(String ids)
    {
        return ogGroupPersonMapper.deleteOgGroupPersonByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工作组人员信息信息
     *
     * @param gpid 工作组人员信息ID
     * @return 结果
     */
    @Override
    public int deleteOgGroupPersonById(String gpid)
    {
        return ogGroupPersonMapper.deleteOgGroupPersonById(gpid);
    }

    @Override
    public int deleteGroupByPersonId(String pId) {
        return ogGroupPersonMapper.deleteGroupByPersonId(pId);
    }

    public List<Map<String, Object>> selectOgGroupPersons(String gpid)
    {
        List<OgGroupPerson> persons = ogGroupPersonMapper.selectOgGroupPersonById(gpid);
        if (CollectionUtils.isEmpty(persons)) {
            throw new BusinessException(String.format("该组%s下没有配置人员!", gpid));
        }
        List<Map<String, Object>> personList = new ArrayList<>();
        for (OgGroupPerson person : persons) {
            Map<String,Object> personMap=new HashMap<>();
            personMap.put("label", person.getPname());
            personMap.put("value", person.getPid());
            personList.add(personMap);
        }
        return personList;
    }
    public List<Map<String, Object>> selectGeneralManagers(String id)
    {
        // 查询机构里的总经理室部室
        OgOrg org = new OgOrg();
        org.setOrgName("总经理室");
        List<OgOrg> ogOrgs = sysDeptMapper.selectDeptList(org);
        List<OgPerson> persons = ogOrgs.stream().map(ogOrg -> ogPersonMapper.selectOgPersonList(OgPerson.builder().orgId(ogOrg.getOrgId()).invalidationMark("1").build()))
                .collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(persons)) {
            throw new BusinessException("总经理室机构未配置人员!");
        }
        List<Map<String, Object>> personList = new ArrayList<>();
        for (OgPerson person : persons) {
            Map<String,Object> personMap=new HashMap<>();
            personMap.put("label", person.getpName());
            personMap.put("value", person.getpId());
            personList.add(personMap);
        }
        return personList;
    }
}
