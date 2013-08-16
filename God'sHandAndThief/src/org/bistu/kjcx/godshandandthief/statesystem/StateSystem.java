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
		stateStore = new Hashtable<String, IGameObject>(3);
		currentState = null;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return currentState.onKeyDown(keyCode, event);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		//Log.i("StateSystem", "onTouchEvent");
		return currentState.onTouchEvent(event);
	} 
	
	public void update(long elapsedTime) {
		//Log.i("StateSystem", "start update");
		currentState.update(elapsedTime);
	}
	
	public void render() {
		//Log.i("StateSystem", "start render");
		currentState.render();
	}
	
	public void render(Canvas canvas) {
		//Log.i("StateSystem", "start render with canvas");
		currentState.render(canvas);
	}
	
	public boolean addState(String statName, IGameObject state) {
		stateStore.put(statName, state);
		Log.i("StateSystem", "addState " + statName + " is succeed");
		return true;
	}
	
	public boolean changeState(String stateName) {
		currentState = stateStore.get(stateName);
		Log.i("StateSystem", "changeState to " + stateName);
		return true;
	}
	
	public boolean exits(String stateId) {
		return true;
	}
	
	public void testArgument(SurfaceHolder sfh,Canvas canvas) {
		this.sfh = sfh;
		this.canvas = canvas;
	}
}
