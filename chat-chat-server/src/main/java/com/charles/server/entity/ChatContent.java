package com.charles.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Charles-H
 * 
 * 聊天表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatContent implements Serializable {

    private static final Long serialVersionUID = 1L;

    /** 聊天id */
    @TableId(value = "chat_id")
    private Long chatId;
    
    /** 发送者id */
    private Long sendId;
    
    /** 接收者id */
    private Long receiveId;
    
    /** 群组id */
    private Long groupId;
    
    /** 聊天内容 */
    private String content;
    
    /** 发送时间 */
    private Date sendTime;

}
