package com.hoohaainc.harden.game;

import android.graphics.Color;
import android.graphics.Rect;

import com.hoohaainc.framework.Game;
import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Input;
import com.hoohaainc.framework.utility.Rectangle;
import com.hoohaainc.framework.utility.Vector2;
import com.hoohaainc.harden.Assets;
import com.hoohaainc.harden.Game1;

import java.util.List;

/**
 * Created by nancio on 11/07/15.
 */
public class Player extends GameObject{
    enum Animations {
        Idle, Harden, Hit//, Win, Lose
    }
    AnimationStrip<Animations> animations = new AnimationStrip<>(Animations.class);
    Game game;
    List<Input.TouchEvent> input;
    int playerIndex;
    Rectangle touchArea;
    float PP = 10.0f;
    boolean won = false;

    public Player(Game game, int index){
        this.game = game;
        this.playerIndex = index;
        animations.add(
                Animations.Idle,
                index == 0 ? Assets.metapodIdle : Assets.kakunaIdle,
                index == 0 ? 48 : 32);
        animations.add(
                Animations.Harden,
                index == 0 ? Assets.metapodHarden : Assets.kakunaHarden,
                index == 0 ? 48 : 32);
        animations.add(
                Animations.Hit,
                index==0 ? Assets.metapodHit : Assets.kakunaHit,
                index == 0 ? 48 : 32);
        animations.get(Animations.Hit).setNextAnimation(Animations.Idle);
        /*animations.add(
                Animations.Win,
                index==0 ? Assets.metapodWin : Assets.kakunaWin,
                1);
        animations.add(
                Animations.Lose,
                index == 0 ? Assets.metapodLose : Assets.kakunaLose,
                1);
        animations.get(Animations.Lose).setRepeat(false);
        */
        animations.setAnimation(Animations.Idle);
        hitbox = new Rectangle(0, 4, 48, 48);
        int screenWidth = game.getGraphics().getWidth();
        int screenHeight = game.getGraphics().getHeight();
        position = new Vector2(
                index==0 ? screenWidth/4 : (3*screenWidth/4 - hitbox.width),
                screenHeight/2);
        animations.setPosition(position);
        touchArea = new Rectangle(
                index==0 ? 0 : 2*screenWidth/3,
                screenHeight/2,
                screenWidth/3,
                screenHeight);
    }

    @Override
    public void update(float deltaTime) {
        Animations newAnimation = animations.getCurrentAnimation();
        if(input != null &&
                (newAnimation == Animations.Idle ||
                newAnimation == Animations.Harden)){
            boolean harden = false;
            //if(!game.getInput().isTouchDown(touchPointer))
            for(int i=0; i<input.size(); ++i){
                Input.TouchEvent event = input.get(i);
                if((event.type == Input.TouchEvent.TOUCH_DOWN ||
                        event.type == Input.TouchEvent.TOUCH_HOLD) &&
                        touchArea.contains(event.x, event.y)){
                    newAnimation = Animations.Harden;
                    PP -= deltaTime;
                    harden = true;
                    break;
                }
            }
            /*if(game.getInput().isTouchDown(touchPointer)
                    && touchArea.contains(game.getInput().getTouchX(touchPointer), game.getInput().getTouchY(touchPointer))){

            }*/
            if(!harden) newAnimation = Animations.Idle;
            if(PP <= 0.0f) {
                harden = false;
                //animations.setAnimation(Animations.Lose);
            }
        }
        animations.setAnimation(newAnimation);
        animations.update(deltaTime);
        input = null;
    }

    @Override
    public void draw(Graphics g) {
        animations.draw(g);
        g.drawRect(
                (int)position.x - (playerIndex==0?20:28),
                (int)position.y + 58, 88, 10, Color.BLACK);
        if(PP>0)
            g.drawRect(
                    (int)position.x-(playerIndex==0?20:28),
                    (int)position.y+58, (int)((88)*PP/10), 10, Color.YELLOW);
    }

    public void restart(){
        PP = 10;
    }

    public void passInput(List<Input.TouchEvent> input){
        this.input = input;
    }

    public boolean isOutOfPP(){
        return PP <= 0;
    }

    public boolean hit(){
        if(animations.getCurrentAnimation() == Animations.Idle) {
            animations.setAnimation(Animations.Hit);
            PP -= 2;
            return true;
        }
        return false;
    }

    public void win(){ won = true; }
}
