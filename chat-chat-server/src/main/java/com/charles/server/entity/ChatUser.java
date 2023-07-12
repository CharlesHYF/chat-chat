package com.charles.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Charles-H
 * 
 * 用户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser implements Serializable {
    
    private static final Long serialVersionUID = 1L;
    
    /** 用户id */
    @TableId(value = "user_id")
    private Long userId;
    
    /** 账号 */
    private String account;
    
    /** 密码 */
    private String password;
    
    /** 用户名 */
    private String username;
    
    /** 性别 */
    private Integer gender;
    
    /** 电子邮箱 */
    private String email;
    
    /** 头像 */
    private String avatar;
    
    /** 个性签名 */
    private String individualSign;
    
    /** 状态 */
    private Integer status;
    
    /** 登录时间 */
    private Date loginTime;
    
}
