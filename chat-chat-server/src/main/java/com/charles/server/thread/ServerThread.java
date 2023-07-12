package com.charles.server.thread;

import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.ForgetPwdForm;
import com.charles.server.entity.model.SendLoginUserMain;
import com.charles.server.entity.vo.LoginForm;
import com.charles.server.entity.vo.RegisterForm;
import com.charles.server.entity.vo.Result;
import com.charles.server.entity.vo.SendBody;
import com.charles.server.mapper.ChatUserMapper;
import com.charles.server.service.ChatUserLogService;
import com.charles.server.utils.SpringContextUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles-H
 * 
 * 服务端线程
 */
public class ServerThread {
    
    public static Map<Long,Socket> socketList = new HashMap<>();

    public ServerThread(Socket socket) {
        ObjectInputStream is = null;
        ObjectOutputStream os = null;
        Result res = null;

        try {
            is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            os = new ObjectOutputStream(socket.getOutputStream());

            SendBody body = (SendBody) is.readObject();
            if (body.getType().equals("login")) {
                res = verifyLogin((LoginForm) body.getObject());
                if (res.getCode() == 200) {
                    SendLoginUserMain user = (SendLoginUserMain) res.getObject();
                    socketList.put(user.getChatUser().getUserId(), socket);
                    // 开启接收转发线程
                    new ServerSendReceiveThread(socket).start();
                }
            } else if(body.getType().equals("register")) {
                res = register((RegisterForm) body.getObject());
            } else if(body.getType().equals("searchForgetUser")) {
                res = searchForgetUser((ForgetPwdForm) body.getObject());
            } else {
                res = modifyPassword((ChatUser) body.getObject());
            }
            os.writeObject(res);
            os.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 登录
    public Result verifyLogin(LoginForm loginForm) {
        System.out.println(">>>客户端进行登录....");
        // 对登录的信息进行校验(因为我们的服务端是静态方法，所以需要用到bean注入)
        return SpringContextUtils.getBean(ChatUserLogService.class).verifyLogin(loginForm.getAccount(), loginForm.getPassword());
    }
    
    // 注册
    public Result register(RegisterForm registerForm) {
        System.out.println(">>>客户端进行注册....");
        return SpringContextUtils.getBean(ChatUserLogService.class).registerUser(registerForm.getAccount(), registerForm.getPassword(), registerForm.getUsername(), registerForm.getEmail());
    }
    
    public Result searchForgetUser(ForgetPwdForm forgetPwdForm) {
        System.out.println(">>>客户端进行查找用户....");
        Map<String, Object> map = new HashMap<>();
        map.put("account", forgetPwdForm.getAccount());
        map.put("email", forgetPwdForm.getEmail());
        List<ChatUser> users = SpringContextUtils.getBean(ChatUserMapper.class).selectByMap(map);
        if (users == null || users.isEmpty()) return new Result().res(500);
        else return new Result().res(200, users.get(0));
    }

    public Result modifyPassword(ChatUser user) {
        System.out.println(">>>客户端进行修改密码....");
        SpringContextUtils.getBean(ChatUserMapper.class).updateById(user);
        return new Result().res(200);
    }
    
}
