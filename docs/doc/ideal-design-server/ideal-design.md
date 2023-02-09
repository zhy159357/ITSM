# 低代码平台使用文档

## 项目依赖低代码平台的内容  把低代码平台的jar安装到本地

mvn install:install-file -Dfile=ideal-design-server-1.0.2.jar -DgroupId=com.ideal -DartifactId=ideal-design-server -Dversion=1.0.2 -Dpackaging=jar
### 在ruoyi-admin的pom文件中添加依赖
```json lines
<dependency>
    <groupId>com.ideal</groupId>
    <artifactId>ideal-design-server</artifactId>
    <version>1.0.1</version>
</dependency>
```
### 在application.yml文件中添加参数

```yml
design:
    standalone:
        enable: false # 关闭独立部署
    datasource:
        enable: false # 关闭设计器数据源
```
###运行即可   Done~

##下面为低代码平台接口  可以不用管

## 接口文档说明
### 响应格式
```json lines
// code可能为200/403/404/500等任意HTTP状态码，一般情况下是200，实际请求成功与否以data为准
// data可能为number,string,object,array,null,float等任意类型
// 以下所有接口主要描述data部分
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```
### 参数格式
> 无特殊说明默认为application/json格式请求
### 请求类型
> 请注意请求类型是否正确，每个请求都可能用到GET POST HEAD PUT DELETE PATCH这些HTTP方法

## 项目设计器
> 基于 `项目-页面-接口` 模式设计的项目设计器，包含项目、页面、配置、字典、模板、函数、数据源、路由和版本等功能
### 项目
* 新增项目
  * URL: /design/project
  * 方法: POST
  * 参数:
  ``` json
    {
      "name": "项目名称(必填)",
      "identity": "项目标识(必填且唯一，只能为字母开头的字母和数字组合，如：Aaxxxx1)",
      "type": "项目类型(数字)",
      "description": "描述",
    }
    ```
  * 响应:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": 1
  }
   ```
* 修改项目
  * URL: /design/project
  * 方法: PUT
  * 参数:
  ``` json
    {
      "id": "项目ID(必填)",
      "name": "项目名称(必填)",
      "identity": "项目标识(必填且唯一，只能为字母开头的字母和数字组合，如：Aaxxxx1)",
      "type": "项目类型(数字)",
      "description": "描述"
    }
    ```
  * 响应:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": 1
  }
  ```
* 项目列表
  * URL: /design/project/list
  * 方法: POST
  * 参数:
  ```json
  {
    "name": "项目名称",
    "identity": "code1",
    "type": 1,
    "description": "描述"
  }
  ```
  * 响应:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
        "total": 1,
        "list": [
            {
                "status": 1,
                "createdTime": "2022-06-24 00:50:23",
                "updatedTime": null,
                "createdBy": null,
                "updatedBy": null,
                "pageSize": 10,
                "pageIndex": 1,
                "id": 19,
                "name": "项目名称",
                "identity": "code1",
                "type": 1,
                "description": "描述",
                "templateId": null,
                "apiHost": "接口地址",
                "rootPath": "根路由",
                "packageName": "包名"
            }
        ],
        "pageNum": 1,
        "pageSize": 10,
        "size": 1,
        "startRow": 1,
        "endRow": 1,
        "pages": 1,
        "prePage": 0,
        "nextPage": 0,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ],
        "navigateFirstPage": 1,
        "navigateLastPage": 1,
        "firstPage": 1,
        "lastPage": 1
    }
  }
  ```
* 获取单个项目
  * URL: /design/project/{id}
  * 方法: GET
  * 参数格式: 路径参数
  * 参数: {id}项目ID
  * 响应:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
        "status": 1,
        "createdTime": "2022-06-24 00:50:23",
        "updatedTime": null,
        "createdBy": null,
        "updatedBy": null,
        "pageSize": 10,
        "pageIndex": 1,
        "id": 19,
        "name": "项目名称",
        "identity": "code1",
        "type": 1,
        "description": "描述",
        "templateId": null,
        "apiHost": "接口地址",
        "rootPath": "根路由",
        "packageName": "包名"
    }
  }
  ```
