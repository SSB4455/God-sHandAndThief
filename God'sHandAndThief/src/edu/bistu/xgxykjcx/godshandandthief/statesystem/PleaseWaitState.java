package edu.bistu.xgxykjcx.godshandandthief.statesystem;

import edu.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import edu.bistu.xgxykjcx.godshandandthief.GHTSurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PleaseWaitState implements IGameObject {
	//private Context context;
	private StateSystem stateSystem;
	
	private Bitmap waitMoment;
	
	private Paint paint;
	
	
	
	public PleaseWaitState(StateSystem stateSystem) {
		//this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		
		waitMoment = BitmapStorage.getWaitMoment();
		
		paint = new Paint();
	}
	
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		canvas.drawBitmap(waitMoment, GHTSurfaceView.SCREEN_W / 5, GHTSurfaceView.SCREEN_H /5, paint);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
