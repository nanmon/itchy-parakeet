package com.nancio.cookie.game;

import android.content.SharedPreferences;

import com.nancio.cookie.R;
import com.nancio.cookie.views.MilkyView;
import com.nancio.cookie.views.StatsView;

import java.util.Date;

/**
 * Created by nancio on 24/06/15.
 */
public class Stats {

    static StatsView view;
    static MilkyView milkyView;

    static double cookies = 0;
    static double spent = 0;
    static double baseCps = 0; //Game.baseCps * multiplier
    static double multiplier = 1;
    static int kittens = 0;
    static double profited = 0;
    static double heavenlyChips = 0;
    static double neededToRestart = 1000000000000.0;
    static long bakingTime = new Date().getTime();
    static long totalBakingTime = bakingTime;
    static int resets = 0;

    static double handmade = 0;
    static int clicks = 0;
    static int goldenClicks = 0;
    static int beforeResetGoldenClicks = 0;

    static void init(SharedPreferences preferences){
        if(!preferences.contains("cookies")) return;

        cookies = Double.parseDouble(preferences.getString("cookies", "0"));
        baseCps = Double.parseDouble(preferences.getString("baseCps", "0"));
        multiplier = Double.parseDouble(preferences.getString("multi", "1"));
        spent = Double.parseDouble(preferences.getString("spent", "0"));
        profited = Double.parseDouble(preferences.getString("profited", "0"));
        heavenlyChips = Double.parseDouble(preferences.getString("heavenlyChips", "0"));
        neededToRestart *= (heavenlyChips+1);
        bakingTime = preferences.getLong("bakingTime", new Date().getTime());
        totalBakingTime = preferences.getLong("totalBakingTime", new Date().getTime());
        resets = preferences.getInt("resets", 0);
        kittens = preferences.getInt("kittens", 3);

        handmade = Double.parseDouble(preferences.getString("handmande", "0"));
        clicks = preferences.getInt("clicks", 0);
        goldenClicks = preferences.getInt("goldenClicks", 0);
        beforeResetGoldenClicks = preferences.getInt("beforeResetGoldenClicks", 0);

    }

    static void save(SharedPreferences.Editor preferences){
        preferences.putString("cookies", cookies + "");
        preferences.putString("baseCps", baseCps + "");
        preferences.putString("multi", multiplier + "");
        preferences.putString("spent", spent + "");
        preferences.putString("profited", profited + "");
        preferences.putString("heavenlyChips", heavenlyChips+"");
        preferences.putLong("bakingTime", bakingTime);
        preferences.putLong("totalBakingTime", totalBakingTime);
        preferences.putInt("resets", resets);
        preferences.putInt("kittens", kittens);

        preferences.putString("handmande", handmade + "");
        preferences.putInt("clicks", clicks);
        preferences.putInt("goldenClicks", goldenClicks);
        preferences.putInt("beforeResetGoldenClicks", beforeResetGoldenClicks);

    }

    public static void setStatsView(StatsView view){
        Stats.view = view;
    }
    public static void setMilkyView(MilkyView view){ milkyView = view; }

    public static double getCookies() {
        return cookies;
    }

    public static double getBaseCps(){
        return baseCps;
    }

    public static double getCps() {
        return baseCps * getMultiplier();
    }

    public static double getBaseMultiplier() {
        double r = multiplier;
        if (kittens > 0) {
            r *= 0.05 * getMilk() + 1;
            if (kittens > 1) {
                r *= 0.1 * getMilk() + 1;
                if (kittens > 2) {
                    r *= 0.2 * getMilk() + 1;
                    if (kittens > 3) {
                        r *= 0.2 * getMilk() + 1;
                        if (kittens > 4)
                            r *= 0.2 * getMilk() + 1;
                    }
                }
            }
        }
        return r;
    }

    public static double getMultiplier() {
        return getBaseMultiplier()*(GoldenCookie.frenzy>-1?7:1);
    }

    public static double getMilk(){
        return AchievementManager.getUnlockedLength()*0.04;
    }

    public static double getSpent() {
        return spent;
    }

    public static double getTotalCookies(){
        return cookies + spent;
    }

    public static double getAllTimeCookies(){
        return getTotalCookies() + profited;
    }

    public static double getProfited() {
        return profited;
    }

    public static double getHeavenlyChips() {
        return heavenlyChips;
    }

    public static double getNeededToRestart() {
        return neededToRestart;
    }

    public static long getBakingTime() {
        return bakingTime;
    }

    public static long getTotalBakingTime() {
        return totalBakingTime;
    }

    public static String getSince(){
        return getSince(true);
    }

    public static String getSince(boolean thisGame){
        long time = new Date().getTime() - (thisGame ? bakingTime : totalBakingTime);
        time/=60000;
        if(time/1440>0) return time/1440 +" "+ Game.context.getResources().getQuantityString(R.plurals.day, (int) (time / 1440)).toLowerCase();
        if(time/60>0) return time/60 +" "+ Game.context.getResources().getQuantityString(R.plurals.hour, (int) (time / 60)).toLowerCase();
        return time +" "+ Game.context.getResources().getQuantityString(R.plurals.minute, (int)time).toLowerCase();
    }

    public static int getResets() {
        return resets;
    }

    public static double getCpc() {
        return BigCookie.getCpc()*(GoldenCookie.clickFrenzy>0?777:1);
    }

    public static double getHandmade() {
        return handmade;
    }

    public static int getClicks() {
        return clicks;
    }

    public static int getGoldenClicks() {
        return goldenClicks;
    }

    public static int getAllTimeGoldenClicks(){ return goldenClicks + beforeResetGoldenClicks; }

    public static int getTotalBuildings(){
        return BuildingManager.getTotalBuildings();
    }



}
