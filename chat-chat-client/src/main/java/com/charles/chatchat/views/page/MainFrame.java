package com.charles.chatchat.views.page;

import com.charles.chatchat.model.ChatContentNode;
import com.charles.chatchat.model.ChatContentNodeRenderer;
import com.charles.chatchat.model.UserNode;
import com.charles.chatchat.model.UserNodeRenderer;
import com.charles.chatchat.thread.Client;
import com.charles.chatchat.utils.ImageLimitUtils;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.*;
import com.charles.server.entity.vo.CallFriend;
import com.charles.server.entity.vo.SendBody;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles-H
 * 
 * 主框架
 */
public class MainFrame extends JFrame {

    public static JLabel userAvatar = new JLabel();

    public static JLabel status = new JLabel();
    
    public static JLabel usernameLabel = new JLabel();

    public static JLabel introductionLabel = new JLabel();

    public static final Map<String, PrivateChatFrame> chatList = new HashMap<>();

    // 最近消息
    public static JTree historyTree;

    public static JScrollPane jScrollHistory = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public static ChatContentNode historyRoot;

    public static final Map<String, Integer> historyNode = new HashMap<>();

    // 好友列表
    public static JTree friendTree;

    public static JScrollPane jScrollFriend = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public static UserNode friendTreeRoot;

    public static UserNode friendNode;

    public static final Map<Long, Integer> friendNodeMap = new HashMap<>();

    public static UserNode strangerNode;

    public static final Map<Long, Integer> strangerNodeMap = new HashMap<>();

    public static UserNode blackListNode;

    public static final Map<Long, Integer> blackNodeMap = new HashMap<>();

    static JPopupMenu friendPop = new JPopupMenu();

    static JPopupMenu strangerPop = new JPopupMenu();

    static JPopupMenu blackListPop = new JPopupMenu();

    static JPopupMenu groupPop = new JPopupMenu();

    // 聊天室列表
    public static JTree groupTree;

    public static JScrollPane jScrollGroup = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public static ChatContentNode groupRoot;

    public static ChatUser user;

    public static MainInfo mainInfo;

    public static Container contentPane;

    public static Long friendSelectId;

    public static Long strangerSelectId;

    public static Long blackListSelectId;

    public static Long groupSelectId;

    /**
     * 创建主窗口
     */
    public MainFrame(SendLoginUserMain sendLoginUserMain) {
        user = sendLoginUserMain.getChatUser();
        mainInfo = sendLoginUserMain.getMainInfo();

        this.setTitle("chat-chat");
        this.setSize(330, 900);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 500);
        int y = (int) (toolkit.getScreenSize().getHeight() - 1000);
        this.setLocation(x, y);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        contentPane = this.getContentPane();
        contentPane.setSize(330, 875);
        body(contentPane);
        bottom(contentPane);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("offLine", user.getUserId()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void body(Container contentPane) {
        /* 第一部分 */
        /** 用户头像 */
        userAvatar.setBounds(10, 20, 60, 60);
        userAvatar.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon(user.getAvatar(), 60, 60)));
        contentPane.add(userAvatar);

        /** 用户名 */
        usernameLabel.setText(user.getUsername());
        usernameLabel.setBounds(90, 16, 240, 30);
        usernameLabel.setFont(new Font("黑体", Font.BOLD, 17));
        contentPane.add(usernameLabel);

        /** 个性签名 */
        introductionLabel.setText(user.getIndividualSign());
        introductionLabel.setBounds(90, 35, 240, 30);
        introductionLabel.setFont(new Font("黑体", Font.BOLD, 14));
        contentPane.add(introductionLabel);

        /** 状态: 0离线，1在线，2隐身 */
        status.setText("在线");
        status.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/online.png", 15, 15)));
        status.setForeground(Color.green);
        if (user.getStatus() == 0) {
            status = new JLabel("离线");
            status.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/offLine.png", 15, 15)));
            status.setForeground(Color.GRAY);
        } else if (user.getStatus() == 2) {
            status = new JLabel("隐身");
            status.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/hiding.png", 15, 15)));
            status.setForeground(Color.GRAY);
        }
        status.setBounds(90, 58, 80, 30);
        status.setFont(new Font("黑体", Font.BOLD, 14));
        contentPane.add(status);

