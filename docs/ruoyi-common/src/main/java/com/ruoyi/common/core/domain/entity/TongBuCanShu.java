package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.Domain;

import java.util.List;

/**
 * cmdb传参实体类
 */

public class TongBuCanShu implements Domain {

        private int pageNum;
        private int pageSize;
        private boolean needCount;
        private List<String> orderFields;
        private List<String> requiredFields;
        private List<Conditions> conditions;
        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }
        public int getPageNum() {
            return pageNum;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
        public int getPageSize() {
            return pageSize;
        }

        public void setNeedCount(boolean needCount) {
            this.needCount = needCount;
        }
        public boolean getNeedCount() {
            return needCount;
        }

        public void setOrderFields(List<String> orderFields) {
            this.orderFields = orderFields;
        }
        public List<String> getOrderFields() {
            return orderFields;
        }

        public void setRequiredFields(List<String> requiredFields) {
            this.requiredFields = requiredFields;
        }
        public List<String> getRequiredFields() {
            return requiredFields;
        }

        public void setConditions(List<Conditions> conditions) {
            this.conditions = conditions;
        }
        public List<Conditions> getConditions() {
            return conditions;
        }


    @Override
    public String toString() {
        return "TongBuCanShu{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", needCount=" + needCount +
                ", orderFields=" + orderFields +
                ", requiredFields=" + requiredFields +
                ", conditions=" + conditions +
                '}';
    }
}
