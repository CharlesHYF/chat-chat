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
public class ApplyFriend implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "apply_id")
    private Long applyId;

    @TableField(value = "be_applied_id")
    private Long beAppliedId;
    
    private String status;
    
}
