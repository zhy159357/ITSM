package com.ruoyi.activiti.controller.itsm.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Redis订阅频道处理类
 * @author yangzhendong01
 */
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenerHandle.class);

    private String msgToAll="websocket.EventRun";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private WebSocket webSocket;
    /**
     * 收到监听消息
     * @param message
     * @param bytes
     */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String rawMsg;
        String topic;
        try {
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            LOGGER.info("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }


        if (msgToAll.equals(topic)) {
            LOGGER.info("Send message to all users:" + rawMsg);
            // 发送消息给所有在线Cid
           // chatService.sendMsg(rawMsg);\
            JSONObject js= JSON.parseObject(rawMsg.toString());
            try {
                webSocket.sendMessageTo(js.getString("message"),js.getString("userId"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.warn("No further operation with this topic!");
        }
    }
}