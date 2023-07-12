package com.charles.server.entity.vo;

import com.charles.server.entity.ChatContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 发送聊天内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendChatContent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String sendName;
    
    private String receiveName;
    
    private String sendAvatar;
    
    private String receiveAvatar;
    
    private ChatContent chatContent;
    
}
