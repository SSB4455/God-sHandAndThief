package org.bistu.xgxykjcx.godshandandthief.statesystem;

import org.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import org.bistu.xgxykjcx.godshandandthief.BluetoothChatService;
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
	public final static int MODEL_BRAND_NEW = 0;
	public final static int MODEL_NORMAL = 1;
	
	private Context context;
	private StateSystem stateSystem;
	
	private PlayerType playerType;
	private boolean isLose, isWin;
	private GodLayout godLayout;
	private Background background;
	private Businessman businessman;
	
	private Bitmap isLoseBitmap, isWinBitmap;
	private Paint paint;
	
	
	
	public ThiefPlayerState(StateSystem stateSystem, GodLayout godLayout, PlayerType playerType) {
		this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		this.godLayout = godLayout;
		background = new Background(context);
		
		isLoseBitmap = BitmapStorage.getLose();
		isWinBitmap = BitmapStorage.getWin();
		
		if(playerType == PlayerType.PlayerWithBlueTooth)
			businessman = new Businessman(PlayerType.PlayerWithBlueTooth);
		else
			businessman = new Businessman();
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
		if(playerType == PlayerType.PlayerWithBlueTooth || playerType == PlayerType.AutoWithBlueTooth) {
			if(((MainActivity) MainActivity.CONTEXT).getChatServiceState() != BluetoothChatService.STATE_CONNECTED)
				godLayout.getProgressBar().stop();

			if(((MainActivity) MainActivity.CONTEXT).getChatServiceState() == BluetoothChatService.STATE_CONNECTED 
					&& !isOver() && !godLayout.getProgressBar().isOver())
				godLayout.getProgressBar().start();
		}
		
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
		
	}
	
	public void render(Canvas canvas) {
		background.render(canvas);
		godLayout.render(canvas);
		businessman.render(canvas);
		if(isWin)
			canvas.drawBitmap(isWinBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isWinBitmap.getHeight(), paint);
		if(isLose)
			canvas.drawBitmap(isLoseBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isLoseBitmap.getHeight(), paint);
		
		if(playerType == PlayerType.PlayerWithBlueTooth) {
			canvas.drawText("对方设备：", 3, MainSurfaceView.SCREEN_H - 7, paint);
			canvas.drawText(((MainActivity) MainActivity.CONTEXT).getConnectedDeviceName(), 63, MainSurfaceView.SCREEN_H - 7, paint);
			
		}
	}
	
	public void reset(int model) {
		// 先将进度条归零 再给小偷加命，防止加了命有马上判断失败导致最终加多条命
		if(playerType == PlayerType.Player) {
			godLayout = GodLayout.createAutoGodLayout(9);
		}else {
			godLayout.clear();
			godLayout.zeroProgressBar();
		}
		isLose = false;
		isWin = false;
		if(model == MODEL_BRAND_NEW)
			businessman.setHreat(3);
		if(model == MODEL_NORMAL)
			businessman.setHreat(businessman.getHreat() + 1);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			if(isWin || isLose) {
				if(playerType == PlayerType.Player) {
					Log.i(this.getClass().toString(), "touched and in update to reset");
					reset(MODEL_NORMAL);
				}
				if(playerType == PlayerType.PlayerWithBlueTooth) {
					stateSystem.changeState("MenuState");
					((MainActivity) MainActivity.CONTEXT).stopBluetooth();
				}
			}
		}
		
		return businessman.onTouchEvent(event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
			((MainActivity) MainActivity.CONTEXT).stopBluetooth();
		}
		return true;		//不让别人做了
	}
	
	public void addObstacleByBluetooth(long distanceLong, int obstacleType) {
		godLayout.addObstacle(distanceLong, obstacleType);
	}
	
	boolean isOver() {
		if(isWin || isLose)
			return true;
		return false;
	}
	
	Businessman getThief() {
		return businessman;
	}
	
}
