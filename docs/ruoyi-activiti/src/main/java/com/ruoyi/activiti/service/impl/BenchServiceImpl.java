package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.IBenchAdapter;
import com.ruoyi.activiti.service.BenchService;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("benchService")
public class BenchServiceImpl implements BenchService, ApplicationContextAware {
    @Autowired
    private ISysRoleService iSysRoleService;

    private static final Logger log = LoggerFactory.getLogger(BenchServiceImpl.class);

    private ApplicationContext applicationContext;

    private List<IBenchAdapter> benchAdapters;

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {
        for (IBenchAdapter benchAdapter : benchAdapters) {
            List<OgRole> roleIds = iSysRoleService.selectRolesByUserId(userId);//拿到登陆人角色
            List<String> list = new ArrayList<>();
            if (!roleIds.isEmpty()) {
                for (OgRole role : roleIds) {
                    list.add(role.getRid());
                }
                if (benchAdapter.support(userId, list)) {//判断登陆人角色有没有权限操作该工单
                    if (benchAdapter.getBenchType().equals(type)) {
                        return benchAdapter.getBenchPagerTasks(userId, type);
                    }
                }
            }
        }
        return new TableDataInfo();
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initBenchAdapter();
    }

    /**
     * 初始化工作台待办适配器
     */
    private void initBenchAdapter() {
        if (this.benchAdapters == null) {
            Map<String, IBenchAdapter> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                    applicationContext, IBenchAdapter.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.benchAdapters = new ArrayList<IBenchAdapter>(matchingBeans.values());
                OrderComparator.sort(this.benchAdapters);
            } else {
                benchAdapters = new ArrayList<IBenchAdapter>();
            }
            log.info("加载工作台适配器：" + matchingBeans.keySet().toString() + ",完成");
        }
    }

}
