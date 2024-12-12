package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.model.Card;

public class CurrentCard implements Message{
    public Card card;
    public MessageType type = MessageType.TheChosenCard;

    public CurrentCard(Card card) {
        this.card = card;
    }
    @Override
    public MessageType getType() {
        return this.type;
    }
}
