package com.hoohaainc.harden.game;

import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Image;
import com.hoohaainc.framework.utility.Vector2;

import java.util.EnumMap;

/**
 * Created by nancio on 11/07/15.
 */
public class AnimationStrip<K extends Enum<K>> {

    public class Animation{
        private Image texture;
        private int width, height, currentFrame = 0, framesLength, repeatedTimes = 0;
        private float velocity = 0.1f, timer = 0;
        private K nextAnimation = null;
        private boolean repeat = true, hasEnded = false;

        private Animation(Image texture, int width) {
            this.texture = texture;
            this.width = width;
            this.height = texture.getHeight();
            framesLength = texture.getWidth()/width;
        }

        void update(float deltaTime){
            if(hasEnded) return;
            timer += deltaTime;
            if(timer >= velocity){
                timer-=velocity;
                ++currentFrame;
                if(currentFrame >= framesLength){
                    if(repeat) {
                        currentFrame = 0;
                        ++repeatedTimes;
                    }
                    else if(nextAnimation != null)
                        setAnimation(nextAnimation);
                    else {
                        --currentFrame;
                        hasEnded = true;
                    }
                }
            }
        }

        void draw(Graphics g){
            g.drawImage(
                    texture,
                    (int)position.x,
                    (int)position.y,
                    currentFrame*width,
                    0,
                    width,
                    height);
        }

        public void setNextAnimation(K next){
            nextAnimation = next;
            repeat = false;
        }

        public void restart(){ currentFrame = 0; }

        public void setRepeat(boolean r){
            repeat = r;
        }

        public void setVelocity(float v){
            velocity = v;
        }

        boolean hasEnded(){
            return hasEnded;
        }

        public int getRepeatedTimes(){ return repeatedTimes; }

        public float getVelocity(){ return velocity; }

        public boolean isRepeating(){ return repeat; }

        public K getNextAnimation(){ return nextAnimation; }

    }

    EnumMap<K, Animation> animations;
    K currentAnimation;
    Vector2 position = new Vector2();

    public AnimationStrip(Class<K> kClass){
        animations = new EnumMap<>(kClass);
    }

    public void add(K key, Image image, int width){
        Animation nAnimation = new Animation(image, width);
        animations.put(key, nAnimation);
    }

    public Animation get(K key){
        return animations.get(key);
    }

    public Animation remove(K key){
        return animations.remove(key);
    }

    public void setAnimation(K key){
        if(currentAnimation == key) return;
        if(currentAnimation != null)
            animations.get(currentAnimation).restart();
        currentAnimation = key;
    }

    public K getCurrentAnimation(){
        return currentAnimation;
    }

    public void setPosition(Vector2 p){
        this.position = p;
    }

    public boolean animationEnded(){
        return animations.get(currentAnimation).hasEnded();
    }

    public void update(float deltaTime){
        animations.get(currentAnimation).update(deltaTime);
    }

    public void draw(Graphics g){
        animations.get(currentAnimation).draw(g);
    }
}
