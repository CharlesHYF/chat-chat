package com.charles.server.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "add_user")
    private Long addUser;
    
    @TableField(value = "being_added_user")
    private Long beingAddedUser;
    
    private Integer relation;
    
}
