package com.ruoyi.activiti.mapper;


import com.ruoyi.activiti.domain.ActHiActinst;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActHiActinstMapper {

	/**
	 * 根据流程实例id查询历史流程列表
	 *
	 * @param instanceIds
	 * @return
	 */
	List<ActHiActinst> listActHiActinst(@Param("instanceIds") List<String> instanceIds);
}
