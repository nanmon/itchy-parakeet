package com.hoohaainc.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by nancio on 11/07/15.
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;
    Bitmap frameBuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame game, Bitmap frameBuffer){
        super(game);
        this.game = game;
        this.frameBuffer = frameBuffer;
        this.holder = getHolder();
    }

    public void resume(){
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    @Override
    public void run() {
        Rect dstRect = new Rect();
        Canvas canvas;
        long startTime = System.nanoTime();
        while(running){
            if(!holder.getSurface().isValid())
                continue;

            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            if(deltaTime >0.0315f){
                deltaTime = 0.0315f;
            }

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(deltaTime);

            canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(frameBuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);

        }
    }

    public void pause(){
        running = false;
        while(true){
            try{
                renderThread.join();
                break;
            }catch(InterruptedException e){
                //retry
            }
        }
    }
}