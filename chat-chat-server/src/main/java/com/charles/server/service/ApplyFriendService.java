package com.charles.server.service;

import com.charles.server.entity.ChatUser;

import java.util.List;

public interface ApplyFriendService {
    
    List<ChatUser> queryApplyUser(Long userId);
    
}
