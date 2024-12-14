package com.liarsbar.liarbar.Service;


import com.liarsbar.liarbar.model.Card;
import com.liarsbar.liarbar.model.Game;
import com.liarsbar.liarbar.model.Player;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final SendMessageService sendMessageService;
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<String, List<Player>> waitingPlayers = new ConcurrentHashMap<>();

    @Autowired
    public GameService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public String getAvailableRoomId() {
        for (int i = 1; ; i++) {
            String roomId = String.valueOf(i);
            if (!waitingPlayers.containsKey(roomId) || waitingPlayers.get(roomId).size() < 4) {
                return roomId;
            }
        }
    }

    public void addPlayerToRoom(Session session, String roomId) {
        sendMessageService.sendPlayerId(session);
        Player player = new Player(session);
        players.put(session.getId(), player);

        waitingPlayers.computeIfAbsent(roomId, k -> new ArrayList<>()).add(player);

        if (waitingPlayers.get(roomId).size() == 4) {
            startGame(roomId);
        }
    }

    private void startGame(String roomId) {
        List<Player> roomPlayers = waitingPlayers.get(roomId);
        Game game = new Game(roomPlayers);
        games.put(roomId, game);

        List<String> playerIds = roomPlayers.stream()
                .map(player -> player.getSession().getId())
                .collect(Collectors.toList());

        Card currentCard = game.getGameState().getCurrentCard();
        String currentPlayerId = game.getGameState().getCurrentPlayer().getSession().getId();

        for (Player player : roomPlayers) {
            Session session = player.getSession();
            sendMessageService.sendPlayerIds(session, playerIds);
            sendMessageService.sendCurrentCard(session, currentCard);
            sendMessageService.sendHandCards(session, player.getHand());
            sendMessageService.sendCurrentPlayer(session, currentPlayerId);
        }
    }
}

