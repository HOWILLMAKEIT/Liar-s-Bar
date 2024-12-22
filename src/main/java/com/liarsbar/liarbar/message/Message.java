package com.liarsbar.liarbar.Message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.liarsbar.liarbar.Message.Msg_Client.DOUBT;
import com.liarsbar.liarbar.Message.Msg_Client.PLAY;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PLAY.class, name = "PLAY"),
        @JsonSubTypes.Type(value = DOUBT.class, name = "DOUBT"),
        @JsonSubTypes.Type(value = DOUBT.class, name = "GAMEOVER"),
})
public abstract class Message {
    @JSONField(name = "type")
    private MessageType type;

    public Message(MessageType messageType) {
        this.type = messageType;
    }
    public Message(){

    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }
}