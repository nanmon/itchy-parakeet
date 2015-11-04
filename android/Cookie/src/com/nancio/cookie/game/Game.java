package com.nancio.cookie.game;

import android.content.Context;
import android.content.SharedPreferences;

import com.nancio.cookie.MainActivity;
import com.nancio.cookie.events.CookieEventsHandler;
import com.nancio.cookie.events.CookieGetEvent;
import com.nancio.cookie.events.CpcChangedEvent;
import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.events.ResetEvent;
import com.nancio.cookie.views.NotificationView;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by nancio on 4/01/15.
 */
public class Game {

    static MainActivity context;
    static NotificationView notifView;
    private static final int VERSION = 2;
    public static final double times = 0.05;
    public static boolean bselected = true;
    public static CookieEventsHandler eventsHandler = new CookieEventsHandler();
    
    private static Runnable frenzyOverRunnable = new Runnable() {
        @Override
        public void run() {
            eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
        }
    };
    
    private static Runnable clickFrenzyOverRunnable = new Runnable() {
        @Override
        public void run() {
            eventsHandler.fireCpcChanged(new CpcChangedEvent(BigCookie.getCpc()));
        }
    };
    
    private static Runnable cookieGetEventRunnable = new Runnable() {
        @Override
        public void run() {
            //AchievementManager.onCookieGet();
            eventsHandler.fireCookieGet(
                    new CookieGetEvent(Stats.cookies, Stats.spent, Stats.profited));
        }
    };

    public static void init(MainActivity main) {
        context = main;
        SharedPreferences preferences = main.getSharedPreferences("game", Context.MODE_PRIVATE);

        Stats.init(preferences);
        BuildingManager.init(preferences);
        BigCookie.init(preferences);
        GoldenCookie.init(preferences);
        AchievementManager.init(preferences);
        UpgradeManager.init(preferences);

    }

    public static void save() {
        //multi, profit, hc, golden clicks
        SharedPreferences.Editor preferences = context.getSharedPreferences("game", 0).edit();
        Stats.save(preferences);
        BuildingManager.save(preferences);
        BigCookie.save(preferences);
        GoldenCookie.save(preferences);
        UpgradeManager.save(preferences);
        AchievementManager.save(preferences);

        preferences.apply();
    }

    public static void setNotificationView(NotificationView view){
        notifView = view;
    }

    public static double calculateHeavenlyChips() {
        if (Stats.getTotalCookies() < Stats.neededToRestart) return -1;
        double trillions = (Stats.getAllTimeCookies() / 1000000000000.0);
        trillions -= trillions%1;
        double hc = (Math.sqrt(8 * trillions + 1) - 1) / 2;
        return hc - hc%1 - Stats.heavenlyChips;
    }

    public static void restoreCookies(long time) {
        float seconds = time / 1000.0f;
        Stats.cookies += Stats.baseCps * seconds;
    }

    public static int lifeCycle(){
        GoldenCookie.frenzy -= times;
        if((int)GoldenCookie.frenzy == -1) {
            //context.repaintSubtitle(); onCpsChanged
            GoldenCookie.frenzy = -2;
            context.runOnUiThread(frenzyOverRunnable);

        }
        GoldenCookie.clickFrenzy -= times;
        if((int)GoldenCookie.clickFrenzy == -1){
            GoldenCookie.clickFrenzy = -2;
            context.runOnUiThread(clickFrenzyOverRunnable);
        }
        GoldenCookie.secondsLeft -= times;
        Stats.cookies += Stats.getCps() * times;
        if((System.currentTimeMillis()/100)%50==0) {
            context.runOnUiThread(cookieGetEventRunnable);

        }
        /* -->   onCookieGet()
        if(Stats.view!=null)
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Stats.view.updateReleated();
                }
            });
        */
        if(GoldenCookie.secondsLeft <= 0){
            GoldenCookie.secondsLeft = GoldenCookie.minutes*60 + GoldenCookie.showSecs;
            context.showGoldenCookie(GoldenCookie.getShowSecs()); // onGoldenCookieShow() ?
            if(GoldenCookie.chain > 0 && !GoldenCookie.lastClicked){
            	GoldenCookie.chain = 0;
            }
            GoldenCookie.lastClicked = false;
            return (int)GoldenCookie.showSecs;
        }
        return -1;
    }

    private Game() {
    }

    /*public static class AchievementManager{
        public static class Achievement{
            public String triggerType, trigger;
            int img, desc;
        }
    }*/

