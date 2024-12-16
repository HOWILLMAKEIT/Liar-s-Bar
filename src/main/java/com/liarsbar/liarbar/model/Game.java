package com.liarsbar.liarbar.model;

import java.util.Random;
import java.util.List;


public class Game {
    // 每一局游戏 对应一个特定的gameState
    private GameState gameState;

    public Game(List<Player> players) {
        // 抽取一张随机的卡牌
        Card[] Cards = {Card.A, Card.K, Card.Q};
        Random random = new Random();
        int index = random.nextInt(Cards.length);
        //初始化游戏状态：
        this.gameState = new GameState(players,Cards[index]);
    }


    // 玩家选择出牌
    public void PLAY(Player player, List<Card> cards) {
        // 遍历cards列表，丢弃每张牌
        for (Card card : cards) {
            player.removeCard(card); // 丢弃卡牌
        }
        // 现在牌桌上的牌更新
        this.gameState.setCard_on_table(cards);
        //下一个玩家
        gameState.nextPlayer();
    }

    //玩家选择质疑
    // 返回需要开枪的玩家
    public Player DOUBT(Player player) {

        for(Card card: this.gameState.getCard_on_table()){
            // 如果这张牌既不是目标牌，又不是KING
            if (card != gameState.getCurrentCard() && card != Card.KING){
                Player last_player = gameState.getlastPlayer();
                System.out.printf("质疑成功!");
                return last_player;
            }
        }
        // 质疑失败本玩家开枪
        System.out.printf("质疑失败!");
        return player;
    }
    public GameState getGameState() {return gameState;}
}
