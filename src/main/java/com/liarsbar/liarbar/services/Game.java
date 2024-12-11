package com.liarsbar.liarbar.services;

import com.liarsbar.liarbar.model.Card;
import com.liarsbar.liarbar.model.GameState;
import com.liarsbar.liarbar.model.Player;

import java.util.Random;
import java.util.List;


public class Game {
    // 每一局游戏 对应一个特定的gameState
    private GameState gameState;
    private List<Card> card_on_table;

    public Game(List<Player> players) {
        // 抽取一张随机的卡牌
        Card[] Cards = {Card.A, Card.K, Card.Q};
        Random random = new Random();
        int index = random.nextInt(Cards.length);
        //初始化游戏状态：
        this.gameState = new GameState(players,Cards[index]);
    }

    // 玩家选择出牌
    public String declareCards(Player player, List<Card> cards) {
        // 遍历cards列表，丢弃每张牌
        for (Card card : cards) {
            player.removeCard(card); // 丢弃卡牌
        }
        // 现在牌桌上的牌更新
        this.card_on_table = cards;
        //下一个玩家
        gameState.nextPlayer();
        return player.getName() + " declared " + cards.size() + " cards.";
    }
    public GameState getGameState() {return gameState;}
    //玩家选择质疑
    public String challenge(Player player) {
        for(Card card: card_on_table){
            // 如果这张牌既不是目标牌，又不是KING
            if (card != gameState.getCurrentCard() && card != Card.KING){
                Player last_player = gameState.getlastPlayer();
                last_player.shothimself();
                gameState.nextPlayer();
                return "质疑成功";
            }
        }
        player.shothimself();
        gameState.nextPlayer();
        return"质疑失败";
    }
}
