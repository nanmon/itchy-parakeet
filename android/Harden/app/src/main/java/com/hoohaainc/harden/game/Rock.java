package com.hoohaainc.harden.game;

import android.graphics.Rect;

import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.utility.Rectangle;
import com.hoohaainc.framework.utility.Vector2;
import com.hoohaainc.harden.Assets;

/**
 * Created by nancio on 12/07/15.
 */
public class Rock extends GameObject{
    enum Animations{ Rock }
    int screenHeight;
    boolean destroyed = false;

    AnimationStrip<Animations> animations;

    public Rock(int screenHeight){
        this.position = new Vector2(0, -64);
        this.screenHeight = screenHeight;
        animations = new AnimationStrip<>(Animations.class);
        animations.add(
                Animations.Rock,
                Assets.rock,
                64);
        animations.setAnimation(Animations.Rock);
        hitbox = new Rectangle(0,0,64,64);
    }

    @Override
    public void update(float deltaTime) {
        position.y += screenHeight*deltaTime;
        animations.setPosition(position);
        animations.update(deltaTime);
        if(position.y >= screenHeight) destroyed = true;
        //if(animations.getCurrentAnimation() == Animations.Destroyed)
        //  if(animations.animationEnded()) destroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        animations.draw(g);
    }

    public void unDestroy(){
        destroyed = false;
    }

    public void destroy(){
        //animations.setAnimation(Animations.Destroyed);
        destroyed = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
