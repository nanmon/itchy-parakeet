package com.nancio.cookie.events;

/**
 * Created by nancio on 9/07/15.
 */
public class AchievementSelectedEvent {
    public final int id;
    public final int name;
    public final int image;
    public final String description;

    public AchievementSelectedEvent(int id, int name, int image, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }
}
