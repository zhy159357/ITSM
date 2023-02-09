package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.OgRolePost;
import com.ruoyi.system.domain.SysRolePost;

import java.util.List;

public interface SysRolePostMapper {


   // public int batchPostRole(List<SysRolePost> list);


    public int batchPostRole(List<OgRolePost> list);


    int insertAuthRoles(OgRolePost rp);


}
