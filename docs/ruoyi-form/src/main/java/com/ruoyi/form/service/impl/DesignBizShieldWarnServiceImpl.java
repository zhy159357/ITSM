package com.ruoyi.form.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringJoiner;

import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.DesignBizShieldWarnMapper;
import com.ruoyi.form.domain.DesignBizShieldWarn;
import com.ruoyi.form.service.IDesignBizShieldWarnService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 屏蔽告警Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-22
 */
@Service
public class DesignBizShieldWarnServiceImpl implements IDesignBizShieldWarnService {
    @Autowired
    private DesignBizShieldWarnMapper designBizShieldWarnMapper;

    /**
     * 查询屏蔽告警
     *
     * @param id 屏蔽告警ID
     * @return 屏蔽告警
     */
    @Override
    public DesignBizShieldWarn selectDesignBizShieldWarnById(Long id) {
        return designBizShieldWarnMapper.selectDesignBizShieldWarnById(id);
    }

    /**
     * 查询屏蔽告警列表
     *
     * @param designBizShieldWarn 屏蔽告警
     * @return 屏蔽告警
     */
    @Override
    public List<DesignBizShieldWarn> selectDesignBizShieldWarnList(DesignBizShieldWarn designBizShieldWarn, Integer pageNum, Integer pageSize) {
        Integer limit = (pageNum - 1) * pageSize;
        Integer offset = pageSize;
        designBizShieldWarn.setLimit(limit);
        designBizShieldWarn.setOffset(offset);
        return designBizShieldWarnMapper.selectDesignBizShieldWarnList(designBizShieldWarn);
    }

    @Override
    public Integer count(DesignBizShieldWarn designBizShieldWarn) {
        return designBizShieldWarnMapper.selectCount(designBizShieldWarn);
    }

    @Override
    public List<DesignBizShieldWarn> selectDesignBizShieldWarnByChangeTaskNo(String changeTaskNo) {
        return designBizShieldWarnMapper.selectDesignBizShieldWarnByChangeTaskNo(changeTaskNo);
    }

