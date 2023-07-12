package com.charles.server.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryBody implements Serializable {
    
    private String username;
    
    private String chatUser;
    
    private ChatBody body;
    
}
