package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.chatchat.utils.ImageLimitUtils;
import com.charles.server.entity.model.Friend;
import com.charles.server.entity.model.SendLoginUserMain;
import com.charles.server.entity.vo.CallFriend;
import com.charles.server.entity.vo.LoginForm;
import com.charles.server.entity.vo.Result;
import com.charles.server.entity.vo.SendBody;
import com.charles.server.exception.LoginException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Charles-H
 * 
 * 登录窗口
 */
public class LoginFrame {
    
    /** 面板 */
    private JPanel contentPanel;
    
    /** 账号 */
    private JTextField accountField;
    
    /** 密码 */
    private JPasswordField passwordField;

    public LoginFrame() {
        JFrame frame = new InitFrame("登录聊天室", 400, 300, true);
        contentPanel = new JPanel();
        contentPanel.setBounds(0, 0, 400, 300);
        loginTemplate(frame, contentPanel);
        contentPanel.setLayout(null);
        frame.add(contentPanel);
        frame.setVisible(true);
    }
    
    public void loginTemplate(JFrame frame, JPanel panel) {
        // 添加logo
        JLabel logo = new JLabel();
        Icon icon = new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/chat-chat-logo.png", 400, 250));
        logo.setIcon(icon);
        logo.setBounds(0, -85, 400, 250);
        panel.add(logo);

        // 账号
        JLabel labelAccount = new JLabel("账号/邮箱");
        labelAccount.setFont(new Font("黑体", Font.BOLD, 18));
        labelAccount.setBounds(35, 90, 100, 25);
        panel.add(labelAccount);

        accountField = new JTextField(20);
        accountField.setBounds(130, 90, 165, 25);
        panel.add(accountField);

        // 密码
        JLabel labelPassword = new JLabel("密码");
        labelPassword.setFont(new Font("黑体", Font.BOLD, 18));
        labelPassword.setBounds(80, 130, 80, 25);
        panel.add(labelPassword);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(130, 130, 165, 25);
        panel.add(passwordField);

        // 登录按钮
        JButton loginBtn = new JButton("登录");
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBounds(150, 170, 100, 40);
        panel.add(loginBtn);
        
        // 注册账号
        JLabel registetBtn = new JLabel("注册账号");
        registetBtn.setFont(new Font("黑体", Font.BOLD, 14));
        registetBtn.setForeground(Color.BLUE);
        registetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registetBtn.setBounds(90, 230, 80, 25);
        panel.add(registetBtn);
        
        // 忘记密码
        JLabel forgetPassword = new JLabel("忘记密码");
        forgetPassword.setFont(new Font("黑体", Font.BOLD, 14));
        forgetPassword.setForeground(Color.BLUE);
        forgetPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgetPassword.setBounds(250, 230, 80, 25);
        panel.add(forgetPassword);

        /** 添加回车事件 */
        class enterKeyHandler implements KeyListener {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getSource() == accountField || e.getSource() == passwordField) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        try {
                            /** 校验账号密码操作 */
                            LoginForm loginForm = new LoginForm(accountField.getText(), String.valueOf(passwordField.getPassword()));
                            SendBody login = new SendBody("login", loginForm);
                            Result result = new Client().loginClient(login);
                            if (result.getCode() == 500) throw new LoginException(result.getMsg());
                            SendLoginUserMain sendLogin = (SendLoginUserMain) result.getObject();
                            // 通知其它好友，用户已登录
                            try {
                                System.out.println("正在通知好友上线...");
                                for (Friend friend : sendLogin.getMainInfo().getFriendList()) {
                                    CallFriend callFriend = new CallFriend(friend, sendLogin.getChatUser().getUserId());
                                    ObjectOutputStream os = null;
                                    os = new ObjectOutputStream(Client.socket.getOutputStream());
                                    os.writeObject(new SendBody("callFriend", callFriend));
                                }
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                            
                            new MainFrame(sendLogin).setVisible(true);
                            frame.dispose();
                        } catch (LoginException exception) {
                            JOptionPane.showMessageDialog(null, exception.getMessage(), "登录失败", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        }

        /** 按钮点击事件 */
        class loginHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    /** 校验账号密码操作 */
                    LoginForm loginForm = new LoginForm(accountField.getText(), String.valueOf(passwordField.getPassword()));
                    SendBody login = new SendBody("login", loginForm);
                    Result result = new Client().loginClient(login);
                    if (result.getCode() == 500) throw new LoginException(result.getMsg());
                    SendLoginUserMain sendLogin = (SendLoginUserMain) result.getObject();
                    // 通知其它好友，用户已登录
                    try {
                        System.out.println("正在通知好友上线...");
                        for (Friend friend : sendLogin.getMainInfo().getFriendList()) {
                            CallFriend callFriend = new CallFriend(friend, sendLogin.getChatUser().getUserId());
                            ObjectOutputStream os = null;
                            os = new ObjectOutputStream(Client.socket.getOutputStream());
                            os.writeObject(new SendBody("callFriend", callFriend));
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    new MainFrame(sendLogin).setVisible(true);
                    frame.dispose();
                } catch (LoginException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "登录失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // 关联事件
        accountField.addKeyListener(new enterKeyHandler());
        passwordField.addKeyListener(new enterKeyHandler());
        loginBtn.addActionListener(new loginHandler());
        
        registetBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame().setVisible(true);
                frame.dispose();
            }
        });

        forgetPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ForgetPwd().setVisible(true);
                frame.dispose();
            }
        });

        forgetPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
