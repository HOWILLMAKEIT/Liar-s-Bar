package com.liarsbar.liarbar.model;
import org.springframework.stereotype.Service;
import java.util.List;



public class GameState {
    private List<Player> players;  // 游戏中的所有玩家
    private Card currentCard;      // 当前轮需要出的牌类型
    private Player currentPlayer;  // 当前出牌的玩家

    // 初始化游戏状态
    public GameState(List<Player> players, Card currentCard) {
        this.players = players;
        this.currentCard = currentCard;
        this.currentPlayer = players.get(0);  // 第一位玩家开始
        for(Player player : players){
            player.newgun(); // 每局游戏重新装一次子弹
        }
        // 获取并打乱卡牌
        List<Card> deck = Card.getDeck();
        Card.shuffleDeck(deck);
        // 发牌
        dealCards(players, deck, 5);
    }
    // 发牌函数
    public static void dealCards(List<Player> players, List<Card> deck, int cardsPerPlayer) {
        int cardIndex = 0;

        for (int i = 0; i < cardsPerPlayer; i++) {
            for (Player player : players) {
                if (cardIndex < deck.size()) {
                    player.addCard(deck.get(cardIndex)); // 给玩家发牌
                    cardIndex++;
                }
            }
        }
    }
    // 获取当前卡牌
    public Card getCurrentCard() {
        return currentCard;
    }
    // 判定是否为当前出牌者
    public boolean isCurrentPlayer(Player player) {
        return currentPlayer.equals(player);
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
    public Player getlastPlayer() {
        int lastPlayerIndex = (players.indexOf(currentPlayer) - 1 + players.size()) % players.size();
        return players.get(lastPlayerIndex);
    }
}

