package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;

import java.util.List;

public class PlayerIds extends Message {
    public List<String> playerIds;
    public MessageType type = MessageType.PlayerIds;

    public PlayerIds(List<String> ids) {
        this.playerIds = ids;
    }

    @Override
    public MessageType getType() {
        return this.type;
    }
}