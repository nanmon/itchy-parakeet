package com.nancio.cookie.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nancio.cookie.Cookiefloat;
import com.nancio.cookie.R;
import com.nancio.cookie.events.AchievementSelectedEvent;
import com.nancio.cookie.events.AchievementSelectedListener;
import com.nancio.cookie.events.AchievementUnlockedEvent;
import com.nancio.cookie.events.AchievementUnlockedListener;
import com.nancio.cookie.events.BuildingSelectedEvent;
import com.nancio.cookie.events.BuildingSelectedListener;
import com.nancio.cookie.events.ResetEvent;
import com.nancio.cookie.events.ResetListener;
import com.nancio.cookie.events.UpgradeBoughtEvent;
import com.nancio.cookie.events.UpgradeBoughtListener;
import com.nancio.cookie.events.UpgradeSelectedEvent;
import com.nancio.cookie.events.UpgradeSelectedListener;
import com.nancio.cookie.events.UpgradeUnlockedEvent;
import com.nancio.cookie.events.UpgradeUnlockedListener;
import com.nancio.cookie.game.AchievementManager;
import com.nancio.cookie.game.Game;
import com.nancio.cookie.game.UpgradeManager;

/**
 * Created by nancio on 4/02/15.
 */
public class UpgradesViewer extends LinearLayout {

    int usage;
    RelativeLayout checkout;
    GridView grid;
    boolean expanded = false;
    UpgradeBoughtListener onBought;
    AchievementUnlockedListener onUnlocked;
    BuildingSelectedListener onBuilding;
    UpgradeSelectedListener onUpgrade;
    AchievementSelectedListener onAchievement;
    UpgradeUnlockedListener onUpUnlocked;
    ResetListener onReset;


    public UpgradesViewer(Context context) {
        super(context);
    }

