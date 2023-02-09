//package com.ruoyi.form.domain;
//
//import com.ruoyi.common.utils.StringUtils;
//import lombok.Data;
//
//import java.io.Serializable;
//
//@Data
//public class PortalVo implements Serializable {
//
//    // 机构id
//    private String deptId;
//    // 用户名
//    private String name;
//    // 最高学历
//    private String highLevel;
//    // 年龄
//    private String age;
//    // 部门名称
//    private String deptName;
//    // 上级部门编码
//    private String adminCode;
//
//    private String hrOrganId;
//    private String hrOrganName;
//    // 职称
//    private String highTitle;
//    // 部门排序
//    private String depOrde;
//    // 部门编码
//    private String depCode;
//    // 岗位
//    private String jobTitle;
//    // 性别
//    private String gender;
//    // 工号
//    private String id;
//    // 行龄
//    private String cyear;
//    // 职务
//    private String duty;
//    // 状态  1@正常；2@停用；3@离行
//    private String status;
//
//    public String getStatusCode() {
//        if(StringUtils.isNotEmpty(this.status)) {
//            String[] arr = this.status.split("@");
//            return arr[0];
//        }
//        return null;
//    }
//
//    public String getStatusName() {
//        if(StringUtils.isNotEmpty(this.status)) {
//            String[] arr = this.status.split("@");
//            return arr[1];
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        PortalVo portalVo = new PortalVo();
//        portalVo.setStatus("1@正常");
//        System.out.println(portalVo.getStatusCode() + "===》" + portalVo.getStatusName());
//    }
//}
