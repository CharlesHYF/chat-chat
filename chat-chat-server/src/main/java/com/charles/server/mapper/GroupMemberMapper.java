package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charles-H
 * 
 * 群成员 数据层
 */
@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
}
