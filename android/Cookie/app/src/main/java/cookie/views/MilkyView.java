package com.nancio.cookie.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nancio.cookie.R;
import com.nancio.cookie.events.AchievementUnlockedEvent;
import com.nancio.cookie.events.AchievementUnlockedListener;
import com.nancio.cookie.game.AchievementManager;
import com.nancio.cookie.game.Game;
import com.nancio.cookie.game.Stats;

/**
 * Created by nancio on 3/07/15.
 */
public class MilkyView extends LinearLayout implements AchievementUnlockedListener {

    private LinearLayout milk;

    public MilkyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MilkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        inflate(getContext(), R.layout.milk_layout, this);
        milk = (LinearLayout)findViewById(R.id.milky_way);
        if(isInEditMode()){
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.MilkyView, 0, 0);
            int percent = 0;
            try {
                percent = a.getInt(R.styleable.MilkyView_percentage_test, 1);
            }finally {
                a.recycle();
            }
            ((LayoutParams) milk.getLayoutParams()).weight = 100.0f*percent/(101-percent);
            milk.requestLayout();
        }else{
            /*final float targetHeight = getResources().getDimension(R.dimen.milk_width);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    milk.setPadding((int) (targetHeight * interpolatedTime), 0, 0, 0);
                    milk.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration(500);
            a.setRepeatCount(Animation.INFINITE);
            milk.startAnimation(a);*/
            //Stats.setMilkyView(this);
            Game.eventsHandler.addAchievementUnlockedListener(this);
            this.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    Game.eventsHandler.removeAchievementUnlockedListener(MilkyView.this);
                }
            });
            milkChanged();
        }

    }

    public void milkChanged(){
        final float initialWeight = ((LayoutParams) milk.getLayoutParams()).weight;
        float b =100.0f*AchievementManager.getUnlockedLength()/AchievementManager.ACHIEVEMENT_COUNT;
        final float targetWeight = 100*b/(101-b);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ((LayoutParams) milk.getLayoutParams()).weight = initialWeight + (targetWeight - initialWeight)*interpolatedTime;
                milk.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(1000);
        a.setStartOffset(1000);
        milk.startAnimation(a);
    }

    @Override
    public void onAchievementUnlocked(AchievementUnlockedEvent e) {
        milkChanged();
    }

}
