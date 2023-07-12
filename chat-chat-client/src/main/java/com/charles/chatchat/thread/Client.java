package com.charles.chatchat.thread;

import com.charles.server.entity.vo.Result;
import com.charles.server.entity.vo.SendBody;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Charles-H
 *
 * 客户端
 */
public class Client {

    public static Socket socket;

    public Client() {
        try {
            socket = new Socket("127.0.0.1", 9001);
            System.out.println(">>>连接服务器...");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 登录请求
    public Result loginClient(SendBody loginForm) {
        Result result = null;
        ObjectInputStream is = null;
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(loginForm);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            is = new ObjectInputStream(socket.getInputStream());
            result = (Result) is.readObject();
            // 登录成功，开启线程接收
            if (result.getCode() == 200) {
                new ClientReceiveMsg(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    // 注册
    public Result register(SendBody body) {
        Result result = null;
        ObjectInputStream is = null;
        ObjectOutputStream os = null;

        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(body);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            is = new ObjectInputStream(socket.getInputStream());
            result = (Result) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 接收完毕后，断开连接
        try {
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    // 查找用户
    public Result searchUser(SendBody body) {
        Result result = null;
        ObjectInputStream is = null;
        ObjectOutputStream os = null;

        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(body);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            is = new ObjectInputStream(socket.getInputStream());
            result = (Result) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 接收完毕后，断开连接
        try {
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    // 修改密码
    public Result modifyPwd(SendBody body) {
        Result result = null;
        ObjectInputStream is = null;
        ObjectOutputStream os = null;

        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(body);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            is = new ObjectInputStream(socket.getInputStream());
            result = (Result) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 接收完毕后，断开连接
        try {
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }
    
}
