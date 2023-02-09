package com.ruoyi.common.core.domain.entity;

/**
 * cmdb接受参数
 */
public class OwnerSystem {
    private String name;
    private String id;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "OwnerSystem{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
