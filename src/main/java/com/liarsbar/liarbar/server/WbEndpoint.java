package com.liarsbar.liarbar.server;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("ws://localhost:8080/game")
public class WbEndpoint {

    // 存储所有会话
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    // 连接建立时
    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        System.out.println("New connection established: " + session.getId());
    }

    // 处理消息
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        // 示例：发送回响应消息
        try {
            session.getBasicRemote().sendText("Message received: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 连接关闭时
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getId());
        System.out.println("Connection closed: " + session.getId());
    }

    // 发生错误时
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error occurred: " + throwable.getMessage());
    }

    // 广播消息
    public static void broadcastMessage(String message) {
        for (Session session : sessions.values()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
