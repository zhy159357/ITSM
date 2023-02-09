package com.ruoyi.form.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.form.entity.TwWorkNode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
public interface ITwWorkNodeService extends IService<TwWorkNode> {
    /**
     * 新增twWorkNode
     *
     * @param twWorkNode
     */
    void insert(TwWorkNode twWorkNode);

    /**
     * 修改twWorkNode
     *
     * @param twWorkNode
     */
    void update(TwWorkNode twWorkNode);

    /**
     * 提交或者暂存服务单 0：暂存  1：提交
     *
     * @param type
     */
    void saveOrSubmit(Integer type, String workNum);

    /**
     * 根据工单id查询子node（服务列表）
     *
     * @param id
     * @return
     */
    List<TwWorkNode> getWorkNodeByWorkOrderId(String id);

    /**
     * 根据id复制一个节点（相当于添加一条一模一样的数据）
     *
     * @param id
     */
    void copy(String id);

    /**
     * 根据id删除一个节点包括他的所有子节点
     * @param id
     */
    void delete(String id);

}
