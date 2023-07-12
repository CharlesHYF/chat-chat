package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.ForgetPwdForm;
import com.charles.server.entity.vo.Result;
import com.charles.server.entity.vo.SendBody;
import com.charles.server.utils.JbcryptUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * 忘记密码
 */
public class ForgetPwd extends JFrame {
    
    public static Container pane; 
    
    public static JFrame frame;
    
    public static ChatUser user;
    
    ForgetPwd() {
        this.setTitle("忘记密码");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 400) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 300) / 2;
        this.setLocation(x, y);
        this.setSize(400, 300);
        this.setResizable(false);
        pane = this.getContentPane();
        frame = this;
        initPanel();
    }
    
    public void initPanel() {
        pane.setLayout(null);
        JLabel searchUser = new JLabel("请输入更改的账号:");
        JTextField account = new JTextField();
        searchUser.setLayout(null);
        searchUser.setBounds(30, 50, 120, 30);
        account.setBounds(150, 50, 180, 30);
        pane.add(searchUser);
        pane.add(account);
        
        JLabel searchEmail = new JLabel("请输入更改的邮箱:");
        JTextField email = new JTextField();
        searchEmail.setLayout(null);
        searchEmail.setBounds(30, 90, 120, 30);
        email.setBounds(150, 90, 180, 30);
        pane.add(searchEmail);
        pane.add(email);
        
        JButton searchBtn = new JButton("搜索账号");
        JButton returnBtn = new JButton("返回登录");
        searchBtn.setBounds(90, 140, 100, 30);
        returnBtn.setBounds(200, 140, 100, 30);
        pane.add(searchBtn);
        pane.add(returnBtn);
        
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (account.getText().isEmpty() || email.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请完善信息", "查找失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    Result res = new Client().searchUser(new SendBody("searchForgetUser", new ForgetPwdForm(account.getText(), email.getText())));
                    if (res.getCode() == 500) {
                        JOptionPane.showMessageDialog(null, "用户不存在", "查找失败", JOptionPane.ERROR_MESSAGE);
                    } else {
                        user = ((ChatUser) res.getObject());
                        addModifyBlock();
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

    public void addModifyBlock() {
        pane.removeAll();
        JLabel passwordLabel = new JLabel("请输入新密码(6-10):");
        JPasswordField password = new JPasswordField();
        passwordLabel.setLayout(null);
        passwordLabel.setBounds(30, 50, 120, 30);
        password.setBounds(150, 50, 180, 30);
        pane.add(passwordLabel);
        pane.add(password);

        JLabel confirmPasswordLabel = new JLabel("请确认密码:");
        JPasswordField confirmPassword = new JPasswordField();
        confirmPasswordLabel.setLayout(null);
        confirmPasswordLabel.setBounds(30, 90, 120, 30);
        confirmPassword.setBounds(150, 90, 180, 30);
        pane.add(confirmPasswordLabel);
        pane.add(confirmPassword);

        JButton modifyBtn = new JButton("修改密码");
        JButton returnBtn = new JButton("返回登录");
        modifyBtn.setBounds(90, 140, 100, 30);
        returnBtn.setBounds(200, 140, 100, 30);
        pane.add(modifyBtn);
        pane.add(returnBtn);

        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new String(password.getPassword()).isEmpty() || new String(confirmPassword.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请完善输入", "更改失败", JOptionPane.ERROR_MESSAGE);
                } else if (password.getPassword().length > 10 || password.getPassword().length < 6) {
                    JOptionPane.showMessageDialog(null, "请规范输入", "更改失败", JOptionPane.ERROR_MESSAGE);
                } else if (!Arrays.equals(confirmPassword.getPassword(), password.getPassword())) {
                    JOptionPane.showMessageDialog(null, "请确认密码", "更改失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setPassword(JbcryptUtils.hashPassword(new String(password.getPassword())));
                    Result res = new Client().searchUser(new SendBody("modifyPassword", user));
                    if (res.getCode() == 200) {
                        JOptionPane.showMessageDialog(null, "更改成功,请登录", "更改成功", JOptionPane.INFORMATION_MESSAGE);
                        new LoginFrame();
                        frame.dispose();
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
        
        pane.repaint();
    }
    
}
