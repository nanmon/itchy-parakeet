package com.nancio.cookie.events;

/**
 * Created by nancio on 9/07/15.
 */
public class BuildingCpsChangedEvent {
    public final int index;
    public final double cps;

    public BuildingCpsChangedEvent(int index, double cps) {
        this.index = index;
        this.cps = cps;
    }
}
