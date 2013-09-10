package org.bistu.xgxykjcx.godshandandthief.statesystem;

import org.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import org.bistu.xgxykjcx.godshandandthief.MainActivity;
import org.bistu.xgxykjcx.godshandandthief.MainSurfaceView;
import org.bistu.xgxykjcx.godshandandthief.actor.Background;
import org.bistu.xgxykjcx.godshandandthief.actor.Businessman;
import org.bistu.xgxykjcx.godshandandthief.actor.GodLayout;
import org.bistu.xgxykjcx.godshandandthief.actor.obstacle.Obstacle;
import org.bistu.xgxykjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ThiefPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private PlayerType playerType;
	private boolean isLose, isWin;
	private GodLayout godLayout;
	private Background background;
	private Businessman businessman;
	
	private Bitmap isLoseBitmap, isWinBitmap;
	private Paint paint;
	
	
	
	public ThiefPlayerState(StateSystem stateSystem, PlayerType playerType) {
		this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		if(playerType == PlayerType.Player) {
			businessman = new Businessman(context);
		}
		if(playerType == PlayerType.Auto) {
			businessman = new Businessman(context, playerType);
		}
		
		background = new Background(context);
		businessman = new Businessman(context);
		
		isLoseBitmap = BitmapStorage.getLose();
		isWinBitmap = BitmapStorage.getWin();
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
		if(godLayout.getProgressBar().isPlay()) {
			background.update(elapsedTime);
			godLayout.update(elapsedTime);
			
			for(int i = 0; i < godLayout.getObstacleSize(); i++)
				if(businessman.isCollisionWith((Obstacle) godLayout.getObstacle(i))) {
					businessman.beInjured();
				}
			
			if(businessman.getHreat() < 1) {
				isLose = true;
				godLayout.getProgressBar().stop();
			}
			businessman.update(elapsedTime);
		} else if(!isLose && godLayout.getProgressBar().isOver()) {
			isWin = true;
			godLayout.getProgressBar().stop();
		}
		
		/*
		if(touchAction == MotionEvent.ACTION_UP) {
			if((isWin || isLose) && playerType == PlayerType.Player) {
				Log.i(this.getClass().toString(), "touched and in update to reset");
				reset();
			}
		}*/
		
	}
	
	public void render(Canvas canvas) {
		background.render(canvas);
		godLayout.render(canvas);
		businessman.render(canvas);
		if(isWin)
			canvas.drawBitmap(isWinBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isWinBitmap.getHeight(), paint);
		if(isLose)
			canvas.drawBitmap(isLoseBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isLoseBitmap.getHeight(), paint);
	}
	
	void setGodLayout(GodLayout godLayout) {
		
		this.godLayout = godLayout;
		if(playerType == PlayerType.Auto) {
			businessman.setGodLayout(godLayout);
		}
	}
	
	public void reset() {
		// 先将进度条归零 再给小偷加命，防止加了命有马上判断失败导致最终加多条命
		if(playerType == PlayerType.Player) {
			godLayout = GodLayout.createAutoGodLayout(9);
		}else {
			godLayout.clear();
			godLayout.zeroProgressBar();
		}
		isLose = false;
		isWin = false;
		businessman.setHreat(businessman.getHreat() + 1);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			if((isWin || isLose) && playerType == PlayerType.Player) {
				Log.i(this.getClass().toString(), "touched and in update to reset");
				reset();
			}
		}
		
		return businessman.onTouchEvent(event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}

	public void render() {
		// TODO Auto-generated method stub
	}
	
}
