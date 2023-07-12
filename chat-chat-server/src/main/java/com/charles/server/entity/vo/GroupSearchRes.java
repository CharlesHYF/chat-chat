package com.charles.server.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSearchRes implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long groupId;
    
    private String groupName;
    
    private String groupAvatar;
    
}
