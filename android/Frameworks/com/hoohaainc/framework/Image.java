package com.hoohaainc.framework;

/**
 * Created by nancio on 10/07/15.
 */
public interface Image {
    public int getWidth();
    public int getHeight();
    public Graphics.ImageFormat getFormat();
    public void dispose();
}
