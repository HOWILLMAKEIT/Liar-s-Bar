package com.liarsbar.liarbar.Message.Msg_Server;

import java.util.List;

public class PlayerIds implements Message {
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