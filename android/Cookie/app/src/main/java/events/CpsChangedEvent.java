package com.nancio.cookie.events;

/**
 * Created by nancio on 8/07/15.
 */
public class CpsChangedEvent {
    public final double cps;
    public final double multiplier;

    public CpsChangedEvent(double cps, double multiplier) {
        this.cps = cps;
        this.multiplier = multiplier;
    }
}
