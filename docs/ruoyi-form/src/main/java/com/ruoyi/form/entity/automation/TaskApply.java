package com.ruoyi.form.entity.automation;

import lombok.Data;

import java.io.Serializable;

/**
 * {
 * "dailytype": 2,
 * "ibusName": "",
 * "icreatetime": "2018-12-27",
 * "icreateuser": "Default User",
 * "iid": 10426,
 * "iprojectname": "1_sy_ut",
 * "itypeid": 8,
 * "iupdatetime": "2018-12-14",
 * "iupdateuser": "Default User",
 * "iversion": "1"
 * }
 */
@Data
public class TaskApply implements Serializable {

    private Integer dailytype;
    private String ibusName;
    private String icreatetime;
    private String icreateuser;
    private Integer iid;
    private String iprojectname;
    private Integer itypeid;
    private String iupdatetime;
    private String iupdateuser;
    private String iversion;

}
