package com.liarsbar.liarbar.Message.Msg_Client;

import com.liarsbar.liarbar.Message.Message;
import com.liarsbar.liarbar.Message.MessageType;


public class DOUBT extends Message {
    // 无参构造函数（FastJSON需要）
    public DOUBT() {
        super(MessageType.DOUBT);
    }

}