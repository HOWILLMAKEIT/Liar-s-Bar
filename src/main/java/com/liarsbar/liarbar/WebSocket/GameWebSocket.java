package com.liarsbar.liarbar.WebSocket;

import com.liarsbar.liarbar.Controller.GameController;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;


@ServerEndpoint("/ws")
@Component
public class GameWebSocket {

    private static GameController gameController = new GameController();

    // 当 WebSocket 连接开启时
    @OnOpen
    public void onOpen(Session session) {
        String roomId = gameController.getAvailableRoomId();
        gameController.addPlayerToRoom(session, roomId);
    }

    // 处理消息
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);

    }
}

