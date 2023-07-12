package com.charles.server.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Charles-H
 * 
 * 聊天主体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBody implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String sendName;
    
    private String receiveName;
    
    private Long sendId;
    
    private Long receivedId;

    private String sendAvatar;
    
    private String receiveAvatar;
    
    private String content;
    
    private Date sendTime;
    
}
