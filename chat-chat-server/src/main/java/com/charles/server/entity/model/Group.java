package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 聊天室
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long groupId;
    
    private String groupImg;
    
    private String groupTitle;
    
}
