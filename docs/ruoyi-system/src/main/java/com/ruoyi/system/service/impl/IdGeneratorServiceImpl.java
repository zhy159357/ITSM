package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.IdGeneratorMapper;
import com.ruoyi.system.service.IIdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author
 */
@Service
public class IdGeneratorServiceImpl implements IIdGeneratorService {

    @Autowired
    private IdGeneratorMapper generatorMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    @Transactional
    public synchronized String selectIdGeneratorByType(IdGenerator generator) {
        Long currentId;
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        IdGenerator idGenerator = selectIdGeneratorByType2(generator);
        if (idGenerator == null) {
            currentId = 1L;
        } else if (!nowDateStr.equals(idGenerator.getCurrentDate())) {
            // 如果根据当前时间查询没有查询到数据，则默认赋值id为1，并修改当前时间
            generator.setCurrentId(1L);
            generator.setCurrentDate(nowDateStr);
            currentId = generator.getCurrentId();
            generator.setId(idGenerator.getId());
            generatorMapper.updateGenerator(generator);
        } else {
            currentId = idGenerator.getCurrentId() + 1L;
            // 如果查询到数据，则将id默认加1
            idGenerator.setCurrentId(currentId);
            generatorMapper.updateGenerator(idGenerator);
        }
        return String.format("%06d", currentId);
    }

    @Override
    public synchronized void updateGenerator(IdGenerator generator) {
        generatorMapper.updateGenerator(generator);
    }

    @Override
    public synchronized String createNoAsLength(String type, int length) {
        Long currentId;
        IdGenerator generator = new IdGenerator();
        generator.setBizType(type);
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        IdGenerator idGenerator = selectIdGeneratorByType2(generator);
        if (idGenerator == null) {
            generator.setId(UUID.getUUIDStr());
            generator.setCurrentId(1L);
            generator.setBizType(type);
            generator.setCurrentDate(nowDateStr);
            currentId = generator.getCurrentId();
            generatorMapper.insertGenerator(generator);
        } else if(!nowDateStr.equals(idGenerator.getCurrentDate())){
            // 如果根据当前时间查询没有查询到数据，则默认赋值id为1，并修改当前时间
            generator.setCurrentId(1L);
            generator.setCurrentDate(nowDateStr);
            currentId = generator.getCurrentId();
            generator.setId(idGenerator.getId());
            generatorMapper.updateGenerator(generator);
        }else {
            currentId = idGenerator.getCurrentId() + 1L;
            // 如果查询到数据，则将id默认加1
            idGenerator.setCurrentId(currentId);
            generatorMapper.updateGenerator(idGenerator);
        }
        return type + nowDateStr + String.format("%0"+length+"d", currentId);
    }

    //兼容mySql
    private IdGenerator selectIdGeneratorByType2(IdGenerator generator) {
        if (generator.getBizType().equals("problem_task_new")) {
            return generatorMapper.selectIdGeneratorByType(generator);
        }
        if ("mysql".equals(dbType)) {
            return generatorMapper.selectIdGeneratorByTypeMysql(generator);
        } else {
            return generatorMapper.selectIdGeneratorByType(generator);
        }
    }


    @Override
    public String createProblemTaskNo(String problemNo) {
        Long currentId;
        IdGenerator generator = new IdGenerator();
        generator.setBizType("problem_task_new");
        generator.setCurrentDate(problemNo);
        IdGenerator idGenerator = selectIdGeneratorByType2(generator);
        if (idGenerator == null) {
            generator.setId(UUID.getUUIDStr());
            generator.setCurrentId(1L);
            generator.setBizType("problem_task_new");
            generator.setCurrentDate(problemNo);
            currentId = generator.getCurrentId();
            generatorMapper.insertGenerator(generator);
        } else {
            currentId = idGenerator.getCurrentId() + 1L;
            // 如果查询到数据，则将id默认加1
            idGenerator.setCurrentId(currentId);
            generatorMapper.updateGenerator(idGenerator);
        }
        return problemNo + "T" + String.format("%0"+2+"d", currentId);
    }
}
