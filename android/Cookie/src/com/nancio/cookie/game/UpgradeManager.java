package com.nancio.cookie.game;

import android.content.SharedPreferences;
import com.nancio.cookie.R;
import com.nancio.cookie.events.AchievementUnlockedEvent;
import com.nancio.cookie.events.AchievementUnlockedListener;
import com.nancio.cookie.events.BigCookieClickEvent;
import com.nancio.cookie.events.BigCookieClickListener;
import com.nancio.cookie.events.BuildingBoughtEvent;
import com.nancio.cookie.events.BuildingBoughtListener;
import com.nancio.cookie.events.BuildingCpsChangedEvent;
import com.nancio.cookie.events.CookieGetEvent;
import com.nancio.cookie.events.CookieGetListener;
import com.nancio.cookie.events.CpcChangedEvent;
import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.events.GoldenCookieClickEvent;
import com.nancio.cookie.events.GoldenCookieClickListener;
import com.nancio.cookie.events.ResetListener;
import com.nancio.cookie.events.UpgradeBoughtEvent;
import com.nancio.cookie.events.UpgradeBoughtListener;
import com.nancio.cookie.events.UpgradeSelectedEvent;
import com.nancio.cookie.events.UpgradeUnlockedEvent;
import com.nancio.cookie.views.UpgradesViewer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nancio on 24/06/15.
 */
public class UpgradeManager {
    public static int selected;

    public enum TYPES {
        CURSOR(0), GRANDMA(1), FARM(2), FACTORY(3), MINE(4), SHIPMENT(5),
        LAB(6), PORTAL(7), TIME(8), ANTIMATTER(9), PRISM(10),
        COOKIE(11), CLICK(12), GOLDEN(13), UPGRADE(14), ACHIEVEMENT(15);
        private final int id;
        TYPES(int id){
            this.id = id;
        }
    }

    static ArrayList<Upgrade> locked = new ArrayList<>();
    static ArrayList<Upgrade> unlocked = new ArrayList<>();
    static ArrayList<Upgrade> bought = new ArrayList<>();

    static UpgradesViewer unlockedView, boughtView;

    public static abstract class Upgrade{
        public final double price;
        protected String desc;
        public final int id, image, name, quote;
        public final TYPES type;
        protected int state;

        public Upgrade(double price, int id, int image, TYPES type) {
            this.price = price;
            this.id = id;
            this.image = image;
            this.name = Game.context.getResources().getIdentifier("up"+id+"_name", "string", Game.context.getPackageName());;
            this.quote = Game.context.getResources().getIdentifier("up"+id+"_quote", "string", Game.context.getPackageName());;
            this.type = type;
        }

        public abstract boolean unlock();

        protected abstract void addListener();

        public boolean buy(double cookies){
            if(cookies >= price){
                state = 2;
                return true;
            }
            return false;
        }

        public int getState(){
            return state;
        }

        public String getDescription(){
            return desc;
        }
    }

    public static class BuildingUpgrade extends Upgrade implements BuildingBoughtListener{

        protected int toUnlock;

