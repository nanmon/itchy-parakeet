package cookie;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import java.io.IOException;

/**
 *
 * @author nancio
 */
public class UpgradeManager{

    public static int COOKIES = 0;
    public static int OWNEDBUILDINGS = 1;
    public static int BUILDINGTYPE = 2;
    
    private class BuildingUpgrade extends Upgrade{

        //public static final int TWICE = 0;

        protected int unlockAt;
        private int btype;
        protected double effect;
        public Sprite sp;
        
        public BuildingUpgrade(int cst, int t, int n, float ef){
            this(new CookieFloat(cst), t, n, ef);
        }
        
        public BuildingUpgrade(String cst, int t, int n, float ef){
            this(new CookieFloat(cst), t, n, ef);
        }
        
        public BuildingUpgrade(CookieFloat cst, int t, int n, float ef){
            super(cst, 0);
            unlockAt = n;
            btype = t;
            effect = ef;
            try {
                sp = new Sprite(Image.createImage("/rsc/up/"+Building.types[t]+n+".png"));
                this.append(sp);
                this.setViewWindow(0,0, 30, 30);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        public int tryUnlock(CookieFloat u[]){
            if(state>=1) return state;
            if(Integer.parseInt(u[0].get(false))== btype && u[1].gt(new CookieFloat(unlockAt))){
                this.state=1;
            }
            return state;
        }

        public double[] getEffect(){
            double r[] = {effect};
            return r;
        }

        public int[] getType(){
            int r[] = {type, btype};
            return r;
        }

        public int[] getUnlockType(){
            return getType();
        }

        public String getDescription(){
            return Building.types[btype] + (effect==0 ? "s are twice as efficient" : "s gains +"+effect+" base CpS");
        }

    }
    private class CursorUpgrade extends BuildingUpgrade{

        //negative: foreach non-cursor, +base

        private double eff2;

        public CursorUpgrade(int cost, int q, float ef){
            this(new CookieFloat(cost), q, ef);
        }

        public CursorUpgrade(String cost, int q, float ef){
            this(new CookieFloat(cost), q, ef);
        }

        public CursorUpgrade(CookieFloat cost, int quantity, float ef){
            super(cost, 0, quantity, ef);
            if(quantity%80==0){
                String img = 40*(quantity/40 -1)+"";
                try {
                    sp = new Sprite(Image.createImage("/rsc/up/cursor"+img+".png"));
                    this.append(sp);
                    this.setViewWindow(0,0, 30, 30);
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            eff2 = ef>0? 1 : -ef;
        }

        public double[] getEffect(){
            double r[] = {effect, eff2};
            return r;
        }

        public String getDescription(){
            if(effect>0) return "mouse gains +1 cookie per click. Cursors gain +0.1 base CpS";
            if(effect==0) return "mouse and cursors are twice as efficient";
            return "mouse and cursors gain +"+eff2+" cookie for each non-cursor object owned";
        }
    }
    private class GrandmaUpgrade extends BuildingUpgrade{

        private int typeToUnlock;

        public GrandmaUpgrade(int cost, int ef){
            this(new CookieFloat(cost), ef);
        }

        public GrandmaUpgrade(String cost, int ef){
            this(new CookieFloat(cost), ef);
        }

        public GrandmaUpgrade(CookieFloat cost, int ef){
            super(cost, 1, 15, 0);
            typeToUnlock = ef;
        }

        public int tryUnlock(CookieFloat u[]){
            if(state>=1) return state;
            if(Integer.parseInt(u[0].get(false))== typeToUnlock && u[1].gt(new CookieFloat(unlockAt))){
                this.state=1;
            }
            return state;
        }

        public int[] getUnlockType(){
            int r[] = {type, typeToUnlock};
            return r;
        }

    }
    private class ClickUpgrade extends Upgrade{

        private CookieFloat handmadeToUnlock;
        private Sprite sp;

        public ClickUpgrade(String u, String c){
            this(new CookieFloat(u), new CookieFloat(c));
        }

        public ClickUpgrade(CookieFloat unlock, CookieFloat cost){
            super(cost, 0);
            handmadeToUnlock = unlock;
            int lng =  unlock.getRound(false, false).length();
            int img = lng<=6 ? 4 : (lng>=14 ? 14 : lng);
            try {
                sp = new Sprite(Image.createImage("/rsc/up/click"+img+".png"));
                this.append(sp);
                this.setViewWindow(0,0,30,30);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        public int tryUnlock(CookieFloat[] a) {
            if(state>=1) return state;
            if(a[1].gt(handmadeToUnlock)) this.state = 1;
            return state;
        }

        public double[] getEffect() {
            return null;
        }

        public int[] getUnlockType() {
            int [] r = {0, 11};
            return r;
        }

        public String getDescription() {
            return "Clicking gains +1% of your CpS";
        }
    }

    private Upgrade upgrades[];
    public int falseIndex[];
    public int falseLength;
    public int selected;

    public UpgradeManager(int type){
        if(type==0) {
            upgrades = new Upgrade[]{
                    new CursorUpgrade(100, 1, 0.1f),
                    new CursorUpgrade(400, 1, 0),
                    new CursorUpgrade(10000, 10, 0),
                    new CursorUpgrade(500000, 20, -0.1f),
                    new CursorUpgrade(50000000, 40, -0.5f),
                    new CursorUpgrade(500000000, 80, -2),
                    new CursorUpgrade("5000000000", 120, -10),
                    new CursorUpgrade("50000000000", 160, -20),
                    new CursorUpgrade("50000000000000", 200, -100),
                    new CursorUpgrade("500000000000000", 240, -200),
                    new CursorUpgrade("5000000000000000", 280, -400),
                    new CursorUpgrade("50000000000000000", 320, -800),
                    new BuildingUpgrade(1000, 1, 1, 0.3f),
                    new BuildingUpgrade(10000, 1, 1, 0),
                    new BuildingUpgrade(100000, 1, 10, 0),
                    new BuildingUpgrade(5000000, 1, 50, 0),
                    new BuildingUpgrade(100000000, 1, 100, 0),
                    new BuildingUpgrade("800000000000", 1, 200, 0),
                    new GrandmaUpgrade(50000, 2),
                    new GrandmaUpgrade(300000, 3),
                    new GrandmaUpgrade(1000000, 4),
                    new GrandmaUpgrade(4000000, 5),
                    new GrandmaUpgrade(20000000, 6),
                    new GrandmaUpgrade(166666600, 7),
                    new GrandmaUpgrade("12345678900", 8),
                    new GrandmaUpgrade("399999999900", 9),
                    new GrandmaUpgrade("7500000000000", 10),
                    new BuildingUpgrade(5000, 2, 1, 1),
                    new BuildingUpgrade(50000, 2, 1, 0),
                    new BuildingUpgrade(500000, 2, 10, 0),
                    new BuildingUpgrade(25000000, 2, 50, 0),
                    new BuildingUpgrade(500000000, 2, 100, 0),
                    new BuildingUpgrade("4000000000000", 2, 200, 0),
                    new BuildingUpgrade(30000, 3, 1, 4),
                    new BuildingUpgrade(300000, 3, 1, 0),
                    new BuildingUpgrade(3000000, 3, 10, 0),
                    new BuildingUpgrade(150000000, 3, 50, 0),
                    new BuildingUpgrade("3000000000", 3, 100, 0),
                    new BuildingUpgrade("24000000000000", 3, 200, 0),
                    new BuildingUpgrade(100000, 4, 1, 10),
                    new BuildingUpgrade(1000000, 4, 1, 0),
                    new BuildingUpgrade(10000000, 4, 10, 0),
                    new BuildingUpgrade(500000000, 4, 50, 0),
                    new BuildingUpgrade("10000000000", 4, 100, 0),
                    new BuildingUpgrade("80000000000000", 4, 200, 0),
                    new BuildingUpgrade(400000, 5, 1, 30),
                    new BuildingUpgrade(4000000, 5, 1, 0),
                    new BuildingUpgrade(40000000, 5, 10, 0),
                    new BuildingUpgrade(2000000000, 5, 50, 0),
                    new BuildingUpgrade("40000000000", 5, 100, 0),
                    new BuildingUpgrade("320000000000000", 5, 200, 0),
                    new BuildingUpgrade(2000000, 6, 1, 100),
                    new BuildingUpgrade(20000000, 6, 1, 0),
                    new BuildingUpgrade(200000000, 6, 10, 0),
                    new BuildingUpgrade("10000000000", 6, 50, 0),
                    new BuildingUpgrade("200000000000", 6, 100, 0),
                    new BuildingUpgrade("1600000000000000", 6, 200, 0),
                    new BuildingUpgrade(16666660, 7, 1, 1666),
                    new BuildingUpgrade(166666600, 7, 1, 0),
                    new BuildingUpgrade(1666666000, 7, 10, 0),
                    new BuildingUpgrade("83333300000", 7, 50, 0),
                    new BuildingUpgrade("166666000000", 7, 100, 0),
                    new BuildingUpgrade("13333328000000000", 7, 200, 0),
                    new BuildingUpgrade(1234567890, 8, 1, 9876),
                    new BuildingUpgrade("9876543210", 8, 1, 0),
                    new BuildingUpgrade("98765456789", 8, 10, 0),
                    new BuildingUpgrade("1234567890000", 8, 50, 0),
                    new BuildingUpgrade("123456789000000", 8, 100, 0),
                    new BuildingUpgrade("987654321000000000", 8, 200, 0),
                    new BuildingUpgrade("39999999990", 9, 1, 99999),
                    new BuildingUpgrade("399999999900", 9, 1, 0),
                    new BuildingUpgrade("3999999999000", 9, 10, 0),
                    new BuildingUpgrade("199999999950000", 9, 50, 0),
                    new BuildingUpgrade("3999999999000000", 9, 100, 0),
                    new BuildingUpgrade("31999999992000000000", 9, 200, 0),
                    new BuildingUpgrade("750000000000", 10, 1, 1000000),
                    new BuildingUpgrade("7500000000000", 10, 1, 0),
                    new BuildingUpgrade("75000000000000", 10, 10, 0),
                    new BuildingUpgrade("3750000000000000", 10, 50, 0),
                    new BuildingUpgrade("75000000000000000", 10, 100, 0),
                    new BuildingUpgrade("600000000000000000000", 10, 200, 0),
                    new ClickUpgrade("1000", "50000"),
                    new ClickUpgrade("100000", "5000000"),
                    new ClickUpgrade("10000000", "500000000"),
                    new ClickUpgrade("1000000000", "50000000000"),
                    new ClickUpgrade("100000000000", "5000000000000"),
                    new ClickUpgrade("10000000000000", "500000000000000"),
                    new ClickUpgrade("1000000000000000", "50000000000000000"),
            };
        }
        falseIndex = new int[upgrades.length];
        falseLength = 0;
        selected = 0;
    }

    public Upgrade[] getUpgrades(){
        return upgrades;
    }

    public Upgrade[] getBuyable(CookieFloat [][] u){
        int iLength = 0;
        for(int i=0; i<upgrades.length; ++i){
            if(upgrades[i].getUnlockType()[0]==0) {
                int b = upgrades[i].getUnlockType()[1];
                CookieFloat [] c = {new CookieFloat(b), u[0][b]};
                if (upgrades[i].tryUnlock(c) == 1) {
                    falseIndex[iLength++] = i;
                }
            }
        }
        Upgrade ups[] = new Upgrade[iLength];
        for(int i=0; i<iLength; ++i){
            ups[i] = upgrades[falseIndex[i]];
        }
        falseLength=iLength;
        return ups;
    }
}
