package com.ruoyi.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.ShDuty;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.ShdutyMapper;
import com.ruoyi.system.service.IShDutyService;

@Service
public class ShDutyServiceImpl implements IShDutyService {
    private static final Logger log = LoggerFactory.getLogger(ShDutyServiceImpl.class);
	@Autowired
	private ShdutyMapper shdutyMapper;

	@Value("${foreign.duty.url}")
	private String url;
	@Value("${foreign.duty.serviceCode}")
	private String serviceCode;

	@Value("${foreign.duty.splitTime}")
	private String splitTime;

	public List<ShDuty> selectDutyList(ShDuty shDuty) {
		return shdutyMapper.selectShDutyList(shDuty);
	}

	public Object selectDutyById(ShDuty shDuty) {
		return shdutyMapper.selectShDutyById(shDuty);
	}
	public Object selectDutyByIdV(ShDuty shDuty) {
		return shdutyMapper.selectDutyByIdV(shDuty);
	}

	/**
	 * 批量删除节假日信息
	 *
	 * @param ids 需要删除的数据ID
	 * @throws Exception
	 */
	public int deleteDutyByIds(String ids) throws BusinessException {
		String[] holidayIds = Convert.toStrArray(ids);
		return shdutyMapper.deleteDutyByIds(holidayIds);
	}

	/**
	 * 删除某天之前的数据
	 * @param chddStr
	 * @return
	 */
	public int deleteDutyBeforeDays(String chddStr) throws BusinessException {
		return shdutyMapper.deleteDutyBeforeDays(chddStr);
	}

	/**
	 *
	 * @param shdutyMapper
	 * @return
	 */
	@Override
	public int insert(ShDuty shDuty) {
		return shdutyMapper.insert(shDuty);
	}

	/**
	 * 修改
	 *
	 * @param shdutyMapper
	 * @return
	 */
	@Override
	public int edit(ShDuty shDuty) {
		return shdutyMapper.edit(shDuty);
	}

	public int sync() {
		String startTime=DateUtils.convertWeekDayStart(new Date());
		String endTime= DateUtils.convertWeekDayEnd(new Date());
		Map<String, Object> jsonData = new HashMap<>();
		Map<String, Object> service = new HashMap<>();
		Map<String, Object> times = new HashMap<>();
		service.put("serviceCode", serviceCode);
		times.put("beginTime", startTime + " 00:00:00");
		times.put("endTime", endTime + " 23:59:59");
		log.info("sync.info.beginTime="+startTime+",endTime:"+endTime+" url:"+url);
		service.put("args", times);
		HttpHeaders httpHeaders = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		httpHeaders.setContentType(type);
		jsonData.put("reqData", service);
		HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(jsonData), httpHeaders);
		ResponseEntity<String> resp = restTemplate.postForEntity(url, httpEntity, String.class);
		String body = resp.getBody();
//		String body="\"content\":{\"data\":{\"total\":1,\"data\":[{\"id\":743222,\"group_code\":\"11111111\",\"nickname\":\"aaa\",\"kind\":\"0\",\"phone\":\"01111111\",\"duty_time\":\"2022-12-09\",\"status\":\"1\",\"group_name\":\"AAAA\"}]}}";
		log.info("sync.info:"+body);
		JSONObject jsonObject = JSON.parseObject(body);
		Map<String, Object> content = (Map<String, Object>) jsonObject.get("content");
		Map<String, Object> data = (Map<String, Object>) content.get("data");
		List<ShDuty> shDutyList = JSONObject.parseArray(data.get("data").toString(), ShDuty.class);
		shDutyList.stream().forEach(p -> {
			ShDuty dhDuty = shdutyMapper.selectShDutyById(p);
			if (null == dhDuty) {
				shdutyMapper.insert(p);
			} else {
				shdutyMapper.editSync(p);
			}
		});
		return 0;
	}

	public ShDuty queryDutyPerson(String startTime, String groupName) {
		String timeFlag = "0";
		Date date = new Date();
		SimpleDateFormat dfcomp = new SimpleDateFormat("yyyy-MM-dd HHmmss");// 设置日期格式
		String zeroTime = startTime + " " + "235959";
		String[] splitTimes = splitTime.split("~");
		String day = splitTimes[0];
		String night = splitTimes[1];

		String startDateTime = startTime + " " + day + "00";
		String endDateTime = startTime + " " + night + "00";

		try{
			Date startDate = dfcomp.parse(startDateTime);
			Date endDate = dfcomp.parse(endDateTime);
			Date zeroDate = dfcomp.parse(zeroTime);
			// 2022-11-29 08:30:00<2022-11-29 12:00:00<2022-11-29 17:30:00   白班    startTime 2022-11-29
			// 2022-11-30 08:30:00X<2022-11-30 01:00:00 <2022-11-30 17:30:00 夜班    startTime 2022-11-30   startTime 12点之后
			// 2022-11-29 08:30:00<2022-11-29 18:30:00 X<2022-11-29 17:30:00 夜班    startTime 2022-11-29   startTime 12点之前
			if(date.after(startDate) && date.before(endDate)) {
				// 白班timeFlag
				timeFlag = "0";
			} else if(mathLongTime(zeroDate, date) <= mathLongTime(zeroDate, endDate)) {  // 当天时间
				timeFlag = "1";
				// 夜班 当前时间还在当天，未过12点
			} else {
				// 过了12点减去一天 2022-11-30 变成  2022-11-29 表示的还是29号的夜班数据   2022-11-30 02:30:00
				timeFlag = "1";
				Date startTimeDate = DateUtils.parseDate(startTime, "yyyy-MM-dd");
				Date date1 = DateUtils.addDays(startTimeDate, -1);
				startTime = DateUtils.formatDate(date1, "yyyy-MM-dd");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {

			// 白班時間
			Date d1 = df2.parse(day);
			String dayTime = df1.format(d1);
			// 夜班時間
			Date d = df2.parse(night);
			String nightTime = df1.format(d);
			// 當前時間
			amBeginTime = df1.parse(dateStr);
			Date amDayTime = df1.parse(dayTime);
			Date amEndTime = df1.parse(nightTime);

			// 当天0点时间
			Date compDate = dfcomp.parse(compTime);
			// 当前时间
			Date currDate = df0.parse(df0.format(date));
			// 当前时间在0点之后，查询前一天的值班数据
			if (currDate.after(compDate)) {
				if (amBeginTime.before(amDayTime)) {
					timeFlag = "1";
				}
			} else {
				if (amBeginTime.after(amEndTime) && amBeginTime.before(amDayTime)) {
					timeFlag = "1";
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}*/

		ShDuty sd = new ShDuty();
		sd.setKind(timeFlag);
		sd.setGroup_name(groupName);
		sd.setDuty_time(startTime);
		return shdutyMapper.queryDutyPerson(sd);
	}

	public long mathLongTime(Date dateBig, Date dateSmall) {
		return dateBig.getTime() - dateSmall.getTime();
	}

	@Autowired
	private RestTemplate restTemplate;
}