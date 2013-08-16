package org.bistu.kjcx.godandthief.statesystem;

import org.bistu.kjcx.godandthief.actor.Background;
import org.bistu.kjcx.godandthief.actor.Businessman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ThiefPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	
	private Background background;
	private Businessman businessman;
	
	private Paint paint;
	
	
	
	public ThiefPlayerState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		background = new Background(context);
		businessman = new Businessman(context);
		
		paint = new Paint();
	}
	
	public void update(long elapsedTime) {
		background.update(elapsedTime);
		businessman.update(elapsedTime);
	}
	
	public void render(Canvas canvas) {
		background.render(canvas);
		businessman.render(canvas);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
			Log.i("ThiefChooseState", "onKeyDown ——> back");
		stateSystem.changeState("MenuState");
		return true;		//不让别人做了
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
