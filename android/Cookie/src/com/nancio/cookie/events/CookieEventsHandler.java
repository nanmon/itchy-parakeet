package com.nancio.cookie.events;

import java.util.ArrayList;

/**
 * Created by nancio on 8/07/15.
 */
public class CookieEventsHandler {
    private ArrayList<CookieGetListener> cookieGetListeners = new ArrayList<>();
    private ArrayList<BigCookieClickListener> bigCookieClickListeners = new ArrayList<>();
    private ArrayList<GoldenCookieClickListener> goldenCookieClickListeners = new ArrayList<>();
    private ArrayList<BuildingBoughtListener> buildingBoughtListeners = new ArrayList<>();
    private ArrayList<UpgradeBoughtListener> upgradeBoughtListeners = new ArrayList<>();
    private ArrayList<AchievementUnlockedListener> achievementUnlockListeners = new ArrayList<>();
    private ArrayList<ResetListener> resetListeners = new ArrayList<>();
    private ArrayList<CpsChangedListener> cpsChangedListeners = new ArrayList<>();
    private ArrayList<CpcChangedListener> cpcChangedListeners = new ArrayList<>();
    private ArrayList<BuildingSelectedListener> buildingSelectedListeners = new ArrayList<>();
    private ArrayList<UpgradeSelectedListener> upgradeSelectedListeners = new ArrayList<>();
    private ArrayList<AchievementSelectedListener> achievementSelectedListeners = new ArrayList<>();
    private ArrayList<BuildingCpsChangedListener> buildingCpsChangedListeners = new ArrayList<>();
    private ArrayList<UpgradeUnlockedListener> upgradeUnlockedListeners = new ArrayList<>();

    public void addCookieGetListener(CookieGetListener l){ cookieGetListeners.add(l);}

    public void removeCookieGetListener(CookieGetListener l){
        cookieGetListeners.remove(l);
    }

    public void fireCookieGet(CookieGetEvent e){
        for(int i=cookieGetListeners.size() -1; i>=0; --i){
            cookieGetListeners.get(i).onCookieGet(e);
        }
    }

    public void addBigCookieClickListener(BigCookieClickListener l){
        bigCookieClickListeners.add(l);
    }

    public void removeBigCookieClickListener(BigCookieClickListener l){
        bigCookieClickListeners.remove(l);
    }

    public void fireBigCookieClick(BigCookieClickEvent e){
        for(int i=bigCookieClickListeners.size() -1; i>=0; --i){
            bigCookieClickListeners.get(i).onBigCookieClick(e);
        }
    }

    public void addGoldenCookieClickListener(GoldenCookieClickListener l){
        goldenCookieClickListeners.add(l);
    }

    public void removeGoldenCookieClickListener(GoldenCookieClickListener l){
        goldenCookieClickListeners.remove(l);
    }

    public void fireGoldenCookieClick(GoldenCookieClickEvent e){
        for(int i=goldenCookieClickListeners.size() -1; i>=0; --i){
            goldenCookieClickListeners.get(i).onGoldenCookieClick(e);
        }
    }

    public void addBuildingBoughtListener(BuildingBoughtListener l){
        buildingBoughtListeners.add(l);
    }

    public void removeBuildingBoughtListener(BuildingBoughtListener l){
        buildingBoughtListeners.remove(l);
    }

    public void fireBuildingBought(BuildingBoughtEvent e){
        for(int i=buildingBoughtListeners.size() -1; i>=0; --i){
            buildingBoughtListeners.get(i).onBuildingBought(e);
        }
    }

    public void addUpgradeBoughtListener(UpgradeBoughtListener l){
        upgradeBoughtListeners.add(l);
    }

    public void removeUpgradeBoughtListener(UpgradeBoughtListener l){
        upgradeBoughtListeners.remove(l);
    }

    public void fireUpgradeBought(UpgradeBoughtEvent e){
        for(int i=upgradeBoughtListeners.size() -1; i>=0; --i){
            upgradeBoughtListeners.get(i).onUpgradeBought(e);
        }
    }

    public void addAchievementUnlockedListener(AchievementUnlockedListener l){
        achievementUnlockListeners.add(l);
    }

