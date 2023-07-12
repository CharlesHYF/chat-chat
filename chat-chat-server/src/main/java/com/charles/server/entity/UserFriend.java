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
 * 好友表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFriend implements Serializable {
    
    private static final Long serialVersionUID = 1L;
    
    /** id标识 */
    @TableId()
    private Long relation_id;
    
    /** 添加的用户id */
    private Long add_user;
    
    /** 被添加的用户id */
    private Long being_added_user;
    
    /** 关系: 0:好友, 1:陌生人, 2:黑名单 */
    private Integer relation;
    
    /** 添加时间 */
    private Date add_time;
    
}
