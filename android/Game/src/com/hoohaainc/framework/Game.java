package com.hoohaainc.framework;

/**
 * Created by nancio on 10/07/15.
 */
public interface Game {

    public Audio getAudio();
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public void update(float gameTime);
    public void draw(float gameTime);
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getInitScreen();
}
