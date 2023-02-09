package com.ruoyi.common.utils.poi;

import com.ruoyi.common.annotation.Word;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.reflect.ReflectUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

public class WordUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(WordUtil.class);

    private final String wordFileSuffix = ".docx";

    /**
     * word文件名称
     */
    private String fileName;

    /**
     * 字段注解map
     */
    private Map<String, Word> wordsMap;

    /**
     * 实体对象
     */
    private T clazz;

    public WordUtil(T clazz) {
        this.clazz = clazz;
    }

    private void init(String wordName) {
        this.fileName = wordName + "-" + DateUtils.dateTimeNow(DateUtils.YYYYMMDD) + wordFileSuffix;
        this.getWordField();
    }

    /**
     * 获取所有的字段带有word注解的组成map集合
     */
    private void getWordField() {
        this.wordsMap = new HashMap<>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getClass().getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getClass().getDeclaredFields()));
        for (Field field : tempFields) {
            // 带有Word注解的字段
            if (field.isAnnotationPresent(Word.class)) {
                wordsMap.put(field.getName(), field.getAnnotation(Word.class));
            }
        }
    }

    /**
     * 导出doc文档具体方法
     * @param in
     * @param wordName
     * @param response
     */
    public void exportWord(InputStream in, String wordName, HttpServletResponse response) {
        this.init(wordName);
        try {
            // 注册IXDocReport实例并加载FreeMarker模板引擎
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
            // 创建IXDocReport上下文对象
            IContext context = report.createContext();
            for (String fieldName : wordsMap.keySet()) {
                context.put(fieldName, getExportWordValue(fieldName, wordsMap.get(fieldName)));
            }

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            report.process(context, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("---------导出word报表失败，失败原因:" + e.getMessage());
        }
    }

    /**
     * 根据字段名称和注解获取对应的value值
     * @param fieldName
     * @param word
     * @return
     */
    public String getExportWordValue(String fieldName, Word word) {
        Object obj = ReflectUtils.invokeGetter(clazz, fieldName);
        String value = "";
        if (StringUtils.isNotNull(obj)) {
            value = obj.toString();
        }
        // 如果注解带有字典项使用字典工具类进行翻译码值
        String paraName = word.paraName();
        if (StringUtils.isNotEmpty(paraName)) {
            value = DictUtils.getParaValue(paraName, value);
        }
        return value;
    }
}
