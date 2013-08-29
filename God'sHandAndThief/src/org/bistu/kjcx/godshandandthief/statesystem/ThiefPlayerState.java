package org.bistu.kjcx.godshandandthief.statesystem;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GodLayout;
import org.bistu.kjcx.godshandandthief.actor.ProgressBar;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle;

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
	
	private boolean isLose, isWin;
	String type;
	private Bitmap isLoseBitmap, isWinBitmap;
	private GodLayout godLayout;
	private ProgressBar progressBar;
	private Background background;
	private Businessman businessman;
	
	private Paint paint;
	
	
	
	public ThiefPlayerState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		//progressBar = new ProgressBar(BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run));
		background = new Background(context);
		businessman = new Businessman(context);
		
		isLoseBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lose);
		isWinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.win);
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		progressBar.update(elapsedTime);
		if(progressBar.isPlay()) {
			background.update(elapsedTime);
			godLayout.update(elapsedTime);
			for(int i = 0; i < godLayout.getObstacles().size(); i++)
				if(businessman.isCollisionWith((Obstacle) godLayout.getObstacles().get(i))) {
					businessman.beInjured();
					Log.i(this.getClass().toString(), "businessman is injured.");
				}
			if(businessman.getHreat() < 1) {
				isLose = true;
				progressBar.stop();
			}
			businessman.update(elapsedTime);
		} else if(!isLose) {
			isWin = true;
			progressBar.stop();
		}
	}
	
	public void render(Canvas canvas) {
		background.render(canvas);
		progressBar.render(canvas);
		godLayout.render(canvas);
		businessman.render(canvas);
		if(isWin)
			canvas.drawBitmap(isWinBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isWinBitmap.getHeight(), paint);
		if(isLose)
			canvas.drawBitmap(isLoseBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isLoseBitmap.getHeight(), paint);
	}
	
	void setGodLayout(GodLayout godLayout) {
		progressBar = new ProgressBar(BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run));
		this.godLayout = godLayout;
		this.godLayout.setProgressBar(progressBar);
	}
	
	boolean setType(String type) {
		this.type = type;
		if(this.type == type)
			return true;
		else
			return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if((isWin || isLose) && type == "withComputer") {
			GodHandPlayerState godHandPlayerState = new GodHandPlayerState(context, stateSystem);
			godLayout = godHandPlayerState.createAutoGodLayout(9);
			progressBar = new ProgressBar(BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run));
			godLayout.setProgressBar(progressBar);
			isWin = false;
			isLose = false;
			businessman.setHreat(businessman.getHreat() + 1);
		}
		return businessman.onTouchEvent(event);
	}

	public void render() {
		// TODO Auto-generated method stub
	}
	
}
