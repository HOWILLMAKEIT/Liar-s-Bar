package com.liarsbar.liarbar.WebSocket;

import com.liarsbar.liarbar.message.CurrentCard;
import com.liarsbar.liarbar.message.HandCards;
import com.liarsbar.liarbar.message.MessageProcessor;
import com.liarsbar.liarbar.model.Card;
import com.liarsbar.liarbar.services.Game;
import com.liarsbar.liarbar.model.Player;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    private static final Map<String, Game> games = new HashMap<>();
    private static final Map<String, Player> players = new HashMap<>();
    private static final Map<String, List<Player>> waitingPlayers = new HashMap<>(); //房间和四名player构成一个元组

    @OnOpen
    public void onOpen(Session session) {
        // 创建Player对象
        Player player = new Player(session);
        players.put(session.getId(), player);

        // 获取房间ID
        String roomId = getAvailableRoomId();  // 获取一个可用的房间ID

        // 将Player对象添加到waitingPlayers中
        if (!waitingPlayers.containsKey(roomId)) {
            waitingPlayers.put(roomId, new ArrayList<>());
        }
        waitingPlayers.get(roomId).add(player);

        // 如果房间中已经有四名玩家，开始游戏
        if (waitingPlayers.get(roomId).size() == 4) {
            // 创建Game对象
            Game game = new Game(waitingPlayers.get(roomId));
            games.put(roomId, game);
            GameStart(game, waitingPlayers.get(roomId));
        }
    }
    // 获取一个可用的房间ID
    private String getAvailableRoomId() {
        // 查找一个空的房间ID，如果当前所有房间已经满员，则创建一个新的房间
        for (int i = 1; ; i++) {
            String roomId = String.valueOf(i);
            if (!waitingPlayers.containsKey(roomId) || waitingPlayers.get(roomId).size() < 4) {
                return roomId;
            }
        }
    }
    public void GameStart(Game game,List<Player> players){
        //确定初始牌：
        Card currentCard = game.getGameState().getCurrentCard();
        CurrentCard Msg_current_card = new CurrentCard(currentCard);

        // 利用 MessageProcessor 工具类将 Msg_current_card 转换为 JSON
        MessageProcessor processor = new MessageProcessor();
        String current_card_json = processor.serialize(Msg_current_card);
        // 遍历玩家列表，发送本局卡牌信息到每个玩家
        for (Player player : players) {
            Session session = player.getSession(); // 获取玩家的 WebSocket Session
            List<Card> handcards = player.getHand();
            HandCards Msg_HandCards = new HandCards(handcards);
            String handcards_json = processor.serialize(Msg_HandCards);

            if (session != null && session.isOpen()) {
                try {
                    //发送
                    session.getBasicRemote().sendText(current_card_json);
                    session.getBasicRemote().sendText(handcards_json);
                    System.out.println("消息已发送到玩家：" + player.getName());
                } catch (IOException e) {
                    System.err.println("消息发送失败：" + e.getMessage());
                }
            } else {
                System.err.println("玩家的 Session 不可用或已关闭：" + player.getName());
            }
        }


    }
    private void sendMessageToAllPlayersInRoom(String roomId, String message) {
        List<Player> playersInRoom = waitingPlayers.get(roomId);
        for (Player player : playersInRoom) {
            try {
                player.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 处理消息
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
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
