package com.nancio.cookie.events;

import com.nancio.cookie.game.GoldenCookie;

/**
 * Created by nancio on 8/07/15.
 */
public class GoldenCookieClickEvent {

    public final int goldenClicks;
    public final GoldenCookie.EFFECTS effect;
    public final String description;

    public GoldenCookieClickEvent(int goldenClicks, GoldenCookie.EFFECTS effect, String desc){
        this.goldenClicks = goldenClicks;
        this.effect = effect;
        this.description = desc;
    }
}
