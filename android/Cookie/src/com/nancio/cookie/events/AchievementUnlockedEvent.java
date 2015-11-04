package com.nancio.cookie.events;

import com.nancio.cookie.game.AchievementManager;

/**
 * Created by nancio on 8/07/15.
 */
public class AchievementUnlockedEvent {
    public final int id;
    public final int name;
    public final int image;
    public final String desc;
    public final AchievementManager.TYPES type;

    public final int achievementsBought;

    public AchievementUnlockedEvent(int id, int name, int image, String desc, AchievementManager.TYPES type, int achievementsBought) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.type = type;
        this.achievementsBought = achievementsBought;
    }
}
