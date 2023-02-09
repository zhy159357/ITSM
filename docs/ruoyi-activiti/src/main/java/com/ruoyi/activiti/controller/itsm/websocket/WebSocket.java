package com.ruoyi.activiti.controller.itsm.websocket;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.controller.itsm.eventRun.EventRunApiController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@ServerEndpoint("/webSocket/{username}")
@Component
public class  WebSocket {
    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    private String username;
    private static final Log logger = LogFactory.getLog(WebSocket.class);
    public WebSocket() {
        logger.debug("===========websocket连接");
    }
    /**
     * 建立连接的时候
     **/
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {

        this.username = username;
        this.session = session;

        addOnlineCount();
        clients.put(username, this);
       logger.debug("===========websocket连接建立");
    }

    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String mes= jsonObject.getString("message");
        if (!jsonObject.getString("To").equals("All")) {
            sendMessageTo(mes, jsonObject.getString("To"));
        } else {
            sendMessageAll("给所有人");
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public void sendMessageTo(String message, String To) throws IOException {
        for (WebSocket item : clients.values()) {
            if (item.username.equals(To) )
                item.session.getAsyncRemote().sendText(message);
        }
    }
    public void sendMessageAll(String message) throws IOException {
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }

    public Session getSession() {
        return session;
    }
}