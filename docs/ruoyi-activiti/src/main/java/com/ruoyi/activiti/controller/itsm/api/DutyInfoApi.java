package com.ruoyi.activiti.controller.itsm.api;

import com.ruoyi.activiti.service.IDutyInfoService;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 值班接口
 */
@Controller
@RequestMapping("/dutyApi")
public class DutyInfoApi extends BaseController {

	@Autowired
	private IDutyInfoService iDutyInfoService;

	private static DutyInfoApi dutyInfoApi = null;

	public static DutyInfoApi getInstance(){
		if(dutyInfoApi==null){
			dutyInfoApi = new DutyInfoApi();
		}
		return dutyInfoApi;
	}

	/**
	 * 统一监控调用接口
	 * @param startTime 开始值班时间
	 * @param endTime 结束值班时间
	 * @param typeNo 类编编码
	 * @return
	 */
	@PostMapping("/getDutyInfo")
	@ResponseBody
    public List<Map<String,Object>> getDutyInfo(String startTime,String endTime,String typeNo){
		return iDutyInfoService.selectDutyInfoList(startTime,endTime,typeNo);
	}

}
