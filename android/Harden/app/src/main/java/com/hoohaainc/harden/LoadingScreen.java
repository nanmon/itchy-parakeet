package com.hoohaainc.harden;

import com.hoohaainc.framework.Game;
import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Screen;

/**
 * Created by nancio on 11/07/15.
 */
public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        /* Load Assets
         * Assets.someImage = g.newImage("name.jpg", ImageFormat.someFormat);
         * Assets.someSound = game.getAudio().createSound("name.ogg");
        */
        Assets.metapodIdle = g.newImage("metapodIdle.png", Graphics.ImageFormat.ARGB4444);
        Assets.metapodHarden = g.newImage("metapodHarden.png", Graphics.ImageFormat.ARGB4444);
        Assets.metapodHit = g.newImage("metapodHit.png", Graphics.ImageFormat.ARGB4444);

        Assets.kakunaIdle = g.newImage("kakunaIdle.png", Graphics.ImageFormat.ARGB4444);
        Assets.kakunaHarden = g.newImage("kakunaHarden.png", Graphics.ImageFormat.ARGB4444);
        Assets.kakunaHit = g.newImage("kakunaHit.png", Graphics.ImageFormat.ARGB4444);

        Assets.rock = g.newImage("rock.png", Graphics.ImageFormat.ARGB4444);

        game.setScreen(new GameScreen(game));
    }

    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
