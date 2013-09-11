package org.bistu.xgxykjcx.godshandandthief.statesystem;

import java.util.Hashtable;

import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class StateSystem {
	public enum PlayerType {
		Player,		// 手动，玩家控制
		Auto,		// 自动，电脑控制
		PlayerWithBlueTooth,	// 通过蓝牙联机，玩家控制
		AutoWithBlueTooth,		// 通过蓝牙联机，自动控制
	}
	
	protected Hashtable<String, IGameObject> stateStore;
	protected IGameObject currentState;
	
	
	
	public StateSystem() {
		stateStore = new Hashtable<String, IGameObject>();
		currentState = null;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return currentState.onKeyDown(keyCode, event);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		//Log.v(this.getClass().toString(), "onTouchEvent");
		return currentState.onTouchEvent(event);
	} 
	
	public void update(long elapsedTime) {
		//Log.v(this.getClass().toString(), "start update");
		currentState.update(elapsedTime);
	}
	
	public void render(Canvas canvas) {
		//Log.v(this.getClass().toString(), "start render with canvas");
		currentState.render(canvas);
	}
	
	public boolean addState(String statName, IGameObject state) {
		Log.v(this.getClass().toString(), "addState " + statName + " is succeed");
		stateStore.put(statName, state);
		return true;
	}
	
	public boolean changeState(String stateName) {
		Log.v(this.getClass().toString(), "changeState to " + stateName);
		currentState = stateStore.get(stateName);
		return true;
	}
	
	public IGameObject getState(String stateName) {
		Log.v(this.getClass().toString(), "get state " + stateName);
		return stateStore.get(stateName);
	}
	
	public boolean exits(String stateId) {
		return true;
	}
	
}
