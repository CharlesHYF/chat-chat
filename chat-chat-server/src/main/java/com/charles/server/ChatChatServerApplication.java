package com.charles.server;

import com.charles.server.mapper.UserFriendMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.Resource;

@SpringBootApplication
public class ChatChatServerApplication {
    
    public static void main(String[] args) {
        new SpringApplicationBuilder(ChatChatServerApplication.class).headless(false).run(args);
        ViewStart.startServer();
    }

}
