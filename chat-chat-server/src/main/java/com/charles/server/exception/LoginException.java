package com.charles.server.exception;

/**
 * @author Charles-H
 * 
 * 登录异常处理
 */
public class LoginException extends RuntimeException {

    public LoginException(String message) {
        super(message);
    }
    
}
