package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.chatchat.utils.ImageLimitUtils;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.vo.SendBody;
import com.charles.server.utils.JbcryptUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.UUID;

/**
 * 更多功能窗口
 */
public class MoreFunction extends JFrame {

    /**
     * 修改头像，修改个人信息，修改密码
     */
    public static Container framePane;

    public static ChatUser user;

    public static JPanel modifyAvatarPanel = new JPanel();

    public static JLabel userAvatarLabel = new JLabel();

    public static JPanel modifyInfoPanel = new JPanel();

    public static JPanel modifyPasswordPanel = new JPanel();

    MoreFunction(ChatUser chatUser) {
        user = chatUser;
        this.setTitle("更多功能");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 500) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 400) / 2;
        this.setLocation(x, y);
        this.setResizable(false);
        this.setSize(500, 400);
        framePane = this.getContentPane();
        initContent();
    }

    public void initContent() {
        JPanel operaBlock = new JPanel();
        operaBlock.setLayout(null);
        JButton addGroup = new JButton("更改头像");
        JButton applyOpera = new JButton("更改个人信息");
        JButton createGroup = new JButton("更改密码");
        addGroup.setBounds(0, 0, 166, 30);
        applyOpera.setBounds(166, 0, 166, 30);
        createGroup.setBounds(332, 0, 166, 30);
        operaBlock.add(addGroup);
        operaBlock.add(applyOpera);
        operaBlock.add(createGroup);
        operaBlock.setBounds(0, 0, 500, 30);
        framePane.add(operaBlock);

        CardLayout cardLayout = new CardLayout();
        JPanel cardContent = new JPanel();
        cardContent.setLayout(cardLayout);
        cardContent.setBounds(0, 35, 500, 370);

        addModifyAvatar();

        addModifyInfo();

        addModifyPassword();

        cardContent.add("modifyAvatar", modifyAvatarPanel);
        cardContent.add("modifyInfo", modifyInfoPanel);
        cardContent.add("modifyPassword", modifyPasswordPanel);
        framePane.add(cardContent);

        addGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "modifyAvatar");
                framePane.validate();
                framePane.repaint();
            }
        });

        applyOpera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "modifyInfo");
                framePane.validate();
                framePane.repaint();
            }
        });

        createGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "modifyPassword");
                framePane.validate();
                framePane.repaint();
            }
        });
    }

    public void addModifyAvatar() {
        modifyAvatarPanel.setLayout(null);
        userAvatarLabel.setLayout(null);
        userAvatarLabel.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(user.getAvatar(), 150, 150)));
        userAvatarLabel.setBounds(180, 50, 150, 150);

        JButton modifyBtn = new JButton("上传头像");
        modifyBtn.setBounds(180, 250, 150, 30);

        modifyAvatarPanel.add(userAvatarLabel);
        modifyAvatarPanel.add(modifyBtn);

        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                /** 过滤文件类型 * */
                FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "png", "jpeg");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(modifyBtn);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] arrfiles = chooser.getSelectedFiles();
                    if (arrfiles == null || arrfiles.length == 0) {
                        return;
                    }

                    File ff = chooser.getSelectedFile();
                    String fileName = ff.getName();
                    String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!(prefix.equals("jpg") || prefix.equals("png") || prefix.equals("jpeg"))) {
                        JOptionPane.showMessageDialog(new JDialog(), ":请选择.jpg 或 .png 以及 .jpeg格式的图片");
                        return;
                    }
                    FileInputStream input = null;
                    FileOutputStream out = null;
                    String path = "D:\\chat-chat-avatar";
                    try {
                        for (File f : arrfiles) {
                            File dir = new File(path);
                            // 如果该路径不存在，则创建
                            if (!dir.exists() && !dir.isDirectory()) {
                                dir.mkdir();
                            }
                            
                            input = new FileInputStream(f);
                            byte[] buffer = new byte[1024];
                            File des = new File(path, UUID.randomUUID().toString() + "." + prefix);
                            out = new FileOutputStream(des);
                            int len = 0;
                            while (-1 != (len = input.read(buffer))) {
                                out.write(buffer, 0, len);
                            }
                            out.close();
                            input.close();
                            
                            ChatUser chatUser = user;
                            chatUser.setAvatar(des.getAbsolutePath());
                            try {
                                ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                                os.writeObject(new SendBody("uploadAvatar", chatUser));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "上传失败！", "提示",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void addModifyInfo() {
        modifyInfoPanel.setLayout(null);
        JLabel usernameLabel = new JLabel("用户名(3-10):");
        usernameLabel.setLayout(null);
        JTextField username = new JTextField(user.getUsername());
        username.setBounds(180, 50, 220, 30);
        usernameLabel.setBounds(50, 50, 180, 30);

        JLabel signLabel = new JLabel("个性签名(0-15):");
        signLabel.setLayout(null);
        JTextField sign = new JTextField(user.getIndividualSign());
        sign.setBounds(180, 90, 220, 30);
        signLabel.setBounds(50, 90, 180, 30);

        JLabel genderLabel = new JLabel("性别:");
        genderLabel.setLayout(null);
        JComboBox<String> gender = new JComboBox<>(new String[]{"男", "女", "保密"});
        if (user.getGender() == 0) gender.setSelectedIndex(0);
        else if (user.getGender() == 1) gender.setSelectedIndex(1);
        else gender.setSelectedIndex(2);
        gender.setBounds(180, 130, 220, 30);
        genderLabel.setBounds(50, 130, 180, 30);

        JButton modifyBtn = new JButton("更改");
        modifyBtn.setBounds(200, 170, 80, 30);

        modifyInfoPanel.add(usernameLabel);
        modifyInfoPanel.add(username);
        modifyInfoPanel.add(signLabel);
        modifyInfoPanel.add(sign);
        modifyInfoPanel.add(genderLabel);
        modifyInfoPanel.add(gender);
        modifyInfoPanel.add(modifyBtn);

        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名不能为空", "更改失败", JOptionPane.ERROR_MESSAGE);
                } else if (username.getText().length() < 3 || username.getText().length() > 10 || sign.getText().length() > 15) {
                    JOptionPane.showMessageDialog(null, "请规范输入", "更改失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    ChatUser chatUser = user;
                    chatUser.setUsername(username.getText());
                    chatUser.setIndividualSign(sign.getText());
                    if (gender.getSelectedIndex() == 0) chatUser.setGender(0);
                    else if (gender.getSelectedIndex() == 1) chatUser.setGender(1);
                    else chatUser.setGender(2);

                    try {
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("modifyInfo", chatUser));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    public void addModifyPassword() {
        modifyPasswordPanel.setLayout(null);
        JLabel passwordLabel = new JLabel("请输入新密码(6-10):");
        passwordLabel.setLayout(null);
        JPasswordField password = new JPasswordField();
        password.setBounds(180, 50, 220, 30);
        passwordLabel.setBounds(50, 50, 180, 30);

        JLabel confirmPasswordLabel = new JLabel("请确认密码:");
        confirmPasswordLabel.setLayout(null);
        JPasswordField confirmPassword = new JPasswordField();
        confirmPassword.setBounds(180, 90, 220, 30);
        confirmPasswordLabel.setBounds(50, 90, 180, 30);

        JButton modifyBtn = new JButton("更改");
        modifyBtn.setBounds(200, 130, 80, 30);
        modifyPasswordPanel.add(passwordLabel);
        modifyPasswordPanel.add(password);
        modifyPasswordPanel.add(confirmPasswordLabel);
        modifyPasswordPanel.add(confirmPassword);
        modifyPasswordPanel.add(modifyBtn);
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
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("modifyPassword", user));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

}
