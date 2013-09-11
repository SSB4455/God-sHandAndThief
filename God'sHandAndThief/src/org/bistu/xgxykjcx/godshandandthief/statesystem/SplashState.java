package org.bistu.xgxykjcx.godshandandthief.statesystem;

import org.bistu.xgxykjcx.godshandandthief.MainSurfaceView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class SplashState implements IGameObject {
	
	private StateSystem stateSystem;
	
	private long splashTime;
	
	private Paint paint;
	
	
	
	public SplashState(StateSystem stateSystem) {
		this.stateSystem = stateSystem;
		
		splashTime = 0;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		splashTime += elapsedTime;
		if(splashTime > 3000)
			stateSystem.changeState("MenuState");
	}
	
	public void render(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);
		canvas.drawLine(MainSurfaceView.SCREEN_W / 4, 0, MainSurfaceView.SCREEN_W / 2, MainSurfaceView.SCREEN_H, paint);
		canvas.drawLine(0, MainSurfaceView.SCREEN_H, MainSurfaceView.SCREEN_W, 0, paint);
		
		//paint.setARGB(255, 255, 255, 255);		//A透明度越低越透明
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
		stateSystem.changeState("MenuState");
		return true;		//不让别人做了
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}
	
}
