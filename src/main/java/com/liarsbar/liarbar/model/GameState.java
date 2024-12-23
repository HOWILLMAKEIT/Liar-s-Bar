package com.liarsbar.liarbar.model;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class GameState {
    private List<Player> players;  // 游戏中的所有玩家
    private Card currentCard;      // 当前轮需要出的牌类型
    private Player currentPlayer;  // 当前出牌的玩家
    private List<Card> card_on_table; // 当前牌桌上的牌（上一名玩家出的牌）
    // 初始化游戏状态
    public GameState(List<Player> players, Card currentCard) {
        this.players = players;
        this.currentCard = currentCard;
        this.currentPlayer = players.get(0);  // 第一位玩家开始
        // 重置玩家状态
        for(Player player : this.players){
            player.newgun();
            player.setisAlive(true);
        }
        // 发牌
        dealCards(players,  5);
    }

    // 发牌函数
    public void dealCards(List<Player> players,  int cardsPerPlayer) {
        // 获取并打乱卡牌
        List<Card> deck = Card.getDeck();
        Card.shuffleDeck(deck);
        int cardIndex = 0;

        for (Player player : players) {
            player.setHand(new ArrayList<>());
            for (int i = 0; i < cardsPerPlayer; i++) {
                if (cardIndex < deck.size()) {
                    player.addCard(deck.get(cardIndex)); // 给玩家发牌
                    cardIndex++;
                }
            }
        }
    }
    // 切换到下一个玩家
    public void nextPlayer() {
        int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
        currentPlayer = players.get(nextPlayerIndex);
        while(currentPlayer.getisAlive() == false){
            nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
            currentPlayer = players.get(nextPlayerIndex);
        }
    }
    //新回合
    public void NewRound(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.card_on_table = new ArrayList<>();
        dealCards(this.players,5);
        // 抽取一张随机的卡牌
        Card[] Cards = {Card.A, Card.K, Card.Q};
        Random random = new Random();
        int index = random.nextInt(Cards.length);
        this.currentCard = Cards[index];
    }

    // set
    public void setCard_on_table(List<Card> card_on_table) {
        this.card_on_table = card_on_table;
    }

    //get
    //上一个出牌的
    public Player getlastPlayer() {
        int lastPlayerIndex = (players.indexOf(currentPlayer) - 1 + players.size()) % players.size();
        while (!players.get(lastPlayerIndex).getisAlive()) {
            lastPlayerIndex = (lastPlayerIndex - 1 + players.size()) % players.size(); // 再切换到上一位玩家
        }
        return players.get(lastPlayerIndex);
    }
    // 当前卡牌
    public Card getCurrentCard() {
        return currentCard;
    }
    // 当前玩家
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public List<Card> getCard_on_table(){
        return card_on_table;
    }
    public List<Player> getPlayers(){
        return players;
    }
    public int getAliveCount() {
        return (int) players.stream()
                .filter(Player::getisAlive)
                .count();
    }
}

