package com.ruoyi.activiti.adapter;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;

import java.util.List;

public abstract class AbstractEndTaskAdapter implements IEndTaskAdapter{


    protected TableDataInfo getDataTable_ideal(List<?> list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<?> newList;
        if (pageNum * pageSize > list.size()) {
            if (pageSize > list.size()) {
                newList = list;
            } else {
                newList = list.subList((pageNum - 1) * pageSize, list.size());
            }
        } else {
            newList = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(newList);
        rspData.setTotal(list.size());
        return rspData;
    }
}
