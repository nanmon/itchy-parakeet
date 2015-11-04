package com.hoohaainc.framework.utility;


import android.graphics.Rect;

/**
 * Created by nancio on 13/07/15.
 */
public class Rectangle {
    public int x=0, y=0, width=0, height=0;

    public Rectangle(){}

    public Rectangle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle r){
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }

    public Rectangle(Rect r){
        this.x = r.left;
        this.y = r.top;
        this.width = r.width();
        this.height = r.height();
    }

    public int right(){ return x + width; }
    public int bottom(){ return y + height; }
    public int centerX(){ return x + width/2; }
    public int centerY(){ return y + height/2; }

    public Vector2 center(){ return new Vector2(x + width/2.0f, y + height/2.0f); }

    public void offSet(int diffX, int diffY){
        x += diffX;
        y += diffY;
    }

    public void offSet(Vector2 diff){
        x += (int)diff.x;
        y += (int)diff.y;
    }

    public Rectangle offSetCopy(int diffX, int diffY){
        return new Rectangle(x + diffX, y + diffY, width, height);
    }

    public Rectangle offSetCopy(Vector2 diff){
        return new Rectangle(x + (int)diff.x, y + (int)diff.y, width, height);
    }


    public boolean intersects(Rectangle b){
        return (x >= b.x && x <= b.x + b.width && y >= b.y && y <= b.y + b.height ) ||
                (b.x >= x && b.x <= x + width && b.y >= y && b.y <= y + height);
    }

    public boolean contains(Rectangle b){
        return b.x >= x && b.x <= x + width && b.y >= y && b.y <= y + height && //contains position
                b.x + b.width >= x && b.x + b.width <= x + width && //contains right
                b.y + b.height >= y && b.y + b.height <= y + height; //contains bottom
    }

    public boolean contains(Vector2 v){
        return v.x >= x && v.x <= x + width && v.y >= y && v.y <= y + width;
    }

    public boolean contains(int x1, int y1){
        return x1 >= x && x1 <= x + width && y1 >= y && y1 <= y + width;
    }


}