        JComboBox<String> userStatus = new JComboBox<>(new String[]{"在线", "隐身"});
        if (user.getStatus() == 1) userStatus.setSelectedIndex(0);
        else userStatus.setSelectedIndex(1);
        userStatus.setBounds(200, 58, 100, 30);
        contentPane.add(userStatus);
        
        userStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userStatus.getSelectedIndex() == 1) {
                    user.setStatus(2);
                    status.setText("隐身");
                    status.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/hiding.png", 15, 15)));
                    status.setForeground(Color.GRAY);
                    contentPane.repaint();
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                        os.writeObject(new SendBody("hidingUser", user.getUserId()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    user.setStatus(1);
                    status.setText("在线");
                    status.setIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/online.png", 15, 15)));
                    status.setForeground(Color.green);
                    contentPane.repaint();
                    try {
                        for (Friend friend : mainInfo.getFriendList()) {
                            CallFriend callFriend = new CallFriend(friend, user.getUserId());
                            ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                            os.writeObject(new SendBody("callFriendOnline", callFriend));
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        /** 三个按钮 */
        JButton history = new JButton("最近消息");
        history.setCursor(new Cursor(Cursor.HAND_CURSOR));
        history.setFont(new Font("黑体", Font.BOLD, 15));
        history.setBounds(0, 100, 110, 35);
        contentPane.add(history);

        JButton friend = new JButton("好友");
        friend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        friend.setFont(new Font("黑体", Font.BOLD, 15));
        friend.setBounds(110, 100, 110, 35);
        contentPane.add(friend);

        JButton group = new JButton("聊天室");
        group.setCursor(new Cursor(Cursor.HAND_CURSOR));
        group.setFont(new Font("黑体", Font.BOLD, 15));
        group.setBounds(220, 100, 110, 35);
        contentPane.add(group);

        /* 第二部分 */
        CardLayout cardLayout = new CardLayout();
        JPanel card = new JPanel();
        card.setLayout(cardLayout);
        card.setBounds(0, 135, 330, 700);

        /** 最近消息 */
        refreshHistory();

        /** 好友 */
        addFriendPopMenu();
        addStrangerPopMenu();
        addBlackPopMenu();
        refreshFriendList();

        /** 聊天室 */
        addGroupPopMenu();
        refreshGroup();

        // 添加
        card.add(jScrollHistory, "history");
        card.add(jScrollFriend, "friend");
        card.add(jScrollGroup, "group");

        // 监听器
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(card, "history");
                contentPane.validate();
                contentPane.repaint();
            }
        });

        friend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(card, "friend");
                contentPane.validate();
                contentPane.repaint();
            }
        });

        group.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(card, "group");
                contentPane.validate();
                contentPane.repaint();
            }
        });
        contentPane.add(card);
    }

    public void bottom(Container contentPane) {
        /* 第三部分 */
        /** 更多功能 */
        JButton moreRole = new JButton("更多功能");
        moreRole.setLayout(null);
        moreRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
        moreRole.setFont(new Font("黑体", Font.BOLD, 14));
        moreRole.setBounds(0, 835, 110, 40);
        contentPane.add(moreRole);

        /** 好友管理 */
        JButton friendManage = new JButton("好友管理");
        friendManage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        friendManage.setFont(new Font("黑体", Font.BOLD, 14));
        friendManage.setBounds(110, 835, 110, 40);
        contentPane.add(friendManage);

        /** 聊天室管理 */
        JButton groupManage = new JButton("聊天室管理");
        groupManage.setLayout(null);
        groupManage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        groupManage.setFont(new Font("黑体", Font.BOLD, 14));
        groupManage.setBounds(220, 835, 110, 40);
        contentPane.add(groupManage);

        moreRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MoreFunction(user).setVisible(true);
            }
        });

        friendManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FriendOperation(user.getUserId()).setVisible(true);
            }
        });

        groupManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GroupOperation(user.getUserId()).setVisible(true);
            }
        });
    }

    /**
     * 添加最近消息内容
     */
    public static void refreshHistory() {
        historyRoot = new ChatContentNode();
        List<History> historyList = mainInfo.getHistoryList();
        int index = 0;
        if (historyList != null && !historyList.isEmpty()) {
            historyList.sort(Comparator.comparing(History::getLastTime).reversed());
            for (History history : historyList) {
                ChatContentNode node = new ChatContentNode(history.getUserId(), history.getAvatar(), history.getUsername(), history.getContent(), history.getIsGroup());
                if (history.getIsGroup()) {
                    historyNode.put("group:" + history.getUserId(), index);
                } else {
                    historyNode.put("user:" + history.getUserId(), index);
                }
                historyRoot.add(node);
                index++;
            }
        }
        if (historyTree != null) {
            jScrollHistory.remove(historyTree);
            historyTree.removeAll();
            historyTree = null;
        }
        historyTree = new JTree(historyRoot);
        historyTree.setLayout(null);
        historyTree.setRootVisible(false);
        historyTree.setRowHeight(50);
        historyTree.putClientProperty("JTree.lineStyle", "Horizontal");
        historyTree.setRowHeight(80);
        historyTree.setCellRenderer(new ChatContentNodeRenderer());
        historyTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == historyTree && e.getClickCount() == 2) {
                    ChatContentNode dmt = (ChatContentNode) historyTree.getLastSelectedPathComponent();
                    if (dmt.getGroup()) {
                        PrivateChatFrame chatFrame = new PrivateChatFrame(user, 0L, dmt.getAvatar(), dmt.getId(), dmt.getName());
                        chatList.put("group:" + dmt.getName(), chatFrame);
                        chatFrame.setVisible(true);
                    } else {
                        PrivateChatFrame chatFrame = new PrivateChatFrame(user, dmt.getId(), dmt.getAvatar(), 0L, dmt.getName());
                        chatList.put(user.getUsername() + "to" + dmt.getName(), chatFrame);
                        chatFrame.setVisible(true);
                    }
                }
            }
        });
        jScrollHistory.add(historyTree);
        jScrollHistory.setViewportView(historyTree);
        jScrollHistory.repaint();
    }

    /**
     * 添加好友信息
     */
    public static void refreshFriendList() {
        friendTreeRoot = new UserNode();
        friendNode = new UserNode(0, "我的好友");
        strangerNode = new UserNode(0, "陌生人");
        blackListNode = new UserNode(0, "黑名单");

        // 排序
        sortStatus();
        friendTreeRoot.removeAllChildren();
        friendNode.removeAllChildren();
        strangerNode.removeAllChildren();
        blackListNode.removeAllChildren();

        // 添加好友数据
        List<Friend> friendList = mainInfo.getFriendList();
        if (friendList != null) {
            for (Friend friend : friendList) {
                UserNode userNode = new UserNode(1, friend.getUserId(), friend.getAvatar(), friend.getUsername(), friend.getIndividualSign(), friend.getStatus());
                friendNode.add(userNode);
                friendNodeMap.put(friend.getUserId(), friendNode.getChildCount() - 1);
            }
        }

        // 添加陌生人数据
        List<Friend> strangerList = mainInfo.getStrangerList();
        if (strangerList != null) {
            for (Friend stranger : strangerList) {
                UserNode userNode = new UserNode(1, stranger.getUserId(), stranger.getAvatar(), stranger.getUsername(), stranger.getIndividualSign(), stranger.getStatus());
                strangerNode.add(userNode);
                strangerNodeMap.put(stranger.getUserId(), strangerNode.getChildCount() - 1);
            }
        }

        // 添加黑名单数据
        List<Friend> blackList = mainInfo.getBlackList();
        if (blackList != null) {
            for (Friend black : blackList) {
                UserNode userNode = new UserNode(1, black.getUserId(), black.getAvatar(), black.getUsername(), black.getIndividualSign(), black.getStatus());
                blackListNode.add(userNode);
                blackNodeMap.put(black.getUserId(), blackListNode.getChildCount() - 1);
            }
        }

        if (friendTree != null) {
            jScrollFriend.remove(friendTree);
            friendTree.removeAll();
            friendTree = null;
        }
        friendTreeRoot.add(friendNode);
        friendTreeRoot.add(strangerNode);
        friendTreeRoot.add(blackListNode);
        friendTree = new JTree(friendTreeRoot);
        friendTree.setCellRenderer(new UserNodeRenderer());
        friendTree.setModel(new DefaultTreeModel(friendTreeRoot));
        friendTree.setLayout(null);
        friendTree.setRootVisible(false);
        friendTree.setToggleClickCount(1);
        friendTree.setRowHeight(50);
        friendTree.putClientProperty("JTree.lineStyle", "Horizontal");
        friendTree.setRowHeight(80);
        friendTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == friendTree && e.getClickCount() == 2) {
                    UserNode dmt = (UserNode) friendTree.getLastSelectedPathComponent();
                    if (dmt.getType() == 1) {
                        PrivateChatFrame chatFrame = new PrivateChatFrame(user, dmt.getUserId(), dmt.getAvatar(), 0L, dmt.getUsername());
                        chatList.put(user.getUsername() + "to" + dmt.getUsername(), chatFrame);
                        chatFrame.setVisible(true);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isMetaDown() && e.getSource() == friendTree) {
                    if (friendTree != null) {
                        UserNode dmt = (UserNode) friendTree.getLastSelectedPathComponent();
                        if (dmt.getType() == 1 && dmt.getParent().equals(friendNode)) {
                            friendSelectId = dmt.getUserId();
                            friendPop.show(friendTree, e.getX(), e.getY());
                        }
                        if (dmt.getType() == 1 && dmt.getParent().equals(strangerNode)) {
                            strangerSelectId = dmt.getUserId();
                            strangerPop.show(friendTree, e.getX(), e.getY());
                        }
                        if (dmt.getType() == 1 && dmt.getParent().equals(blackListNode)) {
                            blackListSelectId = dmt.getUserId();
                            blackListPop.show(friendTree, e.getX(), e.getY());
                        }
                    }
                }
            }
        });
        jScrollFriend.add(friendTree);
        jScrollFriend.setViewportView(friendTree);
        jScrollFriend.repaint();
    }

    /**
     * 添加聊天室面板
     */
    public static void refreshGroup() {
        groupRoot = new ChatContentNode();
        List<Group> groupList = mainInfo.getGroupList();
        if (groupList != null) {
            for (Group group : groupList) {
                groupRoot.add(new ChatContentNode(group.getGroupId(), group.getGroupImg(), group.getGroupTitle(), "", true));
            }
        }
        if (groupTree != null) {
            jScrollGroup.remove(groupTree);
            groupTree.removeAll();
            groupTree = null;
        }
        groupTree = new JTree(groupRoot);
        groupTree.setLayout(null);
        groupTree.setRootVisible(false);
        groupTree.setRowHeight(50);
        groupTree.putClientProperty("JTree.lineStyle", "Horizontal");
        groupTree.setRowHeight(80);
        groupTree.setCellRenderer(new ChatContentNodeRenderer());
        groupTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == groupTree && e.getClickCount() == 2) {
                    ChatContentNode dmt = (ChatContentNode) groupTree.getLastSelectedPathComponent();
                    PrivateChatFrame chatFrame = new PrivateChatFrame(user, 0L, dmt.getAvatar(), dmt.getId(), dmt.getName());
                    chatList.put("group:" + dmt.getName(), chatFrame);
                    chatFrame.setVisible(true);
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isMetaDown() && e.getSource() == groupTree && groupTree != null) {
                    ChatContentNode dmt = (ChatContentNode) groupTree.getLastSelectedPathComponent();
                    if (dmt != null) {
                        groupSelectId = dmt.getId();
                        groupPop.show(groupTree, e.getX(), e.getY());
                    }
                }
            }

        });
        jScrollGroup.add(groupTree);
        jScrollGroup.setViewportView(groupTree);
        jScrollGroup.repaint();
    }

    // 对好友列表进行登录状态排序
    public static void sortStatus() {
        List<Friend> friendList = mainInfo.getFriendList();
        if (friendList != null && !friendList.isEmpty()) {
            for (int i = 0; i < friendList.size() - 1; i++) {
                for (int j = i + 1; j < friendList.size(); j++) {
                    if ((friendList.get(i).getStatus() == 0 || friendList.get(i).getStatus() == 2) && friendList.get(j).getStatus() == 1) {
                        Friend temp = friendList.get(i);
                        friendList.set(i, friendList.get(j));
                        friendList.set(j, temp);
                    }
                }
            }
        }

        List<Friend> strangerList = mainInfo.getStrangerList();
        if (strangerList != null && !strangerList.isEmpty()) {
            for (int i = 0; i < strangerList.size() - 1; i++) {
                for (int j = i + 1; j < strangerList.size(); j++) {
                    if ((strangerList.get(i).getStatus() == 0 || strangerList.get(i).getStatus() == 2) && strangerList.get(j).getStatus() == 1) {
                        Friend temp = strangerList.get(i);
                        strangerList.set(i, strangerList.get(j));
                        strangerList.set(j, temp);
                    }
                }
            }
        }

        List<Friend> blackList = mainInfo.getBlackList();
        if (blackList != null && !blackList.isEmpty()) {
            for (int i = 0; i < blackList.size() - 1; i++) {
                for (int j = i + 1; j < blackList.size(); j++) {
                    if ((blackList.get(i).getStatus() == 0 || blackList.get(i).getStatus() == 2) && blackList.get(j).getStatus() == 1) {
                        Friend temp = blackList.get(i);
                        blackList.set(i, blackList.get(j));
                        blackList.set(j, temp);
                    }
                }
            }
        }
    }

    // 添加好友菜单
    public void addFriendPopMenu() {
        friendPop.add(new AbstractAction("拉黑") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(friendSelectId);
                    changeRelation.setRelation(2);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("changeRelation", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        friendPop.add(new AbstractAction("移至陌生人") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(friendSelectId);
                    changeRelation.setRelation(1);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("changeRelation", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        friendPop.add(new AbstractAction("删除好友") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(friendSelectId);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("deleteFriend", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // 添加陌生人菜单
    public void addStrangerPopMenu() {
        strangerPop.add(new AbstractAction("拉黑") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(strangerSelectId);
                    changeRelation.setRelation(2);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("changeRelation", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        strangerPop.add(new AbstractAction("移至好友") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(strangerSelectId);
                    changeRelation.setRelation(0);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("changeRelation", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        strangerPop.add(new AbstractAction("删除好友") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(strangerSelectId);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("deleteFriend", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // 添加黑名单菜单
    public void addBlackPopMenu() {
        blackListPop.add(new AbstractAction("恢复拉黑") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(blackListSelectId);
                    changeRelation.setRelation(0);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("changeRelation", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        blackListPop.add(new AbstractAction("删除好友") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeRelation changeRelation = new ChangeRelation();
                    changeRelation.setUserId(user.getUserId());
                    changeRelation.setBeChangedUserId(blackListSelectId);
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("deleteFriend", changeRelation));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // 添加聊天室菜单
    public void addGroupPopMenu() {
        groupPop.add(new AbstractAction("退出聊天室") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectOutputStream os = new ObjectOutputStream(Client.socket.getOutputStream());
                    os.writeObject(new SendBody("exitGroup", new ExitGroupId(groupSelectId, user.getUserId())));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}