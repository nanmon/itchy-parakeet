/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication1;

import javax.microedition.lcdui.game.LayerManager;

/**
 *
 * @author nancio
 */
public class UpgradeManager extends LayerManager{
    
    private class BuildingUpgrade extends Upgrade{
        
        private int unlockAt;
        private int type;
        
        public BuildingUpgrade(int cst, int t, int n){
            this(new CookieFloat(cst), t, n);
        }
        
        public BuildingUpgrade(String cst, int t, int n){
            this(new CookieFloat(cst), t, n);
        }
        
        public BuildingUpgrade(CookieFloat cst, int t, int n){
            super(cst);
            unlockAt = n;
            type = t;
        }
        
        public int tryUnlock(int u){
            if(u>=unlockAt) this.state=1;
            return state;
        }
    }
    
    
    private BuildingUpgrade building[];
    
    public UpgradeManager(){
        building = new BuildingUpgrade[] {
            new BuildingUpgrade(1000, 1, 1),
            new BuildingUpgrade(10000, 1, 1),
            new BuildingUpgrade(100000, 1, 10),
            new BuildingUpgrade(5000000, 1, 50),
            new BuildingUpgrade(100000000, 1, 100),
            new BuildingUpgrade("800000000000", 1, 200),
            new BuildingUpgrade(5000, 2, 1),
            new BuildingUpgrade(50000, 2, 1),
            new BuildingUpgrade(500000, 2, 10),
            new BuildingUpgrade(25000000, 2, 50),
            new BuildingUpgrade(500000000, 2, 100),
            new BuildingUpgrade("4000000000000", 2, 200),
            new BuildingUpgrade(30000, 3, 1),
            new BuildingUpgrade(300000, 3, 1),
            new BuildingUpgrade(3000000, 3, 10),
            new BuildingUpgrade(150000000, 3, 50),
            new BuildingUpgrade("3000000000", 3, 100),
            new BuildingUpgrade("24000000000000", 3, 200),
            new BuildingUpgrade(100000, 4, 1),
            new BuildingUpgrade(1000000, 4, 1),
            new BuildingUpgrade(10000000, 4, 10),
            new BuildingUpgrade(500000000, 4, 50),
            new BuildingUpgrade("10000000000", 4, 100),
            new BuildingUpgrade("80000000000000", 4, 200),
            new BuildingUpgrade(400000, 5, 1),
            new BuildingUpgrade(4000000, 5, 1),
            new BuildingUpgrade(40000000, 5, 10),
            new BuildingUpgrade(2000000000, 5, 50),
            new BuildingUpgrade("40000000000", 5, 100),
            new BuildingUpgrade("320000000000000", 5, 200),
            new BuildingUpgrade(2000000, 6, 1),
            new BuildingUpgrade(20000000, 6, 1),
            new BuildingUpgrade(200000000, 6, 10),
            new BuildingUpgrade("10000000000", 6, 50),
            new BuildingUpgrade("200000000000", 6, 100),
            new BuildingUpgrade("1600000000000000", 6, 200),
            new BuildingUpgrade(16666660, 7, 1),
            new BuildingUpgrade(166666600, 7, 1),
            new BuildingUpgrade(1666666000, 7, 10),
            new BuildingUpgrade("83333300000", 7, 50),
            new BuildingUpgrade("166666000000", 7, 100),
            new BuildingUpgrade("13333328000000000", 7, 200),
            new BuildingUpgrade(1234567890, 8, 1),
            new BuildingUpgrade("9876543210", 8, 1),
            new BuildingUpgrade("98765456789", 8, 10),
            new BuildingUpgrade("1234567890000", 8, 50),
            new BuildingUpgrade("123456789000000", 8, 100),
            new BuildingUpgrade("987654321000000000", 8, 200),
            new BuildingUpgrade("39999999990", 9, 1),
            new BuildingUpgrade("399999999900", 9, 1),
            new BuildingUpgrade("3999999999000", 9, 10),
            new BuildingUpgrade("199999999950000", 9, 50),
            new BuildingUpgrade("3999999999000000", 9, 100),
            new BuildingUpgrade("31999999992000000000", 9, 200),
            new BuildingUpgrade("750000000000", 10, 1),
            new BuildingUpgrade("7500000000000", 10, 1),
            new BuildingUpgrade("75000000000000", 10, 10),
            new BuildingUpgrade("3750000000000000", 10, 50),
            new BuildingUpgrade("75000000000000000", 10, 100),
            new BuildingUpgrade("600000000000000000000", 10, 200),
        };
    }
}
