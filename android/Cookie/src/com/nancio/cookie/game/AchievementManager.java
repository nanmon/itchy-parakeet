package com.nancio.cookie.game;

import android.content.SharedPreferences;

import com.nancio.cookie.Cookiefloat;
import com.nancio.cookie.R;
import com.nancio.cookie.events.AchievementSelectedEvent;
import com.nancio.cookie.events.AchievementUnlockedEvent;
import com.nancio.cookie.events.BigCookieClickEvent;
import com.nancio.cookie.events.BigCookieClickListener;
import com.nancio.cookie.events.BuildingBoughtEvent;
import com.nancio.cookie.events.BuildingBoughtListener;
import com.nancio.cookie.events.CookieGetEvent;
import com.nancio.cookie.events.CookieGetListener;
import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.events.CpsChangedListener;
import com.nancio.cookie.events.GoldenCookieClickEvent;
import com.nancio.cookie.events.GoldenCookieClickListener;
import com.nancio.cookie.events.ResetEvent;
import com.nancio.cookie.events.ResetListener;
import com.nancio.cookie.events.UpgradeBoughtEvent;
import com.nancio.cookie.events.UpgradeBoughtListener;
import com.nancio.cookie.views.UpgradesViewer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nancio on 30/06/15.
 */
public class AchievementManager {

    public enum TYPES {
        CURSOR(0), GRANDMA(1), FARM(2), FACTORY(3), MINE(4), SHIPMENT(5),
        LAB(6), PORTAL(7), TIME(8), ANTIMATTER(9), PRISM(10),
        COOKIE(11), CLICK(12), GOLDEN(13), CPS(14), BUILDING(15), UPGRADE(16), RESET(17);
        private int id;
        TYPES(int id){
            this.id = id;
        }
    }

    private static ArrayList<Achievement> locked = new ArrayList<>(), unlocked = new ArrayList<>();
    static UpgradesViewer view;

    public static abstract class Achievement{
        private static int idAssigner = 0;
        protected String desc;
        public final int id, image, name;
        TYPES type;
        protected boolean unlocked = false;

        public Achievement(int image, TYPES type) {
            this.id = idAssigner++;
            this.image = image;
            this.name = Game.context.getResources().getIdentifier("achie" + id, "string", Game.context.getPackageName());
            this.type = type;

            addListener();
        }

        public abstract boolean unlock();

        protected abstract void addListener();

        public String getDescription(){ return desc; }

        /*protected void sendNotification(){
            Game.notifView.notificate(image, Game.context.getString(name), desc, true);
            Stats.milkyView.milkChanged();
        }*/
    }

    public static class CookieAchievement extends Achievement implements CookieGetListener{
        private double toUnlock;

