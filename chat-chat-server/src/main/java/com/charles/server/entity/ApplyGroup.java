package com.charles.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyGroup implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "group_id")
    private Long groupId;
    
    @TableField(value = "user_id")
    private Long userId;
    
    private String status;
    
}
