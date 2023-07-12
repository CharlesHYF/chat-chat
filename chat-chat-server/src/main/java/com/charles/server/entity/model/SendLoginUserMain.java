package com.charles.server.entity.model;

import com.charles.server.entity.ChatUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 发送给用户登录和好友资料
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendLoginUserMain implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ChatUser chatUser;
    
    private MainInfo mainInfo;
    
}
