package com.liarsbar.liarbar.model;

import jakarta.websocket.Session;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;  // 玩家手中的牌
    private boolean isAlive;  // 玩家是否还活着
    private Gun gun;
    private Session session;
    private String roomId;  // 添加roomId字段

    public Player(Session session) {
        this.name = session.getId();
        this.session = session;
        this.hand = new ArrayList<>();; //初始手牌为空
        this.isAlive = true;
        this.gun = new Gun(); // 生成一把随机的枪
    }



    public Gun getgun(){
        return this.gun;
    }

    public boolean shothimself(){
        // 中弹
        if(this.gun.shot()){
            this.isAlive = false;
            return true;//中枪
        }
        else return false;
    }
    public void newgun(){
        this.gun = new Gun(); // 生成一把随机的枪
    }
    public void addCard(Card card){
        hand.add(card);
    }
    // 移除特定手牌
    public void removeCard(Card card) {
        hand.remove(card);
    }
    // set
    public void setHand(List<Card> cards){
        this.hand = cards;
    }
    public void setisAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }



    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    // get
    public String getRoomId(){
        return this.roomId;
    }
    public boolean getisAlive() {
        return this.isAlive;
    }
    public List<Card> getHand() {return hand;}
    public Session getSession() {return session;}

}

