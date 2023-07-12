package com.charles.chatchat.thread;

import com.charles.chatchat.utils.ImageLimitUtils;
import com.charles.chatchat.views.page.*;
import com.charles.server.entity.ChatGroup;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.Friend;
import com.charles.server.entity.model.Group;
import com.charles.server.entity.model.History;
import com.charles.server.entity.vo.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

/**
 * @author Charles-H
 * 
 * 聊天接收线程
 */
public class ClientReceiveMsg extends Thread {
    
    private Socket socket;

    public ClientReceiveMsg(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream is;
        try {
            while(true){
                is = new ObjectInputStream(socket.getInputStream());
                Result result = (Result) is.readObject();
                
                if (result.getCode() == 500 && result.getMsg().equals("sendSingleMsgFail")) {
                    JOptionPane.showMessageDialog(null, "您已拉黑或删除对方(对方已拉黑或删除您)", "发送失败", JOptionPane.ERROR_MESSAGE);
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("receiveSingleMsg")) {
                    ChatBody content = (ChatBody) result.getObject();
                    PrivateChatFrame chatFrame = MainFrame.chatList.get(content.getReceiveName() + "to" + content.getSendName());
                    if (chatFrame != null)  chatFrame.appendText(content);
                    
                    // 添加到最近消息
                    Integer index = MainFrame.historyNode.get("user:" + content.getSendId());
                    if (index != null) {
                        History history = MainFrame.mainInfo.getHistoryList().get(index);
                        history.setContent(content.getContent());
                        history.setLastTime(content.getSendTime());
                        MainFrame.mainInfo.getHistoryList().set(index, history);
                        MainFrame.refreshHistory();
                    } else {
                        History history = new History();
                        history.setUserId(content.getSendId());
                        history.setAvatar(content.getSendAvatar());
                        history.setUsername(content.getSendName());
                        history.setContent(content.getContent());
                        history.setLastTime(content.getSendTime());
                        history.setIsGroup(false);
                        MainFrame.mainInfo.getHistoryList().add(history);
                        MainFrame.refreshHistory();
                    }
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("receiveGroupMsg")) {
                    ChatBody content = (ChatBody) result.getObject();
                    PrivateChatFrame chatFrame = MainFrame.chatList.get("group:" + content.getReceiveName());
                    if (chatFrame != null)  chatFrame.appendText(content);

                    // 添加到最近消息
                    Integer index = MainFrame.historyNode.get("group:" + content.getReceivedId());
                    if (index != null) {
                        History history = MainFrame.mainInfo.getHistoryList().get(index);
                        history.setContent(content.getContent());
                        history.setLastTime(content.getSendTime());
                        MainFrame.mainInfo.getHistoryList().set(index, history);
                        MainFrame.refreshHistory();
                    } else {
                        History history = new History();
                        history.setUserId(content.getReceivedId());
                        history.setAvatar(content.getSendAvatar());
                        history.setUsername(content.getReceiveName());
                        history.setContent(content.getContent());
                        history.setLastTime(content.getSendTime());
                        history.setIsGroup(true);
                        MainFrame.mainInfo.getHistoryList().add(history);
                        MainFrame.refreshHistory();
                    }
                }

                if (result.getCode() == 200 && result.getMsg().equals("friendOnline")) {
                    ChatUser user = (ChatUser) result.getObject();
                    System.out.println("你的好友：" + user.getUsername() + "已上线");
                    // 修改好友状态
                    Integer index = MainFrame.friendNodeMap.get(user.getUserId());
                    MainFrame.mainInfo.getFriendList().set(index, new Friend(user.getUserId(), user.getAvatar(), user.getUsername(), user.getIndividualSign(), user.getStatus()));
                    MainFrame.refreshFriendList();
                }

                if (result.getCode() == 200 && result.getMsg().equals("getSingleHistoryMsg")) {
                    List<ChatHistoryBody> contentList = (List<ChatHistoryBody>) result.getObject();
                    if (contentList != null) {
                        for (ChatHistoryBody content : contentList) {
                            PrivateChatFrame chatFrame = MainFrame.chatList.get(content.getUsername() + "to" + content.getChatUser());
                            if (chatFrame != null)  chatFrame.appendText(content.getBody());
                        }
                    }
                }

                if (result.getCode() == 200 && result.getMsg().equals("getGroupHistoryMsg")) {
                    List<ChatHistoryBody> contentList = (List<ChatHistoryBody>) result.getObject();
                    if (contentList != null) {
                        for (ChatHistoryBody content : contentList) {
                            PrivateChatFrame chatFrame = MainFrame.chatList.get("group:" + content.getUsername());
                            if (chatFrame != null)  chatFrame.appendText(content.getBody());
                        }
                    }
                }
                
                if (result.getCode() == 500 && result.getMsg().equals("userNotFound")) {
                    JOptionPane.showMessageDialog(null, "未找到该用户", "用户搜索失败", JOptionPane.ERROR_MESSAGE);
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("userFound")) {
                    FriendOperation.addSearchResult(((ChatUser) result.getObject()));
                    FriendOperation.framePane.repaint();
                }

                if (result.getCode() == 200 && result.getMsg().equals("applyAddFriendSuccess")) {
                    JOptionPane.showMessageDialog(null, "申请添加成功", "添加用户成功", JOptionPane.INFORMATION_MESSAGE);
                }

                if (result.getCode() == 500 && result.getMsg().equals("alreadyApplied")) {
                    JOptionPane.showMessageDialog(null, "您已添加或已申请添加该用户", "添加用户失败", JOptionPane.ERROR_MESSAGE);
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("requestFriendList")) {
                    System.out.println(">>>已成功获取到申请列表...");
                    List<ChatUser> userList = (List<ChatUser>) result.getObject();
                    FriendOperation.userList = userList;
                    FriendOperation.addApplyResult();
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("addFriendSuccess")) {
                    System.out.println(">>>申请添加好友已通过...");
                    System.out.println(">>>正在更新好友列表...");
                    ChatUser user = (ChatUser) result.getObject();
                    MainFrame.mainInfo.getFriendList().add(new Friend(user.getUserId(), user.getAvatar(), user.getUsername(), user.getIndividualSign(), user.getStatus()));
                    MainFrame.friendNodeMap.put(user.getUserId(), MainFrame.friendNodeMap.size());
                    MainFrame.refreshFriendList();
                }

                if (result.getCode() == 200 && result.getMsg().equals("responseAccept")) {
                    System.out.println(">>>正在更新好友列表...");
                    ChatUser user = (ChatUser) result.getObject();
                    MainFrame.mainInfo.getFriendList().add(new Friend(user.getUserId(), user.getAvatar(), user.getUsername(), user.getIndividualSign(), user.getStatus()));
                    MainFrame.friendNodeMap.put(user.getUserId(), MainFrame.friendNodeMap.size());
                    MainFrame.refreshFriendList();
                }

                if (result.getCode() == 200 && result.getMsg().equals("addGroupSuccess")) {
                    System.out.println(">>>申请添加聊天室已通过...");
                    System.out.println(">>>正在更新聊天室列表...");
                    ChatGroup group = (ChatGroup) result.getObject();
                    MainFrame.mainInfo.getGroupList().add(new Group(group.getGroupId(), "chat-chat-client/src/main/resources/static/images/user.png", group.getGroupName()));
                    MainFrame.refreshGroup();
                }

                if (result.getCode() == 200 && result.getMsg().equals("groupFound")) {
                    ChatGroup group = (ChatGroup) result.getObject();
                    GroupSearchRes groupSearchRes = new GroupSearchRes();
                    groupSearchRes.setGroupId(group.getGroupId());
                    groupSearchRes.setGroupName(group.getGroupName());
                    groupSearchRes.setGroupAvatar("chat-chat-client/src/main/resources/static/images/user.png");
                    GroupOperation.addSearchResult(groupSearchRes);
                    GroupOperation.framePane.repaint();
                }
                
                if (result.getCode() == 500 && result.getMsg().equals("groupNotFound")) {
                    JOptionPane.showMessageDialog(null, "该聊天室不存在", "聊天室搜索失败", JOptionPane.ERROR_MESSAGE);
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("groupApplySuccess")) {
                    JOptionPane.showMessageDialog(null, "申请添加成功", "添加聊天室成功", JOptionPane.INFORMATION_MESSAGE);
                }

                if (result.getCode() == 500 && result.getMsg().equals("cantApplyOwnGroup")) {
                    JOptionPane.showMessageDialog(null, "您不能申请添加自己创建的聊天室", "添加聊天室失败", JOptionPane.ERROR_MESSAGE);
                }
                
                if (result.getCode() == 500 && result.getMsg().equals("groupApplyFail")) {
                    JOptionPane.showMessageDialog(null, "您已添加或已申请添加该聊天室", "添加聊天室失败", JOptionPane.ERROR_MESSAGE);
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("responseGroupList")) {
                    System.out.println(">>>已成功获取到申请列表...");
                    List<ApplyGroupRes> resList = (List<ApplyGroupRes>) result.getObject();
                    GroupOperation.groupSearchResList = resList;
                    GroupOperation.addApplyResult();
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("createGroupSuccess")) {
                    JOptionPane.showMessageDialog(null, "创建成功", "创建聊天室成功", JOptionPane.INFORMATION_MESSAGE);
                    ChatGroup group = (ChatGroup) result.getObject();
                    MainFrame.mainInfo.getGroupList().add(new Group(group.getGroupId(), "chat-chat-client/src/main/resources/static/images/user.png", group.getGroupName()));
                    MainFrame.refreshGroup();
                }

                if (result.getCode() == 200 && result.getMsg().equals("uploadAvatarSuccess")) {
                    JOptionPane.showMessageDialog(null, "头像更改成功", "更改成功", JOptionPane.INFORMATION_MESSAGE);
                    ChatUser user = (ChatUser) result.getObject();
                    MoreFunction.userAvatarLabel.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(user.getAvatar(), 150, 150)));
                    MoreFunction.framePane.repaint();
                    MainFrame.userAvatar.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(user.getAvatar(), 60, 60)));
                    MainFrame.user = user;
                    MainFrame.contentPane.repaint();
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("modifyInfoSuccess")) {
                    JOptionPane.showMessageDialog(null, "个人信息更改成功", "更改成功", JOptionPane.INFORMATION_MESSAGE);
                    ChatUser user = (ChatUser) result.getObject();
                    MainFrame.usernameLabel.setText(user.getUsername());
                    MainFrame.introductionLabel.setText(user.getIndividualSign());
                    MainFrame.user = user;
                    MainFrame.contentPane.repaint();
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("modifyPasswordSuccess")) {
                    JOptionPane.showMessageDialog(null, "密码更改成功", "更改成功", JOptionPane.INFORMATION_MESSAGE);
                }

                if (result.getCode() == 200 && result.getMsg().equals("changeRelationSuccess")) {
                    JOptionPane.showMessageDialog(null, "迁移成功", "迁移成功", JOptionPane.INFORMATION_MESSAGE);
                    ChangeRelationResponse response = (ChangeRelationResponse) result.getObject();

                    ChatUser user = response.getUser();
                    Integer friendIndex = MainFrame.friendNodeMap.get(user.getUserId());
                    if (friendIndex != null) {
                        MainFrame.friendNodeMap.remove(user.getUserId());
                        MainFrame.mainInfo.getFriendList().remove(friendIndex.intValue());
                    }

                    Integer strangerIndex = MainFrame.strangerNodeMap.get(user.getUserId());
                    if (strangerIndex != null) {
                        MainFrame.strangerNodeMap.remove(user.getUserId());
                        MainFrame.mainInfo.getStrangerList().remove(strangerIndex.intValue());
                    }

                    Integer blackIndex = MainFrame.blackNodeMap.get(user.getUserId());
                    if (blackIndex != null) {
                        MainFrame.blackNodeMap.remove(user.getUserId());
                        MainFrame.mainInfo.getBlackList().remove(blackIndex.intValue());
                    }

                    Integer relation = response.getChangeRelation().getRelation();
                    Friend friend = new Friend();
                    friend.setUserId(user.getUserId());
                    friend.setStatus(user.getStatus());
                    friend.setUsername(user.getUsername());
                    friend.setAvatar(user.getAvatar());
                    friend.setIndividualSign(user.getIndividualSign());
                    if (relation == 0) {
                        MainFrame.mainInfo.getFriendList().add(friend);
                        MainFrame.friendNodeMap.put(user.getUserId(), MainFrame.friendNodeMap.size());
                        MainFrame.refreshFriendList();
                    } else if (relation == 1) {
                        MainFrame.mainInfo.getStrangerList().add(friend);
                        MainFrame.strangerNodeMap.put(user.getUserId(), MainFrame.strangerNodeMap.size());
                        MainFrame.refreshFriendList();
                    } else {
                        MainFrame.mainInfo.getBlackList().add(friend);
                        MainFrame.blackNodeMap.put(user.getUserId(), MainFrame.blackNodeMap.size());
                        MainFrame.refreshFriendList();
                    }
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("deleteFriendSuccess")) {
                    JOptionPane.showMessageDialog(null, "删除用户成功", "删除成功", JOptionPane.INFORMATION_MESSAGE);
                    Friend user = (Friend) result.getObject();

                    Integer friendIndex = MainFrame.friendNodeMap.get(user.getUserId());
                    if (friendIndex != null) {
                        MainFrame.friendNodeMap.remove(user.getUserId());
                        MainFrame.mainInfo.getFriendList().remove(friendIndex.intValue());
                        MainFrame.refreshFriendList();
                    }
                    
                    Integer strangerIndex = MainFrame.strangerNodeMap.get(user.getUserId());
                    if (strangerIndex != null) {
                        MainFrame.strangerNodeMap.remove(user.getUserId());
                        MainFrame.mainInfo.getStrangerList().remove(strangerIndex.intValue());
                        MainFrame.refreshFriendList();
                    }

                    Integer blackIndex = MainFrame.blackNodeMap.get(user.getUserId());
                    if (blackIndex != null) {
                        MainFrame.blackNodeMap.remove(user.getUserId());
                        MainFrame.mainInfo.getBlackList().remove(blackIndex.intValue());
                        MainFrame.refreshFriendList();
                    }
                    
                }

                if (result.getCode() == 500 && result.getMsg().equals("exitGroupFail")) {
                    JOptionPane.showMessageDialog(null, "您是聊天室创建人，不能删除", "退出失败", JOptionPane.ERROR_MESSAGE);
                }

                if (result.getCode() == 500 && result.getMsg().equals("sendGroupMsgFail")) {
                    JOptionPane.showMessageDialog(null, "发送失败，您已不是聊天室的一员", "发送失败", JOptionPane.ERROR_MESSAGE);
                }
                        
                if (result.getCode() == 200 && result.getMsg().equals("exitGroupSuccess")) {
                    Group group = (Group) result.getObject();
                    JOptionPane.showMessageDialog(null, "退出成功!", "退出成功", JOptionPane.INFORMATION_MESSAGE);
                    MainFrame.mainInfo.getGroupList().remove(group);
                    MainFrame.refreshGroup();
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("updateFriendData")) {
                    ChatUser user = (ChatUser) result.getObject();
                    Integer index = MainFrame.friendNodeMap.get(user.getUserId());
                    MainFrame.mainInfo.getFriendList().set(index, new Friend(user.getUserId(), user.getAvatar(), user.getUsername(), user.getIndividualSign(), user.getStatus()));
                    MainFrame.refreshFriendList();
                    Integer historyIndex = MainFrame.historyNode.get("user:" + user.getUserId());
                    if (historyIndex != null) {
                        History history = MainFrame.mainInfo.getHistoryList().get(historyIndex);
                        history.setUsername(user.getUsername());
                        history.setAvatar(user.getAvatar());
                        MainFrame.mainInfo.getHistoryList().set(index, history);
                        MainFrame.refreshHistory();
                    }
                }
                
                if (result.getCode() == 200 && result.getMsg().equals("friendOffLine")) {
                    ChatUser user = (ChatUser) result.getObject();
                    System.out.println("你的好友：" + user.getUsername() + "已下线");
                    // 修改好友状态
                    Integer index = MainFrame.friendNodeMap.get(user.getUserId());
                    MainFrame.mainInfo.getFriendList().set(index, new Friend(user.getUserId(), user.getAvatar(), user.getUsername(), user.getIndividualSign(), user.getStatus()));
                    MainFrame.refreshFriendList();
                }
                
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
