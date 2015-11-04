package com.nancio.cookie;

import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.nancio.cookie.events.CpsChangedEvent;
import com.nancio.cookie.events.CpsChangedListener;
import com.nancio.cookie.game.BigCookie;
import com.nancio.cookie.game.Game;
import com.nancio.cookie.game.GoldenCookie;
import com.nancio.cookie.game.Stats;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	//Non auto-generated
	private Runnable subtitleRunnable, titleRunnable;
	private CpsChangedListener cpsListener;
	
	private boolean running, visible;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		// Create the adapter that will return a fragment for each of the three
//		// primary sections of the activity.
//		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
//
//		// Set up the ViewPager with the sections adapter.
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mSectionsPagerAdapter);
		super.onCreate(savedInstanceState);
        System.out.println("onCreate");

        subtitleRunnable = new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setSubtitle(Cookiefloat.format(Stats.getCps(), false)
                        + " " + getResources().getString(R.string.cps).toLowerCase());
            }
        };

        titleRunnable = new Runnable() {
            @Override
            public void run() {
                double cookies = Stats.getCookies();
                setTitle(Cookiefloat.format(cookies, true)
                        + " "+getResources().getQuantityString(R.plurals.cookie, (int)cookies).toLowerCase());
            }
        };

        Cookiefloat.init(this);
        Game.init(this);
        Stats.getCookies();
        startCycle();

        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Starts on big cookie fragment
        mViewPager.setCurrentItem(1);


        cpsListener = new CpsChangedListener() {
            @Override
            public void onCpsChanged(final CpsChangedEvent e) {
                repaintSubtitle();
            }
        };
        Game.eventsHandler.addCpsChangedListener(cpsListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        } else*/ if (id == R.id.action_legacy){
                double plus = Game.calculateHeavenlyChips();
                if(plus == -1){
                    String msg = String.format(getString(R.string.noreset_dialog_text), Cookiefloat.format(Stats.getNeededToRestart(), true));
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(msg)
                            .setTitle(R.string.reset_dialog_title).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }else {
                    String msg = String.format(getString(R.string.reset_dialog_text),Cookiefloat.format(plus, true));
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(msg).setTitle(R.string.reset_dialog_title)
                            .setPositiveButton(R.string.reset_dialog_accept, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Game.softReset();
                                    mViewPager.setCurrentItem(1, true);
                                    getSupportActionBar().setSubtitle("0.0 cps");

                                }
                            }).setNegativeButton(R.string.reset_dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    builder.create().show();
                }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	@Override
    protected void onStart() {
        //startCycle();
        repaintTitle();
        repaintSubtitle();
        super.onStart();
    }

    @Override
    protected void onResume() {
        visible = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        visible = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        running = false;
        Game.eventsHandler.removeCpsChangedListener(cpsListener);
        super.onDestroy();
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState");
        long restore = new Date().getTime();
        restore -= savedInstanceState.getLong("longTime", restore);
        Game.restoreCookies(restore);
    }
	
	@Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        System.out.println("onSaveInstanceState");

        Game.save();

        state.putLong("longTime", new Date().getTime());
    }
	
	@Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem()==0){
            mViewPager.setCurrentItem(1, true);
        }else if(mViewPager.getCurrentItem()==2){
            View checkout = findViewById(R.id.checkout);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)checkout.getLayoutParams();
            if(lp.weight>0) {
                ExpandAnimation ex = new ExpandAnimation(lp.weight, 0, checkout);
                ex.setDuration(300);
                checkout.startAnimation(ex);
            }else  mViewPager.setCurrentItem(1, true);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.exit_dialog_text).setTitle(R.string.exit_dialog_title)
                    .setPositiveButton(R.string.exit_dialog_stay,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setNegativeButton(R.string.exit_dialog_title, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Game.save();
                    finish();
                }
            });
            builder.create().show();
            //super.onBackPressed();
        }
    }
	
	private void startCycle(){
        if(running) return;
        running = true;
        final Timer timer = new Timer();
        TimerTask timerTask= new TimerTask(){
            @Override
            public void run() {
                Game.lifeCycle();
                if(visible) repaintTitle();
                if(!running) timer.cancel();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, (int)(Game.times*1000));

    }
	
	public final void repaintTitle(){
        runOnUiThread(titleRunnable);

    }

    public void repaintSubtitle(){
        runOnUiThread(subtitleRunnable);
    }
    
    public void bigClick(View view){
        try {
            double made = BigCookie.click();
            //
            final TextView t = new TextView(this);
            t.setText("+" + Cookiefloat.format(made, false));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            t.setLayoutParams(params);
            t.setPadding(10, 32, 10, 16);
            t.setTextColor(Color.WHITE);
            t.setAlpha(0.8f);
            //((RelativeLayout)mSectionsPagerAdapter.getItem(1).getView()).addView(t);
            final RelativeLayout rl = (RelativeLayout) findViewById(R.id.bigCookie_layout);
            rl.addView(t);

            TranslateAnimation transl = new TranslateAnimation(0, 0, 0, -16);

            transl.setDuration(1000);


            //Animation set = AnimationUtils.loadAnimation(this, R.anim.upvote);
           transl.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {
               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   rl.removeView(t);
               }

               @Override
               public void onAnimationRepeat(Animation animation) {
                   animation.cancel();
               }
           });
            t.startAnimation(transl);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    public void showGoldenCookie(final float secs){
        if(!visible) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ImageView gold = ((ImageView)findViewById(R.id.golden_cookie_view));
                if(gold==null) return;
                Point d = new Point();
                d.y = ((RelativeLayout)gold.getParent()).getHeight();
                d.x = ((RelativeLayout)gold.getParent()).getWidth();
                ((RelativeLayout.LayoutParams)gold.getLayoutParams()).setMargins(
                        (int) (Math.random() * (d.x - gold.getWidth())), (int) (Math.random() * (d.y - gold.getHeight())), 0, 0
                );
                AnimationSet set = new AnimationSet(true);
                AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
                alpha.setDuration((int)(secs*1000/3));
                gold.setVisibility(View.VISIBLE);
                set.addAnimation(alpha);
                alpha = new AlphaAnimation(1.0f, 0.0f);
                alpha.setDuration((int)(secs*1000/3));
                alpha.setStartOffset((int)(2*secs*1000/3));
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        gold.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                set.addAnimation(alpha);
                gold.startAnimation(set);
            }
        });
    }
    
    public void goldenClick(View view){
        if(view.getVisibility()==View.GONE) return;
        view.clearAnimation();
        GoldenCookie.click();
        //((NotificationView)findViewById(R.id.notifications)).notificate(desc, false);
        view.setVisibility(View.GONE);
    }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			int section;
            switch(this.getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1: section = R.layout.stats_fragment; break;
                case 2: section = R.layout.big_cookie_fragment; break;
                default: section = R.layout.upgrades_fragment;
            }
			return inflater.inflate(section, container, false);
		}
	}

	public class ExpandAnimation extends Animation {

        private final float mStartWeight;
        private final float mDeltaWeight;
        private final View mContent;

        public ExpandAnimation(float start, float end, View target){
            mStartWeight = start;
            mDeltaWeight = end - start;
            mContent = target;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContent.getLayoutParams();
            lp.weight = (mStartWeight+(mDeltaWeight*interpolatedTime));
            mContent.setLayoutParams(lp);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
