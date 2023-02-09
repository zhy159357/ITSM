package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName MyMetaObjectHandler
 * @Description mybatis-plus自动填充字段配置
 * @Author JiaQi Zhang
 * @Date 2022/7/4 7:52
 */
@Slf4j
@Component
public class MyMetaObjectHandler  implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
    }
}
