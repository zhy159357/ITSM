package com.ruoyi.common.core.domain.entity;

/**
 * cmdb接受参数
 */
import java.util.List;

    public class JsonRootBean {

        private int totalRecords;
        private List<DataList> dataList;
        private boolean empty;
        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }
        public int getTotalRecords() {
            return totalRecords;
        }

        public void setDataList(List<DataList> dataList) {
            this.dataList = dataList;
        }
        public List<DataList> getDataList() {
            return dataList;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
        public boolean getEmpty() {
            return empty;
        }

        @Override
        public String toString() {
            return "JsonRootBean{" +
                    "totalRecords=" + totalRecords +
                    ", dataList=" + dataList +
                    ", empty=" + empty +
                    '}';
        }
    }
