package com.hoohaainc.harden;

import com.hoohaainc.framework.Screen;
import com.hoohaainc.framework.implementation.AndroidGame;

/**
 * Created by nancio on 11/07/15.
 */
public class Game1 extends AndroidGame {

    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }
}
