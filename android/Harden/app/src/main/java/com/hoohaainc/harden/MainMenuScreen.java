package com.hoohaainc.harden;

import com.hoohaainc.framework.Game;
import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Input;
import com.hoohaainc.framework.Screen;

import java.util.List;

/**
 * Created by nancio on 11/07/15.
 */
public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        //Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents =
                game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i=0; i<len; ++i){
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP){
                if(inBounds(event, 0, 0, 250, 250))
                    //Start Game
                    game.setScreen(new GameScreen(game));
            }
        }
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height){
        if(event.x > x && event.x < x + width-1 &&
                event.y > y && event.y < y + height-1)
            return true;
        else return false;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        //draw shit
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