    @Override
    public AjaxResult updateDesignBizShieldWarnByChangeTaskNo(DesignBizShieldWarn designBizShieldWarn) {
        designBizShieldWarn = transfer(designBizShieldWarn);
        if(designBizShieldWarn == null){
            return AjaxResult.warn("请填入至少一条规则");
        }
        designBizShieldWarnMapper.updateDesignBizShieldWarnByChangeTaskNo(designBizShieldWarn);
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult parseShieldWarnExcel(String changeTaskNo, MultipartFile file) {
        Long size = file.getSize();
        if (size == 0) {
            return AjaxResult.warn("不允许上传空文件");
        }
        String fileName = file.getOriginalFilename();
        String suffix = "xls";
        String suffix2 = "xlsx";
        if (!FilenameUtils.isExtension(fileName, suffix) && !FilenameUtils.isExtension(fileName, suffix2)) {
            return AjaxResult.warn("只允许上传指定excel文件");
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 1;
            int colsNum = 6;
            int endRow = sheet.getLastRowNum() + 1;
            if (endRow > 1) {
                for (int i = startRow; i < endRow; ++i) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        DesignBizShieldWarn shieldWarn = new DesignBizShieldWarn();
                        shieldWarn.setChangeTaskNo(changeTaskNo);
                        int count = 0;
                        for (int j = 0; j < colsNum; ++j) {
                            Cell dataObj = row.getCell(j);
                            String data = "";
                            if (dataObj != null) {
                                DataFormatter dataFormatter = new DataFormatter();
                                data = dataFormatter.formatCellValue(dataObj);
                            }
                            if ("".equals(data)) {
                                ++count;
                            }
                            if (j == 0) {
                                shieldWarn.setPasoCode(data);
                            } else if (j == 1) {
                                shieldWarn.setIpAddress(data);
                            } else if (j == 2) {
                                shieldWarn.setIndexType(data);
                            } else if (j == 3) {
                                shieldWarn.setSystemCode(data);
                            } else if (j == 4) {
                                shieldWarn.setObjectName(data);
                            } else if (j == 5) {
                                shieldWarn.setInsName(data);
                            }
                        }
                        if (count < colsNum) {
                            designBizShieldWarnMapper.insertDesignBizShieldWarn(shieldWarn);
                        }
                        //添加/更新一行数据到数据库里
                       /* DesignBizShieldWarn changeTaskNoWarn =designBizShieldWarnMapper.selectDesignBizShieldWarnByChangeTaskNo(changeTaskNo);
                        if(changeTaskNoWarn==null){*/
                       /* }else {
                            designBizShieldWarnMapper.updateDesignBizShieldWarnByChangeTaskNo(shieldWarn);
                        }*/
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.warn("异常：" + e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return AjaxResult.success();
    }

    /**
     * 新增屏蔽告警
     *
     * @param designBizShieldWarn 屏蔽告警
     * @return 结果
     */
    @Override
    public AjaxResult insertDesignBizShieldWarn(DesignBizShieldWarn designBizShieldWarn) {
        designBizShieldWarn = transfer(designBizShieldWarn);
        if(designBizShieldWarn == null){
            return AjaxResult.warn("请填入至少一条规则");
        }
        designBizShieldWarnMapper.insertDesignBizShieldWarn(designBizShieldWarn);
        return AjaxResult.success();
    }

    public String mergeStr(String[] array) {
        StringJoiner stringJoiner = new StringJoiner(",");
        if (array != null) {
            for (String a : array) {
                stringJoiner.add(a);
            }
        }
        return stringJoiner.toString();
    }

    public DesignBizShieldWarn transfer(DesignBizShieldWarn designBizShieldWarn) {
        String systemCode = mergeStr(designBizShieldWarn.getSystemCodeArray());
        String pasoCode = mergeStr(designBizShieldWarn.getPassCodeArray());
        String ipAddress = designBizShieldWarn.getIpAddress();
        if (ipAddress == null || "".equals(ipAddress.trim())) {
            ipAddress = mergeStr(designBizShieldWarn.getIpArray());
        }
        String indexType = mergeStr(designBizShieldWarn.getIndexTypeArray());
        String net = mergeStr(designBizShieldWarn.getNetArray());
        designBizShieldWarn.setNet(net);
        designBizShieldWarn.setIndexType(indexType);
        designBizShieldWarn.setIpAddress(ipAddress);
        designBizShieldWarn.setSystemCode(systemCode);
        designBizShieldWarn.setPasoCode(pasoCode);
        String objectName = designBizShieldWarn.getObjectName();
        String insName = designBizShieldWarn.getInsName();
        if ((systemCode == null || "".equals(systemCode.trim()))
                        && (pasoCode == null || "".equals(pasoCode.trim()))
                        && (ipAddress == null || "".equals(ipAddress.trim()))
                        && (indexType == null || "".equals(indexType.trim()))
                        && (net == null || "".equals(net.trim()))
                        && (objectName == null || "".equals(objectName.trim()))
                        && (insName == null || "".equals(insName.trim()))
        ) {
            return null;
        }
        return designBizShieldWarn;
    }

    /**
     * 修改屏蔽告警
     *
     * @param designBizShieldWarn 屏蔽告警
     * @return 结果
     */
    @Override
    public int updateDesignBizShieldWarn(DesignBizShieldWarn designBizShieldWarn) {
        return designBizShieldWarnMapper.updateDesignBizShieldWarn(designBizShieldWarn);
    }

    /**
     * 删除屏蔽告警对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDesignBizShieldWarnByIds(String ids) {
        return designBizShieldWarnMapper.deleteDesignBizShieldWarnByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除屏蔽告警信息
     *
     * @param id 屏蔽告警ID
     * @return 结果
     */
    @Override
    public int deleteDesignBizShieldWarnById(Long id) {
        return designBizShieldWarnMapper.deleteDesignBizShieldWarnById(id);
    }
}
