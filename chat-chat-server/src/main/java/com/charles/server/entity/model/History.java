package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Charles-H
 * 
 * 近期消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class History implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    
    private String avatar;
    
    private String username;
    
    private String content;
    
    private Date lastTime;
    
    private Boolean isGroup;
    
}
