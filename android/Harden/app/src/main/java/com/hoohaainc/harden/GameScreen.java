package com.hoohaainc.harden;

import android.graphics.Color;
import android.graphics.Paint;

import com.hoohaainc.framework.Game;
import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Input;
import com.hoohaainc.framework.Pool;
import com.hoohaainc.framework.Screen;
import com.hoohaainc.framework.utility.Vector2;
import com.hoohaainc.harden.game.Player;
import com.hoohaainc.harden.game.Rock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nancio on 11/07/15.
 */
public class GameScreen extends Screen {
    enum GameState {
        Ready, Playing, Paused, GameOver, EndMenu
    }
    GameState state = GameState.Ready;
    Player metapod, kakuna;
    Pool<Rock> rockPool;
    List<Rock> rockList = new ArrayList<>();
    //final float deathDelay = 5.0f;
    //float deathTimer = 0.0f;
    float rockDelay = 3.0f;
    final float winDelay = 5.0f;
    float winTimer = 0.0f;

    Paint paint;

    public GameScreen(Game game) {
        super(game);
        //Initialize game objects here
        metapod = new Player(game, 0);
        kakuna = new Player(game, 1);
        final int screenHeight = game.getGraphics().getHeight();
        rockPool = new Pool<>(new Pool.PoolObjectFactory<Rock>() {
            @Override
            public Rock createObject() {
                return new Rock(screenHeight);
            }
        }, 10);
        //Define a paint object
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> input = game.getInput().getTouchEvents();
        switch (state){
            case Ready:
                //READY... GO! -> Playing
                state = GameState.Playing;
                break;
            case Playing:
                //Playing stuff until Paused or GameOver
                metapod.passInput(input);
                kakuna.passInput(input);

                if(metapod.isOutOfPP()){
                    kakuna.win();
                    state = GameState.GameOver;
                    for(int i=rockList.size()-1; i>=0; --i)
                        rockPool.free(rockList.remove(i));
                }else if(kakuna.isOutOfPP()) {
                    metapod.win();
                    state = GameState.GameOver;
                    for(int i=rockList.size()-1; i>=0; --i)
                        rockPool.free(rockList.remove(i));
                }
                rockDelay -= deltaTime;
                if(rockDelay <= 0){
                    Rock rock = rockPool.newObject();
                    rock.setPosition(new Vector2(metapod.getPosition().x, -64));
                    rock.unDestroy();
                    rockList.add(rock);
                    rock = rockPool.newObject();
                    rock.setPosition(new Vector2(kakuna.getPosition().x, -64));
                    rock.unDestroy();
                    rockList.add(rock);
                    rockDelay = 1.0f + (float)Math.random()*3;
                }

                break;
            case Paused:
                //Pause menu
                break;
            case GameOver:
                //Win animation
                winTimer += deltaTime;
                if(winTimer >= winDelay){
                    metapod.restart();
                    kakuna.restart();
                    state = GameState.Playing;
                    winTimer = 0.0f;
                }
                break;
            case EndMenu:
                //if(exit) return to MainMenu
                //if(playAgain) state = Ready;
        }
        metapod.update(deltaTime);
        kakuna.update(deltaTime);
        for(int i=rockList.size() -1; i>=0; --i) {
            rockList.get(i).update(deltaTime);
            if(rockList.get(i).getHitbox().intersects(metapod.getHitbox())) {
                if (!metapod.hit()) rockList.get(i).destroy();
            }else if(rockList.get(i).getHitbox().intersects(kakuna.getHitbox()))
                    if(!kakuna.hit()) rockList.get(i).destroy();
            if(rockList.get(i).isDestroyed())
                rockPool.free(rockList.remove(i));
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawARGB(255, 255, 255, 255);

        metapod.draw(g);
        kakuna.draw(g);
        for(int i=rockList.size()-1; i>=0; --i)
            rockList.get(i).draw(g);

        switch (state){
            case Ready:
                break;
            case Playing:
                break;
            case Paused:
                break;
            case GameOver:
                paint.setColor(Color.BLACK);
                g.drawString("fin c:", 100, 100, paint);
        }


    }

    private void nullify(){
        //Set all variables to null
        paint = null;
        //Call garbage collector
        System.gc();
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
