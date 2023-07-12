package com.charles.chatchat.model;

import com.charles.chatchat.utils.ImageLimitUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author Charles-H
 * 
 * 用户节点渲染
 */
public class UserNodeRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        UserNode userNode = (UserNode) value;

        ImageIcon icon = new ImageIcon(userNode.getAvatar() + "");
        icon.setImage(icon.getImage().getScaledInstance(60,60,Image.SCALE_DEFAULT));
        String status = "在线";
        String text = "";
        
        if (userNode.getType() != null) {
            if (userNode.getType() == 0) {
                text = "<html>" + "<span style='font-size: 13px; font-family: SimHei'>" + userNode.getUsername() + "<span/> <html/>";
                setText(text);
                setIconTextGap(2);
            } else {
                if (userNode.getStatus() == 0 || userNode.getStatus() == 2) {
                    status = "离线";
                    text = "<html>" + "<span  style='font-size: 12px; font-family: SimHei'>" + userNode.getUsername() + "<span/>" + "<br/>" +  "<span style='color: gray; font-size: 11px; font-family: SimHei'>" + userNode.getSign() + "<span/>" + "<br/>" + "<span style='color: gray; font-size: 11px; font-family: SimHei'>" + status + "<span/> <html/>";
                } else {
                    text = "<html>" + "<span  style='font-size: 12px; font-family: SimHei'>" + userNode.getUsername() + "<span/>" + "<br/>" +  "<span style='color: gray; font-size: 11px; font-family: SimHei'>" + userNode.getSign() + "<span/>" + "<br/>" + "<span style='color: green; font-size: 11px; font-family: SimHei'>" + status + "<span/> <html/>";
                }
                setIcon(icon);
                setText(text);
                setIconTextGap(15);
            }
            setLeafIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/xiangyou1.png", 20, 20)));
            setClosedIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/xiangyou1.png", 20, 20)));
            setOpenIcon(new ImageIcon(ImageLimitUtils.getImageIcon("chat-chat-client/src/main/resources/static/images/xiangxia2.png", 20, 20)));
        }
        return this;
    }
}
