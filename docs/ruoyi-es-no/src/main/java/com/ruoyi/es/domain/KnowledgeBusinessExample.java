package com.ruoyi.es.domain;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;

/**
 * 参数列别对象 KnowledgeAlarmExample
 * @author dayong_sun
 * @date 2021-06-21
 */
public class KnowledgeBusinessExample implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 所属应用系统 */
    @Excel(name = "所属应用系统")
    private String systemName;

    /** 一级分类 */
    @Excel(name = "一级分类")
    private String content;

    /** 二级分类 */
    @Excel(name = "二级分类")
    private String sectitle;

    /** 三级分类 */
    @Excel(name = "三级分类")
    private String threetitle;

    /** 关键字 */
    @Excel(name = "关键字")
    private String name;

    /** 事件描述 */
    @Excel(name = "事件描述")
    private String describe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSectitle() {
        return sectitle;
    }

    public void setSectitle(String sectitle) {
        this.sectitle = sectitle;
    }

    public String getThreetitle() {
        return threetitle;
    }

    public void setThreetitle(String threetitle) {
        this.threetitle = threetitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
