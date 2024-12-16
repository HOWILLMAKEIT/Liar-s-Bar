package com.liarsbar.liarbar.Message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liarsbar.liarbar.Message.Msg_Client.PlayCard;

import com.alibaba.fastjson2.JSONArray;
import java.util.List;

public class MessageProcessor {
    private final ObjectMapper mapper;
    public MessageProcessor() {
        // 初始化一个普通的ObjectMapper即可，不需要特殊配置
        mapper = new ObjectMapper();}

    // 将 信息序列化为 Json
    public String serialize(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        // 设置序列化配置
        return JSON.toJSONString(message,
                JSONWriter.Feature.WriteClassName,// 写入类名
                JSONWriter.Feature.WriteEnumUsingToString  // 使用枚举的toString方法
        );
    }

    public Message deserialize(String json) {
        try {
            // 直接反序列化即可，注解会自动处理类型转换
            return mapper.readValue(json, Message.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error handling message: " + e.getMessage());
            System.err.println("Problematic JSON: " + json);  // 添加错误的JSON内容，方便调试
            throw new RuntimeException(e);
        }
    }
}


