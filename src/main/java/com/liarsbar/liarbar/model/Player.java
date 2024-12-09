package com.liarsbar.liarbar.model;

import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;  // 玩家手中的牌
    private boolean isAlive;  // 玩家是否还活着
    private Gun gun;

    public Player(String name, List<Card> hand) {
        this.name = name;
        this.hand = hand;
        this.isAlive = true;
        this.gun = new Gun(); // 生成一把随机的枪
    }
    public Gun getgun(){
        return this.gun;
    }
    public void newgun(){
        this.gun = new Gun(); // 生成一把随机的枪
    }
    public void shothimself(){
        // 中弹
        if(this.gun.shot()){
            this.isAlive = false;
        }
        else return;
    }
    public boolean getisAlive() {
        return this.isAlive;
    }
    // 判断是否有特定手牌
    public boolean hasCard(Card card) {
        return hand.contains(card);
    }
    public void addCard(Card card){
        hand.add(card);
    }
    // 移除特定手牌
    public void removeCard(Card card) {
        hand.remove(card);
    }
    public String getName(){
        return name;
    }
}

