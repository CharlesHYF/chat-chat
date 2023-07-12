package com.charles.chatchat.model;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Charles-H
 * 
 * 聊天节点
 */
public class ChatContentNode extends DefaultMutableTreeNode {

    /** 用户或聊天室id */
    private Long id;
    
    /** 用户或聊天室头像 */
    private String avatar;

    /** 用户名或聊天室名 */
    private String name;

    /** 内容 */
    private String content;
    
    /** 识别聊天室 */
    private Boolean isGroup;

    public ChatContentNode() {
    }

    public ChatContentNode(Long id, String avatar, String name, String content, Boolean isGroup) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.content = content;
        this.isGroup = isGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }
}
