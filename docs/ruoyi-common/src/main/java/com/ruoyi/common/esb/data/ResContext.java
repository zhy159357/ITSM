package com.ruoyi.common.esb.data;

import com.ruoyi.common.core.Domain;
import com.ruoyi.common.core.Message;

import java.io.Serializable;
import java.util.List;

/*    */
/*    */
/*    */
/*    */ public class ResContext<T extends Domain>
        /*    */ implements Serializable
        /*    */ {
    /*    */   private static final long serialVersionUID = -1607997323023348578L;
    /*    */   private T record;
    /*    */   private Message message;
    /*    */   private int totalCount;
    /*    */   private List<T> records;
    /*    */   private String instanceId;
    /*    */   private String trancode;

    /*    */
    /*    */
    public T getRecord()
    /*    */ {
        /* 36 */
        return this.record;
        /*    */
    }

    /*    */
    /*    */
    public void setRecord(T record) {
        /* 40 */
        this.record = record;
        /*    */
    }

    /*    */
    /*    */
    public Message getMessage() {
        /* 44 */
        return this.message;
        /*    */
    }

    /*    */
    /*    */
    public void setMessage(Message message) {
        /* 48 */
        this.message = message;
        /*    */
    }

    /*    */
    /*    */
    public int getTotalCount() {
        /* 52 */
        return this.totalCount;
        /*    */
    }

    /*    */
    /*    */
    public void setTotalCount(int totalCount) {
        /* 56 */
        this.totalCount = totalCount;
        /*    */
    }

    /*    */
    /*    */
    public List<? extends T> getRecords() {
        /* 60 */
        return this.records;
        /*    */
    }

    /*    */
    /*    */
    public void setRecords(List<T> records) {
        /* 64 */
        this.records = records;
        /*    */
    }

    /*    */
    /*    */
    public String getInstanceId() {
        /* 68 */
        return this.instanceId;
        /*    */
    }

    /*    */
    /*    */
    public void setInstanceId(String instanceId) {
        /* 72 */
        this.instanceId = instanceId;
        /*    */
    }

    /*    */
    /*    */
    public String toString() {
        /* 76 */
        StringBuffer buf = new StringBuffer();
        /* 77 */
        if (this.record != null) {
            /* 78 */
            buf.append("record:" + this.record.toString() + ";");
            /*    */
        }
        /* 80 */
        if (this.records != null) {
            /* 81 */
            buf.append("records:" + this.records.toString() + ";");
            /*    */
        }
        /* 83 */
        if (this.message != null) {
            /* 84 */
            buf.append("message:" + this.message.toString() + ";");
            /*    */
        }
        /* 86 */
        return buf.toString();
        /*    */
    }

    /*    */
    /*    */
    public void setTrancode(String trancode) {
        /* 90 */
        this.trancode = trancode;
        /*    */
    }

    /*    */
    /*    */
    public String getTrancode() {
        /* 94 */
        return this.trancode;
        /*    */
    }
    /*    */
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.data.ResContext
 * JD-Core Version:    0.6.0
 */