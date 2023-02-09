package com.ruoyi.es.domain;

import org.springframework.data.annotation.Id;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author dayong_sun
 * @version 1.0
 *
 * indexName = "userInfo"          // es 的索引名称
 * type = "doc"                    //默认的
 *
 * 当我们springboot 启动的时候 ，引入es的依赖之后，会去看一下document的实体，如果没有配置  useServerConfiguration  则会 同步到es里面去
 * useServerConfiguration = true   //使用线上的
 * createIndex = false             // 如果没有配置，回去es删除掉这个索引名称，配置了 项目启动的时候，不会去es里面创建新的index
 *
 */
public class EsUserBean  {
    @Id
    private String id;
    private String name;
    private String password;
    private Integer age;
    private String phone;
    private String remark;
    private String createTime;
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
