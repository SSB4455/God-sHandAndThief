package edu.bistu.xgxykjcx.godshandandthief.statesystem;


import edu.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import edu.bistu.xgxykjcx.godshandandthief.GHTMainActivity;
import edu.bistu.xgxykjcx.godshandandthief.GHTSurfaceView;
import edu.bistu.xgxykjcx.godshandandthief.actor.Businessman;
import edu.bistu.xgxykjcx.godshandandthief.actor.GodLayout;
import edu.bistu.xgxykjcx.godshandandthief.actor.obstacle.Obstacle;
import edu.bistu.xgxykjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GodHandPlayerState implements IGameObject {
	//private Context context;
	private GHTMainActivity mainActivity;
	private StateSystem stateSystem;
	
	private final int X = 0, Y = 1;
	
	private float intervalBrush;
	
	private float [][] menuLocation;
	private Bitmap [] menuButton;
	
	private PlayerType playerType;
	private Businessman businessman;
	private GodLayout godLayout;
	
	private ThiefPlayerState thiefPlayerState;
	
	private Paint paint, brushPaint;
	
	
	
	public GodHandPlayerState(StateSystem stateSystem, PlayerType playerType) {
		//this.context = MainActivity.CONTEXT;
		mainActivity = (GHTMainActivity) GHTMainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		
		intervalBrush = 0;
		godLayout = new GodLayout();
		if(playerType == PlayerType.Player) {
		thiefPlayerState = new ThiefPlayerState(stateSystem, godLayout, PlayerType.Auto);
		}
		if(playerType == PlayerType.PlayerWithBlueTooth) {
			thiefPlayerState = new ThiefPlayerState(stateSystem, godLayout, PlayerType.AutoWithBlueTooth);
		}
		businessman = thiefPlayerState.getThief();
		
		menuButton = new Bitmap[2];
		menuButton[0] = BitmapStorage.getHoleMenu();
		menuButton[1] = BitmapStorage.getPitMenu();
		
		menuLocation = new float[2][];
		menuLocation[0] = new float[2];
		menuLocation[0][X] = (GHTSurfaceView.SCREEN_W * 5 / 8);
		menuLocation[0][Y] = (GHTSurfaceView.SCREEN_H / 8);
		menuLocation[1] = new float[2];
		menuLocation[1][X] = (GHTSurfaceView.SCREEN_W * 3 / 4);
		menuLocation[1][Y] = (GHTSurfaceView.SCREEN_H / 6);
		
		paint = new Paint();
		brushPaint = new Paint();
	}
	
	public void update(long elapsedTime) {
		thiefPlayerState.update(elapsedTime);
		
		// 如果是单机 由上帝来给小偷操作指令
		if(playerType == PlayerType.Player) {
			autoFling();
		}
		
		if(playerType == PlayerType.PlayerWithBlueTooth) {
			
		}
		
		if(intervalBrush < GodLayout.INTERVAL_LONG)
			intervalBrush += elapsedTime;
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		canvas.save();		// 开始渲染小偷的画面
		canvas.clipRect(0, GHTSurfaceView.SCREEN_H / 2, GHTSurfaceView.SCREEN_W, GHTSurfaceView.SCREEN_H);
		canvas.scale(0.5f, 0.5f, 0, GHTSurfaceView.SCREEN_H);
		thiefPlayerState.render(canvas);
		canvas.restore();
		
		// 开始渲染障碍按钮
		for(int i = 0; i < menuButton.length; i++) {
			canvas.drawBitmap(menuButton[i], menuLocation[i][X], menuLocation[i][Y], paint);
		}
		// 开始渲染加载时间
		int brushAlpha = (int) (255 - 255*(intervalBrush/(float)GodLayout.INTERVAL_LONG));
		brushPaint.setAlpha(brushAlpha > 0 ? brushAlpha : 0);
		paint.setColor(Color.GREEN);
		for(int i = 0; i < menuButton.length && intervalBrush < GodLayout.INTERVAL_LONG; i++) {
			float brushY = menuLocation[i][Y] + menuButton[i].getHeight()*(1-intervalBrush/(float)GodLayout.INTERVAL_LONG);
			canvas.drawRect(menuLocation[i][X], menuLocation[i][Y], menuLocation[i][X] + menuButton[i].getWidth(), brushY, brushPaint);
			
			canvas.drawLine(menuLocation[i][X], brushY, menuLocation[i][X] + menuButton[i].getWidth(), brushY, paint);
		}
		
		if(playerType == PlayerType.PlayerWithBlueTooth) {
			canvas.drawText("对方设备：", 3, GHTSurfaceView.SCREEN_H - 7, paint);
			canvas.drawText(((GHTMainActivity) GHTMainActivity.CONTEXT).getConnectedDeviceName(), 63, GHTSurfaceView.SCREEN_H - 7, paint);
			
		}
		
	}
	
	void autoFling() {
		for(int i = 0; i < godLayout.getObstacleSize(); i++) {
			Obstacle obstacle = (Obstacle) godLayout.getObstacle(i);
			if(obstacle.getLeft() - 10 <= businessman.getRight() && obstacle.getRight() > businessman.getRight()) {
				switch(obstacle.getType()) {
				case Obstacle.HOLE :
					businessman.setFling(Businessman.DOWN_FLING);
					break;
				case Obstacle.STONE :
				case Obstacle.PIT :
					businessman.setFling(Businessman.UP_FLING);
				}
			}
		}
	}
	
	public void setFling(int direction) {
		businessman.setFling(direction);
	}
	
	public void businessmanbeInjured() {
		businessman.beInjured();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(thiefPlayerState.isOver()) {
			if(playerType == PlayerType.Player) {
				// godLayout应清理一下
				thiefPlayerState.reset(ThiefPlayerState.MODEL_NORMAL);
			}
			if(playerType == PlayerType.PlayerWithBlueTooth) {
				stateSystem.changeState("MenuState");
				((GHTMainActivity) GHTMainActivity.CONTEXT).stopBluetooth();
			}
		} else if(event.getAction() == MotionEvent.ACTION_DOWN && intervalBrush >= GodLayout.INTERVAL_LONG) {
			for(int i = 0; i < menuButton.length; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					intervalBrush = 0;
					int obstacleType = -1;
					String obstacleTypeString = null;
					if(i == 0) {
						obstacleType = Obstacle.HOLE;
						obstacleTypeString = Obstacle.HOLE_STRING;
					}
					if(i == 1) {
						obstacleType = Obstacle.PIT;
						obstacleTypeString = Obstacle.PIT_STRING;
					}
					if(GHTMainActivity.CAN_SENDMESSAGE)
						mainActivity.sendMessage(System.currentTimeMillis() % 10000000 + obstacleTypeString);
					
					godLayout.addObstacle(4000, obstacleType);
					//Toast.makeText(context, "add a " + (i == 0 ? "hole" : "pit"), Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.d(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
			mainActivity.stopBluetooth();
		}
		return true;		//不让别人做了
	}
	
	GodLayout getGodLayout() {
		return godLayout;
	}
	
	PlayerType getType() {
		return playerType;
	}

	public void reset(int model) {
		thiefPlayerState.reset(model);
		
	}
	
}