        public static void softReset() {
            Stats.profited+=Stats.getTotalCookies();
            double trillones = Stats.profited/1000000000000.0;
            trillones -= trillones%1;
            Stats.heavenlyChips = (Math.sqrt(trillones * 8 + 1) - 1) / 2;
            Stats.heavenlyChips -= Stats.heavenlyChips%1;
            Stats.neededToRestart = (Stats.heavenlyChips+1)*1000000000000.0;

            Stats.baseCps = 0;
            Stats.cookies = 0;
            Stats.multiplier = 1+Stats.heavenlyChips*0.02;
            Stats.kittens = 0;
            Stats.spent = 0;
            ++Stats.resets;
            Stats.bakingTime = new Date().getTime();

            BigCookie.foreachBuilding = 0;
            BigCookie.extra = 0;
            BigCookie.cpsPercent = 0;
            BigCookie.baseCpc = 1;
            Stats.clicks = 0;
            Stats.handmade = 0;
            BigCookie.multiplier = 1;

            GoldenCookie.showSecs = 13;
            GoldenCookie.effectTimes = 1;
            GoldenCookie.minutes = 8;
            GoldenCookie.secondsLeft = 8 * 60;
            Stats.beforeResetGoldenClicks = Stats.goldenClicks;
            Stats.goldenClicks = 0;
            GoldenCookie.frenzy = 0;
            GoldenCookie.clickFrenzy = 0;

            for (int i = 0; i < 11; ++i) {
                BuildingManager.n[i] = 0;
                BuildingManager.multis[i] = 1;
            }
            BuildingManager.cursorExtra = 0;
            BuildingManager.baseCps = BuildingManager.defaultBuildingsCps;
            BuildingManager.costs = BuildingManager.defaultBuildingCosts;

            UpgradeManager.reset();
//            AchievementManager.onReset();
//            Stats.view.afterReset();
//            UpgradeManager.unlockedView.update();
//            UpgradeManager.boughtView.update();
//            AchievementManager.view.update();
            eventsHandler.fireReset(new ResetEvent(Stats.resets, Stats.heavenlyChips, Stats.profited));
        }

        //*****************************DEBUG*************************//
        /*public static void resetUpgrades(SharedPreferences.Editor preferences) {
        /*BigCookie.cpc = new Cookiefloat(1);
        BigCookie.multi = 1;
        BigCookie.extra = new Cookiefloat(0);
        BigCookie.percent = 0;

        BuildingManager.baseCps = new Cookiefloat[] {
                new Cookiefloat("0.1"), new Cookiefloat("0.5"), new Cookiefloat(4),
                new Cookiefloat(10), new Cookiefloat(40), new Cookiefloat(100),
                new Cookiefloat(400), new Cookiefloat(6666), new Cookiefloat(98765),
                new Cookiefloat(999999), new Cookiefloat(10000000)
        };

        BuildingManager.multis = new int[] {1,1,1,1,1,1,1,1,1,1,1};

        BuildingManager.cursorExtras = new Cookiefloat(0);

        for(int i=0, j; i<UpgradeManager.bought.size(); ++i){
            for(j=0; j<UpgradeManager.unlocked.size(); ++j){
                if(new Cookiefloat(UpgradeManager.ups[(int)UpgradeManager.bought.get(i)].price).gt(new Cookiefloat(UpgradeManager.ups[(int)UpgradeManager.unlocked.get(j)].price))){
                    UpgradeManager.unlocked.add(j,UpgradeManager.bought.get(i));
                    break;
                }
            }
            if(j==UpgradeManager.unlocked.size()) UpgradeManager.unlocked.add(UpgradeManager.bought.get(i));
        }
        UpgradeManager.bought = new ArrayList();*/

          /*  preferences.putString("cpc", "1");
            preferences.putString("big_extra", "0");
            preferences.putInt("big_multi", 1);
            preferences.putInt("big_percent", 0);


            for (int i = 0; i < 11; ++i) {
                preferences.putString("bbase" + i, BuildingManager.baseCps[i].get());
                preferences.putInt("bmulti" + i, 1);
            }
            preferences.putString("cursor_extra", "0");

            for (int i = 0; i < 88; ++i) {
                preferences.putInt("up" + i, 0);
            }

            preferences.apply();

        }*/

    /*public static void recalcCps(){
        baseCps = new Cookiefloat(0);
        for(int i=0; i<11; ++i){
            baseCps.s(Cookiefloat.m(getBuildingCps(i), new Cookiefloat(BuildingManager.n[i])));
        }
    }*/

}