    public void removeAchievementUnlockedListener(AchievementUnlockedListener l){
        achievementUnlockListeners.remove(l);
    }

    public void fireAchievementUnlocked(AchievementUnlockedEvent e){
        for(int i=achievementUnlockListeners.size() -1; i>=0; --i){
            achievementUnlockListeners.get(i).onAchievementUnlocked(e);
        }
    }

    public void addResetListener(ResetListener l){
        resetListeners.add(l);
    }

    public void removeResetListener(ResetListener l){
        resetListeners.remove(l);
    }

    public void fireReset(ResetEvent e){
        for(int i=resetListeners.size() -1; i>=0; --i){
            resetListeners.get(i).onReset(e);
        }
    }

    public void addCpsChangedListener(CpsChangedListener l){
        cpsChangedListeners.add(l);
    }

    public void removeCpsChangedListener(CpsChangedListener l){
        cpsChangedListeners.remove(l);
    }

    public void fireCpsChanged(CpsChangedEvent e){
        for(int i=cpsChangedListeners.size() -1; i>=0; --i){
            cpsChangedListeners.get(i).onCpsChanged(e);
        }
    }

    public void addCpcChangedListener(CpcChangedListener l){
        cpcChangedListeners.add(l);
    }

    public void removeCpcChangedListener(CpcChangedListener l){
        cpcChangedListeners.remove(l);
    }

    public void fireCpcChanged(CpcChangedEvent e){
        for(int i=cpcChangedListeners.size() -1; i>=0; --i){
            cpcChangedListeners.get(i).onCpcChanged(e);
        }
    }

    public void addBuildingSelectedListener(BuildingSelectedListener l){
        buildingSelectedListeners.add(l);
    }

    public void removeBuildingSelectedListener(BuildingSelectedListener l){
        buildingSelectedListeners.remove(l);
    }

    public void fireBuildingSelected(BuildingSelectedEvent e){
        for(int i=buildingSelectedListeners.size() -1; i>=0; --i){
            buildingSelectedListeners.get(i).onBuildingSelected(e);
        }
    }

    public void addUpgradeSelectedListener(UpgradeSelectedListener l){
        upgradeSelectedListeners.add(l);
    }

    public void removeUpgradeSelectedListener(UpgradeSelectedListener l){
        upgradeSelectedListeners.remove(l);
    }

    public void fireUpgradeSelected(UpgradeSelectedEvent e){
        for(int i=upgradeSelectedListeners.size() -1; i>=0; --i){
           upgradeSelectedListeners.get(i).onUpgradeSelected(e);
        }
    }

    public void addAchievementSelectedListener(AchievementSelectedListener l){
        achievementSelectedListeners.add(l);
    }

    public void removeAchievementSelectedListener(AchievementSelectedListener l){
        achievementSelectedListeners.remove(l);
    }

    public void fireAchievementSelected(AchievementSelectedEvent e){
        for(int i=achievementSelectedListeners.size() -1; i>=0; --i){
            achievementSelectedListeners.get(i).onAchievementSelected(e);
        }
    }

    public void addBuildingCpsChangedListener(BuildingCpsChangedListener l){
        buildingCpsChangedListeners.add(l);
    }

    public void removeBuildingCpsChangedListener(BuildingCpsChangedListener l){
        buildingCpsChangedListeners.remove(l);
    }

    public void fireBuildingCpsChanged(BuildingCpsChangedEvent e){
        for(int i=buildingCpsChangedListeners.size() -1; i>=0; --i){
            buildingCpsChangedListeners.get(i).onBuildingCpsChanged(e);
        }
    }

    public void addUpgradeUnlockedListener(UpgradeUnlockedListener l){
        upgradeUnlockedListeners.add(l);
    }

    public void removeUpgradeUnlockedListener(UpgradeUnlockedListener l){
        upgradeUnlockedListeners.remove(l);
    }

    public void fireUpgradeUnlocked(UpgradeUnlockedEvent e){
        for(int i=upgradeUnlockedListeners.size() -1; i>=0; --i){
            upgradeUnlockedListeners.get(i).onUpgradeUnlocked(e);
        }
    }
}
