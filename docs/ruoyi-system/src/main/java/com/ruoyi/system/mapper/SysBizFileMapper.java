package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysBizFile;

import java.util.List;

public interface SysBizFileMapper {
    List<SysBizFile> list(SysBizFile file);
    SysBizFile get(Long id);
    Integer deleteByBizId(String bizId);
    Integer insert(SysBizFile file);
    Integer update(SysBizFile file);
    Integer delete(Long[] ids);
}
