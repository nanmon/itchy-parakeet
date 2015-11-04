package com.nancio.cookie.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nancio.cookie.Cookiefloat;
import com.nancio.cookie.R;
import com.nancio.cookie.events.BuildingBoughtEvent;
import com.nancio.cookie.events.BuildingBoughtListener;
import com.nancio.cookie.events.BuildingCpsChangedEvent;
import com.nancio.cookie.events.BuildingCpsChangedListener;
import com.nancio.cookie.events.BuildingSelectedEvent;
import com.nancio.cookie.events.BuildingSelectedListener;
import com.nancio.cookie.events.ResetEvent;
import com.nancio.cookie.events.ResetListener;
import com.nancio.cookie.events.UpgradeSelectedEvent;
import com.nancio.cookie.events.UpgradeSelectedListener;
import com.nancio.cookie.game.BuildingManager;
import com.nancio.cookie.game.Game;

/**
 * Created by nancio on 4/01/15.
 */
public class BuildingView extends RelativeLayout
    implements BuildingSelectedListener, UpgradeSelectedListener, ResetListener{
    private boolean expanded = false;
    private int index;
    private View expansion;

    public BuildingView(Context context) {
        super(context);
//        LayoutInflater layoutInflater = (LayoutInflater)context.
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutInflater.inflate(R.layout.building_layout, this);
    }

    public BuildingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BuildingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        inflate(getContext(), R.layout.building_layout, this);
        TypedArray a = context.getTheme().
                obtainStyledAttributes(attrs, R.styleable.BuildingView, 0, 0);
        try {
            index = a.getInt(R.styleable.BuildingView_index, 0);
        }finally {
            a.recycle();
        }

        ((ImageView) findViewById(R.id.building_image)).setImageResource(BuildingManager.DRAWABLES[index]);
        if(!this.isInEditMode())
            ((TextView)findViewById(R.id.building_name)).setText(context.getResources().getQuantityText(BuildingManager.NAMES[index], 1));
        else((TextView)findViewById(R.id.building_name)).setText(BuildingManager.NAMES[index]);
        ((TextView)findViewById(R.id.building_cost)).setText(Cookiefloat.format(BuildingManager.getCost(index), true));
        ((TextView)findViewById(R.id.building_n)).setText(BuildingManager.getN(index) + "");
        //((TextView)findViewById(R.id.building_cps)).setText("+" + Cookiefloat.format(BuildingManager.getCps(index), false) + " cps");
        ((TextView)findViewById(R.id.building_description)).setText(BuildingManager.DESCRIPTIONS[index]);
        findViewById(R.id.building_description).requestLayout();
        expansion = findViewById(R.id.building_expansion);
        expansion.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        expansion.getLayoutParams().height = 0;
        findViewById(R.id.building_mainlayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggleExpansion();
                if(!expanded) BuildingManager.select(index);
                else contract();
            }
        });
        findViewById(R.id.building_buy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
        //BuildingManager.setBuildingView(this, index);
        Game.eventsHandler.addBuildingSelectedListener(this);
        Game.eventsHandler.addUpgradeSelectedListener(this);
        Game.eventsHandler.addResetListener(this);
        this.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Game.eventsHandler.removeBuildingSelectedListener(BuildingView.this);
                Game.eventsHandler.removeUpgradeSelectedListener(BuildingView.this);
                Game.eventsHandler.removeResetListener(BuildingView.this);
            }
        });
    }

    public void contract(){
        if(!expanded) return;
        final int initialHeight = expansion.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1)
                    expansion.setVisibility(View.GONE);
                else{
                    expansion.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    expansion.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight/expansion.getContext().getResources().getDisplayMetrics().density)*4);
        expansion.startAnimation(a);

        expanded = false;
        //invalidate();
        //requestLayout();
    }

    public void expand(){
        if(expanded) return;
        ViewGroup papi = ((ViewGroup) this.getParent());
        //BuildingManager.select(index);
        //papi.callOnClick();
        /*for(int i=0; i<papi.getChildCount(); ++i){
            if(papi.getChildAt(i) instanceof BuildingView)
                ((BuildingView)papi.getChildAt(i)).contract();
        }*/
        ((TextView)findViewById(R.id.building_cps)).setText("+" + Cookiefloat.format(BuildingManager.getNetoIncome(index), false)
                + " "+ getResources().getString(R.string.cps).toLowerCase());


        expansion.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = expansion.getMeasuredHeight();
        expansion.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                expansion.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)((targetHeight) * interpolatedTime);
                expansion.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetHeight / expansion.getContext().getResources().getDisplayMetrics().density) * 4);
        expansion.startAnimation(a);
        expanded = true;

    }

    public void toggleExpansion(){
        if(expanded) contract();
        else expand();
    }

    public void buy(){
        //BuildingManager.select(index);
        BuildingManager.buy();
        ((TextView)findViewById(R.id.building_cost)).setText(Cookiefloat.format(BuildingManager.getCost(index), true));
        ((TextView)findViewById(R.id.building_n)).setText(BuildingManager.getN(index) + "");
        //invalidate();
        //requestLayout();
    }

    @Override
    public void onBuildingSelected(BuildingSelectedEvent e) {
        if (e.i == index) expand();
        else contract();
    }

    @Override
    public void onUpgradeSelected(UpgradeSelectedEvent e){
        contract();
    }

    @Override
    public void onReset(ResetEvent e) {
        ((TextView)findViewById(R.id.building_cost)).setText(Cookiefloat.format(BuildingManager.getCost(index), true));
        ((TextView)findViewById(R.id.building_n)).setText(BuildingManager.getN(index) + "");
    }

}
