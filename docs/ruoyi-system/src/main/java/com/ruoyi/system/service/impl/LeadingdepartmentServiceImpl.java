package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.Leadingdepartment;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.PubHoliday;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.system.mapper.LeadingdepartmentMapper;
import com.ruoyi.system.mapper.PubHolidayMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ILeadingdepartmentService;
import com.ruoyi.system.service.IPubHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LeadingdepartmentServiceImpl implements ILeadingdepartmentService {

    @Autowired
    private LeadingdepartmentMapper leadingdepartmentMapper;
    @Autowired
    private SysDeptMapper deptMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 新增牵头部门
     *
     * @param leadingdepartment
     * @return
     */
    @Override
    public int insert(Leadingdepartment leadingdepartment) {

        List<OgOrg> ogOrgs = deptMapper.selectDeptByIds(Convert.toStrArray(leadingdepartment.getOrgId()));
        for (OgOrg branch : ogOrgs) {
            leadingdepartment.setOrgName(branch.getOrgName());
            leadingdepartment.setPorgName(branch.getParentName());
        }
        return leadingdepartmentMapper.insert(leadingdepartment);
    }


    //根据分页条件查出牵头部门信息
    @Override
    public List<Leadingdepartment> selectLpList( Leadingdepartment leadingdepartment) {
        return leadingdepartmentMapper.selectLpList(leadingdepartment);
    }

    /**
     * 批量删除牵头部门信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public int deletelpByIds(String ids) throws BusinessException {
        String[] holidayIds = Convert.toStrArray(ids);
        return leadingdepartmentMapper.deleteHolidayByIds(holidayIds);
    }




}
