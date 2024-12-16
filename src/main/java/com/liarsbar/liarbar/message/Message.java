package com.liarsbar.liarbar.Message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.liarsbar.liarbar.Message.Msg_Client.Doubt;
import com.liarsbar.liarbar.Message.Msg_Client.PlayCard;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayCard.class, name = "PLAY"),
        @JsonSubTypes.Type(value = Doubt.class, name = "DOUBT"),

        // ... 其他类型
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