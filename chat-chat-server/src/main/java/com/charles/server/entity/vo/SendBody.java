package com.charles.server.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 发送给服务端的body
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendBody implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String type;
    
    private Object object;
    
}
