package cookie;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author nancio
 */
public class Building extends LayerManager{
    
    /*public static final int CURSOR = 0;
    public static final int GRANDMA = 1;
    public static final int FARM = 2;
    public static final int FACTORY = 3;
    public static final int MINE = 4;
    public static final int SHIP = 5;
    public static final int LAB = 6;
    public static final int PORTAL = 7;
    public static final int TIME = 8;
    public static final int ANTIMATTER = 9;
    public static final int PRISM = 10;*/
    
    public static final String types[] = {"cursor", "grandma", "farm", "factory", "mine", "ship", "lab", "portal", "time", "antimatter", "prism"};
    private static final String prices[] = {"15", "100", "500", "3000", "10000", "40000", "200000", "1666666", "123456789", "3999999999", "75000000000"};
    private static final String bases[] = {"0.1", "0.5", "4", "10", "40", "100", "400", "6666", "98765", "999999", "10000000"};

    private static int quantities []= new int[11];

    private Sprite sprite;
    private CookieFloat price;
    private CookieFloat baseCps;
    private int multi;
    //private CookieFloat unlockAt;
    private int type;
    private CookieFloat extraCps;
    private CookieFloat cps;
    
    public Building(int type){
        this(types[type], prices[type], bases[type]);
        this.type = type;
    }
    
    private Building(String img, String p, String c){
        try{
            sprite = new Sprite(Image.createImage("/rsc/"+img+".png"));
        }catch(Exception err){}
        this.append(sprite);
        price = new CookieFloat(p);
        baseCps = new CookieFloat(c);
        multi=1;
        extraCps = new CookieFloat(0);
        cps = baseCps;
        
        this.setViewWindow(0, 0, 48, 48);
    }
    
    public CookieFloat getPrice(){
        return new CookieFloat(price.getRound(true, false));
    }
    
    public int getN(){
        return quantities[type];
    }

    public static int getN(int i){ return quantities[i];}
    
    public CookieFloat getCps(){
        return cps;
    }
    
    public CookieFloat buy(){
        CookieFloat r = this.getPrice();
        ++quantities[type];
        price.m(new CookieFloat("1", "15"));//*=1.15;
        return r;
    }

    public void addBase(double add){
        baseCps.s(new CookieFloat(add + ""));
        reCalcCps();
    }

    public void twice(){
        multi*=2;
        reCalcCps();
    }

    public void addExtra(CookieFloat e){
        extraCps.s(e);
        reCalcCps();
    }

    public void reCalcCps(){
        cps = CookieFloat.s(CookieFloat.m(baseCps, new CookieFloat(multi + "")), extraCps);
    }
    
}
