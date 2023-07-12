package com.charles.server.entity.vo;

import java.io.Serializable;

/**
 * @author Charles-H
 * 
 * 服务器响应客户端
 */
public class Result implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer code;
    
    private String msg;
    
    private Object object;

    public Result() {
    }
    
    public Result(Integer code, String msg, Object object) {
        this.code = code;
        this.msg = msg;
        this.object = object;
    }

    public Result res(Integer code) {
        return new Result(code, null, null);
    }

    public Result res(Integer code, Object object) {
        return new Result(code, null, object);
    }

    public Result res(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    public Result res(Integer code, String msg, Object object) {
        return new Result(code, msg, object);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", object=" + object +
                '}';
    }
}
