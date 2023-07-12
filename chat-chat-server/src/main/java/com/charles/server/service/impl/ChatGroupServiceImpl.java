package com.charles.server.service.impl;

import com.charles.server.entity.ChatGroup;
import com.charles.server.entity.vo.Result;
import com.charles.server.mapper.ChatGroupMapper;
import com.charles.server.service.ChatGroupService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {
    
    @Resource
    private ChatGroupMapper chatGroupMapper;
    
    @Override
    public Result searchGroup(Long groupId) {
        ChatGroup group = chatGroupMapper.selectById(groupId);
        if (group == null) {
            return new Result().res(500, "groupNotFound");
        }
        return new Result().res(200, "groupFound", group);
    }
}
