package com.ruoyi.form.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.form.constants.EventFieldConstants;
import com.ruoyi.form.constants.TinywebConstants;
import com.ruoyi.form.domain.ChangeTaskScene;
import com.ruoyi.form.domain.ChmBasedata;
import com.ruoyi.form.domain.ChmParavalue;
import com.ruoyi.form.domain.CommonTree;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.IChangeTaskSceneService;
import com.ruoyi.form.service.IChmBasedataService;
import com.ruoyi.form.service.IChmParavalueService;
import com.ruoyi.form.service.impl.CommonTreeServiceImpl;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.mapper.OgTypeinfoMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.ProblemConstant.*;

/**
 * @ClassName IDCodeConvertChineseUtil
 * @Description 字段转换汉字
 * @Author JiaQi Zhang
 * @Date 2022/9/28 9:46
 */
@Component
@RequiredArgsConstructor
public class IDCodeConvertChineseUtil {

	private final IOgPersonService ogPersonService;
	private final IOgUserService ogUserService;
	private final IChangeTaskSceneService changeTaskSceneService;
	private final OgPersonMapper ogPersonMapper;
	private final ISysDeptService iSysDeptService;
	private final IOgTypeinfoService ogTypeinfoService;
	private final ISysWorkService sysWorkService;
	private final IPubParaValueService pubParaValueService;
	private final ISysApplicationManagerService applicationManagerService;
	private final OgTypeinfoMapper ogTypeinfoMapper;
	private final PubParaValueMapper pubParaValueMapper;
	private final IChmParavalueService iChmParavalueService;
	private final IChmBasedataService iChmBasedataService;
	public static IDCodeConvertChineseUtil idCodeConvertChineseUtil;
	private CommonTreeServiceImpl commonTreeService = SpringUtils.getBean(CommonTreeServiceImpl.class);

	@PostConstruct
	public void init() {
		idCodeConvertChineseUtil = this;
	}

	public static OgPerson getPerson(String pId) {
		return idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(pId);
	}

	public static List<OgPerson> getPerson(List<String> ids) {
		return idCodeConvertChineseUtil.ogPersonMapper.selectPersonListByPIds((String[]) ids.toArray());
	}

