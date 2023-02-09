package com.ruoyi.form.service.impl;

import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.form.service.IServiceRuleService;
import com.ruoyi.system.mapper.OgUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 服务模块-规则服务
 */
@Service
public class ServiceRuleServiceImpl implements IServiceRuleService {
    @Autowired
    private OgUserMapper userMapper;

    /**
     * 获取工单类型
     * @return
     */
    @Override
    public List<Map<String, Object>> getTicketTypes() {
        List<Map<String, Object>> ret = new ArrayList<>();
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "查询");
                put("value", "0");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "巡检");
                put("value", "1");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "操作");
                put("value", "2");
            }
        });
        return ret;
    }

    /**
     * 获取规则类型
     * @return
     */
    @Override
    public List<Map<String, Object>> getRuleTypes() {
        List<Map<String, Object>> ret = new ArrayList<>();
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "单次");
                put("value", "0");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "多次");
                put("value", "1");
            }
        });
        return ret;
    }

    /**
     * 获取是否复核
     * @return
     */
    @Override
    public List<Map<String, Object>> recheckStatus() {
        List<Map<String, Object>> ret = new ArrayList<>();
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "是");
                put("value", "1");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "否");
                put("value", "0");
            }
        });
        return ret;
    }

    /**
     * 获取规则状态
     */
    @Override
    public List<Map<String, Object>> ruleStatus() {
        List<Map<String, Object>> ret = new ArrayList<>();
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "草稿");
                put("value", "0");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "待审核");
                put("value", "1");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "审批退回");
                put("value", "2");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "审批通过");
                put("value", "3");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "规则被取消");
                put("value", "4");
            }
        });
        return ret;
    }

    /**
     * 获取提交人
     */
    @Override
    public List<Map<String, Object>> commitUsers() {
        List<Map<String, Object>> ret = new ArrayList<>();
        OgUser user = ShiroUtils.getOgUser();
        ret.add(new HashMap<String, Object>() {
            {
                put("label", user.getUsername());
                put("value", user.getUserId());
            }
        });
        return ret;
    }

    /**
     * 获取审核人（当前用户所属部门的负责人）
     * @param f f=0查询当前用户所属科室的全部领导/f=1查询当前用户所属科室全部员工
     */
    @Override
    public List<Map<String, Object>> allUsers(Integer f) {
        List<OgUser> users;
        String userId = ShiroUtils.getOgUser().getUserId();
        if (f.equals(0)) {
            users = this.userMapper.selectCurrentOrgLeaders(userId);
        } else {
            users = this.userMapper.selectCurrentOrgUsers(userId);
        }
        List<Map<String, Object>> ret = new ArrayList<>();
        for (OgUser user : users) {
            ret.add(new HashMap<String, Object>() {
                {
                    put("label", user.getUsername());
                    put("value", user.getUserId());
                }
            });
        }
        return ret;
    }
}
