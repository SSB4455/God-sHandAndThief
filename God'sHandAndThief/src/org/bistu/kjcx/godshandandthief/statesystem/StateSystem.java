package org.bistu.kjcx.godshandandthief.statesystem;

import java.util.Hashtable;

import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class StateSystem {
	public enum PlayerType {
		Auto,		//自动，电脑控制
		Player,		//手动，玩家控制
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
	
	public void render() {
		//Log.v(this.getClass().toString(), "start render no canvas");
		currentState.render();
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
