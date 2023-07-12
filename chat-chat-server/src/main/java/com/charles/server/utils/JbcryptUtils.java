package com.charles.server.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Charles-H
 * 
 * 密码加密工具类
 */
public class JbcryptUtils {
    
    /** 加密 */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /** 校验 */
    public static boolean verifyPassword(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }
    
}