        public BuildingUpgrade(double price, int id, TYPES type, int toUnlock) {
            super(price, id, Game.context.getResources().getIdentifier("up"+(type.id<10?"0":"")+type.id+"_"+toUnlock, "drawable", Game.context.getPackageName()), type);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.up_effect0), Game.context.getResources().getQuantityString(BuildingManager.NAMES[type.id%12], 2));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addBuildingBoughtListener(this);
        }

        @Override
        public void onBuildingBought(BuildingBoughtEvent e) {
            if(state!=0) {
                Game.eventsHandler.removeBuildingBoughtListener(this);
                return;
            }
            if (unlock()) {
                upgradeUnlocked(BuildingUpgrade.this);
                Game.eventsHandler.removeBuildingBoughtListener(this);
            }
        }

        @Override
        public boolean unlock() {
            if(BuildingManager.n[type.id] >= toUnlock) {
                state = 1;
                return true;
            }
            return false;
        }

        @Override
        public boolean buy(double cookies){
            if(!super.buy(cookies)) return false;
            Stats.baseCps += BuildingManager.getCps(type.id)*BuildingManager.n[type.id];
            BuildingManager.multis[type.id]*=2;
            Game.eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
            Game.eventsHandler.fireBuildingCpsChanged(
                    new BuildingCpsChangedEvent(type.id, BuildingManager.getNetoIncome(type.id))
            );
            return true;
        }
    }

    public static class CursorUpgrade extends BuildingUpgrade {

        private float cpsPerBuilding;

        public CursorUpgrade(double price, int id, int toUnlock, float cpsPerBuilding) {
            super(price, id, TYPES.CURSOR, toUnlock%80==0?toUnlock-40:toUnlock);
            this.toUnlock=toUnlock;
            this.cpsPerBuilding = cpsPerBuilding;
            if(cpsPerBuilding!=0){
                desc = String.format(Game.context.getString(R.string.up_effect01),cpsPerBuilding+"");
            }else desc = Game.context.getString(R.string.up_effect010);
        }

        @Override
        public boolean buy(double cookies) {
            if(cookies >= price){
                state = 2;
                if (cpsPerBuilding == 0) {
                    Stats.baseCps += BuildingManager.n[0] * BuildingManager.getCps(0);
                    BuildingManager.multis[0] *= 2;
                    BigCookie.multiplier *= 2;
                } else {
                    float n = 0;
                    for (int i = 1; i < 11; ++i) {
                        n += BuildingManager.n[i];
                    }
                    n *= cpsPerBuilding;
                    Stats.baseCps += BuildingManager.n[0]*n;
                    BuildingManager.cursorExtra += n;
                    BigCookie.extra += n;
                    BigCookie.foreachBuilding += cpsPerBuilding;
                }
                Game.eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
                Game.eventsHandler.fireBuildingCpsChanged(
                        new BuildingCpsChangedEvent(type.id, BuildingManager.getNetoIncome(type.id))
                );
                return true;
            }
            return false;
        }
    }

    public static class GrandmaUpgrade extends BuildingUpgrade{
        public GrandmaUpgrade(double price, int id, int toUnlock) {
            super(price, id, TYPES.GRANDMA, 15);
            this.toUnlock = toUnlock;
        }

        @Override
        public boolean unlock() {
            if(BuildingManager.n[toUnlock] >= 15) {
                state = 1;
                return true;
            }
            return false;
        }
    }

    public static class FlavoredUpgrade extends Upgrade implements CookieGetListener{

        double toUnlock;
        float multi;

        public FlavoredUpgrade(double price, int id, double toUnlock, float multi) {
            super(price, id, Game.context.getResources().getIdentifier("up11_"+id, "drawable", Game.context.getPackageName()), TYPES.COOKIE);
            this.toUnlock = toUnlock;
            this.multi = multi;
            desc = String.format(Game.context.getString(R.string.up_effect10), (int)(multi*100));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addCookieGetListener(this);
        }

        @Override
        public void onCookieGet(CookieGetEvent e) {
            if(state!=0) {
                Game.eventsHandler.removeCookieGetListener(this);
                return;
            }
            if (unlock()) {
                upgradeUnlocked(FlavoredUpgrade.this);
                Game.eventsHandler.removeCookieGetListener(this);
            }
        }

        @Override
        public boolean unlock() {
            if(Stats.getTotalCookies() >= toUnlock){
                state = 1;
                return true;
            }
            return false;
        }

        @Override
        public boolean buy(double cookies) {
            if(!super.buy(cookies)) return false;
            Stats.multiplier += multi;
            Game.eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
            return true;
        }
    }

    public static class BigCookieUpgrade extends Upgrade implements BigCookieClickListener{

        private double toUnlock;

        public BigCookieUpgrade(double price, int id, double toUnlock) {
            super(price, id, Game.context.getResources().getIdentifier("up12_"+((Math.log10(toUnlock)<7?3:Math.log10(toUnlock)>11?13:(int)Math.log10(toUnlock))+1), "drawable", Game.context.getPackageName()), TYPES.CLICK);
            this.toUnlock = toUnlock;
            desc = Game.context.getString(R.string.up_effect11);
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addBigCookieClickListener(this);
        }

        @Override
        public void onBigCookieClick(BigCookieClickEvent e) {
            if(state!=0) {
                Game.eventsHandler.removeBigCookieClickListener(this);
                return;
            }
            if(unlock()){
                upgradeUnlocked(BigCookieUpgrade.this);
                Game.eventsHandler.removeBigCookieClickListener(this);
            }
        }

        @Override
        public boolean unlock() {
            if(Stats.handmade >= toUnlock){
                state = 1;
                return true;
            }
            return false;
        }

        @Override
        public boolean buy(double cookies) {
            if(!super.buy(cookies)) return false;
            ++BigCookie.cpsPercent;
            Game.eventsHandler.fireCpcChanged(new CpcChangedEvent(BigCookie.getCpc()));
            return true;
        }
    }

    public static class GoldenUpgrade extends Upgrade implements GoldenCookieClickListener{

        private int toUnlock;
        private boolean halfSpawn;

        public GoldenUpgrade(double price, int id, int toUnlock, boolean halfSpawn) {
            super(price, id, R.drawable.up13, TYPES.GOLDEN);
            this.halfSpawn = halfSpawn;
            this.toUnlock = toUnlock;
            if(halfSpawn){
                desc = Game.context.getString(R.string.up_effect122);
            }else{
                desc = Game.context.getString(R.string.up_effect121);
            }
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addGoldenCookieClickListener(this);
        }

        @Override
        public void onGoldenCookieClick(GoldenCookieClickEvent e) {
            if(state!=0) {
                Game.eventsHandler.removeGoldenCookieClickListener(this);
                return;
            }
            if (unlock()) {
                upgradeUnlocked(GoldenUpgrade.this);
                Game.eventsHandler.removeGoldenCookieClickListener(this);
            }
        }

        @Override
        public boolean unlock() {
            if(Stats.goldenClicks >= toUnlock){
                state = 1;
                return true;
            }
            return false;
        }

        @Override
        public boolean buy(double cookies) {
            if(!super.buy(cookies)) return false;
            if(halfSpawn){
                GoldenCookie.minutes/=2;
                GoldenCookie.showSecs*=2;
                GoldenCookie.secondsLeft/=2;
            }else
                GoldenCookie.effectTimes *= 2;
            return true;
        }
    }

    public abstract static class BuyingUpgrade extends Upgrade implements UpgradeBoughtListener{
        private int[] toUnlock;

        public BuyingUpgrade(double price, int id, int image, int[] toUnlock) {
            super(price, id, image, TYPES.UPGRADE);
            this.toUnlock = toUnlock;
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addUpgradeBoughtListener(this);
        }

        @Override
        public void onUpgradeBought(UpgradeBoughtEvent e) {
            if(state!=0) {
                Game.eventsHandler.removeUpgradeBoughtListener(this);
                return;
            }
            if (unlock()) {
                upgradeUnlocked(BuyingUpgrade.this);
                Game.eventsHandler.removeUpgradeBoughtListener(this);
            }
        }

        @Override
        public boolean unlock() {
            for(int i=0; i<toUnlock.length; ++i)
                if(ups[toUnlock[i]].state!=2)
                    return false;
            state=1;
            return true;
        }
    }

    public static class FlavoredBUpgrade extends BuyingUpgrade {
        private float multi;

        public FlavoredBUpgrade(double price, int id, float multi, int[] toUnlock) {
            super(price, id, Game.context.getResources().getIdentifier("up11_"+id, "drawable", Game.context.getPackageName()), toUnlock);
            this.multi = multi;
            desc = String.format(Game.context.getString(R.string.up_effect10), (int)(multi*100));
        }

        @Override
        public boolean buy(double cookies) {
            if(cookies >= price){
                state = 2;
                Stats.multiplier += multi;
                Game.eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
                return true;
            }
            return false;
        }
    }

    public static class KittenUpgrade extends Upgrade implements AchievementUnlockedListener {
        int toUnlock;

        public KittenUpgrade(double price, int id, int toUnlock) {
            super(price, id, Game.context.getResources().getIdentifier("up15_"+toUnlock, "drawable", Game.context.getPackageName()), TYPES.ACHIEVEMENT);
            this.toUnlock = toUnlock;
            desc = Game.context.getString(R.string.up_effect15);
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addAchievementUnlockedListener(this);
        }

        @Override
        public void onAchievementUnlocked(AchievementUnlockedEvent e) {
            if(state!=0) {
                Game.eventsHandler.removeAchievementUnlockedListener(this);
                return;
            }
            if (unlock()) {
                upgradeUnlocked(KittenUpgrade.this);
                Game.eventsHandler.removeAchievementUnlockedListener(this);
            }
        }

        @Override
        public boolean unlock() {
            if(AchievementManager.getUnlockedLength() >= toUnlock){
                state = 1;
                return true;
            }
            return false;
        }

        @Override
        public boolean buy(double cookies) {
            if(!super.buy(cookies)) return false;
            ++Stats.kittens;
            Game.eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
            return true;
        }
    }

    public static class HeavenlyFlavoredUpgrade extends FlavoredUpgrade{
        double hcToUnlock;

        public HeavenlyFlavoredUpgrade(double price, int id, double cToUnlock, double hcToUnlock, float multi){
            super(price, id, cToUnlock, multi);
            this.hcToUnlock = hcToUnlock;
        }
    }


    static void init(SharedPreferences preferences){
        locked.clear();
        unlocked.clear();
        bought.clear();
        locked.addAll(Arrays.asList(ups));

        for(int i=locked.size()-1; i>=0; --i) {
            int iState = preferences.getInt("up" + i, 0);
            if (iState == 2) {
                locked.get(i).state = 2;
                bought.add(0, locked.remove(i));
            }else if(iState == 1 || locked.get(i).unlock()){
                locked.get(i).state = 1;
                upgradeUnlocked(locked.get(i));
            }else locked.get(i).addListener();
        }

//        onBuildingBought();
//        onHandmade();
//        onGoldenClick();
//        onCookieGet();
//        onUpgradeBought();
    }

    static void save(SharedPreferences.Editor preferences){
        for(int i=0; i<ups.length; ++i)
            preferences.putInt("up"+i, ups[i].state);
    }

    static void reset(){
        UpgradeManager.bought.clear();
        UpgradeManager.unlocked.clear();
        UpgradeManager.locked.clear();
        UpgradeManager.locked.addAll(Arrays.asList(UpgradeManager.ups));
        for(int i=0; i<UpgradeManager.ups.length; ++i) {
            UpgradeManager.ups[i].state = 0;
            if(ups[i].unlock()){
                upgradeUnlocked(ups[i]);
            }else ups[i].addListener();
        }
    }

    public static void setUpgradesViewer(UpgradesViewer view, int usage){
        if(usage==1) unlockedView = view;
        else if(usage==2) boughtView = view;
    }

    private static void upgradeUnlocked(Upgrade up){
        int j=0;
        for(; j<unlocked.size(); ++j)
            if(unlocked.get(j).price > up.price) {
                unlocked.add(j, up);
                break;
            }
        if(j == unlocked.size()) unlocked.add(up);
        locked.remove(up);
        Game.eventsHandler.fireUpgradeUnlocked(
                new UpgradeUnlockedEvent(
                        up.id, up.name, up.image,
                        up.quote, up.desc, unlocked.size()
                )
        );
    }

    /*public static void onBuildingBought(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type.id <= TYPES.PRISM.id && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).price > locked.get(i).price) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j = 0;
            }
        }
    }

    public static void onHandmade(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type == TYPES.CLICK && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).price > locked.get(i).price) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j=0;
            }
        }
    }

    public static void onGoldenClick(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type == TYPES.GOLDEN && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).price > locked.get(i).price) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j=0;
            }
        }
    }

    public static void onCookieGet(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type == TYPES.COOKIE && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).price > locked.get(i).price) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j=0;
            }
        }
    }

    public static void onUpgradeBought(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type == TYPES.UPGRADE && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).price > locked.get(i).price) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j=0;
            }
        }
    }

    public static void onAchievementUnlocked(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type == TYPES.ACHIEVEMENT && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).price > locked.get(i).price) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j=0;
            }
        }
    }*/

    public static Upgrade[] getUnlocked(){
        //onCookieGet();
        return unlocked.toArray(new Upgrade[unlocked.size()]);
    }

    public static int getUnlockedLength() { return unlocked.size(); }

    public static Upgrade[] getBought(){
        return bought.toArray(new Upgrade[bought.size()]);
    }

    public static int getBoughtLength(){
        return bought.size();
    }

    public static void select(int unlockedIndex) {
        //BuildingManager.select(-1);
        selected = unlockedIndex;
        //Game.bselected = false;
        Game.eventsHandler.fireUpgradeSelected(
                new UpgradeSelectedEvent(
                        unlocked.get(selected).state,
                        unlocked.get(selected).id,
                        unlocked.get(selected).image,
                        unlocked.get(selected).name,
                        unlocked.get(selected).quote,
                        unlocked.get(selected).desc
                )
        );
    }

    public static void selectBought(int boughtIndex){
        Game.eventsHandler.fireUpgradeSelected(
                new UpgradeSelectedEvent(
                        bought.get(boughtIndex).state,
                        bought.get(boughtIndex).id,
                        bought.get(boughtIndex).image,
                        bought.get(boughtIndex).name,
                        bought.get(boughtIndex).quote,
                        bought.get(boughtIndex).desc
                )
        );
    }

    public static int buy(){
        if(unlocked.get(selected).buy(Stats.cookies)){
            int j=0;
            for(; j<bought.size(); ++j)
                if(bought.get(j).id > unlocked.get(selected).id) {
                    bought.add(j, unlocked.remove(selected));
                    break;
                }
            if(j == bought.size()) bought.add(unlocked.remove(selected));
            Stats.cookies -= bought.get(j).price;
            Stats.spent += bought.get(j).price;
//            onUpgradeBought();
//            AchievementManager.onUpgradeBought();
//            AchievementManager.onCpsChange();
//            Game.context.repaintSubtitle();
//            unlockedView.update();
//            boughtView.update();
            Game.eventsHandler.fireUpgradeBought(
                    new UpgradeBoughtEvent(
                            bought.get(j).id,
                            bought.get(j).image,
                            bought.get(j).name,
                            bought.get(j).quote,
                            bought.get(j).desc,
                            bought.get(j).type,
                            bought.size())
            );
            return selected;
        }
        return -1;
    }

    public static final Upgrade[] ups = new Upgrade[] {
            new CursorUpgrade(100, 0, 1, 0),
            new CursorUpgrade(400, 1, 1, 0),
            new CursorUpgrade(10000, 2, 10, 0),
            new CursorUpgrade(500000, 3, 20, 0.1f),
            new CursorUpgrade(50000000, 4, 40, 0.5f),
            new CursorUpgrade(500000000, 5, 80, 2f),
            new CursorUpgrade(5000000000.0, 6, 120, 10f),
            new CursorUpgrade(50000000000.0, 7, 160, 20f),
            new CursorUpgrade(50000000000000.0, 8, 200, 100f),
            new CursorUpgrade(500000000000000.0, 9, 240, 200f),
            new CursorUpgrade(5000000000000000.0, 10, 280, 400f),
            new CursorUpgrade(50000000000000000.0, 11, 320, 800f),
            new BuildingUpgrade(1000, 12, TYPES.GRANDMA, 1),
            new BuildingUpgrade(10000, 13, TYPES.GRANDMA, 1),
            new BuildingUpgrade(10000, 14, TYPES.GRANDMA, 10),
            new BuildingUpgrade(5000000, 15, TYPES.GRANDMA, 50),
            new BuildingUpgrade(100000000, 16, TYPES.GRANDMA, 100),
            new BuildingUpgrade(800000000000.0, 17, TYPES.GRANDMA, 200),
            new GrandmaUpgrade(50000, 18, 2),
            new GrandmaUpgrade(300000, 19, 3),
            new GrandmaUpgrade(1000000, 20, 4),
            new GrandmaUpgrade(4000000, 21, 5),
            new GrandmaUpgrade(20000000, 22, 6),
            new GrandmaUpgrade(166666600, 23, 7),
            new GrandmaUpgrade(12345678900.0, 24, 8),
            new GrandmaUpgrade(399999999900.0, 25, 9),
            new GrandmaUpgrade(7500000000000.0, 26, 10),
            new BuildingUpgrade(5000, 27, TYPES.FARM, 1),
            new BuildingUpgrade(50000, 28, TYPES.FARM, 1),
            new BuildingUpgrade(500000, 29, TYPES.FARM, 10),
            new BuildingUpgrade(25000000, 30, TYPES.FARM, 50),
            new BuildingUpgrade(500000000, 31, TYPES.FARM, 100),
            new BuildingUpgrade(4000000000000.0, 32, TYPES.FARM, 200),
            new BuildingUpgrade(30000, 33, TYPES.FACTORY, 1),
            new BuildingUpgrade(300000, 34, TYPES.FACTORY, 1),
            new BuildingUpgrade(3000000, 35, TYPES.FACTORY, 10),
            new BuildingUpgrade(150000000, 36, TYPES.FACTORY, 50),
            new BuildingUpgrade(3000000000.0, 37, TYPES.FACTORY, 100),
            new BuildingUpgrade(24000000000000.0, 38, TYPES.FACTORY, 200),
            new BuildingUpgrade(100000, 39, TYPES.MINE, 1),
            new BuildingUpgrade(1000000, 40, TYPES.MINE, 1),
            new BuildingUpgrade(10000000, 41, TYPES.MINE, 10),
            new BuildingUpgrade(500000000, 42, TYPES.MINE, 50),
            new BuildingUpgrade(10000000000.0, 43, TYPES.MINE, 100),
            new BuildingUpgrade(80000000000000.0, 44, TYPES.MINE, 200),
            new BuildingUpgrade(400000, 45, TYPES.SHIPMENT, 1),
            new BuildingUpgrade(4000000, 46, TYPES.SHIPMENT, 1),
            new BuildingUpgrade(40000000, 47, TYPES.SHIPMENT, 10),
            new BuildingUpgrade(2000000000, 48, TYPES.SHIPMENT, 50),
            new BuildingUpgrade(40000000000.0, 49, TYPES.SHIPMENT, 100),
            new BuildingUpgrade(320000000000000.0, 50, TYPES.SHIPMENT, 200),
            new BuildingUpgrade(2000000, 51, TYPES.LAB, 1),
            new BuildingUpgrade(20000000, 52, TYPES.LAB, 1),
            new BuildingUpgrade(200000000, 53, TYPES.LAB, 10),
            new BuildingUpgrade(10000000000.0, 54, TYPES.LAB, 50),
            new BuildingUpgrade(200000000000.0, 55, TYPES.LAB, 100),
            new BuildingUpgrade(1600000000000000.0, 56, TYPES.LAB, 200),
            new BuildingUpgrade(16666660, 57, TYPES.PORTAL, 1),
            new BuildingUpgrade(166666600, 58, TYPES.PORTAL, 1),
            new BuildingUpgrade(1666666000, 59, TYPES.PORTAL, 10),
            new BuildingUpgrade(83333300000.0, 60, TYPES.PORTAL, 50),
            new BuildingUpgrade(1666666000000.0, 61, TYPES.PORTAL, 100),
            new BuildingUpgrade(13333328000000000.0, 62, TYPES.PORTAL, 200),
            new BuildingUpgrade(1234567890, 63, TYPES.TIME, 1),
            new BuildingUpgrade(9876543210.0, 64, TYPES.TIME, 1),
            new BuildingUpgrade(98765456789.0, 65, TYPES.TIME, 10),
            new BuildingUpgrade(1234567890000.0, 66, TYPES.TIME, 50),
            new BuildingUpgrade(123456789000000.0, 67, TYPES.TIME, 100),
            new BuildingUpgrade(987654321000000000.0, 68, TYPES.TIME, 200),
            new BuildingUpgrade(39999999990.0, 69, TYPES.ANTIMATTER, 1),
            new BuildingUpgrade(399999999900.0, 70, TYPES.ANTIMATTER, 1),
            new BuildingUpgrade(3999999999000.0, 71, TYPES.ANTIMATTER, 10),
            new BuildingUpgrade(199999999950000.0, 72, TYPES.ANTIMATTER, 50),
            new BuildingUpgrade(3999999999000000.0, 73, TYPES.ANTIMATTER, 100),
            new BuildingUpgrade(31999999992000000000.0, 74, TYPES.ANTIMATTER, 200),
            new BuildingUpgrade(750000000000.0, 75, TYPES.PRISM, 1),
            new BuildingUpgrade(7500000000000.0, 76, TYPES.PRISM, 1),
            new BuildingUpgrade(75000000000000.0, 77, TYPES.PRISM, 10),
            new BuildingUpgrade(3750000000000000.0, 78, TYPES.PRISM, 50),
            new BuildingUpgrade(75000000000000000.0, 79, TYPES.PRISM, 100),
            new BuildingUpgrade(600000000000000000000.0, 80, TYPES.PRISM, 200),
            new BigCookieUpgrade(50000, 81, 1000),
            new BigCookieUpgrade(5000000, 82, 100000),
            new BigCookieUpgrade(500000000.0, 83, 10000000),
            new BigCookieUpgrade(50000000000.0, 84, 1000000000),
            new BigCookieUpgrade(5000000000000.0, 85, 100000000000.0),
            new BigCookieUpgrade(500000000000000.0, 86, 10000000000000.0),
            new BigCookieUpgrade(50000000000000000.0, 87, 1000000000000000.0),
            new GoldenUpgrade(777777777, 88, 7, true),
            new GoldenUpgrade(77777777777.0, 89, 27, true),
            new GoldenUpgrade(77777777777777.0, 90, 77, false),
            ///Oatmeal raisin - sugar
            new FlavoredUpgrade(99999999, 91, 9999999, 0.05f),
            new FlavoredUpgrade(99999999, 92, 9999999, 0.05f),
            new FlavoredUpgrade(99999999, 93, 9999999, 0.05f),
            new FlavoredUpgrade(99999999, 94, 9999999, 0.05f),
            ///Coconut - Macadamia nut
            new FlavoredUpgrade(999999999, 95, 99999999, 0.05f),
            new FlavoredUpgrade(999999999, 96, 99999999, 0.05f),
            new FlavoredUpgrade(999999999, 97, 99999999, 0.05f),
            ///Double chip - All chocolate
            new FlavoredUpgrade(99999999999.0, 98, 999999999, 0.1f),
            new FlavoredUpgrade(99999999999.0, 99, 999999999, 0.1f),
            new FlavoredUpgrade(99999999999.0, 100, 999999999, 0.1f),
            //Dark chocolate coated - white chocolate coated
            new FlavoredUpgrade(999999999999.0, 101, 9999999999.0, 0.15f),
            new FlavoredUpgrade(999999999999.0, 102, 9999999999.0, 0.15f),
            //Eclipse - Zebra
            new FlavoredUpgrade(9999999999999.0, 103, 99999999999.0, 0.15f),
            new FlavoredUpgrade(9999999999999.0, 104, 99999999999.0, 0.15f),
            //Snickedoodles - Macaroons
            new FlavoredUpgrade(9999999999999.0, 105, 99999999999.0, 0.15f),
            new FlavoredUpgrade(9999999999999.0, 106, 99999999999.0, 0.15f),
            new FlavoredUpgrade(9999999999999.0, 107, 99999999999.0, 0.15f),
            //Madeleines - Sablés
            new FlavoredUpgrade(199999999999999.0, 108, 9999999999999.0, 0.2f),
            new FlavoredUpgrade(199999999999999.0, 109, 9999999999999.0, 0.2f),
            new FlavoredUpgrade(199999999999999.0, 110, 9999999999999.0, 0.2f),
            new FlavoredUpgrade(199999999999999.0, 111, 9999999999999.0, 0.2f),
            //Empire biscuits - Round chocolate british tea biscuits with heart motif
            new FlavoredBUpgrade(100000000000000.0, 112, 0.15f, new int[]{105, 106, 107}),
            new FlavoredBUpgrade(100000000000000.0, 113, 0.15f, new int[]{112}),
            new FlavoredBUpgrade(100000000000000.0, 114, 0.15f, new int[]{113}),
            new FlavoredBUpgrade(100000000000000.0, 115, 0.15f, new int[]{114}),
            new FlavoredBUpgrade(100000000000000.0, 116, 0.15f, new int[]{115}),
            new FlavoredBUpgrade(100000000000000.0, 117, 0.15f, new int[]{116}),
            new FlavoredBUpgrade(100000000000000.0, 118, 0.15f, new int[]{117}),
            //Kittens
            new KittenUpgrade(900000, 119, 13),
            new KittenUpgrade(900000000, 120, 25),
            new KittenUpgrade(9000000000000.0, 121, 50),
            new KittenUpgrade(9000000000000000.0, 122, 75),
            new KittenUpgrade(90000000000000000000.0, 123, 100),
            //Heavenly Flavored
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 124, 10000000000000.0, 1, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 125, 10000000000000.0, 2, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 126, 10000000000000.0, 3, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 127, 10000000000000.0, 4, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 128, 10000000000000.0, 10, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 129, 10000000000000.0, 100, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 130, 10000000000000.0, 500, 0.25f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 131, 10000000000000.0, 2000, 0.25f),
//            //Color cookies
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 132, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 133, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 134, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 135, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 136, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 137, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 138, 10000000000000.0, 10000, 0.3f),
//            new HeavenlyFlavoredUpgrade(1000000000000000.0, 139, 10000000000000.0, 10000, 0.3f),
            //Heavenly Upgrades
    };

    public static int UPGRADE_COUNT = ups.length;
}
