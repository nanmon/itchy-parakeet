package com.nancio.cookie.events;

/**
 * Created by nancio on 8/07/15.
 */
public class BuildingBoughtEvent {
    public final int building;
    public final int n;
    public final double cps;

    public final int totalN;

    public BuildingBoughtEvent(int building, int n, double cps, int totalN) {
        this.building = building;
        this.n = n;
        this.cps = cps;
        this.totalN = totalN;
    }
}
