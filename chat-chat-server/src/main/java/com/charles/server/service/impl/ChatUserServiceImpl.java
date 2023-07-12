package com.charles.server.service.impl;

import com.charles.server.entity.ChatUser;
import com.charles.server.entity.vo.FriendId;
import com.charles.server.entity.vo.Result;
import com.charles.server.mapper.ChatUserMapper;
import com.charles.server.mapper.UserFriendMapper;
import com.charles.server.service.ChatUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatUserServiceImpl implements ChatUserService {

    @Resource
    private ChatUserMapper chatUserMapper;
    
    @Resource
    private UserFriendMapper userFriendMapper;

    @Override
    public Result searchUser(String account) {

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        List<ChatUser> user = chatUserMapper.selectByMap(map);
        if (user.isEmpty()) {
            return new Result().res(500, "userNotFound");
        } else {
            return new Result().res(200, "userFound", user.get(0));
        }
    }

    @Override
    public List<Long> offLine(Long userId) {
        // 修改登录状态
        ChatUser chatUser = chatUserMapper.queryUserById(userId);
        chatUser.setStatus(0);
        chatUserMapper.updateById(chatUser);
        // 通知其它好友，已下线
        List<Long> friendList = new ArrayList<>();

        for (FriendId id : userFriendMapper.queryFriendIdList(userId)) {
            if (id.getAddUser().equals(userId)) {
                if (id.getRelation() == 0) {
                    friendList.add(id.getBeingAddedUser());
                }
            } else{
                if (id.getRelation() == 0) {
                    friendList.add(id.getAddUser());
                }
            }
        }
        return friendList;
    }

    @Override
    public List<Long> hideUser(Long userId) {
        // 修改登录状态
        ChatUser chatUser = chatUserMapper.queryUserById(userId);
        chatUser.setStatus(2);
        chatUserMapper.updateById(chatUser);
        // 通知其它好友，已下线(隐身)
        List<Long> friendList = new ArrayList<>();

        for (FriendId id : userFriendMapper.queryFriendIdList(userId)) {
            if (id.getAddUser().equals(userId)) {
                if (id.getRelation() == 0) {
                    friendList.add(id.getBeingAddedUser());
                }
            } else{
                if (id.getRelation() == 0) {
                    friendList.add(id.getAddUser());
                }
            }
        }
        return friendList;
    }

    @Override
    public List<Long> getUserFriend(Long userId) {
        List<Long> friendList = new ArrayList<>();

        for (FriendId id : userFriendMapper.queryFriendIdList(userId)) {
            if (id.getAddUser().equals(userId)) {
                if (id.getRelation() == 0) {
                    friendList.add(id.getBeingAddedUser());
                }
            } else{
                if (id.getRelation() == 0) {
                    friendList.add(id.getAddUser());
                }
            }
        }
        return friendList;
    }
}
