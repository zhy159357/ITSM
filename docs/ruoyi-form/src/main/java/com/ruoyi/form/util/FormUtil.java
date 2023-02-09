package com.ruoyi.form.util;

import com.ruoyi.common.constant.GenConstants;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.form.domain.ItSourceData;
import com.ruoyi.system.service.IPubParaValueService;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class FormUtil {

    /**
     * 将集合按指定数量分组
     *
     * @param list     数据集合
     * @param quantity 分组数量
     * @return 分组结果
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (list == null || list.size() == 0) {
            return null;
        }
        if (quantity <= 0) {
            new IllegalArgumentException("Wrong quantity.");
        }
        List<List<T>> wrapList = new ArrayList<List<T>>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<T>(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity)));
            count += quantity;
        }
        return wrapList;
    }

    /**
     * 动态拼接form表单，目前支持input、select、textarea三种类型
     * @param taskId  流程任务id
     * @param formId  form表单id标识
     * @param businessId  业务表id
     * @param listMap   需要展示的元数据列表
     * @return
     */
    public static Map<String, Object> packageFormHtml(String taskId, String formId, String businessId, Map<String, List<ItSourceData>> listMap) {
        Map<String, Object> params = new HashMap<>();
        List<String> datetimeIdList = new ArrayList<>();
        IPubParaValueService pubParaValueService = SpringUtils.getBean(IPubParaValueService.class);
        Collection<List<ItSourceData>> dataS = listMap.values();
        StringBuilder formHtmlSb = new StringBuilder();
        formHtmlSb.append("<form id=\"" + formId + "\" class=\"form-horizontal\">");
        if(StringUtils.isNotEmpty(taskId)) {
            formHtmlSb.append("<input id=\"taskId\" name=\"params[taskId]\" value="+ taskId +" type=\"hidden\" />");
        }
        formHtmlSb.append("<input id=\"formId\" name=\"formId\" value="+ formId +" type=\"hidden\" />");
        formHtmlSb.append("<input id=\"businessId\" name=\"businessId\" value="+ businessId +" type=\"hidden\" />");
        for (List<ItSourceData> dataList : dataS) {
            String divRowPrefix = "<div class=\"row\">";
            formHtmlSb.append(divRowPrefix);
            /** -----------组装div行对象开始------------- */
            for (ItSourceData sourceData : dataList) {
                String isRequired = "";
                String isChange = "";
                String fieldHtml = "";
                // 是否必需元素，label标签带星号
                if (GenConstants.REQUIRE.equals(sourceData.getIsRequired())) {
                    isRequired = " is-required";
                }
                // 是否能修改，不能修改设置readonly属性
                if (!GenConstants.REQUIRE.equals(sourceData.getIsChange())) {
                    isChange = " readonly";
                }
                // 拼装input（时间框年月日）、select、textarea三类html标签数据
                if (GenConstants.HTML_INPUT.equals(sourceData.getHtmlType())) {
                    fieldHtml = "<input name=\"" + sourceData.getDataKey() + "\" id=\"" + sourceData.getDataKey() + "\" class=\"form-control\" type=\"text\" " + isChange + " value=\"" + sourceData.getDataValue() + "\"/>\n";
                } else if(GenConstants.HTML_DATETIME.equals(sourceData.getHtmlType())) {
                    fieldHtml = "<input name=\"" + sourceData.getDataKey() + "\" id=\"" + sourceData.getDataKey() + "\" class=\"form-control\" type=\"text\" " + isChange + " value=\"" + sourceData.getDataValue() + "\"/>\n";
                    datetimeIdList.add(sourceData.getDataKey());
                } else if (GenConstants.HTML_SELECT.equals(sourceData.getHtmlType())) {
                    List<PubParaValue> pubParaValues = pubParaValueService.selectPubParaValueByParaName(sourceData.getDictKey());
                    String optionHtml = "";
                    for (PubParaValue p : pubParaValues) {
                        // 字典值与列表匹配，若相等则默认选中，否则正常显示下拉选区域
                        if (sourceData.getDataValue().equals(p.getValue())) {
                            optionHtml += "<option value=" + p.getValue() + " selected=\"selected\">" + p.getValueDetail() + "</option>\n";
                        } else {
                            optionHtml += "<option value=" + p.getValue() + ">" + p.getValueDetail() + "</option>\n";
                        }
                    }
                    fieldHtml = "<select name=\"" + sourceData.getDataKey() + "\" id=\"" + sourceData.getDataKey() + "\" class=\"form-control\" " + isChange + ">\n" +
                            optionHtml +
                            "</select>";
                } else if (GenConstants.HTML_TEXTAREA.equals(sourceData.getHtmlType())) {
                    fieldHtml = "<textarea rows=\"5\" cols=\"10\" name=\"" + sourceData.getDataKey() + "\" id=\"" + sourceData.getDataKey() + "\" class=\"form-control\" " + isChange + ">"
                            + sourceData.getDataValue()
                            + "</textarea>\n";
                }
                String divStr = "<div class=\"col-sm-6\">\n" +
                        "                            <div class=\"form-group\">\n" +
                        "                                <label class=\"col-sm-4 control-label " + isRequired + "\">" + sourceData.getLabelName() + "：</label>\n" +
                        "                                <div class=\"col-sm-8\">\n" +
                        fieldHtml +
                        "                                </div>\n" +
                        "                            </div>\n" +
                        "                        </div>";
                formHtmlSb.append(divStr);
            }
            /** -----------组装div行对象结束------------- */
            String divRowSuffix = "</div>";
            formHtmlSb.append(divRowSuffix);
        }
        formHtmlSb.append("</form>");
        params.put("formHtml", formHtmlSb.toString());
        params.put("datetimeIdList", datetimeIdList);
        return params;
    }
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();

        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

}
