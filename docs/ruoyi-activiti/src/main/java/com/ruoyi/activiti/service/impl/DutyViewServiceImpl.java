package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.mapper.*;
import com.ruoyi.activiti.service.IDutyViewService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.OgPersonMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * 值班视图Service业务层处理
 * @author dayong_sun
 * @date 2020-12-06
 */
@Service
public class DutyViewServiceImpl implements IDutyViewService {

    @Autowired
    private DutySchedulingMapper dutySchedulingMapper;
    @Autowired
    private DutyVersionMapper dutyVersionMapper;
    @Autowired
    private DutyTypeinfoMapper dutyTypeinfoMapper;
    @Autowired
    private DutyViewMapper dutyViewMapper;
    @Autowired
    private DutySubstituteMapper dutySubstituteMapper;
    @Autowired
    private OgPersonMapper ogPersonMapper;
    @Value("${cntxtag.enabled}")
    private Boolean cntxtag;
    @Value("${pagehelper.helperDialect}")
    private String dbType;
    @Value("${cntxtag.zbzbxx}")
    private String zbzbxx;

    private final String SUCCESS = "1";// 启用状态

    /**
     * 查询值班视图列表
     * @param dutyScheduling 值班视图
     * @return 值班视图
     */
    @Override
    public List<DutyScheduling> selectDutySchedulingList(DutyScheduling dutyScheduling) {
        return dutySchedulingMapper.selectDutySchedulingList(dutyScheduling);
    }

    /**
     * 查询值班视图列表
     * @param dutyVersion 值班视图
     * @return 值班视图
     */
    @Override
    public AjaxResult exportExcel(DutyVersion dutyVersion){
        OutputStream out = null;
        try {
            ExcelUtil excelUtil = ExcelUtil.getExcelUtil();
            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 28);//设置字体大小
            style.setFont(font);//选择需要用到的字体格式
            style.setAlignment(HorizontalAlignment.CENTER);
            HSSFSheet sheet = workbook.createSheet("sheet");

            Map<String,Object> map = this.selectVersionByDutyDate(dutyVersion);
            String title = String.valueOf(map.get("title"));
            int colnum = Integer.valueOf(String.valueOf(map.get("colnum")));
            List<DutyView> list1 = (List)map.get("list1");
            List<DutyView> list2 = (List)map.get("list2");
            List<DutyView> list3 = (List)map.get("list3");
            List<DutyView> list4 = (List)map.get("list4");
            List<DutyView> list5 = (List)map.get("list5");
            List<DutyView> list6 = (List)map.get("list6");
            List<Map<String,Object>> list = (List)map.get("list");

            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell_00 = row0.createCell(0);
            cell_00.setCellStyle(style);
            cell_00.setCellValue(title);
            // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
            // 行和列都是从0开始计数，且起始结束都会合并
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, colnum - 1 );
            sheet.addMergedRegion(region);

            HSSFRow row1 = sheet.createRow(1);
            int rowFlag = 1;
            for(int i = 0;i<list1.size();i++){
                if(1 != rowFlag){
                    HSSFCell cell_10 = row1.createCell(rowFlag + 1);
                    cell_10.setCellStyle(style);
                    cell_10.setCellValue(list1.get(i).getTitle());
                    region = new CellRangeAddress(1, list1.get(i).getRowspan(), rowFlag + 1, rowFlag + Integer.valueOf(list1.get(i).getColspan()));
                }else {
                    if (1 == Integer.valueOf(list1.get(i).getColspan())) {
                        font = workbook.createFont();
                        font.setFontHeightInPoints((short) 12);
                        font.setFontName("黑体");
                        style = workbook.createCellStyle();
                        style.setAlignment(HorizontalAlignment.CENTER);
                        style.setFont(font);//选择需要用到的字体格式

                        region = new CellRangeAddress(1, list1.get(i).getRowspan(), i, i);
                    } else {
                        font = workbook.createFont();
                        font.setFontHeightInPoints((short) 24);
                        font.setFontName("黑体");
                        style = workbook.createCellStyle();
                        style.setAlignment(HorizontalAlignment.CENTER);
                        style.setFont(font);//选择需要用到的字体格式

                        rowFlag = i + Integer.valueOf(list1.get(i).getColspan()) - 1;
                        region = new CellRangeAddress(1, list1.get(i).getRowspan(), i, rowFlag);
                    }
                    HSSFCell cell_10 = row1.createCell(i);
                    cell_10.setCellStyle(style);
                    cell_10.setCellValue(list1.get(i).getTitle());
                }
                sheet.addMergedRegion(region);
            }

            font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("黑体");
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行
            HSSFRow row2 = sheet.createRow(2);

            rowFlag = 3;
            for(int i = 0;i<list2.size();i++){
                HSSFCell cell_10 = row2.createCell(rowFlag);
                cell_10.setCellStyle(style);
                cell_10.setCellValue(list2.get(i).getTitle());
                int col = rowFlag + Integer.valueOf(list2.get(i).getColspan());
                if(1!=Integer.valueOf(list2.get(i).getColspan())){
                    region = new CellRangeAddress(2, 2, rowFlag, col - 1);
                    sheet.addMergedRegion(region);
                }
                rowFlag = col;
            }

