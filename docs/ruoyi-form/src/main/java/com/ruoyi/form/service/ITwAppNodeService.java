package com.ruoyi.form.service;

import com.ruoyi.form.entity.TwAppNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chw
 * @since 2022-08-08
 */
public interface ITwAppNodeService extends IService<TwAppNode> {

    /**
     *
     * @param twAppNode
     * @return
     */
  List<TwAppNode> getTwAppNode(TwAppNode twAppNode);

}
