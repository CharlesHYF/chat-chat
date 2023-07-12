package com.charles.chatchat.model;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Charles-H
 * 
 * 好友节点
 */
public class UserNode extends DefaultMutableTreeNode {

    /**
     * 节点类型
     */
    private Integer type;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户名
     */
    private String username;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 用户状态
     */
    private Integer status;

    public UserNode() {
    }

    public UserNode(Integer type, String username) {
        super();
        this.type = type;
        this.username = username;
    }

    public UserNode(Integer type, Long userId, String avatar, String username, String sign, Integer status) {
        super();
        this.type = type;
        this.userId = userId;
        this.avatar = avatar;
        this.username = username;
        this.sign = sign;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserNode{" +
                "type=" + type +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", sign='" + sign + '\'' +
                ", status=" + status +
                '}';
    }

    public UserNode getChildAt(int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return (UserNode) children.elementAt(index);
    }
}
