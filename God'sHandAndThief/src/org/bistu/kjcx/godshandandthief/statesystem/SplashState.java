package org.bistu.kjcx.godshandandthief.statesystem;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class SplashState implements IGameObject {
	
	private StateSystem stateSystem;
	
	private Paint paint;
	
	
	
	public SplashState(Context context, StateSystem stateSystem) {
		this.stateSystem = stateSystem;
		
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
	}
	
	public void render(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);
		canvas.drawLine(MainSurfaceView.SCREEN_W / 4, 0, MainSurfaceView.SCREEN_W / 2, MainSurfaceView.SCREEN_H, paint);
		canvas.drawLine(0, MainSurfaceView.SCREEN_H, MainSurfaceView.SCREEN_W, 0, paint);
		
		//paint.setARGB(255, 255, 255, 255);		//A͸����Խ��Խ͸��
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			Log.i("SplashScreenState", "onKeyDown ����> back");
		stateSystem.changeState("MenuState");
		return true;		//���ñ�������
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}