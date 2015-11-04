package cookie;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cookie.CookieFloat;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author nancio
 */
public abstract class Upgrade extends LayerManager{

    public static int BUILDINGTYPE = 0;

    protected CookieFloat cost;
    protected int state;
    protected int type;
    
    public Upgrade(CookieFloat cst, int t){
        cost = cst;
        state = 0;
        type = t;
    }
    
    public int getState(){
        return state;
    }
    
    public CookieFloat getCost(){
        return cost;
    }

    public int[] getType() {
        int r[] = {type};
        return r;
    }
    
    public int tryBuy(CookieFloat b){
        if(b.gt(this.cost)) this.state=2;
        return state;
    }

    public int buyed(){
        this.state = 2;
        return state;
    }

    abstract public int tryUnlock(CookieFloat[] a);
    abstract public double[] getEffect();
    abstract public int[] getUnlockType();
    abstract public String getDescription();
    
}
