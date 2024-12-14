package com.liarsbar.liarbar.Service;

import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandleMessageService {
    private final GameService gameService;
    private final SendMessageService sendMessageService;

    @Autowired
    public HandleMessageService(GameService gameService, SendMessageService sendMessageService) {
        this.gameService = gameService;
        this.sendMessageService = sendMessageService;
    }


}

