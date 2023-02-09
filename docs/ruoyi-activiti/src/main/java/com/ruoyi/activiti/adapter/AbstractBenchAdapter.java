package com.ruoyi.activiti.adapter;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * 工作台代办适配器抽象类
 *
 * @author LiuPeng
 */
public abstract class AbstractBenchAdapter implements IBenchAdapter {

    @Override
    public boolean support(String userId, List<String> roleIds) {
        List<String> authRoles = getAuthRoles();
        for (String authRole : authRoles) {
            if (roleIds.contains(authRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMainTask() {
        return false;
    }

    /**
     * 获取有权限的角色列表
     *
     * @return
     */
    public abstract List<String> getAuthRoles();

    /**
     * 自定义分页
     *
     * @param list
     * @return
     */
    protected TableDataInfo getDataTable_ideal(List<?> list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<?> newList = new ArrayList<>();
        int pageIndex = Integer.valueOf(pageNum).intValue();
        if(pageIndex *pageSize>list.size()){
            newList = list.subList((pageIndex-1)*pageSize,list.size());
        }else{
            newList =list.subList((pageIndex-1)*pageSize,pageIndex*pageSize);
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(newList);
        rspData.setTotal(list.size());
        return rspData;
    }

    @Override
    public long getTotalCount(String userId, String type) {
        TableDataInfo list = getBenchPagerTasks(userId, type);
        System.out.println("代办数量是：" + list.getTotal());
        return list.getTotal();
    }
}
