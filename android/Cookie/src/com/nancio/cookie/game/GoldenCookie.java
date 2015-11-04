package com.nancio.cookie.game;

import android.content.SharedPreferences;

import com.nancio.cookie.Cookiefloat;
import com.nancio.cookie.R;
import com.nancio.cookie.events.CpcChangedEvent;
import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.events.GoldenCookieClickEvent;
import com.nancio.cookie.views.StatsView;

/**
 * Created by nancio on 24/06/15.
 */
public class GoldenCookie {
    public enum EFFECTS {
        LUCKY, FRENZY, CLICK_FRENZY, CHAIN;
    }

    
    public static int minutes = 8;
    public static float secondsLeft = minutes*60;
    public static int showSecs = 13;
    public static int effectTimes = 1;
    public static float frenzy=-1;
    public static float clickFrenzy=-1;
    public static int chain = 1;
    public static float chainShowSecs = 10;
    public static int expChain = 0;
    public static boolean lastClicked = false;

    static void init(SharedPreferences preferences){
        minutes = preferences.getInt("GoldenCookieMinutes", 8);
        showSecs = preferences.getInt("GoldenCookieShowSecs", 13);
        effectTimes = preferences.getInt("GoldenCookieEffectTimes", 1);
        secondsLeft = minutes*60;
    }

    static void save(SharedPreferences.Editor preferences){
        preferences.putInt("GoldenCookieMinutes", minutes);
        preferences.putInt("GoldenCookieShowSecs", showSecs);
        preferences.putInt("GoldenCookieEffectTimes", effectTimes);
    }

    public static String click(){
        ++Stats.goldenClicks;
        secondsLeft = GoldenCookie.minutes*60;
        //EVENT::::UpgradeManager.onGoldenClick();
        //EVENT::::AchievementManager.onGoldenClick();
        //EVENT::::Stats.view.updateGoldClicks();
        double prob = Math.random();
        String desc;
        if(chain>0 || prob<=0.01){
        	//Cookie chain! +77 cookies
        	lastClicked = true;
            ++chain;
            if(chain<=1){
            	expChain = (int)Math.log10(Stats.cookies) - 9;
            	if(expChain <0) expChain=0;
            }
            double cookieChain = 7.0*Math.pow(10, expChain + chain)/9;
            double nextChain = 7.0*Math.pow(10, expChain + chain + 1)/9;
            if((nextChain > Stats.cookies/4 || nextChain > Stats.getCps()*10800) &&
            		Math.random() < chain/15.0){
            	chain = 0;
            	secondsLeft = minutes*60;
            }else{
	            chainShowSecs = chain <= 10? 10/chain : 1;
	            secondsLeft = 3;
            }
            Stats.cookies += cookieChain;
            desc = Game.context.getString(R.string.cookie_chain, Cookiefloat.format(cookieChain, true));
            Game.eventsHandler.fireGoldenCookieClick(
            		new GoldenCookieClickEvent(Stats.goldenClicks, EFFECTS.CHAIN, desc));
            
            return desc;
        }
        if(prob<=0.49){
            //Lucky! + (s || r) cookies!
            double r = Stats.getCps()*1200;
            if(r > Stats.cookies*0.1){
                r = Stats.cookies*0.1;
            }
            r += 13;
            Stats.cookies += r;
            desc = Game.context.getString(R.string.lucky, Cookiefloat.format(r, true));
            Game.eventsHandler.fireGoldenCookieClick(
                    new GoldenCookieClickEvent(Stats.goldenClicks, EFFECTS.LUCKY, desc)
            );
            return desc;
        }if(prob<=0.97) {
            //Frenzy x7 for 77 seconds
            GoldenCookie.frenzy = 77 * GoldenCookie.effectTimes;
            //EVENT::::Game.context.repaintSubtitle();
            //EVENT::::Stats.view.updateMultiplier();
            desc = Game.context.getString(R.string.frenzy, 77 * GoldenCookie.effectTimes);
            Game.eventsHandler.fireGoldenCookieClick(
                    new GoldenCookieClickEvent(Stats.goldenClicks, EFFECTS.FRENZY, desc)
            );
            Game.eventsHandler.fireCpsChanged(new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier()));
            return desc;
        }
        //Click frenzy! clicks x777 for 13 seconds
        GoldenCookie.clickFrenzy=13*GoldenCookie.effectTimes;
        //EVENT::::Stats.view.updateCpc();
        desc = Game.context.getString(R.string.click_frenzy, 13*GoldenCookie.effectTimes);
        Game.eventsHandler.fireGoldenCookieClick(
                new GoldenCookieClickEvent(Stats.goldenClicks, EFFECTS.CLICK_FRENZY, desc)
        );
        Game.eventsHandler.fireCpcChanged(new CpcChangedEvent(BigCookie.getCpc()*777));
        return desc;
        
    }
    
    
    public static float getShowSecs(){
    	if(chain>0) return chainShowSecs;
    	return showSecs;
    }
}
