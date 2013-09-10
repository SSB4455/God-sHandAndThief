package org.bistu.xgxykjcx.godshandandthief.statesystem;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract interface IGameObject{
	
	abstract void update(long elapsedTime);
	
	abstract void render(Canvas canvas);
	
	abstract boolean onKeyDown(int keyCode, KeyEvent event);
	
	abstract boolean onTouchEvent(MotionEvent event);
	
}