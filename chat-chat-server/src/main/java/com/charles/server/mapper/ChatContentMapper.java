package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.ChatContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charles-H
 *
 * 聊天内容 数据层
 */
@Mapper
public interface ChatContentMapper extends BaseMapper<ChatContent> {
    
    /* 获取 1对1的聊天记录 */
    List<ChatContent> getContents(Long userId, Long chatId);
    
    /* 聊天室聊天记录 */
    List<ChatContent> getGroupContents(Long groupId);

}
