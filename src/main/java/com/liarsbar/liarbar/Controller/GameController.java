package com.liarsbar.liarbar.Controller;

import com.liarsbar.liarbar.Message.*;
import com.liarsbar.liarbar.Message.Msg_Server.*;
import com.liarsbar.liarbar.model.Card;
import com.liarsbar.liarbar.model.Game;
import com.liarsbar.liarbar.model.Player;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController {

    private static final Map<String, Game> games = new HashMap<>();
    private static final Map<String, Player> players = new HashMap<>();
    private static final Map<String, List<Player>> waitingPlayers = new HashMap<>();
    private final MessageProcessor processor = new MessageProcessor();

    // 获取一个可用的房间ID
    public String getAvailableRoomId() {
        for (int i = 1; ; i++) {
            String roomId = String.valueOf(i);
            if (!waitingPlayers.containsKey(roomId) || waitingPlayers.get(roomId).size() < 4) {
                return roomId;
            }
        }
    }

    // 添加玩家到房间
    public void addPlayerToRoom(Session session, String roomId) {
        sendPlayerId(session);
        Player player = new Player(session);
        players.put(session.getId(), player);

        waitingPlayers.computeIfAbsent(roomId, k -> new ArrayList<>()).add(player);

        // 如果房间已经满员，开始游戏
        if (waitingPlayers.get(roomId).size() == 4) {
            Game game = new Game(waitingPlayers.get(roomId));
            games.put(roomId, game);
            startGame(game, waitingPlayers.get(roomId), roomId);
        }
    }

    // 启动游戏
    public void startGame(Game game, List<Player> players, String roomId) {

        List<String> playerIdList = players.stream()
                .map(player -> player.getSession().getId())
                .collect(Collectors.toList());

        Card currentCard = game.getGameState().getCurrentCard();
        String currentPlayerId = game.getGameState().getCurrentPlayer().getSession().getId();

        for (Player player : players) {
            Session session = player.getSession();
            sendPlayerIds(session, playerIdList);
            sendCurrentCard(session, currentCard);
            sendHandCards(session, player.getHand());
            sendCurrentPlayer(session, currentPlayerId);
        }
    }

//    // 处理出牌
//    public void handlePlayCards(List<String> cardStrings, Session session) {
//        List<Card> cards = new ArrayList<>();
//        for (String cardStr : cardStrings) {
//            Card card = Card.fromString(cardStr);
//            if (card != null) {
//                cards.add(card);
//            }
//        }
//
//        // 根据业务需求处理出牌
//        Game game = getGameBySession(session);
//        if (game != null) {
//            game.playCards(cards);
//            // 通知其他玩家
//            notifyPlayers(game, cards);
//        }
//    }

//    // 处理质疑
//    public void handleDoubt(List<String> cards, Session session) {
//        // 处理质疑的业务逻辑
//        System.out.println("质疑的卡牌：" + cards);
//    }

//    // 获取游戏对象
//    private Game getGameBySession(Session session) {
//        for (Game game : games.values()) {
//            if (game.isPlayerInGame(session)) {
//                return game;
//            }
//        }
//        return null;
//    }

    //发送玩家id到前端
    public void sendPlayerId(Session session){
        if (session != null && session.isOpen()) {
            try {
                // 构造消息并序列化
                PlayerId Msg_player_id = new PlayerId(session.getId());
                String Msg_player_id_json = processor.serialize(Msg_player_id);

                // 发送消息
                session.getBasicRemote().sendText(Msg_player_id_json);
                System.out.println("当前玩家id信息已发送到玩家。");
            } catch (IOException e) {
                System.err.println("发送玩家id信息信息失败：" + e.getMessage());
            }
        } else {
            System.err.println("Session 不可用或已关闭。");
        }
    }
    // 发送房间内玩家的id
    public void sendPlayerIds(Session session, List<String> ids) {
        if (session != null && session.isOpen()) {
            try {
                // 构造消息并序列化
                PlayerIds Msg_Player_ids = new PlayerIds(ids);
                String Msg_Player_ids_json = processor.serialize(Msg_Player_ids);

                // 发送消息
                session.getBasicRemote().sendText(Msg_Player_ids_json);
                System.out.println("当前房间玩家id信息已发送到玩家。");
            } catch (IOException e) {
                System.err.println("当前房间玩家id信息失败：" + e.getMessage());
            }
        } else {
            System.err.println("Session 不可用或已关闭。");
        }
    }
    // 发送当前卡牌信息到玩家
    public void sendCurrentCard(Session session, Card currentCard) {
        if (session != null && session.isOpen()) {
            try {
                // 构造消息并序列化
                CurrentCard Msg_current_card = new CurrentCard(currentCard);
                String current_card_json = processor.serialize(Msg_current_card);

                // 发送消息
                session.getBasicRemote().sendText(current_card_json);
                System.out.println("当前卡牌信息已发送到玩家。");
            } catch (IOException e) {
                System.err.println("发送当前卡牌信息失败：" + e.getMessage());
            }
        } else {
            System.err.println("Session 不可用或已关闭。");
        }
    }
    // 发送玩家手牌信息
    public void sendHandCards(Session session, List<Card> handCards) {
        if (session != null && session.isOpen()) {
            try {
                // 构造消息并序列化
                HandCards Msg_HandCards = new HandCards(handCards);
                String handcards_json = processor.serialize(Msg_HandCards);

                // 发送消息
                session.getBasicRemote().sendText(handcards_json);
                System.out.println("手牌信息已发送到玩家。");
            } catch (IOException e) {
                System.err.println("发送手牌信息失败：" + e.getMessage());
            }
        } else {
            System.err.println("Session 不可用或已关闭。");
        }
    }
    // 发送当前玩家信息
    private void sendCurrentPlayer(Session session, String currentPlayerId) {
        if (session != null && session.isOpen()) {
            try {
                // 构造消息并序列化
                CurrentPlayer Msg_CurrenetPlayer = new CurrentPlayer(currentPlayerId);
                String Msg_CurrenetPlayer_json = processor.serialize(Msg_CurrenetPlayer);

                // 发送消息
                session.getBasicRemote().sendText(Msg_CurrenetPlayer_json);
                System.out.println("当前玩家信息已发送到玩家。");
            } catch (IOException e) {
                System.err.println("发送当前玩家信息信息失败：" + e.getMessage());
            }
        } else {
            System.err.println("Session 不可用或已关闭。");
        }
    }

}

