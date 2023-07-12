package com.charles.chatchat.views.page;


import javax.swing.*;
import java.awt.*;

/**
 * @author Charles-H
 * 
 * 初始化框架
 */
public class InitFrame extends JFrame {

    private String title;

    private Integer width;

    private Integer height;
    
    private Boolean isCenter;

    public InitFrame(String title, Integer width, Integer height, Boolean isCenter) {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (isCenter) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int x = (int) (toolkit.getScreenSize().getWidth() - width) / 2;
            int y = (int) (toolkit.getScreenSize().getHeight() - height) / 2;
            this.setLocation(x, y);
        }
        this.setSize(width, height);
        this.setResizable(false);
    }
    
}
