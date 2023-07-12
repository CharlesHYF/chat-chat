package com.charles.server.service.impl;

import com.charles.server.entity.ChatContent;
import com.charles.server.entity.ChatGroup;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.GroupMember;
import com.charles.server.entity.model.*;
import com.charles.server.entity.vo.FriendId;
import com.charles.server.entity.vo.Result;
import com.charles.server.mapper.*;
import com.charles.server.service.ChatUserLogService;
import com.charles.server.utils.JbcryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Charles-H
 *
 * 用户登录注册 业务实现
 */
@Service
public class ChatUserLogServiceImpl implements ChatUserLogService {
    
    @Autowired
    private ChatUserMapper chatUserMapper;
    
    @Autowired
    private UserFriendMapper userFriendMapper;
    
    @Autowired
    private ChatContentMapper chatContentMapper;
    
    @Autowired
    private ChatGroupMapper chatGroupMapper;
    
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    
    @Override
    public Result verifyLogin(String account, String password) {
        if (account.isEmpty() || password.isEmpty()) {
            return new Result().res(500, "账号或密码为空");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        List<ChatUser> user = chatUserMapper.selectByMap(map);

        if (user.isEmpty()) {
            map.put("email", account);
            map.keySet().removeIf(key -> key.equals("account"));
            user = chatUserMapper.selectByMap(map);
            if (user.isEmpty()) {
                return new Result().res(500, "账号不存在");
            }
        }

        if (!JbcryptUtils.verifyPassword(password, user.get(0).getPassword())) {
            return new Result().res(500, "密码错误");
        }

        ChatUser chatUser = user.get(0);
        chatUser.setStatus(1);
        chatUser.setLoginTime(new Date());
        chatUserMapper.updateById(chatUser);
        MainInfo mainInfo = new MainInfo();
        Map<String, History> historyMap = new HashMap<>();
        List<Friend> friendList = new ArrayList<>();
        List<Friend> strangerList = new ArrayList<>();
        List<Friend> blackList = new ArrayList<>();
        List<Group> groupList = new ArrayList<>();
        
        List<ChatContent> chatContents = chatContentMapper.selectList(null);
        chatContents.sort(Comparator.comparing(ChatContent::getSendTime));
        for (ChatContent chatContent : chatContents) {
            if (chatContent.getGroupId() == 0) {
                if (chatContent.getReceiveId().equals(chatUser.getUserId())) {
                    History history = new History();
                    ChatUser contentUser = chatUserMapper.queryUserById(chatContent.getSendId());
                    history.setUserId(contentUser.getUserId());
                    history.setUsername(contentUser.getUsername());
                    history.setAvatar(contentUser.getAvatar());
                    history.setContent(chatContent.getContent());
                    history.setLastTime(chatContent.getSendTime());
                    history.setIsGroup(false);

                    if (historyMap.get("user:" + contentUser.getUserId()) != null) {
                        historyMap.replace("user:" + contentUser.getUserId(), history);
                    } else historyMap.put("user:" + contentUser.getUserId(), history);

                } else if(chatContent.getSendId().equals(chatUser.getUserId())) {
                    History history = new History();
                    ChatUser contentUser = chatUserMapper.queryUserById(chatContent.getReceiveId());
                    history.setUserId(contentUser.getUserId());
                    history.setUsername(contentUser.getUsername());
                    history.setAvatar(contentUser.getAvatar());
                    history.setContent(chatContent.getContent());
                    history.setLastTime(chatContent.getSendTime());
                    history.setIsGroup(false);

                    if (historyMap.get("user:" + contentUser.getUserId()) != null) {
                        historyMap.replace("user:" + contentUser.getUserId(), history);
                    } else historyMap.put("user:" + contentUser.getUserId(), history);
                }
            } else {
                ChatGroup group = chatGroupMapper.selectById(chatContent.getGroupId());
                Map<String, Object> memberMap = new HashMap<>();
                memberMap.put("group_id", group.getGroupId());
                for (GroupMember groupMember : groupMemberMapper.selectByMap(memberMap)) {
                    if (groupMember.getUserId().equals(chatUser.getUserId())) {
                        History history = new History();
                        history.setUserId(chatContent.getGroupId());
                        history.setUsername(group.getGroupName());
                        history.setAvatar("chat-chat-client/src/main/resources/static/images/user.png");
                        history.setContent(chatContent.getContent());
                        history.setLastTime(chatContent.getSendTime());
                        history.setIsGroup(true);

                        if (historyMap.get("group:" + chatContent.getGroupId()) != null) {
                            historyMap.replace("group:" + chatContent.getGroupId(), history);
                        } else historyMap.put("group:" + chatContent.getGroupId(), history);
                    }
                }
            }
        }

        for (FriendId id : userFriendMapper.queryFriendIdList(chatUser.getUserId())) {
            if (id.getAddUser().equals(chatUser.getUserId())) {
                if (id.getRelation() == 0) {
                    friendList.add(chatUserMapper.queryFriend(id.getBeingAddedUser()));
                } else if (id.getRelation() == 1) {
                    strangerList.add(chatUserMapper.queryFriend(id.getBeingAddedUser()));
                } else {
                    blackList.add(chatUserMapper.queryFriend(id.getBeingAddedUser()));
                }
            } else{
                if (id.getRelation() == 0) {
                    friendList.add(chatUserMapper.queryFriend(id.getAddUser()));
                } else if (id.getRelation() == 1) {
                    strangerList.add(chatUserMapper.queryFriend(id.getAddUser()));
                } else {
                    blackList.add(chatUserMapper.queryFriend(id.getAddUser()));
                }
            }
        }
        
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("user_id", chatUser.getUserId());
        List<GroupMember> groupMembers = groupMemberMapper.selectByMap(selectMap);
        for (GroupMember member : groupMembers) {
            ChatGroup chatGroup = chatGroupMapper.selectById(member.getGroupId());
            groupList.add(new Group(chatGroup.getGroupId(),"chat-chat-client/src/main/resources/static/images/user.png" ,chatGroup.getGroupName()));
        }
        
        friendList = friendList.stream().sorted(Comparator.comparing(Friend::getStatus).reversed()).collect(Collectors.toList());
        strangerList = strangerList.stream().sorted(Comparator.comparing(Friend::getStatus).reversed()).collect(Collectors.toList());
        blackList = blackList.stream().sorted(Comparator.comparing(Friend::getStatus).reversed()).collect(Collectors.toList());

        List<History> historyList = historyMap.values().stream()
                .sorted(Comparator.comparing(History::getLastTime).reversed()).collect(Collectors.toList());
        
        mainInfo.setHistoryList(historyList);
        mainInfo.setFriendList(friendList);
        mainInfo.setStrangerList(strangerList);
        mainInfo.setBlackList(blackList);
        mainInfo.setGroupList(groupList);
        
        return new Result().res(200, "登陆成功", new SendLoginUserMain(chatUser, mainInfo));
    }

    @Override
    public Result registerUser(String account, String password, String username, String email) {
        
        if (account.isEmpty() || password.isEmpty() || username.isEmpty() || email.isEmpty()) {
            return new Result().res(500, "请完善信息");
        }
        
        if (account.length() < 10 || account.length() > 20 || Pattern.matches("[0-9].*", account)) {
            return new Result().res(500, "账号不符合规则");
        }
        
        if (password.length() < 6 || password.length() > 10) {
            return new Result().res(500, "密码不符合规则");
        }
        
        if (username.length() < 2 || username.length() > 10) {
            return new Result().res(500, "用户名不符合规则");
        }
        
        if (!Pattern.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", email)) {
            return new Result().res(500, "邮箱不符合规则");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        List<ChatUser> users = chatUserMapper.selectByMap(map);
        if (!users.isEmpty()) {
            return new Result().res(500, "账号已存在");
        } else {
            map.put("email", email);
            map.keySet().removeIf(key -> key.equals("account"));
            users = chatUserMapper.selectByMap(map);
            if (!users.isEmpty()) {
                return new Result().res(500, "邮箱已存在");
            }
        }
        
        ChatUser user = new ChatUser();
        user.setAccount(account);
        user.setPassword(JbcryptUtils.hashPassword(password));
        user.setUsername(username);
        user.setEmail(email);
        user.setAvatar("chat-chat-client/src/main/resources/static/images/user.png");
        user.setStatus(0);
        chatUserMapper.insert(user);
        return new Result().res(200, "注册成功");
    }
    
}
