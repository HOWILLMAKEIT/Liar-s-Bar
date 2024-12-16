package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;
import com.liarsbar.liarbar.model.Card;

import java.util.List;

public class PlayedCard extends Message {
    public List<Card> cards;
    public String id;

    private MessageType type = MessageType.PlayedCard;
    public PlayedCard(List<Card> cards,String id) {
        this.cards = cards;
        this.id = id;
    }
    @Override
    public MessageType getType() {
        return this.type;
    }
}
