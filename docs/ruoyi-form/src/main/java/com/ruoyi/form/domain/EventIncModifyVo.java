package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class EventIncModifyVo {

    private String eventNo;// 柜面事件编号
    private String operationType;// 操作类型  追加(add)或者撤单(cancel)

    /**柜面撤单时需要传输的字段**/
    private String evenTenvaluation;// 满意度
    private String envaluationDesc;// 评价内容

    /**客服撤单或追加信息时需要传输的字段**/
    private String addInfoDesc;// 处理意见
    private String reminderLog;// 催单日志
    private String processLog;// 评价内容
    private String custInfo;// 客户信息

    private String files;// 图片信息
}
