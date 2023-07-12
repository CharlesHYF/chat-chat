package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    
    private String account;
    
}
