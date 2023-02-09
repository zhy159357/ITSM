package com.ruoyi.form.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.form.domain.CustomerFormSearchDTO;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.vo.ExportExcelParams;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.CustomerStrategyConstants.EXTRA1;
import static com.ruoyi.form.constants.CustomerStrategyConstants.STATUS;

/**
 * @ClassName CustomerStrategyContext
 * @Description 自定义策略工具类
 * @Author JiaQi Zhang
 * @Date 2022/10/2 17:06
 */
public class CustomerStrategyUtil {


    /**
     * 构造表单追加的json
     * @param data  单条数据
     * @param formName  表单名称
     * @return
     */
    public static List<Map<String, String>>  buildAppendJson(List<Map<String, Object>> data,String formName){
        List<Map<String, String>> appendJsonData=new ArrayList<>();

        Map<String, String> bizNoMap = new HashMap<>();
        bizNoMap.put("label", formName + "编号");
        bizNoMap.put("index", "firstIndex");
        bizNoMap.put("value", Optional.ofNullable(data).isPresent() ? data.get(0).get(EXTRA1).toString() : "");
        appendJsonData.add(bizNoMap);
        Map<String,String> buildAppendJson=new HashMap<>();
        buildAppendJson.put("label", "工单状态");
        buildAppendJson.put("index", "lastIndex");
        buildAppendJson.put("value", Optional.ofNullable(data).isPresent() ? data.get(0).get(STATUS).toString() : "");
        appendJsonData.add(buildAppendJson);
        return appendJsonData;

    }

    /**
     * 结果验空
     * @param params
     * @param customerBusinessEnum
     */
    public static void  verificationNull(Object params, CustomerBusinessEnum customerBusinessEnum){
        if (!Optional.ofNullable(params).isPresent()){
            throw new BusinessException(customerBusinessEnum.getCode(),customerBusinessEnum.getDesc());
        }
    }

    /**
     * 结果验空
     * @param params
     * @param customerBusinessEnum
     */
    public static void  verificationNull(Collection params, CustomerBusinessEnum customerBusinessEnum){
        if (CollectionUtil.isEmpty(params)){
            throw new BusinessException(customerBusinessEnum.getCode(),customerBusinessEnum.getDesc());
        }
    }

    /**
     * 结果验空
     * @param params
     * @param customerBusinessEnum
     */
    public static void  verificationExportIdsNull(Collection params, CustomerBusinessEnum customerBusinessEnum){
        if (CollectionUtil.isEmpty(params)){
            throw new BusinessException(customerBusinessEnum.getCode(),customerBusinessEnum.getDesc());
        }
    }

    /**
     * 构造表头
     * @param exportable 需要导出的字段
     * @return
     */
    public static List<List<String>> head(List<Map<String, String>> exportable) {
        List<List<String>> list = ListUtils.newArrayList();
        exportable.stream().forEach(a->{
            List<String> head = ListUtils.newArrayList();
            head.add(a.get("description"));
            list.add(head);
        });
        return list;
    }


    /**
     * 构造导出的内容
     * @param resultList  数据结果
     * @param exportable  字段集合
     * @return  数据结果
     */
    public static List<List<Object>> dataList(String code,List<Map<String, Object>> resultList,List<Map<String, String>> exportable) {
        List<List<Object>> list = ListUtils.newArrayList();
        List<String> keys=new ArrayList<>();
        IDCodeConvertChineseUtil.convertIdToName(code,resultList);
        IDCodeConvertChineseUtil.convertEnumToName(code,resultList);
        //key按照表头顺序添加到集合
        exportable.forEach(a-> keys.add(a.get("name")));

        resultList.forEach(a->{
            List<Object> data = ListUtils.newArrayList();
            keys.forEach(b->{
                data.add(a.get(b));
            });
            list.add(data);

        });

        return list;
    }


    /**
     * 查询SQL追加
     * @param queryWrapper
     * @param formName
     * @param fields
     */
    public static void  extractedSelectSql(QueryWrapper queryWrapper, String formName, List<Map<String, String>> fields) {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("name", "status");
        statusMap.put("description", "工单状态");
        statusMap.put("display", "1");
        statusMap.put("exportable", "0");
        statusMap.put("editable", "0");
        statusMap.put("record_data_change", "0");
        fields.add(0, statusMap);
        Map<String, String> bizNo = new HashMap<>();
        bizNo.put("name", RedundancyFieldEnum.extra1.name);
        bizNo.put("description", formName + "编号");
        bizNo.put("display", "1");
        bizNo.put("exportable", "0");
        bizNo.put("editable", "0");
        bizNo.put("record_data_change", "0");
        fields.add(0, bizNo);
        String name ="id,"+"instance_id,"+ fields.stream().map(a -> a.get("name")).collect(Collectors.joining(","));
        queryWrapper.select(name).orderByDesc("id");
    }



