package com.charles.server.service.impl;

import com.charles.server.entity.ChatContent;
import com.charles.server.entity.ChatGroup;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.ChatHistoryContent;
import com.charles.server.entity.vo.ChatBody;
import com.charles.server.entity.vo.ChatHistoryBody;
import com.charles.server.mapper.ChatContentMapper;
import com.charles.server.mapper.ChatGroupMapper;
import com.charles.server.mapper.ChatUserMapper;
import com.charles.server.service.ChatContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatContentServiceImpl implements ChatContentService {
    
    @Autowired
    private ChatContentMapper chatContentMapper;
    
    @Autowired
    private ChatUserMapper chatUserMapper;
    
    @Resource
    private ChatGroupMapper chatGroupMapper;
    
    @Override
    public void addContent(ChatContent content) {
        chatContentMapper.insert(content);
    }

    @Override
    public List<ChatHistoryBody> getHistoryContents(ChatHistoryContent historyContent) {
        List<ChatHistoryBody> chatBodies = new ArrayList<>();
        if (historyContent.getGroupId() == null || historyContent.getGroupId() == 0L) {
            List<ChatContent> contents = chatContentMapper.getContents(historyContent.getUserId(), historyContent.getChatId());
            ChatUser user = chatUserMapper.queryUserById(historyContent.getUserId());
            ChatUser chatUser = chatUserMapper.queryUserById(historyContent.getChatId());
            for (ChatContent content : contents) {
                ChatHistoryBody sendBody = new ChatHistoryBody();
                ChatBody body = new ChatBody();
                if (content.getSendId().equals(user.getUserId())) {
                    body.setSendName(user.getUsername());
                    body.setReceiveName(chatUser.getUsername());
                } else {
                    body.setSendName(chatUser.getUsername());
                    body.setReceiveName(user.getUsername());
                }
                body.setContent(content.getContent());
                body.setSendTime(content.getSendTime());
                sendBody.setBody(body);
                sendBody.setUsername(user.getUsername());
                sendBody.setChatUser(chatUser.getUsername());
                chatBodies.add(sendBody);
            }
        } else {
            List<ChatContent> groupContents = chatContentMapper.getGroupContents(historyContent.getGroupId());
            ChatGroup group = chatGroupMapper.selectById(historyContent.getGroupId());
            for (ChatContent groupContent : groupContents) {
                ChatUser user = chatUserMapper.selectById(groupContent.getSendId());
                ChatHistoryBody sendBody = new ChatHistoryBody();
                ChatBody body = new ChatBody();
                body.setSendName(user.getUsername());
                body.setContent(groupContent.getContent());
                body.setSendTime(groupContent.getSendTime());
                sendBody.setBody(body);
                sendBody.setUsername(group.getGroupName());
                chatBodies.add(sendBody);
            }
        }

        return chatBodies;
    }
}
