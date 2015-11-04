package com.hoohaainc.framework.implementation;

import android.graphics.Bitmap;

import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Image;

/**
 * Created by nancio on 11/07/15.
 */
public class AndroidImage implements Image {
    Bitmap bitmap;
    Graphics.ImageFormat format;

    public AndroidImage(Bitmap bitmap, Graphics.ImageFormat format){
        this.bitmap = bitmap;
        this.format = format;
    }


    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Graphics.ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
