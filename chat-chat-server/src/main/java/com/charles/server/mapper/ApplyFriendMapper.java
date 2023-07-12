package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.ApplyFriend;
import com.charles.server.entity.UserFriend;
import com.charles.server.entity.vo.FriendId;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplyFriendMapper extends BaseMapper<ApplyFriend> {
    
    ApplyFriend getAppliFriendById(FriendId friendId);
    
    void updateApplyFriend(ApplyFriend applyFriend);
    
    void deleteApply(UserFriend userFriend);
    
}
