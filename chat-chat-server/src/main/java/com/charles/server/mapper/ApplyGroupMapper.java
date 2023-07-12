package com.charles.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.server.entity.ApplyGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplyGroupMapper extends BaseMapper<ApplyGroup> {
    
    List<ApplyGroup> getApplyGroup(Long groupId);

    ApplyGroup getUserApplyGroup(Long groupId, Long userId);
    
    void updateApplyGroup(ApplyGroup group);
    
}
