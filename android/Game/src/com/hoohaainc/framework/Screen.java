package com.hoohaainc.framework;

/**
 * Created by nancio on 10/07/15.
 */
public abstract class Screen {
    protected final Game game;

    public Screen(Game game){
        this.game = game;
    }

    public abstract void update(float deltaTime);
    public abstract void paint(float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
    /**
     * do something when back button is pressed
     * @return should make default action (minimize)
     */
    public abstract boolean backButton();
}
