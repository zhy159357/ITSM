package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.mapper.OgSysMapper;
import com.ruoyi.system.mapper.OgUserMapper;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用系统管理业务层处理
 */
@Service("application")
@Configuration
public class SysApplicationManagerServiceImpl implements ISysApplicationManagerService {

    @Value("${foreign.adpm.syncDate}")
    private String valueDate;
    /**
     * admp
     */
    @Value("${foreign.adpm.sysUrl}")
    private String adpmSysUrl;
    @Autowired
    private OgSysMapper ogSysMapper;

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Autowired
    private OgUserMapper ogUserMapper;

    @Autowired
    private IPubParaValueService pubParaValue;

    @Override
    public List<OgSys> selectOgSysList(OgSys sys) {
        List<OgSys> list = ogSysMapper.selectOgSysList(sys);
        return list;
    }

    @Override
    public List<OgPerson> selectOgPersonList(OgPerson person) {
        return ogPersonMapper.selectOgPersonList(person);
    }

    @Override
    public OgSys selectOgSysBySysId(String sysId) {
        OgSys ogSys = ogSysMapper.selectOgSysBySysId(sysId);
        // 如果外联渠道标识为空，则默认赋值为0
        if (StringUtils.isNotNull(ogSys)) {
            if (StringUtils.isEmpty(ogSys.getIsOutChannel())) {
                ogSys.setIsOutChannel("0");
            }
        }
        return ogSys;
    }

    @Override
    public OgSys selectOgSysBySysName(String sysName) {
        return ogSysMapper.selectOgSysBySysName(sysName);
    }

    @Override
    public int insertOgSys(OgSys sys) {
        if (sys.getSysId() == null || "".equals(sys.getSysId().trim())) {
            sys.setSysId(UUID.getUUIDStr());
        }
        return ogSysMapper.insertOgSys(sys);
    }

    @Override
    public int updateOgSys(OgSys sys) {
        return ogSysMapper.updateOgSys(sys);
    }

    @Override
    public int deleteOgSysByIds(String ids) {
        String[] sysIds = Convert.toStrArray(ids);
        return ogSysMapper.deleteOgSysByIds(sysIds);
    }

    @Override
    public List<OgSys> selectOgSysListByCondition(OgSys sys) {
        return ogSysMapper.selectOgSysListByCondition(sys);
    }

    @Override
    public List<OgPerson> selectOgPersonForOrgIdList(String partType) {
        List<PubParaValue> list = null;
        if ("1".equals(partType)) {
            list = pubParaValue.selectPubParaValueByParaName("Fm_dept_1");
        } else if ("2".equals(partType)) {
            list = pubParaValue.selectPubParaValueByParaName("Fm_dept_2");
        } else if ("3".equals(partType)) {
            list = pubParaValue.selectPubParaValueByParaName("Fm_dept_3");
        } else if ("4".equals(partType)) {
            list = pubParaValue.selectPubParaValueByParaName("Fm_dept_4");
        }
        String orgId = "";
        if (null != list && !list.isEmpty()) {
            orgId = list.get(0).getValue();
            return ogPersonMapper.selectOgPersonForOrgIdList(orgId);
        } else {
            return new ArrayList<OgPerson>();
        }
    }

    @Override
    public void updateOgSysAndCmdb(OgSys ogSys) {
        ogSysMapper.updateOgSysAndCmdb(ogSys);
    }

    @Override
    public List<OgSys> selectOgSysListWork(OgSys sys) {
        return ogSysMapper.selectOgSysListWork(sys);
    }

    @Override
    public OgSys selectOgSysBySysCodeForProblem(String sysCode) {
        if (StringUtils.isEmpty(sysCode)) {
            return null;
        }
        return ogSysMapper.selectOgSysBySysCodeForProblem(sysCode);
    }

    /**
     * 通过系统编码查询
     *
     * @param sysCode
     * @return
     */
    @Override
    public OgSys selectOgSysBySysCode(String sysCode) {
        if (StringUtils.isEmpty(sysCode)) {
            return null;
        }
        return ogSysMapper.selectOgSysBySysCode(sysCode);
    }


    @Override
    public List<OgSys> selectOgSysByName(String s) {
        return ogSysMapper.selectOgSysByName(s);
    }

    @Override
    public List<OgSys> selectOgSysByIds(List<String> ids) {
        return ogSysMapper.selectOgSysByIds(ids);
    }

    /**
     * 查询系统 flag == true 查当前登陆人所负责的系统 : 查所有
     *
     * @param flag
     * @return
     */
    @Override
    public List<LabelValue> selectOgSysForTinyWeb(String flag, String userId) {

        List<LabelValue> labelValueList = new ArrayList<>();
        if (StringUtils.isNotEmpty(flag)) {
            OgSys ogSys = new OgSys();
            if ("true".equals(flag)) {
                OgUser ogUser = ogUserMapper.selectOgUserByUserIdMysql(userId);
                if (!StringUtils.isEmpty(ogUser)) {
                    ogSys.setPid(ogUser.getUsername());
                }
            }
            List<OgSys> ogSysList = ogSysMapper.selectOgSysList(ogSys);
            if (!CollectionUtils.isEmpty(ogSysList)) {
                for (OgSys ogSys1 : ogSysList) {
                    LabelValue labelValue = new LabelValue();
                    labelValue.setValue(ogSys1.getCaption());
                    labelValue.setLabel(ogSys1.getCaption());
                    labelValueList.add(labelValue);
                }
            }
        }

        return labelValueList;
    }
}