* 删除项目
  * URL: /design/project/{id}
  * 方法: DELETE
  * 参数格式: 路径参数
  * 参数: {id}项目ID
  * 响应:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": 1
  }
  ```
### 配置
* 新增配置
* URL: /design/config
  * 方法: POST
  * 参数:
  ``` json
    {
      "projectId": "项目ID(必填且唯一)",
      "apiHost": "项目标识",
      "type": "项目类型(数字)",
      "description": "描述",
      "apiHost": "接口地址",
      "rootPath": "根路由",
      "packageName": "包名"
    }
    ```
  * 响应:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": 1
  }
* 修改配置
* 删除配置
* 获取项目配置信息
### 页面

### 函数

### 数据源

### 路由

### 模板

### 配置

### 字典

### 版本

## 表单设计器
> 基于 `表单-表-字段` 模式设计的表单设计器，提供了表单构建和CRUD自动生成等功能

### 表单
* 新增表单
  * URL: /design/form
  * 方法：POST
  * 参数格式：application/json
  * 参数：
    ``` json
    {
        "name": "表单名称",
        "code": "代码（这个参数暂定，后面研究一下是不是可以作为表名）",
        "type": "表单类型(数字)",
        "charset": "字符集(不知道有没有用，他那个设计页面上有个编码，不知道是不是字符集的意思，传'utf-8'就行",
        "html": "页面html",
        "json": "页面json",
        "data": {
            "id": "组件ID(必填)",
            "name": "字段名(必填)",
            "comment": "字段描述(必填)",
            "type": "字段类型(默认varchar)，可选值：bigint, int, tinyint, smallint, mediumint(前面这些都传int就行，可以细化最好), varchar, char(前面这些都传varchar就行), float, double, decimal(前面这些都是带精度的数值类型，都要传decimals参数), date, datetime, timestamp, year, time(前面这些都传datetime就行)",
            "nullable": "是否可以为空(默认为1), 0->不可为空, 1->可为空",
            "length": "字段长度(对不同type都有默认值), 对于整数、浮点数和字符串，都应该有长度",
            "decimals": "精度(默认都会处理), 对于float, double, decimal三种类型，都应该有精度，默认为0, 例如期望数值是2.42, 那么精度就是2"
        }
    }
    ```
* 修改表单
  * URL: /design/form
  * 方法：PUT
  * 参数格式：application/json
  * 参数：
    ``` json
    {
      "id": "表单ID",
      "name": "表单名称",
      "code": "代码（这个参数暂定，后面研究一下是不是可以作为表名）",
      "type": "表单类型(数字)",
      "charset": "字符集(不知道有没有用，他那个设计页面上有个编码，不知道是不是字符集的意思，传'utf-8'就行",
      "html": "页面html",
      "json": "页面json",
      "data": {
          "id": "组件ID(必填)",
          "name": "字段名(必填)",
          "comment": "字段描述(必填)",
          "type": "字段类型(默认varchar)，可选值：bigint, int, tinyint, smallint, mediumint(前面这些都传int就行，可以细化最好), varchar, char(前面这些都传varchar就行), float, double, decimal(前面这些都是带精度的数值类型，都要传decimals参数), date, datetime, timestamp, year, time(前面这些都传datetime就行)",
          "nullable": "是否可以为空(默认为1), 0->不可为空, 1->可为空",
          "length": "字段长度(对不同type都有默认值), 对于整数、浮点数和字符串，都应该有长度",
          "decimals": "精度(默认都会处理), 对于float, double, decimal三种类型，都应该有精度，默认为0, 例如期望数值是2.42, 那么精度就是2"
      }
    }
    ```
* 查询表单：(现在表单会带一个版本信息，默认选择最新版本，会以对象形式出现)
  * URL：/design/form/list
  * 方法：POST
  * 参数格式：application/json
  * 参数：
    ``` json
    {
        "pageIndex": 1,
        "pageSize": 10,
        "id": "ID",
        "name": "表单名称",
        "code": "代码（这个参数暂定，后面研究一下是不是可以作为表名）",
        "type": "表单类型(数字)",
        "charset": "字符集(不知道有没有用，他那个设计页面上有个编码，不知道是不是字符集的意思，传'utf-8'就行"
    }
    ```
* 查询表单带版本信息：(现在表单会带一个版本信息，会以数组形式出现)
  * URL：/design/form/list/versions
  * 方法：POST
  * 参数格式：application/json
  * 参数：
    ``` json
    {
        "pageIndex": 1,
        "pageSize": 10,
        "id": "ID",
        "name": "表单名称",
        "code": "代码（这个参数暂定，后面研究一下是不是可以作为表名）",
        "type": "表单类型(数字)",
        "charset": "字符集(不知道有没有用，他那个设计页面上有个编码，不知道是不是字符集的意思，传'utf-8'就行"
    }
    ```
* 获取指定表单：(现在表单会带一个版本信息，默认选择最新版本，会以对象形式出现)
  * URL：/design/form/{id}
  * 方法：GET
  * 参数格式：路径参数

* 获取指定表单带版本信息：(现在表单会带一个版本信息，会以数组形式出现)
  * URL：/design/form/{id}/versions
  * 方法：GET
  * 参数格式：路径参数
* 删除表单：(会删除表单和表单版本，是否删除表和表版本、字段和字段版本有待商榷)
  * URL: /design/form/{id}
  * 方法：DELETE
  * 参数格式: 路径参数
* 发布表单：(发布表单可以指定两个参数，第一个是表单ID id，第二个是版本ID versionId, 版本ID可以不传,此时使用的是最新版本，作用就是使用最新版本替换已经生成库表的版本，如果是指定versionId,作用就是使用某个版本替换已经生成库表的版本)
  * URL：/design/form/publish?id={id}&versionId={versionId}
  * 方法：GET
  * 参数格式：查询字符串
### 表单版本

### 表
* 获取表列表: (现在表会带一个版本信息，会以对象形式出现)
  * /design/table/list
  * 方法：POST
  * 参数格式：请求体
  * 参数：
  ```json 
  {
      "name": "表名称",
      "pageIndex": "页码",
      "pageSize": "页大小"
  }
  ```
* 获取表列表带版本信息: (现在表会带一个版本信息，会以数组形式出现)
  * URL：/design/table/list/versions
  * 方法：POST
  * 参数格式：请求体
  * 参数：
  ``` json
  {
      "name": "表名称",
      "formId": "所属表单ID",
      "pageIndex": "页码",
      "pageSize": "页大小"
  }
  ```

### 表版本

### 表配置

### 字段
* 获取字段列表: (现在字段会带一个版本信息，会以数组形式出现)
  * URL：http://zq9999.vaiwan.com/design/table/list
  * 方法：POST
  * 参数格式：请求体
  * 参数：
  ```json 
  {
      "name": "字段名称",
      "tableId": "所属表ID",
      "pageIndex": "页码",
      "pageSize": "页大小"
  }
  ```

### 字段版本
