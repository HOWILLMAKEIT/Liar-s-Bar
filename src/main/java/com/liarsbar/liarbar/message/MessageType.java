package com.liarsbar.liarbar.Message;

public enum MessageType {
    PlayerId("player-id"),
    PlayerIds("player-ids"),
    TheChosenCard("chosen-card"),
    HandCard("hand-card"),
    CurrentPlayer("current-player"),
    PlayedCard("played-card"),
    PLAY("play"),
    DOUBT("doubt"),
    GAMEOVER("game-over"),
    SHOT("shot");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    // 重写toString方法，这样序列化时会使用这个值
    @Override
    public String toString() {
        return this.value;
    }
}
