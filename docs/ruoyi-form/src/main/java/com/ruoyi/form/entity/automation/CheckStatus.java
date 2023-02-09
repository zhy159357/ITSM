package com.ruoyi.form.entity.automation;

import lombok.Data;

@Data
public class CheckStatus {
    /**
     * "auditorUser": "Server Admin",
     *             "execStrategy": 2,
     *             "performUser": "Server Admin",
     *             "startTime": "2019-02-19 14:54",
     *             "endTime": "",
     *           	"runTime": "",
     *             "startUser": "Server Admin",
     *             "status": "运行",
     *             "taskId": "92",
     *             "taskName": "testut201902191404",
     * 				"butterflyVersion": "2342342342334",
     * 				"ibusname": ""
     */
    /**
     * 状态
     */
    private String status;
    /**
     * 工单号
     */
    private String butterflyVersion;
    private String auditorUser;
    private String execStrategy;
    private String performUser;
    private String startTime;
    private String endTime;
    private String runTime;
    private String startUser;
    private String taskId;
    private String taskName;
    private String ibusname;
}
