package com.ruoyi.activiti.controller.itsm.websocket;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: WebSocketController
 * @Auther: zx
 * @Date: 20210916
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("socket")
public class WebSocketController {
    private String msgToAll="websocket.EventRun";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @ApiOperation("wesocket测试接口")
    @RequestMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message")String message) {
        try {
            redisTemplate.convertAndSend(msgToAll, message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
