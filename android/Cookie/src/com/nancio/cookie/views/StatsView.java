package com.nancio.cookie.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nancio.cookie.Cookiefloat;
import com.nancio.cookie.R;
import com.nancio.cookie.events.AchievementUnlockedEvent;
import com.nancio.cookie.events.AchievementUnlockedListener;
import com.nancio.cookie.events.BigCookieClickEvent;
import com.nancio.cookie.events.BigCookieClickListener;
import com.nancio.cookie.events.BuildingBoughtEvent;
import com.nancio.cookie.events.BuildingBoughtListener;
import com.nancio.cookie.events.CookieGetEvent;
import com.nancio.cookie.events.CookieGetListener;
import com.nancio.cookie.events.CpcChangedEvent;
import com.nancio.cookie.events.CpcChangedListener;
import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.events.CpsChangedListener;
import com.nancio.cookie.events.GoldenCookieClickEvent;
import com.nancio.cookie.events.GoldenCookieClickListener;
import com.nancio.cookie.events.ResetEvent;
import com.nancio.cookie.events.ResetListener;
import com.nancio.cookie.events.UpgradeBoughtEvent;
import com.nancio.cookie.events.UpgradeBoughtListener;
import com.nancio.cookie.game.Game;
import com.nancio.cookie.game.GoldenCookie;
import com.nancio.cookie.game.Stats;

/**
 * Created by nancio on 30/06/15.
 */
public class StatsView extends LinearLayout {
    private CookieGetListener onCookieGet;
    private CpsChangedListener onCpsChanged;
    private ResetListener onReset;
    private CpcChangedListener onCpcChanged;
    private BuildingBoughtListener onBuildingBought;
    private UpgradeBoughtListener onUpgradeBought;
    private BigCookieClickListener onBigCookieClick;
    private GoldenCookieClickListener onGoldenCookieClick;
    private AchievementUnlockedListener onAchievementUnlocked;

    public StatsView(Context context) {
        super(context);
        init(context);
    }

