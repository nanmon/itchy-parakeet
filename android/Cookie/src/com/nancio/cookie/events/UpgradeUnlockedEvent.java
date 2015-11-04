package com.nancio.cookie.events;

/**
 * Created by nancio on 9/07/15.
 */
public class UpgradeUnlockedEvent {
    public final int id;
    public final int name;
    public final int image;
    public final int quote;
    public final String description;

    public final int upgradesUnlocked;

    public UpgradeUnlockedEvent(int id, int name, int image, int quote, String description, int upgradesUnlocked) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.quote = quote;
        this.description = description;
        this.upgradesUnlocked = upgradesUnlocked;
    }
}
