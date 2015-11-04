package com.hoohaainc.framework;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by nancio on 10/07/15.
 */
public interface Graphics {
    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Image newImage(String fileName, ImageFormat format);
    public void clearScreen(int color);
    public void drawLine(int x, int y, int x2, int y2, int color);
    public void drawRect(int x, int y, int width, int height, int color);
    public void drawCircle(int cx, int cy, int r, int color);
    public void drawOval(int x, int y, int width, int height, int color);
    public void drawImage(Image image, int x, int y);
    public void drawImage(Image image, Point dst);
    public void drawImage(Image image, int x, int y, int width, int height);
    public void drawImage(Image image, Rect dst);
    public void drawImage(Image image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight);
    public void drawImage(Image image, Point dst, Rect src);
    public void drawImage(Image image, int x, int y, int width, int height, int srcX, int srcY,
            int srcWidth, int srcHeight);
    public void drawImage(Image image, Rect dst, Rect src);
    
    void drawString(String text, int x, int y, Paint paint);
    public int getWidth();
    public int getHeight();
    public void drawARGB(int i, int j, int k, int l);
}
