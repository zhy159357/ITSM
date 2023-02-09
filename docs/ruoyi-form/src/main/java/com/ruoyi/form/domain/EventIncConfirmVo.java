package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class EventIncConfirmVo {

    private String eventNo;// 柜面事件编号
    private String imCode;// IM事件编号

    private String confirmFlag;// 确认标识  Y-通过；N-拒绝；通过事件流程走向关闭环节，拒绝事件流程退回到服务台
    private String envaluationDesc;// 拒绝理由(客服)｜评价内容（柜面）[意见及建议]

    /**柜面事件确认接口需要传输评价标签**/
    private String evaluateLabel;// 评价标签

    private String eventSolution;// 事件解决方案

    private String processTime;// 事件处理时长

    private String evaluationScore;// 评价分数
}
