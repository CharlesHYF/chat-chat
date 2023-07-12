package com.charles.server;

import com.charles.server.service.ChatUserLogService;
import com.charles.server.thread.ServerStart;
import com.charles.server.utils.SpringContextUtils;

import java.awt.*;

/**
 * @author Charles-H
 * 
 * 服务端启动器
 */
public class ViewStart {
    
    public static void startServer() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SpringContextUtils.getBean(ServerStart.class).setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
}
