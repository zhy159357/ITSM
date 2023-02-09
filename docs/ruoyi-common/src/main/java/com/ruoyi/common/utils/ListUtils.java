package com.ruoyi.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * list工具类
 *
 * @author 14735
 */
public class ListUtils {

    /**
     * 后台计算对list分页
     *
     * @param list
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static List subList(List list, Integer pageIndex, Integer pageSize) {
        List newList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return newList;
        }
        if (pageIndex * pageSize > list.size()) {
            newList = list.subList((pageIndex - 1) * pageSize, list.size());
        } else {
            newList = list.subList((pageIndex - 1) * pageSize, pageIndex * pageSize);
        }
        return newList;
    }
}
