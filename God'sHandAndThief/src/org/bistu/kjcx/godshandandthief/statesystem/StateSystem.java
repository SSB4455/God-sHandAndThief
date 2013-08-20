package org.bistu.kjcx.godshandandthief.statesystem;

import java.util.Hashtable;

import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class StateSystem {
	
	Hashtable<String, IGameObject> stateStore;
	IGameObject currentState;
	
	SurfaceHolder sfh;
	Canvas canvas;
	
	
	
	public StateSystem() {
		stateStore = new Hashtable<String, IGameObject>();
		currentState = null;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return currentState.onKeyDown(keyCode, event);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		//Log.i(this.getClass().toString(), "onTouchEvent");
		return currentState.onTouchEvent(event);
	} 
	
	public void update(long elapsedTime) {
		//Log.i(this.getClass().toString(), "start update");
		currentState.update(elapsedTime);
	}
	
	public void render() {
		//Log.i(this.getClass().toString(), "start render no canvas");
		currentState.render();
	}
	
	public void render(Canvas canvas) {
		//Log.i(this.getClass().toString(), "start render with canvas");
		currentState.render(canvas);
	}
	
	public boolean addState(String statName, IGameObject state) {
		Log.i(this.getClass().toString(), "addState " + statName + " is succeed");
		stateStore.put(statName, state);
		return true;
	}
	
	public boolean changeState(String stateName) {
		Log.i(this.getClass().toString(), "changeState to " + stateName);
		currentState = stateStore.get(stateName);
		return true;
	}
	
	public boolean exits(String stateId) {
		return true;
	}
	
}
