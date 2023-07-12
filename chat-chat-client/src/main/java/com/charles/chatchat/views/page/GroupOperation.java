package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.chatchat.utils.ImageLimitUtils;
import com.charles.server.entity.ApplyGroup;
import com.charles.server.entity.ChatGroup;
import com.charles.server.entity.model.SearchUserModel;
import com.charles.server.entity.vo.ApplyGroupRes;
import com.charles.server.entity.vo.GroupSearchRes;
import com.charles.server.entity.vo.SendBody;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天室管理窗口
 */
public class GroupOperation extends JFrame {
    
    public static JFrame frame;

    public static Container framePane;

    public static Long uid;

    public static JPanel addGroupPanel = new JPanel();

    public static JTextField searchGroup = new JTextField();

    public static JLabel result;

    public static JButton requestAddBtn;

    public static int countAddGroupPanel = 2;

    public static JPanel operaGroupPanel = new JPanel();

    public static JPanel resultPanel = new JPanel();

    public static JScrollPane resScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public static List<ApplyGroupRes> groupSearchResList;

    public static int countData = 0;

    public static Map<ApplyGroupRes, Integer> countMap = new HashMap<>();

    public static JPanel createGroupPanel = new JPanel();

    public static JTextField createGroupNumber = new JTextField();

    public static JTextField createGroupName = new JTextField();


