package com.liarsbar.liarbar.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

public class MessageProcessor {

    // 将 信息序列化为 Json
    public String serialize(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        return JSON.toJSONString(message, JSONWriter.Feature.WriteClassName); // WriteClassName 用于保留类信息
    }

    // 将Json转化为信息
    public Message deserialize(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("JSON cannot be null or empty");
        }
        return JSON.parseObject(json, Message.class); // 自动解析为具体的 Message 子类
    }

}


