package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRelation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    
    private Long beChangedUserId;
    
    private Integer relation;
    
}
