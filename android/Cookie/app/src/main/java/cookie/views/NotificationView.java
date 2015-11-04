package com.nancio.cookie.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nancio.cookie.R;
import com.nancio.cookie.events.AchievementUnlockedEvent;
import com.nancio.cookie.events.AchievementUnlockedListener;
import com.nancio.cookie.events.GoldenCookieClickEvent;
import com.nancio.cookie.events.GoldenCookieClickListener;
import com.nancio.cookie.game.Game;

/**
 * Created by nancio on 1/07/15.
 */
public class NotificationView extends LinearLayout implements
    GoldenCookieClickListener, AchievementUnlockedListener {

    public NotificationView(Context context) {
        super(context);
        init();
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //Game.setNotificationView(this);
        setOrientation(VERTICAL);
        Game.eventsHandler.addGoldenCookieClickListener(this);
        Game.eventsHandler.addAchievementUnlockedListener(this);
        this.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Game.eventsHandler.removeGoldenCookieClickListener(NotificationView.this);
                Game.eventsHandler.removeAchievementUnlockedListener(NotificationView.this);
            }
        });
    }

    public void notificate(String desc, boolean sound){
        LayoutInflater inflator = LayoutInflater.from(getContext());
        final View son = inflator.inflate(R.layout.notification_layout, null);
        son.findViewById(R.id.notification_img).setVisibility(GONE);
        son.findViewById(R.id.notification_title).setVisibility(GONE);
        TextView gd = (TextView)son.findViewById(R.id.notification_desc);
        gd.setText(desc);
        gd.setVisibility(VISIBLE);
        addView(son, 0);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(1000);
        alpha.setStartOffset(3000);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeView(son);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.cancel();
            }
        });
        son.startAnimation(alpha);
        if(sound){

        }
    }

    public void notificate(int img, String title, String desc, boolean sound){
        LayoutInflater inflator = LayoutInflater.from(getContext());
        final View son = inflator.inflate(R.layout.notification_layout, null);
        ImageView iView = (ImageView)son.findViewById(R.id.notification_img);
        iView.setVisibility(VISIBLE);
        iView.setImageResource(img);
        TextView tView = (TextView)son.findViewById(R.id.notification_title);
        tView.setVisibility(VISIBLE);
        tView.setText(title);
        TextView gd = (TextView)son.findViewById(R.id.notification_desc);
        gd.setText(desc);
        gd.setVisibility(VISIBLE);
        addView(son, 0);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(1000);
        alpha.setStartOffset(3000);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeView(son);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.cancel();
            }
        });
        son.startAnimation(alpha);
        if(sound){

        }
    }

    @Override
    public void onGoldenCookieClick(GoldenCookieClickEvent e) {
        notificate(e.description, false);
    }

    @Override
    public void onAchievementUnlocked(AchievementUnlockedEvent e) {
        notificate(e.image, getResources().getString(e.name), e.desc, true);
    }
}
