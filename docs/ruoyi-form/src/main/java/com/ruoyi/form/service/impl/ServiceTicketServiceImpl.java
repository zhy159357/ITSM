package com.ruoyi.form.service.impl;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.form.service.IServiceTicketService;
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
public class ServiceTicketServiceImpl implements IServiceTicketService {
    @Autowired
    private OgUserMapper userMapper;

    /**
     * 获取工单状态
     * @return
     */
    @Override
    public List<Map<String, Object>> ticketStatus() {
        List<Map<String, Object>> ret = new ArrayList<>();
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "待接单");
                put("value", "0");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "已接单");
                put("value", "1");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "已转派");
                put("value", "2");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "处理中");
                put("value", "3");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "处理完成待复核");
                put("value", "4");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "复核退回");
                put("value", "5");
            }
        });
        ret.add(new HashMap<String, Object>() {
            {
                put("label", "已关闭");
                put("value", "6");
            }
        });
        return ret;
    }
}