            HSSFRow row3 = sheet.createRow(3);
            rowFlag = 3;
            int allcol = 0;
            for(int i = 0;i<list3.size();i++){
                HSSFCell cell_10 = row3.createCell(rowFlag);
                cell_10.setCellStyle(style);
                cell_10.setCellValue(list3.get(i).getTitle());
                int col = rowFlag + Integer.valueOf(list3.get(i).getColspan());
                int row = Integer.valueOf(list3.get(i).getRowspan());
                if(1!=row){
                    region = new CellRangeAddress(3, 3 + row - 1, rowFlag, rowFlag);
                    sheet.addMergedRegion(region);
                }else if(1!=Integer.valueOf(list3.get(i).getColspan())){
                    region = new CellRangeAddress(3, 3, rowFlag, col - 1);
                    sheet.addMergedRegion(region);
                }
                rowFlag = col;
                allcol += list3.get(i).getColspan();
            }

            HSSFRow row4 = sheet.createRow(4);
            rowFlag = 3;
            for(int i = 0;i<list4.size();i++){
                HSSFCellStyle rstyle = workbook.createCellStyle();
                HSSFFont rfont = workbook.createFont();
                rfont.setFontName("黑体");
                rfont.setFontHeightInPoints((short) 12);
                rstyle.setAlignment(HorizontalAlignment.CENTER);
                rstyle.setFont(rfont);//选择需要用到的字体格式
                rstyle.setWrapText(true);//设置自动换行
                if("动力".equals(list4.get(i).getTitle())||"安全".equals(list4.get(i).getTitle())||"灾备".equals(list4.get(i).getTitle())){
                    HSSFCell cell_10 = row4.createCell(allcol-1);
                    cell_10.setCellStyle(rstyle);
                    cell_10.setCellValue(list4.get(i).getTitle());
                    allcol ++;
                }else {
                    HSSFCell cell_10 = row4.createCell(rowFlag);
                    if("1".equals(list4.get(i).getLeader())){
                        rfont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                    }else{
                        rfont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
                    }
                    rstyle.setFont(rfont);
                    cell_10.setCellStyle(rstyle);
                    cell_10.setCellValue(list4.get(i).getTitle());
                    int col = rowFlag + Integer.valueOf(list4.get(i).getColspan());
                    if (1 != Integer.valueOf(list4.get(i).getColspan())) {
                        region = new CellRangeAddress(4, 4, rowFlag, col - 1);
                        sheet.addMergedRegion(region);
                    }
                    rowFlag = col;
                }
            }

            HSSFRow row5 = sheet.createRow(5);
            rowFlag = 3;
            int khCol = 0;
            for(int i = 0;i<list5.size();i++){
                HSSFCell cell_10 = row5.createCell(rowFlag);
                cell_10.setCellStyle(style);
                cell_10.setCellValue(list5.get(i).getTitle());
                int getcol = Integer.valueOf(list5.get(i).getColspan());
                int row = Integer.valueOf(list5.get(i).getRowspan());
                int col = rowFlag + getcol;
                if(1!=row){
                    region = new CellRangeAddress(5, 5 + row - 1, rowFlag, col - 1);
                    sheet.addMergedRegion(region);
                    if(getcol>5){
                        khCol = getcol;
                    }
                }else if(1!=getcol){
                    region = new CellRangeAddress(5, 5, rowFlag, col - 1);
                    sheet.addMergedRegion(region);
                }
                rowFlag = col;
            }

            HSSFRow row6 = sheet.createRow(6);
            rowFlag = 3;
            for(int i = 0;i<list6.size();i++){
                HSSFCell cell_10 = row6.createCell(rowFlag);
                if(i>3){
                    cell_10 = row6.createCell(rowFlag + khCol);
                }
                cell_10.setCellStyle(style);
                cell_10.setCellValue(list6.get(i).getTitle());
                rowFlag ++;

            }

