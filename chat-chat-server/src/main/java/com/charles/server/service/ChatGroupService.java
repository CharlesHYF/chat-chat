package com.charles.server.service;

import com.charles.server.entity.vo.Result;

public interface ChatGroupService {

    /**
     * 查询聊天室
     */
    Result searchGroup(Long groupId);

}
