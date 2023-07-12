package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.UserFriend;
import com.charles.server.entity.model.ChangeRelation;
import com.charles.server.entity.vo.FriendId;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charles-H
 * 
 * 好友 数据层
 */
@Mapper
public interface UserFriendMapper extends BaseMapper<UserFriend> {
    
    // 查询用户好友id
    List<FriendId> queryFriendIdList(Long userId);
    
    // 查询好友关系
    Integer getRelation(Long hostId, Long userId);
    
    void changeRelation(ChangeRelation changeRelation);
    
    void deleteFriend(UserFriend userFriend);
    
}
