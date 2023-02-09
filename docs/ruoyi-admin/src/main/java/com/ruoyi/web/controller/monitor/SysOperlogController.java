package com.ruoyi.web.controller.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.web.esb.utils.URLCodeUtils;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志记录
 * 
 * @author ruoyi
 */
@Controller
@Configurable
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
	@Value("${itsm.logs}")
	private String logs;

	private String prefix = "monitor/operlog";

	@Autowired
	private ISysOperLogService operLogService;

	@RequiresPermissions("monitor:operlog:view")
	@GetMapping()
	public String operlog() {
		return prefix + "/operlog";
	}

	@GetMapping("/add")
	public String add() {
		return prefix + "/operdownload";
	}

	@RequiresPermissions("monitor:operlog:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysOperLog operLog) {
		startPage();
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		return getDataTable(list);
	}

	@Log(title = "操作日志", businessType = BusinessType.EXPORT)
	@RequiresPermissions("monitor:operlog:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysOperLog operLog) {
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
		return util.exportExcel(list, "操作日志");
	}

	@RequiresPermissions("monitor:operlog:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(operLogService.deleteOperLogByIds(ids));
	}

	@RequiresPermissions("monitor:operlog:detail")
	@GetMapping("/detail/{operId}")
	public String detail(@PathVariable("operId") Long operId, ModelMap mmap) {
		mmap.put("operLog", operLogService.selectOperLogById(operId));
		return prefix + "/detail";
	}

	@Log(title = "操作日志", businessType = BusinessType.CLEAN)
	@RequiresPermissions("monitor:operlog:remove")
	@PostMapping("/clean")
	@ResponseBody
	public AjaxResult clean() {
		operLogService.cleanOperLog();
		return success();
	}

	/**
	 * 附件下载
	 */
	@GetMapping("/downloadInfo")
	public void download(HttpServletResponse response, HttpServletRequest request, String d, String filen, String lines)
			throws Exception {
		// 构建File
		String fileName = "sys-info";
		if ("N".equals(filen)) {
			fileName = "sys-error";
		}
		if (StringUtils.isNotEmpty(d)) {
			fileName = fileName + "." + d;
		}
		fileName = fileName + ".log";
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, URLCodeUtils.encode(fileName)));
		int line = 0;
		if (StringUtils.isNotEmpty(lines)) {
			line = Integer.parseInt(lines);
		}
		File file = new File(logs + File.separator + fileName);
		InputStream in = null;
		ServletOutputStream out = null;
		if (line == 0) {
			try {
				in = new FileInputStream(file);
				out = response.getOutputStream();
				byte[] arr = new byte[1024];
				int len;
				while ((len = in.read(arr)) > 0) {
					out.write(arr, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != in) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != out) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			ReversedLinesFileReader reader = null;
			String LINE_SEPARATOR_WINDOWS = "\r\n";
			try {
				out = response.getOutputStream();
				reader = new ReversedLinesFileReader(file, Charset.defaultCharset());
				List<String> lastNLogLines = getLastNLogLines(reader, line);
//				Collections.reverse(lastNLogLines);// 将文本倒转
				for (final String data : lastNLogLines) {
					if (data != null) {
						out.write(data.toString().getBytes());
					}
					out.write(LINE_SEPARATOR_WINDOWS.getBytes());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != reader) {
					reader.close();
				}
				if (null != out) {
					out.close();
				}
			}
		}
	}

	public static List<String> getLastNLogLines(ReversedLinesFileReader reader, int nLines) throws IOException {
		List<String> lastNLogLines = new ArrayList<>();
		int counter = 0;
			while (counter < nLines) {
				String value = reader.readLine();
				if (value == null) {
					break;
				}
				lastNLogLines.add(0,value);
				counter++;
			}

		return lastNLogLines;
	}

}