            font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font);//选择需要用到的字体格式
            for(int i = 0;i<list.size();i++){
                HSSFRow row7 = sheet.createRow(7 + i);
                List<String> dataList = (List)list.get(i).get("typeNo");
                List<String> colorList = (List)list.get(i).get("typeColor");
                for(int j = 0;j<dataList.size();j++){
                    HSSFCellStyle rstyle = workbook.createCellStyle();
                    HSSFFont rfont = workbook.createFont();
                    rfont.setFontName("黑体");
                    rfont.setFontHeightInPoints((short) 12);
                    rstyle.setAlignment(HorizontalAlignment.CENTER);
                    rstyle.setFont(rfont);//选择需要用到的字体格式
                    rstyle.setWrapText(true);//设置自动换行
                    HSSFCell cell_10 = row7.createCell(j);
                    if("1".equals(colorList.get(j))){
                        rfont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                    }
                    rstyle.setFont(rfont);
                    cell_10.setCellStyle(rstyle);
                    cell_10.setCellValue(dataList.get(j));
                }
            }
            sheet.setColumnWidth(0,3600);
            for(int x=3;x<colnum;x++){
                sheet.setColumnWidth(x,4000);
            }
            String filename = ExcelUtil.getExcelUtil().encodingFilename(title);
            out = new FileOutputStream(ExcelUtil.getExcelUtil().getAbsoluteFile(filename));
            workbook.write(out);
            return AjaxResult.success(filename);
        }catch (Exception e){
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
    }

    @Override
    public AjaxResult exportExcelMysql(DutyVersion dutyVersion){
        OutputStream out = null;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 28);//设置字体大小
            style.setFont(font);//选择需要用到的字体格式
            style.setAlignment(HorizontalAlignment.CENTER);
            HSSFSheet sheet = workbook.createSheet("sheet");

            Map<String,Object> map = this.selectVersionByDutyDateMysql(dutyVersion);
            String title = String.valueOf(map.get("title"));
            int colnum = Integer.valueOf(String.valueOf(map.get("colnum")));
            List<DutyView> list1 = (List)map.get("list1");
            List<DutyView> list2 = (List)map.get("list2");
            List<Map<String,List<DutyView>>> list4 = (List)map.get("list3");
            List<DutyView> list5 = (List)map.get("list5");
            List<Map<String,String>> list6 = (List)map.get("list6");
            List<DutyVersion> cdvs = (List)map.get("cdvs");
            List<DutyView> list7 = (List)map.get("list7");
            List<String> list8 = (List)map.get("list8");
            Map<String,List<DutyView>> sldMap = (Map)map.get("sldmap");

            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell_00 = row0.createCell(0);
            cell_00.setCellStyle(style);
            cell_00.setCellValue(title);
            // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列) 行和列都是从0开始计数，且起始结束都会合并
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, colnum - 1 );
            sheet.addMergedRegion(region);

            style = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeightInPoints((short) 18);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font);//选择需要用到的字体格式
            HSSFRow row1 = sheet.createRow(1);
            int rowFlag = 1;
            for(int i = 0;i<list1.size();i++){
                if(1 != rowFlag){
                    HSSFCell cell_10 = row1.createCell(rowFlag + 1);
                    cell_10.setCellStyle(style);
                    cell_10.setCellValue(list1.get(i).getTitle());
                    region = new CellRangeAddress(1, list1.get(i).getRowspan(), rowFlag + 1, rowFlag + Integer.valueOf(list1.get(i).getColspan()));
                    rowFlag = rowFlag + Integer.valueOf(list1.get(i).getColspan());
                }else {
                    if (1 == Integer.valueOf(list1.get(i).getColspan())) {
                        region = new CellRangeAddress(1, list1.get(i).getRowspan(), i, i);
                    } else {
                        rowFlag = i + Integer.valueOf(list1.get(i).getColspan()) - 1;
                        region = new CellRangeAddress(1, list1.get(i).getRowspan(), i, rowFlag);
                    }
                    HSSFCell cell_10 = row1.createCell(i);
                    cell_10.setCellStyle(style);
                    cell_10.setCellValue(list1.get(i).getTitle());
                    sheet.setColumnWidth(0,3600);
                    for(int x=1;x<colnum;x++){
                        sheet.setColumnWidth(x,4000);
                    }
                }
                sheet.addMergedRegion(region);
            }

            font = workbook.createFont();
            font.setFontHeightInPoints((short) 18);
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font);//选择需要用到的字体格式
            style.setWrapText(true);//设置自动换行

            HSSFRow row2 = sheet.createRow(2);
            rowFlag = 1;
            for(int i = 0;i<list2.size();i++){
                HSSFCell cell_10 = row2.createCell(rowFlag);
                cell_10.setCellStyle(style);
                cell_10.setCellValue(list2.get(i).getTitle());
                int col = rowFlag + Integer.valueOf(list2.get(i).getColspan());
                if(1!=Integer.valueOf(list2.get(i).getColspan())){
                    region = new CellRangeAddress(2, 2, rowFlag, col - 1);
                    sheet.addMergedRegion(region);
                }
                rowFlag = col;
            }

            int rc = 3;
            for(int k=0;k<list4.size();k++){
                HSSFRow row3 = sheet.createRow(rc++);
                Map<String,List<DutyView>> msl = list4.get(k);
                List<DutyView> ldv = msl.get(list5.get(k));
                Map<String,String> li6 = list6.get(k);
                HSSFCell cell_30 = row3.createCell(0);
                cell_30.setCellStyle(style);
                cell_30.setCellValue(li6.get(list5.get(k)));
                sheet.setColumnWidth(0,10800);
                if("c002".equals(list5.get(k))){
                    region = new CellRangeAddress(rc-1, rc, 0, 0);
                    sheet.addMergedRegion(region);
                    for(int si=0;si<list8.size();si++) {
                        rowFlag = 1;
                        if(si==0) {
                            List<DutyView> ldu = sldMap.get(list8.get(si));
                            for (int jk = 0; jk < ldu.size(); jk++) {
                                DutyView dut = ldu.get(jk);
                                HSSFCell cell_10 = row3.createCell(rowFlag);
                                cell_10.setCellStyle(style);
                                cell_10.setCellValue(dut.getTitle());
                                if (dut.getTitle().length() > 4) {
                                    sheet.setColumnWidth(si + 1, dut.getTitle().length() * 1000);
                                }
                                int col = rowFlag + Integer.valueOf(dut.getColspan());
                                int row = Integer.valueOf(dut.getRowspan());
                                if (1 != row) {
                                    region = new CellRangeAddress(3 + si, si + 3 + row - 1, rowFlag, rowFlag);
                                    sheet.addMergedRegion(region);
                                } else if (1 != Integer.valueOf(dut.getColspan())) {
                                    region = new CellRangeAddress(3, 3, rowFlag, col - 1);
                                    sheet.addMergedRegion(region);
                                    sheet.addMergedRegion(region);
                                }
                                rowFlag = col;
                            }
                        }else{
                            row3 = sheet.createRow(rc++);
                            List<DutyView> ldu = sldMap.get(list8.get(si));
                            for (int jk = 0; jk < ldu.size(); jk++) {
                                DutyView dut = ldu.get(jk);
                                HSSFCell cell_10 = row3.createCell(rowFlag);
                                cell_10.setCellStyle(style);
                                cell_10.setCellValue(dut.getTitle());
                                if (dut.getTitle().length() > 4) {
                                    sheet.setColumnWidth(si + 1, dut.getTitle().length() * 1000);
                                }
                                int col = rowFlag + Integer.valueOf(dut.getColspan());
                                int row = Integer.valueOf(dut.getRowspan());
                                if (1 != row) {
                                    region = new CellRangeAddress(3 + si, si + 3 + row - 1, rowFlag, rowFlag);
                                    sheet.addMergedRegion(region);
                                } else if (1 != Integer.valueOf(dut.getColspan())) {
                                    region = new CellRangeAddress(3, 3, rowFlag, col - 1);
                                    sheet.addMergedRegion(region);
                                    sheet.addMergedRegion(region);
                                }
                                rowFlag = col;

                            }
                        }
                    }
                }else {
                    rowFlag = 1;
                    for (int j = 0; j < ldv.size(); j++) {
                        DutyView duv = ldv.get(j);
                        HSSFCell cell_10 = row3.createCell(rowFlag);
                        cell_10.setCellStyle(style);
                        cell_10.setCellValue(duv.getTitle());
                        if (duv.getTitle().length() > 4) {
                            sheet.setColumnWidth(j + 1, duv.getTitle().length() * 1000);
                        }
                        int col = rowFlag + Integer.valueOf(duv.getColspan());
                        int row = Integer.valueOf(duv.getRowspan());
                        if (1 != row) {
                            region = new CellRangeAddress(3 + j, j + 3 + row - 1, rowFlag, rowFlag);
                            sheet.addMergedRegion(region);
                        } else if (1 != Integer.valueOf(duv.getColspan())) {
                            region = new CellRangeAddress(3, 3, rowFlag, col - 1);
                            sheet.addMergedRegion(region);
                            sheet.addMergedRegion(region);
                        }
                        rowFlag = col;
                    }
                }
            }

            style = workbook.createCellStyle();
            HSSFRow endrow = sheet.createRow(rc);
            HSSFCell endcell = endrow.createCell(0);
            HSSFFont rfont = workbook.createFont();
            rfont.setFontName("黑体");
            rfont.setFontHeightInPoints((short) 28);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(rfont);//选择需要用到的字体格式
            endcell.setCellStyle(style);
            endcell.setCellValue(zbzbxx);
            // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
            // 行和列都是从0开始计数，且起始结束都会合并
            CellRangeAddress regionc = new CellRangeAddress(rc, rc, 0, colnum - 1 );
            sheet.addMergedRegion(regionc);
            String filename = ExcelUtil.getExcelUtil().encodingFilename(title);
            out = new FileOutputStream(ExcelUtil.getExcelUtil().getAbsoluteFile(filename));
            workbook.write(out);
            return AjaxResult.success(filename);
        }catch (Exception e){
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
    }

    /**
     * 根据条件分页查询版本数据
     * @param dutyVersion 版本信息
     * @return 版本数据集合信息
     */
    @Override
    public Map<String,Object> selectVersionByDutyDate(DutyVersion dutyVersion)
    {
        Map<String,Object> map = new HashMap<>();
        map.put("flag",0);
        List<DutyView> list1 = new ArrayList<>();//list1为excel第一行表头
        List<DutyVersion> latelist1 = new ArrayList<>();//预list2为excel第二行表头
        List<DutyView> list2 = new ArrayList<>();//list2为excel第二行表头
        List<DutyVersion> latelist2 = new ArrayList<>();//预list3为excel第三行表头
        List<DutyView> list3 = new ArrayList<>();//list3为excel第三行表头
        List<DutyVersion> latelist3 = new ArrayList<>();//预list4为excel第四行表头
        List<DutyView> list4 = new ArrayList<>();//list4为excel第四行表头
        List<DutyVersion> latelist4 = new ArrayList<>();//预list5为excel第五行表头
        List<DutyView> list5 = new ArrayList<>();//list5为excel第五行表头
        List<DutyVersion> latelist5 = new ArrayList<>();//预list6为excel第六行表头
        List<DutyView> list6 = new ArrayList<>();//list6为excel第六行表头
        List<String> dataList = new ArrayList<>();//存储所有数据
        List<String> colorList = new ArrayList<>();//存储颜色数据
        List<String> typeNoList = new ArrayList<>();//按照顺序存储所有类别编码
        String date = dutyVersion.getDutyDate();
        if(StringUtils.isEmpty(dutyVersion.getDutyDate())){
            date = DateUtils.parseDateToStr(DateUtils.YYYY_MM,DateUtils.getNowDate());
            dutyVersion.setDutyDate(date);
        }
        DutyTypeinfo dutyTypeinfo = dutyTypeinfoMapper.checkTypeNoUnique(cntxtag?VersionStatusConstants.ZBZX_CANO:VersionStatusConstants.ZBZX_NO);
        List<DutyVersion> dutyVersions = dutyVersionMapper.selectVersionByDutyDate(dutyVersion);
        //根据月份查询当月的所有值班信息
        DutyScheduling dutyScheduling = new DutyScheduling();
        dutyScheduling.setDutyDate(date);
        String endTypeNo = "";
        if(null!=dutyVersions&&dutyVersions.size()>0) {
            DutyView dutyDay = new DutyView();
            dutyDay.setTitle("日期");
            dutyDay.setRowspan(6);
            list1.add(dutyDay);
            DutyView dutyWeek = new DutyView();
            dutyWeek.setTitle("星期");
            dutyWeek.setRowspan(6);
            list1.add(dutyWeek);

            for (DutyVersion version : dutyVersions) {
                if (null != dutyTypeinfo && dutyTypeinfo.getTypeinfoId().equals(version.getParentId())) {
                    DutyView dutyView = new DutyView();
                    dutyView.setTitle(version.getTypeName());
                    dutyView.setColspan(Integer.valueOf(version.getTypeRows()==null?"1":version.getTypeRows()));
                    dutyView.setRowspan(Integer.valueOf(version.getTypeColumns()==null?"1":version.getTypeColumns()));
                    dutyView.setTypeDescription(version.getTypeDescription());
                    dutyView.setLeader(version.getLeader());
                    list1.add(dutyView);
                    latelist1.add(version);
                }
            }
            for(DutyVersion late : latelist1){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        DutyView dutyView = new DutyView();
                        dutyView.setTitle(version.getTypeName());
                        dutyView.setColspan(Integer.valueOf(version.getTypeRows()==null?"1":version.getTypeRows()));
                        dutyView.setRowspan(Integer.valueOf(version.getTypeColumns()==null?"1":version.getTypeColumns()));
                        list2.add(dutyView);
                        latelist2.add(version);
                    }
                }
            }
            for(DutyVersion late : latelist2){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        DutyView dutyView = new DutyView();
                        dutyView.setTitle(version.getTypeName());
                        dutyView.setColspan(Integer.valueOf(version.getTypeColumns()==null?"1":version.getTypeColumns()));
                        dutyView.setRowspan(Integer.valueOf(version.getTypeRows()==null?"1":version.getTypeRows()));
                        list3.add(dutyView);
                        latelist3.add(version);
                    }
                }
            }

            for(DutyVersion late : latelist3){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        if("1".equals(late.getTypeRows())){
                            DutyView dutyView = new DutyView();
                            dutyView.setTitle(version.getTypeName());
                            dutyView.setColspan(Integer.valueOf(version.getTypeColumns()==null?"1":version.getTypeColumns()));
                            dutyView.setRowspan(Integer.valueOf(version.getTypeRows()==null?"1":version.getTypeRows()));
                            dutyView.setTypeDescription(version.getTypeDescription());
                            dutyView.setLeader(version.getLeader());
                            list4.add(dutyView);
                        }
                        latelist4.add(version);
                    }
                }
            }
            if(null!=latelist3&&latelist3.size()>0){
                endTypeNo = latelist3.get(latelist3.size() - 1).getTypeNo();
            }
            for(DutyVersion late : latelist4){
                boolean flag = true;
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        DutyView dutyView = new DutyView();
                        dutyView.setTitle(version.getTypeName());
                        dutyView.setColspan(Integer.valueOf(version.getTypeColumns()==null?"1":version.getTypeColumns()));
                        dutyView.setRowspan(Integer.valueOf(version.getTypeRows()==null?"1":version.getTypeRows()));
                        list5.add(dutyView);
                        flag = false;
                        if("1".equals(version.getTypeRows())){
                            latelist5.add(version);
                        }
                    }
                }
                if(flag&&Integer.valueOf(late.getTypeRows()==null?"1":late.getTypeRows())>1){
                    DutyView dutyView = new DutyView();
                    dutyView.setTitle(late.getTypeName());
                    dutyView.setColspan(Integer.valueOf(late.getTypeColumns()==null?"1":late.getTypeColumns()));
                    dutyView.setRowspan(Integer.valueOf(late.getTypeRows()==null?"1":late.getTypeRows()));
                    list5.add(dutyView);
                }
            }
            for(DutyVersion late : latelist5){
                for(DutyVersion version : dutyVersions){
                    if(late.getTypeinfoId().equals(version.getParentId())){
                        DutyView dutyView = new DutyView();
                        dutyView.setTitle(version.getTypeName());
                        dutyView.setColspan(Integer.valueOf(version.getTypeColumns()==null?"1":version.getTypeColumns()));
                        dutyView.setRowspan(Integer.valueOf(version.getTypeRows()==null?"1":version.getTypeRows()));
                        list6.add(dutyView);
                    }
                }
            }

            if(null!=latelist1&&latelist1.size()>0){
                typeNoList.add(latelist1.get(0).getTypeNo());
            }

            for(DutyVersion late : latelist4){
                DutyVersion dv = new DutyVersion();
                dv.setDutyDate(date);
                dv.setParentId(late.getTypeinfoId());
                List<DutyVersion> versions = dutyViewMapper.selectVersionByParentId(dv);
                if(null==versions||versions.size()==0){
                    if("2".equals(late.getTypeRows())&&"5".equals(late.getTypeColumns())){
                        typeNoList.add(late.getTypeNo());
                    }else {
                        int columns = Integer.valueOf(late.getTypeColumns()==null?"1":late.getTypeColumns());
                        typeNoList.add(late.getTypeNo());
                        if(columns>1){
                            dv.setTypeinfoId(late.getParentId());
                            List<DutyVersion> vtypes = dutyVersionMapper.selectVersionByParentId(dv);
                            for(DutyVersion vt : vtypes){
                                typeNoList.add(vt.getTypeNo());
                            }
                        }
                    }
                }else{
                    for(DutyVersion version : versions){
                        dv.setParentId(version.getTypeinfoId());
                        //根据父id查询下级类别
                        List<DutyVersion> vers = dutyViewMapper.selectVersionByParentId(dv);
                        if(null==vers||vers.size()==0){
                            typeNoList.add(version.getTypeNo());
                        }else{
                            for(DutyVersion ver : vers){
                                for(int i=0;i<Integer.valueOf(ver.getTypeColumns()==null?"1":ver.getTypeColumns());i++){
                                    typeNoList.add(ver.getTypeNo());
                                }

                            }
                        }
                    }
                }
            }
            typeNoList.add(endTypeNo);
        }else{
            map.put("flag",1);
            map.put("msg","当前月份没有绑定值班信息！");
        }
        int colnum = 2;//合并的行数
        for(DutyVersion columns : latelist1){
            colnum += Integer.valueOf(columns.getTypeRows()==null?"1":columns.getTypeRows());
        }

        String str[] = date.split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(str[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(str[1]) - 1); // 7月
        int maxDate  =  cal.getActualMaximum(Calendar.DATE);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(maxDate);
        List<DutyScheduling> dutySchedulings = dutyViewMapper.selectViewListByDutyDate(dutyScheduling);
        for(int i=0;i<maxDate;i++){
            cal.set(Calendar.DAY_OF_MONTH, i+1);
            String day = DateUtils.dateTime(cal.getTime());
            Map<String,Object> map1 = new HashMap<>();
            dataList.add(day);
            dataList.add(dateToWeek(day));
            colorList.add("");
            colorList.add("");
            for(String type : typeNoList ){
                int no = 0;
                for(DutyScheduling scheduling : dutySchedulings){
                    if(day.equals(scheduling.getDutyDate())){
                        if(type.equals(scheduling.getTypeNo())){
                            dataList.add(scheduling.getPname());
                            colorList.add(scheduling.getLeader());
                            no += 1;
                            break;
                        }
                    }
                }
                //no为空的表格补位用
                if(0==no){
                    dataList.add("");
                    colorList.add("");
                }
            }
            map1.put("typeNo",dataList);
            map1.put("typeColor",colorList);
            list.add(map1);
            dataList = new ArrayList<>();
            colorList = new ArrayList<>();
        }

        String title = "数据中心"+date+ "值班安排表";//表头
        map.put("colnum",colnum);
        map.put("title",title);
        map.put("list1",list1);
        map.put("list2",list2);
        map.put("list3",list3);
        map.put("list4",list4);
        map.put("list5",list5);
        map.put("list6",list6);
        map.put("list",list);
        map.put("typeNoList",typeNoList);
        return map;
    }

    @Override
    public Map<String,Object> selectVersionByDutyDateMysql(DutyVersion dutyVersion)
    {
        Map<String,Object> map = new HashMap<>();
        map.put("flag",0);
        List<DutyView> list1 = new ArrayList<>();//list1为excel第一行表头
        List<DutyView> list2 = new ArrayList<>();//list2为excel第二行表头
        List<DutyView> list3 = new ArrayList<>();//list3为excel第三行表头
        List<Map<String,List<DutyView>>> list4 = new ArrayList();
        List<String> list5 = new ArrayList();
        List<Map<String,String>> list6 = new ArrayList();
        List<DutyVersion> cdvs = new ArrayList();//值班大厅
        List<DutyView> list7 = new ArrayList<>();//值班大厅
        List<String> list8 = new ArrayList();
        Map<String,List<DutyView>> sldmap = new HashMap<>();
        Map<String,String> ssmap = new HashMap<>();
        int colnum = 2;//合并的行数
        List<String> dateList = new ArrayList<>();//日期list
        String date = dutyVersion.getDutyDate();
        if(StringUtils.isEmpty(dutyVersion.getDutyDate())){
            date = DateUtils.parseDateToStr(DateUtils.YYYY_MM,DateUtils.getNowDate());
            dutyVersion.setDutyDate(date);
        }
        String caFlag = cntxtag?VersionStatusConstants.ZBZX_CANO:VersionStatusConstants.ZBZX_NO;
        dutyVersion.setTypeNo(caFlag);
        List<DutyVersion> dutyVersions = dutyVersionMapper.selectVersionByDutyDate(dutyVersion);
        //根据月份查询当月的所有值班信息
        String trhtml = "";
        if(null!=dutyVersions&&dutyVersions.size()>0) {
            DutyView dutyDay = new DutyView();
            dutyDay.setTitle("日期");
            dutyDay.setRowspan(2);
            list1.add(dutyDay);
            String str[] = date.split("-");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.valueOf(str[0]));
            cal.set(Calendar.MONTH, Integer.valueOf(str[1]) - 1); // 7月
            int maxDate  =  cal.getActualMaximum(Calendar.DATE);
            colnum = 1 + maxDate * 2;
            for(int i=0;i<maxDate;i++){
                cal.set(Calendar.DAY_OF_MONTH, i+1);
                String day = DateUtils.dateTime(cal.getTime());
                DutyView dutyDay1 = new DutyView();
                dutyDay1.setColspan(2);
                dutyDay1.setRowspan(1);
                dutyDay1.setTitle(day);
                list1.add(dutyDay1);
                dateList.add(day);

                DutyView dutyView1 = new DutyView();
                dutyView1.setTitle("白班");
                dutyView1.setColspan(1);
                dutyView1.setRowspan(1);
                list2.add(dutyView1);
                DutyView dutyView2 = new DutyView();
                dutyView2.setTitle("夜班");
                dutyView2.setColspan(1);
                dutyView2.setRowspan(1);
                list2.add(dutyView2);
            }

            for (DutyVersion version : dutyVersions) {
                Map map1 = new HashMap();
                ssmap = new HashMap<>();
                DutyScheduling dutyScheduling;
                list3 = new ArrayList<>();//list3为excel第三行表头
                String tdtml = "";
                if ("c002".equals((version.getTypeNo()))) {
                    DutyVersion cdv = new DutyVersion();
                    cdv.setTypeNo("c002");
                    cdv.setDutyDate(dutyVersion.getDutyDate());
                    cdvs = dutyVersionMapper.selectVersionByDutyDate(cdv);
                    for (int h=0;h<cdvs.size();h++){
                        trhtml += "<tr>";
                        if(h==0) {
                            tdtml = "<td rowspan='2' colspan='1'>" + version.getTypeName() + "</td>";
                        }
                        list8.add(cdvs.get(h).getTypeNo());
                        for (int i = 0; i < dateList.size(); i++) {
                            dutyScheduling = new DutyScheduling();
                            dutyScheduling.setDutyDate(dateList.get(i));
                            dutyScheduling.setTypeNo(cdvs.get(h).getTypeNo());
                            List<DutyScheduling> dutySchedulings = dutyViewMapper.selectViewListByDutyDate(dutyScheduling);
                            if (null != dutySchedulings && dutySchedulings.size() > 0) {
                                if (dutySchedulings.size() > 1) {
                                    for (int j = 0; j < dutySchedulings.size(); j++) {
                                        if ("0".equals(dutySchedulings.get(j).getTimeShift())) {
                                            DutyView dutyTime1 = new DutyView();
                                            dutyTime1.setTitle(dutySchedulings.get(j).getPname());
                                            dutyTime1.setColspan(1);
                                            dutyTime1.setRowspan(1);
                                            list3.add(dutyTime1);
                                            list7.add(dutyTime1);
                                            tdtml += "<td rowspan='1' colspan='1'>" + dutySchedulings.get(j).getPname() + "</td>";
                                            break;
                                        }
                                    }
                                    for (int j = 0; j < dutySchedulings.size(); j++) {
                                        if ("1".equals(dutySchedulings.get(j).getTimeShift())) {
                                            DutyView dutyTime1 = new DutyView();
                                            dutyTime1.setTitle(dutySchedulings.get(j).getPname());
                                            dutyTime1.setColspan(1);
                                            dutyTime1.setRowspan(1);
                                            list3.add(dutyTime1);
                                            list7.add(dutyTime1);
                                            tdtml += "<td rowspan='1' colspan='1'>" + dutySchedulings.get(j).getPname() + "</td>";
                                            break;
                                        }
                                    }
                                } else {
                                    if ("0".equals(dutySchedulings.get(0).getTimeShift())) {
                                        DutyView dutyTime1 = new DutyView();
                                        dutyTime1.setTitle(dutySchedulings.get(0).getPname());
                                        dutyTime1.setColspan(1);
                                        dutyTime1.setRowspan(1);
                                        list3.add(dutyTime1);
                                        list7.add(dutyTime1);
                                        DutyView dutyTime2 = new DutyView();
                                        dutyTime2.setTitle("");
                                        dutyTime2.setColspan(1);
                                        dutyTime2.setRowspan(1);
                                        list3.add(dutyTime2);
                                        list7.add(dutyTime2);
                                        tdtml += "<td rowspan='1' colspan='1'>" + dutySchedulings.get(0).getPname() + "</td><td rowspan='1' colspan='1'>&nbsp;</td>";
                                    } else {
                                        DutyView dutyTime1 = new DutyView();
                                        dutyTime1.setTitle("");
                                        dutyTime1.setColspan(1);
                                        dutyTime1.setRowspan(1);
                                        list3.add(dutyTime1);
                                        list7.add(dutyTime1);
                                        DutyView dutyTime2 = new DutyView();
                                        dutyTime2.setTitle(dutySchedulings.get(0).getPname());
                                        dutyTime2.setColspan(1);
                                        dutyTime2.setRowspan(1);
                                        list3.add(dutyTime2);
                                        list7.add(dutyTime2);
                                        tdtml += "<td rowspan='1' colspan='1'>&nbsp;</td><td rowspan='1' colspan='1'>" + dutySchedulings.get(0).getPname() + "</td>";
                                    }
                                }
                            } else {
                                DutyView view1 = new DutyView();
                                view1.setTitle("");
                                view1.setColspan(1);
                                view1.setRowspan(1);
                                list3.add(view1);
                                list7.add(view1);
                                DutyView view2 = new DutyView();
                                view2.setTitle("");
                                view2.setColspan(1);
                                view2.setRowspan(1);
                                list3.add(view2);
                                list7.add(view2);

                                tdtml += "<td rowspan='1' colspan='1'>&nbsp;</td><td rowspan='1' colspan='1'>&nbsp;</td>";

                            }
                        }
                        sldmap.put(cdvs.get(h).getTypeNo(),list7);
                        list7 = new ArrayList<>();
                        trhtml = trhtml+tdtml + "</tr>";
                        tdtml = "";
                    }
                } else {
                    trhtml += "<tr>";
                    tdtml = "<td rowspan='1' colspan='1'>" + version.getTypeName() + "</td>";
                    for (int i = 0; i < dateList.size(); i++) {
                        dutyScheduling = new DutyScheduling();
                        dutyScheduling.setDutyDate(dateList.get(i));
                        dutyScheduling.setTypeNo(version.getTypeNo());
                        List<DutyScheduling> dutySchedulings = dutyViewMapper.selectViewListByDutyDate(dutyScheduling);

                        if (null != dutySchedulings && dutySchedulings.size() > 0) {
                            if (dutySchedulings.size() > 1) {
                                for (int j = 0; j < dutySchedulings.size(); j++) {
                                    if ("0".equals(dutySchedulings.get(j).getTimeShift())) {
                                        DutyView dutyTime1 = new DutyView();
                                        dutyTime1.setTitle(dutySchedulings.get(j).getPname());
                                        dutyTime1.setColspan(1);
                                        dutyTime1.setRowspan(1);
                                        list3.add(dutyTime1);
                                        tdtml += "<td rowspan='1' colspan='1'>" + dutySchedulings.get(j).getPname() + "</td>";
                                        break;
                                    }
                                }
                                for (int j = 0; j < dutySchedulings.size(); j++) {
                                    if ("1".equals(dutySchedulings.get(j).getTimeShift())) {
                                        DutyView dutyTime1 = new DutyView();
                                        dutyTime1.setTitle(dutySchedulings.get(j).getPname());
                                        dutyTime1.setColspan(1);
                                        dutyTime1.setRowspan(1);
                                        list3.add(dutyTime1);
                                        tdtml += "<td rowspan='1' colspan='1'>" + dutySchedulings.get(j).getPname() + "</td>";
                                        break;
                                    }
                                }
                            } else {
                                if ("0".equals(dutySchedulings.get(0).getTimeShift())) {
                                    DutyView dutyTime1 = new DutyView();
                                    dutyTime1.setTitle(dutySchedulings.get(0).getPname());
                                    dutyTime1.setColspan(1);
                                    dutyTime1.setRowspan(1);
                                    list3.add(dutyTime1);
                                    DutyView dutyTime2 = new DutyView();
                                    dutyTime2.setTitle("");
                                    dutyTime2.setColspan(1);
                                    dutyTime2.setRowspan(1);
                                    list3.add(dutyTime2);
                                    tdtml += "<td rowspan='1' colspan='1'>" + dutySchedulings.get(0).getPname() + "</td><td rowspan='1' colspan='1'>&nbsp;</td>";
                                } else {
                                    DutyView dutyTime1 = new DutyView();
                                    dutyTime1.setTitle("");
                                    dutyTime1.setColspan(1);
                                    dutyTime1.setRowspan(1);
                                    list3.add(dutyTime1);
                                    DutyView dutyTime2 = new DutyView();
                                    dutyTime2.setTitle(dutySchedulings.get(0).getPname());
                                    dutyTime2.setColspan(1);
                                    dutyTime2.setRowspan(1);
                                    list3.add(dutyTime2);
                                    tdtml += "<td rowspan='1' colspan='1'>&nbsp;</td><td rowspan='1' colspan='1'>" + dutySchedulings.get(0).getPname() + "</td>";
                                }
                            }
                        } else {
                            DutyView view1 = new DutyView();
                            view1.setTitle("");
                            view1.setColspan(1);
                            view1.setRowspan(1);
                            list3.add(view1);
                            DutyView view2 = new DutyView();
                            view2.setTitle("");
                            view2.setColspan(1);
                            view2.setRowspan(1);
                            list3.add(view2);

                            tdtml += "<td rowspan='1' colspan='1'>&nbsp;</td><td rowspan='1' colspan='1'>&nbsp;</td>";

                        }
                    }
                    trhtml += tdtml + "</tr>";
                }
                map1.put(version.getTypeNo(),list3);
                ssmap.put(version.getTypeNo(),version.getTypeName());
                list4.add(map1);
                list5.add(version.getTypeNo());
                list6.add(ssmap);
            }
        }else{
            map.put("flag",1);
            map.put("msg","当前月份没有绑定值班信息！");
        }
        String title = "重保值班表("+date+ ")";//表头
        map.put("colnum",colnum);
        map.put("title",title);
        map.put("list1",list1);
        map.put("list2",list2);
        map.put("list3",list4);
        map.put("list5",list5);
        map.put("list6",list6);
        map.put("list7",list7);
        map.put("list8",list8);
        map.put("sldmap",sldmap);
        map.put("cdvs",cdvs);
        map.put("trhtml",trhtml);
        map.put("zbzbxx",zbzbxx);
        return map;
    }

    @Override
    public List<DutyVersion> selectCalendar(DutyVersion dutyVersion){
        String date = dutyVersion.getDutyDate();
        if(StringUtils.isEmpty(dutyVersion.getDutyDate())){
            date = DateUtils.parseDateToStr(DateUtils.YYYY_MM,DateUtils.getNowDate());
            dutyVersion.setDutyDate(date);
        }
        dutyVersion.setPid(ShiroUtils.getOgUser().getpId());
        List<DutyVersion> versions;
        if("mysql".equals(dbType)){
            versions = dutyViewMapper.selectCalendarMysql(dutyVersion);
        }else{
            versions = dutyViewMapper.selectCalendar(dutyVersion);
        }
        return versions;
    }

    @Override
    public List<DutyVersion> selectSchedulingById(String dutyDate){
        DutySubstitute dutySubstitute = new DutySubstitute();
        dutySubstitute.setDutyDate(dutyDate);
        String pid = ShiroUtils.getOgUser().getpId();
        dutySubstitute.setPid(pid);
        List<DutyVersion> dutyVersion = dutyViewMapper.selectSchedulingByNotId(dutySubstitute);
        return dutyVersion;
    }

    /**
     * 根据日期获取 星期 （2019-05-06 ——> 星期一）
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {

        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = DateUtils.parseDate(datetime);
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    @Override
    public int addCheck(DutySubstitute dutySubstitute) {
        int i = 0;
        dutySubstitute.setPid(ShiroUtils.getOgUser().getpId());
        List<DutyVersion> version = dutyViewMapper.selectSchedulingById(dutySubstitute);;
        if(StringUtils.isEmpty(version)){
            i = 1;
        }else{
            for(DutyVersion v : version) {
                dutySubstitute.setSchedulingId(v.getSchedulingId());
                DutySubstitute ds = dutySubstituteMapper.selectSubstituteBySchedulingId(dutySubstitute);
                if (null != ds) {
                    i = 2;
                }else{
                    i = 0;
                    break;
                }
            }
        }
        return i;
    }

    @Override
    public int addDateCheck(DutySubstitute dutySubstitute) {
        int i = 0;
        List<DutyVersion> versions = dutyViewMapper.exportSchedulingList(dutySubstitute);;
        if(null==versions||versions.size()==0){
            i = 1;
        }
        return i;
    }

    @Override
    public int insertDutySubstitute(DutySubstitute dutySubstitute) {
        dutySubstitute.setSubstituteId(UUID.getUUIDStr());
        dutySubstitute.setStatus("0");
        return dutySubstituteMapper.insertDutySubstitute(dutySubstitute);
    }

}
