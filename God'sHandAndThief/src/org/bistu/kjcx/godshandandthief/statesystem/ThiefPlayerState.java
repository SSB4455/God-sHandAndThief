package org.bistu.kjcx.godshandandthief.statesystem;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.ProgressBar;
import org.bistu.kjcx.godshandandthief.actor.obstacle.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ThiefPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private ProgressBar progressBar;
	private Background background;
	private Businessman businessman;
	private Obstacle obstacleSupervisor;
	
	private Paint paint;
	
	
	
	public ThiefPlayerState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		progressBar = new ProgressBar(BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run));
		background = new Background(context);
		businessman = new Businessman(context);
		
		obstacleSupervisor = new Obstacle("obstacleSupervisor");
		obstacleSupervisor.addChild(new Hole(context));
		obstacleSupervisor.addChild(new Stone(context));
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		progressBar.update(elapsedTime);
		if(progressBar.isPlay()) {
			background.update(elapsedTime);
			obstacleSupervisor.update(elapsedTime);
			businessman.update(elapsedTime);
		}
	}
	
	public void render(Canvas canvas) {
		background.render(canvas);
		progressBar.render(canvas);
		if(progressBar.isPlay()) {
			obstacleSupervisor.render(canvas);
			businessman.render(canvas);
		}
	}
	
	void start(String type) {
		progressBar = new ProgressBar(BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run));
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

	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