    public StatsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(getContext(), R.layout.stats_layout, this);
        if(isInEditMode()) return;
        if(Stats.getResets()>0)
            afterReset();
        //Stats.setStatsView(this);
        updateAll();
        onCookieGet = new CookieGetListener() {
            @Override
            public void onCookieGet(CookieGetEvent e) {
                updateTimeReleated();
            }
        };
        onCpsChanged = new CpsChangedListener() {
            @Override
            public void onCpsChanged(CpsChangedEvent e) {
                updateMultiplier();
            }
        };
        onBigCookieClick = new BigCookieClickListener() {
            @Override
            public void onBigCookieClick(BigCookieClickEvent e) {
                updateClicks();
                updateHandmade();
            }
        };
        onCpcChanged = new CpcChangedListener() {
            @Override
            public void onCpcChanged(CpcChangedEvent e) {
                updateCpc();
            }
        };
        onBuildingBought = new BuildingBoughtListener() {
            @Override
            public void onBuildingBought(BuildingBoughtEvent e) {
                updateBuildings();
                updateSpent();
            }
        };
        onUpgradeBought = new UpgradeBoughtListener() {
            @Override
            public void onUpgradeBought(UpgradeBoughtEvent e) {
                updateSpent();
            }
        };
        onGoldenCookieClick = new GoldenCookieClickListener() {
            @Override
            public void onGoldenCookieClick(GoldenCookieClickEvent e) {
                updateGoldClicks();
            }
        };
        onReset = new ResetListener() {
            @Override
            public void onReset(ResetEvent e) {
                afterReset();
            }
        };
        onAchievementUnlocked = new AchievementUnlockedListener() {
            @Override
            public void onAchievementUnlocked(AchievementUnlockedEvent e) {
                updateMilk();
            }
        };
        Game.eventsHandler.addCookieGetListener(onCookieGet);
        Game.eventsHandler.addCpsChangedListener(onCpsChanged);
        Game.eventsHandler.addBigCookieClickListener(onBigCookieClick);
        Game.eventsHandler.addCpcChangedListener(onCpcChanged);
        Game.eventsHandler.addGoldenCookieClickListener(onGoldenCookieClick);
        Game.eventsHandler.addBuildingBoughtListener(onBuildingBought);
        Game.eventsHandler.addUpgradeBoughtListener(onUpgradeBought);
        Game.eventsHandler.addResetListener(onReset);
        Game.eventsHandler.addAchievementUnlockedListener(onAchievementUnlocked);
        this.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Game.eventsHandler.removeCookieGetListener(onCookieGet);
                Game.eventsHandler.removeCpsChangedListener(onCpsChanged);
                Game.eventsHandler.removeBigCookieClickListener(onBigCookieClick);
                Game.eventsHandler.removeCpcChangedListener(onCpcChanged);
                Game.eventsHandler.removeGoldenCookieClickListener(onGoldenCookieClick);
                Game.eventsHandler.removeBuildingBoughtListener(onBuildingBought);
                Game.eventsHandler.removeUpgradeBoughtListener(onUpgradeBought);
                Game.eventsHandler.removeResetListener(onReset);
                Game.eventsHandler.removeAchievementUnlockedListener(onAchievementUnlocked);
            }
        });
    }

    public void afterReset(){
        findViewById(R.id.game_resets).setVisibility(View.VISIBLE);
        findViewById(R.id.game_resets_label).setVisibility(View.VISIBLE);
        findViewById(R.id.total_time).setVisibility(View.VISIBLE);
        findViewById(R.id.total_time_label).setVisibility(View.VISIBLE);
        findViewById(R.id.all_time_cookies).setVisibility(View.VISIBLE);
        findViewById(R.id.all_time_cookies_label).setVisibility(View.VISIBLE);
        findViewById(R.id.profited_cookies).setVisibility(View.VISIBLE);
        findViewById(R.id.profited_cookies_label).setVisibility(View.VISIBLE);
        findViewById(R.id.heavenly_chips).setVisibility(View.VISIBLE);
        findViewById(R.id.heavenly_chips_label).setVisibility(View.VISIBLE);

        updateTotalTime();
        updateAllTime();
        updateProfited();
        updateResets();
        updateHeavenlyChips();
    }

    public void updateCpc(){
        ((TextView) findViewById(R.id.cpc_label)).setText(
                Cookiefloat.format(Stats.getCpc(), false)
                        +" "+ getResources().getString(R.string.cpc).toLowerCase());
    }

    public void updateSpent(){
        double spent = Stats.getSpent();
        ((TextView) findViewById(R.id.spent_cookies_label)).setText(
                Cookiefloat.format(spent, true)
                +" "+ getResources().getQuantityString(R.plurals.cookie, (int) spent).toLowerCase());
    }

    public void updateClicks(){
        int clicks = Stats.getClicks();
        ((TextView) findViewById(R.id.cookie_clicks_label))
                .setText(Cookiefloat.format(clicks)
                        +" "+ getResources().getQuantityString(R.plurals.click, clicks).toLowerCase());
    }

    public void updateHandmade(){
        double handmade = Stats.getHandmade();
        ((TextView) findViewById(R.id.handmade_label))
                .setText(Cookiefloat.format(handmade, true)
                        +" "+ getResources().getQuantityString(R.plurals.cookie, (int) handmade).toLowerCase());
    }

    public void updateBuildings(){
        ((TextView) findViewById(R.id.buildings_label))
                .setText(Cookiefloat.format(Stats.getTotalBuildings()));
    }

    public void updateGoldClicks(){
        ((TextView) findViewById(R.id.goldclicks_label))
                .setText(Cookiefloat.format(Stats.getGoldenClicks())
                        +" "+ getResources().getString(R.string.clicked).toLowerCase());
    }

    public void updateMultiplier(){
        ((TextView) findViewById(R.id.cps_multi_label)).setText(
                Cookiefloat.format(Stats.getMultiplier() * 100, false)
                        +" "+ getResources().getString(R.string.percent).toLowerCase());
    }

    public void updateMilk(){
        ((TextView) findViewById(R.id.milk_label)).setText(
                Cookiefloat.format(Stats.getMilk() * 100, false)
                        +" "+ getResources().getString(R.string.percent).toLowerCase());
    }

    public void updateTotalCookies(){
        double totalCookies = Stats.getTotalCookies();
        ((TextView) findViewById(R.id.total_cookies_label))
                .setText(Cookiefloat.format(totalCookies, true)
                        +" "+getResources().getQuantityString(R.plurals.cookie, (int) totalCookies).toLowerCase());
    }

    public void updateBakingSince(){
        ((TextView) findViewById(R.id.bakin_since_label))
                .setText(Stats.getSince());
    }

    public void updateTotalTime(){
        ((TextView) findViewById(R.id.total_time_label))
                .setText(Stats.getSince(false));
    }

    public void updateAllTime(){
        double allTime = Stats.getAllTimeCookies();
        ((TextView) findViewById(R.id.all_time_cookies_label))
                .setText(Cookiefloat.format(allTime, true)
                        +" "+getResources().getQuantityString(R.plurals.cookie, (int) allTime).toLowerCase());
    }

    public void updateProfited(){
        double profited = Stats.getProfited();
        ((TextView) findViewById(R.id.profited_cookies_label))
                .setText(Cookiefloat.format(profited, false)
                        +" "+ getResources().getQuantityString(R.plurals.cookie, (int)profited).toLowerCase());
    }

    public void updateResets(){
        ((TextView) findViewById(R.id.game_resets_label))
                .setText(Stats.getResets() + "");
    }

    public void updateHeavenlyChips(){
        double hc = Stats.getHeavenlyChips();
        ((TextView) findViewById(R.id.heavenly_chips_label))
                .setText(String.format(getResources().getQuantityString(R.plurals.heavenly_chips_description, (int)hc),
                        Cookiefloat.format(hc, false),
                        Cookiefloat.format(hc*2, false)));
                // .setText(Cookiefloat.format(hc, true) + " chips (+" + Cookiefloat.format(hc * 2, true) + "% multiplier)");
    }

    public void updateAll(){
        updateCpc();
        updateSpent();
        updateClicks();
        updateHandmade();
        updateBuildings();
        updateGoldClicks();
        updateMultiplier();
        updateTotalCookies();
        updateBakingSince();
        updateMilk();

        if(Stats.getResets()>0) {
            updateTotalTime();
            updateAllTime();
            updateProfited();
            updateResets();
            updateHeavenlyChips();
        }
    }

    public void updateTimeReleated(){
        updateTotalCookies();
        updateBakingSince();
        if(Stats.getResets()>0){
            updateTotalTime();
            updateAllTime();
        }
    }
}