    /**
     * 添加聊天室，管理聊天室，创建聊天室
     */
    GroupOperation(Long userId) {
        frame = this;
        uid = userId;
        this.setTitle("聊天室管理");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 500) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 400) / 2;
        this.setLocation(x, y);
        this.setResizable(false);
        this.setSize(500, 400);
        framePane = this.getContentPane();
        initContent();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                frame.removeAll();
                frame.repaint();
                frame.dispose();
            }
        });
    }

    public void initContent() {
        JPanel operaBlock = new JPanel();
        operaBlock.setLayout(null);
        JButton addGroup = new JButton("添加聊天室");
        JButton applyOpera = new JButton("管理聊天室");
        JButton createGroup = new JButton("创建聊天室");
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

        addGroupContent();

        addOperaContent();

        addCreateContent();

        cardContent.add("addGroup", addGroupPanel);
        cardContent.add("applyOpera", operaGroupPanel);
        cardContent.add("createGroup", createGroupPanel);
        framePane.add(cardContent);

        addGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "addGroup");
                framePane.validate();
                framePane.repaint();
            }
        });

        applyOpera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "applyOpera");
                framePane.validate();
                framePane.repaint();
            }
        });

        createGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardContent, "createGroup");
                framePane.validate();
                framePane.repaint();
            }
        });

    }

    public static void addGroupContent() {
        addGroupPanel.setLayout(null);
        searchGroup.setText("请输入聊天室号码");
        searchGroup.setBounds(100, 35, 220, 30);
        JButton searchBtn = new JButton("搜索");
        searchBtn.setBounds(320, 35, 80, 30);
        JLabel line = new JLabel("--搜索结果---------------------------------------------------");
        line.setBounds(0, 70, 500, 40);
        line.setFont(new Font("黑体", Font.BOLD, 14));
        addGroupPanel.add(searchGroup);
        addGroupPanel.add(searchBtn);
        addGroupPanel.add(line);
        searchGroup.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchGroup.getText().equals("请输入聊天室号码")) {
                    searchGroup.setText("");
                    searchGroup.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchGroup.getText().equals("")) {
                    searchGroup.setText("请输入聊天室号码");
                    searchGroup.setForeground(Color.GRAY);
                }
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchGroup.getText().equals("请输入聊天室号码") || searchGroup.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入搜索的聊天室号码", "聊天室搜索失败", JOptionPane.ERROR_MESSAGE);
                } else if (!isNumber(searchGroup.getText())) {
                    JOptionPane.showMessageDialog(null, "聊天室号码只能是数字", "聊天室搜索失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    SearchUserModel searchUserModel = new SearchUserModel(uid, searchGroup.getText());
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("searchGroup", searchUserModel));
                        System.out.println(">>>正在搜索聊天室信息...");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }

    public static void addSearchResult(GroupSearchRes groupSearchRes) {
        if (countAddGroupPanel > 3) {
            addGroupPanel.remove(result);
            addGroupPanel.remove(requestAddBtn);
            countAddGroupPanel = 3;
        } else {
            countAddGroupPanel = 5;
        }
        result = new JLabel(groupSearchRes.getGroupName());
        result.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(groupSearchRes.getGroupAvatar(), 40, 40)));
        result.setBounds(100, 110, 150, 50);
        result.setFont(new Font("黑体", Font.BOLD, 16));
        requestAddBtn = new JButton("申请添加");
        requestAddBtn.setBounds(250, 120, 100, 30);
        addGroupPanel.add(result);
        addGroupPanel.add(requestAddBtn);
        requestAddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(">>>正在申请添加聊天室...");
                try {
                    ApplyGroup applyGroup = new ApplyGroup();
                    applyGroup.setGroupId(groupSearchRes.getGroupId());
                    applyGroup.setUserId(uid);
                    applyGroup.setStatus("0");
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("addGroup", applyGroup));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
    }

    public static void addOperaContent() {
        operaGroupPanel.setLayout(null);
        JLabel line = new JLabel("--申请管理---------------------------------------------------");
        line.setBounds(0, 40, 500, 40);
        line.setFont(new Font("黑体", Font.BOLD, 14));
        operaGroupPanel.add(line);
        try {
            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
            os.writeObject(new SendBody("applyGroupList", uid));
            System.out.println(">>>正在获取聊天室申请列表...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addApplyResult() {
        if (resultPanel.getComponentCount() > 0) {
            countMap.clear();
            resultPanel.removeAll();
            resScrollPane.remove(resultPanel);
        }
        if (groupSearchResList != null && !groupSearchResList.isEmpty()) {
            resultPanel.setLayout(null);
            resultPanel.setPreferredSize(new Dimension(500, 100 * groupSearchResList.size()));
            for (ApplyGroupRes group : groupSearchResList) {
                JLabel result = new JLabel(group.getUser().getUsername() + "(" + group.getGroupName() + ")");
                result.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(group.getUser().getAvatar(), 60, 60)));
                result.setBounds(50, countData * 90, 300, 60);
                result.setFont(new Font("黑体", Font.BOLD, 17));
                JButton acceptBtn = new JButton("同意");
                JButton refuseBtn = new JButton("拒绝");
                acceptBtn.setBounds(350, countData * 90, 80, 30);
                refuseBtn.setBounds(350, 30 + countData * 90, 80, 30);
                resultPanel.add(result);
                resultPanel.add(acceptBtn);
                resultPanel.add(refuseBtn);
                countMap.put(group, countData);
                acceptBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            System.out.println(">>>正在同意添加...");
                            ApplyGroup applyGroup = new ApplyGroup();
                            applyGroup.setGroupId(group.getGroupId());
                            applyGroup.setUserId(group.getUser().getUserId());
                            applyGroup.setStatus("1");
                            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                            os.writeObject(new SendBody("responseGroupApplyResult", applyGroup));
                            resultPanel.remove(result);
                            resultPanel.remove(acceptBtn);
                            resultPanel.remove(refuseBtn);
                            int count = countMap.get(group);
                            groupSearchResList.remove(count);
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
                            ApplyGroup applyGroup = new ApplyGroup();
                            applyGroup.setGroupId(group.getGroupId());
                            applyGroup.setUserId(group.getUser().getUserId());
                            applyGroup.setStatus("2");
                            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                            os.writeObject(new SendBody("responseGroupApplyResult", applyGroup));
                            resultPanel.remove(result);
                            resultPanel.remove(acceptBtn);
                            resultPanel.remove(refuseBtn);
                            int count = countMap.get(group);
                            groupSearchResList.remove(count);
                            addApplyResult();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                countData++;
            }
            resScrollPane.add(resultPanel);
            resScrollPane.setViewportView(resultPanel);
            resScrollPane.setBounds(0, 90, 500, 330);
            operaGroupPanel.add(resScrollPane);
            countData = 0;
            resScrollPane.repaint();
        } else {
            resScrollPane.removeAll();
            resScrollPane.repaint();
        }
    }

    public static void addCreateContent() {
        createGroupPanel.setLayout(null);
        createGroupNumber.setText("请输入聊天室号码(6-10)");
        createGroupName.setText("请输入聊天室名称");
        createGroupNumber.setBounds(150, 50, 220, 30);
        createGroupName.setBounds(150, 90, 220, 30);
        JButton createBtn = new JButton("创建");
        createBtn.setBounds(200, 130, 80, 30);
        createGroupPanel.add(createGroupNumber);
        createGroupPanel.add(createGroupName);
        createGroupPanel.add(createBtn);
        createGroupNumber.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (createGroupNumber.getText().equals("请输入聊天室号码(6-10)")) {
                    createGroupNumber.setText("");
                    createGroupNumber.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (createGroupNumber.getText().equals("")) {
                    createGroupNumber.setText("请输入聊天室号码(6-10)");
                    createGroupNumber.setForeground(Color.GRAY);
                }
            }
        });
        createGroupName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (createGroupName.getText().equals("请输入聊天室名称")) {
                    createGroupName.setText("");
                    createGroupName.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (createGroupName.getText().equals("")) {
                    createGroupName.setText("请输入聊天室名称");
                    createGroupName.setForeground(Color.GRAY);
                }
            }
        });
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (createGroupNumber.getText().equals("请输入聊天室号码(6-10)") || createGroupNumber.getText().isEmpty() ||
                        createGroupName.getText().equals("请输入聊天室名称") || createGroupName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请完善信息", "聊天室创建失败", JOptionPane.ERROR_MESSAGE);
                } else if (!isNumber(createGroupNumber.getText())) {
                    JOptionPane.showMessageDialog(null, "聊天室号码只能是数字", "聊天室创建失败", JOptionPane.ERROR_MESSAGE);
                } else if (createGroupNumber.getText().length() < 6 || createGroupNumber.getText().length() > 10) {
                    JOptionPane.showMessageDialog(null, "聊天室号码不符合规范", "聊天室创建失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    ChatGroup group = new ChatGroup();
                    group.setGroupId(Long.parseLong(createGroupNumber.getText()));
                    group.setGroupName(createGroupName.getText());
                    group.setCreatorId(uid);
                    group.setCreateTime(new Date());
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("createGroup", group));
                        System.out.println(">>>正在创建聊天室...");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    public static boolean isNumber(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
