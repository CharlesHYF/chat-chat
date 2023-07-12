package com.charles.server.service;

import com.charles.server.entity.vo.Result;

/**
 * @author Charles-H
 * 
 * 用户登录注册 业务层
 */
public interface ChatUserLogService {
    
    /** 登录校验 */
    Result verifyLogin(String account, String password);
    
    /** 注册用户 */
    Result registerUser(String account, String password, String username, String email);
    
}
