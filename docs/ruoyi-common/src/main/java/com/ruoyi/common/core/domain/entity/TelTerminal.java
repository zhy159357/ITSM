package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * tel_terminal
 * 
 * @author ruoyi
 * @date 2020-12-17
 */
public class TelTerminal
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 话机IP */
    @Excel(name = "话机IP")
    private String telip;

    /** 服务IP */
    @Excel(name = "服务IP")
    private String serviceIp;

    /** 工号 */
    //@Excel(name = "工号")
    private String jobNumber;

    /** 创建人 */
    private String createId;

    /** 分机号 */
    @Excel(name = "分机号")
    private String extno;

    private String createTime;

    private OgPerson ogPerson;// 人员
    private OgUser ogUser;// 用户

    /*工号name*/
    @Excel(name = "工号")
    private String username;

    /*创建人name*/
    @Excel(name = "创建人")
    private String pName;

    //技能组id
    private String skillId;
    //是否添加默认技能组
    private String ifskillno;
    //技能组名称
    private String skillsGroupTelname;
    //技能组别名
    private String skillsGroupName;

    public String getSkillsGroupName() {
        return skillsGroupName;
    }

    public void setSkillsGroupName(String skillsGroupName) {
        this.skillsGroupName = skillsGroupName;
    }

    public String getSkillsGroupTelname() {
        return skillsGroupTelname;
    }

    public void setSkillsGroupTelname(String skillsGroupTelname) {
        this.skillsGroupTelname = skillsGroupTelname;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getIfskillno() {
        return ifskillno;
    }

    public void setIfskillno(String ifskillno) {
        this.ifskillno = ifskillno;
    }

    private Map<String,Object> params;

    private String showFlag="0";

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public OgPerson getOgPerson() {
        return ogPerson;
    }

    public void setOgPerson(OgPerson ogPerson) {
        this.ogPerson = ogPerson;
    }

    public OgUser getOgUser() {
        return ogUser;
    }

    public void setOgUser(OgUser ogUser) {
        this.ogUser = ogUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setTelip(String telip) 
    {
        this.telip = telip;
    }

    public String getTelip() 
    {
        return telip;
    }
    public void setServiceIp(String serviceIp) 
    {
        this.serviceIp = serviceIp;
    }

    public String getServiceIp() 
    {
        return serviceIp;
    }
    public void setJobNumber(String jobNumber) 
    {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() 
    {
        return jobNumber;
    }
    public void setCreateId(String createId) 
    {
        this.createId = createId;
    }

    public String getCreateId() 
    {
        return createId;
    }
    public void setExtno(String extno) 
    {
        this.extno = extno;
    }

    public String getExtno() 
    {
        return extno;
    }

}
