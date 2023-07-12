package com.charles.server.service;

import com.charles.server.entity.ChatContent;
import com.charles.server.entity.model.ChatHistoryContent;
import com.charles.server.entity.vo.ChatBody;
import com.charles.server.entity.vo.ChatHistoryBody;

import java.util.List;

/**
 * @author Charles-H
 * 
 * 聊天内容 业务层
 */
public interface ChatContentService {
    
    /** 添加数据 */
    void addContent(ChatContent content);
    
    /** 获取聊天记录 */
    List<ChatHistoryBody> getHistoryContents(ChatHistoryContent historyContent);
    
}
