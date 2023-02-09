package com.ruoyi.form.entity.automation;

import lombok.Data;

import java.io.Serializable;

/**
 * {
 *             "agentIp": "192.168.0.183",
 *             "agentPort": 15101,
 *             "agentState": 0,
 *             "appAdmin": "",
 *             "centerName": "主中心",
 *             "ctype": "",
 *             "dbType": "db2,mysql",
 *             "dbVersion": "10.5,7.2",
 *             "envType": "1",
 *             "hostName": "vm6-pc183",
 *             "iagentid": "1",
 *             "iid": 1,
 *             "isHa": "true",
 *             "middlewareType": "weblogci,tomcat",
 *             "middlewareVersion": "5.5,7.5",
 *             "osType": "Linux,Windows",
 *             "osTypeAgentinfo": "Linux",
 *             "osVersion": "6.5,Microsoft Windows Server 2016 Standard10.0.14393",
 *             "startUser": "root",
 *             "sysAdmin": "Default User",
 *             "systemInfo": "aaaaa"
 *         }
 */
@Data
public class EquipmentInfo implements Serializable {

    private String agentIp;

    private String agentPort;

    private String iagentid;

    private Integer iid;

}
