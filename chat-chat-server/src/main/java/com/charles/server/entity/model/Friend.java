package com.charles.server.entity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id")
    private Long userId;
    
    private String avatar;
    
    private String username;
    
    private String individualSign;
    
    private Integer status;
    
}
