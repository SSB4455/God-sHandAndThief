package org.bistu.xgxykjcx.godshandandthief.statesystem;

import org.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import org.bistu.xgxykjcx.godshandandthief.BluetoothChatService;
import org.bistu.xgxykjcx.godshandandthief.MainActivity;
import org.bistu.xgxykjcx.godshandandthief.MainSurfaceView;
import org.bistu.xgxykjcx.godshandandthief.actor.Businessman;
import org.bistu.xgxykjcx.godshandandthief.actor.GodLayout;
import org.bistu.xgxykjcx.godshandandthief.actor.obstacle.Obstacle;
import org.bistu.xgxykjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GodHandPlayerState implements IGameObject {
	//private Context context;
	private StateSystem stateSystem;
	
	private final int X = 0, Y = 1;
	
	private float intervalBrush;
	private boolean canSendMessage;
	
	private float [][] menuLocation;
	private Bitmap [] menuButton;
	
	private PlayerType playerType;
	private Businessman businessman;
	private GodLayout godLayout;
	
	private ThiefPlayerState thiefPlayerState;
	
	private Paint paint, brushPaint;
	
	
	
	public GodHandPlayerState(StateSystem stateSystem, PlayerType playerType) {
		//this.context = MainActivity.CONTEXT;
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
		menuLocation[0][X] = (MainSurfaceView.SCREEN_W * 5 / 8);
		menuLocation[0][Y] = (MainSurfaceView.SCREEN_H / 8);
		menuLocation[1] = new float[2];
		menuLocation[1][X] = (MainSurfaceView.SCREEN_W * 3 / 4);
		menuLocation[1][Y] = (MainSurfaceView.SCREEN_H / 6);
		
		paint = new Paint();
		brushPaint = new Paint();
		
	}
	
	public void update(long elapsedTime) {
		thiefPlayerState.update(elapsedTime);
		
		if(playerType == PlayerType.PlayerWithBlueTooth && 
				((MainActivity) MainActivity.CONTEXT).getChatServiceState() == BluetoothChatService.STATE_CONNECTED)
			canSendMessage = true;
		else
			canSendMessage = false;
		
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
		canvas.clipRect(0, MainSurfaceView.SCREEN_H / 2, MainSurfaceView.SCREEN_W, MainSurfaceView.SCREEN_H);
		canvas.scale(0.5f, 0.5f, 0, MainSurfaceView.SCREEN_H);
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
			canvas.drawText("对方设备：", 50, MainSurfaceView.SCREEN_H - 50, paint);
			canvas.drawText(((MainActivity) MainActivity.CONTEXT).getConnectedDeviceName(), 110, MainSurfaceView.SCREEN_H - 50, paint);
			
		}
		
	}
	
	void autoFling() {
		for(int i = 0; i < godLayout.getObstacleSize(); i++) {
			Obstacle obstacle = (Obstacle) godLayout.getObstacle(i);
			if(obstacle.getLeft() - 10 <= businessman.getRight() && obstacle.getRight() > businessman.getRight()) {
				switch(obstacle.getType()) {
				case Obstacle.HOLE :
					businessman.setFling(Businessman.DOWN);
					break;
				case Obstacle.STONE :
				case Obstacle.PIT :
					businessman.setFling(Businessman.UP);
				}
			}
		}
	}
	
	public void setFling(int direction) {
		businessman.setFling(direction);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(thiefPlayerState.isOver()) {
			if(playerType == PlayerType.Player) {
				// godLayout应清理一下
				thiefPlayerState.reset(ThiefPlayerState.MODEL_NORMAL);
			}
			if(playerType == PlayerType.PlayerWithBlueTooth) {
				stateSystem.changeState("MenuState");
				((MainActivity) MainActivity.CONTEXT).stopBluetooth();
			}
		} else if(event.getAction() == MotionEvent.ACTION_DOWN && intervalBrush >= GodLayout.INTERVAL_LONG) {
			for(int i = 0; i < menuButton.length; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					intervalBrush = 0;
					int obstacleType = -1;
					if(i == 0)
						obstacleType = Obstacle.HOLE;
					if(i == 1)
						obstacleType = Obstacle.PIT;
					if(canSendMessage)
						((MainActivity) MainActivity.CONTEXT).sendMessage(obstacleType + "");
					
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
			((MainActivity) MainActivity.CONTEXT).stopBluetooth();
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
