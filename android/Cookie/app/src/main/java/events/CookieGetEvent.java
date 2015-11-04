package com.nancio.cookie.events;

/**
 * Created by nancio on 8/07/15.
 */
public class CookieGetEvent {
    public final double cookies;
    public final double spent;
    public final double profited;

    public CookieGetEvent(double cookies, double spent, double profited) {
        this.cookies = cookies;
        this.spent = spent;
        this.profited = profited;
    }

    public double getTotalMade(){
        return cookies + spent;
    }

    public double getAllTimeCookies(){
        return cookies + spent + profited;
    }
}
