package com.liarsbar.liarbar.Service;


import com.alibaba.fastjson2.JSON;
import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageProcessor;
import com.liarsbar.liarbar.Message.MessageType;
import com.liarsbar.liarbar.Message.Msg_Client.PlayCard;
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
    private final HandleMessageService handleMessageService;

    private final Map<String, Game> games = new ConcurrentHashMap<>(); // roomid + game
    private final Map<String, Player> players = new ConcurrentHashMap<>(); // palyerid + player
    private final Map<String, List<Player>> waitingPlayers = new ConcurrentHashMap<>(); // roomid + players
    private final MessageProcessor processor = new MessageProcessor();

    @Autowired
    public GameService(SendMessageService sendMessageService, HandleMessageService handleMessageService) {
        this.sendMessageService = sendMessageService;
        this.handleMessageService = handleMessageService;
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
        player.setRoomId(roomId);

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

    public void handleMessage(Session session,String messageJson) {
        // 获取玩家对应的玩家对象和游戏对象
        Player player = players.get(session.getId());
        Game game = games.get(player.getRoomId());
        List<Player> roomPlayers = waitingPlayers.get(player.getRoomId());

        try {
            // 反序列化为java类对象
            Message baseMessage = processor.deserialize(messageJson);
            switch (baseMessage.getType()) {
                case PLAY:
                    PlayCard playCard = (PlayCard) baseMessage;
                    handleMessageService.HandlePLAY(game,player,playCard.getCards(), roomPlayers,playCard);
                    break;
                case DOUBT:
                    //后端处理
                    Player PlayerNextRound = handleMessageService.HandleDOUBT(game,player, roomPlayers);
                    //发送消息到前端
                    NewRound(roomPlayers,PlayerNextRound,game);
                    break;
                default:
                    System.err.println("Unknown message type: " + baseMessage.getType());
            }
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void NewRound(List<Player> players,Player CurrentPlayer,Game game){
        for (Player eachPlayer : players) {
            Session eachSession = eachPlayer.getSession();
            this.sendMessageService.sendCurrentPlayer(eachSession,CurrentPlayer.getSession().getId());
            this.sendMessageService.sendHandCards(eachSession,eachPlayer.getHand());
            this.sendMessageService.sendCurrentCard(eachSession, game.getGameState().getCurrentCard());
        }
    }

}

