package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.ChatGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charles-H
 * 
 * 群组(聊天室) 数据层
 */
@Mapper
public interface ChatGroupMapper extends BaseMapper<ChatGroup> {
}
