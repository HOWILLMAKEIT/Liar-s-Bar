package com.liarsbar.liarbar.Message.Msg_Client;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;
import com.liarsbar.liarbar.model.Card;

import java.util.ArrayList;
import java.util.List;

public class PlayCard extends Message {
    private List<String> cards;

    // 无参构造函数（FastJSON需要）
    public PlayCard() {
        super(MessageType.PLAY);
        this.cards = new ArrayList<>();
    }

    public PlayCard(List<String> cards) {
        super(MessageType.PLAY);
        this.cards = new ArrayList<>(cards);  // 创建新的ArrayList
    }

    public List<Card> getCards() {
        List<Card> cardList = new ArrayList<>();
        for (String cardStr : cards) {
            Card card = Card.fromString(cardStr);
            cardList.add(card);
        }
        return cardList;
    }
}

