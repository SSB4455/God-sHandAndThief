package org.bistu.kjcx.godshandandthief.statesystem;

import java.util.Random;

import org.bistu.kjcx.godshandandthief.BitmapStorage;
import org.bistu.kjcx.godshandandthief.MainActivity;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.GodLayout;
import org.bistu.kjcx.godshandandthief.actor.ProgressBar;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle.ObstacleType;
import org.bistu.kjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class GodHandPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private final int X = 0, Y = 1;
	
	private float intervalBrush;
	private float [][] menuLocation;
	private Bitmap [] menuButton;
	
	private PlayerType playerType;
	GodLayout godLayout;
	
	private ThiefPlayerState thiefPlayerState;
	
	//private Bitmap waitMoment;
	private Paint paint, brushPaint;
	
	
	
	public GodHandPlayerState(StateSystem stateSystem, PlayerType playerType) {
		this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		if(playerType == PlayerType.Player) {
			intervalBrush = 0;
			godLayout = new GodLayout();
			
			menuButton = new Bitmap[2];
			menuButton[0] = BitmapStorage.getHoleMenu();
			menuButton[1] = BitmapStorage.getPitMenu();
			
			menuLocation = new float[2][];
			menuLocation[0] = new float[2];
			menuLocation[0][X] = (MainSurfaceView.SCREEN_W * 5 / 8);
			menuLocation[0][Y] = (MainSurfaceView.SCREEN_H / 8);
			menuLocation[1] = new float[2];
			menuLocation[1][X] = (MainSurfaceView.SCREEN_W * 3 / 4);
			menuLocation[1][Y] = (MainSurfaceView.SCREEN_H / 6);
			
			paint = new Paint();
			brushPaint = new Paint();
		}
	}
	
	public void update(long elapsedTime) {
		thiefPlayerState.update(elapsedTime);
		
		if(intervalBrush < GodLayout.INTERVAL_LONG)
			intervalBrush += elapsedTime;
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		
		for(int i = 0; i < menuButton.length; i++) {
			canvas.drawBitmap(menuButton[i], menuLocation[i][X], menuLocation[i][Y], paint);
		}
		int brushAlpha = (int) (255 - 255*(intervalBrush/(float)GodLayout.INTERVAL_LONG));
		brushPaint.setAlpha(brushAlpha > 0 ? brushAlpha : 0);
		paint.setColor(Color.GREEN);
		for(int i = 0; i < menuButton.length && intervalBrush < GodLayout.INTERVAL_LONG; i++) {
			float brushY = menuLocation[i][Y] + menuButton[i].getHeight()*(1-intervalBrush/(float)GodLayout.INTERVAL_LONG);
			canvas.drawRect(menuLocation[i][X], menuLocation[i][Y], menuLocation[i][X] + menuButton[i].getWidth(), brushY, brushPaint);
			
			canvas.drawLine(menuLocation[i][X], brushY, menuLocation[i][X] + menuButton[i].getWidth(), brushY, paint);
		}
		
		canvas.save();		//渲染小偷的画面
		canvas.clipRect(0, MainSurfaceView.SCREEN_H / 2, MainSurfaceView.SCREEN_W, MainSurfaceView.SCREEN_H);
		canvas.scale(0.5f, 0.5f, 0, MainSurfaceView.SCREEN_H);
		thiefPlayerState.render(canvas);
		canvas.restore();
	}
	
	boolean setThiefPlayerState(ThiefPlayerState thiefPlayerState) {
		this.thiefPlayerState = thiefPlayerState;
		if(this.thiefPlayerState == thiefPlayerState)
			return true;
		else
			return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		if(!godLayout.getProgressBar().isPlay()) {
			thiefPlayerState.reset();
		} else if(event.getAction() == MotionEvent.ACTION_DOWN && intervalBrush >= GodLayout.INTERVAL_LONG) {
			for(int i = 0; i < menuButton.length; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					intervalBrush = 0;
					godLayout.addObstacle(4000, i == 0 ? ObstacleType.Hole : ObstacleType.Pit);
					//Toast.makeText(context, "add a " + (i == 0 ? "hole" : "pit"), Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.d(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
	GodLayout createAutoGodLayout(int level) {
		Random random = new Random();
		godLayout = new GodLayout();
		//godLayout.setProgressBar(new ProgressBar());
		level = level % 10;
		long interval = GodLayout.INTERVAL_LONG;		//至少间隔1秒
		long partLong = (ProgressBar.TOTAL_Long - 4000 - interval * level) / level;		//多减4s是为了给第一个障碍的间隔预留2s为最后一个障碍预留2s
		for(int i = 0; i < level; i++) {
			long position = 2000 + i * (partLong + interval) + random.nextInt((int) partLong);
			//第一个障碍多添加2s的准备时间
			godLayout.addObstacle(position, i % 2 == 0 ? ObstacleType.Pit : ObstacleType.Hole);
			Log.v(this.getClass().toString(), "create a obstacle, position = " + position + " type = " + (i % 2 == 0 ? ObstacleType.Pit + "" : ObstacleType.Hole + ""));
		}
		return godLayout;
	}
	
	GodLayout getGodLayout() {
		return godLayout;
	}
	
	PlayerType getType() {
		return playerType;
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
