package com.liarsbar.liarbar.Service;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageProcessor;
import com.liarsbar.liarbar.Message.Msg_Server.*;
import com.liarsbar.liarbar.model.Card;
import jakarta.websocket.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SendMessageService {
    private final MessageProcessor processor = new MessageProcessor();

    public void sendPlayerId(Session session) {
        sendMessage(session, new PlayerId(session.getId()));
    }
    public void sendPlayerIds(Session session, List<String> ids) {
        sendMessage(session, new PlayerIds(ids));
    }
    public void sendCurrentCard(Session session, Card currentCard) {
        sendMessage(session, new CurrentCard(currentCard));
    }
    public void sendHandCards(Session session, List<Card> handCards) {
        sendMessage(session, new HandCards(handCards));
    }
    public void sendCurrentPlayer(Session session, String currentPlayerId) {
        sendMessage(session, new CurrentPlayer(currentPlayerId));
    }
    public void sendPlayedCard(Session session, List<Card> cards,String id){
        sendMessage(session,new PlayedCard(cards,id));
    }
    public void sendShot(Session session,boolean res, String id) {
        sendMessage(session, new Shot(res,id));
    }

    private void sendMessage(Session session, Object message) {
        if (session != null && session.isOpen()) {
            try {
                String json = processor.serialize((Message) message);
                session.getBasicRemote().sendText(json);
                System.out.println("消息已发送到玩家。");
            } catch (IOException e) {
                System.err.println("发送消息失败：" + e.getMessage());
            }
        }
    }
}


