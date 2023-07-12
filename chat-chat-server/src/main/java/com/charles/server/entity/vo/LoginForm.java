package com.charles.server.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 登录表单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 登录的账号或邮箱
     */
    private String account;

    /**
     * 密码
     */
    private String password;

}
