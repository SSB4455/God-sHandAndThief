package org.bistu.kjcx.godandthief.statesystem;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ThiefPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	

	public ThiefPlayerState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
	}

	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}

	public void render() {
		// TODO Auto-generated method stub

	}

	public void render(Canvas canvas) {
		// TODO Auto-generated method stub

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

}
