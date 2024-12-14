package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;

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
