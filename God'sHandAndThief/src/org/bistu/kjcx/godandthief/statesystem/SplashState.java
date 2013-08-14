package org.bistu.kjcx.godandthief.statesystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class SplashState implements IGameObject {
	
	private StateSystem stateSystem;
	
	private int screenW, screenH;
	int i, j, laps, sideLength; 
	float startX, stopX, startY, stopY;
	long contractStep, diffuseStep;		//收缩步骤，扩散步骤
	float startXC, stopXC, startYC, stopYC;
	
	double includedXYAngle;
	boolean isTurn, isC, isD, isC2, isD2;
	double currentAngle;
	Paint paint;
	
	
	
	public SplashState(Context context, StateSystem stateSystem, SurfaceHolder sfh) {
		this.stateSystem = stateSystem;
		
		screenW = sfh.getSurfaceFrame().right;
		screenH = sfh.getSurfaceFrame().bottom;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
	}
	
	public void render(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);
		canvas.drawLine(100, 100, 500, 100, paint);
		
		//paint.setARGB(255, 255, 255, 255);		//A透明度越低越透明
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			Log.i("SplashScreenState", "onKeyDown ——> back");
		stateSystem.changeState("MenuState");
		return true;		//不让别人做了
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
