package org.bistu.kjcx.godshandandthief.statesystem;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GodLayout;
import org.bistu.kjcx.godshandandthief.actor.ProgressBar;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle;
import org.bistu.kjcx.godshandandthief.statesystem.StateSystem.PlayerType;

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
	private PlayerType playerType;
	private GodLayout godLayout;
	private ProgressBar progressBar;
	private Background background;
	private Businessman businessman;
	
	private Bitmap isLoseBitmap, isWinBitmap;
	private Paint paint;
	
	
	
	public ThiefPlayerState(Context context, StateSystem stateSystem, PlayerType playerType) {
		this.context = context;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		if(playerType == PlayerType.Player) {
			businessman = new Businessman(context);
		}
		if(playerType == PlayerType.Auto) {
			businessman = new Businessman(context, playerType);
		}
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
		progressBar = new ProgressBar();
		this.godLayout = godLayout;
		this.godLayout.setProgressBar(progressBar);
	}
	

	
	public void reset() {
		isWin = false;
		isLose = false;
		progressBar = new ProgressBar();
		godLayout.setProgressBar(progressBar);
		businessman.setHreat(businessman.getHreat() + 1);
	}
	
	PlayerType getType() {
		return playerType;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if((isWin || isLose) && playerType == PlayerType.Player) {
			GodHandPlayerState godHandPlayerState = new GodHandPlayerState(context, stateSystem, PlayerType.Auto);
			godLayout = godHandPlayerState.createAutoGodLayout(9);
			reset();
		}
		return businessman.onTouchEvent(event);
	}

	public void render() {
		// TODO Auto-generated method stub
	}
	
}
