package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.model.Card;

import java.util.List;


public class HandCards implements Message{
    public List<Card> cards;
    public MessageType type = MessageType.HandCard;

    public HandCards(List<Card> cards) {
        this.cards = cards;
    }
    @Override
    public MessageType getType() {
        return this.type;
    }
}