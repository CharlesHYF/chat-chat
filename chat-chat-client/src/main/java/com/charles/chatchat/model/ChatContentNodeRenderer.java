package com.charles.chatchat.model;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author Charles-H
 * 
 * 聊天节点渲染
 */
public class ChatContentNodeRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        ChatContentNode contentNode = (ChatContentNode) value;

        ImageIcon icon = new ImageIcon(contentNode.getAvatar() + "");
        icon.setImage(icon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        String text = "<html>" + "<span  style='font-size: 12px; font-family: SimHei'>" + contentNode.getName() + "<span/>" + "<br/>" + "<span style='color: gray; font-size: 11px; font-family: SimHei'>" + contentNode.getContent() + "<span/> <html/>";
        setIcon(icon);
        setText(text);
        setIconTextGap(15);
        return this;
    }
}
