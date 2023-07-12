package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.Friend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charles-H
 * 
 * 用户表 数据层
 */
@Mapper
public interface ChatUserMapper extends BaseMapper<ChatUser> {
    
    // 查询用户的好友数据
    Friend queryFriend(Long userId);
    
    // 查询用户数据
    ChatUser queryUserById(Long userId);
    
}
