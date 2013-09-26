package org.bistu.xgxykjcx.godshandandthief.statesystem;

import org.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import org.bistu.xgxykjcx.godshandandthief.MainActivity;
import org.bistu.xgxykjcx.godshandandthief.MainSurfaceView;
import org.bistu.xgxykjcx.godshandandthief.actor.GodLayout;
import org.bistu.xgxykjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class ThiefChooseState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private final int X = 0, Y = 1;
	private float [][] menuLocation;
	private Bitmap [] menuButton;
	private Paint paint;
	
	
	
	public ThiefChooseState(StateSystem stateSystem) {
		this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		
		menuButton = new Bitmap[4];
		menuButton[0] = BitmapStorage.getImThiefComeOn();
		menuButton[1] = BitmapStorage.getComputerPursueMe();
		menuButton[2] = BitmapStorage.getGodPursueMe();
		menuButton[3] = BitmapStorage.getThiefHappy();
		
		menuLocation = new float[4][];
		menuLocation[0] = new float[2];
		menuLocation[0][X] = MainSurfaceView.SCREEN_W / 8;
		menuLocation[0][Y] = MainSurfaceView.SCREEN_H / 8;
		menuLocation[1] = new float[2];
		menuLocation[1][X] = MainSurfaceView.SCREEN_W / 3;
		menuLocation[1][Y] = MainSurfaceView.SCREEN_H / 2;
		menuLocation[2] = new float[2];
		menuLocation[2][X] = MainSurfaceView.SCREEN_W / 3;
		menuLocation[2][Y] = MainSurfaceView.SCREEN_H * 3 / 4;
		menuLocation[3] = new float[2];
		menuLocation[3][X] = 0;
		menuLocation[3][Y] = MainSurfaceView.SCREEN_H / 4;
		
		paint = new Paint();
		
	}
	
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		for(int i = 0; i < menuButton.length; i++) {
			canvas.drawBitmap(menuButton[i], menuLocation[i][X], menuLocation[i][Y], paint);
		}
		
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {		//防止到另一个State中产生Fling的崩溃
			for(int i = 0; i < menuLocation.length; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					if(i == 1) {
						ThiefPlayerState thiefPlayerState = new ThiefPlayerState(stateSystem, GodLayout.createAutoGodLayout(9), PlayerType.Player);
						stateSystem.addState("ThiefPlayerState", thiefPlayerState);
						stateSystem.changeState("ThiefPlayerState");
						Toast.makeText(context, "Comptuer pursue me...", Toast.LENGTH_SHORT).show();
					}
					if(i == 2) {
						// 启动用蓝牙连接的小偷状态
						ThiefPlayerState thiefPlayerState = new ThiefPlayerState(stateSystem, new GodLayout(), PlayerType.PlayerWithBlueTooth);
						// start bluetooth
						((MainActivity) MainActivity.CONTEXT).startBluetooth(MainActivity.THIEF, thiefPlayerState);
						stateSystem.addState("ThiefPlayerState", thiefPlayerState);
						stateSystem.changeState("ThiefPlayerState");
						Toast.makeText(context, "请允许开启蓝牙to链接上帝之手...", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
}
