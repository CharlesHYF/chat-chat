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
 * 群成员表(聊天室成员)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember implements Serializable {

    private static final Long serialVersionUID = 1L;

    /** id标识 */
    @TableId()
    private Long mid;
    
    /** 聊天室id */
    private Long groupId;
    
    /** 成员id */
    private Long userId;
    
    /** 加入时间 */
    private Date joinTime;

}
