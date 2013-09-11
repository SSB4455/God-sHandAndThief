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
		
		if(playerType == PlayerType.Player 
				|| playerType == PlayerType.Auto) {
			businessman = new Businessman();
		}
		if(playerType == PlayerType.PlayerWithBlueTooth) {
			businessman = new Businessman();
			godLayout.getProgressBar().stop();
		}
		if(playerType == PlayerType.AutoWithBlueTooth) {
			businessman = new Businessman(PlayerType.AutoWithBlueTooth);
		}
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
		if(((MainActivity) MainActivity.CONTEXT).getBluetoothState() != BluetoothChatService.STATE_CONNECTED)
			godLayout.getProgressBar().stop();
		else
			godLayout.getProgressBar().start();
		
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
			canvas.drawText("对方设备：", 50, MainSurfaceView.SCREEN_H - 50, paint);
			canvas.drawText(((MainActivity) MainActivity.CONTEXT).getConnectedDeviceName(), 110, MainSurfaceView.SCREEN_H - 50, paint);
			
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

	Businessman getThief() {
		return businessman;
	}
	
}
