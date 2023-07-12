package com.charles.server.service.impl;

import com.charles.server.entity.ApplyFriend;
import com.charles.server.entity.ChatUser;
import com.charles.server.mapper.ApplyFriendMapper;
import com.charles.server.mapper.ChatUserMapper;
import com.charles.server.service.ApplyFriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplyFriendServiceImpl implements ApplyFriendService {
    
    @Resource
    private ChatUserMapper chatUserMapper;
    
    @Resource
    private ApplyFriendMapper applyFriendMapper;
    
    @Override
    public List<ChatUser> queryApplyUser(Long userId) {
        List<ChatUser> userList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("be_applied_id", userId);

        for (ApplyFriend applyFriend : applyFriendMapper.selectByMap(map)) {
            if (applyFriend.getStatus().equals("0")) {
                userList.add(chatUserMapper.queryUserById(applyFriend.getApplyId()));
            }
        }

        return userList;
    }
}
