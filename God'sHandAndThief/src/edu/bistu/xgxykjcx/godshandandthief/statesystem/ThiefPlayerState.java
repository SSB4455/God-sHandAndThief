package edu.bistu.xgxykjcx.godshandandthief.statesystem;


import edu.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import edu.bistu.xgxykjcx.godshandandthief.BluetoothChatService;
import edu.bistu.xgxykjcx.godshandandthief.GHTMainActivity;
import edu.bistu.xgxykjcx.godshandandthief.GHTSurfaceView;
import edu.bistu.xgxykjcx.godshandandthief.actor.Background;
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

public class ThiefPlayerState implements IGameObject {
	public final static int MODEL_BRAND_NEW = 0;
	public final static int MODEL_NORMAL = 1;
	
	//private Context context;
	private GHTMainActivity mainActivity;
	private StateSystem stateSystem;
	
	private PlayerType playerType;
	private boolean isLose, isWin;
	private GodLayout godLayout;
	private Background background;
	private Businessman businessman;
	
	private Bitmap isLoseBitmap, isWinBitmap;
	private Paint paint;
	
	
	
	public ThiefPlayerState(StateSystem stateSystem, GodLayout godLayout, PlayerType playerType) {
		//this.context = MainActivity.CONTEXT;
		mainActivity = (GHTMainActivity) GHTMainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		this.godLayout = godLayout;
		background = new Background();
		
		isLoseBitmap = BitmapStorage.getLose();
		isWinBitmap = BitmapStorage.getWin();
		
		if(playerType == PlayerType.PlayerWithBlueTooth)
			businessman = new Businessman(PlayerType.PlayerWithBlueTooth);
		else if(playerType == PlayerType.AutoWithBlueTooth)
			businessman = new Businessman(PlayerType.AutoWithBlueTooth);
		else
			businessman = new Businessman();
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
		if(playerType == PlayerType.PlayerWithBlueTooth || playerType == PlayerType.AutoWithBlueTooth) {
			if(((GHTMainActivity) GHTMainActivity.CONTEXT).getChatServiceState() != BluetoothChatService.STATE_CONNECTED)
				godLayout.getProgressBar().stop();

			if(((GHTMainActivity) GHTMainActivity.CONTEXT).getChatServiceState() == BluetoothChatService.STATE_CONNECTED 
					&& !isOver() && !godLayout.getProgressBar().isOver())
				godLayout.getProgressBar().start();
		}
		
		if(godLayout.getProgressBar().isPlay()) {
			background.update(elapsedTime);
			godLayout.update(elapsedTime);
			
			for(int i = 0; i < godLayout.getObstacleSize(); i++)
				if(businessman.isCollisionWith((Obstacle) godLayout.getObstacle(i))) {
					businessman.beInjured();
					if(GHTMainActivity.CAN_SENDMESSAGE)
						mainActivity.sendMessage(Businessman.IS_INJURED_STRING);
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
			canvas.drawBitmap(isWinBitmap, GHTSurfaceView.SCREEN_W / 5, GHTSurfaceView.SCREEN_H - isWinBitmap.getHeight(), paint);
		if(isLose)
			canvas.drawBitmap(isLoseBitmap, GHTSurfaceView.SCREEN_W / 5, GHTSurfaceView.SCREEN_H - isLoseBitmap.getHeight(), paint);
		
		if(playerType == PlayerType.PlayerWithBlueTooth) {
			canvas.drawText("�Է��豸��", 3, GHTSurfaceView.SCREEN_H - 7, paint);
			canvas.drawText(((GHTMainActivity) GHTMainActivity.CONTEXT).getConnectedDeviceName(), 63, GHTSurfaceView.SCREEN_H - 7, paint);
			
		}
	}
	
	public void reset(int model) {
		// �Ƚ����������� �ٸ�С͵��������ֹ�������������ж�ʧ�ܵ������ռӶ�����
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
					((GHTMainActivity) GHTMainActivity.CONTEXT).stopBluetooth();
				}
			}
		}
		
		return businessman.onTouchEvent(event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ����> back");
			stateSystem.changeState("MenuState");
			((GHTMainActivity) GHTMainActivity.CONTEXT).stopBluetooth();
		}
		return true;		//���ñ�������
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
	
	public void businessmanbeInjured() {
		businessman.beInjured();
	}
	
}
