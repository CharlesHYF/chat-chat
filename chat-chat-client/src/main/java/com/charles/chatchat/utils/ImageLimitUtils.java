package com.charles.chatchat.utils;

import java.awt.*;

/**
 * @author Charles-H
 * 
 * 缩放图
 */
public class ImageLimitUtils {

    public static Image getImageIcon(String path, int width, int height) {
        return Toolkit.getDefaultToolkit().createImage(path).getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    
}
