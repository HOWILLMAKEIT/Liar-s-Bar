package com.liarsbar.liarbar.model;

import java.util.*;


public enum Card {
    A, K, Q, KING; //A K Q （6张） KING （2张）

    // 添加一个静态方法来将字符串转换为Card枚举
    public static Card fromString(String cardStr) {
        return Card.valueOf(cardStr);
    }

    // 一副牌
    public static List<Card> getDeck() {
        List<Card> deck = new ArrayList<>();

        // 添加每张卡片的数量
        for (int i = 0; i < 6; i++) {
            deck.add(A); // A牌有6张
            deck.add(K); // K牌有6张
            deck.add(Q); // Q牌有6张
        }

        for (int i = 0; i < 2; i++) {
            deck.add(KING); // KING牌有2张
        }

        return deck;
    }

    public static void shuffleDeck(List<Card> deck) {
        Collections.shuffle(deck); // 打乱卡片顺序
    }
}

