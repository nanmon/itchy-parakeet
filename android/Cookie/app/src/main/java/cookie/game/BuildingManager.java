package com.nancio.cookie.game;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nancio.cookie.Cookiefloat;
import com.nancio.cookie.R;
import com.nancio.cookie.events.BuildingBoughtEvent;
import com.nancio.cookie.events.BuildingSelectedEvent;
import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.views.BuildingView;

import java.util.Arrays;

/**
 * Created by nancio on 24/06/15.
 */
public class BuildingManager {

    public static final int NUM_BUILDINGS = 11;
    public static final double[] defaultBuildingCosts = new double[] {
            15, 100, 500, 3000, 10000, 40000, 200000, 1666666, 123456789, 3999999999.0, 75000000000.0
    };
    public static final double[] defaultBuildingsCps = new double[] {
            0.1, 0.5, 4, 10, 40, 100, 400, 6666, 98765, 999999, 10000000
    };

    public static final int NAMES[] = {R.plurals.cursor, R.plurals.grandma,
            R.plurals.farm, R.plurals.factory, R.plurals.mine, R.plurals.ship,
            R.plurals.lab, R.plurals.portal, R.plurals.time_machine, R.plurals.antimatter, R.plurals.prism
    };

    public static final int DESCRIPTIONS[] = {R.string.cursor_desc, R.string.grandma_desc,
            R.string.farm_desc, R.string.factory_desc, R.string.mine_desc, R.string.ship_desc,
            R.string.lab_desc, R.string.portal_desc, R.string.time_machine_desc, R.string.antimatter_desc, R.string.prism_desc
    };

    public static final int DRAWABLES[] = {R.drawable.ic_cursor, R.drawable.ic_grandma,
            R.drawable.ic_farm, R.drawable.ic_factory, R.drawable.ic_mine, R.drawable.ic_ship,
            R.drawable.ic_lab, R.drawable.ic_portal, R.drawable.ic_time, R.drawable.ic_antimatter, R.drawable.ic_prism
    };

    private static int selected;
    private static final double priceRiser = 1.15;

    static int[] n = new int[NUM_BUILDINGS];
    static double[] costs = Arrays.copyOf(defaultBuildingCosts, NUM_BUILDINGS);
    static double[] baseCps = Arrays.copyOf(defaultBuildingsCps, NUM_BUILDINGS);
    static int multis[] = {1,1,1,1,1,1,1,1,1,1,1};

    static BuildingView[] views = new BuildingView[NUM_BUILDINGS];

    static double cursorExtra = 0;


    static void init(SharedPreferences preferences){
        cursorExtra = Double.parseDouble(preferences.getString("BuildingManagerCursorExtra", "0"));
        for(int i=0; i<NUM_BUILDINGS; ++i){
            n[i] = preferences.getInt("BuildingManagerN"+i, 0);
            baseCps[i] = Double.parseDouble(preferences.getString("BuildingManagerBaseCps" + i, defaultBuildingsCps[i] + ""));
            multis[i] = preferences.getInt("BuildingManagerMultis"+i, 1);
            costs[i] = Double.parseDouble(preferences.getString("BuildingManagerCost"+i, defaultBuildingCosts[i]+""));
        }
    }

    static void save(SharedPreferences.Editor preferences){
        preferences.putString("BuildingManagerCursorExtra", cursorExtra+"");
        for(int i=0; i<NUM_BUILDINGS; ++i){
            preferences.putInt("BuildingManagerN"+i, n[i]);
            preferences.putString("BuildingManagerBaseCps"+i, baseCps[i] +"");
            preferences.putInt("BuildingManagerMultis"+i, multis[i]);
            preferences.putString("BuildingManagerCost"+i, costs[i]+"");
        }
    }

    public static void setBuildingView(BuildingView view, int i){
        views[i] = view;
    }

    public static double getCps(int i){
        double r = baseCps[i]*multis[i];
        if(i==0) r += cursorExtra;
        return r;
    }

    public static int getN(int i) {
        return n[i];
    }

    public static double getCost(int i) {
        return costs[i];
    }

    public static int getTotalBuildings(){
        int m=0;
        for(int i=0; i<NUM_BUILDINGS; ++i) m+= n[i];
        return m;
    }

    public static void select(int i){
        selected = i;
        Game.bselected = true;
        Game.eventsHandler.fireBuildingSelected(new BuildingSelectedEvent(selected));
//        for(int j=0; j<NUM_BUILDINGS; ++j)
//            views[j].contract();
//        UpgradeManager.unlockedView.contract();
    }

    public static int buy(){
        if(Stats.cookies >= costs[selected]) {
            Stats.cookies -= costs[selected];
            Stats.spent += costs[selected];
            ++n[selected];
            Stats.baseCps += getCps(selected);
            //Game.context.repaintSubtitle();
            costs[selected]*=priceRiser;
//            UpgradeManager.onBuildingBought();
//            AchievementManager.onBuildingBought();
//            AchievementManager.onCpsChange();
//            UpgradeManager.unlockedView.update();
            if(selected !=0 && BigCookie.foreachBuilding !=0.0f){
                double s = BigCookie.foreachBuilding;
                BigCookie.extra += s;
                cursorExtra += s;
                Stats.baseCps += s * n[0];
            }
            Game.eventsHandler.fireBuildingBought(
                    new BuildingBoughtEvent(selected, n[selected], getCps(selected), getTotalBuildings()));
            Game.eventsHandler.fireCpsChanged(
                    new CpsChangedEvent(Stats.getCps(), Stats.getMultiplier())
            );
            return selected;
        }
        return -1;
    }

    public static double getNetoIncome(int i){
        return getCps(i) * Stats.getBaseMultiplier();
    }
}
