package com.charles.server.service.impl;

import com.charles.server.entity.ApplyFriend;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.UserFriend;
import com.charles.server.mapper.ChatUserMapper;
import com.charles.server.mapper.UserFriendMapper;
import com.charles.server.service.UserFriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserFriendServiceImpl implements UserFriendService {
    
    @Resource
    private UserFriendMapper userFriendMapper;
    
    @Resource
    private ChatUserMapper chatUserMapper;
    
    @Override
    public void addUserFriend(ApplyFriend applyFriend) {
        ChatUser addUser = chatUserMapper.queryUserById(applyFriend.getApplyId());
        ChatUser beAddedUser = chatUserMapper.queryUserById(applyFriend.getBeAppliedId());
        UserFriend userFriend = new UserFriend();
        userFriend.setAdd_user(addUser.getUserId());
        userFriend.setBeing_added_user(beAddedUser.getUserId());
        userFriend.setRelation(0);
        userFriend.setAdd_time(new Date());
        userFriendMapper.insert(userFriend);
    }
}
