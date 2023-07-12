package com.charles.server.thread;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Charles-H
 * 
 * 服务端启动类
 */
@Component("ServerStart")
public class ServerStart extends JFrame {
    
    ServerSocket serverSocket;
    
    Socket socket;

    ServerStart() {
        this.setTitle("服务器");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 300) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 250) / 2;
        this.setLocation(x, y);
        this.setSize(175, 100);
        this.setResizable(false);
        JButton startBtn = new JButton("启动服务");
        JButton stopBtn = new JButton("关闭服务");
        this.add(startBtn, BorderLayout.WEST);
        this.add(stopBtn, BorderLayout.EAST);
        
        startBtn.addActionListener((e) -> {
            try {
                serverSocket = new ServerSocket(9001);
                System.out.println(">>>chat-chat服务端启动成功，等待连接...");
                System.out.println(">>>端口号:" + serverSocket.getLocalPort());
                
                while (true) {
                    socket = serverSocket.accept();
                    System.out.println("客户端：" + socket.getInetAddress() + ":" + socket.getPort() + "上线\n");
                    new ServerThread(socket);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
    
}
