package com.nancio.cookie.events;

import com.nancio.cookie.game.UpgradeManager;

/**
 * Created by nancio on 8/07/15.
 */
public class UpgradeBoughtEvent {
    public final int id;
    public final int image;
    public final int name;
    public final int quote;
    public final String description;
    public final UpgradeManager.TYPES type;
    public final int upgradesBought;

    public UpgradeBoughtEvent(int id, int image, int name, int quote, String description, UpgradeManager.TYPES type, int upgradesBought) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.quote = quote;
        this.description = description;
        this.type = type;
        this.upgradesBought = upgradesBought;
    }
}
