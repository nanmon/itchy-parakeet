package com.nancio.cookie.events;

/**
 * Created by nancio on 8/07/15.
 */
public class UpgradeSelectedEvent {
    public final int state;
    public final int id;
    public final int img;
    public final int name;
    public final int quote;
    public final String description;

    public UpgradeSelectedEvent(int state, int id, int img, int name, int quote, String description) {
        this.state = state;
        this.id = id;
        this.img = img;
        this.name = name;
        this.quote = quote;
        this.description = description;
    }
}
