package com.hoohaainc.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Image;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by nancio on 10/07/15.
 */
public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer){
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Image newImage(String fileName, ImageFormat format) {
        Bitmap.Config config = null;
        if(format == ImageFormat.RGB565)
            config = Bitmap.Config.RGB_565;
        else if(format == ImageFormat.ARGB4444)
            config = Bitmap.Config.ARGB_4444;
        else
            config = Bitmap.Config.ARGB_8888;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try{
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if(bitmap == null){
                throw new RuntimeException("Could not load bitmap from asset '"
                + fileName + "'");
            }
        }catch(IOException e){
            throw new RuntimeException("Could not load bitmap from asset '"
                    + fileName + "'");
        }finally {
            if(in != null)
                try{
                    in.close();
                }catch(IOException e) {}
        }

        if(bitmap.getConfig() == Bitmap.Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Bitmap.Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;

        return new AndroidImage(bitmap, format);
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }
    
    @Override
    public void drawCircle(int cx, int cy, int r, int color) {
    	paint.setColor(color);
    	paint.setStyle(Paint.Style.FILL);
    	canvas.drawCircle(cx, cy, r, paint);
    }
    
    @Override
    public void drawOval(int x, int y, int width, int height, int color) {
    	paint.setColor(color);
    	paint.setStyle(Paint.Style.FILL);
    	canvas.drawOval(new RectF(x, y, x+width, y+height), paint);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, int x, int y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }
    
    @Override
    public void drawImage(Image image, int x, int y) {
        canvas.drawBitmap(((AndroidImage)image).bitmap, x, y, null);
    }
    
    @Override
    public void drawImage(Image image, Point dst) {
    	drawImage(image, dst.x, dst.y);
    }
    
    @Override
    public void drawImage(Image image, int x, int y, int width, int height) {
    	drawImage(image, x, y, width, height, //dst
    			0, 0, image.getWidth(), image.getHeight());
    }
    
    @Override
    public void drawImage(Image image, Rect dst) {
    	drawImage(image, dst.left, dst.top, dst.right-dst.left, dst.bottom-dst.top, //dst
    			0, 0, image.getWidth(), image.getHeight()); //src
    }

    @Override
    public void drawImage(Image image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
    }
    
    @Override
    public void drawImage(Image image, Point dst, Rect src) {
    	drawImage(image, dst.x, dst.y, src.left, src.top, src.right-src.left, src.bottom-src.top);
    }

    
    @Override
    public void drawImage(Image image, int x, int y, int width, int height,
                                int srcX, int srcY, int srcWidth, int srcHeight){
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;

        canvas.drawBitmap(((AndroidImage)image).bitmap, srcRect, dstRect, null);
    }
    
    @Override
    public void drawImage(Image image, Rect dst, Rect src) {
    	drawImage(image, dst.left, dst.top, dst.right-dst.left, dst.height(),
    			src.left, src.top, src.width(), src.height());
    	
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
