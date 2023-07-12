package com.charles.chatchat.views.page;

import com.charles.chatchat.thread.Client;
import com.charles.server.entity.ChatContent;
import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.ChatHistoryContent;
import com.charles.server.entity.model.History;
import com.charles.server.entity.vo.ChatBody;
import com.charles.server.entity.vo.SendBody;
import com.charles.server.entity.vo.SendChatContent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Charles-H
 * 
 * 打开聊天页开始建立连接
 * 聊天页
 */
public class PrivateChatFrame extends JFrame {
    
    JTextArea data;

    public PrivateChatFrame(ChatUser chatUser, Long chatId, String avatar, Long groupId, String username) {
        if (chatId == 0) {
            this.setTitle(username);
        } else {
            this.setTitle(chatUser.getUsername() + "对" + username + "说");
        }
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - 600) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - 500) / 2;
        this.setLocation(x, y);
        this.setResizable(false);
        Container pane = this.getContentPane();
        pane.setLayout(null);
        pane.setSize(600, 475);
        pane.setVisible(true);

        // 内容
        data = new JTextArea();
        data.setLayout(null);
        data.setFont(new Font("黑体", Font.BOLD, 16));
        data.setBounds(0, 0, 600, 435);
        data.setEditable(false);
        JScrollPane content = new JScrollPane(data, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        content.setBounds(0, 0, 600, 435);

        // 调用服务器，获取聊天记录
        try {
            System.out.println("正在获取聊天记录...");
            if (groupId == null || groupId == 0) {
                ChatHistoryContent chatHistoryContent = new ChatHistoryContent(chatUser.getUserId(), chatId, groupId);
                ObjectOutputStream os = null;
                os = new ObjectOutputStream(Client.socket.getOutputStream());
                os.writeObject(new SendBody("requestSingleHistoryMsg", chatHistoryContent));
            } else {
                ChatHistoryContent chatHistoryContent = new ChatHistoryContent(chatUser.getUserId(), chatId, groupId);
                ObjectOutputStream os = null;
                os = new ObjectOutputStream(Client.socket.getOutputStream());
                os.writeObject(new SendBody("requestGroupHistoryMsg", chatHistoryContent));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // 输入
        JTextField sendField = new JTextField(20);
        sendField.setText("按回车发送");
        sendField.setForeground(Color.GRAY);
        sendField.setBounds(150, 438, 300, 25);
        sendField.setFont(new Font("黑体", Font.BOLD, 16));

        // 回车发送，调用业务
        sendField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (chatId != 0L) {
                        appendText(new ChatBody(chatUser.getUsername(), username, chatUser.getUserId(), chatId, chatUser.getAvatar(), avatar, sendField.getText(), new Date()));
                        // 发送给服务器，服务器进行响应
                        ChatContent chatContent = new ChatContent(null, chatUser.getUserId(), chatId, 0L, sendField.getText(), new Date());
                        SendChatContent sendChatContent = new SendChatContent(chatUser.getUsername(), username, chatUser.getAvatar(), avatar, chatContent);
                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(Client.socket.getOutputStream());
                            outputStream.writeObject(new SendBody("sendSingleMsg", sendChatContent));
                            System.out.println("你向服务器发送了一串数据：" + sendChatContent);
                            Integer index = MainFrame.historyNode.get("user:" + chatId);
                            if (index != null) {
                                History history = MainFrame.mainInfo.getHistoryList().get(index);
                                history.setContent(sendChatContent.getChatContent().getContent());
                                history.setLastTime(new Date());
                                MainFrame.mainInfo.getHistoryList().set(index, history);
                                MainFrame.refreshHistory();
                            } else {
                                History history = new History();
                                history.setUserId(chatId);
                                history.setAvatar(avatar);
                                history.setUsername(username);
                                history.setContent(sendChatContent.getChatContent().getContent());
                                history.setLastTime(new Date());
                                history.setIsGroup(false);
                                MainFrame.mainInfo.getHistoryList().add(history);
                                MainFrame.refreshHistory();
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        sendField.setText("");
                    } else {
                        appendText(new ChatBody(chatUser.getUsername(), "", chatUser.getUserId(), 0L, null, null, sendField.getText(), new Date()));
                        // 发送给服务器，服务器进行响应
                        ChatContent chatContent = new ChatContent(null, chatUser.getUserId(), 0L, groupId, sendField.getText(), new Date());
                        SendChatContent sendChatContent = new SendChatContent(null, null, null, null, chatContent);
                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(Client.socket.getOutputStream());
                            outputStream.writeObject(new SendBody("sendGroupMsg", sendChatContent));
                            System.out.println("你向服务器发送了一串数据：" + sendChatContent);
                            Integer index = MainFrame.historyNode.get("group:" + groupId);
                            if (index != null) {
                                History history = MainFrame.mainInfo.getHistoryList().get(index);
                                history.setContent(sendChatContent.getChatContent().getContent());
                                history.setLastTime(new Date());
                                MainFrame.mainInfo.getHistoryList().set(index, history);
                                MainFrame.refreshHistory();
                            } else {
                                History history = new History();
                                history.setUserId(groupId);
                                history.setAvatar(avatar);
                                history.setUsername(username);
                                history.setContent(sendChatContent.getChatContent().getContent());
                                history.setLastTime(new Date());
                                history.setIsGroup(true);
                                MainFrame.mainInfo.getHistoryList().add(history);
                                MainFrame.refreshHistory();
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        sendField.setText("");
                    }
                    
                }
            }
        });
        
        sendField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (sendField.getText().equals("按回车发送")) {
                    sendField.setText("");
                    sendField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (sendField.getText().equals("")) {
                    sendField.setText("按回车发送");
                    sendField.setForeground(Color.GRAY);
                }
            }
        });
        pane.add(content);
        pane.add(sendField);
    }

    public String formatTime(Date date) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }
    
    public void appendText(ChatBody body) {
        if (body.getReceiveName() == null || body.getReceiveName().isEmpty()) {
            data.append("\t\t\t" + formatTime(body.getSendTime()) + "\n");
            data.append(body.getSendName() + "说:" + body.getContent() + "\n");
            data.setCaretPosition(data.getText().length());
        } else {
            data.append("\t\t\t" + formatTime(body.getSendTime()) + "\n");
            data.append(body.getSendName() + "对" + body.getReceiveName() + "说:" + body.getContent() + "\n");
            data.setCaretPosition(data.getText().length());
        }
    }
    
}
