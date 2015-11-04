package com.hoohaainc.harden.game;

import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.utility.Rectangle;
import com.hoohaainc.framework.utility.Vector2;

/**
 * Created by nancio on 11/07/15.
 */
public abstract class GameObject {

    protected Vector2 position;
    protected Rectangle hitbox;

    public abstract void update(float deltaTime);
    public abstract void draw(Graphics g);

    public Vector2 getPosition(){ return position; }
    public void setPosition(Vector2 p){ position = p; }
    public Rectangle getHitbox(){ return hitbox.offSetCopy(position); }

}
