package com.ruoyi.es.domain;

import java.io.Serializable;

/**
 * @author dayong_sun
 * @version 1.0
 */
public class KnowledgeRanking implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;
    /** 创建人 */
    private String createId;
    /** 用户名称 */
    private String pname;
    /** 文档数量 */
    private int docNum;
    /** 好评文章 */
    private int goodNum;
    /** 差评文章 */
    private int badNum;
    /** 文章好评率 */
    private String goodRate;
    /** 被访问总量 */
    private int visitedNum ;
    /** 累计好评 */
    private int cumulativecGood;
    /** 累计差评 */
    private int cumulativeBad;
    /** 状态 */
    private String status;
    /** 文章总数 */
    private int totalNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getDocNum() {
        return docNum;
    }

    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getBadNum() {
        return badNum;
    }

    public void setBadNum(int badNum) {
        this.badNum = badNum;
    }

    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate;
    }

    public int getVisitedNum() {
        return visitedNum;
    }

    public void setVisitedNum(int visitedNum) {
        this.visitedNum = visitedNum;
    }

    public int getCumulativecGood() {
        return cumulativecGood;
    }

    public void setCumulativecGood(int cumulativecGood) {
        this.cumulativecGood = cumulativecGood;
    }

    public int getCumulativeBad() {
        return cumulativeBad;
    }

    public void setCumulativeBad(int cumulativeBad) {
        this.cumulativeBad = cumulativeBad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}