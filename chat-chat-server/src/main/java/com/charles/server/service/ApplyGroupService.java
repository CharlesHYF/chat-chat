package com.charles.server.service;

import com.charles.server.entity.vo.ApplyGroupRes;

import java.util.List;

public interface ApplyGroupService {
    
    List<ApplyGroupRes> getApplyInfo(Long userId);
    
}
