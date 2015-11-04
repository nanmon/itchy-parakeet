package com.nancio.cookie.events;

/**
 * Created by nancio on 8/07/15.
 */
public class BigCookieClickEvent {
    public final int clicks;
    public final double handmade;
    public final double cpc;

    public BigCookieClickEvent(int clicks, double handmade, double cpc) {
        this.clicks = clicks;
        this.handmade = handmade;
        this.cpc = cpc;
    }
}
