package com.ruoyi.common.core.domain.entity;


import java.util.Date;
import java.util.List;

/**
 * cmdb接受参数
 */
    public class DataList {

        private String id;
        private String tenantId;
        private String classCode;
        private String className;
        private String outerObjectId;
        private int objectVersion;
        private String state;
        private Date createTime;
        private Date updateTime;
        private List<String> sources;
        private String tags;
        private List<String> resOwners;
        private String bizSerial;
        private String name;
        private String systemUrl;
        private String simpleSpelling;
        private String showStatus;
        private String systemType;
        private OwnerSystem ownerSystem;
        private String systemLevel;
        private String deployStatus;
        private String system_stage;
        private OwnerDir ownerDir;
        private String platform_number;

        public String getPlatform_number() {
            return platform_number;
        }

        public void setPlatform_number(String platform_number) {
            this.platform_number = platform_number;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }
        public String getTenantId() {
            return tenantId;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }
        public String getClassCode() {
            return classCode;
        }

        public void setClassName(String className) {
            this.className = className;
        }
        public String getClassName() {
            return className;
        }

        public void setOuterObjectId(String outerObjectId) {
            this.outerObjectId = outerObjectId;
        }
        public String getOuterObjectId() {
            return outerObjectId;
        }

        public void setObjectVersion(int objectVersion) {
            this.objectVersion = objectVersion;
        }
        public int getObjectVersion() {
            return objectVersion;
        }

        public void setState(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
        public Date getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }
        public Date getUpdateTime() {
            return updateTime;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }
        public List<String> getSources() {
            return sources;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
        public String getTags() {
            return tags;
        }

        public void setResOwners(List<String> resOwners) {
            this.resOwners = resOwners;
        }
        public List<String> getResOwners() {
            return resOwners;
        }

        public void setBizSerial(String bizSerial) {
            this.bizSerial = bizSerial;
        }
        public String getBizSerial() {
            return bizSerial;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setSystemUrl(String systemUrl) {
            this.systemUrl = systemUrl;
        }
        public String getSystemUrl() {
            return systemUrl;
        }

        public void setSimpleSpelling(String simpleSpelling) {
            this.simpleSpelling = simpleSpelling;
        }
        public String getSimpleSpelling() {
            return simpleSpelling;
        }

        public void setShowStatus(String showStatus) {
            this.showStatus = showStatus;
        }
        public String getShowStatus() {
            return showStatus;
        }

        public void setSystemType(String systemType) {
            this.systemType = systemType;
        }
        public String getSystemType() {
            return systemType;
        }

        public void setOwnerSystem(OwnerSystem ownerSystem) {
            this.ownerSystem = ownerSystem;
        }
        public OwnerSystem getOwnerSystem() {
            return ownerSystem;
        }

        public void setSystemLevel(String systemLevel) {
            this.systemLevel = systemLevel;
        }
        public String getSystemLevel() {
            return systemLevel;
        }

        public void setDeployStatus(String deployStatus) {
            this.deployStatus = deployStatus;
        }
        public String getDeployStatus() {
            return deployStatus;
        }

        public void setSystem_stage(String system_stage) {
            this.system_stage = system_stage;
        }
        public String getSystem_stage() {
            return system_stage;
        }

        public void setOwnerDir(OwnerDir ownerDir) {
            this.ownerDir = ownerDir;
        }
        public OwnerDir getOwnerDir() {
            return ownerDir;
        }


        @Override
        public String toString() {
            return "DataList{" +
                    "id='" + id + '\'' +
                    ", tenantId='" + tenantId + '\'' +
                    ", classCode='" + classCode + '\'' +
                    ", className='" + className + '\'' +
                    ", outerObjectId='" + outerObjectId + '\'' +
                    ", objectVersion=" + objectVersion +
                    ", state='" + state + '\'' +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    ", sources=" + sources +
                    ", tags='" + tags + '\'' +
                    ", resOwners=" + resOwners +
                    ", bizSerial='" + bizSerial + '\'' +
                    ", name='" + name + '\'' +
                    ", systemUrl='" + systemUrl + '\'' +
                    ", simpleSpelling='" + simpleSpelling + '\'' +
                    ", showStatus='" + showStatus + '\'' +
                    ", systemType='" + systemType + '\'' +
                    ", ownerSystem=" + ownerSystem +
                    ", systemLevel='" + systemLevel + '\'' +
                    ", deployStatus='" + deployStatus + '\'' +
                    ", system_stage='" + system_stage + '\'' +
                    ", ownerDir=" + ownerDir +
                    ", platform_number='" + platform_number + '\'' +
                    '}';
        }
    }
