package com.liarsbar.liarbar.Message.Msg_Server;

public class PlayerId implements Message {
    public String playerId;
    public MessageType type = MessageType.PlayerId;

    public PlayerId(String id) {
        this.playerId = id;
    }

    @Override
    public MessageType getType() {
        return this.type;
    }
}
