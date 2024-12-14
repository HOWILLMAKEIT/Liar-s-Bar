package com.liarsbar.liarbar.WebSocket;

import com.liarsbar.liarbar.Service.GameService;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@ServerEndpoint("/ws")
@Component
public class GameWebSocket {

    private static GameService gameService;

    @Autowired
    public void setGameService(GameService gameService) {
        GameWebSocket.gameService = gameService;
    }

    @OnOpen
    public void onOpen(Session session) {
        String roomId = gameService.getAvailableRoomId();
        gameService.addPlayerToRoom(session, roomId);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        // 处理消息
    }
}


