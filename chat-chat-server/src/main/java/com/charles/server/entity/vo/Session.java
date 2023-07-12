package com.charles.server.entity.vo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles-H
 * 
 * 自定义session
 */
@Component
public class Session {
    
    private static Map<String, Object> session = new HashMap<>();

    public static Map<String, Object> getSession(){
        return session;
    }
    
}
