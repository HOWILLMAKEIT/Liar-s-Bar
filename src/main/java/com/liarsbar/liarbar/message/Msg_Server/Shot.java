package com.liarsbar.liarbar.Message.Msg_Server;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;
import com.liarsbar.liarbar.model.Player;

public class Shot extends Message {
    public boolean ShotRes;
    public String id;
    public MessageType type = MessageType.SHOT;

    public Shot(boolean shotres, String id) {
        this.ShotRes = shotres;
        this.id = id;
    }
}
