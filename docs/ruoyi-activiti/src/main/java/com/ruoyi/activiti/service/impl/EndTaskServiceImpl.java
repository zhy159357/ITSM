package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.IEndTaskAdapter;
import com.ruoyi.activiti.service.EndTaskService;
import com.ruoyi.activiti.utils.SpringContextUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

@Service("endTaskService")
public class EndTaskServiceImpl implements EndTaskService {


    private IEndTaskAdapter endTaskAdapter;


    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {

        //判断d
        //从容器中获取获取对应类型bean对象
        endTaskAdapter = SpringContextUtil.getBean(processKey);
        return endTaskAdapter.getEndPagerTasks(processKey, number);
    }

    @Override
    public int delete(String id, String type) throws Exception{
        endTaskAdapter = SpringContextUtil.getBean(type);
        return endTaskAdapter.remove(id);
    }

    @Override
    public int close(String id, String type) throws Exception{
        endTaskAdapter = SpringContextUtil.getBean(type);
        return endTaskAdapter.close(id);
    }


}
