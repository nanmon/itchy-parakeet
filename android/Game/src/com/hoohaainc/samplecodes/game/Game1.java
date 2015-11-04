package com.hoohaainc.samplecodes.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.hoohaainc.framework.Game;
import com.hoohaainc.framework.Graphics;
import com.hoohaainc.framework.Input.TouchEvent;
import com.hoohaainc.framework.Screen;
import com.hoohaainc.framework.implementation.AndroidGame;

import android.graphics.Color;
import android.graphics.Paint;

public class Game1 extends AndroidGame {
	
	

	@Override
	public Screen getInitScreen() {
		return new Screen1(this);
	}
	
	public static class Screen1 extends Screen {
		
		boolean clear = false;

		List<TouchEvent> touchEvents;
		HashMap<Integer, List<Touch>> allTouchEvents = new HashMap<>();
		HashMap<Integer, Float> times = new HashMap<>();
		
		public Screen1(Game game) {
			super(game);
		}

		@Override
		public void update(float deltaTime) {
			if(clear){
				allTouchEvents.clear();
				clear = false;
			}
			touchEvents = game.getInput().getTouchEvents();
			TouchEvent actual;
			for(int i=0; i<touchEvents.size(); ++i){
				actual = touchEvents.get(i);
				switch(actual.type){
				case TouchEvent.TOUCH_DOWN:
					times.put(actual.pointer, 0f);
					break;
				case TouchEvent.TOUCH_DRAGGED:
				case TouchEvent.TOUCH_HOLD:
					if(times.containsKey(actual.pointer))
						times.put(actual.pointer, times.get(actual.pointer)+deltaTime);
					else times.put(actual.pointer, deltaTime);
					break;
				case TouchEvent.TOUCH_UP:
					times.remove(actual.pointer);
				}
				if(!allTouchEvents.containsKey(actual.pointer)){
					allTouchEvents.put(actual.pointer, new ArrayList<Touch>());
				}
				if(times.containsKey(actual.pointer))
					allTouchEvents.get(actual.pointer).add(new Touch(actual, times.get(actual.pointer)));
				else allTouchEvents.get(actual.pointer).add(new Touch(actual, 0));
			}
		}

		@Override
		public void paint(float deltaTime) {
			Graphics spriteBatch = game.getGraphics();
			spriteBatch.clearScreen(Color.WHITE);
			TouchEvent event;
			Paint textPaint = new Paint();
			textPaint.setColor(Color.BLACK);
			Iterator<Entry<Integer, List<Touch>>> iter = allTouchEvents.entrySet().iterator();
			List<Touch> list;
			while(iter.hasNext()){
				list = iter.next().getValue();
				for(int i=0; i<list.size(); ++i)
					list.get(i).draw(spriteBatch,  deltaTime);
				
			}
			for(int i=0; i<touchEvents.size(); ++i){
				event = touchEvents.get(i);
				spriteBatch.drawString("Touch"+event.pointer, 0, i*60, textPaint);
				spriteBatch.drawString("Position: (" +event.x+","+event.y+")", 0, 20+i*60, textPaint);
				spriteBatch.drawString("Type:"+event.type, 0, 40+i*60, textPaint);
			}
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean backButton() {
			clear = true;
			return false;
		}
		
	}
	
	public static class Touch {
		int x,y,type;
		float timer = 0;
		
		public Touch(TouchEvent e, float time){
			x = e.x;
			y = e.y;
			type = e.type;
			timer = time;
		}
		
		public void draw(Graphics spriteBatch, float gameTime){
			switch(type){
			case TouchEvent.TOUCH_DOWN:
				spriteBatch.drawCircle(x, y, 10, Color.BLUE);
				break;
			case TouchEvent.TOUCH_UP:
				spriteBatch.drawCircle(x, y, 3, Color.RED);
				break;
			case TouchEvent.TOUCH_HOLD:
				int width = (int)(timer*10);
				spriteBatch.drawOval(x-width/2, y-10, width, 10, Color.YELLOW);
				break;
			case TouchEvent.TOUCH_DRAGGED:
				int radius = (int)(timer*5);
				spriteBatch.drawCircle(x, y, radius, Color.GREEN);
				break;
			}
		}
	}

}
