package com.charles.server.thread;

import com.charles.server.entity.*;
import com.charles.server.entity.model.*;
import com.charles.server.entity.vo.*;
import com.charles.server.mapper.*;
import com.charles.server.service.*;
import com.charles.server.utils.SpringContextUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSendReceiveThread extends Thread {

    private Socket socket;

    public ServerSendReceiveThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                SendBody body = (SendBody) is.readObject();

                // 接收消息
                if (body.getType().equals("sendSingleMsg")) {
                    SendChatContent content = (SendChatContent) body.getObject();
                    System.out.println(">>>服务端接收到数据：" + content);
                    Integer relation = SpringContextUtils.getBean(UserFriendMapper.class).getRelation(content.getChatContent().getSendId(), content.getChatContent().getReceiveId());
                    if (relation == null || relation == 2) {
                        Socket receiverSocket = (Socket) ServerThread.socketList.get(content.getChatContent().getSendId());
                        ObjectOutputStream os = new ObjectOutputStream(receiverSocket.getOutputStream());
                        os.writeObject(new Result().res(500, "sendSingleMsgFail"));
                    } else {
                        // 存入数据库
                        SpringContextUtils.getBean(ChatContentService.class).addContent(content.getChatContent());
                        Socket receiverSocket = (Socket) ServerThread.socketList.get(content.getChatContent().getReceiveId());
                        if (receiverSocket != null) {
                            Result result = new Result().res(200, "receiveSingleMsg", new ChatBody(content.getSendName(), content.getReceiveName(),
                                    content.getChatContent().getSendId(), content.getChatContent().getReceiveId(), content.getSendAvatar(),
                                    content.getReceiveAvatar(), content.getChatContent().getContent(), content.getChatContent().getSendTime()));

                            ObjectOutputStream os = new ObjectOutputStream(receiverSocket.getOutputStream());
                            os.writeObject(result);
                        }
                    }
                }

                // 接收群聊消息
                if (body.getType().equals("sendGroupMsg")) {
                    SendChatContent content = (SendChatContent) body.getObject();
                    System.out.println(">>>服务端接收到数据：" + content);
                    // 存入数据库
                    Map<String, Object> searchMap = new HashMap<>();
                    searchMap.put("group_id", content.getChatContent().getGroupId());
                    searchMap.put("user_id", content.getChatContent().getSendId());
                    List<GroupMember> members = SpringContextUtils.getBean(GroupMemberMapper.class).selectByMap(searchMap);
                    if (members == null || members.isEmpty()) {
                        Socket receiverSocket = (Socket) ServerThread.socketList.get(content.getChatContent().getSendId());
                        ObjectOutputStream os = new ObjectOutputStream(receiverSocket.getOutputStream());
                        os.writeObject(new Result().res(500, "sendGroupMsgFail"));
                    } else {
                        SpringContextUtils.getBean(ChatContentService.class).addContent(content.getChatContent());
                        ChatGroup group = SpringContextUtils.getBean(ChatGroupMapper.class).selectById(content.getChatContent().getGroupId());
                        ChatUser user = SpringContextUtils.getBean(ChatUserMapper.class).selectById(content.getChatContent().getSendId());
                        // 向群成员发送
                        Map<String, Object> map = new HashMap<>();
                        map.put("group_id", content.getChatContent().getGroupId());
                        List<GroupMember> groupMembers = SpringContextUtils.getBean(GroupMemberMapper.class).selectByMap(map);
                        for (GroupMember groupMember : groupMembers) {
                            if (!groupMember.getUserId().equals(user.getUserId())) {
                                Socket receiverSocket = (Socket) ServerThread.socketList.get(groupMember.getUserId());
                                if (receiverSocket != null) {
                                    Result result = new Result().res(200, "receiveGroupMsg", new ChatBody(user.getUsername(), group.getGroupName(),
                                            user.getUserId(), group.getGroupId(), "chat-chat-client/src/main/resources/static/images/user.png",
                                            null, content.getChatContent().getContent(), content.getChatContent().getSendTime()));

                                    ObjectOutputStream os = new ObjectOutputStream(receiverSocket.getOutputStream());
                                    os.writeObject(result);
                                }
                            }
                        }   
                    }
                }

                // 好友登录通知
                if (body.getType().equals("callFriend")) {
                    CallFriend user = (CallFriend) body.getObject();
                    ChatUser chatUser = SpringContextUtils.getBean(ChatUserMapper.class).selectById(user.getUserId());
                    Socket resSocket = ServerThread.socketList.get(user.getFriend().getUserId());
                    if (resSocket != null) {
                        System.out.println(">>>正在通知其它好友:用户已登录...");
                        Result result = new Result().res(200, "friendOnline", chatUser);
                        ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                        os.writeObject(result);
                    }
                }
                
                // 修改在线状态
                if (body.getType().equals("callFriendOnline")) {
                    CallFriend user = (CallFriend) body.getObject();
                    ChatUser chatUser = SpringContextUtils.getBean(ChatUserMapper.class).selectById(user.getUserId());
                    chatUser.setStatus(1);
                    SpringContextUtils.getBean(ChatUserMapper.class).updateById(chatUser);
                    Socket resSocket = ServerThread.socketList.get(user.getFriend().getUserId());
                    if (resSocket != null) {
                        System.out.println(">>>正在通知其它好友:用户已登录...");
                        Result result = new Result().res(200, "friendOnline", chatUser);
                        ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                        os.writeObject(result);
                    }
                }

                // 获取聊天记录
                if (body.getType().equals("requestSingleHistoryMsg")) {
                    ChatHistoryContent contentId = (ChatHistoryContent) body.getObject();
                    Socket resSocket = ServerThread.socketList.get(contentId.getUserId());
                    Result sendHistoryMsg = new Result().res(200, "getSingleHistoryMsg", SpringContextUtils.getBean(ChatContentService.class).getHistoryContents(contentId));
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(sendHistoryMsg);
                }

                // 获取聊天室的聊天记录
                if (body.getType().equals("requestGroupHistoryMsg")) {
                    ChatHistoryContent contentId = (ChatHistoryContent) body.getObject();
                    Socket resSocket = ServerThread.socketList.get(contentId.getUserId());
                    Result sendHistoryMsg = new Result().res(200, "getGroupHistoryMsg", SpringContextUtils.getBean(ChatContentService.class).getHistoryContents(contentId));
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(sendHistoryMsg);
                }

                // 获取用户信息
                if (body.getType().equals("searchUser")) {
                    SearchUserModel userModel = (SearchUserModel) body.getObject();
                    Socket resSocket = ServerThread.socketList.get(userModel.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(SpringContextUtils.getBean(ChatUserService.class).searchUser(userModel.getAccount()));
                }

                // 添加好友
                if (body.getType().equals("addFriend")) {
                    Result res = null;
                    ApplyFriend result = (ApplyFriend) body.getObject();
                    FriendId friendId = new FriendId();
                    friendId.setAddUser(result.getApplyId());
                    friendId.setBeingAddedUser(result.getBeAppliedId());
                    ApplyFriend applyFriend = SpringContextUtils.getBean(ApplyFriendMapper.class).getAppliFriendById(friendId);
                    if (applyFriend == null) {
                        SpringContextUtils.getBean(ApplyFriendMapper.class).insert(result);
                        res = new Result().res(200, "applyAddFriendSuccess");
                    } else {
                        res = new Result().res(500, "alreadyApplied");
                    }
                    Socket resSocket = ServerThread.socketList.get(result.getApplyId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(res);
                }

                // 申请好友列表
                if (body.getType().equals("applyFriendList")) {
                    Long userId = (Long) body.getObject();
                    Result res = new Result().res(200, "requestFriendList", SpringContextUtils.getBean(ApplyFriendService.class).queryApplyUser(userId));
                    Socket resSocket = ServerThread.socketList.get(userId);
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(res);
                }

                // 响应申请结果
                if (body.getType().equals("responseApplyResult")) {
                    ApplyFriend result = (ApplyFriend) body.getObject();
                    if (result.getStatus().equals("1")) {
                        // 添加成功
                        Socket resSocket = ServerThread.socketList.get(result.getApplyId());
                        if (resSocket != null) {
                            Result res = new Result().res(200, "addFriendSuccess", SpringContextUtils.getBean(ChatUserMapper.class).queryUserById(result.getBeAppliedId()));
                            ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                            os.writeObject(res);
                        }
                        Socket hostSocket = ServerThread.socketList.get(result.getBeAppliedId());
                        Result hostRes = new Result().res(200, "responseAccept", SpringContextUtils.getBean(ChatUserMapper.class).queryUserById(result.getApplyId()));
                        ObjectOutputStream os = new ObjectOutputStream(hostSocket.getOutputStream());
                        os.writeObject(hostRes);
                    }
                    // 存入信息
                    SpringContextUtils.getBean(ApplyFriendMapper.class).updateApplyFriend(result);
                    // 存入好友信息
                    SpringContextUtils.getBean(UserFriendService.class).addUserFriend(result);
                }

                if (body.getType().equals("searchGroup")) {
                    SearchUserModel groupModel = (SearchUserModel) body.getObject();
                    Socket resSocket = ServerThread.socketList.get(groupModel.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(SpringContextUtils.getBean(ChatGroupService.class).searchGroup(Long.parseLong(groupModel.getAccount())));
                }

                if (body.getType().equals("addGroup")) {
                    Result res = null;
                    ApplyGroup applyGroup = (ApplyGroup) body.getObject();
                    ChatGroup group = SpringContextUtils.getBean(ChatGroupMapper.class).selectById(applyGroup.getGroupId());
                    ApplyGroup apply = SpringContextUtils.getBean(ApplyGroupMapper.class).getUserApplyGroup(applyGroup.getGroupId(), applyGroup.getUserId());
                    if (group.getCreatorId().equals(applyGroup.getUserId())) {
                        res = new Result().res(500, "cantApplyOwnGroup");
                    } else if (apply == null) {
                        SpringContextUtils.getBean(ApplyGroupMapper.class).insert(applyGroup);
                        res = new Result().res(200, "groupApplySuccess");
                    } else {
                        res = new Result().res(500, "groupApplyFail");
                    }
                    Socket resSocket = ServerThread.socketList.get(applyGroup.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(res);
                }

                if (body.getType().equals("applyGroupList")) {
                    Long userId = (Long) body.getObject();
                    Result res = new Result().res(200, "responseGroupList", SpringContextUtils.getBean(ApplyGroupService.class).getApplyInfo(userId));
                    Socket resSocket = ServerThread.socketList.get(userId);
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(res);
                }

                if (body.getType().equals("responseGroupApplyResult")) {
                    ApplyGroup applyGroup = (ApplyGroup) body.getObject();
                    ChatGroup group = SpringContextUtils.getBean(ChatGroupMapper.class).selectById(applyGroup.getGroupId());
                    if (applyGroup.getStatus().equals("1")) {
                        // 添加成功
                        GroupMember groupMember = new GroupMember();
                        groupMember.setGroupId(group.getGroupId());
                        groupMember.setUserId(applyGroup.getUserId());
                        groupMember.setJoinTime(new Date());
                        SpringContextUtils.getBean(GroupMemberMapper.class).insert(groupMember);
                        Socket resSocket = ServerThread.socketList.get(applyGroup.getUserId());
                        if (resSocket != null) {
                            Result res = new Result().res(200, "addGroupSuccess", group);
                            ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                            os.writeObject(res);
                        }
                    }
                    // 存入信息
                    SpringContextUtils.getBean(ApplyGroupMapper.class).updateApplyGroup(applyGroup);
                }

                if (body.getType().equals("createGroup")) {
                    ChatGroup group = (ChatGroup) body.getObject();
                    SpringContextUtils.getBean(ChatGroupMapper.class).insert(group);
                    GroupMember groupMember = new GroupMember();
                    groupMember.setUserId(group.getCreatorId());
                    groupMember.setGroupId(group.getGroupId());
                    groupMember.setJoinTime(group.getCreateTime());
                    SpringContextUtils.getBean(GroupMemberMapper.class).insert(groupMember);
                    Socket resSocket = ServerThread.socketList.get(group.getCreatorId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(new Result().res(200, "createGroupSuccess", group));
                }

                // 修改头像
                if (body.getType().equals("uploadAvatar")) {
                    ChatUser user = (ChatUser) body.getObject();
                    SpringContextUtils.getBean(ChatUserMapper.class).updateById(user);
                    Socket resSocket = ServerThread.socketList.get(user.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(new Result().res(200, "uploadAvatarSuccess", user));
                    List<Long> userFriend = SpringContextUtils.getBean(ChatUserService.class).getUserFriend(user.getUserId());
                    if (userFriend != null && !userFriend.isEmpty()) {
                        for (Long uid : userFriend) {
                            Socket friendsSocket = ServerThread.socketList.get(uid);
                            if (friendsSocket != null) {
                                ObjectOutputStream oss = new ObjectOutputStream(friendsSocket.getOutputStream());
                                oss.writeObject(new Result().res(200, "updateFriendData", user));
                            }
                        }
                    }
                }

                // 修改个人信息
                if (body.getType().equals("modifyInfo")) {
                    ChatUser user = (ChatUser) body.getObject();
                    SpringContextUtils.getBean(ChatUserMapper.class).updateById(user);
                    Socket resSocket = ServerThread.socketList.get(user.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(new Result().res(200, "modifyInfoSuccess", user));
                    List<Long> userFriend = SpringContextUtils.getBean(ChatUserService.class).getUserFriend(user.getUserId());
                    if (userFriend != null && !userFriend.isEmpty()) {
                        for (Long uid : userFriend) {
                            Socket friendsSocket = ServerThread.socketList.get(uid);
                            if (friendsSocket != null) {
                                ObjectOutputStream oss = new ObjectOutputStream(friendsSocket.getOutputStream());
                                oss.writeObject(new Result().res(200, "updateFriendData", user));
                            }
                        }
                    }
                }

                // 修改密码
                if (body.getType().equals("modifyPassword")) {
                    ChatUser user = (ChatUser) body.getObject();
                    SpringContextUtils.getBean(ChatUserMapper.class).updateById(user);
                    Socket resSocket = ServerThread.socketList.get(user.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(new Result().res(200, "modifyPasswordSuccess", user));
                }

                if (body.getType().equals("changeRelation")) {
                    ChangeRelation relation = (ChangeRelation) body.getObject();
                    SpringContextUtils.getBean(UserFriendMapper.class).changeRelation(relation);
                    ChatUser user = SpringContextUtils.getBean(ChatUserMapper.class).selectById(relation.getBeChangedUserId());
                    ChangeRelationResponse responseData = new ChangeRelationResponse();
                    responseData.setChangeRelation(relation);
                    responseData.setUser(user);
                    Socket resSocket = ServerThread.socketList.get(relation.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(new Result().res(200, "changeRelationSuccess", responseData));
                }

                if (body.getType().equals("deleteFriend")) {
                    ChangeRelation relation = (ChangeRelation) body.getObject();
                    UserFriend userFriend = new UserFriend();
                    userFriend.setAdd_user(relation.getUserId());
                    userFriend.setBeing_added_user(relation.getBeChangedUserId());
                    SpringContextUtils.getBean(UserFriendMapper.class).deleteFriend(userFriend);
                    SpringContextUtils.getBean(ApplyFriendMapper.class).deleteApply(userFriend);
                    Friend friend = new Friend();
                    ChatUser user = SpringContextUtils.getBean(ChatUserMapper.class).selectById(relation.getBeChangedUserId());
                    friend.setUserId(user.getUserId());
                    friend.setUsername(user.getUsername());
                    friend.setStatus(user.getStatus());
                    friend.setAvatar(user.getAvatar());
                    friend.setIndividualSign(user.getIndividualSign());
                    Socket resSocket = ServerThread.socketList.get(relation.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(new Result().res(200, "deleteFriendSuccess", friend));
                }

                if (body.getType().equals("exitGroup")) {
                    Result res = null;
                    ExitGroupId exitGroupId = (ExitGroupId) body.getObject();
                    ChatGroup group = SpringContextUtils.getBean(ChatGroupMapper.class).selectById(exitGroupId.getGroupId());
                    if (group.getCreatorId().equals(exitGroupId.getUserId())) {
                        res = new Result().res(500, "exitGroupFail");
                    } else {
                        Group g = new Group();
                        g.setGroupId(group.getGroupId());
                        g.setGroupTitle(group.getGroupName());
                        g.setGroupImg("chat-chat-client/src/main/resources/static/images/user.png");
                        res = new Result().res(200, "exitGroupSuccess", g);
                        Map<String, Object> map = new HashMap<>();
                        map.put("group_id", exitGroupId.getGroupId());
                        map.put("user_id", exitGroupId.getUserId());
                        SpringContextUtils.getBean(GroupMemberMapper.class).deleteByMap(map);
                        SpringContextUtils.getBean(ApplyGroupMapper.class).deleteByMap(map);
                    }
                    Socket resSocket = ServerThread.socketList.get(exitGroupId.getUserId());
                    ObjectOutputStream os = new ObjectOutputStream(resSocket.getOutputStream());
                    os.writeObject(res);
                }

                if (body.getType().equals("hidingUser")) {
                    System.out.println(">>>正在通知其它好友:用户已离线(隐身)...");
                    Long userId = (Long) body.getObject();
                    List<Long> friendIdList = SpringContextUtils.getBean(ChatUserService.class).hideUser(userId);
                    ChatUser user = SpringContextUtils.getBean(ChatUserMapper.class).selectById(userId);
                    if (friendIdList != null && !friendIdList.isEmpty()) {
                        for (Long uid : friendIdList) {
                            Socket friendsSocket = ServerThread.socketList.get(uid);
                            if (friendsSocket != null) {
                                ObjectOutputStream os = new ObjectOutputStream(friendsSocket.getOutputStream());
                                os.writeObject(new Result().res(200, "friendOffLine", user));
                            }
                        }
                    }
                }
                
                // 断开连接接收
                if (body.getType().equals("offLine")) {
                    System.out.println(">>>正在通知其它好友:用户已下线...");
                    Long userId = (Long) body.getObject();
                    ChatUser user = SpringContextUtils.getBean(ChatUserMapper.class).selectById(userId);
                    List<Long> friendIdList = SpringContextUtils.getBean(ChatUserService.class).offLine(userId);
                    if (friendIdList != null && !friendIdList.isEmpty()) {
                        for (Long uid : friendIdList) {
                            Socket friendsSocket = ServerThread.socketList.get(uid);
                            if (friendsSocket != null) {
                                ObjectOutputStream os = new ObjectOutputStream(friendsSocket.getOutputStream());
                                os.writeObject(new Result().res(200, "friendOffLine", user));
                            }
                        }
                    }
                    ServerThread.socketList.remove(userId);
                    this.stop();
                    break;
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
