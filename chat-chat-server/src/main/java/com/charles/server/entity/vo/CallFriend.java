package com.charles.server.entity.vo;

import com.charles.server.entity.model.Friend;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallFriend implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Friend friend;
    
    private Long userId;
    
}
