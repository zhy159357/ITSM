package com.ruoyi.form.entity.automation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {
 * 			"iparamName":"ftp_localfile",
 * 			"iparamType":"String",
 * 			"iparamValue":"d:\\",
 * 			"iparamDes":""
 *                }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonParamArr {

    private String iparamName;

    private String iparamType;

    private String iparamValue;

    private String iparamDes;
}