    public UpgradesViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
       init(context, attrs);
    }

    public UpgradesViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        inflate(context, R.layout.upgrades_viewer, this);
        checkout = (RelativeLayout)findViewById(R.id.checkout);
        grid = (GridView)findViewById(R.id.upgrade_box);
        checkout.getLayoutParams().height=0;
        //shows just 7 images
        if(isInEditMode()){
            usage=-1;
            grid.setAdapter(new UpgradeAdapter(grid, usage));
            return;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.UpgradesViewer, 0, 0);
        try {
            usage = a.getInt(R.styleable.UpgradesViewer_usage, 1);
        }finally {
            a.recycle();
        }
        //unlockd upgrades view
        if(usage == 1){
            //count gone
            findViewById(R.id.upgrades_num).setVisibility(View.GONE);
            findViewById(R.id.upgrades_num_label).setVisibility(View.GONE);
            //onBuy
            findViewById(R.id.check_buy).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpgradeManager.buy();
                    contract(true);
                }
            });
            //grid.height = 30dp
            grid.getLayoutParams().height = getContext().getResources().getDimensionPixelSize(R.dimen.upgrade_img_size);
            //onBuildinSelected += contract();
            onBuilding = new BuildingSelectedListener() {
                @Override
                public void onBuildingSelected(BuildingSelectedEvent e) {
                    contract();
                }
            };
            //onUpgradeSelected += expand();
            onUpgrade = new UpgradeSelectedListener() {
                @Override
                public void onUpgradeSelected(UpgradeSelectedEvent e) {
                    if(e.state==1) expand();
                }
            };
            onUpUnlocked = new UpgradeUnlockedListener() {
                @Override
                public void onUpgradeUnlocked(UpgradeUnlockedEvent e) {
                    grid.setAdapter(new UpgradeAdapter(grid, usage));
                }
            };
            //addEvents
            Game.eventsHandler.addBuildingSelectedListener(onBuilding);
            Game.eventsHandler.addUpgradeSelectedListener(onUpgrade);
            Game.eventsHandler.addUpgradeUnlockedListener(onUpUnlocked);
        }else { //not unlocked upgrades
            //grid.height = 90dp;
            grid.getLayoutParams().height = 3*getContext().getResources().getDimensionPixelSize(R.dimen.upgrade_img_size);
            ((LayoutParams)grid.getLayoutParams()).setMargins(0,0,0,0);
            //bye buy buttons
            findViewById(R.id.check_buttons).setVisibility(View.GONE);
            //bought upgrade selected
            onUpgrade = new UpgradeSelectedListener() {
                @Override
                public void onUpgradeSelected(UpgradeSelectedEvent e) {
                    if(e.state==2)
                        if(usage==2) expand();
                        else contract();
                }
            };
            //achievement selected
            onAchievement = new AchievementSelectedListener() {
                @Override
                public void onAchievementSelected(AchievementSelectedEvent e) {
                    if(usage==0) expand();
                    else contract();
                }
            };
            //addEvents();
            Game.eventsHandler.addUpgradeSelectedListener(onUpgrade);
            Game.eventsHandler.addAchievementSelectedListener(onAchievement);
        }
        if(usage==0){ //achievements view
            //no quote, no price, "achievements unlocked: x/y"
            findViewById(R.id.check_quote).setVisibility(View.GONE);
            findViewById(R.id.check_favicon).setVisibility(View.INVISIBLE);
            findViewById(R.id.check_cookies).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.upgrades_num)).setText(R.string.achievements_unlocked);
            //update achievements
            onUnlocked = new AchievementUnlockedListener() {
                @Override
                public void onAchievementUnlocked(AchievementUnlockedEvent e) {
                    grid.setAdapter(new UpgradeAdapter(grid, usage));
                    ((TextView)findViewById(R.id.upgrades_num_label)).setText(AchievementManager.getUnlockedLength()+"/"+AchievementManager.ACHIEVEMENT_COUNT);
                }
            };
            Game.eventsHandler.addAchievementUnlockedListener(onUnlocked);

        }else{ //not achievements view
            //update upgrades
            onBought = new UpgradeBoughtListener() {
                @Override
                public void onUpgradeBought(UpgradeBoughtEvent e) {
                    grid.setAdapter(new UpgradeAdapter(grid, usage));
                    if(usage==2){
                        ((TextView)findViewById(R.id.upgrades_num_label)).setText(UpgradeManager.getBoughtLength()+"/"+UpgradeManager.UPGRADE_COUNT);
                    }
                }
            };
            onReset = new ResetListener() {
                @Override
                public void onReset(ResetEvent e) {
                    grid.setAdapter(new UpgradeAdapter(grid, usage));
                    if(usage==2){
                        ((TextView)findViewById(R.id.upgrades_num_label)).setText(UpgradeManager.getBoughtLength()+"/"+UpgradeManager.UPGRADE_COUNT);
                    }
                }
            };
            Game.eventsHandler.addUpgradeBoughtListener(onBought);
            Game.eventsHandler.addResetListener(onReset);

        }
        //on checkout close: contract()
        findViewById(R.id.check_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contract();
            }
        });
        //set Viewers !!!useless
        //if(usage!=0)UpgradeManager.setUpgradesViewer(this, usage);
        //else AchievementManager.setView(this);
        //set adapter & set count
        grid.setAdapter(new UpgradeAdapter(grid, usage));
        if(usage==0)
            ((TextView)findViewById(R.id.upgrades_num_label)).setText(AchievementManager.getUnlockedLength() + "/" + AchievementManager.ACHIEVEMENT_COUNT);
        else if(usage==2)
            ((TextView)findViewById(R.id.upgrades_num_label)).setText(UpgradeManager.getBoughtLength()+"/"+UpgradeManager.UPGRADE_COUNT);
        //remove events
        this.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if(onUnlocked!=null)
                    Game.eventsHandler.removeAchievementUnlockedListener(onUnlocked);
                if(onBought!=null)
                    Game.eventsHandler.removeUpgradeBoughtListener(onBought);
                if(onUpgrade!=null)
                    Game.eventsHandler.removeUpgradeSelectedListener(onUpgrade);
                if(onBuilding!=null)
                    Game.eventsHandler.removeBuildingSelectedListener(onBuilding);
                if(onAchievement!=null)
                    Game.eventsHandler.removeAchievementSelectedListener(onAchievement);
                if(onUpUnlocked!=null)
                    Game.eventsHandler.removeUpgradeUnlockedListener(onUpUnlocked);
                if(onReset!=null)
                    Game.eventsHandler.removeResetListener(onReset);
            }
        });
    }

    private void upgradeClick(UpgradeManager.Upgrade up){
        ((TextView)findViewById(R.id.check_title)).setText(up.name);
        ((TextView)findViewById(R.id.check_description)).setText(up.getDescription());
        ((TextView)findViewById(R.id.check_cookies)).setText(Cookiefloat.format(up.price, true));
        ((ImageView)findViewById(R.id.check_image)).setImageResource(up.image);
        ((TextView)findViewById(R.id.check_quote)).setText(up.quote);

        //expand();
    }

    private void achievementClick(AchievementManager.Achievement achie){
        ((TextView)findViewById(R.id.check_title)).setText(achie.name);
        ((TextView)findViewById(R.id.check_description)).setText(achie.getDescription());
        ((ImageView)findViewById(R.id.check_image)).setImageResource(achie.image);

        //expand();
    }

    /*public void update(){
        grid.setAdapter(new UpgradeAdapter(grid, usage));
        if(usage==2){
            ((TextView)findViewById(R.id.upgrades_num_label)).setText(UpgradeManager.getBoughtLength()+"/"+UpgradeManager.UPGRADE_COUNT);
        }else if(usage==0)
            ((TextView)findViewById(R.id.upgrades_num_label)).setText(AchievementManager.getUnlockedLength()+"/"+AchievementManager.ACHIEVEMENT_COUNT);
    }*/

    public void expand(){
        final int initialHeight = checkout.getMeasuredHeight();
        checkout.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = checkout.getMeasuredHeight();
        checkout.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                checkout.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (initialHeight + (int)((targetHeight - initialHeight) * interpolatedTime));
                checkout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetHeight / checkout.getContext().getResources().getDisplayMetrics().density) * 4);
        checkout.startAnimation(a);
        expanded= true;
    }

    public void contract(){
        contract(false);
    }

    public void contract(boolean quick){
        if(!expanded) return;
        final int initialHeight = checkout.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1)
                    checkout.setVisibility(View.GONE);
                else {
                    checkout.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    checkout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight / checkout.getContext().getResources().getDisplayMetrics().density) * (quick?1:4));
        checkout.startAnimation(a);
        expanded = false;
    }

    private class UpgradeAdapter extends BaseAdapter{
        int usage;
        GridView grid;

        public UpgradeAdapter(GridView grid, int usage){
            this.grid = grid;
            this.usage = usage;
        }

        @Override
        public int getCount() {
            if(usage==-1)
                return 7;
            if(usage == 1)
                return UpgradeManager.getUnlockedLength();
            if(usage == 2)
                return UpgradeManager.getBoughtLength();
            return AchievementManager.getUnlockedLength();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(UpgradesViewer.this.getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(
                        UpgradesViewer.this.getContext().getResources().getDimensionPixelSize(R.dimen.upgrade_img_size),
                        UpgradesViewer.this.getContext().getResources().getDimensionPixelSize(R.dimen.upgrade_img_size)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else imageView = (ImageView) convertView;

            if(usage==-1){
                imageView.setImageResource(
                        R.drawable.up00_1
                );
                return imageView;
            }

            final int i = position;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usage == 0) AchievementManager.select(i);
                    else if (usage == 1) UpgradeManager.select(i);
                    else if (usage == 2) UpgradeManager.selectBought(i);
                    if (usage != 0)
                        UpgradesViewer.this.upgradeClick((usage == 1
                                ? UpgradeManager.getUnlocked()
                                : UpgradeManager.getBought())[i]);
                    else UpgradesViewer.this.achievementClick(AchievementManager.getUnlocked()[i]);
                }
            });

            imageView.setImageResource(usage == 1
                    ? UpgradeManager.getUnlocked()[position].image
                    : usage== 2 ? UpgradeManager.getBought()[position].image
                    : AchievementManager.getUnlocked()[position].image);
            return imageView;
        }
    }


}