    /**
     * 搜索条件追加
     * @param queryWrapper
     * @param customerFormSearchDTOS
     * @return
     */
    public static QueryWrapper extractedSqlCondition(QueryWrapper queryWrapper,List<CustomerFormSearchDTO> customerFormSearchDTOS) {
        customerFormSearchDTOS.forEach(a->{
            switch (a.getSearchCondition()) {
                case "eq":
                    queryWrapper.eq(a.getSearchKey(), a.getSearchValue());
                    break;
                case "ne":
                    queryWrapper.ne(a.getSearchKey(), a.getSearchValue());
                case "like":
                    queryWrapper.like(a.getSearchKey(), a.getSearchValue());
                    break;
                case "gt":
                    queryWrapper.gt(a.getSearchKey(), a.getSearchValue());
                    break;
                case "ge":
                    queryWrapper.ge(a.getSearchKey(), a.getSearchValue());
                    break;
                case "lt":
                    queryWrapper.lt(a.getSearchKey(), a.getSearchValue());
                    break;
                case "le":
                    queryWrapper.le(a.getSearchKey(), a.getSearchValue());
                    break;
            }
        });

        return queryWrapper;
    }


    /**
     * 导出excel数据
     * @param resultList   数据结果
     * @param exportTableField  字段对象集合
     * @param formName  表单名
     * @param params  参数
     * @param response  响应结果
     */
    public static void exportExcel(List<Map<String, Object>> resultList,List<Map<String, String>> exportTableField,String formName,ExportExcelParams params, HttpServletResponse response){
        extractedSelectSql(formName,exportTableField);
        //过滤需要导出字段的数据
        List<Map<String, String>> exportable = exportTableField.stream().filter(a -> String.valueOf(a.get("exportable")).equals("1")).collect(Collectors.toList());
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(formName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream()).head(head(exportable)).sheet("模板").doWrite(dataList(params.getCode(),resultList,exportable));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void exportExcelSet(List<Map<String, Object>> resultList,List<Map<String, String>> exportTableField,String formName,ExportExcelParams params, HttpServletResponse response){
        extractedSelectSql(formName,exportTableField);
        try {
        	  List<Map<String, String>> exportable = exportTableField.stream().filter(a -> String.valueOf(a.get("exportable")).equals("1")).collect(Collectors.toList());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(formName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<List<Object>> list = ListUtils.newArrayList();
            List<String> keys=new ArrayList<>();
//            IDCodeConvertChineseUtil.convertIdToName(params.getCode(),resultList);
//            IDCodeConvertChineseUtil.convertEnumToName(params.getCode(),resultList);
            IDCodeConvertChineseUtil.convertEnumToNameSet(params.getCode(),resultList);
            //key按照表头顺序添加到集合
            exportable.forEach(a-> keys.add(a.get("name")));
            resultList.forEach(a->{
                List<Object> data = ListUtils.newArrayList();
                keys.forEach(b->{
                    data.add(a.get(b));
                });
                list.add(data);
            });
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream()).head(head(exportable)).sheet("模板").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 构造导出field对象
     * @param formName  表名
     * @param fields   字段信息
     */
    public static void  extractedSelectSql(String formName, List<Map<String, String>> fields) {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("name", "status");
        statusMap.put("description", "工单状态");
        statusMap.put("display", "1");
        statusMap.put("exportable", "1");
        statusMap.put("editable", "0");
        statusMap.put("record_data_change", "0");
        fields.add(0, statusMap);
        Map<String, String> bizNo = new HashMap<>();
        bizNo.put("name", RedundancyFieldEnum.extra1.name);
        bizNo.put("description", formName + "编号");
        bizNo.put("display", "1");
        bizNo.put("exportable", "1");
        bizNo.put("editable", "0");
        bizNo.put("record_data_change", "0");
        fields.add(0, bizNo);
    }

}
