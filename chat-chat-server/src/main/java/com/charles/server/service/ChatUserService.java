package com.charles.server.service;

import com.charles.server.entity.vo.Result;

import java.util.List;

public interface ChatUserService {
    
    Result searchUser(String account);
    
    List<Long> offLine(Long userId);
    
    List<Long> hideUser(Long userId);
    
    List<Long> getUserFriend(Long userId);
    
}
