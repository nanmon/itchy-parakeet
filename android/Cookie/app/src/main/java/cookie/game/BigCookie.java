package com.nancio.cookie.game;

import android.content.SharedPreferences;

import com.nancio.cookie.events.BigCookieClickEvent;

/**
 * Created by nancio on 24/06/15.
 */
public class BigCookie {
    static double baseCpc = 1;
    static int multiplier = 1;
    static double extra = 0; //???
    static int cpsPercent = 0;
    static float foreachBuilding = 0;

    static void init(SharedPreferences preferences){
        baseCpc = Double.parseDouble(preferences.getString("BigCookieBaseCpc", "1"));
        multiplier = preferences.getInt("BigCookieMultiplier", 1);
        extra = Double.parseDouble(preferences.getString("BigCookieExtra", "0"));
        cpsPercent = preferences.getInt("BigCookieCpsPercent", 0);
        foreachBuilding = preferences.getFloat("BigCookieForeachBuilding", 0);

    }

    static void save(SharedPreferences.Editor preferences){
        preferences.putString("BigCookieBaseCpc", baseCpc+"");
        preferences.putInt("BigCookieMultiplier", multiplier);
        preferences.putString("BigCookieExtra", extra+"");
        preferences.putInt("BigCookieCpsPercent", cpsPercent);
        preferences.putFloat("BigCookieForeachBuilding", foreachBuilding);
    }

    public static double click(){
        ++Stats.clicks;
        double cpc = getCpc()*(GoldenCookie.clickFrenzy>0?777:1);
        Stats.cookies += cpc;
        Stats.handmade += cpc;
        //Stats.view.updateClicks();
        //Stats.view.updateHandmade();
        //UpgradeManager.onHandmade();
        //AchievementManager.onBigCookieClick();
        Game.eventsHandler.fireBigCookieClick(new BigCookieClickEvent(Stats.clicks, Stats.handmade, cpc));
        return cpc;
    }

    public static double getCpc(){
        return baseCpc*multiplier + extra + cpsPercent * Stats.getCps()/100 + foreachBuilding*Stats.getTotalBuildings();
    }
}
