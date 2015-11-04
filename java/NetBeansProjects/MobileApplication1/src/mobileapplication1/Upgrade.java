/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication1;

/**
 *
 * @author nancio
 */
public class Upgrade {
    private CookieFloat cost;
    protected int state;
    
    public Upgrade(int cst){
        this(new CookieFloat(cst));
    }
    
    public Upgrade(CookieFloat cst){
        cost = cst;
        state = 0;
    }
    
    public int getState(){
        return state;
    }
    
    public CookieFloat getCost(){
        return cost;
    }
    
    public int tryBuy(CookieFloat b){
        if(b.gt(this.cost)) this.state=2;
        return state;
    }
    
}
