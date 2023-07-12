package com.charles.server.service.impl;

import com.charles.server.entity.ApplyGroup;
import com.charles.server.entity.ChatGroup;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.vo.ApplyGroupRes;
import com.charles.server.mapper.ApplyGroupMapper;
import com.charles.server.mapper.ChatGroupMapper;
import com.charles.server.mapper.ChatUserMapper;
import com.charles.server.service.ApplyGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplyGroupServiceImpl implements ApplyGroupService {

    @Resource
    private ApplyGroupMapper applyGroupMapper;

    @Resource
    private ChatGroupMapper chatGroupMapper;

    @Resource
    private ChatUserMapper chatUserMapper;

    @Override
    public List<ApplyGroupRes> getApplyInfo(Long userId) {
        List<ApplyGroupRes> res = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("creator_id", userId);
        List<ChatGroup> groupList = chatGroupMapper.selectByMap(map);
        if (groupList != null && !groupList.isEmpty()) {
            for (ChatGroup group : groupList) {
                List<ApplyGroup> applyGroupList = applyGroupMapper.getApplyGroup(group.getGroupId());
                if (applyGroupList != null && !applyGroupList.isEmpty()) {
                    for (ApplyGroup applyGroup : applyGroupList) {
                        if (applyGroup.getStatus().equals("0")) {
                            ApplyGroupRes applyGroupRes = new ApplyGroupRes();
                            ChatUser user = chatUserMapper.selectById(applyGroup.getUserId());
                            applyGroupRes.setUser(user);
                            applyGroupRes.setGroupId(group.getGroupId());
                            applyGroupRes.setGroupName(group.getGroupName());
                            res.add(applyGroupRes);
                        }
                    }
                }
            }
        }
        return res;
    }
}
