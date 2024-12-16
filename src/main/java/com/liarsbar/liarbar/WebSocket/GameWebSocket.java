package com.liarsbar.liarbar.WebSocket;

import com.alibaba.fastjson2.JSON;
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
        if (!isValidJson(message)) {
            // 如果不是JSON格式，当作普通字符串处理
            System.out.println("收到普通文本消息: " + message);
            return;
            }
        System.out.println("Received message: " + message);
        gameService.handleMessage(session,message);
    }
    // 判断字符串是否是合法的JSON格式
    private boolean isValidJson(String str) {
        try {
            JSON.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


