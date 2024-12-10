package com.liarsbar.liarbar.server;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    // 连接建立时
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New connection established: " + session.getId());
    }

    // 处理消息
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        try {
            session.getBasicRemote().sendText("Message received: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 连接关闭时
    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
    }

    // 发生错误时
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error occurred: " + throwable.getMessage());
    }
}
