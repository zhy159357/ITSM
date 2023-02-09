package com.ruoyi.activiti.controller;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.ruoyi.activiti.config.ICustomProcessDiagramGenerator;
import com.ruoyi.activiti.config.WorkflowConstants;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.activiti.domain.ProcessDefinition;
import com.ruoyi.activiti.service.ProcessDefinitionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;

@Controller
@RequestMapping("/definition")
public class ProcessDefinitionController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

	private String prefix = "definition";

	@Autowired
	private ProcessDefinitionService processDefinitionService;
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ProcessEngine processEngine;


	@Autowired
	private RuntimeService runtimeService;

	@GetMapping
	public String processDefinition() {
		return prefix + "/definition";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProcessDefinition processDefinition) {
		List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(processDefinition);
		return getDataTable(list);
	}

	/**
	 * 部署流程定义
	 */
	@Log(title = "流程定义", businessType = BusinessType.INSERT)
	@PostMapping("/upload")
	@ResponseBody
	public AjaxResult upload(@RequestParam("processDefinition") MultipartFile file) {
		try {
			if (!file.isEmpty()) {
				String extensionName = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf('.') + 1);
				if (!"bpmn".equalsIgnoreCase(extensionName) && !"zip".equalsIgnoreCase(extensionName)
						&& !"bar".equalsIgnoreCase(extensionName)) {
					return error("流程定义文件仅支持 bpmn, zip 和 bar 格式！");
				}
				// p.s. 此时 FileUploadUtils.upload() 返回字符串 fileName 前缀为
				// Constants.RESOURCE_PREFIX，需剔除
				// 详见: FileUploadUtils.getPathFileName(...)
				String fileName = FileUploadUtils.upload(RuoYiConfig.getProfile() + "/processDefiniton", file);
				if (StringUtils.isNotBlank(fileName)) {
					/*String realFilePath = RuoYiConfig.getProfile()
							+ fileName.substring(Constants.RESOURCE_PREFIX.length());*/
					processDefinitionService.deployProcessDefinition(fileName);
					return success();
				}
			}
			return error("不允许上传空文件！");
		} catch (Exception e) {
			log.error("上传流程定义文件失败！", e);
			return error(e.getMessage());
		}
	}

	@Log(title = "流程定义", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		try {
			return toAjax(processDefinitionService.deleteProcessDeploymentByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	@Log(title = "流程定义", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export() {
		List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(new ProcessDefinition());
		ExcelUtil<ProcessDefinition> util = new ExcelUtil<>(ProcessDefinition.class);
		return util.exportExcel(list, "流程定义数据");
	}

	@PostMapping("/suspendOrActiveApply")
	@ResponseBody
	public AjaxResult suspendOrActiveApply(String id, String suspendState) {
		processDefinitionService.suspendOrActiveApply(id, suspendState);
		return success();
	}

	/**
	 * 读取流程资源
	 *
	 * @param processDefinitionId 流程定义ID
	 * @param resourceName        资源名称
	 */
	@RequestMapping(value = "/readResource")
	public void readResource(@RequestParam("pdid") String processDefinitionId,
			@RequestParam("resourceName") String resourceName, HttpServletResponse response) throws Exception {
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId)
				.singleResult();

		// 通过接口读取
		InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 转换流程定义为模型
	 * 
	 * @param processDefinitionId
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@PostMapping(value = "/convert2Model")
	@ResponseBody
	public AjaxResult convertToModel(@Param("processDefinitionId") String processDefinitionId)
			throws UnsupportedEncodingException, XMLStreamException {
		org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				processDefinition.getResourceName());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);
		Model modelData = repositoryService.newModel();
		modelData.setKey(processDefinition.getKey());
		modelData.setName(processDefinition.getResourceName());
		modelData.setCategory(processDefinition.getDeploymentId());

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);

		repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

		return success();
	}
	@RequestMapping(value = "/read-resource")
	public void readResource(String pProcessInstanceId, HttpServletResponse response) throws Exception {
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		String processDefinitionId = "";
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(pProcessInstanceId).singleResult();
		if (processInstance == null) {
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pProcessInstanceId).singleResult();
			processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		} else {
			processDefinitionId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

		String resourceName = pd.getDiagramResourceName();

		if (resourceName.endsWith(".png") && StringUtils.isEmpty(pProcessInstanceId) == false) {
			getActivitiProccessImage(pProcessInstanceId, response);
			// ProcessDiagramGenerator.generateDiagram(pde, "png",
			// getRuntimeService().getActiveActivityIds(processInstanceId));
		} else {
			// 通过接口读取
			InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

			// 输出资源内容到相应对象
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		}
	}

	/**
	 * 获取流程图像，已执行节点和流程线高亮显示
	 */
	public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
		// logger.info("[开始]-获取流程图图像");
		try {
			// 获取历史流程实例
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pProcessInstanceId).singleResult();

			if (historicProcessInstance == null) {
				// throw new BusinessException("获取流程实例ID[" + pProcessInstanceId +
				// "]对应的历史流程实例失败！");
			} else {
				// 获取流程定义
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

				// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
				List<HistoricActivityInstance> historicActivityInstanceList = historyService
						.createHistoricActivityInstanceQuery().processInstanceId(pProcessInstanceId)
						.orderByHistoricActivityInstanceId().asc().list();

				// 已执行的节点ID集合
				List<String> executedActivityIdList = new ArrayList<String>();
				int index = 1;
				// logger.info("获取已经执行的节点ID");
				for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
					executedActivityIdList.add(activityInstance.getActivityId());

					// logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + "
					// : " +activityInstance.getActivityName());
					index++;
				}

				BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

				// 已执行的线集合
				List<String> flowIds = new ArrayList<String>();
				// 获取流程走过的线 (getHighLightedFlows是下面的方法)
				flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);

//                // 获取流程图图像字符流
//                ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//                //配置字体
//                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,"宋体","微软雅黑","黑体",null,2.0);

				Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(pProcessInstanceId).list()
						.stream().map(e -> e.getActivityId()).collect(Collectors.toSet());

				ICustomProcessDiagramGenerator diagramGenerator = (ICustomProcessDiagramGenerator) processEngine
						.getProcessEngineConfiguration().getProcessDiagramGenerator();
				InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
						flowIds, "宋体", "宋体", "宋体", null, 1.0,
						new Color[] { WorkflowConstants.COLOR_NORMAL, WorkflowConstants.COLOR_CURRENT }, currIds);

				response.setContentType("image/png");
				OutputStream os = response.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				imageStream.close();
			}
			// logger.info("[完成]-获取流程图图像");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// logger.error("【异常】-获取流程图失败！" + e.getMessage());
			// throw new BusinessException("获取流程图失败！" + e.getMessage());
		}
	}

	private List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
											 List<HistoricActivityInstance> historicActivityInstances) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
			// 对历史流程节点进行遍历
			// 得到节点定义的详细信息
			FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess()
					.getFlowElement(historicActivityInstances.get(i).getActivityId());

			List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
			FlowNode sameActivityImpl1 = null;

			HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
			HistoricActivityInstance activityImp2_;

			for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
				activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

				if (activityImpl_.getActivityType().equals("userTask")
						&& activityImp2_.getActivityType().equals("userTask")
						&& df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) // 都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
				{

				} else {
					sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(historicActivityInstances.get(k).getActivityId());// 找到紧跟在后面的一个节点
					break;
				}

			}
			sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

				if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
					FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// 有不相同跳出循环
					break;
				}
			}
			List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

			for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
				FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess()
						.getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return highFlows;

	}
}
