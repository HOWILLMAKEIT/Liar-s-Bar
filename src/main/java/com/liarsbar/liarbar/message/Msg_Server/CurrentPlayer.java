package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;

public class CurrentPlayer implements Message {
    public String id;
    public MessageType type = MessageType.CurrentPlayer;

    public CurrentPlayer(String id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return this.type;
    }
}