package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.chatchat.utils.ImageLimitUtils;
import com.charles.server.entity.ApplyFriend;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.SearchUserModel;
import com.charles.server.entity.vo.SendBody;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles-H
 * 
 * 好友管理
 */
public class FriendOperation extends JFrame {
    
    public static JFrame frame;

    public static Container framePane;

    public static JPanel addFriendPanel = new JPanel();

    public static JPanel operaFriendPanel = new JPanel();

    public static JScrollPane jScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public static JPanel panel = new JPanel();

    public static JTextField searchUser = new JTextField();

    public static JLabel result;

    public static JButton requestAddBtn;

    public static int countAddFriendPanel = 2;

    public static Long uid;

    public static List<ChatUser> userList;

    public static int countData = 0;

    public static Map<ChatUser, Integer> countMap = new HashMap<>();

    FriendOperation(Long userId) {
        frame = this;
        uid = userId;
        this.setTitle("好友管理");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 500) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 400) / 2;
        this.setLocation(x, y);
        this.setResizable(false);
        this.setSize(500, 400);
        framePane = this.getContentPane();
        initContent(framePane, userId);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (countAddFriendPanel > 3) {
                    addFriendPanel.remove(result);
                    addFriendPanel.remove(requestAddBtn);
                    countAddFriendPanel = 3;
                }
                if (panel.getComponentCount() > 0) {
                    countMap.clear();
                    panel.removeAll();
                    jScrollPane.remove(panel);
                }
                frame.dispose();
            }
        });
    }

    public void initContent(Container pane, Long userId) {
        JPanel operaBlock = new JPanel();
        operaBlock.setLayout(null);
        JButton addFriend = new JButton("添加好友");
        JButton applyOpera = new JButton("管理申请");
        addFriend.setBounds(0, 0, 250, 30);
        applyOpera.setBounds(250, 0, 250, 30);
        operaBlock.add(addFriend);
        operaBlock.add(applyOpera);
        operaBlock.setBounds(0, 0, 500, 30);
        pane.add(operaBlock);

        CardLayout cardLayout = new CardLayout();
        JPanel cardContent = new JPanel();
        cardContent.setLayout(cardLayout);
        cardContent.setBounds(0, 35, 500, 370);

        // 添加好友区块
        addAddUserPanel(userId);

        // 申请管理
        addApplyPanel();

        cardContent.add("addFriend", addFriendPanel);
        cardContent.add("friendOpera", operaFriendPanel);
        pane.add(cardContent);

        addFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "addFriend");
                pane.validate();
                pane.repaint();
            }
        });

        applyOpera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "friendOpera");
                pane.validate();
                pane.repaint();
            }
        });
    }

    // 添加好友面板
    public static void addAddUserPanel(Long userId) {
        addFriendPanel.setLayout(null);
        searchUser.setText("请输入用户账号");
        searchUser.setBounds(100, 35, 220, 30);
        JButton searchBtn = new JButton("搜索");
        searchBtn.setBounds(320, 35, 80, 30);
        JLabel line = new JLabel("--搜索结果---------------------------------------------------");
        line.setBounds(0, 70, 500, 40);
        line.setFont(new Font("黑体", Font.BOLD, 14));
        addFriendPanel.add(searchUser);
        addFriendPanel.add(searchBtn);
        addFriendPanel.add(line);
        searchUser.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchUser.getText().equals("请输入用户账号")) {
                    searchUser.setText("");
                    searchUser.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchUser.getText().equals("")) {
                    searchUser.setText("请输入用户账号");
                    searchUser.setForeground(Color.GRAY);
                }
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchUser.getText().equals("请输入用户账号") || searchUser.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入搜索的用户账号", "用户搜索失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    SearchUserModel searchUserModel = new SearchUserModel(userId, searchUser.getText());
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("searchUser", searchUserModel));
                        System.out.println(">>>正在搜索用户信息...");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // 搜索结果
    public static void addSearchResult(ChatUser user) {
        if (countAddFriendPanel > 3) {
            addFriendPanel.remove(result);
            addFriendPanel.remove(requestAddBtn);
            countAddFriendPanel = 3;
        } else {
            countAddFriendPanel = 5;
        }
        result = new JLabel(user.getUsername());
        result.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(user.getAvatar(), 40, 40)));
        result.setBounds(100, 110, 150, 50);
        result.setFont(new Font("黑体", Font.BOLD, 16));
        requestAddBtn = new JButton("申请添加");
        requestAddBtn.setBounds(250, 120, 100, 30);
        addFriendPanel.add(result);
        addFriendPanel.add(requestAddBtn);
        requestAddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(">>>正在申请添加好友...");
                if (user.getUserId().equals(uid)) {
                    JOptionPane.showMessageDialog(null, "您不能添加自己", "添加失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        ApplyFriend applyFriend = new ApplyFriend();
                        applyFriend.setApplyId(uid);
                        applyFriend.setBeAppliedId(user.getUserId());
                        applyFriend.setStatus("0");
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("addFriend", applyFriend));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // 管理申请面板
    public static void addApplyPanel() {
        operaFriendPanel.setLayout(null);
        JLabel line = new JLabel("--申请管理---------------------------------------------------");
        line.setBounds(0, 40, 500, 40);
        line.setFont(new Font("黑体", Font.BOLD, 14));
        operaFriendPanel.add(line);
        try {
            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
            os.writeObject(new SendBody("applyFriendList", uid));
            System.out.println(">>>正在获取申请列表...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 管理申请内容
    public static void addApplyResult() {
        if (panel.getComponentCount() > 0) {
            countMap.clear();
            panel.removeAll();
            jScrollPane.remove(panel);
        }
        if (userList != null && !userList.isEmpty()) {
            panel.setLayout(null);
            panel.setPreferredSize(new Dimension(500, 100 * userList.size()));
            for (ChatUser user : userList) {
                JLabel result = new JLabel(user.getUsername());
                result.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(user.getAvatar(), 60, 60)));
                result.setBounds(100, countData * 90, 180, 60);
                result.setFont(new Font("黑体", Font.BOLD, 17));
                JButton acceptBtn = new JButton("同意");
                JButton refuseBtn = new JButton("忽略");
                acceptBtn.setBounds(280, countData * 90, 80, 30);
                refuseBtn.setBounds(280, 30 + countData * 90, 80, 30);
                panel.add(result);
                panel.add(acceptBtn);
                panel.add(refuseBtn);
                countMap.put(user, countData);
                acceptBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            System.out.println(">>>正在同意添加...");
                            ApplyFriend applyFriend = new ApplyFriend();
                            applyFriend.setApplyId(user.getUserId());
                            applyFriend.setBeAppliedId(uid);
                            applyFriend.setStatus("1");
                            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                            os.writeObject(new SendBody("responseApplyResult", applyFriend));
                            panel.remove(result);
                            panel.remove(acceptBtn);
                            panel.remove(refuseBtn);
                            int count = countMap.get(user);
                            userList.remove(count);
                            addApplyResult();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                refuseBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            System.out.println(">>>正在拒绝添加...");
                            ApplyFriend applyFriend = new ApplyFriend();
                            applyFriend.setApplyId(user.getUserId());
                            applyFriend.setBeAppliedId(uid);
                            applyFriend.setStatus("2");
                            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                            os.writeObject(new SendBody("responseApplyResult", applyFriend));
                            panel.remove(result);
                            panel.remove(acceptBtn);
                            panel.remove(refuseBtn);
                            int count = countMap.get(user);
                            userList.remove(count);
                            addApplyResult();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                countData++;
            }
            jScrollPane.add(panel);
            jScrollPane.setViewportView(panel);
            jScrollPane.setBounds(0, 90, 500, 330);
            operaFriendPanel.add(jScrollPane);
            countData = 0;
            jScrollPane.repaint();
        } else {
            jScrollPane.removeAll();
            jScrollPane.repaint();
        }
    }

}