        public CookieAchievement(double toUnlock, int img) {
            super(img, TYPES.COOKIE);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc11), Cookiefloat.format(toUnlock, true));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addCookieGetListener(this);
        }

        @Override
        public boolean unlock() {
            if(Stats.getTotalCookies() >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onCookieGet(CookieGetEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeCookieGetListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(CookieAchievement.this);
                Game.eventsHandler.removeCookieGetListener(this);
            }
        }
    }

    public static class CpsAchievement extends Achievement implements CpsChangedListener{
        private double toUnlock;

        public CpsAchievement(double toUnlock, int img) {
            super(img, TYPES.CPS);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc14), Cookiefloat.format(toUnlock, true));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addCpsChangedListener(this);
        }

        @Override
        public boolean unlock() {
            if(Stats.getCps() >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onCpsChanged(CpsChangedEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeCpsChangedListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(CpsAchievement.this);
                Game.eventsHandler.removeCpsChangedListener(this);
            }
        }
    }

    public static class ClickAchievement extends Achievement implements BigCookieClickListener{
        private double toUnlock;

        public ClickAchievement(double toUnlock, int img) {
            super(img, TYPES.CLICK);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc12), Cookiefloat.format(toUnlock, true));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addBigCookieClickListener(this);
        }

        @Override
        public boolean unlock() {
            if(Stats.getHandmade() >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onBigCookieClick(BigCookieClickEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeBigCookieClickListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(ClickAchievement.this);
                Game.eventsHandler.removeBigCookieClickListener(this);
            }
        }
    }

    public static class BuildingAchievement extends Achievement implements BuildingBoughtListener{
        private int toUnlock;

        public BuildingAchievement(TYPES type, int toUnlock, int img) {
            super(img, type);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc0), toUnlock, Game.context.getResources().getQuantityString(BuildingManager.NAMES[type.id], toUnlock));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addBuildingBoughtListener(this);
        }

        @Override
        public boolean unlock() {
            if(BuildingManager.n[type.id] >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onBuildingBought(BuildingBoughtEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeBuildingBoughtListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(BuildingAchievement.this);
                Game.eventsHandler.removeBuildingBoughtListener(this);
            }
        }
    }

    public static class BuildingAchievement2 extends Achievement implements BuildingBoughtListener{
        private int toUnlock;

        public BuildingAchievement2(int toUnlock, int img) {
            super(img, TYPES.BUILDING);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc15), toUnlock);
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addBuildingBoughtListener(this);
        }

        @Override
        public boolean unlock() {
            if(BuildingManager.getTotalBuildings() >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onBuildingBought(BuildingBoughtEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeBuildingBoughtListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(BuildingAchievement2.this);
                Game.eventsHandler.removeBuildingBoughtListener(this);
            }
        }
    }

    public static class BuildingAchievement3 extends Achievement implements BuildingBoughtListener{
        private int toUnlock;

        public BuildingAchievement3(int toUnlock, int img) {
            super(img, TYPES.BUILDING);
            this.toUnlock = toUnlock > UpgradeManager.UPGRADE_COUNT
                ? UpgradeManager.UPGRADE_COUNT
                : toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc15_5), toUnlock);
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addBuildingBoughtListener(this);
        }

        @Override
        public boolean unlock() {
            for(int i=0; i<BuildingManager.NUM_BUILDINGS; ++i)
                if(BuildingManager.n[i] < toUnlock)
                    return false;
            //sendNotification();
            return unlocked = true;
        }

        @Override
        public void onBuildingBought(BuildingBoughtEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeBuildingBoughtListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(BuildingAchievement3.this);
                Game.eventsHandler.removeBuildingBoughtListener(this);
            }
        }
    }

    public static class UpgradeAchievement extends Achievement implements UpgradeBoughtListener{
        private int toUnlock;

        public UpgradeAchievement(int toUnlock, int img) {
            super(img, TYPES.UPGRADE);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc16), toUnlock);
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addUpgradeBoughtListener(this);
        }

        @Override
        public boolean unlock() {
           if(UpgradeManager.getBoughtLength() >= toUnlock) {
               unlocked = true;
               //sendNotification();
           }
            return unlocked;
        }

        @Override
        public void onUpgradeBought(UpgradeBoughtEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeUpgradeBoughtListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(UpgradeAchievement.this);
                Game.eventsHandler.removeUpgradeBoughtListener(this);
            }
        }
    }

    public static class GoldenAchievement extends Achievement implements GoldenCookieClickListener{
        private int toUnlock;

        public GoldenAchievement(int toUnlock) {
            super(R.drawable.up13, TYPES.GOLDEN);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc13), toUnlock);
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addGoldenCookieClickListener(this);
        }

        @Override
        public boolean unlock() {
            if(Stats.getAllTimeGoldenClicks() >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onGoldenCookieClick(GoldenCookieClickEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeGoldenCookieClickListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(GoldenAchievement.this);
                Game.eventsHandler.removeGoldenCookieClickListener(this);
            }
        }
    }

    public static class ResetAchievement extends Achievement implements ResetListener{
        private double toUnlock;

        public ResetAchievement(double toUnlock, int img) {
            super(img, TYPES.RESET);
            this.toUnlock = toUnlock;
            desc = String.format(Game.context.getString(R.string.achie_desc17), Cookiefloat.format(toUnlock, true));
        }

        @Override
        protected void addListener() {
            Game.eventsHandler.addResetListener(this);
        }

        @Override
        public boolean unlock() {
            if(Stats.profited >= toUnlock) {
                unlocked = true;
                //sendNotification();
            }
            return unlocked;
        }

        @Override
        public void onReset(ResetEvent e) {
            if(unlocked) {
                Game.eventsHandler.removeResetListener(this);
                return;
            }
            if(unlock()){
                achievementUnlocked(ResetAchievement.this);
                Game.eventsHandler.removeResetListener(this);
            }
        }
    }

    //BuildingAchievement4 : make x cookies with y building
    //Own at least 7 grandma types
    //Have at least 2^i of each i building
    //Have at least (i+1)*10 of each i building
    //Cookie dunker
    //make 1million cookies with only 15 cookie clicks

    public static void init(SharedPreferences preferences){
        locked.clear();
        unlocked.clear();
        locked.addAll(Arrays.asList(achies));

        for(int i=locked.size(); i>=0; --i)
            if(preferences.getInt("achie"+i, 0)==2) {
                locked.get(i).unlocked = true;
                unlocked.add(0, locked.remove(i));
            }
        //EVENT?:::UpgradeManager.onAchievementUnlocked();
    }

    static void save(SharedPreferences.Editor preferences){
        for(int i=0; i<unlocked.size(); ++i)
            preferences.putInt("achie"+unlocked.get(i).id, 2);
    }

    public static void setView(UpgradesViewer view){
        AchievementManager.view = view;
    }

    public static Achievement[] getUnlocked(){
        return unlocked.toArray(new Achievement[unlocked.size()]);
    }

    public static int getUnlockedLength() { return unlocked.size(); }

    private static void achievementUnlocked(Achievement achie){
        int j=0;
        for(; j<unlocked.size(); ++j)
            if(unlocked.get(j).id > achie.id) {
                unlocked.add(j, achie);
                break;
            }
        if(j == unlocked.size()) unlocked.add(achie);
        locked.remove(achie);
        Game.eventsHandler.fireAchievementUnlocked(
                new AchievementUnlockedEvent(
                        achie.id,
                        achie.name,
                        achie.image,
                        achie.desc,
                        achie.type,
                        unlocked.size()
                )
        );
    }

    public static void select(int index){
        Game.eventsHandler.fireAchievementSelected(
                new AchievementSelectedEvent(
                        unlocked.get(index).id,
                        unlocked.get(index).name,
                        unlocked.get(index).image,
                        unlocked.get(index).desc
                )
        );
    }

    /*public static void onCookieGet(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type==TYPES.COOKIE && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j = 0;
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }

    public static void onCpsChange(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type==TYPES.CPS && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j = 0;
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }

    public static void onBigCookieClick(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type==TYPES.CLICK && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j = 0;
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }

    public static void onBuildingBought(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if((locked.get(i).type.id<11 || locked.get(i).type==TYPES.BUILDING) && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j = 0;
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }

    public static void onUpgradeBought(){
        for(int i=0, j; i<locked.size(); ++i){
            if(locked.get(i).type==TYPES.UPGRADE && locked.get(i).unlock()){
                for(j=0; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }

    public static void onGoldenClick(){
        for(int i=0, j=0; i<locked.size(); ++i){
            if(locked.get(i).type==TYPES.GOLDEN && locked.get(i).unlock()){
                for(; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                j = 0;
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }

    public static void onReset(){
        for(int i=0, j; i<locked.size(); ++i){
            if(locked.get(i).type==TYPES.RESET && locked.get(i).unlock()){
                for(j=0; j<unlocked.size(); ++j)
                    if(unlocked.get(j).id > locked.get(i).id) {
                        unlocked.add(j, locked.remove(i--));
                        break;
                    }
                if(j == unlocked.size()) unlocked.add(locked.remove(i--));
                Stats.view.updateMilk();
                UpgradeManager.onAchievementUnlocked();
            }
        }
    }*/

    static Achievement[] achies = new Achievement[] {
            new CookieAchievement(1, R.drawable.achie11_1),
            new CookieAchievement(100, R.drawable.achie11_2),
            new CookieAchievement(1000, R.drawable.achie11_3),
            new CookieAchievement(10000, R.drawable.achie11_4),
            new CookieAchievement(100000, R.drawable.achie11_5),
            new CookieAchievement(1000000, R.drawable.achie11_6),
            new CookieAchievement(10000000, R.drawable.achie11_7),
            new CookieAchievement(100000000, R.drawable.achie11_8),
            new CookieAchievement(1000000000, R.drawable.achie11_9),
            new CookieAchievement(10000000000.0, R.drawable.achie11_10),
            new CookieAchievement(100000000000.0, R.drawable.achie11_11),
            new CookieAchievement(1000000000000.0, R.drawable.achie11_11),
            new CookieAchievement(10000000000000.0, R.drawable.achie11_11),
            new CookieAchievement(100000000000000.0, R.drawable.achie11_11),
            new CookieAchievement(1000000000000000.0, R.drawable.achie11_11),
            new CookieAchievement(10000000000000000.0, R.drawable.achie11_12),
            new CpsAchievement(1, R.drawable.achie11_1),
            new CpsAchievement(10, R.drawable.achie11_2),
            new CpsAchievement(100, R.drawable.achie11_3),
            new CpsAchievement(1000, R.drawable.achie11_4),
            new CpsAchievement(10000, R.drawable.achie11_5),
            new CpsAchievement(1000000, R.drawable.achie11_6),
            new CpsAchievement(10000000, R.drawable.achie11_7),
            new CpsAchievement(100000000, R.drawable.achie11_8),
            new CpsAchievement(10000000000.0, R.drawable.achie11_9),
            new CpsAchievement(1000000000000.0, R.drawable.achie11_10),
            new ClickAchievement(1000, R.drawable.up12_4),
            new ClickAchievement(100000, R.drawable.up12_8),
            new ClickAchievement(10000000, R.drawable.up12_8),
            new ClickAchievement(1000000000, R.drawable.up12_10),
            new ClickAchievement(100000000000.0, R.drawable.up12_12),
            new ClickAchievement(10000000000000.0, R.drawable.up12_14),
            new ClickAchievement(1000000000000000.0, R.drawable.up12_14),
            new BuildingAchievement(TYPES.CURSOR, 1, R.drawable.up00_1),
            new BuildingAchievement(TYPES.CURSOR, 2, R.drawable.up00_10),
            new BuildingAchievement(TYPES.CURSOR, 50, R.drawable.up00_20),
            new BuildingAchievement(TYPES.CURSOR, 100, R.drawable.up00_40),
            new BuildingAchievement(TYPES.CURSOR, 200, R.drawable.up00_120),
            new BuildingAchievement(TYPES.CURSOR, 300, R.drawable.up00_200),
            new BuildingAchievement(TYPES.CURSOR, 400, R.drawable.up00_280),
            new BuildingAchievement(TYPES.GRANDMA, 1, R.drawable.up01_1),
            new BuildingAchievement(TYPES.GRANDMA, 50, R.drawable.up01_10),
            new BuildingAchievement(TYPES.GRANDMA, 100, R.drawable.up01_50),
            new BuildingAchievement(TYPES.GRANDMA, 150, R.drawable.up01_100),
            new BuildingAchievement(TYPES.GRANDMA, 200, R.drawable.up01_200),
            new BuildingAchievement(TYPES.GRANDMA, 250, R.drawable.up01_200),
            //Own at least 7 grandma types
            new BuildingAchievement(TYPES.FARM, 1, R.drawable.up02_1),
            new BuildingAchievement(TYPES.FARM, 50, R.drawable.up02_10),
            new BuildingAchievement(TYPES.FARM, 100, R.drawable.up02_50),
            new BuildingAchievement(TYPES.FARM, 150, R.drawable.up02_100),
            new BuildingAchievement(TYPES.FARM, 200, R.drawable.up02_200),
            new BuildingAchievement(TYPES.FACTORY, 1, R.drawable.up03_1),
            new BuildingAchievement(TYPES.FACTORY, 50, R.drawable.up03_10),
            new BuildingAchievement(TYPES.FACTORY, 100, R.drawable.up03_50),
            new BuildingAchievement(TYPES.FACTORY, 150, R.drawable.up03_100),
            new BuildingAchievement(TYPES.FACTORY, 200, R.drawable.up03_200),
            new BuildingAchievement(TYPES.MINE, 1, R.drawable.up04_1),
            new BuildingAchievement(TYPES.MINE, 50, R.drawable.up04_10),
            new BuildingAchievement(TYPES.MINE, 100, R.drawable.up04_50),
            new BuildingAchievement(TYPES.MINE, 150, R.drawable.up04_100),
            new BuildingAchievement(TYPES.MINE, 200, R.drawable.up04_200),
            new BuildingAchievement(TYPES.SHIPMENT, 1, R.drawable.up05_1),
            new BuildingAchievement(TYPES.SHIPMENT, 50, R.drawable.up05_10),
            new BuildingAchievement(TYPES.SHIPMENT, 100, R.drawable.up05_50),
            new BuildingAchievement(TYPES.SHIPMENT, 150, R.drawable.up05_100),
            new BuildingAchievement(TYPES.SHIPMENT, 200, R.drawable.up05_200),
            new BuildingAchievement(TYPES.LAB, 1, R.drawable.up06_1),
            new BuildingAchievement(TYPES.LAB, 50, R.drawable.up06_10),
            new BuildingAchievement(TYPES.LAB, 100, R.drawable.up06_50),
            new BuildingAchievement(TYPES.LAB, 150, R.drawable.up06_100),
            new BuildingAchievement(TYPES.LAB, 200, R.drawable.up06_200),
            new BuildingAchievement(TYPES.PORTAL, 1, R.drawable.up07_1),
            new BuildingAchievement(TYPES.PORTAL, 50, R.drawable.up07_10),
            new BuildingAchievement(TYPES.PORTAL, 100, R.drawable.up07_50),
            new BuildingAchievement(TYPES.PORTAL, 150, R.drawable.up07_100),
            new BuildingAchievement(TYPES.PORTAL, 200, R.drawable.up07_200),
            new BuildingAchievement(TYPES.TIME, 1, R.drawable.up08_1),
            new BuildingAchievement(TYPES.TIME, 50, R.drawable.up08_10),
            new BuildingAchievement(TYPES.TIME, 100, R.drawable.up08_50),
            new BuildingAchievement(TYPES.TIME, 150, R.drawable.up08_100),
            new BuildingAchievement(TYPES.TIME, 200, R.drawable.up08_200),
            new BuildingAchievement(TYPES.ANTIMATTER, 1, R.drawable.up09_1),
            new BuildingAchievement(TYPES.ANTIMATTER, 50, R.drawable.up09_10),
            new BuildingAchievement(TYPES.ANTIMATTER, 100, R.drawable.up09_50),
            new BuildingAchievement(TYPES.ANTIMATTER, 150, R.drawable.up09_100),
            new BuildingAchievement(TYPES.ANTIMATTER, 200, R.drawable.up09_200),
            new BuildingAchievement(TYPES.PRISM, 1, R.drawable.up10_1),
            new BuildingAchievement(TYPES.PRISM, 50, R.drawable.up10_10),
            new BuildingAchievement(TYPES.PRISM, 100, R.drawable.up10_50),
            new BuildingAchievement(TYPES.PRISM, 150, R.drawable.up10_100),
            new BuildingAchievement(TYPES.PRISM, 200, R.drawable.up10_200),
            new BuildingAchievement2(100, R.drawable.achie15_1),
            new BuildingAchievement2(400, R.drawable.achie15_2),
            new BuildingAchievement2(800, R.drawable.achie15_3),
            new BuildingAchievement2(1500, R.drawable.achie15_3),
            new UpgradeAchievement(20, R.drawable.achie16_1),
            new UpgradeAchievement(50, R.drawable.achie16_2),
            new UpgradeAchievement(100, R.drawable.achie16_3),
            new UpgradeAchievement(150, R.drawable.achie16_3),
            new BuildingAchievement3(1, R.drawable.achie15_5_1),
            new BuildingAchievement3(100, R.drawable.achie15_5_2),
            new BuildingAchievement3(150, R.drawable.achie15_5_2),
            new BuildingAchievement3(200, R.drawable.achie15_5_2),
            //Have at least 2^i of each i building
            //Have at least (i+1)*10 of each i building
            new GoldenAchievement(1),
            new GoldenAchievement(7),
            new GoldenAchievement(27),
            new GoldenAchievement(77),
            new GoldenAchievement(777),
            new GoldenAchievement(7777),
            //Cookie dunker
            //make 1million cookies with only 15 cookie clicks
            new ResetAchievement(1000000, R.drawable.achie17_1),
            new ResetAchievement(1000000000, R.drawable.achie17_1),
            new ResetAchievement(1000000000000.0, R.drawable.achie17_1),
            new ResetAchievement(1000000000000000.0, R.drawable.achie17_2),
            new ResetAchievement(1000000000000000000.0, R.drawable.achie17_2),
            new ResetAchievement(1000000000000000000000.0, R.drawable.achie17_2),
            new ResetAchievement(1000000000000000000000000.0, R.drawable.achie17_3),
            new ResetAchievement(1000000000000000000000000000.0, R.drawable.achie17_3)
    };

    public static final int ACHIEVEMENT_COUNT = achies.length;
}
