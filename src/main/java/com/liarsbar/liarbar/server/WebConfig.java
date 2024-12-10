package com.liarsbar.liarbar.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebConfig implements WebSocketConfigurer {

    private final WebHandler webHandler;

    // 通过构造函数注入 WebServer
    public WebConfig(WebHandler webHandler) {
        this.webHandler = webHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 端点 /gamepage，使用 webServer 作为处理器
        registry.addHandler(webHandler, "/ws")
                .setAllowedOrigins("*");  // 设置允许的来源
    }
}
