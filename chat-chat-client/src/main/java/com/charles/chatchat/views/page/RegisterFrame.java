package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.server.entity.vo.RegisterForm;
import com.charles.server.entity.vo.Result;
import com.charles.server.entity.vo.SendBody;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * @author Charles-H
 * 
 * 注册窗口
 */
public class RegisterFrame extends JFrame {
    
    public static Container pane;
    
    public static JFrame frame;
    
    public RegisterFrame() {
        frame = this;
        this.setTitle("账号注册");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 400) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 300) / 2;
        this.setLocation(x, y);
        this.setSize(400, 300);
        this.setResizable(false);
        pane = this.getContentPane();
        initPanel();
    }
    
    public void initPanel() {
        pane.setLayout(null);
        JLabel accountLabel = new JLabel("账号(小写字母开头,10-20):");
        JTextField account = new JTextField();
        accountLabel.setLayout(null);
        accountLabel.setBounds(20, 50, 150, 30);
        account.setBounds(170, 50, 180, 30);
        pane.add(accountLabel);
        pane.add(account);

        JLabel passwordLabel = new JLabel("请输入密码(6-10):");
        JPasswordField password = new JPasswordField();
        passwordLabel.setLayout(null);
        passwordLabel.setBounds(20, 90, 150, 30);
        password.setBounds(170, 90, 180, 30);
        pane.add(passwordLabel);
        pane.add(password);
        
        JLabel usernameLabel = new JLabel("请输入用户名(2-10):");
        JTextField username = new JTextField();
        usernameLabel.setLayout(null);
        usernameLabel.setBounds(20, 130, 150, 30);
        username.setBounds(170, 130, 180, 30);
        pane.add(usernameLabel);
        pane.add(username);
        
        JLabel emailLabel = new JLabel("请输入邮箱地址:");
        JTextField email = new JTextField();
        emailLabel.setLayout(null);
        emailLabel.setBounds(20, 170, 150, 30);
        email.setBounds(170, 170, 180, 30);
        pane.add(emailLabel);
        pane.add(email);
        
        JButton registerBtn = new JButton("注册账号");
        JButton returnBtn = new JButton("返回登录");
        registerBtn.setBounds(90, 220, 100, 30);
        returnBtn.setBounds(200, 220, 100, 30);
        pane.add(registerBtn);
        pane.add(returnBtn);

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (account.getText().isEmpty() || Arrays.toString(password.getPassword()).isEmpty() || username.getText().isEmpty() || email.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请完善信息", "注册失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    RegisterForm registerForm = new RegisterForm(account.getText(), new String(password.getPassword()), username.getText(), email.getText());
                    Result res = new Client().register(new SendBody("register", registerForm));
                    if (res.getCode() != 200) {
                        JOptionPane.showMessageDialog(null, res.getMsg(), "注册失败", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "注册成功！请登录", "注册成功", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        new LoginFrame();
                    }
                }
            }
        });
        
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();
                frame.dispose();
            }
        });
    }
}