	// 人员 部门 基础数据id转名称
	public static void convertIdToName(String code, List<Map<String, Object>> records) {
		if (CollectionUtils.isEmpty(records)) {
			return;
		}
		if (code.equals(WorkOrderInformation.problem.getCode())) {
			records.forEach(problem -> {
				if (ObjectUtil.isNotEmpty(problem.get(ORIGINATOR_ID))) {
					problem.put(ORIGINATOR_ID, idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(problem.get(ORIGINATOR_ID).toString()).getpName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(SOLVER_ID))) {
					problem.put(SOLVER_ID, idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(problem.get(SOLVER_ID).toString()).getpName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(AUDITOR_ID))) {
					problem.put(AUDITOR_ID, idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(problem.get(AUDITOR_ID).toString()).getpName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(ORI_DEP_MANAGER_ID))) {
					problem.put(ORI_DEP_MANAGER_ID, idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(problem.get(ORI_DEP_MANAGER_ID).toString()).getpName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(MANAGER_ID))) {
					List<String> nameList = Lists.newArrayList();
					for (String id : problem.get(MANAGER_ID).toString().split(",")) {
						nameList.add(idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(id).getpName());
					}
					problem.put(MANAGER_ID, StringUtils.join(nameList, ","));
				}
				if (ObjectUtil.isNotEmpty(problem.get(CURRENT_HANDLER_ID))) {
					List<String> nameList = Lists.newArrayList();
					for (String id : problem.get(CURRENT_HANDLER_ID).toString().split(",")) {
						nameList.add(idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(id).getpName());
					}
					problem.put(CURRENT_HANDLER_ID, StringUtils.join(nameList, ","));
				}
				if (ObjectUtil.isNotEmpty(problem.get(SOLVER_DEP_ID))) {
					problem.put(SOLVER_DEP_ID, idCodeConvertChineseUtil.commonTreeService
							.selectCommonTreeById(Long.valueOf(problem.get(SOLVER_DEP_ID).toString())).getName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(INTERRUPT_FLAG))) {
					problem.put(INTERRUPT_FLAG,
							StringUtils.equals(InterruptFlag.ONE, problem.get(INTERRUPT_FLAG).toString())
									? InterruptFlag.CH_ZERO
									: InterruptFlag.CH_ONE);
				}
				if (ObjectUtil.isNotEmpty(problem.get(CAUSE_CLZ1))) {
					problem.put(CAUSE_CLZ1, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(problem.get(CAUSE_CLZ1).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(CAUSE_CLZ2))) {
					List<OgTypeinfo> ogTypeinfos = idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNoForProblem(problem.get(CAUSE_CLZ2).toString());
					problem.put(CAUSE_CLZ2, ogTypeinfos.get(0).getTypeName());
				}
                if (ObjectUtil.isNotEmpty(problem.get("system_id"))) {
                	// 处理数据
					String systemId = (String)problem.get("system_id");
					List<String> codes = JSON.parseArray(systemId, String.class);
					StringBuilder stringBuilder = new StringBuilder();
					for (String sysCode : codes) {
						OgSys ogSys = idCodeConvertChineseUtil.applicationManagerService.selectOgSysBySysCodeForProblem(sysCode);
						String caption = ogSys.getCaption();
						stringBuilder.append(caption).append(",");
					}
					problem.put("system_id", stringBuilder.substring(0, stringBuilder.lastIndexOf(",")));
                }
				if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_CATEGORY))) {
					problem.put(PROBLEM_CATEGORY, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_CATEGORY).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(problem.get("org_id"))) {
					problem.put("org_id", idCodeConvertChineseUtil.iSysDeptService
							.selectDeptById(problem.get("org_id").toString()).getOrgName());
				}
				if (ObjectUtil.isNotEmpty(problem.get("ori_dep_id"))) {
					problem.put("ori_dep_id", idCodeConvertChineseUtil.iSysDeptService
							.selectDeptById(problem.get("ori_dep_id").toString()).getOrgName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_SUBCLZ))) {
					problem.put(PROBLEM_SUBCLZ, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_SUBCLZ).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_ENTRY))) {
					problem.put(PROBLEM_ENTRY, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_ENTRY).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_SUBENTRY1))) {
					problem.put(PROBLEM_SUBENTRY1, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_SUBENTRY1).toString()).getTypeName());
				}
			});
		} else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
			records.forEach(task -> {
				if (StringUtils.isNotEmpty(task.get(CURRENT_HANDLER_ID_TASK))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(task.get(CURRENT_HANDLER_ID_TASK).toString());
					if (person != null)
						task.put(CURRENT_HANDLER_ID_TASK, person.getpName());
				}
				if (StringUtils.isNotEmpty(task.get(ORIGINATOR_ID))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(task.get(ORIGINATOR_ID).toString());
					if (person != null)
						task.put(ORIGINATOR_ID, person.getpName());
				}
				// assistant_id
				if (StringUtils.isNotEmpty(task.get("assistant_id"))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(task.get("assistant_id").toString());
					if (person != null)
						task.put("assistant_id", person.getpName());
				}
				if (ObjectUtil.isNotEmpty(task.get("system_id"))) {
					// 处理数据
					String systemId = (String)task.get("system_id");
					List<String> codes = JSON.parseArray(systemId, String.class);
					StringBuilder stringBuilder = new StringBuilder();
					for (String sysCode : codes) {
						OgSys ogSys = idCodeConvertChineseUtil.applicationManagerService.selectOgSysBySysCodeForProblem(sysCode);
						String caption = ogSys.getCaption();
						stringBuilder.append(caption).append(",");
					}
					task.put("system_id", stringBuilder.substring(0, stringBuilder.lastIndexOf(",")));
				}
				if (ObjectUtil.isNotEmpty(task.get(PROBLEM_CATEGORY))) {
					task.put(PROBLEM_CATEGORY, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(task.get(PROBLEM_CATEGORY).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(task.get(PROBLEM_SUBCLZ))) {
					task.put(PROBLEM_SUBCLZ, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(task.get(PROBLEM_SUBCLZ).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(task.get(PROBLEM_ENTRY))) {
					task.put(PROBLEM_ENTRY, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(task.get(PROBLEM_ENTRY).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(task.get(PROBLEM_SUBENTRY1))) {
					task.put(PROBLEM_SUBENTRY1, idCodeConvertChineseUtil.ogTypeinfoService
							.selectOgTypeInfoByTypeNo(task.get(PROBLEM_SUBENTRY1).toString()).getTypeName());
				}
				if (ObjectUtil.isNotEmpty(task.get("assistant_dept_id"))) {
					task.put("assistant_dept_id", idCodeConvertChineseUtil.commonTreeService
							.selectCommonTreeById(Long.valueOf(task.get("assistant_dept_id").toString())).getName());
				}
				if (ObjectUtil.isNotEmpty(task.get("assistant_dept_id"))) {
					task.put("ori_dep_id", idCodeConvertChineseUtil.commonTreeService
							.selectCommonTreeById(Long.valueOf(task.get("ori_dep_id").toString())).getName());
				}
			});
		} else if (code.equals(WorkOrderInformation.incident.getCode())) {
			records.forEach(event -> {
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.REPORT_ORG))) {
					OgOrg reportOrg = idCodeConvertChineseUtil.iSysDeptService
							.selectDeptById(event.get(EventFieldConstants.REPORT_ORG).toString());
					if (reportOrg != null) {
						event.put(EventFieldConstants.REPORT_ORG, reportOrg.getOrgName());
					}
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.REPORT_PERSON))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(event.get(EventFieldConstants.REPORT_PERSON).toString());
					if (person != null)
						event.put(EventFieldConstants.REPORT_PERSON, person.getpName());
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ASSIGNED_GROUP))) {
					CommonTree commonTree = idCodeConvertChineseUtil.commonTreeService
							.selectCommonTreeByOgId(event.get(EventFieldConstants.ASSIGNED_GROUP).toString());
					if (commonTree != null)
						event.put(EventFieldConstants.ASSIGNED_GROUP, commonTree.getName());
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ASSIGNED_PERSON))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(event.get(EventFieldConstants.ASSIGNED_PERSON).toString());
					if (person != null)
						event.put(EventFieldConstants.ASSIGNED_PERSON, person.getpName());
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.SECOND_DEAL_ORG))) {
					OgOrg ogOrg = idCodeConvertChineseUtil.iSysDeptService
							.selectDeptById(event.get(EventFieldConstants.SECOND_DEAL_ORG).toString());
					if (ogOrg != null)
						event.put(EventFieldConstants.SECOND_DEAL_ORG, ogOrg.getOrgName());
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.SECOND_DEAL_PERSON))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(event.get(EventFieldConstants.SECOND_DEAL_PERSON).toString());
					if (person != null)
						event.put(EventFieldConstants.SECOND_DEAL_PERSON, person.getpName());
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.SIDE_FLAG))) {
					List<PubParaValue> list = idCodeConvertChineseUtil.pubParaValueService
							.selectPubParaValueByParaName("side_flag");
					event.put(EventFieldConstants.SIDE_FLAG, EventFieldConstants.convertParaList(list,
							event.get(EventFieldConstants.SIDE_FLAG).toString()));
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.FINANCE_FLAG))) {
					event.put(EventFieldConstants.FINANCE_FLAG, EventFieldConstants
							.convertFinanceFlag(event.get(EventFieldConstants.FINANCE_FLAG).toString()));
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.URGENT_FLAG))) {
					event.put(EventFieldConstants.URGENT_FLAG, EventFieldConstants
							.convertFinanceFlag(event.get(EventFieldConstants.URGENT_FLAG).toString()));
				}
				if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ORG_FLAG))) {
					List<PubParaValue> list = idCodeConvertChineseUtil.pubParaValueService
							.selectPubParaValueByParaName("org_flag");
					event.put(EventFieldConstants.ORG_FLAG, EventFieldConstants.convertParaList(list,
							event.get(EventFieldConstants.ORG_FLAG).toString()));
				}
				if (StringUtils.isNotEmpty(event.get("extra5"))) {
					String id = (String) event.get("extra5");
					String currentDealName = "";
					if (StringUtils.isNotEmpty(id)) {
						OgPerson person = idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(id);
						currentDealName += person != null ? person.getpName() : "";

						/*
						 * OgGroup ogGroup =
						 * idCodeConvertChineseUtil.sysWorkService.selectOgGroupById(id);
						 * currentDealName += ogGroup != null ? ogGroup.getGrpName() : "";
						 * 
						 * OgOrg ogOrg = idCodeConvertChineseUtil.iSysDeptService.selectDeptById(id);
						 * currentDealName += ogOrg != null ? ogOrg.getOrgName() : "";
						 */
					}
					event.put("extra5", currentDealName);
				}
				if (StringUtils.isNotEmpty(event.get("solve_person"))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(String.valueOf(event.get("solve_person")));
					if (person != null) {
						event.put("solve_person", person.getpName());
					}
				}
				if (StringUtils.isNotEmpty(event.get("solve_org"))) {
					OgOrg ogOrg = idCodeConvertChineseUtil.iSysDeptService
							.selectDeptById(String.valueOf(event.get("solve_org")));
					if (ogOrg != null) {
						event.put("solve_person", ogOrg.getOrgName());
					}
				}

				if (StringUtils.isNotEmpty(event.get("solve_group"))) {
					OgGroup ogGroup = idCodeConvertChineseUtil.sysWorkService
							.selectOgGroupById(String.valueOf(event.get("solve_group")));
					if (ogGroup != null) {
						event.put("solve_group", ogGroup.getGrpName());
					}
				}
			});
		} else if (code.equals(WorkOrderInformation.TINYWEB_DB_RECOVER.getCode())) {
			// tinyWeb 数据库恢复 列表 部分字段ID转译
			records.forEach(dbRecover -> {
				if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.APP_SYSTEM))) {
					OgSys ogSys = idCodeConvertChineseUtil.applicationManagerService
							.selectOgSysBySysId(dbRecover.get(TinywebConstants.APP_SYSTEM).toString());
					dbRecover.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
				}
				if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.CHECK_PEOPLE))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonEvoById(dbRecover.get(TinywebConstants.CHECK_PEOPLE).toString());
					dbRecover.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
				}
				if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.DESENSITIZATION_TYPE))) {

					List<PubParaValue> list = idCodeConvertChineseUtil.pubParaValueService
							.selectPubParaValueByParaName(TinywebConstants.DESENSITIZATION_TYPE);
					for (PubParaValue pubParaValue : list) {
						if (pubParaValue.getValue().equals(dbRecover.get(TinywebConstants.DESENSITIZATION_TYPE))) {
							dbRecover.put(TinywebConstants.DESENSITIZATION_TYPE, pubParaValue.getValueDetail());
							break;
						}
					}
				}
			});
		} else if (code.equals(WorkOrderInformation.TINYWEB_FAULT_SOLVE.getCode())) {
			// tinyWeb 故障解决 列表 部分字段ID转译
			records.forEach(faultSolve -> {
				if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.APP_SYSTEM))) {
					OgSys ogSys = idCodeConvertChineseUtil.applicationManagerService
							.selectOgSysBySysId(faultSolve.get(TinywebConstants.APP_SYSTEM).toString());
					faultSolve.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
				}
				if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.CHECK_PEOPLE))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonEvoById(faultSolve.get(TinywebConstants.CHECK_PEOPLE).toString());
					faultSolve.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
				}
				if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.MALFUNCTION_TYPE))) {

					List<PubParaValue> list = idCodeConvertChineseUtil.pubParaValueService
							.selectPubParaValueByParaName(TinywebConstants.FAULT_TYPE);
					for (PubParaValue pubParaValue : list) {
						String type = (String) faultSolve.get(TinywebConstants.MALFUNCTION_TYPE);
						if (pubParaValue.getValue().equals(type)) {
							faultSolve.put(TinywebConstants.MALFUNCTION_TYPE, pubParaValue.getValueDetail());
							break;
						}
					}
				}
			});
		} else if (code.equals(WorkOrderInformation.TINYWEB_SERVER.getCode())) {
			// tinyWeb 服务请求 列表 部分字段ID转译
			records.forEach(server -> {
				if(isNumeric(server.get(TinywebConstants.APP_SYSTEM).toString())){
					if (StringUtils.isNotEmpty(server.get(TinywebConstants.APP_SYSTEM))) {
						OgSys ogSys = idCodeConvertChineseUtil.applicationManagerService
								.selectOgSysBySysId(server.get(TinywebConstants.APP_SYSTEM).toString());
						server.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
					}
				}
				if (StringUtils.isNotEmpty(server.get(TinywebConstants.CHECK_PEOPLE))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonEvoById(server.get(TinywebConstants.CHECK_PEOPLE).toString());
					server.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
				}
			});
		} else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
			records.forEach(cell -> {
				if (StringUtils.isNotEmpty(cell.get("current_handler_id"))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonEvoById(cell.get("current_handler_id").toString());
					cell.put("current_handler_id", person.getpName());
				}
				if (StringUtils.isNotEmpty(cell.get("originator_id"))) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonEvoById(cell.get("originator_id").toString());
					cell.put("originator_id", person.getpName());
				}
			});
		} else if (code.equals(WorkOrderInformation.changeTask.getCode())) {
			records.forEach(cell -> {
				Object type = cell.get(ChangeTaskFieldEnum.TYPE.getName());
				Object preApproval = cell.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName());
				Object assignee = cell.get(ChangeTaskFieldEnum.ASSIGNEE.getName());
				Object assignedGroup = cell.get(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName());
				Object referSys = cell.get(ChangeTaskFieldEnum.REFER_SYS.getName());
				Object fundamental = cell.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
				Object autoVerifier = cell.get("autoVerifier");
				Object checkMan = cell.get(ChangeTaskFieldEnum.CHECK_MAN.getName());
				Object implMan = cell.get(ChangeTaskFieldEnum.IMPL_MAN.getName());
				Object remedyReason = cell.get(ChangeTaskFieldEnum.REMEDY_REASON.getName());
				if (StringUtils.isNotEmpty(type)) {
					ChangeTaskScene changeTaskScene = idCodeConvertChineseUtil.changeTaskSceneService
							.getChangeTaskSceneByCode(type.toString());
					if (changeTaskScene != null) {
						String name = changeTaskScene.getName();
						cell.put(ChangeTaskFieldEnum.TYPE.getName(), name);
					}

				}
				if (StringUtils.isNotEmpty(implMan)) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(implMan.toString());
					OgUser ogUser = idCodeConvertChineseUtil.ogUserService.selectOgUserByUserId(implMan.toString());
					String name = person.getpName();
					if (ogUser != null) {
						name = name + "(" + ogUser.getUsername() + ")";
					}
					cell.put(ChangeTaskFieldEnum.IMPL_MAN.getName(), name);
				}
				if (StringUtils.isNotEmpty(checkMan)) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(checkMan.toString());
					OgUser ogUser = idCodeConvertChineseUtil.ogUserService.selectOgUserByUserId(checkMan.toString());
					String name = person.getpName();
					if (ogUser != null) {
						name = name + "(" + ogUser.getUsername() + ")";
					}
					cell.put(ChangeTaskFieldEnum.CHECK_MAN.getName(), name);
				}
				if (StringUtils.isNotEmpty(autoVerifier)) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(autoVerifier.toString());
					OgUser ogUser = idCodeConvertChineseUtil.ogUserService
							.selectOgUserByUserId(autoVerifier.toString());
					String name = person.getpName();
					if (ogUser != null) {
						name = name + "(" + ogUser.getUsername() + ")";
					}
					cell.put("autoVerifier", name);
				}
				if (StringUtils.isNotEmpty(preApproval)) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService
							.selectOgPersonById(preApproval.toString());
					OgUser ogUser = idCodeConvertChineseUtil.ogUserService.selectOgUserByUserId(preApproval.toString());
					String name = person.getpName();
					if (ogUser != null) {
						name = name + "(" + ogUser.getUsername() + ")";
					}
					cell.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), name);
				}
				if (StringUtils.isNotEmpty(assignee)) {
					OgPerson person = idCodeConvertChineseUtil.ogPersonService.selectOgPersonById(assignee.toString());
					OgUser ogUser = idCodeConvertChineseUtil.ogUserService.selectOgUserByUserId(assignee.toString());
					String name = "";
					if(person!=null) {name = person.getpName();}
					if (ogUser != null) {
						name = name + "(" + ogUser.getUsername() + ")";
					}
					cell.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), name);
				}
				if (StringUtils.isNotEmpty(assignedGroup)) {
					OgOrg ogOrg = idCodeConvertChineseUtil.iSysDeptService.selectDeptById(assignedGroup.toString());
					String name = ogOrg.getOrgName();
					String parent = ogOrg.getParentName();
					if (parent != null) {
						name = parent + "-" + name;
					}
					cell.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), name);
				}
				if (StringUtils.isNotEmpty(referSys)) {
					OgSys ogSys = idCodeConvertChineseUtil.applicationManagerService
							.selectOgSysBySysCode(referSys.toString());
					if(ogSys!=null){
						String name = ogSys.getCaption() + "(" + referSys.toString() + ")";
						cell.put(ChangeTaskFieldEnum.REFER_SYS.getName(), name);
					}
				}
				if (StringUtils.isNotEmpty(fundamental)) {
					String str = fundamental.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "")
							.replaceAll(",", "；");
					cell.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), str);
				}
				if (StringUtils.isNotEmpty(remedyReason)) {
					List<PubParaValue> pubParaValueList = idCodeConvertChineseUtil.pubParaValueService
							.selectPubParaValueByParaName("change_task_defect_reason");
					String str = remedyReason.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
					String[] array = str.split(",");
					StringJoiner stringJoiner = new StringJoiner("，");
					for (int i = 0; i < array.length; ++i) {
						String detailValue = array[i];
						pubParaValueList.stream().filter(p -> detailValue.equals(p.getValue())).findFirst()
								.ifPresent(pubParaValue -> stringJoiner.add(pubParaValue.getValueDetail()));
					}
					String reason = stringJoiner.toString();
					cell.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), reason);
				}
			});
		}
		if (code.equals(WorkOrderInformation.chm_task.getCode())) {
			records.forEach(record -> {
				// 故障类型
				String chmType = record.get("chm_type") == null ? "" : record.get("chm_type").toString();
				record.put("chm_type", "1".equals(chmType) ? "硬件" : "软件");
				ChmParavalue chmParavalue = idCodeConvertChineseUtil.iChmParavalueService
						.selectChmParavalueById(Long.valueOf(record.get("equipment_type").toString()));
				record.put("equipment_type", chmParavalue.getName());

				ChmParavalue chmParavalue1 = idCodeConvertChineseUtil.iChmParavalueService
						.selectChmParavalueById(Long.valueOf(record.get("equipment_name").toString()));
				record.put("equipment_name", chmParavalue1.getName());

				ChmParavalue chmParavalue2 = idCodeConvertChineseUtil.iChmParavalueService
						.selectChmParavalueById(Long.valueOf(record.get("equipment_model").toString()));
				record.put("equipment_model", chmParavalue2.getName());
				String scopeInfluence = record.get("scope_influence") == null ? ""
						: record.get("scope_influence").toString();
				if (StringUtils.isNotEmpty(scopeInfluence)) {
					record.put("scope_influence", ChmSFnum.getInfo(scopeInfluence));
				}
				String reportDepartment = record.get("report_department").toString();
				ChmBasedata chmBasedata = idCodeConvertChineseUtil.iChmBasedataService
						.selectChmBasedataById(Long.valueOf(reportDepartment));
				record.put("report_department", chmBasedata.getOrgName());
				String urgent = record.get("urgent") == null ? "" : record.get("urgent").toString();
				record.put("urgent", "0".equals(urgent) ? "否" : "是");
				String telephone_follow = record.get("telephone_follow") == null ? ""
						: record.get("telephone_follow").toString();
				if ("0".equals(telephone_follow)) {
					record.put("telephone_follow", "否");
				}
				if ("1".equals(telephone_follow)) {
					record.put("telephone_follow", "是");
				}

			});

		}
	}

	public static void convertEnumToName(String code, List<Map<String, Object>> records) {
		if (WorkOrderInformation.change.getCode().equals(code)) {
			for (Map<String, Object> record : records) {
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					String key = entry.getKey();
					if (ChangeFieldParamMap.fieldList.contains(key)) {
						String param = ChangeFieldParamMap.valueOf(key).getParam();
						List<PubParaValue> pubParaList = idCodeConvertChineseUtil.pubParaValueMapper
								.selectPubParaValueByParaName(param);
						if (CollectionUtil.isNotEmpty(pubParaList)) {
							Object oldObj = entry.getValue();
							String old = String.valueOf(oldObj);
							pubParaList.stream().filter(p -> p.getValue().equals(old)).findFirst()
									.ifPresent(pubParaValue -> entry.setValue(pubParaValue.getValueDetail()));
						}
					}
				}
			}
		} else if (WorkOrderInformation.changeTask.getCode().equals(code)) {
			for (Map<String, Object> record : records) {
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					String key = entry.getKey();
					if (ChangeTaskFieldParamMap.fieldList.contains(key)) {
						String param = ChangeTaskFieldParamMap.valueOf(key).getParam();
						List<PubParaValue> pubParaList = idCodeConvertChineseUtil.pubParaValueMapper
								.selectPubParaValueByParaName(param);
						if (CollectionUtil.isNotEmpty(pubParaList)) {
							Object oldObj = entry.getValue();
							String old = String.valueOf(oldObj);
							pubParaList.stream().filter(p -> p.getValue().equals(old)).findFirst()
									.ifPresent(pubParaValue -> entry.setValue(pubParaValue.getValueDetail()));
						}
					}
				}
			}
		} else if (WorkOrderInformation.incident.getCode().equals(code)) {
			for (Map<String, Object> record : records) {
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					if (entry.getValue() instanceof String || entry.getValue() instanceof Integer) {
						String paramKey = entry.getKey();
						// 影响程度
						if ("inf_level".equals(entry.getKey())) {
							paramKey = "event_impact_degree";
						}
						entry.setValue(DictUtils.getParaValue(paramKey, String.valueOf(entry.getValue())));
					}
				}
			}
		} else {
			QueryWrapper<OgTypeinfo> queryWrapper = new QueryWrapper<>();
			queryWrapper.select("type_no,type_name");
			List<Map<String, Object>> treeList = idCodeConvertChineseUtil.ogTypeinfoMapper.selectMaps(queryWrapper);
			List<Map<String, String>> allParams = idCodeConvertChineseUtil.pubParaValueMapper.selectAllParams();
			for (Map<String, Object> record : records) {
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					// popType不进行任何处理
					if (record.containsKey("popType")) {
						continue;
					}
					Map<String, String> map = allParams.stream().filter(a -> entry.getKey().equals(a.get("para_name"))
							&& (ObjectUtil.isEmpty(entry.getValue()) ? "not value" : String.valueOf(entry.getValue()))
									.equals(a.get("code_value")))
							.findFirst().orElse(null);
					if (CollectionUtil.isNotEmpty(map)) {
						entry.setValue(map.get("value_detail"));
					} else {
						List<Map<String, Object>> typeNo = treeList
								.stream().filter(
										a -> String.valueOf(a.get("type_no"))
												.equals(ObjectUtil.isEmpty(entry.getValue()) ? "not value"
														: String.valueOf(entry.getValue())))
								.collect(Collectors.toList());
						if (CollectionUtil.isNotEmpty(typeNo)) {
							entry.setValue(typeNo.get(0).get("type_name").toString());
						}
					}
				}
			}
		}
	}

	public static void convertEnumToNameSet(String code, List<Map<String, Object>> records) {
		if (WorkOrderInformation.incident.getCode().equals(code)) {
			for (Map<String, Object> record : records) {
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					String paramKey = entry.getKey();
					if ("urgent_flag".equals(paramKey) || "finance_flag".equals(paramKey)
							|| "change_flag".equals(paramKey) || "event_source".equals(paramKey)
							|| "close_code".equals(paramKey) || "solution_valid_flag".equals(paramKey)
							|| "inf_use".equals(paramKey) || "inf_face".equals(paramKey)
							|| "mointor_invalid".equals(paramKey)) {
						entry.setValue(DictUtils.getParaValue(paramKey, String.valueOf(entry.getValue())));
					} else if ("inf_level".equals(entry.getKey())) {
						paramKey = "event_impact_degree";
						entry.setValue(DictUtils.getParaValue(paramKey, String.valueOf(entry.getValue())));
					} else if ("event_reason_category".equals(entry.getKey())) {
						paramKey = "event_reason_category_monitor";
						entry.setValue(DictUtils.getParaValue(paramKey, String.valueOf(entry.getValue())));
					}
				}
			}
		}
	}

	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}
