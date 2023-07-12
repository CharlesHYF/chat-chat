package com.charles.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Charles-H
 * 
 * 群组表(聊天室)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroup implements Serializable {

    private static final Long serialVersionUID = 1L;

    /** 聊天室id */
    @TableId(value = "group_id")
    private Long groupId;
    
    /** 创建者id */
    @TableField("creator_id")
    private Long creatorId;
    
    /** 聊天室名称 */
    private String groupName;
    
    /** 创建时间 */
    private Date createTime;
    
}
