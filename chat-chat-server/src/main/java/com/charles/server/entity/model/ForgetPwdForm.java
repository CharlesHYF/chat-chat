package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPwdForm implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String account;
    
    private String email;
    
}
