package com.ruoyi.es.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author dayong_sun
 * @version 1.0
 * indexName = "userInfo"          // es 的索引名称
 * type = "doc"                    //默认的
 * 当我们springboot 启动的时候 ，引入es的依赖之后，会去看一下document的实体，如果没有配置  useServerConfiguration  则会 同步到es里面去
 * useServerConfiguration = true   //使用线上的
 * createIndex = false             // 如果没有配置，回去es删除掉这个索引名称，配置了 项目启动的时候，不会去es里面创建新的index
 *
 */
@Document(indexName = "elasticsearch" ,type = "doc",
        useServerConfiguration = true,createIndex = false)
public class EsSearchBean {
    @Id
    private String id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String remark;
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private String create_time;
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private String update_time;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content_id;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }
}
