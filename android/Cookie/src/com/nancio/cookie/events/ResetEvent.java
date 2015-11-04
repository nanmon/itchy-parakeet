package com.nancio.cookie.events;

/**
 * Created by nancio on 8/07/15.
 */
public class ResetEvent {
    public final int resets;
    public final double heavenlyChips;
    public final double profited;

    public ResetEvent(int resets, double heavenlyChips, double profited) {
        this.resets = resets;
        this.heavenlyChips = heavenlyChips;
        this.profited = profited;
    }
}
